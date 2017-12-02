package com.augugrumi.spacerace;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.augugrumi.spacerace.listener.NetworkChangeListener;
import com.augugrumi.spacerace.utility.LanguageManager;
import com.augugrumi.spacerace.utility.NetworkingUtility;
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

import static com.augugrumi.spacerace.utility.Costants.MAX_NUMBER_OF_PLAYERS;
import static com.augugrumi.spacerace.utility.Costants.MIN_NUMBER_OF_PLAYERS;

public class MainActivity extends AbsRoomActivity implements NetworkChangeListener {

    private static final int RC_PERMISSION_GRANTED = 15151;
    public static final int [] toEnable = {
            R.id.new_match,
            R.id.join,
            R.id.sigleplayer_btn,
            R.id.button_accept_popup_invitation
    };

    @BindView(R.id.invitation_popup) ViewGroup invitationPopUp;
    @BindView(R.id.incoming_invitation_text) TextView incomingInvitationText;

    private AlertDialog noPermissionDialog;

    private boolean invitationPopupIsShowing;

    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        NetworkingUtility.registerListener(this);
        disablePlayButtons();

        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

        if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
            Log.w("SIGNIN", "*** Warning: setup problems detected. Sign in may not work!");
        }

        // start the sign-in flow
        Log.d("SIGNIN", "Sign-in button clicked");
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);

        LanguageManager.languageManagement(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
            Log.w("SIGNIN", "*** Warning: setup problems detected. Sign in may not work!");
        }
        Log.d("SIGNIN", "Sign-in silently");

        if (!NetworkingUtility.isNetworkAvailable() ||
                GoogleSignIn.getLastSignedInAccount(SpaceRace.getAppContext()) == null ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED)
            disablePlayButtons();


        signInSilently();
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


    public void signInSilently() {
        Log.d("SIGNIN", "signInSilently()");

        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d("SIGNIN", "signInSilently(): success");
                            onConnected(task.getResult());
                            enablePlayButtons();
                        } else {
                            Log.d("SIGNIN", "signInSilently(): failure", task.getException());

                            onDisconnected();
                        }
                    }
                });
    }



    @Override
    protected void startGame() {
        Intent i = new Intent(this, MultiPlayerActivity.class);
        i.putExtra(CREATOR_INTENT_EXTRA, creator);
        startActivity(i);
    }


    @Override
    protected void showPopUpNotification(boolean showInvPopup, String message) {
        invitationPopupIsShowing = showInvPopup;

        StringBuilder toPrint = new StringBuilder()
                .append(getString(R.string.preamble_invitation))
                .append(" ")
                .append(message)
                .append(", ")
                .append(getString(R.string.is_inviting_you));

        incomingInvitationText.setText(toPrint.toString());
        invitationPopUp.setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.button_accept_popup_invitation)
    public void onClickAcceptPopUpInvitation(View view) {
        acceptInviteToRoom(mIncomingInvitationId);
        mIncomingInvitationId = null;
        showPopUpNotification(false, "");
    }

    @OnClick(R.id.button_decline_popup_invitation)
    public void onClickDeclinePopUpInvitation(View view) {
        MainActivity.this.showPopUpNotification(false, "");
    }

    @OnClick(R.id.new_match)
    public void onClickNewMatch(View view) {
        if (!invitationPopupIsShowing) {
            showPopUpNotification(false, "");
            mRealTimeMultiplayerClient
                    .getSelectOpponentsIntent(MIN_NUMBER_OF_PLAYERS, MAX_NUMBER_OF_PLAYERS)
                    .addOnSuccessListener(
                    new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_SELECT_PLAYERS);
                        }
                    }
            ).addOnFailureListener(createFailureListener("There was a problem selecting opponents."));
        }
    }

    @OnClick(R.id.join)
    public void onClickJoin(View view) {
        if (!invitationPopupIsShowing) {
            /*acceptInviteToRoom(mIncomingInvitationId);
            showPopUpNotification(false, "");*/
            mInvitationsClient.getInvitationInboxIntent().addOnCompleteListener(new OnCompleteListener<Intent>() {
                @Override
                public void onComplete(@NonNull Task<Intent> task) {
                    startActivityForResult(task.getResult(), RC_INVITATION_INBOX);
                }
            });
        }
    }

    @OnClick(R.id.credits)
    public void onClickCredits(View view) {
        if (!invitationPopupIsShowing) {
            Intent i = new Intent(MainActivity.this, CreditsActivity.class);
            startActivity(i);
        }
    }

    @OnClick(R.id.leaderboard)
    public void onClickLeaderboard(View view) {
        Log.d("LEADERBOARD", "on click");
        Games.getLeaderboardsClient(this, mSignedInAccount)
                .getLeaderboardIntent(getString(R.string.leaderboard_id))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }

    @OnClick(R.id.main_settings)
    public void onClickSettings(View view) {
        Intent i = new Intent(MainActivity.this, SpaceRacePreferenceActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.sigleplayer_btn)
    public void onClickSeeMap(View view) {
        Intent i = new Intent(MainActivity.this, SinglePlayerActivity.class);
        startActivity(i);
    }

    @Override
    public void onNetworkAvailable() {
        Log.d("NETWORKINGUTILITY", "onNetworkAvailable");

        if (NetworkingUtility.isNetworkAvailable()) {
            if (mSignedInAccount == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
                    }
                });
            }
            enablePlayButtons();
        } else {
            disablePlayButtons();
        }

    }

    @Override
    protected void showGameError() {
        BaseGameUtils.makeSimpleDialog(MainActivity.this, SpaceRace.getAppContext()
                .getString(R.string.game_problem));
        showPopUpNotification(false, "");
    }


    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RC_PERMISSION_GRANTED: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    enablePlayButtons();

                    if (noPermissionDialog != null) {
                        noPermissionDialog.hide();
                        noPermissionDialog.dismiss();
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    //TODO show pop up explaining that the application could not work
                    noPermissionDialog = new AlertDialog.Builder(this,
                                    android.R.style.Theme_Material_Dialog_Alert)
                            .setTitle(R.string.permission_granted_alert_title)
                            .setMessage(R.string.permission_granted_alert_summary)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .create();
                    noPermissionDialog.show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void enablePlayButtons() {
        if (NetworkingUtility.isNetworkAvailable() &&
            ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            mSignedInAccount != null) {
            for (int i : toEnable) {
                findViewById(i).setEnabled(true);

            }
        }
    }

    private void disablePlayButtons() {
        for (int i : toEnable) {
            findViewById(i).setEnabled(false);
        }
    }

    @Override
    public void requestPermissionsNeeded() {
        final String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        ActivityCompat.requestPermissions(this, permissions, RC_PERMISSION_GRANTED);
    }

    @Override
    protected void handleSelectPlayersResult(int response, Intent data) {
        super.handleSelectPlayersResult(response, data);
        keepScreenOn();
    }

    @Override
    protected void onConnected(GoogleSignInAccount googleSignInAccount) {
        super.onConnected(googleSignInAccount);

        enablePlayButtons();
    }
}
