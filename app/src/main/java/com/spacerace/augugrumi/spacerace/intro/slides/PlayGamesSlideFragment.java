package com.spacerace.augugrumi.spacerace.intro.slides;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.spacerace.augugrumi.spacerace.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayGamesSlideFragment extends Fragment
        implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.playButton)
    SignInButton gPlaySignInButton;


    private GoogleApiClient gAPIClient;

    public PlayGamesSlideFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {

        super.onActivityCreated(savedInstance);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_play_games_slide, container, false);

        ButterKnife.bind(this, v);

        gAPIClient = new GoogleApiClient.Builder(getContext())
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER)
                .build();

        gPlaySignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gAPIClient != null && gAPIClient.isConnected()) {
                    gAPIClient.clearDefaultAccountAndReconnect();
                }
                gAPIClient.connect();
            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d("INTRO", "Something went wrong with Google Play Games connection...");

        new AlertDialog.Builder(getContext())
                .setTitle(getResources().getText(R.string.warningSlider3Title))
                .setMessage(getResources().getText(R.string.warningSlider3SubTitle))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Nothing?
                    }
                })
                .show();
    }
}
