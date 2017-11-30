package com.augugrumi.spacerace;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.augugrumi.spacerace.pathCreator.PathCreator;
import com.google.android.gms.maps.GoogleMap;
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

    @Override
    public void hideHintAndShowMap() {
        super.hideHintAndShowMap();

        if(path.isEmpty())
            endMatch();
    }

    public void endMatch() {
        Log.d("END_MATCH", "the game is over");
	    launchEndMatchActivity();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.exit)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SinglePlayerActivity.this.finish();
                        startActivity(new Intent(SinglePlayerActivity.this, MainActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
