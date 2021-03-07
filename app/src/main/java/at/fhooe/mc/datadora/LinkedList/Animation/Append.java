package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import java.util.Vector;

public class Append implements LinkedListAnimation {

    private static final String TAG = "Append : ";

    private static final String PROPERTY_TRANSLATE_Y_APPEND = "PROPERTY_TRANSLATE_APPEND";
    private static final String PROPERTY_ALPHA_APPEND = "PROPERTY_ALPHA_APPEND";

    // Animator for the appended element
    protected final ValueAnimator mAnimatorAppend = new ValueAnimator();

    // the current translation on the y-axis - used for the append animation
    private  int mTranslateYAppend;

    // the current alpha value - used for the append animation
    private int mAlphaAppend;

    private LLValue mValues;


    public Append(LLValue _values) {
        mValues = _values;

        PropertyValuesHolder propertyTranslateYAppend = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_APPEND, -200, 0);
        PropertyValuesHolder propertyAlphaAppend = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_APPEND, 0, 255);

        mAnimatorAppend.setValues(propertyTranslateYAppend, propertyAlphaAppend);
        mAnimatorAppend.setDuration(700);
        mAnimatorAppend.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void start(){
        mAnimatorAppend.start();
    }

    public void animate(int _pos) {
        if(_pos == mValues.getLinkedListRec().size() - 1) {
            mValues.getLinkedListRec().get(_pos).top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) + mTranslateYAppend;
            mValues.getItemTextPaint().setAlpha(mAlphaAppend);
            mValues.getItemPaint().setAlpha(mAlphaAppend);
        }
    }

    public void addUpdate(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorAppend.addUpdateListener(_listener);
    }
    public void update(ValueAnimator _animation) {
        if(_animation == mAnimatorAppend) {
            mTranslateYAppend = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_APPEND);
            mAlphaAppend = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_APPEND);
        }
    }
}