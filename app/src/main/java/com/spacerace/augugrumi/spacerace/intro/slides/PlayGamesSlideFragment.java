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
import com.spacerace.augugrumi.spacerace.SpaceRace;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayGamesSlideFragment extends Fragment {
    @BindView(R.id.playButton)
    SignInButton gPlaySignInButton;


    private final GoogleApiClient gAPIClient = SpaceRace.getgAPIClient();

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
}
