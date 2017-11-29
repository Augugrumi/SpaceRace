package com.augugrumi.spacerace;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;

import com.luolc.emojirain.EmojiRainLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EndMatchActivity extends AppCompatActivity {
    
    @BindView(R.id.end_match_activity)
    EmojiRainLayout endMatch;

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
}
