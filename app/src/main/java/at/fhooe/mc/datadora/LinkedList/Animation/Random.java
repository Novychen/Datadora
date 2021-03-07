package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import java.util.Vector;

public class Random implements LinkedListAnimation {

    private static final String TAG = "Random : ";

    private static final String PROPERTY_TRANSLATE_Y_RANDOM = "PROPERTY_TRANSLATE_RANDOM";
    private static final String PROPERTY_ALPHA_RANDOM = "PROPERTY_ALPHA_RANDOM";

    // Animator for the created random list
    private final ValueAnimator mAnimatorRandom = new ValueAnimator();

    // the current alpha value - used for the random animation
    private int mAlphaRandom;

    // the current translation on the y-axis - used for the random animation
    private int mTranslateYRandom;

    private LLValue mValues;

    public Random(LLValue _values) {
        mValues = _values;

        PropertyValuesHolder propertyTranslateYRandom = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_RANDOM, -200, 0);
        PropertyValuesHolder propertyAlphaRandom = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_RANDOM, 0, 255);

        mAnimatorRandom.setValues(propertyTranslateYRandom, propertyAlphaRandom);
        mAnimatorRandom.setDuration(350);
        mAnimatorRandom.setInterpolator(new AccelerateInterpolator());
    }

    public void start(Vector<Integer> _list) {
        mAnimatorRandom.setRepeatCount(_list.size() - 1);
        mAnimatorRandom.start();
    }

    @Override
    public void start() {

    }

    @Override
    public void animate(int _pos) {
        if (_pos == mValues.getLinkedListRec().size() - 1) {
            mValues.getLinkedListRec().get(_pos).top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) + mTranslateYRandom;
            mValues.getItemTextPaint().setAlpha(mAlphaRandom);
            mValues.getItemPaint().setAlpha(mAlphaRandom);
        }
    }

    @Override
    public void addUpdate(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorRandom.addUpdateListener(_listener);
    }

    @Override
    public void update(ValueAnimator _animation) {
        if(_animation == mAnimatorRandom) {
            mTranslateYRandom = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_RANDOM);
            mAlphaRandom = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_RANDOM);
        }
    }

    public void addUpdate(Animator.AnimatorListener _listener) {
        mAnimatorRandom.addListener(_listener);
    }

    public void animStart(Animator _animation) {
        if(_animation == mAnimatorRandom) {
            mValues.getLinkedListNum().add(mValues.getLinkedListRand().get(mValues.getPosition()));
            mValues.getLinkedListRec().add(new RectF());
            mValues.setPosition(mValues.getPosition() + 1);
        }
    }

    public void animRepeat(Animator _animation) {
        if(_animation == mAnimatorRandom) {
            mValues.getLinkedListNum().add(mValues.getLinkedListRand().get(mValues.getPosition()));
            mValues.getLinkedListRec().add(new RectF());
            mValues.setPosition(mValues.getPosition() + 1);
        }
    }
}
