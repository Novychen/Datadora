package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.animation.AccelerateDecelerateInterpolator;

import at.fhooe.mc.datadora.Operation;

public class Append implements LinkedListAnimation {

    //TODO: Pointer Append Pointer not animated -> + additional animation steps need to be implemented
    private static final String TAG = "Append : ";

    private static final String PROPERTY_TRANSLATE_Y_APPEND = "PROPERTY_TRANSLATE_APPEND";
    private static final String PROPERTY_ALPHA_APPEND = "PROPERTY_ALPHA_APPEND";
    private static final String PROPERTY_STROKE_WIDTH = "PROPERTY_STROKE_WIDTH";

    // Animator for the appended element
    private final ValueAnimator mAnimatorAppend = new ValueAnimator();

    private final ValueAnimator mAnimatorWidth = new ValueAnimator();

    // the current translation on the y-axis - used for the append animation
    private int mTranslateYAppend;

    // the current alpha value - used for the append animation
    private int mAlphaAppend;

    private int mWidth;

    private final LLValue mValues;
    private boolean mPointer;

    public void setPointer(boolean _pointer) {
        mPointer = _pointer;
    }

    public Append(LLValue _values) {
        mValues = _values;
        setUp();
    }

    /**
     * This method sets the different ValueAnimators up. These vary depending if the animation will be with or without pointers.
     */
    public void setUp() {
        if (mPointer) {
            PropertyValuesHolder propertyWidth = PropertyValuesHolder.ofInt(PROPERTY_STROKE_WIDTH, 6, 10, 6);
            PropertyValuesHolder propertyAlphaAppend = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_APPEND, 0, 255);

            mAnimatorAppend.setValues(propertyAlphaAppend);
            mAnimatorAppend.setDuration(500);
            mAnimatorAppend.setInterpolator(new AccelerateDecelerateInterpolator());

            mAnimatorWidth.setValues(propertyWidth);
            mAnimatorWidth.setRepeatCount(mValues.getLinkedListNum().size());
            mAnimatorWidth.setInterpolator(new AccelerateDecelerateInterpolator());
        } else {
            PropertyValuesHolder propertyTranslateYAppend = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_APPEND, -200, 0);
            PropertyValuesHolder propertyAlphaAppend = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_APPEND, 0, 255);

            mAnimatorAppend.setValues(propertyTranslateYAppend, propertyAlphaAppend);
            mAnimatorAppend.setDuration(700);
            mAnimatorAppend.setInterpolator(new AccelerateDecelerateInterpolator());
        }
    }

    /**
     * This method starts the different ValueAnimators. The started Animators vary depending if the animation will be with or without pointers.
     */
    public void start() {
        if (mPointer) {
            if(mValues.getLinkedListNum().isEmpty()) {
                mAnimatorWidth.setDuration(100);
            } else {
                mAnimatorWidth.setDuration(150 * (mValues.getLinkedListNum().size() - 1));
            }
            mAnimatorWidth.start();
        } else {
            mAnimatorAppend.start();
        }
    }

    /**
     * This method starts the ValueAnimator that is needed for the fade-in animation of the new item that will be appended.
     */
    public void startNewItemAnim() {
        mAnimatorAppend.start();
    }

    /**
     * This method animates the fade-in and translate animation of the append animation without pointers. It only animates the last element in the list as this will be appended.
     *
     * @param _pos the position of the current drawn element
     */
    public void animateNoPointer(int _pos) {
        if (_pos == mValues.getLinkedListRec().size() - 1) {
            mValues.getLinkedListRec().get(_pos).top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) + mTranslateYAppend;
            mValues.getItemTextPaint().setAlpha(mAlphaAppend);
            mValues.getItemPaint().setAlpha(mAlphaAppend);
        }
    }

    public void animateWidth(Pointer _pointer, Canvas _canvas, int _pos) {
        mValues.getItemPaint().setStrokeWidth(mWidth);
        _pointer.setStrokeWidth(mWidth);
        _pointer.drawPointers(_canvas, _pos);
        mValues.getItemPaint().setStrokeWidth(mWidth);
    }

    /**
     * This method animates the fade-in animation of the append animation with pointers. It also defines where the new item will be drawn according to the current last position of the list.
     *
     * @param _pos  the position of the last element in the list - used to determine the position for the new drawn element
     * @param _rect the rect that will be drawn for the new element
     */
    public void animateNewElement(int _pos, RectF _rect) {
        if (mValues.getCurrentOperation() == Operation.APPEND) {
            _rect.top = (int) (mValues.getMaxHeight() - ((mValues.getMaxWidth() / 4) + (mValues.getMaxWidth() / 4 * _pos)) * mValues.getScale()) - (mValues.getMaxWidth() / 8);
            mValues.getItemTextPaint().setAlpha(mAlphaAppend);
            mValues.getItemPaint().setAlpha(mAlphaAppend);
        }
    }

    public void animateOperation(final Pointer _pointer, final Canvas _canvas, final RectF _rect) {
        _pointer.setBigPointer(true);
        _pointer.drawBigPointer(_canvas, _rect);
    }

    /**
     * This method is called after the animation with pointer has ended. It adds the value to the list.
     *
     * @param _value the value that is added
     */
    public void afterPointerAnimation(int _value) {
        RectF r = new RectF();
      //  mValues.getLinkedListRec().add(r);
      //  mValues.getLinkedListNum().add(_value);
    }

    /**
     * This method adds ValueAnimator.AnimatorUpdateListener to the ValueAnimators.
     *
     * @param _listener the ValueAnimator.AnimatorUpdateListener that is added
     */
    public void addUpdateValueAnimator(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorAppend.addUpdateListener(_listener);
        mAnimatorWidth.addUpdateListener(_listener);
    }

    /**
     * This method adds Animator.AnimatorListener to the ValueAnimators.
     *
     * @param _listener the Animator.AnimatorListener that is added
     */
    public void addUpdateAnimator(Animator.AnimatorListener _listener) {
        mAnimatorWidth.addListener(_listener);
        mAnimatorAppend.addListener(_listener);
    }

    /**
     * This method is called in the ValueAnimator.AnimatorUpdateListener.onAnimationUpdate method and
     * updates the values from the ValueAnimators variables. This is different depending on
     * the animation (with pointer / no pointer)
     *
     * @param _animation the ValueAnimator that is currently updated.
     */
    public void update(ValueAnimator _animation) {
        if (_animation == mAnimatorAppend) {
            if (!mPointer) {
                mTranslateYAppend = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_APPEND);
            }
            mAlphaAppend = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_APPEND);
        } else if (_animation == mAnimatorWidth) {
            mWidth = (int) _animation.getAnimatedValue(PROPERTY_STROKE_WIDTH);
        }
    }

    /**
     * This method is called in the Animator.AnimatorListener.onAnimationRepeat method.
     * It is used to let the width of the stroke of each element's rect vary independently
     * one after another.
     * @param _animation the Animator currently used.
     */
    public void animRepeat(Animator _animation) {
        if (_animation == mAnimatorWidth) {
            mValues.setPosition(mValues.getPosition() + 1);
        }
    }

    /**
     * This method is called in the Animator.AnimatorListener.onAnimationEnd method. It is used
     * to start the animation of the new item, after the pointer animation has finished and calls
     * the {@ink #afterPointerAnimation()}.
     * @param _animation the Animator currently used.
     * @param _value the value that will be passed to {@link #afterPointerAnimation(int)} to be added to the list
     * @return a boolean that indicates if the pointer animation of the append animation has ended
     */
    public boolean animEnd(Animator _animation, int _value) {
        if (_animation == mAnimatorAppend && mPointer) {
            afterPointerAnimation(_value);
            //mValues.setCurrentOperation(Operation.NONE);
        } else if (_animation == mAnimatorWidth && mPointer) {
            startNewItemAnim();
            return true;
        }
        return false;
    }
}