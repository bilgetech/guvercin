package com.bilgetech.guvercin;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ToggleFragment extends Fragment {
    public static final int INITIAL_ANIMATION_START_DELAY = 200;
    private PigeonButton pigeonButton;
    private PulseAnimationView pulseAnimationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toggle, container, false);
        pigeonButton = view.findViewById(R.id.pigeonButton);
        pulseAnimationView = view.findViewById(R.id.pulseAnimationView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pigeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pigeonButton.isActive()) {
                    Prefs.get().setActive(false).save();
                    pigeonButton.deactivate(new Runnable() {
                        @Override
                        public void run() {
                            pulseAnimationView.hide();
                        }
                    });
                } else {
                    Prefs.get().setActive(true).save();
                    pigeonButton.activate(new Runnable() {
                        @Override
                        public void run() {
                            pulseAnimationView.show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        pigeonButton.setActive(Prefs.get().isActive());

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
    public void onPause() {
        pigeonButton.onPause();
        pulseAnimationView.stopAnimation();
        super.onPause();
    }

    public void hideButton(Runnable endAction) {
        pigeonButton.hide(endAction);
    }
}
