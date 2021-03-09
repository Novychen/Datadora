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

import androidx.lifecycle.ViewModelProvider;
import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.RoomDatabase.DatadoraViewModel;
import at.fhooe.mc.datadora.RoomDatabase.QueueRoom;
import at.fhooe.mc.datadora.RoomDatabase.StackRoom;
import at.fhooe.mc.datadora.Stack.StackView;

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
        SAVE
    }

    // int that counts how often the operator enqueued was used (by operator random)
    private int mPositionAnimation;

    // Animator for the last enqueued element
    private final ValueAnimator mAnimatorEnqueue = new ValueAnimator();

    // Animator for the last dequeued element
    private final ValueAnimator mAnimatorDequeue = new ValueAnimator();

    // Animator for the operation peek (for the area of one item)
    private ValueAnimator mAnimatorPeekArea = new ValueAnimator();

    // Animator for the operation peek (for the text of one item)
    private ValueAnimator mAnimatorPeekText = new ValueAnimator();

    // Animator for the operation random
    private final ValueAnimator mAnimatorRandom = new ValueAnimator();

    // Vector that contains all Rects, that are drawn
    private final Vector<RectF> mQueue = new Vector<>();

    // Vector that contains all Integers, that are drawn / the user put in
    private final Vector<Integer> mQueueNumbers = new Vector<>();

    //Getter for the mQueueNumbers
    protected Vector<Integer> getQueueNumbers(){
        return mQueueNumbers;
    }

    //Room database setup
    private DatadoraViewModel mDatadoraViewModel;

    // Vector for the animation of the operation random
    private final Vector<Integer> mQueueAnimation = new Vector<>();

    // Rect in order to save the TextBounds from the current number
    private final Rect mBounds = new Rect();

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
    private final int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    private final int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    private final int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    private final int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    private final Paint mQueueItemPaint = new Paint();
    private final Paint mQueueItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
        mDatadoraViewModel = new ViewModelProvider(mQueueActivity).get(DatadoraViewModel.class);
    }

    /**
     * New entry in the queue table
     * @param value the value to be added into the database
     */
    private void sendInputQueueValuesToDatabase(int value){
        mDatadoraViewModel.insert(new QueueRoom(value));
    }


    /**
     * Loads the saved integers from the database
     * There is an observer that observes the LiveData from
     * the database and is notified when they change.
     */
    private void loadQueueFromDatabase(){

        mDatadoraViewModel.getmAllQueueValues().observe(mQueueActivity, queueRooms -> {

            if(mQueueNumbers.isEmpty() && mCurrentOperation != QueueView.Operation.RANDOM) {
                for (QueueRoom r : queueRooms) {
                    mQueue.add(new RectF());
                    mQueueNumbers.add(r.getVal());
                }
                Log.i(TAG, "QueueView: loadStackFromDatabase mQueueNumbers" + mQueueNumbers);
                Log.i(TAG, "QueueView: loadStackFromDatabase mQueueNumbers size was: " + mQueueNumbers.size());
            }
            reScale();
            invalidate();
        });
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
        mDatadoraViewModel.deleteAllQueueDatabaseEntries();
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

        mDatadoraViewModel.deleteAllQueueDatabaseEntries();
        reScaleUndo();

        for (int i = 0; i < _queue.size(); i++){
            sendInputQueueValuesToDatabase(_queue.elementAt(i));
        }

        mAnimatorRandom.setRepeatCount(_queue.size() - 1);
        mAnimatorRandom.start();

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
        sendInputQueueValuesToDatabase(_value);
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

        setUpAnimation();

        Log.i(TAG, "------onSizeChanged was called, mQueue and mQueueNumbers was cleared and vals loadedFromDatabase again-------");
        mQueue.clear();
        mQueueNumbers.clear();
        loadQueueFromDatabase();
        mCurrentOperation = Operation.SAVE;
        reScale();
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
            mDatadoraViewModel.deleteByIDQueue(mQueueNumbers.get(0)); //remove from database
            mQueueNumbers.remove(0);
            mQueue.remove(0);
            mCurrentOperation = Operation.SAVE;

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
        // int that specifies the radius of the corners of the drawn rectangles
        int mRadius = 4;
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
                if(_pos == 0){
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
