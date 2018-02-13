package com.bilgetech.guvercin;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class PulseAnimationView extends FrameLayout {

    private int animationDuration = 4000;
    private int pulseCount = 5;
    private int pulseDelay;

    ArrayList<AnimatorSet> pulseAnimations;

    public PulseAnimationView(@NonNull Context context) {
        super(context);
        init();
    }

    public PulseAnimationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PulseAnimationView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        // Prepare pulses
        pulseDelay = animationDuration / pulseCount;
        pulseAnimations = new ArrayList<>();

        for (int i = 0; i < pulseCount; i++) {

            FrameLayout.LayoutParams pulseParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pulseParams.gravity = Gravity.CENTER;

            final View pulseView = new View(getContext());
            pulseView.setLayoutParams(pulseParams);
            pulseView.setBackgroundResource(R.drawable.pulse_circular);

            addView(pulseView);

            // Set object animators
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(pulseView, "alpha", 0);
            ValueAnimator sizeAnimator = ValueAnimator.ofInt(0, 1000);
            sizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int size = (int) animation.getAnimatedValue();
                    pulseView.getLayoutParams().width = size;
                    pulseView.getLayoutParams().height = size;
                    pulseView.setLayoutParams(pulseView.getLayoutParams());
                }
            });

            alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
            sizeAnimator.setRepeatCount(ValueAnimator.INFINITE);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(animationDuration);//.setStartDelay(i * pulseDelay);
            animatorSet.playTogether(sizeAnimator, alphaAnimator);

            pulseAnimations.add(animatorSet);
        }

    }

    public void startAnimation() {
        for (int i = 0; i < pulseAnimations.size(); i++) {
            AnimatorSet animatorSet = pulseAnimations.get(i);
            animatorSet.start();

            for (Animator animator : animatorSet.getChildAnimations()) {
                ((ValueAnimator) animator).setCurrentPlayTime(i * pulseDelay);
            }
        }
    }

    public void stopAnimation() {
        for (AnimatorSet animatorSet : pulseAnimations) {
            animatorSet.cancel();
        }
    }

    public void show() {
        setVisibility(View.VISIBLE);
        setAlpha(0);
        animate().alpha(1).setDuration(500).start();
        startAnimation();
    }

    public void hide() {
        animate().alpha(0).setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                stopAnimation();
                setVisibility(INVISIBLE);
            }
        }).start();
    }
}
