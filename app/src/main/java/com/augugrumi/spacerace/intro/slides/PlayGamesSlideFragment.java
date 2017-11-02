package com.augugrumi.spacerace.intro.slides;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;

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
