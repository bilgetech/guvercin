package com.bilgetech.guvercin;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final int INITIAL_ANIMATION_START_DELAY = 500;

    private PigeonButton pigeonButton;
    private PulseAnimationView pulseAnimationView;
    private ImageView menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pigeonButton = findViewById(R.id.pigeonButton);
        pulseAnimationView = findViewById(R.id.pulseAnimationView);
        menuButton = findViewById(R.id.menuButton);

        pigeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pigeonButton.isRotating()) return;

                if (pigeonButton.isActive()) {
                    pigeonButton.deactivate(new Runnable() {
                        @Override
                        public void run() {
                            pulseAnimationView.hide();
                        }
                    });
                } else {
                    pigeonButton.activate(new Runnable() {
                        @Override
                        public void run() {
                            pulseAnimationView.show();
                        }
                    });
                }
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuButton.setSelected(!menuButton.isSelected());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pigeonButton.showUp();
            }
        }, INITIAL_ANIMATION_START_DELAY);

        if (pigeonButton.isActive()) {
            pulseAnimationView.show();
        }

        pigeonButton.onResume();
    }

    @Override
    protected void onPause() {
        pigeonButton.onPause();
        pulseAnimationView.stopAnimation();
        super.onPause();
    }

}
