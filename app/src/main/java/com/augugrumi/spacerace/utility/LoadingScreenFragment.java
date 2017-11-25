package com.augugrumi.spacerace.utility;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augugrumi.spacerace.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingScreenFragment extends Fragment {

    @BindView(R.id.loading_tip)
    TextView tip;

    @BindView(R.id.loading_action)
    TextView action;

    public LoadingScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_loading_screen, container, false);

        ButterKnife.bind(this, v);
        tip.setText(LoadingPhraseChooser.pickRandomTip());
        action.setText(LoadingPhraseChooser.pickRandomAction());

        return v;
    }

}
