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

    private int myScore;
    private int opponentScore;
    private boolean win;

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

        myScore = getIntent().getIntExtra(MapActivity.MY_SCORE, -1);
        opponentScore = getIntent().getIntExtra(MapActivity.OPPONENT_SCORE, -1);

        win = myScore > opponentScore;

        myScoreText.setText("" + myScore);

        if (opponentScore != -1) {
            opponentScoreTextView.setText("" + opponentScore);
            if (!win) {
                endStringTextView.setText(R.string.end_match_loose);
            }
        } else {
            endStringTextView.setText(R.string.end_match_single_player);
            opponentLayout.setVisibility(View.GONE);
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
                } finally {

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
    }
}
