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

import com.augugrumi.spacerace.utility.LanguageManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.championswimmer.libsocialbuttons.fabs.FABFacebook;

import static com.augugrumi.spacerace.utility.Costants.FACEBOOK_URI;
import static com.augugrumi.spacerace.utility.Costants.GITHUB_URI;

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

        LanguageManager.languageManagement(this);
    }

    @OnClick(R.id.facebook)
    public void goOnFacebookPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URI));
        startActivity(browserIntent);
    }

    @OnClick(R.id.github)
    public void goOnGithubPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URI));
        startActivity(browserIntent);
    }
}
