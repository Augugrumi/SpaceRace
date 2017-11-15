package com.augugrumi.spacerace;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.augugrumi.spacerace.intro.IntroActivity;
import com.augugrumi.spacerace.listener.RoomListenerImpl;
import com.augugrumi.spacerace.utility.GoogleApiClientManager;
import com.augugrumi.spacerace.utility.SharedPreferencesManager;
import com.augugrumi.spacerace.utility.gameutility.BaseGameActivity;
import com.augugrumi.spacerace.utility.gameutility.BaseGameUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.invitation_popup) ViewGroup invitationPopUp;
    @BindView(R.id.incoming_invitation_text) TextView incomingInvitationText;

    private final RoomListenerImpl mRoomListenerImpl =
            new RoomListenerImpl(this, RC_WAITING_ROOM);

    final static int[] SCREENS = {
            //R.id.screen_main,
            R.id.screen_wait
    };

    // Request codes for the UIs that we show with startActivityForResult:
    private final static int RC_SELECT_PLAYERS = 10000;
    private final static int RC_INVITATION_INBOX = 10001;
    private final static int RC_WAITING_ROOM = 10002;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    private boolean alreadySignIn = false;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;

    // Are we playing in multiplayer mode?
    boolean mMultiplayer = false;

    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String mMyId = null;

    // If non-null, this is the id of the invitation we received via the
    // invitation listener
    String mIncomingInvitationId = null;

    // Message buffer for sending messages
    byte[] mMsgBuf = new byte[2];

    private ProgressDialog mProgressDialog;

    private GoogleApiClient mGoogleApiClient;

    private int mCurScreen = -1;

    private boolean backOnNewMatch = false;

//    @BindView(R.id.main_activity_bg)
//    ImageView activityBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mGoogleApiClient = GoogleApiClientManager.setUpGoogleApi(this,
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle connectionHint) {
                        Games.Invitations.registerInvitationListener(mGoogleApiClient,
                                mRoomListenerImpl);

                        if (connectionHint != null) {
                            Log.d("CONNECTION", "onConnected: connection hint provided. Checking for invite.");
                            Invitation inv = connectionHint
                                    .getParcelable(Multiplayer.EXTRA_INVITATION);
                            if (inv != null && inv.getInvitationId() != null) {
                                // retrieve and cache the invitation ID
                                Log.d("CONNECTION","onConnected: connection hint has a room invite!");
                                acceptInviteToRoom(inv.getInvitationId());
                                return;
                            }
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d("CONNECTION", "onConnectionSuspended() called. Trying to reconnect.");
                        mGoogleApiClient.connect();
                    }
                }, this);

        SpaceRace.setgAPIClient(mGoogleApiClient);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onStop() {
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, mRoomListenerImpl, mRoomId);
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onStop();
    }

    // Accept the given invitation.
    void acceptInviteToRoom(String invId) {
        // accept the invitation
        Log.d("ROOM", "Accepting invitation: " + invId);
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(mRoomListenerImpl);
        roomConfigBuilder.setInvitationIdToAccept(invId)
                .setMessageReceivedListener(mRoomListenerImpl)
                .setRoomStatusUpdateListener(mRoomListenerImpl);
        showPopUpNotification(false, "");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());
    }

    @Override
    public void onStart() {
        super.onStart();
        alreadySignIn = mGoogleApiClient.isConnected();
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
                    mRoomListenerImpl);


        if (!mGoogleApiClient.isConnected()) {
            Log.d("INTRO","Connecting client.");
            mGoogleApiClient.connect();
        } else {
            Games.Invitations.registerInvitationListener(mGoogleApiClient,
                    mRoomListenerImpl);
            Log.w("INTRO",
                    "GameHelper: client was already connected on onStart()");
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();

        if (!SharedPreferencesManager.getFirstApplicationRun()) {

            Log.d("INTRO", "First run detected, launching sliders...");

            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);

            SharedPreferencesManager.setFirstApplicationRun(true);
        }
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
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == RC_SELECT_PLAYERS) {
            if (resultCode != Activity.RESULT_OK) {
                // user canceled
                return;
            }

            // get the invitee list
            Bundle extras = data.getExtras();
            final ArrayList<String> invitees =
                    data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            // get auto-match criteria
            Bundle autoMatchCriteria = null;
            int minAutoMatchPlayers =
                    data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers =
                    data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

            if (minAutoMatchPlayers > 0) {
                autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                        minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            } else {
                autoMatchCriteria = null;
            }

            // create the room and specify a variant if appropriate
            RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(mRoomListenerImpl)
                    .setMessageReceivedListener(mRoomListenerImpl)
                    .setRoomStatusUpdateListener(mRoomListenerImpl);
            roomConfigBuilder.addPlayersToInvite(invitees);
            if (autoMatchCriteria != null) {
                roomConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
            }
            RoomConfig roomConfig = roomConfigBuilder.build();
            Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);

            // prevent screen from sleeping during handshake
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    private void handleSignInResult(@NonNull GoogleSignInResult result) {
        Log.d("INTRO", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mGoogleApiClient.connect();
            Log.i("INTRO", acct.getDisplayName());
        } else {
            Log.i("INTRO", "account not connected");
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    //FIXME set mGoogleApiClient on application

    public void setIncomingInvitationId(String incomingInvitationId) {
        mIncomingInvitationId = incomingInvitationId;
    }

    public void showPopUpNotification(boolean showInvPopup, String message) {
        incomingInvitationText.setText(message + " " + getString(R.string.is_inviting_you));
        invitationPopUp.setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.button_accept_popup_invitation)
    public void onClickAcceptPopUpInvitation(View view) {
        //FIXME ?????
        acceptInviteToRoom(mIncomingInvitationId);
        mIncomingInvitationId = null;
    }

    @OnClick(R.id.new_match)
    public void onClickNewMatch(View view) {
        //FIXME ?????
        backOnNewMatch = true;
        showPopUpNotification(false, "");
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 1);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    @OnClick(R.id.join)
    public void onClickJoin(View view) {
        //FIXME ?????
        findViewById(R.id.invitation_popup).setVisibility(View.GONE);
        backOnNewMatch = true;
        showPopUpNotification(false, "");
        Intent intent = Games.Invitations.getInvitationInboxIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_INVITATION_INBOX);
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

}
