package com.augugrumi.spacerace;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.augugrumi.spacerace.listener.PathReceiver;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashSet;

/**
 * @author Marco Zanella
 * @version 0.01
 * date 01/11/17
 */

public class SpaceRace extends Application {

    private static Context instance;
    private static GoogleApiClient gAPIClient;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        gAPIClient = new GoogleApiClient.Builder(instance)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d("INTRO", "Connected to Google Play Games");

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d("INTRO", "Connection to Google Play Games suspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {

                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Log.d("INTRO", "Something went wrong with Google Play Games connection...\n" +
                                connectionResult.toString());

                        /*new AlertDialog.Builder(instance)
                                .setTitle(getResources().getText(R.string.warningSlider3Title))
                                .setMessage(getResources().getText(R.string.warningSlider3SubTitle))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        // Nothing?
                                    }
                                })
                                .show();*/
                    }
                })
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER)
                //.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    public static Context getAppContext() {
        return instance;
    }

    public static GoogleApiClient getgAPIClient() {
        return gAPIClient;
    }

    public static void setgAPIClient(GoogleApiClient client) {
        SpaceRace.gAPIClient = client;
    }

    public static MessageManager messageManager = new MessageManager();

    public static class MessageManager implements OnRealTimeMessageReceivedListener,
            RealTimeMultiplayerClient.ReliableMessageSentCallback{

        private Room mRoom = null;
        private String mMyParticipantId = null;
        private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;
        private HashSet<Integer> pendingMessageSet = new HashSet<>();
        private PathReceiver pathReceiver;

        private MessageManager(){}

        public void setRealTimeMultiplayerClient(RealTimeMultiplayerClient client) {
            mRealTimeMultiplayerClient = client;
        }

        public void setRoom(Room room) {
            mRoom = room;
        }

        public void setParticipantId(String participantId) {
            mMyParticipantId = participantId;
        }

        public void sendToAllReliably(final String messageString) {
            byte [] message = messageString.getBytes();
            for (final String participantId : mRoom.getParticipantIds()) {
                if (!participantId.equals(mMyParticipantId)) {
                    mRealTimeMultiplayerClient.sendReliableMessage(message,
                            mRoom.getRoomId(), participantId,
                            this).addOnCompleteListener(
                            new OnCompleteListener<Integer>() {
                                @Override
                                public void onComplete(@NonNull Task<Integer> task) {
                                    // Keep track of which messages are sent, if desired.
                                    Log.d("MEXX", "Sent '" + messageString + "' to " + participantId);
                                    recordMessageToken(task.getResult());
                                }
                            });
                }
            }
        }

        private synchronized void recordMessageToken(int tokenId) {
            pendingMessageSet.add(tokenId);
        }

        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            // Handle messages received here.
            byte[] message = realTimeMessage.getMessageData();
            Log.d("MEXX", "Received:" + new String(message));
            if (pathReceiver!=null)
                pathReceiver.receivePath(new String(message));
        }

        @Override
        public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
            // handle the message being sent.
            synchronized (this) {
                pendingMessageSet.remove(tokenId);
            }
        }

        public void registerForReceivePaths(PathReceiver pathReceiver) {
            this.pathReceiver = pathReceiver;
        }
    }
}