package com.bilgetech.guvercin;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;

public class PigeonButton extends AppCompatImageButton {
    public static final int ATTENTION_INTERVAL = 6000; // in millis
    public static final int MAX_ATTENTION_COUNT = 20;

    private boolean isActive;
    private boolean isAnimating;

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
        isAnimating = false;
        setVisibility(INVISIBLE);
        setBackgroundResource(R.color.transparent);
        setImageResource(R.drawable.pigeon_circular_passive);
        setScaleX(0);
        setScaleY(0);

        attentionTimer = new CountDownTimer(MAX_ATTENTION_COUNT * ATTENTION_INTERVAL, ATTENTION_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                if (!isActive && !isAnimating) {
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
    }


    public void activate(final Runnable endAction) {
        isActive = true;
        setImageResource(R.drawable.pigeon_circular_active);
        startRotationAnimation(720, new Runnable() {
            @Override
            public void run() {
                startPinchUpAnimation();
                endAction.run();
            }
        });
    }

    public void deactivate(final Runnable endAction) {
        isActive = false;
        setImageResource(R.drawable.pigeon_circular_passive);
        startRotationAnimation(-720, new Runnable() {
            @Override
            public void run() {
                startPinchDownAnimation();
                endAction.run();
            }
        });
    }

    private void startPinchDownAnimation() {
        isAnimating = true;
        animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                isAnimating = false;
                startSpringAnimation(0.06f, SpringForce.STIFFNESS_LOW);
            }
        }).start();
    }

    private void startPinchUpAnimation() {
        isAnimating = true;
        animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                isAnimating = false;
                startSpringAnimation(0.1f, SpringForce.STIFFNESS_LOW);
            }
        }).start();
    }

    private void startSpringAnimation(float dampingRatio, float stiffness) {
        isAnimating = true;
        SpringForce spring = new SpringForce(1f)
                .setDampingRatio(dampingRatio)
                .setStiffness(stiffness);

        SpringAnimation springXAnimation = new SpringAnimation(this, DynamicAnimation.SCALE_X).setSpring(spring);
        SpringAnimation springYAnimation = new SpringAnimation(this, DynamicAnimation.SCALE_Y).setSpring(spring);

        springXAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                isAnimating = false;
            }
        });

        springXAnimation.start();
        springYAnimation.start();
    }

    private void startRotationAnimation(float angle, final Runnable endAction) {
        isAnimating = true;
        animate().rotationBy(angle).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                isAnimating = false;
                if (endAction != null) {
                    endAction.run();
                }
            }
        }).start();
    }

    public void onPause() {
        attentionTimer.cancel();
        clearAnimation();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void setAnimating(boolean animating) {
        isAnimating = animating;
    }
}