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

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luolc.emojirain.EmojiRainLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EndMatchActivity extends AppCompatActivity {

    @BindView(R.id.end_match_activity)
    EmojiRainLayout endMatch;
    @BindView(R.id.end_string)
    TextView endStringTextView;
    @BindView(R.id.end_match_my_score_points)
    TextView myScoreText;
    @BindView(R.id.end_match_opponent_score_layout)
    LinearLayout opponentLayout;
    @BindView(R.id.end_match_opponent_score_points)
    TextView opponentScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_match);

        ButterKnife.bind(this);

        endMatch.addEmoji(R.drawable.endmatch_party_popper);
        endMatch.addEmoji(R.drawable.endmatch_confetti_ball);
        endMatch.addEmoji(R.drawable.endmatch_wind_chime);
        endMatch.addEmoji(R.drawable.endmatch_balloon);
        endMatch.addEmoji(R.drawable.endmatch_trophy);

        //endMatch.setPer(10);
        endMatch.setDuration(2000);
        endMatch.setDropFrequency(250);

        int myScore = getIntent().getIntExtra(MapActivity.MY_SCORE, -1);
        int opponentScore = getIntent().getIntExtra(MapActivity.OPPONENT_SCORE, -1);

        myScoreText.setText("" + myScore);

        switch (opponentScore) {
            case Integer.MIN_VALUE:
                endStringTextView.setText(R.string.your_opponent_retired);
                opponentLayout.setVisibility(View.GONE);
                break;
            case -1:
                endStringTextView.setText(R.string.end_match_single_player);
                opponentLayout.setVisibility(View.GONE);
                break;
            default:
                opponentScoreTextView.setText("" + opponentScore);
                if (myScore < opponentScore) {
                    endStringTextView.setText(R.string.end_match_loose);
                } else if (myScore == opponentScore) {
                    endStringTextView.setText(R.string.end_match_draw);
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                endMatch.startDropping();
            }
        }.execute(null, null, null);
        endMatch.startDropping();
    }

    @OnClick(R.id.go_to_the_home)
    public void onClickGoToHome(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EndMatchActivity.this, MainActivity.class));
        finish();
    }
}
