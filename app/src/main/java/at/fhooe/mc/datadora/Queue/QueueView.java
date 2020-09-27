package at.fhooe.mc.datadora.Queue;

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
import at.fhooe.mc.datadora.Stack.StackView;

/**
 * Class QueueView, implements the animation for the Queue in normal mode
 */
public class QueueView extends View {

    private static final String PROPERTY_TRANSLATE_Y_ENQUEUE = "PROPERTY_TRANSLATE_Y_ENQUEUE";
    private static final String PROPERTY_TRANSLATE_Y_DEQUEUE = "PROPERTY_TRANSLATE_Y_DEQUEUE";
    private static final String PROPERTY_ALPHA_ENQUEUE = "PROPERTY_ALPHA_ENQUEUE";
    private static final String PROPERTY_ALPHA_DEQUEUE = "PROPERTY_ALPHA_DEQUEUE";

    private static final String TAG = "QueueView : ";

    // Booleans that check which is the current operation
    boolean mDequeue, mEnqueue, mPeek, mClear, mRandom;

    // int that counts how often the operator enqueued was used (by operator random)
    int mPosition;

    // int that specifies the radius of the corners of the drawn rectangles
    int mRadius = 4;

    // Vector that gets the random generated Queue
    Vector<Integer> mRandomQueue = new Vector<>();

    // Animator for the last enqueued element
    ValueAnimator mAnimatorEnqueue = new ValueAnimator();

    // Animator for the last dequeued element
    ValueAnimator mAnimatorDequeue = new ValueAnimator();

    // Animator for the operation peek (for the area of one item)
    ValueAnimator mAnimatorPeekArea = new ValueAnimator();

    // Animator for the operation peek (for the text of one item)
    ValueAnimator mAnimatorPeekText = new ValueAnimator();

    // PropertyValuesHolder for the move animation of the operation dequeue
    PropertyValuesHolder mPropertyTranslateYDequeue;

    // PropertyValuesHolder for the alpha animation of the operation dequeue
    PropertyValuesHolder mPropertyAlphaDequeue;

    // Vector that contains all Rects, that are drawn
    Vector<RectF> mQueue = new Vector<>();

    // Vector that contains all Integers, that are drawn / the user put in
    Vector<Integer> mQueueNumbers = new Vector<>();

    // Rect in order to save the TextBounds from the current number
    Rect mBounds = new Rect();

    // factor for the change of height of the Queue item boxes, when the Queue is too high
    float mScale = 1;

    // factor for the change of width of the Queue item boxes, when the number is to big for the current width
    int mResize = 1;

    // the current translation on the y-axis - used for the enqueue animation
    int mTranslateYEnqueue;

    // the current translation on the y-axis - used for the dequeue animation
    int mTranslateYDequeue;

    // the current alpha value - used for the enqueue animation
    int mAlphaEnqueue;

    // the current alpha value - used for the dequeue animation
    int mAlphaDequeue;

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

    Paint mQueueItemPaint = new Paint();
    Paint mQueueItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // current width of the QueueView within the layout
    float mMaxWidthQueue;

    // current height of the QueueView within the layout
    float mMaxHeightQueue;

    private QueueActivity mQueueActivity;

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

    /**
     * Initializes the key components such as Paint
     */
    private void init() {
        mQueueItemPaint.setColor(mPrimaryColor);
        mQueueItemPaint.setStyle(Paint.Style.STROKE);
        mQueueItemPaint.setStrokeWidth(6);

        mQueueItemTextPaint.setColor(mOnSurfaceColor);
        mQueueItemTextPaint.setTextSize(55);
    }

    protected void setActivity(QueueActivity _activity) {
        mQueueActivity = _activity;
    }

    /**
     * marks the latest element of the queue
     */
    protected void peek() {
        mAnimatorPeekText.start();
        mAnimatorPeekArea.start();
        mPeek = true;
        mClear = false;
        mRandom = false;
        mDequeue = false;
        mEnqueue = false;
        invalidate();
    }

    /**
     * clears the queue
     */
    protected void clear() {
        mPeek = false;
        mClear = true;
        mRandom = false;
        mDequeue = true;
        mEnqueue = false;
        mAnimatorDequeue.setDuration(200);
        mAnimatorDequeue.start();
    }

    /**
     * creates a random queue
     */
    protected void random(Vector<Integer> _queue) {
        mQueueNumbers.clear();
        mQueue.clear();
        mPeek = false;
        mClear = false;
        mRandom = true;
        mDequeue = false;
        mEnqueue = false;
        mRandomQueue = _queue;
        mAnimatorEnqueue.setDuration(350);
        enqueue(_queue.get(mPosition));
        mPosition++;
    }

    /**
     * prepares the view for the Dequeue
     */
    protected void preDequeue() {
        mPeek = false;
        mClear = false;
        mRandom = false;
        mDequeue = true;
        mEnqueue = false;
        mAnimatorDequeue.start();
    }

    /**
     * removes a element from the Queue
     */
    private void dequeue() {
        mQueue.removeElementAt(0);
        mQueueNumbers.removeElementAt(0);

        if (mMaxHeightQueue > (mMaxWidthQueue / 4) * (mScale * 1.2f) * mQueue.size()) {
            mScale = mScale * 1.2f;
            if (mScale > 1) {
                mScale = 1;
            }
        }
    }

    /**
     * adds a element to the Queue
     */
    protected void enqueue(int _value) {
        RectF r = new RectF();
        mQueue.add(r);
        mQueueNumbers.add(_value);

        if (mMaxHeightQueue <= (mMaxWidthQueue / 4) * mScale * mQueue.size()) {
            mScale = mScale / 1.2f;
        }
        mPeek = false;
        mClear = false;
        mDequeue = false;
        mEnqueue = true;
        mAnimatorEnqueue.start();
    }

    /**
     * checks in what range the current max value is and changes the scale value for the boxes accordingly
     *
     * @param _value the maximum _value of the current Queue
     * @param _enqueue  true if a _value was pushed, false if it was dequeued
     */
    private void changeBoxSize(int _value, boolean _enqueue) {
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

        if (!_enqueue) {
            val++;
        }
        mResize = 40 * val;

        mPropertyTranslateYDequeue = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DEQUEUE, 0, (int)  (-(mMaxWidthQueue / 4 ) * mScale));
        mAnimatorDequeue.setValues(mPropertyTranslateYDequeue, mPropertyAlphaDequeue);
    }

    @Override
    protected void onSizeChanged(int _w, int _h, int _oldW, int _oldH) {
        super.onSizeChanged(_w, _h, _oldW, _oldH);
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        // size of parent of this view
        mMaxHeightQueue = (float) _h - ypad - 6;
        mMaxWidthQueue = (float) _w - xpad - 6;
        mScale = 1;

        PropertyValuesHolder propertyTranslateYEnqueue = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_ENQUEUE, -200, 0);
        PropertyValuesHolder propertyAlphaEnqueue = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_ENQUEUE, 0, 255);


        mPropertyTranslateYDequeue = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DEQUEUE, 0, (int)  (-(mMaxWidthQueue / 4 ) * mScale));
        mPropertyAlphaDequeue = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DEQUEUE, 255, 0);

        mAnimatorEnqueue.setValues(propertyTranslateYEnqueue, propertyAlphaEnqueue);
        mAnimatorEnqueue.setDuration(700);
        mAnimatorEnqueue.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorEnqueue.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYEnqueue = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_ENQUEUE);
                mAlphaEnqueue = (int) animation.getAnimatedValue(PROPERTY_ALPHA_ENQUEUE);
                invalidate();
            }
        });

        mAnimatorDequeue.setValues(mPropertyTranslateYDequeue, mPropertyAlphaDequeue);
        mAnimatorDequeue.setDuration(700);
        mAnimatorDequeue.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDequeue.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYDequeue = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DEQUEUE);
                mAlphaDequeue = (int) animation.getAnimatedValue(PROPERTY_ALPHA_DEQUEUE);
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

        //Visualize the vector in the Shared Preferences
        Vector<Integer> v = mQueueActivity.loadFromSave();
        if(v != null) {
            for (int i = 0; i < v.size(); i++) {
                mQueueNumbers.add(v.get(i));
                mQueue.add(new RectF());
            }
            //mCurrentOperation = Operation.SAVE; TODO enum
            //TODO: last element not visalized properly
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        onDrawNormalMode(_canvas);
    }

    private void onDrawNormalMode(Canvas _canvas) {
        for (int i = 0; i < mQueueNumbers.size(); i++) {
            if(i == 0 && !mEnqueue){
                if (mDequeue) {
                    mQueue.get(i).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * i)) * mScale) - mTranslateYDequeue;
                    mQueueItemTextPaint.setAlpha(mAlphaDequeue);
                    mQueueItemPaint.setAlpha(mAlphaDequeue);

                }  else if(mPeek){
                    mQueue.get(i).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * i)) * mScale);
                    mQueueItemPaint.setColor(mColorAreaPeek);
                    mQueueItemPaint.setStyle(Paint.Style.FILL);
                    _canvas.drawRoundRect(mQueue.get(i), mRadius, mRadius, mQueueItemPaint);
                    mQueueItemPaint.setColor(mPrimaryColor);
                    mQueueItemPaint.setStyle(Paint.Style.STROKE);
                    mQueueItemTextPaint.setColor(mColorTextPeek);
                }
            } else if (i == mQueueNumbers.size() - 1) {  // only animate the last object - translate box + text and change the alpha
                if (mEnqueue) {
                    mQueue.get(i).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * i)) * mScale) + mTranslateYEnqueue;
                    mQueueItemTextPaint.setAlpha(mAlphaEnqueue);
                    mQueueItemPaint.setAlpha(mAlphaEnqueue);
                } else  if (mDequeue) {
                    mQueue.get(i).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * i)) * mScale) - mTranslateYDequeue;
                    mQueueItemTextPaint.setAlpha(255);
                    mQueueItemPaint.setAlpha(255);
                }
            } else {
                if (mDequeue) {
                    mQueue.get(i).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * i)) * mScale) - mTranslateYDequeue;
                } else {
                    mQueue.get(i).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * i)) * mScale);
                }
                mQueueItemTextPaint.setAlpha(255);
                mQueueItemPaint.setAlpha(255);
                mQueueItemTextPaint.setColor(mOnSurfaceColor);
                mQueueItemPaint.setColor(mPrimaryColor);
            }
            // save the size of the text ("box" around the text) in mBounds
            mQueueItemTextPaint.getTextBounds(mQueueNumbers.get(i).toString(), 0, mQueueNumbers.get(i).toString().length(), mBounds);

            // set Queue item size
            mQueue.get(i).left = (int) ((mMaxWidthQueue / 2) - (mMaxWidthQueue / 8) - (mResize / 2));
            mQueue.get(i).right = (int) (mQueue.get(i).left + (mMaxWidthQueue / 4) + mResize);
            mQueue.get(i).bottom = (int) (mQueue.get(i).top + ((mMaxWidthQueue / 4) * mScale) - 10);

            // get BoundingBox from Text & draw Text + QueueItem
            _canvas.drawText(mQueueNumbers.get(i).toString(), getExactCenterX(mQueue.get(i)) - mBounds.exactCenterX(), (getExactCenterY(mQueue.get(i)) - mBounds.exactCenterY()), mQueueItemTextPaint);
            _canvas.drawRoundRect(mQueue.get(i), mRadius, mRadius, mQueueItemPaint);
        }

        if (mDequeue && mAlphaDequeue == 0 && !mClear) { // Animation of dequeue is over -> remove element
            dequeue();
            mDequeue = false;
            mQueueActivity.setPressedDequeue(false);
        } else if (mClear && mQueueNumbers.isEmpty()) { // clear operation is finished
            mDequeue = false;
            mClear = false;
            mQueueActivity.showEmpty();
            mAnimatorDequeue.setDuration(700);
            mPosition = 0;
        } else if (mClear && mDequeue && mAlphaDequeue == 0) { // clear operation is ongoing - animation is finished - remove last element
            dequeue();
            mAnimatorDequeue.start();
        } else if (mRandom && mRandomQueue.size() == mQueueNumbers.size()) { // random operation is finished
            mRandom = false;
            mAnimatorEnqueue.setDuration(700);
            mQueueActivity.setPressedRandom(false);
            mPosition = 0;
        } else if (mRandom && mAlphaEnqueue == 255) { // random animation for element is finished - add next element
            enqueue(mRandomQueue.get(mPosition));
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
