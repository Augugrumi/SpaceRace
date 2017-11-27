package com.augugrumi.spacerace;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.augugrumi.spacerace.listener.EndMatchReceiver;
import com.augugrumi.spacerace.listener.PathReceiver;
import com.augugrumi.spacerace.pathCreator.PathCreator;
import com.augugrumi.spacerace.pathCreator.PathManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Deque;

public class MultiPlayerActivity extends MapActivity
        implements PathReceiver, EndMatchReceiver{

    private boolean hasToCreatePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        hasToCreatePath = getIntent().getBooleanExtra(MainActivity.CREATOR_INTENT_EXTRA, false);
        Log.d("MEXX", "has to create:" + hasToCreatePath);

        SpaceRace.messageManager.registerForReceiveEndMatch(this);
        SpaceRace.messageManager.registerPathReceiver(this);
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
        SpaceRace.messageManager.sendToAllReliably(PathReceiver.ACK_PATH);
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
                hideLoadingScreen();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveAck() {
        Log.d("ACK_RECEIVED", "ack");

        hideLoadingScreen();
    }

    @Override
    public void hideHintAndShowMap() {
        super.hideHintAndShowMap();

        int score = getTotalScore().getScore();

        if (path.isEmpty()) {
            SpaceRace.messageManager.sendToAllReliably(
                    new EndMessageBuilder()
                            .setType(END_MATCH)
                            .setScore(score)
                            .build()
            );
            Games.getLeaderboardsClient(this,
                    GoogleSignIn.getLastSignedInAccount(this))
                    .submitScore(getString(R.string.leaderboard_id),
                            getTotalScore().getScore());
        }
    }

    @Override
    public void receiveEndMatch(String message) {
        //TODO add check which player won
        Log.d("END_MATCH",
                "your opponent arrived to destination before you with score:" +
                    EndMessageBuilder.decodeScore(message));

        int score = getTotalScore().getScore();

        Games.getLeaderboardsClient(this,
                GoogleSignIn.getLastSignedInAccount(this))
                .submitScore(getString(R.string.leaderboard_id),
                score);
        SpaceRace.messageManager.sendToAllReliably(
                new EndMessageBuilder()
                .setType(EndMatchReceiver.ACK_END_MATCH)
                .setScore(score)
                .build()
        );
    }

    @Override
    public void receiveAckEndMatch(String message) {
        //TODO add check which player won
        Log.d("END_MATCH",
                "your opponent score:" +
                        EndMessageBuilder.decodeScore(message));
    }

    private static class EndMessageBuilder {
        private String messageType;
        private int score;

        private static int decodeScore(String message) {
            int s = -1;

            JSONObject jsonMessage;
            try {
                jsonMessage = new JSONObject(message);
                s = jsonMessage.getInt("score");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return s;
        }

        private EndMessageBuilder setType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        private EndMessageBuilder setScore(int score) {
            this.score = score;
            return this;
        }

        public String build() {
            JSONObject obj = new JSONObject();
            try {
                obj.put("type", messageType).put("score", score);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj.toString();
        }
    }
}
