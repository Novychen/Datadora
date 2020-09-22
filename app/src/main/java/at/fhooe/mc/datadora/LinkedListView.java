package at.fhooe.mc.datadora;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.Vector;

public class LinkedListView extends View {

    //TODO: Predecessor Animation
    //TODO: Successor Animation
    //TODO: App crashes when list empty & change in type
    //TODO: Type not tied to element
    //TODO: Sorted / Unsorted

    //TODO: put Size Button after random (?)

    private static final String TAG = "LinkedListView : ";
    private static final String PROPERTY_TRANSLATE_Y_APPEND = "PROPERTY_TRANSLATE_APPEND";
    private static final String PROPERTY_ALPHA_APPEND = "PROPERTY_ALPHA_APPEND";
    private static final String PROPERTY_TRANSLATE_Y_PREPEND = "PROPERTY_TRANSLATE_Y_PREPEND";
    private static final String PROPERTY_ALPHA_PREPEND = "PROPERTY_ALPHA_PREPEND";
    private static final String PROPERTY_TRANSLATE_Y_INSERT = "PROPERTY_TRANSLATE_Y_INSERT";
    private static final String PROPERTY_ALPHA_INSERT = "PROPERTY_ALPHA_INSERT";
    private static final String PROPERTY_TRANSLATE_Y_DELETE_AT = "PROPERTY_TRANSLATE_Y_DELETE_AT";
    private static final String PROPERTY_ALPHA_DELETE_AT = "PROPERTY_ALPHA_DELETE_AT";
    private static final String PROPERTY_TRANSLATE_Y_DELETE_LAST = "PROPERTY_TRANSLATE_Y_DELETE_LAST";
    private static final String PROPERTY_ALPHA_DELETE_LAST = "PROPERTY_ALPHA_DELETE_LAST";
    private static final String PROPERTY_TRANSLATE_Y_DELETE_FIRST = "PROPERTY_TRANSLATE_Y_DELETE_FIRST";
    private static final String PROPERTY_ALPHA_DELETE_FIRST = "PROPERTY_ALPHA_DELETE_FIRST";
    private static final String PROPERTY_TRANSLATE_Y_RANDOM = "PROPERTY_TRANSLATE_RANDOM";
    private static final String PROPERTY_ALPHA_RANDOM = "PROPERTY_ALPHA_RANDOM";

    Paint mItemPaint = new Paint();
    Paint mTypePaint = new Paint();
    Paint mItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPositionTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mTypeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Animator for the appended element
    private ValueAnimator mAnimatorAppend = new ValueAnimator();

    // Animator for the prepended element
    private ValueAnimator mAnimatorPrepend = new ValueAnimator();

    // Animator for the inserted element
    private  ValueAnimator mAnimatorInsert = new ValueAnimator();

    // Animator for the alpha value of the inserted element
    private ValueAnimator mAnimatorInsertAlpha = new ValueAnimator();

    // Animator for the deleteLast element
    private ValueAnimator mAnimatorDeleteFirst  = new ValueAnimator();

    // Animator for the deletedAt element
    private  ValueAnimator mAnimatorDeleteAt = new ValueAnimator();

    // Animator for the alpha value of the deletedAt element
    private ValueAnimator mAnimatorDeleteAtAlpha = new ValueAnimator();

    // Animator for the deleteLast element
    private ValueAnimator mAnimatorDeleteLast  = new ValueAnimator();

    // Animator for the get operations
    private ValueAnimator mAnimatorGetText  = new ValueAnimator();

    // Animator for the get operations
    private ValueAnimator mAnimatorGetArea  = new ValueAnimator();

    // Animator for the created random list
    private ValueAnimator mAnimatorRandom  = new ValueAnimator();

    // the current translation on the y-axis - used for the append animation
    private  int mTranslateYAppend;

    // the current alpha value - used for the append animation
    private int mAlphaAppend;

    // the current translation on the y-axis - used for the prepend animation
    private int mTranslateYPrepend;

    // the current alpha value - used for the prepend animation
    private int mAlphaPrepend;

    // the current translation on the y-axis - used for the insertAt animation
    private int mTranslateYInsert;

    // the current alpha value - used for the insertAt animation
    private int mAlphaInsert;

    // the current translation on the y-axis - used for the deleteFirst animation
    private int mTranslateYDeleteFirst;

    // the current alpha value - used for the deleteFirst animation
    private int mAlphaDeleteFirst;

    // the current translation on the y-axis - used for the deleteAt animation
    private int mTranslateYDeleteAt;

    // the current alpha value - used for the deleteAt animation
    private int mAlphaDeleteAt;

    // the current translation on the y-axis - used for the deleteLast animation
    private int mTranslateYDeleteLast;

    // the current alpha value - used for the deleteLast animation
    private int mAlphaDeleteLast;

    // the current color value - used for get animation (for the area of one item)
    int mColorAreaGet;

    // the current color value - used for get animation (for the text of one item)
    int mColorTextGet;

    // the current alpha value - used for the random animation
    private int mAlphaRandom;

    // the current translation on the y-axis - used for the random animation
    private int mTranslateYRandom;

    // the position where the element will be inserted with the insertAt operation
    private int mPosition;

    // the position for animation of random & size
    private int mPositionAnimation = 0;

    enum Operation {
        PREPEND,
        APPEND,
        INSERT_AT,
        CLEAR,
        DELETE_FIRST,
        DELETE_LAST,
        DELETE_AT,
        GET_SIZE,
        PREDECESSOR,
        SUCCESSOR,
        GET_FIRST,
        GET_LAST,
        GET_AT,
        RANDOM
    }

    enum Filter {
        SORTED,
        UNSORTED
    }

    enum Type {
        HEAD,
        TAIL,
        HEAD_TAIL
    }

    LinkedListActivity mActivity;

    // Vector that contains all Rects, that are drawn
    Vector<RectF> mLinkedList = new Vector<>();

    // Vector that contains all Integers, that are drawn / the user put in
    Vector<Integer> mLinkedListNumbers = new Vector<>();

    // Vector used for animation
    Vector<Integer> mLinkedListAnimation = new Vector<>();

    // Rect in order to save the TextBounds from the current number
    Rect mBounds = new Rect();

    // int that specifies the radius of the corners of the drawn rectangles
    int mRadius = 4;

    // factor for the change of height of the Queue item boxes, when the LinkedList is too high
    float mScale = 1;

    // factor for the change of width of the LinkedList item boxes, when the number is to big for the current width
    int mResize = 1;

    // the current primary color of the currently used theme
    int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    // current width of the LinkedListView within the layout
    float mMaxWidthLinkedList;

    // current height of the LinkedListView within the layout
    float mMaxHeightLinkedList;

    // the current operation
    Operation mCurrentOperation;

    // the current type
    Type mCurrentType;

    // the current filter (sorted or unsorted)
    Filter mCurrentFilter;

    // the RectF for head, tail or both
    RectF mTypeRect = new RectF();

    // First point of the triangle used in the background to display the type of the linked list
    Point mFirst = new Point();

    // Second point of the triangle used in the background to display the type of the linked list
    Point mSecond = new Point();

    // Third point of the triangle used in the background to display the type of the linked list
    Point mThird = new Point();

    // Path of the drawn triangle used in the background to display the type of the linked list
    Path mTriangle = new Path();

    public LinkedListView(Context context) {
        super(context);
        init();
    }

    public LinkedListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinkedListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initializes the key components such as Paint
     */
    private void init() {
        mItemPaint.setColor(mPrimaryColor);
        mItemPaint.setStyle(Paint.Style.STROKE);
        mItemPaint.setStrokeWidth(6);

        mTypePaint.setColor(mPrimaryColor);
        mTypePaint.setStyle(Paint.Style.FILL);

        mItemTextPaint.setColor(mOnSurfaceColor);
        mItemTextPaint.setTextSize(55);

        mTypeTextPaint.setColor(mOnPrimaryColor);
        mTypeTextPaint.setTextSize(40);

        mPositionTextPaint.setColor(mOnSurfaceColor);
        mPositionTextPaint.setTextSize(30);
/*
        if(loadFromSave()) {
            for (int i = 0; i < mLinkedListNumbers.size(); i++) {
                mLinkedList.add(new RectF());
            }
        }*/
    }

    protected void init(LinkedListActivity _activity) {
        mActivity = _activity;
    }

    protected void sorted(){
        mCurrentFilter = Filter.SORTED;
    }

    protected void unsorted(){
        mCurrentFilter = Filter.UNSORTED;
    }

    protected void head(){
        mCurrentType = Type.HEAD;
        invalidate();
    }

    protected void tail(){
        mCurrentType = Type.TAIL;
        invalidate();
    }

    protected void both(){
        mCurrentType = Type.HEAD_TAIL;
        invalidate();
    }

    protected void prepend(int _value){
        mCurrentOperation = Operation.PREPEND;

        RectF r = new RectF();
        mLinkedList.add(r);
        mLinkedListNumbers.add(0,_value);
        reScale();
        mAnimatorPrepend.start();
    }

    protected void append(int _value){
        mCurrentOperation = Operation.APPEND;
        RectF r = new RectF();
        mLinkedList.add(r);
        mLinkedListNumbers.add(_value);
        reScale();
        mAnimatorAppend.start();
    }

    protected void insertAt(int _value, int _pos){
        mCurrentOperation = Operation.INSERT_AT;

        RectF r = new RectF();
        mLinkedList.add(r);
        mLinkedListNumbers.add(_pos,_value);
        reScale();
        mPosition = _pos;
        mAnimatorInsert.start();
        mAnimatorInsertAlpha.start();
    }

    protected void deleteFirst() {
        mCurrentOperation = Operation.DELETE_FIRST;
        mAnimatorDeleteFirst.start();
        reScale();
    }

    protected void deleteLast() {
        mCurrentOperation = Operation.DELETE_LAST;
        mAnimatorDeleteLast.setDuration(700);
        mAnimatorDeleteLast.setRepeatCount(0);
        mAnimatorDeleteLast.start();
        reScale();
    }

    protected void deleteAt(int _pos) {
        mCurrentOperation = Operation.DELETE_AT;
        reScale();
        mPosition = _pos;
        mAnimatorDeleteAt.start();
        mAnimatorDeleteAtAlpha.start();
    }

    protected void clear(){
        mCurrentOperation = Operation.CLEAR;
        mAnimatorDeleteLast.setDuration(200);
        mAnimatorDeleteLast.setRepeatCount(mLinkedListNumbers.size()-1);
        mAnimatorDeleteLast.start();
        reScale();
    }

    //TODO gerald: the 3 getter operations, then predecessor and successor


    protected void predecessor(){
        mCurrentOperation = Operation.PREDECESSOR;
    }

    protected void successor(){
        mCurrentOperation = Operation.SUCCESSOR;
    }

    protected void getSize(){
        mCurrentOperation = Operation.GET_SIZE;
        mPositionAnimation = 0;
        mAnimatorGetArea.setRepeatCount(mLinkedListNumbers.size()-1);
        mAnimatorGetText.setRepeatCount(mLinkedListNumbers.size()-1);
        mAnimatorGetArea.setDuration(600);
        mAnimatorGetText.setDuration(600);

        mAnimatorGetArea.start();
        mAnimatorGetText.start();
    }

    protected void getFirst(){
        mCurrentOperation = Operation.GET_FIRST;
        mAnimatorGetText.setRepeatCount(0);
        mAnimatorGetArea.setRepeatCount(0);
        mAnimatorGetArea.start();
        mAnimatorGetText.start();
    }

    protected void getLast(){
        mCurrentOperation = Operation.GET_LAST;
        mAnimatorGetText.setRepeatCount(0);
        mAnimatorGetArea.setRepeatCount(0);
        mAnimatorGetArea.start();
        mAnimatorGetText.start();
    }

    protected void getAt(int _pos){
        mCurrentOperation = Operation.GET_AT;
        mPosition = _pos;
        mAnimatorGetArea.start();
        mAnimatorGetText.start();
    }

    protected void random(Vector<Integer> _list) {
        mLinkedListNumbers.clear();
        mLinkedList.clear();
        mLinkedListAnimation.clear();
        mPositionAnimation = 0;
        mLinkedListAnimation.addAll(_list);
        mCurrentOperation = Operation.RANDOM;
        mAnimatorRandom.setRepeatCount(_list.size() - 1);
        mAnimatorRandom.start();
    }

    private void reScale() {
        if (mMaxHeightLinkedList <= (mMaxWidthLinkedList / 4) * mScale * mLinkedList.size()) {
            mScale = mScale / 1.2f;
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int _w, int _h, int _oldW, int _oldH) {
        super.onSizeChanged(_w, _h, _oldW, _oldH);
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        // size of parent of this view
        mMaxHeightLinkedList = (float) _h - ypad - 6;
        mMaxWidthLinkedList = (float) _w - xpad - 6;
        mScale = 1;

        setUpAnimation();
        invalidate();
    }

    private void setUpAnimation(){
        PropertyValuesHolder propertyTranslateYAppend = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_APPEND, -200, 0);
        PropertyValuesHolder propertyAlphaAppend = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_APPEND, 0, 255);

        mAnimatorAppend.setValues(propertyTranslateYAppend, propertyAlphaAppend);
        mAnimatorAppend.setDuration(700);
        mAnimatorAppend.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorAppend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYAppend = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_APPEND);
                mAlphaAppend = (int) animation.getAnimatedValue(PROPERTY_ALPHA_APPEND);
                invalidate();
            }
        });

        int number = (int) (((mMaxWidthLinkedList / 4) * mScale) - 10);
        PropertyValuesHolder propertyTranslateYPrepend = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_PREPEND, -number, 0);
        PropertyValuesHolder propertyAlphaPrepend = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_PREPEND, 0, 255);

        mAnimatorPrepend.setValues(propertyTranslateYPrepend, propertyAlphaPrepend);
        mAnimatorPrepend.setDuration(700);
        mAnimatorPrepend.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorPrepend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYPrepend = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_PREPEND);
                mAlphaPrepend = (int) animation.getAnimatedValue(PROPERTY_ALPHA_PREPEND);
                invalidate();
            }
        });

        PropertyValuesHolder propertyTranslateYInsert = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_INSERT, -number, 0);
        PropertyValuesHolder propertyAlphaInsert = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_INSERT, 0, 255);

        mAnimatorInsert.setValues(propertyTranslateYInsert);
        mAnimatorInsert.setDuration(700);
        mAnimatorInsert.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorInsert.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYInsert = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_INSERT);
                invalidate();
            }
        });

        mAnimatorInsertAlpha.setValues(propertyAlphaInsert);
        mAnimatorInsertAlpha.setDuration(900);
        mAnimatorInsertAlpha.setInterpolator(new AccelerateInterpolator());
        mAnimatorInsertAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAlphaInsert = (int) animation.getAnimatedValue(PROPERTY_ALPHA_INSERT);
                invalidate();
            }
        });

        PropertyValuesHolder propertyTranslateYDeleteAt = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DELETE_AT, 0, -number - 10);
        PropertyValuesHolder propertyAlphaDeleteAt = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DELETE_AT, 255, 0);

        mAnimatorDeleteAt.setValues(propertyTranslateYDeleteAt);
        mAnimatorDeleteAt.setDuration(900);
        mAnimatorDeleteAt.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDeleteAt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYDeleteAt = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DELETE_AT);
                invalidate();
            }
        });

        mAnimatorDeleteAtAlpha.setValues(propertyAlphaDeleteAt);
        mAnimatorDeleteAtAlpha.setDuration(700);
        mAnimatorDeleteAtAlpha.setInterpolator(new AccelerateInterpolator());
        mAnimatorDeleteAtAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAlphaDeleteAt = (int) animation.getAnimatedValue(PROPERTY_ALPHA_DELETE_AT);
                invalidate();
            }
        });

        PropertyValuesHolder propertyTranslateYDeleteFirst = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DELETE_FIRST, 0, number + 10);
        PropertyValuesHolder propertyAlphaDeleteFirst = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DELETE_FIRST, 255, 0);

        mAnimatorDeleteFirst.setValues(propertyTranslateYDeleteFirst, propertyAlphaDeleteFirst);
        mAnimatorDeleteFirst.setDuration(700);
        mAnimatorDeleteFirst.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDeleteFirst.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYDeleteFirst = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DELETE_FIRST);
                mAlphaDeleteFirst = (int) animation.getAnimatedValue(PROPERTY_ALPHA_DELETE_FIRST);
                invalidate();
            }
        });

        PropertyValuesHolder propertyTranslateYDeleteLast = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DELETE_LAST, 0, -200);
        PropertyValuesHolder propertyAlphaDeleteLast = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DELETE_LAST, 255, 0);

        mAnimatorDeleteLast.setValues(propertyTranslateYDeleteLast, propertyAlphaDeleteLast);
        mAnimatorDeleteLast.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDeleteLast.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateYDeleteLast = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DELETE_LAST);
                mAlphaDeleteLast = (int) animation.getAnimatedValue(PROPERTY_ALPHA_DELETE_LAST);
                invalidate();
            }
        });

        mAnimatorDeleteLast.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mLinkedListNumbers.remove(mLinkedListNumbers.size() - 1);
                mLinkedList.remove(mLinkedList.size() - 1);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                mLinkedListNumbers.remove(mLinkedListNumbers.size() - 1);
                mLinkedList.remove(mLinkedList.size() - 1);
            }
        });

        mAnimatorGetArea = ValueAnimator.ofObject(new ArgbEvaluator(), mSurfaceColor, mPrimaryColor);
        mAnimatorGetArea.setDuration(500);
        mAnimatorGetArea.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorGetArea.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColorAreaGet = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        mAnimatorGetText = ValueAnimator.ofObject(new ArgbEvaluator(), mOnSurfaceColor, mOnPrimaryColor);
        mAnimatorGetText.setDuration(500);
        mAnimatorGetText.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorGetText.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColorTextGet = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        mAnimatorGetText.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                mPositionAnimation++;
                mColorTextGet = 0;
                mColorAreaGet = 0;
            }
        });

        PropertyValuesHolder propertyTranslateYRandom = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_RANDOM, -200, 0);
        PropertyValuesHolder propertyAlphaRandom = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_RANDOM, 0, 255);

        mAnimatorRandom.setValues(propertyTranslateYRandom, propertyAlphaRandom);
        mAnimatorRandom.setDuration(350);
        mAnimatorRandom.setInterpolator(new AccelerateInterpolator());
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
                mLinkedListNumbers.add(mLinkedListAnimation.get(mPositionAnimation));
                mLinkedList.add(new RectF());
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
                mLinkedListNumbers.add(mLinkedListAnimation.get(mPositionAnimation));
                mLinkedList.add(new RectF());
                mPositionAnimation++;
            }
        });
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);

        for(int i = 0; i < mLinkedListNumbers.size(); i++) {

            mLinkedList.get(i).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * i)) * mScale);
            mItemTextPaint.setAlpha(255);
            mItemPaint.setAlpha(255);
            mItemTextPaint.setColor(mOnSurfaceColor);
            mItemPaint.setColor(mPrimaryColor);

            animateOperation(_canvas, i);

            String type;
            if (mCurrentType == Type.HEAD && i == 0){
                type = getResources().getString(R.string.LinkedList_Activity_Type_Head);
                drawType(_canvas, 0, type);
            } else if (mCurrentType == Type.TAIL && i == mLinkedList.size() - 1) {
                type = getResources().getString(R.string.LinkedList_Activity_Type_Tail);
                drawType(_canvas, i, type);
            } else if (mCurrentType == Type.HEAD_TAIL) {
                if(i == 0) {
                    type = getResources().getString(R.string.LinkedList_Activity_Type_Head);
                    drawType(_canvas, 0, type);
                } else if(i == mLinkedList.size() - 1) {
                    type = getResources().getString(R.string.LinkedList_Activity_Type_Tail);
                    drawType(_canvas, mLinkedList.size() - 1, type);
                }
            }
        }

        if (mCurrentOperation == Operation.DELETE_AT && !mAnimatorDeleteAt.isRunning()) {
            mLinkedList.remove(mPosition);
            mLinkedListNumbers.remove(mPosition);
        } else if (mCurrentOperation == Operation.DELETE_LAST && !mAnimatorDeleteLast.isRunning()) {
            mLinkedList.remove(mLinkedList.size() - 1);
            mLinkedListNumbers.remove(mLinkedListNumbers.size() - 1);
        } else if (mCurrentOperation == Operation.DELETE_FIRST && !mAnimatorDeleteFirst.isRunning()) {
            mLinkedList.remove(0);
            mLinkedListNumbers.remove(0);
        } else if (mCurrentOperation == Operation.GET_SIZE && !mAnimatorGetText.isRunning()) {
            mItemPaint.setColor(mSurfaceColor);
            mItemPaint.setStyle(Paint.Style.FILL);
            _canvas.drawRoundRect(mLinkedList.get(mLinkedList.size() - 1), mRadius, mRadius, mItemPaint);
            mItemPaint.setStyle(Paint.Style.STROKE);
            mItemPaint.setColor(mPrimaryColor);
            mItemTextPaint.setColor(mOnSurfaceColor);
            _canvas.drawRoundRect(mLinkedList.get(mLinkedList.size() - 1), mRadius, mRadius, mItemPaint);
            int pos = mLinkedList.size() - 1;
            mItemTextPaint.getTextBounds(mLinkedListNumbers.get(pos).toString(), 0, mLinkedListNumbers.get(pos).toString().length(), mBounds);
            _canvas.drawText(mLinkedListNumbers.get(pos).toString(), getExactCenterX(mLinkedList.get(pos)) - mBounds.exactCenterX(), (getExactCenterY(mLinkedList.get(pos)) - mBounds.exactCenterY()), mItemTextPaint);
        }
    }

    /**
     * draws the head, tail or both of them
     * @param _canvas canvas on which the head and/or tail is painted
     */
    private void drawType(Canvas _canvas, int _pos, String _type) {
        mTypeTextPaint.getTextBounds(_type, 0, _type.length(), mBounds);
        int padding = 15;

       float topLeft = (getExactCenterY(mLinkedList.get(_pos)) - mBounds.exactCenterY()) + 9;

        mTypeRect.top = topLeft - mBounds.height() - padding;
        mTypeRect.bottom = mTypeRect.top + mBounds.height() + (padding * 2);
        mTypeRect.left = 45;
        mTypeRect.right = mTypeRect.left + mBounds.width() + 6 + (padding * 2);

        int y = (int) (mTypeRect.bottom - (mTypeRect.height() / 2));

        // setup triangle
        int width = 20;
        mFirst.set((int) mTypeRect.right, y - width/2);
        mSecond.set((int) mTypeRect.right + (width/2), y);
        mThird.set((int) mTypeRect.right, y + width/2);

        // draw triangle
        mTriangle.setFillType(Path.FillType.EVEN_ODD);
        mTriangle.moveTo(mFirst.x, mFirst.y);
        mTriangle.lineTo(mSecond.x, mSecond.y);
        mTriangle.lineTo(mThird.x, mThird.y);
        mTriangle.close();

        _canvas.drawPath(mTriangle, mTypePaint);
        mTriangle.reset();
        _canvas.drawRoundRect(mTypeRect, 10, 10, mTypePaint);
        _canvas.drawText(_type,mTypeRect.left + padding, topLeft , mTypeTextPaint);
    }


    /**
     * draws the get animation (for getFirst, getLast, getAt, Successor & Predecessor)
     * @param _canvas canvas on which the animation is painted
     * @param _pos the current position the loop from the onDraw is in
     */
    private void drawGet(Canvas _canvas, int _pos) {

        mItemPaint.setColor(mColorAreaGet);
        mItemTextPaint.setColor(mColorTextGet);

        mItemPaint.setStyle(Paint.Style.FILL);
        _canvas.drawRoundRect(mLinkedList.get(_pos), mRadius, mRadius, mItemPaint);

        mItemPaint.setStyle(Paint.Style.STROKE);
        mItemPaint.setColor(mPrimaryColor);
        _canvas.drawRoundRect(mLinkedList.get(_pos), mRadius, mRadius, mItemPaint);
    }

    /**
     * draws the get animation all operations
     * @param _canvas canvas on which the animation is painted
     * @param _pos the current position the loop from the onDraw is in
     */
    private void animateOperation(Canvas _canvas, int _pos) {
        switch (mCurrentOperation) {
            case PREPEND: {
                if(_pos == 0) {
                    mItemTextPaint.setAlpha(mAlphaPrepend);
                    mItemPaint.setAlpha(mAlphaPrepend);
                }
                mLinkedList.get(_pos).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * _pos)) * mScale) - mTranslateYPrepend;
            } break;
            case APPEND: {
                if(_pos == mLinkedList.size() - 1) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * _pos)) * mScale) + mTranslateYAppend;
                    mItemTextPaint.setAlpha(mAlphaAppend);
                    mItemPaint.setAlpha(mAlphaAppend);
                }
            } break;
            case INSERT_AT: {
                if (mPosition + 1 == mLinkedListNumbers.size()) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * _pos)) * mScale) ;
                }
                if (_pos == mPosition) {
                    mItemTextPaint.setAlpha(mAlphaInsert);
                    mItemPaint.setAlpha(mAlphaInsert);
                } else if (_pos > mPosition) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * _pos)) * mScale) - mTranslateYInsert;
                    mItemTextPaint.setAlpha(255);
                    mItemPaint.setAlpha(255);
                }
            } break;
            case DELETE_FIRST: {
                if(_pos == 0) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * _pos)) * mScale) + mTranslateYDeleteFirst;
                    mItemTextPaint.setAlpha(mAlphaDeleteFirst);
                    mItemPaint.setAlpha(mAlphaDeleteFirst);
                } else {
                    mLinkedList.get(_pos).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * _pos)) * mScale) + mTranslateYDeleteFirst;
                    mItemTextPaint.setAlpha(255);
                    mItemPaint.setAlpha(255);
                }
            } break;
            case DELETE_AT: {
                if (_pos == mPosition) {
                    mItemTextPaint.setAlpha(mAlphaDeleteAt);
                    mItemPaint.setAlpha(mAlphaDeleteAt);
                } else if (_pos > mPosition) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * _pos)) * mScale) - mTranslateYDeleteAt;
                    mItemTextPaint.setAlpha(255);
                    mItemPaint.setAlpha(255);
                }
            } break;
            case CLEAR :
            case DELETE_LAST: {
                if (_pos == mLinkedListNumbers.size() - 1) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * _pos)) * mScale) + mTranslateYDeleteLast;
                    mItemTextPaint.setAlpha(mAlphaDeleteLast);
                    mItemPaint.setAlpha(mAlphaDeleteLast);
                }
            } break;
            case PREDECESSOR: {

            } break;
            case SUCCESSOR: {

            } break;
            case GET_FIRST: { if (_pos == 0) { drawGet(_canvas, _pos); } } break;
            case GET_AT: { if (_pos == mPosition) { drawGet(_canvas, _pos); } } break;
            case GET_LAST: { if (_pos == mLinkedList.size() - 1) { drawGet(_canvas, _pos); } } break;
            case RANDOM: {
                if (_pos == mLinkedList.size() - 1) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * _pos)) * mScale) + mTranslateYRandom;
                    mItemTextPaint.setAlpha(mAlphaRandom);
                    mItemPaint.setAlpha(mAlphaRandom);
                }
            } break;
            case GET_SIZE: { if (_pos == mPositionAnimation) { drawGet(_canvas, _pos); } } break;
        }

        // save the size of the text ("box" around the text) in mBounds
        mItemTextPaint.getTextBounds(mLinkedListNumbers.get(_pos).toString(), 0, mLinkedListNumbers.get(_pos).toString().length(), mBounds);

        // set LinkedList item size
        mLinkedList.get(_pos).left = (int) ((mMaxWidthLinkedList / 1.5) - (mMaxWidthLinkedList / 8) - (mResize / 2));
        mLinkedList.get(_pos).right = (int) (mLinkedList.get(_pos).left + (mMaxWidthLinkedList / 4) + mResize);
        mLinkedList.get(_pos).bottom = (int) (mLinkedList.get(_pos).top + ((mMaxWidthLinkedList / 4) * mScale) - 10);

        // get BoundingBox from Text & draw Text + LinkedList item
        _canvas.drawRoundRect(mLinkedList.get(_pos), mRadius, mRadius, mItemPaint);
        _canvas.drawText(mLinkedListNumbers.get(_pos).toString(), getExactCenterX(mLinkedList.get(_pos)) - mBounds.exactCenterX(), (getExactCenterY(mLinkedList.get(_pos)) - mBounds.exactCenterY()), mItemTextPaint);

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
