package at.fhooe.mc.datadora.Stack;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.LinkedListView;
import at.fhooe.mc.datadora.R;

/**
 * Class StackView, implements the animation for the stack in normal mode
 */
public class StackView extends View {

    private static final String PROPERTY_TRANSLATE_Y_PUSH = "PROPERTY_TRANSLATE_Y_PUSH";
    private static final String PROPERTY_TRANSLATE_Y_POP = "PROPERTY_TRANSLATE_Y_POP";
    private static final String PROPERTY_TRANSLATE_Y_RANDOM = "PROPERTY_TRANSLATE_Y_RANDOM";
    private static final String PROPERTY_ALPHA_PUSH = "PROPERTY_ALPHA_PUSH";
    private static final String PROPERTY_ALPHA_POP = "PROPERTY_ALPHA_POP";
    private static final String PROPERTY_ALPHA_RANDOM = "PROPERTY_ALPHA_RANDOM";
    private static final String TAG = "StackView : ";

    // the current operation
    private Operation mCurrentOperation;

    //Check what the current operation is
    private enum Operation{
        POP,
        PUSH,
        PEEK,
        CLEAR,
        RANDOM,
        SIZE,
        SAVE
    }

    // int that counts how often the operator pushed was used (by operator random)
    private int mPositionAnimation;

    // int that specifies the radius of the corners of the drawn rectangles
    private int mRadius = 4;

    // Animator for the last pushed element
    private ValueAnimator mAnimatorPush = new ValueAnimator();

    // Animator for the last pushed element
    private ValueAnimator mAnimatorPop = new ValueAnimator();

    // Animator for the operation peek (for the area of one item)
    private ValueAnimator mAnimatorPeekArea = new ValueAnimator();

    // Animator for the operation peek (for the text of one item)
    private ValueAnimator mAnimatorPeekText = new ValueAnimator();

    // Animator for the operation random
    private ValueAnimator mAnimatorRandom = new ValueAnimator();

    // Vector that contains all Rects, that are drawn
    private Vector<RectF> mStack = new Vector<>();

    // Vector that contains all Integers, that are drawn / the user put in
    private Vector<Integer> mStackNumbers = new Vector<>();

    // Vector for the animation of the operation random
    private Vector<Integer> mStackAnimation = new Vector<>();

    // Rect in order to save the TextBounds from the current number
    private Rect mBounds = new Rect();

    // factor for the change of height of the stack item boxes, when the stack is too high
    private float mScale = 1;

    // the current translation on the y-axis - used for the push animation
    private int mTranslateYPush;

    // the current translation on the y-axis - used for the push animation
    private int mTranslateYPop;

    // the current translation on the y-axis - used for the random animation
    private int mTranslateYRandom;

    // the current alpha value - used for the push animation
    private int mAlphaPush;

    // the current alpha value - used for the pop animation
    private int mAlphaPop;

    // the current alpha value - used for the random animation
    private int mAlphaRandom;

    // the current color value - used for peek animation (for the area of one item)
    private int mColorAreaPeek;

    // the current color value - used for peek animation (for the text of one item)
    private int mColorTextPeek;

    // the current primary color of the currently used theme
    private int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    private int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    private int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    private int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    private Paint mItemPaint = new Paint();
    private Paint mItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // current width of the StackView within the layout
    private float mMaxWidth;

    // current height of the StackView within the layout
    private float mMaxHeight;

    private StackActivity mStackActivity;

    public StackView(Context context) {
        super(context);
        init();
    }

    public StackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected Vector<Integer> getStackNumbers() {
        return mStackNumbers;
    }

    /**
     * Initializes the key components such as Paint
     */
    private void init() {
        mItemPaint.setColor(mPrimaryColor);
        mItemPaint.setStyle(Paint.Style.STROKE);
        mItemPaint.setStrokeWidth(6);

        mItemTextPaint.setColor(mOnSurfaceColor);
        mItemTextPaint.setTextSize(55);
    }

    protected void setActivity (StackActivity _activity) {
        mStackActivity = _activity;
    }

    /**
     * marks the latest element of the stack
     */
    protected void peek() {
        mCurrentOperation = Operation.PEEK;
        mAnimatorPeekText.start();
        mAnimatorPeekArea.start();
        invalidate();
    }

    /**
     * clears the stack
     */
    protected void clear() {
        mCurrentOperation = Operation.CLEAR;
        mAnimatorPop.setDuration(200);
        mAnimatorPop.setRepeatCount(mStackNumbers.size()-1);
        mAnimatorPop.start();
        reScale();
    }

    /**
     * creates a random stack
     */
    protected void random(Vector<Integer> _stack) {
        mCurrentOperation = Operation.RANDOM;
        mStackNumbers.clear();
        mStack.clear();
        mStackAnimation.clear();
        mPositionAnimation = 0;
        mStackAnimation.addAll(_stack);
        mAnimatorRandom.setRepeatCount(_stack.size() - 1);
        mAnimatorRandom.start();
    }


    /**
     * removes a element from the stack
     */
    protected void pop() {
        mCurrentOperation = Operation.POP;
        mAnimatorPop.setDuration(700);
        mAnimatorPop.setRepeatCount(0);
        mAnimatorPop.start();
        reScale();
    }

    /**
     * adds a element to the stack
     */
    protected void push(int _value) {
        mCurrentOperation = Operation.PUSH;
        RectF r = new RectF();
        mStack.add(r);
        mStackNumbers.add(_value);

        if (mMaxHeight <= (mMaxWidth / 4) * mScale * mStack.size()) {
            mScale = mScale / 1.2f;
        }
        mAnimatorPush.start();
    }

    private void reScale() {
        if (mMaxHeight <= (mMaxWidth / 4) * mScale * mStack.size()) {
            mScale = mScale / 1.2f;
        }
    }

    @Override
    protected void onSizeChanged(int _w, int _h, int _oldW, int _oldH) {
        super.onSizeChanged(_w, _h, _oldW, _oldH);

        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        // size of parent of this view
        mMaxHeight = (float) _h - ypad - 6;
        mMaxWidth = (float) _w - xpad - 6;
        mScale = 1;

        setUpAnimation();

        //Visualize the vector in the Shared Preferences
        Vector<Integer> v = mStackActivity.loadFromSave();
        if(v != null) {
            for (int i = 0; i < v.size(); i++) {
                mStackNumbers.add(v.get(i));
                mStack.add(new RectF());
            }
            mCurrentOperation = Operation.SAVE;
        }
    }

    private void setUpAnimation() {
        PropertyValuesHolder propertyTranslateYPush = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_PUSH, -200, 0);
        PropertyValuesHolder propertyAlphaPush = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_PUSH, 0, 255);

        mAnimatorPush.setValues(propertyTranslateYPush, propertyAlphaPush);
        mAnimatorPush.setDuration(700);
        mAnimatorPush.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorPush.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYPush = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_PUSH);
                mAlphaPush = (int) animation.getAnimatedValue(PROPERTY_ALPHA_PUSH);
                invalidate();
            }
        });

        PropertyValuesHolder propertyTranslateYPop = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_POP, 0, -200);
        PropertyValuesHolder propertyAlphaPop = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_POP, 255, 0);

        mAnimatorPop.setValues(propertyTranslateYPop, propertyAlphaPop);
        mAnimatorPop.setDuration(700);
        mAnimatorPop.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorPop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYPop = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_POP);
                mAlphaPop = (int) animation.getAnimatedValue(PROPERTY_ALPHA_POP);
                invalidate();
            }
        });

        mAnimatorPop.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(mCurrentOperation == Operation.CLEAR ) {
                    mStackNumbers.remove(mStackNumbers.size() - 1);
                    mStack.remove( mStack.size() - 1);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                mStackNumbers.remove(mStackNumbers.size() - 1);
                mStack.remove(mStack.size() - 1);
            }
        });

        mAnimatorPeekArea = ValueAnimator.ofObject(new ArgbEvaluator(), mSurfaceColor, mPrimaryColor);
        mAnimatorPeekArea.setDuration(1000);
        mAnimatorPeekArea.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorPeekArea.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColorAreaPeek = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        mAnimatorPeekText = ValueAnimator.ofObject(new ArgbEvaluator(), mOnSurfaceColor, mOnPrimaryColor);
        mAnimatorPeekText.setDuration(1000);
        mAnimatorPeekText.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorPeekText.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColorTextPeek = (int) animation.getAnimatedValue();
                invalidate();
            }
        });


        PropertyValuesHolder propertyTranslateYRandom = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_RANDOM, -200, 0);
        PropertyValuesHolder propertyAlphaRandom = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_RANDOM, 0, 255);

        mAnimatorRandom.setValues(propertyTranslateYRandom, propertyAlphaRandom);
        mAnimatorRandom.setDuration(350);
        mAnimatorRandom.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorRandom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYRandom = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_RANDOM);
                mAlphaRandom = (int) animation.getAnimatedValue(PROPERTY_ALPHA_RANDOM);
                invalidate();
            }
        });

        mAnimatorRandom.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mStackNumbers.add(mStackAnimation.get(mPositionAnimation));
                mStack.add(new RectF());
                mPositionAnimation++;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                mStackNumbers.add(mStackAnimation.get(mPositionAnimation));
                mStack.add(new RectF());
                mPositionAnimation++;
            }
        });

    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        for (int i = 0; i < mStackNumbers.size(); i++) {

            mStack.get(i).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * i)) * mScale);
            mItemTextPaint.setAlpha(255);
            mItemPaint.setAlpha(255);
            mItemTextPaint.setColor(mOnSurfaceColor);
            mItemPaint.setColor(mPrimaryColor);

            animateOperation(_canvas, i);
        }
        if (mCurrentOperation == Operation.POP && !mAnimatorPop.isRunning()) {
            mStackActivity.setPressedPop(false);

            mStackNumbers.remove(mStackNumbers.size() - 1);
            mStack.remove( mStack.size() - 1);

            if (mMaxHeight > (mMaxWidth / 4) * (mScale * 1.2f) * mStack.size()) {
                mScale = mScale * 1.2f;
                if (mScale > 1) {
                    mScale = 1;
                }
            }
        } else if(mCurrentOperation == Operation.RANDOM && !mAnimatorRandom.isRunning()) {
            mStackActivity.setPressedRandom(false);
        }
    }

    /**
     * draws the get animation all operations
     * @param _canvas canvas on which the animation is painted
     * @param _pos the current position the loop from the onDraw is in
     */
    private void animateOperation(Canvas _canvas, int _pos) {
        switch (mCurrentOperation) {
            case PUSH: {
                if(_pos == mStack.size() - 1) {
                    mStack.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) + mTranslateYPush;
                    mItemTextPaint.setAlpha(mAlphaPush);
                    mItemPaint.setAlpha(mAlphaPush);
                }
            } break;
            case CLEAR :
            case POP: {
                if (_pos == mStack.size() - 1) {
                    mStack.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) + mTranslateYPop;
                    mItemTextPaint.setAlpha(mAlphaPop);
                    mItemPaint.setAlpha(mAlphaPop);
                }
            } break;
            case PEEK: {
                if (_pos == mStack.size() - 1) {
                    mStack.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale);
                    mItemPaint.setColor(mColorAreaPeek);
                    mItemPaint.setStyle(Paint.Style.FILL);
                    _canvas.drawRoundRect(mStack.get(_pos), mRadius, mRadius, mItemPaint);
                    mItemPaint.setColor(mPrimaryColor);
                    mItemPaint.setStyle(Paint.Style.STROKE);
                    mItemTextPaint.setColor(mColorTextPeek);
                }
            } break;
            case RANDOM: {
                if (_pos == mStack.size() - 1) {
                    mStack.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) + mTranslateYRandom;
                    mItemTextPaint.setAlpha(mAlphaRandom);
                    mItemPaint.setAlpha(mAlphaRandom);
                }
            } break;
        }

        // save the size of the text ("box" around the text) in mBounds
        mItemTextPaint.getTextBounds(mStackNumbers.get(_pos).toString(), 0, mStackNumbers.get(_pos).toString().length(), mBounds);

        // set Stack item size
        // factor for the change of width of the stack item boxes, when the number is to big for the current width
        int mResize = 1;
        mStack.get(_pos).left = (int) ((mMaxWidth / 2) - (mMaxWidth / 8) - (mResize / 2));
        mStack.get(_pos).right = (int) (mStack.get(_pos).left + (mMaxWidth / 4) + mResize);
        mStack.get(_pos).bottom = (int) (mStack.get(_pos).top + ((mMaxWidth / 4) * mScale) - 10);

        // get BoundingBox from Text & draw Text + StackItem
        _canvas.drawText(mStackNumbers.get(_pos).toString(), getExactCenterX(mStack.get(_pos)) - mBounds.exactCenterX(), (getExactCenterY(mStack.get(_pos)) - mBounds.exactCenterY()), mItemTextPaint);
        _canvas.drawRoundRect(mStack.get(_pos), mRadius, mRadius, mItemPaint);
    }

    /**
     * Get the exact Center from the given RectF in the x-Axis
     * @param _rect the RectF, which center is wanted
     * @return the center of the given RectF
     */
    private float getExactCenterX(RectF _rect) {
        return ((_rect.right - _rect.left) / 2) + _rect.left;
    }

    /**
     * Get the exact Center from the given RectF in the y-Axis
     * @param _rect the RectF, which center is wanted
     * @return the center of the given RectF
     */
    private float getExactCenterY(RectF _rect) {
        return ((_rect.bottom - _rect.top) / 2) + _rect.top;
    }
}
