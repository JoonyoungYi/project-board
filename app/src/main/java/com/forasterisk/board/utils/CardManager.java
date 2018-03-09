package com.forasterisk.board.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.forasterisk.board.R;

/**
 * Created by yearnning on 15. 7. 31..
 */
public class CardManager {

    private long start_datetime = 0;
    private View cardView;
    private Context context;

    public CardManager(Context context, View cardView) {
        this.context = context;
        this.cardView = cardView;
    }

    public void appear() {
        if (cardView.getVisibility() == View.GONE) {
            cardView.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.card_appear);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    start_datetime = System.currentTimeMillis();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    start_datetime = 0;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.cardView.startAnimation(animation);
        }
    }

    public void dismiss() {
        if (cardView.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.card_dismiss);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    start_datetime = System.currentTimeMillis();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    cardView.setVisibility(View.GONE);
                    start_datetime = 0;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.cardView.startAnimation(animation);
        }
    }

    public long getLeftTimeOfAnimation() {
        if (start_datetime == 0) {
            return 0;
        }

        if (isAnimating()) {
            long last_datetime = System.currentTimeMillis() - start_datetime;

            long result = cardView.getAnimation().computeDurationHint() - last_datetime + 100;
            if (result > 0) {
                return result;
            } else {
                return 0;
            }
        }

        return 500;
    }

    private boolean isAnimating() {
        Animation animation = cardView.getAnimation();
        if (animation == null) {
            return false;
        }
        return (animation.hasStarted() && !animation.hasEnded());
    }

}
