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

        endMatch.addEmoji(R.drawable.astronaut);
    }

    @OnClick(R.id.rain_test)
    public void onClick() {
        endMatch.startDropping();
    }
}
