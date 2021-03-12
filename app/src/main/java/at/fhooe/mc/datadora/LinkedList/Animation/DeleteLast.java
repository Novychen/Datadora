package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import at.fhooe.mc.datadora.Operation;

public class DeleteLast implements Animator.AnimatorListener, LinkedListAnimation{

    private static final String TAG = "DeleteLast : ";

    private static final String PROPERTY_TRANSLATE_Y_DELETE_LAST = "PROPERTY_TRANSLATE_Y_DELETE_LAST";
    private static final String PROPERTY_ALPHA_DELETE_LAST = "PROPERTY_ALPHA_DELETE_LAST";

    // Animator for the deleteLast element
    private final ValueAnimator mAnimatorDeleteLast = new ValueAnimator();

    // the current translation on the y-axis - used for the deleteLast animation
    private int mTranslateYDeleteLast;

    // the current alpha value - used for the deleteLast animation
    private int mAlphaDeleteLast;

    private LLValue mValues;

    public DeleteLast(LLValue _values) {
        mValues = _values;

        PropertyValuesHolder propertyTranslateYDeleteLast = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DELETE_LAST, 0, -200);
        PropertyValuesHolder propertyAlphaDeleteLast = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DELETE_LAST, 255, 0);

        mAnimatorDeleteLast.setValues(propertyTranslateYDeleteLast, propertyAlphaDeleteLast);
        mAnimatorDeleteLast.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDeleteLast.addListener(this);
    }

    @Override
    public void start() {
        mAnimatorDeleteLast.setDuration(700);
        mAnimatorDeleteLast.setRepeatCount(0);
        mAnimatorDeleteLast.start();
    }

    public void startAsClear() {
        mAnimatorDeleteLast.setDuration(200);
        mAnimatorDeleteLast.setRepeatCount( mValues.getLinkedListNum().size()-1);
        mAnimatorDeleteLast.start();
    }

    @Override
    public void animateNoPointer(int _pos) {
        if (_pos ==  mValues.getLinkedListNum().size() - 1) {
            mValues.getLinkedListRec().get(_pos).top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) + mTranslateYDeleteLast;
            mValues.getItemTextPaint().setAlpha(mAlphaDeleteLast);
            mValues.getItemPaint().setAlpha(mAlphaDeleteLast);
        }
    }

    @Override
    public void addUpdateValueAnimator(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorDeleteLast.addUpdateListener(_listener);
    }

    @Override
    public void update(ValueAnimator _animation) {
        if(_animation == mAnimatorDeleteLast) {
            Log.i(TAG, "HELLO " + mAlphaDeleteLast);
            mTranslateYDeleteLast = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DELETE_LAST);
            mAlphaDeleteLast = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_DELETE_LAST);
        }
    }

    public void afterAnimation(){
        if(mValues.getCurrentOperation() == Operation.DELETE_LAST && !mAnimatorDeleteLast.isRunning()) {
            mValues.getLinkedListNum().remove(mValues.getLinkedListNum().size() - 1);
            mValues.getLinkedListRec().remove(mValues.getLinkedListRec().size() - 1);
            mValues.setCurrentOperation(Operation.NONE);
        }
    }


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator _animation) {
        if (_animation == mAnimatorDeleteLast && mValues.getCurrentOperation() == Operation.CLEAR) {
            mValues.getLinkedListNum().remove(mValues.getLinkedListNum().size() - 1);
            mValues.getLinkedListRec().remove(mValues.getLinkedListRec().size() - 1);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator _animation) {
        if(_animation == mAnimatorDeleteLast) {
            mValues.getLinkedListNum().remove(mValues.getLinkedListNum().size() - 1);
            mValues.getLinkedListRec().remove( mValues.getLinkedListRec().size() - 1);
        }
    }
}
