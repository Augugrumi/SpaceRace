package com.augugrumi.spacerace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.championswimmer.libsocialbuttons.fabs.FABFacebook;

public class CreditsActivity extends AppCompatActivity {
    @BindView(R.id.facebook)
    FABFacebook fabFacebook;

    @BindView(R.id.github)
    ImageButton fabGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.facebook)
    public void goOnFacebookPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
        startActivity(browserIntent);
    }

    @OnClick(R.id.github)
    public void goOnGithubPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Augugrumi"));
        startActivity(browserIntent);
    }
}
