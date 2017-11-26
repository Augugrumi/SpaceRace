package com.augugrumi.spacerace;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.augugrumi.spacerace.pathCreator.PathCreator;
import com.google.android.gms.maps.model.LatLng;

public class SinglePlayerActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void createAndDrawPath() {
        PathCreator p = new PathCreator(
                new LatLng(
                        initialPosition.getLatitude(),
                        initialPosition.getLongitude()
                ),
                0.3,
                2.5);

        path = p.generatePath();
        Log.d("PATH_CREATED", path.toString());

        drawPath();
        hideLoadingScreen();
    }
}
