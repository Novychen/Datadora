package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

public class InsertAt implements LinkedListAnimation {

    private static final String PROPERTY_TRANSLATE_Y_INSERT = "PROPERTY_TRANSLATE_Y_INSERT";
    private static final String PROPERTY_ALPHA_INSERT = "PROPERTY_ALPHA_INSERT";

    // Animator for the inserted element
    private final ValueAnimator mAnimatorInsert = new ValueAnimator();

    // Animator for the alpha value of the inserted element
    private final ValueAnimator mAnimatorInsertAlpha = new ValueAnimator();

    // the current translation on the y-axis - used for the insertAt animation
    private int mTranslateYInsert;

    // the current alpha value - used for the insertAt animation
    private int mAlphaInsert;

    private LLValue mValues;

    public InsertAt(LLValue _values) {
        mValues = _values;

        int number = (int) (((mValues.getMaxWidth() / 4) * mValues.getScale()) - 10);

        PropertyValuesHolder propertyTranslateYInsert = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_INSERT, -number, 0);
        PropertyValuesHolder propertyAlphaInsert = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_INSERT, 0, 255);

        mAnimatorInsert.setValues(propertyTranslateYInsert);
        mAnimatorInsert.setDuration(700);
        mAnimatorInsert.setInterpolator(new AccelerateDecelerateInterpolator());

        mAnimatorInsertAlpha.setValues(propertyAlphaInsert);
        mAnimatorInsertAlpha.setDuration(900);
        mAnimatorInsertAlpha.setInterpolator(new AccelerateInterpolator());
    }

    public void addUpdateValueAnimator(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorInsertAlpha.addUpdateListener(_listener);
        mAnimatorInsert.addUpdateListener(_listener);
    }

    public void update(ValueAnimator _animation) {
        if(_animation == mAnimatorInsert) {
            mTranslateYInsert = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_INSERT);
        } else if(_animation == mAnimatorInsertAlpha) {
            mAlphaInsert = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_INSERT);
        }
    }

    public void start(){
        mAnimatorInsert.start();
        mAnimatorInsertAlpha.start();
    }

    public void animateNoPointer(int _pos){
        if (mValues.getPosition() + 1 == mValues.getLinkedListRec().size()) {
            mValues.getLinkedListRec().get(_pos).top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) ;
        }
        if (_pos == mValues.getPosition()) {
            mValues.getItemTextPaint().setAlpha(mAlphaInsert);
            mValues.getItemPaint().setAlpha(mAlphaInsert);
        } else if (_pos > mValues.getPosition()) {
            mValues.getLinkedListRec().get(_pos).top = (int) ((mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) - mTranslateYInsert);
            mValues.getItemTextPaint().setAlpha(255);
            mValues.getItemPaint().setAlpha(255);
        }
    }
}
