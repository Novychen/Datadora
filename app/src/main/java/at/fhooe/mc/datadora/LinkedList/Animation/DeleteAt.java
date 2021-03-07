package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import at.fhooe.mc.datadora.LinkedList.Operation;

public class DeleteAt implements LinkedListAnimation{

    private static final String PROPERTY_TRANSLATE_Y_DELETE_AT = "PROPERTY_TRANSLATE_Y_DELETE_AT";
    private static final String PROPERTY_ALPHA_DELETE_AT = "PROPERTY_ALPHA_DELETE_AT";

    // Animator for the deletedAt element
    private final ValueAnimator mAnimatorDeleteAt = new ValueAnimator();

    // Animator for the alpha value of the deletedAt element
    private final ValueAnimator mAnimatorDeleteAtAlpha = new ValueAnimator();

    // the current translation on the y-axis - used for the deleteAt animation
    private int mTranslateYDeleteAt;

    // the current alpha value - used for the deleteAt animation
    private int mAlphaDeleteAt;

    private LLValue mValues;

    public DeleteAt(LLValue _values) {
        mValues = _values;

        int number = (int) (((mValues.getMaxWidth() / 4) * mValues.getScale()) - 10);

        PropertyValuesHolder propertyTranslateYDeleteAt = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DELETE_AT, 0, -number - 10);
        PropertyValuesHolder propertyAlphaDeleteAt = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DELETE_AT, 255, 0);

        mAnimatorDeleteAt.setValues(propertyTranslateYDeleteAt);
        mAnimatorDeleteAt.setDuration(900);
        mAnimatorDeleteAt.setInterpolator(new AccelerateDecelerateInterpolator());

        mAnimatorDeleteAtAlpha.setValues(propertyAlphaDeleteAt);
        mAnimatorDeleteAtAlpha.setDuration(700);
        mAnimatorDeleteAtAlpha.setInterpolator(new AccelerateInterpolator());
    }

    @Override
    public void start() {
        mAnimatorDeleteAt.start();
        mAnimatorDeleteAtAlpha.start();
    }

    @Override
    public void animate(int _pos) {
        if (_pos == mValues.getPosition()) {
            mValues.getItemTextPaint().setAlpha(mAlphaDeleteAt);
            mValues.getItemPaint().setAlpha(mAlphaDeleteAt);
        } else if (_pos > mValues.getPosition()) {
            mValues.getLinkedListRec().get(_pos).top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) *  mValues.getScale()) - mTranslateYDeleteAt;
            mValues.getItemTextPaint().setAlpha(255);
            mValues.getItemPaint().setAlpha(255);
        }
    }

    @Override
    public void addUpdate(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorDeleteAt.addUpdateListener(_listener);
        mAnimatorDeleteAtAlpha.addUpdateListener(_listener);
    }

    @Override
    public void update(ValueAnimator _animation) {
        if(_animation == mAnimatorDeleteAt) {
            mTranslateYDeleteAt = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DELETE_AT);
        } else if(_animation == mAnimatorDeleteAtAlpha) {
            mAlphaDeleteAt = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_DELETE_AT);
        }
    }

    public void afterAnimation() {
        if (!mAnimatorDeleteAt.isRunning()) {
            mValues.getLinkedListRec().remove(mValues.getPosition());
            mValues.getLinkedListNum().remove(mValues.getPosition());
        }
    }
}
