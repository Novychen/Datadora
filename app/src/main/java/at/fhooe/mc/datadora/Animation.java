package at.fhooe.mc.datadora;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;

public class Animation {

    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";

    private final Activity mActivity;
    private final View mView;

    private final int mX;
    private final int mY;

    public Animation(View _view, Intent _intent, Activity _activity) {
        mActivity = _activity;
        mView = _view;

        mX = _intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
        mY = _intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

        ViewTreeObserver viewTreeObserver = _view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    circularReveal();
                    mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }


    protected void circularReveal() {
        float finalRadius = (float) (Math.max(mView.getWidth(), mView.getHeight()) * 1.1);

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(mView, mX, mY, 0, finalRadius);
        circularReveal.setDuration(400);
        circularReveal.setInterpolator(new AccelerateInterpolator());

        mView.setVisibility(View.VISIBLE);
        mView.setBackgroundColor(mActivity.getResources().getColor(R.color.colorSurface,  mActivity.getTheme()));
        circularReveal.start();
    }

    public void circularUnreveal() {
            float finalRadius = (float) (Math.max(mView.getWidth(), mView.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    mView, mX, mY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mView.setVisibility(View.INVISIBLE);
                    mActivity.finish();
                    mActivity.overridePendingTransition(0, 0);
                }
            });
            circularReveal.start();
    }
}
