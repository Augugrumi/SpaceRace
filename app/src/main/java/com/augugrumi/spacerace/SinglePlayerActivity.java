/**
* Copyright 2017 Davide Polonio <poloniodavide@gmail.com>, Federico Tavella
* <fede.fox16@gmail.com> and Marco Zanella <zanna0150@gmail.com>
* 
* This file is part of SpaceRace.
* 
* SpaceRace is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SpaceRace is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with SpaceRace.  If not, see <http://www.gnu.org/licenses/>.
*/

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
import com.augugrumi.spacerace.utility.CoordinatesUtility;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import static com.augugrumi.spacerace.utility.Costants.KM_DISTANCE_HINT;

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

        if (path == null || path.isEmpty()) {
            dieNoPath();
        } else {
            drawPath();
            hideLoadingScreen();
        }
    }

    private void dieNoPath() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle(R.string.path_problem)
                .setMessage(R.string.path_not_found)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(SinglePlayerActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void hideHintAndShowMap() {
        super.hideHintAndShowMap();

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        if(path.isEmpty() && CoordinatesUtility.get2DDistanceInKm(currentLatLng,
                poi) < KM_DISTANCE_HINT) {
            endMatch();
        }
    }

    public void endMatch() {
        Log.d("END_MATCH", "the game is over");
        myScore = getTotalScore().getScore();
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
