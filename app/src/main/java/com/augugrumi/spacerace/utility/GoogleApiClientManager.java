package com.augugrumi.spacerace.utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.augugrumi.spacerace.SpaceRace;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 04/11/17
 */

public class GoogleApiClientManager {
    public static GoogleApiClient setUpGoogleApi(
            FragmentActivity activity,
            GoogleApiClient.ConnectionCallbacks callbacks,
            GoogleApiClient.OnConnectionFailedListener failedListener) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Games.SCOPE_GAMES, Drive.SCOPE_APPFOLDER)
                .requestEmail()
                .build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(SpaceRace.getAppContext())
                .enableAutoManage(activity, failedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(callbacks)
                .addApi(Games.API)//.addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API)//.addScope(Drive.SCOPE_APPFOLDER)
                .build();

        if (!mGoogleApiClient.isConnected()) {
            //mGoogleApiClient.connect();
            SpaceRace.setgAPIClient(mGoogleApiClient);
        }

        return mGoogleApiClient;
    }
}
