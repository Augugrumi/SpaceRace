package com.augugrumi.spacerace;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.augugrumi.spacerace.intro.IntroActivity;
import com.augugrumi.spacerace.utility.SharedPreferencesManager;
import com.augugrumi.spacerace.utility.gameutility.BaseGameUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.invitation_popup) ViewGroup invitationPopUp;
    @BindView(R.id.incoming_invitation_text) TextView incomingInvitationText;


    /**************************************************************************/
    // Request codes for the UIs that we show with startActivityForResult:
    private final static int RC_SELECT_PLAYERS = 10000;
    private final static int RC_INVITATION_INBOX = 10001;
    private final static int RC_WAITING_ROOM = 10002;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    // Client used to interact with the real time multiplayer system.
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;
    private GoogleSignInAccount mSignedInAccount = null;
    private InvitationsClient mInvitationsClient = null;
    private String mPlayerId;
    private String mRoomId = null;
    private RoomConfig mRoomConfig;
    private ArrayList<Participant> mParticipants = null;
    private String mIncomingInvitationId = null;
    // My participant ID in the currently active game
    private String mMyId = null;

    private InvitationCallback mInvitationCallback = new InvitationCallback() {
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

            if (mIncomingInvitationId.equals(invitationId) && mIncomingInvitationId != null) {
                mIncomingInvitationId = null;
                showPopUpNotification(false, ""); // This will hide the invitation popup
            }
        }
    };

    private final RoomUpdateCallbackImpl mRoomUpdateCallbackImpl = new RoomUpdateCallbackImpl();
    private final RoomStatusUpdateCallbackImp mRoomStatusUpdateCallback = new RoomStatusUpdateCallbackImp();


    OnRealTimeMessageReceivedListener mOnRealTimeMessageReceivedListener = new OnRealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            byte[] buf = realTimeMessage.getMessageData();
            String sender = realTimeMessage.getSenderParticipantId();
        }
    };
    /**************************************************************************/

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;


    private ProgressDialog mProgressDialog;

    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

        if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
            Log.w("SIGNIN", "*** Warning: setup problems detected. Sign in may not work!");
        }

        // start the sign-in flow
        Log.d("SIGNIN", "Sign-in button clicked");
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();

        /*if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
            Log.w("SIGNIN", "*** Warning: setup problems detected. Sign in may not work!");
        }

        // start the sign-in flow
        Log.d("SIGNIN", "Sign-in button clicked");
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);*/


        if (!SharedPreferencesManager.getFirstApplicationRun()) {

            Log.d("INTRO", "First run detected, launching sliders...");

            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);

            SharedPreferencesManager.setFirstApplicationRun(true);
        } else {
            if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
                Log.w("SIGNIN", "*** Warning: setup problems detected. Sign in may not work!");
            }
            Log.d("SIGNIN", "Sign-in silently");
            signInSilently();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregister our listeners.  They will be re-registered via onResume->signInSilently->onConnected.
        if (mInvitationsClient != null) {
            mInvitationsClient.unregisterInvitationCallback(mInvitationCallback);
        }
    }

    @Override
    public void onStop() {
        Log.d("STOP", "**** got onStop");

        // if we're in a room, leave it.
        leaveRoom();

        // stop trying to keep the screen on
        stopKeepingScreenOn();
        super.onStop();
    }

    // Accept the given invitation.
    void acceptInviteToRoom(String invitationId) {
        Log.d("ROOM", "Accepting invitation: " + invitationId);

        mRoomConfig = RoomConfig.builder(mRoomUpdateCallbackImpl)
                .setInvitationIdToAccept(invitationId)
                .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .build();

        keepScreenOn();

        mRealTimeMultiplayerClient.join(mRoomConfig)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ROOM", "Room Joined Successfully!");
                    }
                });
    }

    /*@Override
    public void onStart() {
        super.onStart();
        if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
            Log.w("SIGNIN", "*** Warning: setup problems detected. Sign in may not work!");
        }

        // start the sign-in flow
        Log.d("SIGNIN", "Sign-in button clicked");
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }/*    alreadySignIn = mGoogleApiClient.isConnected();
        if (! alreadySignIn) {

            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d("INTRO", "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
            alreadySignIn = true;
        } else
            Games.Invitations.registerInvitationListener(mGoogleApiClient,
                    mRoomUpdateCallbackImpl);


        if (!mGoogleApiClient.isConnected()) {
            Log.d("INTRO","Connecting client.");
            mGoogleApiClient.connect();
        } else {
            Games.Invitations.registerInvitationListener(mGoogleApiClient,
                    mRoomUpdateCallbackImpl);
            Log.w("INTRO",
                    "GameHelper: client was already connected on onStart()");
        }
    }*/




    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d("SIGNIN", "onConnected(): connected to Google APIs");
        if (mSignedInAccount != googleSignInAccount) {

            mSignedInAccount = googleSignInAccount;

            // update the clients
            mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this, googleSignInAccount);
            mInvitationsClient = Games.getInvitationsClient(MainActivity.this, googleSignInAccount);

            // get the playerId from the PlayersClient
            PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
            playersClient.getCurrentPlayer().addOnSuccessListener(
                    new OnSuccessListener<Player>() {
                        @Override
                        public void onSuccess(Player player) {
                            mPlayerId = player.getPlayerId();
                        }
                    }
            );
        }

        // register listener so we are notified if we receive an invitation to play
        // while we are in the game
        mInvitationsClient.registerInvitationCallback(mInvitationCallback);

        // get the invitation from the connection hint
        // Retrieve the TurnBasedMatch from the connectionHint
        GamesClient gamesClient = Games.getGamesClient(MainActivity.this, googleSignInAccount);
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

    private OnFailureListener createFailureListener(final String string) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FAILURE", "onDisconnected()" + e.toString());
            }
        };
    }

    public void onDisconnected() {
        Log.d("SIGNIN", "onDisconnected()");

        mRealTimeMultiplayerClient = null;
        mInvitationsClient = null;
    }

    public void signInSilently() {
        Log.d("SIGNIN", "signInSilently()");

        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d("SIGNIN", "signInSilently(): success");
                            onConnected(task.getResult());
                        } else {
                            Log.d("SIGNIN", "signInSilently(): failure", task.getException());
                            onDisconnected();
                        }
                    }
                });
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

    void startGame() {
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                }

                onDisconnected();

                new AlertDialog.Builder(this)
                        .setMessage(message + apiException.toString())
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        } else if (requestCode == RC_SELECT_PLAYERS) {
            // we got the result from the "select players" UI -- ready to create the room
            handleSelectPlayersResult(resultCode, intent);

        } else if (requestCode == RC_INVITATION_INBOX) {
            // we got the result from the "select invitation" UI (invitation inbox). We're
            // ready to accept the selected invitation:
            handleInvitationInboxResult(resultCode, intent);

        } else if (requestCode == RC_WAITING_ROOM) {
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
        }
        super.onActivityResult(requestCode, resultCode, intent);

    }

    private void handleSelectPlayersResult(int response, Intent data) {
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
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d("SELECT_PLAYERS_RESULT", "Automatch criteria: " + autoMatchCriteria);
        }

        // create the room
        Log.d("SELECT_PLAYERS_RESULT", "Creating room...");
        keepScreenOn();

        mRoomConfig = RoomConfig.builder(mRoomUpdateCallbackImpl)
                .addPlayersToInvite(invitees)
                .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();
        mRealTimeMultiplayerClient.create(mRoomConfig);
        Log.d("SELECT_PLAYERS_RESULT", "Room created, waiting for it to be ready...");
    }

    private void handleInvitationInboxResult(int response, Intent data) {
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

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void showPopUpNotification(boolean showInvPopup, String message) {
        incomingInvitationText.setText(message + " " + getString(R.string.is_inviting_you));
        invitationPopUp.setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.button_accept_popup_invitation)
    public void onClickAcceptPopUpInvitation(View view) {
        acceptInviteToRoom(mIncomingInvitationId);
        mIncomingInvitationId = null;
    }

    @OnClick(R.id.new_match)
    public void onClickNewMatch(View view) {
        showPopUpNotification(false, "");
        mRealTimeMultiplayerClient.getSelectOpponentsIntent(1, 3).addOnSuccessListener(
                new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_SELECT_PLAYERS);
                    }
                }
        ).addOnFailureListener(createFailureListener("There was a problem selecting opponents."));
    }

    @OnClick(R.id.join)
    public void onClickJoin(View view) {
        acceptInviteToRoom(mIncomingInvitationId);
        showPopUpNotification(false, "");
    }

    @OnClick(R.id.credits)
    public void onClickCredits(View view) {
        Intent i = new Intent(MainActivity.this, CreditsActivity.class);
        startActivity(i);
    }

    //DEBUG
    @OnClick(R.id.debug_btn)
    public void onClickSeeMap(View view) {
        Intent i = new Intent(MainActivity.this, MapActivity.class);
        startActivity(i);
    }

    private class RoomUpdateCallbackImpl extends RoomUpdateCallback {

        @Override
        public void onRoomCreated(int statusCode, Room room) {
            Log.d("ROOM", "onRoomCreated(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.e("ROOM", "*** Error: onRoomCreated, status " + statusCode);
                showGameError();
                return;
            }

            // save room ID so we can leave cleanly before the game starts.
            mRoomId = room.getRoomId();

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

            // show the waiting room UI
            showWaitingRoom(room);
        }

        @Override
        public void onLeftRoom(int i, String s) {Log.i("ROOM", "on room joined");}

        @Override
        public void onRoomConnected(int statusCode, Room room) {
            Log.i("ROOM", "on room connected");
            if (statusCode != GamesStatusCodes.STATUS_OK) {
                BaseGameUtils.makeSimpleDialog(MainActivity.this,
                        SpaceRace.getAppContext().getString(R.string.game_problem));
            } else {
                updateRoom(room);
            }
            Intent i = new Intent(MainActivity.this, MapActivity.class);
            MainActivity.this.startActivity(i);
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

    private void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
        }
    }

    void showGameError() {
        BaseGameUtils.makeSimpleDialog(MainActivity.this, SpaceRace.getAppContext()
                .getString(R.string.game_problem));
        MainActivity.this.showPopUpNotification(false, "");
    }

    private class RoomStatusUpdateCallbackImp extends RoomStatusUpdateCallback {
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

    // Sets the flag to keep this screen on. It's recommended to do that during
    // the
    // handshake when setting up a game, because if the screen turns off, the
    // game will be
    // cancelled.
    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    void leaveRoom() {
        Log.d("ROOM", "Leaving room.");
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
        }
    }
}
