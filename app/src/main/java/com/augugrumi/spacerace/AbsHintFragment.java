package com.augugrumi.spacerace;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by dpolonio on 30/11/17.
 */

public abstract class AbsHintFragment extends Fragment {
    protected MapActivity parent;
    protected LatLng actualPoi;
    protected LatLng nextPoi;
    protected ArrayList<ViewGroup> layouts;

    protected void showView(ViewGroup view) {
        for (ViewGroup v : layouts) {
            v.setVisibility(View.GONE);
        }
        view.setVisibility(View.VISIBLE);
    }

    public abstract void setHintData ();

    @Override
    public void onStart() {
        super.onStart();
        setHintData();
    }

    public void setPOI(LatLng actualPoi, LatLng nextPoi) {
        Log.d("SET_POI", actualPoi.toString());
        this.actualPoi = actualPoi;
        this.nextPoi = nextPoi;
    }
}
