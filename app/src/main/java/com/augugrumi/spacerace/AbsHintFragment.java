package com.augugrumi.spacerace;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.augugrumi.spacerace.utility.gameutility.ScoreCounter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by dpolonio on 30/11/17.
 */

public abstract class AbsHintFragment extends Fragment {
    protected MapActivity parent;
    protected LatLng poi;
    protected ArrayList<ViewGroup> layouts;

    protected void showView(ViewGroup view) {
        for (ViewGroup v : layouts) {
            v.setVisibility(View.GONE);
        }
        view.setVisibility(View.VISIBLE);
    }

    protected abstract void setHintData ();

    @Override
    public void onStart() {
        super.onStart();
        setHintData();
    }

    public void setPOI(LatLng poi) {
        Log.d("SET_POI", poi.toString());
        this.poi = poi;
    }
}
