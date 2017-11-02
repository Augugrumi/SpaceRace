package com.augugrumi.spacerace.intro.slides;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augugrumi.spacerace.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideOneFragment extends Fragment {


    public SlideOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {

        super.onActivityCreated(savedInstance);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slide_one, container, false);
    }

}
