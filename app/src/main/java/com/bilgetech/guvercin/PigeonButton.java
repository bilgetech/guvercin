package com.bilgetech.guvercin;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

public class PigeonButton extends AppCompatImageButton {
    public static final int ATTENTION_INTERVAL = 6000; // in millis
    public static final int MAX_ATTENTION_COUNT = 20;

    private boolean isActive;
    private boolean isRotating;

    private CountDownTimer attentionTimer;

    public PigeonButton(Context context) {
        super(context);
        init();
    }

    public PigeonButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PigeonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        isActive = false;
        isRotating = false;
        setVisibility(INVISIBLE);
        setBackgroundResource(R.color.transparent);
        setImageResource(R.drawable.pigeon_circular_passive);
        setScaleX(0);
        setScaleY(0);

        attentionTimer = new CountDownTimer(MAX_ATTENTION_COUNT * ATTENTION_INTERVAL, ATTENTION_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                if (!isActive && !isRotating) {
                    startPinchDownAnimation();
                }
            }

            public void onFinish() {
            }
        };
    }

    public void showUp() {
        setVisibility(View.VISIBLE);
        startSpringAnimation(0.3f, SpringForce.STIFFNESS_LOW);
        startTimerAfterDelay();
    }

    public void hide(@Nullable final Runnable endAction) {
        attentionTimer.cancel();
        animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(300)
                .setInterpolator(new AnticipateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (endAction != null) {
                            endAction.run();
                        }
                    }
                })
                .start();
    }

    private void startTimerAfterDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                attentionTimer.start();
            }
        }, ATTENTION_INTERVAL);
    }


    public void activate(final Runnable endAction) {
        setActive(true);
        startRotationAnimation(720, new Runnable() {
            @Override
            public void run() {
                startPinchUpAnimation();
                endAction.run();
            }
        });

        startTimerAfterDelay();
    }

    public void deactivate(final Runnable endAction) {
        setActive(false);
        startRotationAnimation(-720, new Runnable() {
            @Override
            public void run() {
                startPinchDownAnimation();
                endAction.run();
            }
        });

        attentionTimer.cancel();
    }

    public void setActive(boolean active) {
        isActive = active;
        updateUI();
    }

    public void updateUI() {
        setImageResource(isActive
                ? R.drawable.pigeon_circular_active
                : R.drawable.pigeon_circular_passive);
    }

    private void startPinchDownAnimation() {
        animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                startSpringAnimation(0.06f, SpringForce.STIFFNESS_LOW);
            }
        }).start();
    }

    private void startPinchUpAnimation() {
        animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                startSpringAnimation(0.1f, SpringForce.STIFFNESS_LOW);
            }
        }).start();
    }

    private void startSpringAnimation(float dampingRatio, float stiffness) {
        SpringForce spring = new SpringForce(1f)
                .setDampingRatio(dampingRatio)
                .setStiffness(stiffness);

        SpringAnimation springXAnimation = new SpringAnimation(this, DynamicAnimation.SCALE_X).setSpring(spring);
        SpringAnimation springYAnimation = new SpringAnimation(this, DynamicAnimation.SCALE_Y).setSpring(spring);

        springXAnimation.start();
        springYAnimation.start();
    }

    private void startRotationAnimation(float angle, final Runnable endAction) {
        isRotating = true;
        animate().rotationBy(angle).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                isRotating = false;
                if (endAction != null) {
                    endAction.run();
                }
            }
        }).start();
    }

    public void onResume() {
        startTimerAfterDelay();
    }

    public void onPause() {
        attentionTimer.cancel();
        clearAnimation();
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isRotating() {
        return isRotating;
    }
}
