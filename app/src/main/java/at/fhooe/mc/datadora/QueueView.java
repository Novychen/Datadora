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
public class QueueView extends View {

    private static final String PROPERTY_TRANSLATE_X = "PROPERTY_TRANSLATE_X";
    private static final String PROPERTY_TRANSLATE_Y = "PROPERTY_TRANSLATE_Y";
    private static final String PROPERTY_ALPHA = "PROPERTY_ALPHA";
    private static final String TAG = "QueueView :: ";

    // Animator for the enqueued element
    ValueAnimator mAnimatorEnqueue = new ValueAnimator();

    // Animator for the dequeued element
    ValueAnimator mAnimatorDequeue = new ValueAnimator();

    // Vector that contains all Rects, that are drawn
    Vector<Rect> mQueue = new Vector<>();

    // Vector that contains all Integers, that are drawn / the user put in
    Vector<Integer> mQueueNumbers = new Vector<>();

    // Rect in order to save the TextBounds from the current number
    Rect mBounds = new Rect();

    // factor for the change of height of the stack item boxes, when the stack is too high
    float mScale = 1;

    // factor for the change of width of the stack item boxes, when the number is to big for the current width
    int mResize = 1;

    // the current translation on the x-axis - used for the animation
    int mTranslateX;

    // the current translation on the y-axis - used for the animation
    int mTranslateY;

    // the current alpha value - used for the animation
    int mAlpha;

    boolean mDequeued;
    Paint mQueueItemPaint = new Paint();
    Paint mQueueItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // current width of the StackView within the layout
    float mMaxWidthQueue;

    // current height of the StackView within the layout
    float mMaxHeightQueue;


    public QueueView(Context context) {
        super(context);
        init();
    }

    public QueueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QueueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public QueueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        for (int i : mQueueNumbers) {
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
        mQueueItemPaint.setColor(getResources().getColor(R.color.primaryColor, this.getContext().getTheme()));
        mQueueItemPaint.setStyle(Paint.Style.STROKE);
        mQueueItemPaint.setStrokeWidth(6);

        mQueueItemTextPaint.setColor(Color.BLACK);
        mQueueItemTextPaint.setTextSize(55);

        PropertyValuesHolder propertyTranslateX = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_X, (int) (-mMaxWidthQueue - 100), 0);
        PropertyValuesHolder propertyTranslateY = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y, (int) (-mMaxHeightQueue - 100), 0);
        PropertyValuesHolder propertyAlphaPush = PropertyValuesHolder.ofInt(PROPERTY_ALPHA, 0, 255);

        mAnimatorDequeue.setValues(propertyTranslateX);
        mAnimatorDequeue.setDuration(1000);
        mAnimatorDequeue.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDequeue.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateX = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_X);
                invalidate();
            }
        });

        mAnimatorEnqueue.setValues(propertyTranslateY, propertyAlphaPush);
        mAnimatorEnqueue.setDuration(1000);
        mAnimatorEnqueue.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorEnqueue.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
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
    protected void dequeue() {
        mDequeued = true;
        mAnimatorDequeue.start();
        invalidate();

        mQueue.removeElementAt(0);
        mQueueNumbers.removeElementAt(0);

        if (mMaxHeightQueue > (mMaxWidthQueue / 4) * (mScale * 1.2f) * mQueue.size()) {
            mScale = mScale * 1.2f;
            if (mScale > 1) {
                mScale = 1;
            }
        }
        changeBoxSize(getMax(), false);
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
    protected void enqueue(int value) {

        Rect r = new Rect();
        mQueue.add(r);
        mQueueNumbers.add(value);

        if (mMaxHeightQueue <= (mMaxWidthQueue / 4) * mScale * mQueue.size()) {
            mScale = mScale / 1.2f;
        }

        changeBoxSize(getMax(), true);
        mDequeued = false;
        mAnimatorEnqueue.start();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        // size of parent of this view
        mMaxHeightQueue = (float) h - ypad - 6;
        mMaxWidthQueue = (float) w - xpad - 6;
        mScale = 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mQueue.size(); i++) {

            // only animate the last object - translate box + text and change the alpha
            if (i == mQueue.size() - 1 && !mDequeued) {
                mQueue.get(i).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * i)) * mScale) + mTranslateY;
                mQueue.get(i).left = (int) ((mMaxWidthQueue / 2) - (mMaxWidthQueue / 8) - (mResize / 2));
                mQueueItemTextPaint.setAlpha(mAlpha);
                mQueueItemPaint.setAlpha(mAlpha);
            } else if (i == 0 && mDequeued) {
                mQueue.get(i).left = (int) ((mMaxWidthQueue / 2) - (mMaxWidthQueue / 8) - (mResize / 2) - mTranslateX);
            } else {
                mQueue.get(i).left = (int) ((mMaxWidthQueue / 2) - (mMaxWidthQueue / 8) - (mResize / 2));
                mQueue.get(i).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * i)) * mScale);
                mQueueItemTextPaint.setAlpha(255);
                mQueueItemPaint.setAlpha(255);
            }
            // save the size of the text ("box" around the text) in mBounds
            mQueueItemTextPaint.getTextBounds(mQueueNumbers.get(i).toString(), 0, mQueueNumbers.get(i).toString().length(), mBounds);

            // set Stack item size
            mQueue.get(i).right = (int) (mQueue.get(i).left + (mMaxWidthQueue / 4) + mResize);
            mQueue.get(i).bottom = (int) (mQueue.get(i).top + ((mMaxWidthQueue / 4) * mScale) - 10);

            // get BoundingBox from Text & draw Text + StackItem
            canvas.drawText(mQueueNumbers.get(i).toString(), mQueue.get(i).exactCenterX() - mBounds.exactCenterX(), (mQueue.get(i).exactCenterY() - mBounds.exactCenterY()), mQueueItemTextPaint);
            canvas.drawRect(mQueue.get(i), mQueueItemPaint);
        }
    }
}
