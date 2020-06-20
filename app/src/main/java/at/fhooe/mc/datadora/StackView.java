package at.fhooe.mc.datadora;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.Vector;

/**
 * Class StackView, implements the animation for the stack in normal mode
 */
public class StackView extends View {

    private static final String PROPERTY_TRANSLATE_Y = "PROPERTY_TRANSLATE_Y";
    private static final String PROPERTY_ALPHA = "PROPERTY_ALPHA";
    private static final String TAG = "StackView :: ";

    // Animator for the last poped & pushed element
    ValueAnimator mAnimator = new ValueAnimator();

    // Vector that contains all Rects, that are drawn
    Vector<Rect> mStack = new Vector<>();

    // Vector that contains all Integers, that are drawn / the user put in
    Vector<Integer> mStackNumbers = new Vector<>();

    // Rect in order to save the TextBounds from the current number
    Rect mBounds = new Rect();

    // factor for the change of height of the stack item boxes, when the stack is too high
    float mScale = 1;

    // factor for the change of width of the stack item boxes, when the number is to big for the current width
    int mResize = 1;

    // the current translation on the y-axis - used for the animation
    int mTranslateY;

    // the current alpha value - used for the animation
    int mAlpha;

    Paint mStackItemPaint = new Paint();
    Paint mStackItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // current width of the StackView within the layout
    float mMaxWidthStack;

    // current height of the StackView within the layout
    float mMaxHeightStack;


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

    public StackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
     * Initializes the key components such as Paint, the PropertyHolders for the animations as well as the animation itself
     */
    private void init() {
        mStackItemPaint.setColor(getResources().getColor(R.color.primaryColor, this.getContext().getTheme()));
        mStackItemPaint.setStyle(Paint.Style.STROKE);
        mStackItemPaint.setStrokeWidth(6);

        mStackItemTextPaint.setColor(Color.BLACK);
        mStackItemTextPaint.setTextSize(55);

        PropertyValuesHolder mPropertyTranslateY = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y, (int) (-mMaxHeightStack - 100), 0);
        PropertyValuesHolder mPropertyAlphaPush = PropertyValuesHolder.ofInt(PROPERTY_ALPHA, 0, 255);

        mAnimator.setValues(mPropertyTranslateY, mPropertyAlphaPush);
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateY = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y);
                mAlpha = (int) animation.getAnimatedValue(PROPERTY_ALPHA);
                invalidate();
            }
        });
    }

    /**
     * removes a element from the stack
     */
    protected void pop() {
        mStack.removeElementAt(mStack.size() - 1);
        mStackNumbers.removeElementAt(mStackNumbers.size() - 1);

        if (mMaxHeightStack > (mMaxWidthStack / 4) * (mScale * 1.2f) * mStack.size()) {
            mScale = mScale * 1.2f;
            if (mScale > 1) {
                mScale = 1;
            }
        }
        changeBoxSize(getMax(), false);

        invalidate();
    }

    /**
     * checks in what range the current max value is and changes the scale value for the boxes accordingly
     *
     * @param value the maximum value of the current stack
     * @param push  true if a value was pushed, false if it was poped
     */
    private void changeBoxSize(int value, boolean push) {
        int val;
        if (value > 999999999) {
            val = 6;
        } else if (value > 99999999) {
            val = 5;
        } else if (value > 9999999) {
            val = 4;
        } else if (value > 999999) {
            val = 3;
        } else if (value > 99999) {
            val = 2;
        } else if (value > 9999) {
            mResize = 40 * 2;
            return;
        } else if (value > 999) {
            mResize = 40;
            return;
        } else {
            mResize = 1;
            return;
        }

        if (!push) {
            val++;
        }
        mResize = 40 * val;
    }


    /**
     * adds a element to the stack
     */
    protected void push(int value) {

        Rect r = new Rect();
        mStack.add(r);
        mStackNumbers.add(value);

        if (mMaxHeightStack <= (mMaxWidthStack / 4) * mScale * mStack.size()) {
            mScale = mScale / 1.2f;
        }

        changeBoxSize(getMax(), true);

        mAnimator.start();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        // size of parent of this view
        mMaxHeightStack = (float) h - ypad - 6;
        mMaxWidthStack = (float) w - xpad - 6;
        mScale = 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw" + mMaxHeightStack);
        for (int i = 0; i < mStack.size(); i++) {

            // only animate the last object - translate box + text and change the alpha
            if (i == mStack.size() - 1) {
                mStack.get(i).top = (int) (mMaxHeightStack - ((mMaxWidthStack / 4) + (mMaxWidthStack / 4 * i)) * mScale) + mTranslateY;
                mStackItemTextPaint.setAlpha(mAlpha);
                mStackItemPaint.setAlpha(mAlpha);
            } else {
                mStack.get(i).top = (int) (mMaxHeightStack - ((mMaxWidthStack / 4) + (mMaxWidthStack / 4 * i)) * mScale);
                mStackItemTextPaint.setAlpha(255);
                mStackItemPaint.setAlpha(255);
            }
            // save the size of the text ("box" around the text) in mBounds
            mStackItemTextPaint.getTextBounds(mStackNumbers.get(i).toString(), 0, mStackNumbers.get(i).toString().length(), mBounds);

            // set Stack item size
            mStack.get(i).left = (int) ((mMaxWidthStack / 2) - (mMaxWidthStack / 8) - (mResize / 2));
            mStack.get(i).right = (int) (mStack.get(i).left + (mMaxWidthStack / 4) + mResize);
            mStack.get(i).bottom = (int) (mStack.get(i).top + ((mMaxWidthStack / 4) * mScale) - 10);

            // get BoundingBox from Text & draw Text + StackItem
            canvas.drawText(mStackNumbers.get(i).toString(), mStack.get(i).exactCenterX() - mBounds.exactCenterX(), (mStack.get(i).exactCenterY() - mBounds.exactCenterY()), mStackItemTextPaint);
            canvas.drawRect(mStack.get(i), mStackItemPaint);
        }
    }
}
