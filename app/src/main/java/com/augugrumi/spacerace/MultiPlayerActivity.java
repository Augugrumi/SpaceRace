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

import com.augugrumi.spacerace.listener.AckReceiver;
import com.augugrumi.spacerace.listener.EndMatchReceiver;
import com.augugrumi.spacerace.listener.PathReceiver;
import com.augugrumi.spacerace.pathCreator.PathCreator;
import com.augugrumi.spacerace.pathCreator.PathManager;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Deque;

public class MultiPlayerActivity extends MapActivity
        implements PathReceiver, AckReceiver, EndMatchReceiver{

    private boolean hasToCreatePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasToCreatePath = getIntent().getBooleanExtra(MainActivity.CREATOR_INTENT_EXTRA, false);
        Log.d("MEXX", "has to create:" + hasToCreatePath);
        SpaceRace.messageManager.registerForReceiveEndMatch(this);
        if (!hasToCreatePath)
            SpaceRace.messageManager.registerForReceivePaths(this);
        else
            SpaceRace.messageManager.registerForReceiveAck(this);
    }

    protected void createAndDrawPath() {
        if (hasToCreatePath) {
            PathCreator p = new PathCreator(
                    new LatLng(
                            initialPosition.getLatitude(),
                            initialPosition.getLongitude()
                    ),
                    0.3,
                    2.5);

            path = p.generatePath();

            sendPath(path);

            drawPath();
        }
    }

    private void checkIfValidPathOrDie() {

        if (path == null || path.isEmpty()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this,
                    android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle(R.string.path_problem)
                    .setMessage(R.string.path_not_found)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(MultiPlayerActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    protected void sendPath(Deque<PathCreator.DistanceFrom> path) {
        PathManager pathManager = new PathManager(path);
        SpaceRace.messageManager.sendToAllReliably(pathManager.toJson());
        checkIfValidPathOrDie();
    }

    protected void sendAck() {
        SpaceRace.messageManager.sendToAllReliably(ACK);
    }

    @Override
    public void receivePath(String jsonPath) {
        try {
            if (jsonPath.isEmpty() || jsonPath.equals("[]")) {
                checkIfValidPathOrDie();
            } else {

                PathManager pathManager = new PathManager(new JSONArray(jsonPath));
                path = pathManager.getPath();
                Log.d("MEXX", "decoded:" + path.toString());
                drawPath();
                sendAck();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveAck() {
        Log.d("ACK_RECEIVED", "ack");
    }

    @Override
    public void hideHintAndShowMap() {
        super.hideHintAndShowMap();

        if (path.isEmpty()) {
            SpaceRace.messageManager.sendToAllReliably(END_MATCH);
        }
    }

    @Override
    public void endMatch() {
        Log.d("END_MATCH", "your opponent arrived to destination before you");
    }
}
