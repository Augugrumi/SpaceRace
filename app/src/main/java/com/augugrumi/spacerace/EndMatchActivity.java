package com.augugrumi.spacerace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

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
    }

    @OnClick(R.id.rain_test)
    public void onClick() {
        endMatch.startDropping();
    }
}
