package com.bilgetech.guvercin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView menuButton;

    ToggleFragment toggleFragment = new ToggleFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuButton.setSelected(!menuButton.isSelected());

                if (menuButton.isSelected()) {
                    toggleFragment.hideButton(new Runnable() {
                        @Override
                        public void run() {
                            showFragment(settingsFragment);
                        }
                    });
                } else {
                    showFragment(toggleFragment);
                }
            }
        });

        showFragment(toggleFragment);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if(menuButton.isSelected()) {
             menuButton.performClick();
        } else {
            super.onBackPressed();
        }
    }
}
