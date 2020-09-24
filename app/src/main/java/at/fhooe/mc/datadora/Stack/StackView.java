package at.fhooe.mc.datadora.Stack;

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

import at.fhooe.mc.datadora.R;

/**
 * Class StackView, implements the animation for the stack in normal mode
 */
public class StackView extends View {

    private static final String PROPERTY_TRANSLATE_Y_PUSH = "PROPERTY_TRANSLATE_Y_PUSH";
    private static final String PROPERTY_TRANSLATE_Y_POP = "PROPERTY_TRANSLATE_Y_POP";
    private static final String PROPERTY_ALPHA_PUSH = "PROPERTY_ALPHA_PUSH";
    private static final String PROPERTY_ALPHA_POP = "PROPERTY_ALPHA_POP";
    private static final String TAG = "StackView : ";

    // Booleans that check which is the current operation
    boolean mPop, mPush, mPeek, mClear, mRandom;

    // int that counts how often the operator pushed was used (by operator random)
    int mPosition;

    // int that specifies the radius of the corners of the drawn rectangles
    int mRadius = 4;

    // Vector that gets the random generated stack
    Vector<Integer> mRandomStack = new Vector<>();

    // Animator for the last pushed element
    ValueAnimator mAnimatorPush = new ValueAnimator();

    // Animator for the last pushed element
    ValueAnimator mAnimatorPop = new ValueAnimator();

    // Animator for the operation peek (for the area of one item)
    ValueAnimator mAnimatorPeekArea = new ValueAnimator();

    // Animator for the operation peek (for the text of one item)
    ValueAnimator mAnimatorPeekText = new ValueAnimator();

    // Vector that contains all Rects, that are drawn
    Vector<RectF> mStack = new Vector<>();

    // Vector that contains all Integers, that are drawn / the user put in
    Vector<Integer> mStackNumbers = new Vector<>();

    // Rect in order to save the TextBounds from the current number
    Rect mBounds = new Rect();

    // factor for the change of height of the stack item boxes, when the stack is too high
    float mScale = 1;

    // factor for the change of width of the stack item boxes, when the number is to big for the current width
    int mResize = 1;

    // the current translation on the y-axis - used for the push animation
    int mTranslateYPush;

    // the current translation on the y-axis - used for the push animation
    int mTranslateYPop;

    // the current alpha value - used for the push animation
    int mAlphaPush;

    // the current alpha value - used for the pop animation
    int mAlphaPop;

    // the current color value - used for peek animation (for the area of one item)
    int mColorAreaPeek;

    // the current color value - used for peek animation (for the text of one item)
    int mColorTextPeek;

    // the current primary color of the currently used theme
    int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    Paint mStackItemPaint = new Paint();
    Paint mStackItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // current width of the StackView within the layout
    float mMaxWidthStack;

    // current height of the StackView within the layout
    float mMaxHeightStack;

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

    /**
     * gets the current maximum out of the mStackNumbers Vector
     *
     * @return the maximum int of the mStackNumbers Vector
     */
    private int getMax() {
        int max = 0;
        for (int i : mStackNumbers) {
            if (Math.abs(i) > max) {
                max = Math.abs(i);
            }
        }
        return max;
    }

    /**
     * Initializes the key components such as Paint
     */
    private void init() {
        mStackItemPaint.setColor(mPrimaryColor);
        mStackItemPaint.setStyle(Paint.Style.STROKE);
        mStackItemPaint.setStrokeWidth(6);

        mStackItemTextPaint.setColor(mOnSurfaceColor);
        mStackItemTextPaint.setTextSize(55);
    }

    protected void init(StackActivity _activity) {
        mStackActivity = _activity;
    }

    /**
     * marks the latest element of the stack
     */
    protected void peek() {
        //mAnimatorPeekBorder.start();
        mAnimatorPeekText.start();
        mAnimatorPeekArea.start();
        mPeek = true;
        mClear = false;
        mRandom = false;
        mPop = false;
        mPush = false;
        invalidate();
    }

    /**
     * clears the stack
     */
    protected void clear() {
        mPeek = false;
        mClear = true;
        mRandom = false;
        mPop = true;
        mPush = false;
        mAnimatorPop.setDuration(200);
        mAnimatorPop.start();
    }

    /**
     * creates a random stack
     */
    protected void random(Vector<Integer> _stack) {
        mStackNumbers.clear();
        mStack.clear();
        mPeek = false;
        mClear = false;
        mRandom = true;
        mPop = false;
        mPush = false;
        mRandomStack = _stack;
        mAnimatorPush.setDuration(350);
        push(_stack.get(mPosition));
        mPosition++;
    }

    /**
     * prepares the view for the pop
     */
    protected void prePop() {
        mPeek = false;
        mClear = false;
        mRandom = false;
        mPop = true;
        mPush = false;
        mAnimatorPop.start();
    }

    /**
     * removes a element from the stack
     */
    private void pop() {
        mStack.removeElementAt(mStack.size() - 1);
        mStackNumbers.removeElementAt(mStackNumbers.size() - 1);

        if (mMaxHeightStack > (mMaxWidthStack / 4) * (mScale * 1.2f) * mStack.size()) {
            mScale = mScale * 1.2f;
            if (mScale > 1) {
                mScale = 1;
            }
        }
        changeBoxSize(getMax(), false);
    }

    /**
     * adds a element to the stack
     */
    protected void push(int _value) {
        RectF r = new RectF();
        mStack.add(r);
        mStackNumbers.add(_value);

        if (mMaxHeightStack <= (mMaxWidthStack / 4) * mScale * mStack.size()) {
            mScale = mScale / 1.2f;
        }
        changeBoxSize(getMax(), true);
        mPeek = false;
        mClear = false;
        mPop = false;
        mPush = true;
        mAnimatorPush.start();
    }

    /**
     * checks in what range the current max value is and changes the scale value for the boxes accordingly
     *
     * @param _value the maximum value of the current stack
     * @param _push  true if a value was pushed, false if it was poped
     */
    private void changeBoxSize(int _value, boolean _push) {
        int val;
        if (_value > 999999999) {
            val = 6;
        } else if (_value > 99999999) {
            val = 5;
        } else if (_value > 9999999) {
            val = 4;
        } else if (_value > 999999) {
            val = 3;
        } else if (_value > 99999) {
            val = 2;
        } else if (_value > 9999) {
            mResize = 40 * 2;
            return;
        } else if (_value > 999) {
            mResize = 40;
            return;
        } else {
            mResize = 1;
            return;
        }

        if (!_push) {
            val++;
        }
        mResize = 40 * val;
    }

    @Override
    protected void onSizeChanged(int _w, int _h, int _oldW, int _oldH) {
        super.onSizeChanged(_w, _h, _oldW, _oldH);
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        // size of parent of this view
        mMaxHeightStack = (float) _h - ypad - 6;
        mMaxWidthStack = (float) _w - xpad - 6;
        mScale = 1;

        PropertyValuesHolder propertyTranslateYPush = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_PUSH, -200, 0);
        PropertyValuesHolder propertyAlphaPush = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_PUSH, 0, 255);

        PropertyValuesHolder propertyTranslateYPop = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_POP, 0, -200);
        PropertyValuesHolder propertyAlphaPop = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_POP, 255, 0);

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

        invalidate();
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        for (int i = 0; i < mStackNumbers.size(); i++) {
            if (i == mStackNumbers.size() - 1) {  // only animate the last object - translate box + text and change the alpha
                if (mPush) {
                    mStack.get(i).top = (int) (mMaxHeightStack - ((mMaxWidthStack / 4) + (mMaxWidthStack / 4 * i)) * mScale) + mTranslateYPush;
                    mStackItemTextPaint.setAlpha(mAlphaPush);
                    mStackItemPaint.setAlpha(mAlphaPush);
                } else if (mPop) {
                    mStack.get(i).top = (int) (mMaxHeightStack - ((mMaxWidthStack / 4) + (mMaxWidthStack / 4 * i)) * mScale) + mTranslateYPop;
                    mStackItemTextPaint.setAlpha(mAlphaPop);
                    mStackItemPaint.setAlpha(mAlphaPop);
                } else if(mPeek){
                    mStack.get(i).top = (int) (mMaxHeightStack - ((mMaxWidthStack / 4) + (mMaxWidthStack / 4 * i)) * mScale);
                    mStackItemPaint.setColor(mColorAreaPeek);
                    mStackItemPaint.setStyle(Paint.Style.FILL);
                    _canvas.drawRoundRect(mStack.get(i), mRadius, mRadius, mStackItemPaint);
                    mStackItemPaint.setColor(mPrimaryColor);
                    mStackItemPaint.setStyle(Paint.Style.STROKE);
                    mStackItemTextPaint.setColor(mColorTextPeek);
                }
            } else {
                mStack.get(i).top = (int) (mMaxHeightStack - ((mMaxWidthStack / 4) + (mMaxWidthStack / 4 * i)) * mScale);
                mStackItemTextPaint.setAlpha(255);
                mStackItemPaint.setAlpha(255);
                mStackItemTextPaint.setColor(mOnSurfaceColor);
                mStackItemPaint.setColor(mPrimaryColor);
            }
            // save the size of the text ("box" around the text) in mBounds
            mStackItemTextPaint.getTextBounds(mStackNumbers.get(i).toString(), 0, mStackNumbers.get(i).toString().length(), mBounds);

            // set Stack item size
            mStack.get(i).left = (int) ((mMaxWidthStack / 2) - (mMaxWidthStack / 8) - (mResize / 2));
            mStack.get(i).right = (int) (mStack.get(i).left + (mMaxWidthStack / 4) + mResize);
            mStack.get(i).bottom = (int) (mStack.get(i).top + ((mMaxWidthStack / 4) * mScale) - 10);

            // get BoundingBox from Text & draw Text + StackItem
            _canvas.drawText(mStackNumbers.get(i).toString(), getExactCenterX(mStack.get(i)) - mBounds.exactCenterX(), (getExactCenterY(mStack.get(i)) - mBounds.exactCenterY()), mStackItemTextPaint);
            _canvas.drawRoundRect(mStack.get(i), mRadius, mRadius, mStackItemPaint);
        }

        if (mPop && mAlphaPop == 0 && !mClear) { // Animation of pop is over -> remove element
            pop();
            mPop = false;
            mStackActivity.setPressedPop(false);
        } else if (mClear && mStackNumbers.isEmpty()) { // clear operation is finished
            mPop = false;
            mClear = false;
            mStackActivity.showEmpty();
            mAnimatorPop.setDuration(700);
            mPosition = 0;
        } else if (mClear && mPop && mAlphaPop == 0) { // clear operation is ongoing - animation is finished - remove last element
            pop();
            mAnimatorPop.start();
        } else if (mRandom && mRandomStack.size() == mStackNumbers.size()) { // random operation is finished
            mRandom = false;
            mAnimatorPush.setDuration(700);
            mStackActivity.setPressedRandom(false);
            mPosition = 0;
        } else if (mRandom && mAlphaPush == 255) { // random animation for element is finished - add next element
            push(mRandomStack.get(mPosition));
            mPosition++;
        }
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