package com.augugrumi.spacerace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.augugrumi.spacerace.intro.IntroActivity;
import com.augugrumi.spacerace.utility.SharedPreferencesManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.main_activity)
    View myView;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Games.SCOPE_GAMES, Drive.SCOPE_APPFOLDER)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        //Games.setViewForPopups(mGoogleApiClient, getWindow().getDecorView().findViewById(android.R.id.content));
                        Log.d("INTRO", "onConnected");
                        /*Games.setViewForPopups(mGoogleApiClient,
                                findViewById(R.id.gps_popup));*/
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d("INTRO", "OnConnectionSuspended");
                    }
                })
                .addApi(Games.API)//.addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API)//.addScope(Drive.SCOPE_APPFOLDER)
                .build();


        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        //mGoogleApiClient.clearDefaultAccountAndReconnect();
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.

        // [END customize_button]


            /*Intent intent = new Intent(MainActivity.this, RoomActivity.class);
            startActivity(intent);*/
        //}
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SharedPreferencesManager.getFirstApplicationRun()) {

            Log.d("INTRO", "First run detected, launching sliders...");

            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);

            SharedPreferencesManager.setFirstApplicationRun(true);
        }
        /*
        } else {*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("INTRO", "onConnectionFailed:" + connectionResult);
    }
}
