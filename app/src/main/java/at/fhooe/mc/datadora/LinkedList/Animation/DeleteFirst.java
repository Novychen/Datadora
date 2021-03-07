package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import at.fhooe.mc.datadora.LinkedList.Operation;

public class DeleteFirst implements LinkedListAnimation{

    private static final String TAG = "DeleteFirst : ";

    private static final String PROPERTY_TRANSLATE_Y_DELETE_FIRST = "PROPERTY_TRANSLATE_Y_DELETE_FIRST";
    private static final String PROPERTY_ALPHA_DELETE_FIRST = "PROPERTY_ALPHA_DELETE_FIRST";

    // Animator for the deleteLast element
    private final ValueAnimator mAnimatorDeleteFirst  = new ValueAnimator();

    // the current translation on the y-axis - used for the deleteFirst animation
    private int mTranslateYDeleteFirst;

    // the current alpha value - used for the deleteFirst animation
    private int mAlphaDeleteFirst;

    private LLValue mValues;

    public DeleteFirst(LLValue _values) {
        mValues = _values;

        int number = (int) (((mValues.getMaxWidth() / 4) * mValues.getScale()) - 10);

        PropertyValuesHolder propertyTranslateYDeleteFirst = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DELETE_FIRST, 0, number + 10);
        PropertyValuesHolder propertyAlphaDeleteFirst = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DELETE_FIRST, 255, 0);

        mAnimatorDeleteFirst.setValues(propertyTranslateYDeleteFirst, propertyAlphaDeleteFirst);
        mAnimatorDeleteFirst.setDuration(700);
        mAnimatorDeleteFirst.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    public void start() {
        mAnimatorDeleteFirst.start();
    }

    @Override
    public void animate(int _pos) {
        if(_pos == 0) {
            mValues.getLinkedListRec().get(_pos).top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) + mTranslateYDeleteFirst;
            mValues.getItemTextPaint().setAlpha(mAlphaDeleteFirst);
            mValues.getItemPaint().setAlpha(mAlphaDeleteFirst);
        } else {
            mValues.getLinkedListRec().get(_pos).top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) + mTranslateYDeleteFirst;
            mValues.getItemTextPaint().setAlpha(255);
            mValues.getItemPaint().setAlpha(255);
        }
    }

    @Override
    public void addUpdate(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorDeleteFirst.addUpdateListener(_listener);
    }

    @Override
    public void update(ValueAnimator _animation) {
        if(_animation == mAnimatorDeleteFirst) {
            mTranslateYDeleteFirst = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DELETE_FIRST);
            mAlphaDeleteFirst = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_DELETE_FIRST);
        }
    }

    public void afterAnimation(){
        if (!mAnimatorDeleteFirst.isRunning()) {
            mValues.getLinkedListNum().remove(0);
            mValues.getLinkedListRec().remove(0);
            mValues.setCurrentOperation(Operation.NONE);
        }
    }
}
