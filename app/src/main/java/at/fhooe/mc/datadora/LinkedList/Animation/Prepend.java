package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Vector;

public class Prepend implements LinkedListAnimation {

    private static final String TAG = "Prepend : ";

    private static final String PROPERTY_TRANSLATE_Y_PREPEND = "PROPERTY_TRANSLATE_Y_PREPEND";
    private static final String PROPERTY_ALPHA_PREPEND = "PROPERTY_ALPHA_PREPEND";

    // Animator for the prepended element
    private final ValueAnimator mAnimatorPrepend = new ValueAnimator();

    // the current translation on the y-axis - used for the prepend animation
    private int mTranslateYPrepend;

    // the current alpha value - used for the prepend animation
    private int mAlphaPrepend;

    private LLValue mValues;

    public Prepend(LLValue _values) {
        mValues = _values;

        int number = (int) (((mValues.getMaxWidth() / 4) * mValues.getScale()) - 10);
        PropertyValuesHolder propertyTranslateYPrepend = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_PREPEND, -number, 0);
        PropertyValuesHolder propertyAlphaPrepend = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_PREPEND, 0, 255);

        mAnimatorPrepend.setValues(propertyTranslateYPrepend, propertyAlphaPrepend);
        mAnimatorPrepend.setDuration(700);
        mAnimatorPrepend.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void start(){
        mAnimatorPrepend.start();
    }

    public void animate(int _pos){
        if (_pos == 0) {
            mValues.getItemTextPaint().setAlpha(mAlphaPrepend);
            mValues.getItemPaint().setAlpha(mAlphaPrepend);
        }
        mValues.getLinkedListRec().get(_pos).top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) - mTranslateYPrepend;

    }

    @Override
    public void addUpdate(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorPrepend.addUpdateListener(_listener);
    }

    @Override
    public void update(ValueAnimator _animation) {
        if(_animation == mAnimatorPrepend) {
            mTranslateYPrepend = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_PREPEND);
            mAlphaPrepend = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_PREPEND);
        }
    }
}
