package com.bilgetech.guvercin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoImage = findViewById(R.id.logoImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTransitionAfterSomeTime();
    }


    private void startTransitionAfterSomeTime() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                String transitionName = getString(R.string.transition_logo);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(SplashActivity.this, logoImage, transitionName);
                startActivity(intent, options.toBundle());
                finishActivityAfterSomeTime();
            }
        }, 1000);
    }

    private void finishActivityAfterSomeTime() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }
}
