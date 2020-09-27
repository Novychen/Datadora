package at.fhooe.mc.datadora.Queue;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.R;

/**
 * Class QueueView, implements the animation for the Queue in normal mode
 */
public class QueueView extends View {

    private static final String PROPERTY_TRANSLATE_Y_ENQUEUE = "PROPERTY_TRANSLATE_Y_ENQUEUE";
    private static final String PROPERTY_TRANSLATE_Y_DEQUEUE = "PROPERTY_TRANSLATE_Y_DEQUEUE";
    private static final String PROPERTY_TRANSLATE_Y_RANDOM = "PROPERTY_TRANSLATE_Y_RANDOM";
    private static final String PROPERTY_ALPHA_ENQUEUE = "PROPERTY_ALPHA_ENQUEUE";
    private static final String PROPERTY_ALPHA_DEQUEUE = "PROPERTY_ALPHA_DEQUEUE";
    private static final String PROPERTY_ALPHA_RANDOM = "PROPERTY_ALPHA_RANDOM";

    private static final String TAG = "QueueView : ";

    // the current operation
    private Operation mCurrentOperation;

    //Check what the current operation is
    private enum Operation{
        DEQUEUE,
        ENQUEUE,
        PEEK,
        CLEAR,
        RANDOM,
        SIZE,
        SAVE
    }

    // int that counts how often the operator enqueued was used (by operator random)
    private int mPositionAnimation;

    // int that specifies the radius of the corners of the drawn rectangles
    private int mRadius = 4;

    // Vector that gets the random generated Queue
    private Vector<Integer> mRandomQueue = new Vector<>();

    // Animator for the last enqueued element
    private ValueAnimator mAnimatorEnqueue = new ValueAnimator();

    // Animator for the last dequeued element
    private ValueAnimator mAnimatorDequeue = new ValueAnimator();

    // Animator for the operation peek (for the area of one item)
    private ValueAnimator mAnimatorPeekArea = new ValueAnimator();

    // Animator for the operation peek (for the text of one item)
    private ValueAnimator mAnimatorPeekText = new ValueAnimator();

    // Animator for the operation random
    private ValueAnimator mAnimatorRandom = new ValueAnimator();

    // Vector that contains all Rects, that are drawn
    private Vector<RectF> mQueue = new Vector<>();

    // Vector that contains all Integers, that are drawn / the user put in
    private Vector<Integer> mQueueNumbers = new Vector<>();

    // Vector for the animation of the operation random
    private Vector<Integer> mQueueAnimation = new Vector<>();

    // Rect in order to save the TextBounds from the current number
    private Rect mBounds = new Rect();

    // factor for the change of height of the Queue item boxes, when the Queue is too high
    private float mScale = 1;

    // the current translation on the y-axis - used for the enqueue animation
    private int mTranslateYEnqueue;

    // the current translation on the y-axis - used for the dequeue animation
    private int mTranslateYDequeue;

    // the current translation on the y-axis - used for the random animation
    private int mTranslateYRandom;

    // the current alpha value - used for the enqueue animation
    private int mAlphaEnqueue;

    // the current alpha value - used for the dequeue animation
    private int mAlphaDequeue;

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

    private Paint mQueueItemPaint = new Paint();
    private Paint mQueueItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // current width of the QueueView within the layout
    private float mMaxWidthQueue;

    // current height of the QueueView within the layout
    private float mMaxHeightQueue;

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

    protected Vector<Integer> getQueueNumbers(){
        return mQueueNumbers;
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
        mCurrentOperation = Operation.PEEK;
        mAnimatorPeekText.start();
        mAnimatorPeekArea.start();
        invalidate();
    }

    /**
     * clears the queue
     */
    protected void clear() {
        mCurrentOperation = Operation.CLEAR;
        reScaleUndo();
        setUpDequeueAnimation();
        mAnimatorDequeue.setDuration(200);
        mAnimatorDequeue.setRepeatCount(mQueueNumbers.size() -1);
        mAnimatorDequeue.start();
    }

    /**
     * creates a random queue
     */
    protected void random(Vector<Integer> _queue) {
        mCurrentOperation = Operation.RANDOM;
        mQueueNumbers.clear();
        mQueue.clear();
        mQueueAnimation.clear();
        mPositionAnimation = 0;
        mQueueAnimation.addAll(_queue);
        mAnimatorRandom.setRepeatCount(_queue.size() - 1);
        mAnimatorRandom.start();
        reScaleUndo();
    }

    /**
     * removes a element from the Queue
     */
    protected void dequeue() {
        mCurrentOperation = Operation.DEQUEUE;
        reScaleUndo();
        setUpDequeueAnimation();
        mAnimatorDequeue.setDuration(700);
        mAnimatorDequeue.setRepeatCount(0);
        mAnimatorDequeue.start();
    }

    private void setUpDequeueAnimation() {
        PropertyValuesHolder propertyTranslateYDequeue = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DEQUEUE, 0, (int)  (-(mMaxWidthQueue / 4 ) * mScale));
        PropertyValuesHolder propertyAlphaDequeue = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DEQUEUE, 255, 0);

        mAnimatorDequeue.setValues(propertyTranslateYDequeue, propertyAlphaDequeue);
        mAnimatorDequeue.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDequeue.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYDequeue = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DEQUEUE);
                mAlphaDequeue = (int) animation.getAnimatedValue(PROPERTY_ALPHA_DEQUEUE);
                invalidate();
            }
        });

        mAnimatorDequeue.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(mCurrentOperation == Operation.CLEAR){
                    mQueueNumbers.remove(0);
                    mQueue.remove(0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if(!mQueueNumbers.isEmpty()) {
                    mQueueNumbers.remove(0);
                    mQueue.remove(0);
                }
            }
        });
    }

    /**
     * adds a element to the Queue
     */
    protected void enqueue(int _value) {
        mCurrentOperation = Operation.ENQUEUE;
        RectF r = new RectF();
        mQueue.add(r);
        mQueueNumbers.add(_value);
        mAnimatorEnqueue.start();
        reScaleUndo();
        reScale();
    }

    private void reScale() {
        while (mMaxHeightQueue <= (mMaxWidthQueue / 4) * mScale * mQueue.size()) {
            mScale = mScale / 1.2f;
        }
    }

    private void reScaleUndo() {
        while (mMaxHeightQueue > (mMaxWidthQueue / 4) * (mScale * 1.2f) * mQueue.size()) {
            mScale = mScale * 1.2f;
            if (mScale > 1) {
                mScale = 1;
                break;
            }
        }
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
        reScale();
        setUpAnimation();

        //Visualize the vector in the Shared Preferences
        Vector<Integer> v = mQueueActivity.loadFromSave();
        if(v != null) {
            for (int i = 0; i < v.size(); i++) {
                mQueueNumbers.add(v.get(i));
                mQueue.add(new RectF());
            }
            mCurrentOperation = Operation.SAVE;
            reScale();
        }
    }

    private void setUpAnimation(){

        PropertyValuesHolder propertyTranslateYEnqueue = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_ENQUEUE, -200, 0);
        PropertyValuesHolder propertyAlphaEnqueue = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_ENQUEUE, 0, 255);

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
                mQueueNumbers.add(mQueueAnimation.get(mPositionAnimation));
                mQueue.add(new RectF());
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
                mQueueNumbers.add(mQueueAnimation.get(mPositionAnimation));
                mQueue.add(new RectF());
                mPositionAnimation++;
            }
        });
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);

        for (int i = 0; i < mQueueNumbers.size(); i++) {

            mQueue.get(i).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * i)) * mScale);
            mQueueItemTextPaint.setAlpha(255);
            mQueueItemPaint.setAlpha(255);
            mQueueItemTextPaint.setColor(mOnSurfaceColor);
            mQueueItemPaint.setColor(mPrimaryColor);

            animateOperation(_canvas, i);
        }

        if(mCurrentOperation == Operation.DEQUEUE && !mAnimatorDequeue.isRunning()){
            mQueueActivity.setPressedDequeue(false);
            mQueueNumbers.remove(0);
            mQueue.remove(0);

        } else if (mCurrentOperation == Operation.RANDOM && !mAnimatorRandom.isRunning()){
            mQueueActivity.setPressedRandom(false);
        }
    }

    /**
     * draws the get animation all operations
     * @param _canvas canvas on which the animation is painted
     * @param _pos the current position the loop from the onDraw is in
     */
    private void animateOperation(Canvas _canvas, int _pos){
        switch (mCurrentOperation){
            case ENQUEUE:{
                if (_pos == mQueue.size() - 1) {
                    mQueue.get(_pos).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * _pos)) * mScale) + mTranslateYEnqueue;
                    mQueueItemTextPaint.setAlpha(mAlphaEnqueue);
                    mQueueItemPaint.setAlpha(mAlphaEnqueue);
                }
            } break;
            case CLEAR:
            case DEQUEUE:{
                if(_pos == 0){
                    mQueue.get(_pos).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * _pos)) * mScale) - mTranslateYDequeue;
                    mQueueItemTextPaint.setAlpha(mAlphaDequeue);
                    mQueueItemPaint.setAlpha(mAlphaDequeue);
                } else {
                    mQueue.get(_pos).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * _pos)) * mScale) - mTranslateYDequeue;
                    mQueueItemTextPaint.setAlpha(255);
                    mQueueItemPaint.setAlpha(255);
                }

            } break;
            case PEEK:{
                if(_pos == mQueue.size() - 1){
                    mQueue.get(_pos).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * _pos)) * mScale);
                    mQueueItemPaint.setColor(mColorAreaPeek);
                    mQueueItemPaint.setStyle(Paint.Style.FILL);
                    _canvas.drawRoundRect(mQueue.get(_pos), mRadius, mRadius, mQueueItemPaint);
                    mQueueItemPaint.setColor(mPrimaryColor);
                    mQueueItemPaint.setStyle(Paint.Style.STROKE);
                    mQueueItemTextPaint.setColor(mColorTextPeek);
                }

            } break;
            case RANDOM:{
                if (_pos == mQueue.size() - 1){
                    mQueue.get(_pos).top = (int) (mMaxHeightQueue - ((mMaxWidthQueue / 4) + (mMaxWidthQueue / 4 * _pos)) * mScale) + mTranslateYRandom;
                    mQueueItemTextPaint.setAlpha(mAlphaRandom);
                    mQueueItemPaint.setAlpha(mAlphaRandom);
                }

            } break;
        }

        // save the size of the text ("box" around the text) in mBounds
        mQueueItemTextPaint.getTextBounds(mQueueNumbers.get(_pos).toString(), 0, mQueueNumbers.get(_pos).toString().length(), mBounds);

        // set Queue item size
        // factor for the change of width of the Queue item boxes, when the number is to big for the current width
        int mResize = 1;
        mQueue.get(_pos).left = (int) ((mMaxWidthQueue / 2) - (mMaxWidthQueue / 8) - (mResize / 2));
        mQueue.get(_pos).right = (int) (mQueue.get(_pos).left + (mMaxWidthQueue / 4) + mResize);
        mQueue.get(_pos).bottom = (int) (mQueue.get(_pos).top + ((mMaxWidthQueue / 4) * mScale) - 10);

        // get BoundingBox from Text & draw Text + QueueItem
        _canvas.drawText(mQueueNumbers.get(_pos).toString(), getExactCenterX(mQueue.get(_pos)) - mBounds.exactCenterX(),
                (getExactCenterY(mQueue.get(_pos)) - mBounds.exactCenterY()), mQueueItemTextPaint);
        _canvas.drawRoundRect(mQueue.get(_pos), mRadius, mRadius, mQueueItemPaint);
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
