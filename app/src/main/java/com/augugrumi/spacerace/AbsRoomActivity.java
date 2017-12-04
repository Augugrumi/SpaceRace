/**
* Copyright 2017 Davide Polonio <poloniodavide@gmail.com>, Federico Tavella
* <fede.fox16@gmail.com> and Marco Zanella <zanna0150@gmail.com>
* 
* This file is part of SpaceRace.
* 
* SpaceRace is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SpaceRace is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with SpaceRace.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.augugrumi.spacerace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.augugrumi.spacerace.utility.gameutility.BaseGameUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.InvitationsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationCallback;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static com.augugrumi.spacerace.utility.Costants.MAX_NUMBER_OF_PLAYERS;
import static com.augugrumi.spacerace.utility.Costants.MIN_NUMBER_OF_PLAYERS;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 02/12/17
 */

public abstract class AbsRoomActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    // Request codes for the UIs that we show with startActivityForResult:
    protected final static int RC_SELECT_PLAYERS = 10000;
    protected final static int RC_INVITATION_INBOX = 10001;
    private final static int RC_WAITING_ROOM = 10002;
    protected static final int RC_LEADERBOARD_UI = 9004;
    // Request code used to invoke sign in user interactions.
    protected static final int RC_SIGN_IN = 9001;

    public static final String CREATOR_INTENT_EXTRA = "isCreator";

    // Client used to interact with the real time multiplayer system.
    protected RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;
    protected GoogleSignInAccount mSignedInAccount = null;
    protected InvitationsClient mInvitationsClient = null;
    private String mPlayerId;
    private String mRoomId = null;
    private RoomConfig mRoomConfig;
    private List<Participant> mParticipants;
    protected String mIncomingInvitationId = null;
    // My participant ID in the currently active game
    private String mMyId = null;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;

    protected final RoomUpdateCallbackImpl mRoomUpdateCallbackImpl = new RoomUpdateCallbackImpl();
    protected final RoomStatusUpdateCallbackImp mRoomStatusUpdateCallback = new RoomStatusUpdateCallbackImp();
    protected InvitationCallback mInvitationCallback = new InvitationCallback() {
        // Called when we get an invitation to play a game. We react by showing that to the user.
        @Override
        public void onInvitationReceived(@NonNull Invitation invitation) {
            // We got an invitation to play a game! So, store it in
            // mIncomingInvitationId
            // and show the popup on the screen.
            mIncomingInvitationId = invitation.getInvitationId();
            showPopUpNotification(true,
                    invitation.getInviter().getDisplayName());
        }

        @Override
        public void onInvitationRemoved(@NonNull String invitationId) {

            if (mIncomingInvitationId != null && mIncomingInvitationId.equals(invitationId)) {
                mIncomingInvitationId = null;
                showPopUpNotification(false, ""); // This will hide the invitation popup
            }
        }
    };

    protected boolean creator = false;

    protected void acceptInviteToRoom(String invitationId) {
        Log.d("ROOM", "Accepting invitation: " + invitationId);

        creator = false;

        mRoomConfig = RoomConfig.builder(mRoomUpdateCallbackImpl)
                .setInvitationIdToAccept(invitationId)
                .setOnMessageReceivedListener(SpaceRace.messageManager)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .build();

        SpaceRace.messageManager.setmRoomConfig(mRoomConfig);

        mRealTimeMultiplayerClient.join(mRoomConfig)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ROOM", "Room Joined Successfully!");
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task =
                        GoogleSignIn.getSignedInAccountFromIntent(intent);

                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    onConnected(account);
                } catch (ApiException apiException) {
                    String message = apiException.getMessage();
                    message = getString(R.string.signin_other_error) + " " + "(" + message + ")";

                    onDisconnected();

                    new AlertDialog.Builder(this)
                            .setTitle(R.string.error_during_signin)
                            .setMessage(message + apiException.toString())
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                }
                break;
            case RC_SELECT_PLAYERS:
                // we got the result from the "select players" UI -- ready to create the room
                handleSelectPlayersResult(resultCode, intent);
                break;
            case RC_INVITATION_INBOX:
                // we got the result from the "select invitation" UI (invitation inbox). We're
                // ready to accept the selected invitation:
                handleInvitationInboxResult(resultCode, intent);
                break;
            case RC_WAITING_ROOM:
                // we got the result from the "waiting room" UI.
                if (resultCode == Activity.RESULT_OK) {
                    // ready to start playing
                    Log.d("ROOM", "Starting game (waiting room returned OK).");
                    startGame();
                } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player indicated that they want to leave the room

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // Dialog was cancelled (user pressed back key, for instance). In our game,
                    // this means leaving the room too. In more elaborate games, this could mean
                    // something else (like minimizing the waiting room UI).

                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);

    }

    protected void handleSelectPlayersResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w("SELECT_PLAYERS_RESULT", "*** select players UI cancelled, " + response);
            return;
        }

        Log.d("SELECT_PLAYERS_RESULT", "Select players UI succeeded.");

        // get the invitee list
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.d("SELECT_PLAYERS_RESULT", "Invitee count: " + invitees.size());

        // get the automatch criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, MIN_NUMBER_OF_PLAYERS);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, MAX_NUMBER_OF_PLAYERS);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d("SELECT_PLAYERS_RESULT", "Automatch criteria: " + autoMatchCriteria);
        }

        // create the room
        Log.d("SELECT_PLAYERS_RESULT", "Creating room...");

        mRoomConfig = RoomConfig.builder(mRoomUpdateCallbackImpl)
                .addPlayersToInvite(invitees)
                .setOnMessageReceivedListener(SpaceRace.messageManager)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();
        mRealTimeMultiplayerClient.create(mRoomConfig);
        Log.d("SELECT_PLAYERS_RESULT", "Room created, waiting for it to be ready...");
    }

    protected void handleInvitationInboxResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w("INVITATION INBOX", "*** invitation inbox UI cancelled, " + response);
            return;
        }

        Log.d("INVITATION INBOX", "Invitation inbox UI succeeded.");
        Invitation invitation = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);

        // accept invitation
        if (invitation != null) {
            acceptInviteToRoom(invitation.getInvitationId());
        }
    }

    protected void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d("SIGNIN", "onConnected(): connected to Google APIs");
        if (mSignedInAccount != googleSignInAccount) {

            mSignedInAccount = googleSignInAccount;

            // update the clients
            mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this, googleSignInAccount);
            SpaceRace.messageManager.setRealTimeMultiplayerClient(mRealTimeMultiplayerClient);
            mInvitationsClient = Games.getInvitationsClient(this, googleSignInAccount);

            // get the playerId from the PlayersClient
            PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
            playersClient.getCurrentPlayer().addOnSuccessListener(
                    new OnSuccessListener<Player>() {
                        @Override
                        public void onSuccess(Player player) {
                            mPlayerId = player.getPlayerId();
                            SpaceRace.messageManager.setParticipantId(mPlayerId);
                        }
                    }
            );

            requestPermissionsNeeded();
        }

        // register listener so we are notified if we receive an invitation to play
        // while we are in the game
        mInvitationsClient.registerInvitationCallback(mInvitationCallback);

        // get the invitation from the connection hint
        // Retrieve the TurnBasedMatch from the connectionHint
        GamesClient gamesClient = Games.getGamesClient(this, googleSignInAccount);
        gamesClient.getActivationHint()
                .addOnSuccessListener(new OnSuccessListener<Bundle>() {
                    @Override
                    public void onSuccess(Bundle hint) {
                        if (hint != null) {
                            Invitation invitation =
                                    hint.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);

                            if (invitation != null && invitation.getInvitationId() != null) {
                                // retrieve and cache the invitation ID
                                Log.d("ROOM", "onConnected: connection hint has a room invite!");
                                acceptInviteToRoom(invitation.getInvitationId());
                            }
                        }
                    }
                })
                .addOnFailureListener(createFailureListener("There was a problem getting the activation hint!"));
    }

    protected abstract void requestPermissionsNeeded();

    protected OnFailureListener createFailureListener(final String string) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FAILURE", "onDisconnected()" + e.toString());
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregister our listeners.  They will be re-registered via onResume->signInSilently->onConnected.
        if (mInvitationsClient != null) {
            mInvitationsClient.unregisterInvitationCallback(mInvitationCallback);
        }
    }

    public void onDisconnected() {
        Log.d("SIGNIN", "onDisconnected()");

        mRealTimeMultiplayerClient = null;
        mInvitationsClient = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("INTRO", "onConnectionFailed:" + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d("CONNECTION", "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this,
                    SpaceRace.getgAPIClient(),
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }
    }

    protected void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
        }
    }

    protected abstract void startGame();
    protected abstract void showGameError();
    protected abstract void showPopUpNotification(boolean visible, String mex);

    protected class RoomUpdateCallbackImpl extends RoomUpdateCallback {

        @Override
        public void onRoomCreated(int statusCode, Room room) {
            Log.d("ROOM", "onRoomCreated(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.e("ROOM", "*** Error: onRoomCreated, status " + statusCode);
                showGameError();
                return;
            }

            creator = true;

            // save room ID so we can leave cleanly before the game starts.
            mRoomId = room.getRoomId();
            SpaceRace.messageManager.setRoom(room);

            // show the waiting room UI
            showWaitingRoom(room);
        }

        @Override
        public void onJoinedRoom(int statusCode, Room room) {
            Log.d("ROOM", "onJoinedRoom(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.e("ROOM", "*** Error: onRoomConnected, status " + statusCode);
                showGameError();
                return;
            }
            SpaceRace.messageManager.setRoom(room);
            // show the waiting room UI
            showWaitingRoom(room);
        }

        @Override
        public void onLeftRoom(int i, String s) {Log.i("ROOM", "on room joined");}

        @Override
        public void onRoomConnected(int statusCode, Room room) {
            Log.i("ROOM", "on room connected");
            if (statusCode != GamesStatusCodes.STATUS_OK) {
                BaseGameUtils.makeSimpleDialog(AbsRoomActivity.this,
                        SpaceRace.getAppContext().getString(R.string.game_problem));
            } else {
                updateRoom(room);
            }
            Intent i = new Intent(AbsRoomActivity.this, MultiPlayerActivity.class);
            i.putExtra(CREATOR_INTENT_EXTRA, creator);
            AbsRoomActivity.this.startActivity(i);
        }

        void showWaitingRoom(Room room) {
            // minimum number of players required for our game
            // For simplicity, we require everyone to join the game before we start it
            // (this is signaled by Integer.MAX_VALUE).

            final int MIN_PLAYERS = Integer.MAX_VALUE;
            mRealTimeMultiplayerClient.getWaitingRoomIntent(room, MIN_PLAYERS)
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            // show waiting room UI
                            startActivityForResult(intent, RC_WAITING_ROOM);
                        }
                    })
                    .addOnFailureListener(createFailureListener("There was a problem getting the waiting room!"));
        }

    }

    protected class RoomStatusUpdateCallbackImp extends RoomStatusUpdateCallback {
        @Override
        public void onConnectedToRoom(Room room) {
            Log.d("ROOM", "onConnectedToRoom.");

            //get participants and my ID:
            mParticipants = room.getParticipants();
            mMyId = room.getParticipantId(mPlayerId);

            // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
            if (mRoomId == null) {
                mRoomId = room.getRoomId();
            }

            // print out the list of participants (for debug purposes)
            Log.d("ROOM", "Room ID: " + mRoomId);
            Log.d("ROOM", "My ID " + mMyId);
            Log.d("ROOM", "<< CONNECTED TO ROOM>>");
        }

        // Called when we get disconnected from the room. We return to the main screen.
        @Override
        public void onDisconnectedFromRoom(Room room) {
            mRoomId = null;
            mRoomConfig = null;
            showGameError();
        }


        // We treat most of the room update callbacks in the same way: we update our list of
        // participants and update the display. In a real game we would also have to check if that
        // change requires some action like removing the corresponding player avatar from the screen,
        // etc.
        @Override
        public void onPeerDeclined(Room room, @NonNull List<String> arg1) {
            Log.i("ROOM", "on peer declined");
            updateRoom(room);
        }

        public void onPeerInvitedToRoom(Room room, List<String> list) {
            Log.i("ROOM", "on peer invited to room");
            updateRoom(room);
        }

        @Override
        public void onPeerJoined(Room room, List<String> list) {
            Log.i("ROOM", "on peer joined");
            updateRoom(room);
        }

        @Override
        public void onPeerLeft(Room room, List<String> list) {
            Log.i("ROOM", "on peer left");
            updateRoom(room);
        }

        @Override
        public void onPeersConnected(Room room, List<String> list) {
            Log.i("ROOM", "on peers connected");
            updateRoom(room);
        }

        @Override
        public void onPeersDisconnected(Room room, List<String> list) {
            Log.i("ROOM", "on peers disconnected");
            updateRoom(room);
        }

        @Override
        public void onP2PConnected(String s) {Log.i("ROOM", "on P2P connected");}

        @Override
        public void onP2PDisconnected(String s) {Log.i("ROOM", "on P2P disconnected");}

        @Override
        public void onRoomAutoMatching(Room room) {
            Log.i("ROOM", "on room auto matching");
            updateRoom(room);
        }

        @Override
        public void onRoomConnecting(Room room) {
            Log.i("ROOM", "on room connecting");
            updateRoom(room);
        }

    }

    //TODO put at the end on the match or when exit the game
    protected void leaveRoom() {
        /*Log.d("ROOM", "Leaving room.");
        stopKeepingScreenOn();
        if (mRoomId != null) {
            mRealTimeMultiplayerClient.leave(mRoomConfig, mRoomId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mRoomId = null;
                            mRoomConfig = null;
                        }
                    });
        }*/
    }
}
