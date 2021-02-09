 package at.fhooe.mc.datadora.LinkedList;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.R;

public class LinkedListView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    //TODO: Tail & Head animation aus/einblenden statt "springen"
    //TODO: Animation von add und remove von den Pfeilen

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
    private static final String PROPERTY_DRAW_LINE = "PROPERTY_DRAW_LINE_RIGHT";


    private final Paint mItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTypePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPositionTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTypeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Animator for the appended element
    private final ValueAnimator mAnimatorAppend = new ValueAnimator();

    // Animator for the prepended element
    private final ValueAnimator mAnimatorPrepend = new ValueAnimator();

    // Animator for the inserted element
    private final ValueAnimator mAnimatorInsert = new ValueAnimator();

    // Animator for the alpha value of the inserted element
    private final ValueAnimator mAnimatorInsertAlpha = new ValueAnimator();

    // Animator for the deleteLast element
    private final ValueAnimator mAnimatorDeleteFirst  = new ValueAnimator();

    // Animator for the deletedAt element
    private final ValueAnimator mAnimatorDeleteAt = new ValueAnimator();

    // Animator for the alpha value of the deletedAt element
    private final ValueAnimator mAnimatorDeleteAtAlpha = new ValueAnimator();

    // Animator for the deleteLast element
    private final ValueAnimator mAnimatorDeleteLast = new ValueAnimator();

    // Animator for the get operations
    private ValueAnimator mAnimatorGetText = new ValueAnimator();

    // Animator for the get operations
    private ValueAnimator mAnimatorGetArea = new ValueAnimator();

    // Animator for the created random list
    private final ValueAnimator mAnimatorRandom = new ValueAnimator();

    // Animator for the drawing animation
    private final ValueAnimator mAnimatorDrawLine = new ValueAnimator();

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
    private int mColorAreaGet;

    // the current color value - used for get animation (for the text of one item)
    private int mColorTextGet;

    // the current alpha value - used for the random animation
    private int mAlphaRandom;

    // the current translation on the y-axis - used for the random animation
    private int mTranslateYRandom;

    // the current draw process - used for the drawing animation
    private int mDrawProcess;

    // checks if a list item was touched
    private boolean mTouched;

    // the position where the element will be inserted with the insertAt operation
    private int mPosition;

    // the position for animation of random & size
    private int mPositionAnimation = 0;

    private final Path mPath = new Path();

    enum Operation {
        PREPEND, APPEND, INSERT_AT,

        CLEAR, DELETE_FIRST, DELETE_LAST, DELETE_AT,

        GET_SIZE, PREDECESSOR, SUCCESSOR, GET_FIRST, GET_LAST, GET_AT,

        RANDOM, SAVE
    }

    enum Filter {SORTED, UNSORTED}

    enum Type {HEAD, TAIL, HEAD_TAIL}

    private LinkedListActivity mActivity;

    // Vector that contains all Rects, that are drawn
    private final Vector<RectF> mLinkedList = new Vector<>();

    // Vector that contains all Integers, that are drawn / the user put in
    private final Vector<Integer> mLinkedListNumbers = new Vector<>();

    // Vector used for animation
    private final Vector<Integer> mLinkedListAnimation = new Vector<>();

    // Rect in order to save the TextBounds from the current number
    private final Rect mBounds = new Rect();

    // int that specifies the radius of the corners of the drawn rectangles
    private final int mRadius = 4;

    // factor for the change of height of the Queue item boxes, when the LinkedList is too high
    private float mScale = 1;

    // factor for the change of width of the LinkedList item boxes, when the number is to big for the current width
    private final int mResize = 1;

    // the current primary color of the currently used theme
    private final int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current secondary color of the currently used theme
    private final int mSecondaryColor = getResources().getColor(R.color.primaryLightColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    private final int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    private final int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    private final int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    // current width of the LinkedListView within the layout
    private float mMaxWidth;

    // current height of the LinkedListView within the layout
    private float mMaxHeight;

    // the current operation
    private Operation mCurrentOperation;

    // the current type
    private Type mCurrentType;

    // the current filter (sorted or unsorted)
    private Filter mCurrentFilter;

    // the RectF for head, tail or both
    private final RectF mTypeRect = new RectF();

    // First point of the triangle used in the background to display the type of the linked list
    private final Point mFirst = new Point();

    // Second point of the triangle used in the background to display the type of the linked list
    private final Point mSecond = new Point();

    // Third point of the triangle used in the background to display the type of the linked list
    private final Point mThird = new Point();

    // Path of the drawn triangle used in the background to display the type of the linked list
    private final Path mTriangle = new Path();

    private final PathMeasure mPathMeasure = new PathMeasure();

    private boolean mDraw;

    private boolean mSwitch;

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
        mItemPaint.setStrokeCap(Paint.Cap.ROUND);
        mItemPaint.setStrokeWidth(6);

        mTypePaint.setColor(mPrimaryColor);
        mTypePaint.setStyle(Paint.Style.FILL);

        mItemTextPaint.setColor(mOnSurfaceColor);
        mItemTextPaint.setTextSize(55);

        mTypeTextPaint.setColor(mOnPrimaryColor);
        mTypeTextPaint.setTextSize(40);

        mPositionTextPaint.setColor(mOnSurfaceColor);
        mPositionTextPaint.setTextSize(30);
    }

    protected void setActivity(LinkedListActivity _activity) {
        mActivity = _activity;
    }

    public void setSwitch(boolean isChecked) {
        mDraw = !isChecked;
        mSwitch = isChecked;
        invalidate();
    }

    public Vector<Integer> getLinkedListNumbers() {
        return mLinkedListNumbers;
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
        mDraw = false;
    }

    protected void append(int _value){
        mCurrentOperation = Operation.APPEND;
        RectF r = new RectF();
        mLinkedList.add(r);
        mLinkedListNumbers.add(_value);
        reScale();
        mAnimatorAppend.start();
        mDraw = false;
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
        reScaleUndo();
    }

    protected void deleteLast() {
        mCurrentOperation = Operation.DELETE_LAST;
        mAnimatorDeleteLast.setDuration(700);
        mAnimatorDeleteLast.setRepeatCount(0);
        mAnimatorDeleteLast.start();
        reScaleUndo();
    }

    protected void deleteAt(int _pos) {
        mCurrentOperation = Operation.DELETE_AT;
        reScaleUndo();
        mPosition = _pos;
        mAnimatorDeleteAt.start();
        mAnimatorDeleteAtAlpha.start();
    }

    protected void clear(){
        mCurrentOperation = Operation.CLEAR;
        mAnimatorDeleteLast.setDuration(200);
        mAnimatorDeleteLast.setRepeatCount(mLinkedListNumbers.size()-1);
        mAnimatorDeleteLast.start();
        reScaleUndo();
    }


    protected void predecessor(){
        mCurrentOperation = Operation.PREDECESSOR;
        mPosition = 0;
    }

    protected void successor(){
        mCurrentOperation = Operation.SUCCESSOR;
        mPosition = 0;
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
        while (mMaxHeight <= (mMaxWidth / 4) * mScale * mLinkedList.size()) {
            mScale = mScale / 1.2f;
        }
    }

    private void reScaleUndo() {
        if (mMaxHeight > (mMaxWidth / 4) * (mScale * 1.2f) * mLinkedList.size()) {
            mScale = mScale * 1.2f;
            if (mScale > 1) {
                mScale = 1;
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
        mMaxHeight = (float) _h - ypad - 6;
        mMaxWidth = (float) _w - xpad - 6;
        mScale = 1;

        //Visualize the vector in the Shared Preferences
        Vector<Integer> v = mActivity.loadFromSave();
        if(v != null) {
            for (int i = 0; i < v.size(); i++) {
                mLinkedListNumbers.add(v.get(i));
                mLinkedList.add(new RectF());
            }
            mCurrentOperation = Operation.SAVE;
            reScale();
        }

        setUpAnimation();
        invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator _animation) {

        if(_animation == mAnimatorDrawLine){
            mDrawProcess = (int) _animation.getAnimatedValue(PROPERTY_DRAW_LINE);
        } else if(_animation == mAnimatorAppend) {
            mTranslateYAppend = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_APPEND);
            mAlphaAppend = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_APPEND);
        } else if(_animation == mAnimatorPrepend) {
            mTranslateYPrepend = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_PREPEND);
            mAlphaPrepend = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_PREPEND);
        } else if(_animation == mAnimatorInsert) {
            mTranslateYInsert = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_INSERT);
        } else if(_animation == mAnimatorInsertAlpha) {
            mAlphaInsert = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_INSERT);
        } else if(_animation == mAnimatorDeleteAt) {
            mTranslateYDeleteAt = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DELETE_AT);
        } else if(_animation == mAnimatorDeleteAtAlpha) {
            mAlphaDeleteAt = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_DELETE_AT);
        } else if(_animation == mAnimatorDeleteFirst) {
            mTranslateYDeleteFirst = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DELETE_FIRST);
            mAlphaDeleteFirst = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_DELETE_FIRST);
        } else if(_animation == mAnimatorDeleteLast) {
            mTranslateYDeleteLast = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_DELETE_LAST);
            mAlphaDeleteLast = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_DELETE_LAST);
        } else if(_animation == mAnimatorGetArea) {
            mColorAreaGet = (int) _animation.getAnimatedValue();
        } else if(_animation == mAnimatorGetText) {
            mColorTextGet = (int) _animation.getAnimatedValue();
        } else if(_animation == mAnimatorRandom) {
            mTranslateYRandom = (int) _animation.getAnimatedValue(PROPERTY_TRANSLATE_Y_RANDOM);
            mAlphaRandom = (int) _animation.getAnimatedValue(PROPERTY_ALPHA_RANDOM);
        }
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator _animation) {
        if(_animation == mAnimatorRandom) {
            mLinkedListNumbers.add(mLinkedListAnimation.get(mPositionAnimation));
            mLinkedList.add(new RectF());
            mPositionAnimation++;
        }
    }

    @Override
    public void onAnimationEnd(Animator _animation) {
        if(_animation == mAnimatorGetText) {
            if(mCurrentOperation == Operation.PREDECESSOR || mCurrentOperation == Operation.SUCCESSOR) {
                if (mCurrentOperation == Operation.PREDECESSOR && mPosition < 0) {
                    Toast.makeText(mActivity, R.string.LinkedList_Activity_Pre_Empty, Toast.LENGTH_SHORT).show();
                } else if (mCurrentOperation == Operation.SUCCESSOR && mPosition >= mLinkedListNumbers.size()) {
                    Toast.makeText(mActivity, R.string.LinkedList_Activity_Succ_Empty, Toast.LENGTH_SHORT).show();
                } else {
                    mActivity.getBinding().LinkedListActivityReturnValue.setText(String.format("%s", mLinkedListNumbers.get(mPosition).toString()));
                }
                mTouched = false;
            }
        } else if (_animation == mAnimatorDeleteLast) {
            if (mCurrentOperation == Operation.CLEAR) {
                mLinkedListNumbers.remove(mLinkedListNumbers.size() - 1);
                mLinkedList.remove(mLinkedList.size() - 1);
            }
            mActivity.setDelete(false);
        }
    }

    @Override
    public void onAnimationCancel(Animator _animation) {

    }

    @Override
    public void onAnimationRepeat(Animator _animation) {
        if(_animation == mAnimatorRandom) {
            mLinkedListNumbers.add(mLinkedListAnimation.get(mPositionAnimation));
            mLinkedList.add(new RectF());
            mPositionAnimation++;
        } else if(_animation == mAnimatorGetText) {
            if (mCurrentOperation == Operation.PREDECESSOR) { mPosition--;
            } else if (mCurrentOperation == Operation.SUCCESSOR) { mPosition++;
            } else { mPositionAnimation++; }

            mColorTextGet = 0;
            mColorAreaGet = 0;
        } else if(_animation == mAnimatorDeleteLast) {
            mLinkedListNumbers.remove(mLinkedListNumbers.size() - 1);
            mLinkedList.remove(mLinkedList.size() - 1);
            reScaleUndo();
        }
    }


    private void setUpAnimation(){
        PropertyValuesHolder propertyTranslateYAppend = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_APPEND, -200, 0);
        PropertyValuesHolder propertyAlphaAppend = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_APPEND, 0, 255);

        int number = (int) (((mMaxWidth / 4) * mScale) - 10);
        PropertyValuesHolder propertyTranslateYPrepend = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_PREPEND, -number, 0);
        PropertyValuesHolder propertyAlphaPrepend = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_PREPEND, 0, 255);

        PropertyValuesHolder propertyTranslateYInsert = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_INSERT, -number, 0);
        PropertyValuesHolder propertyAlphaInsert = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_INSERT, 0, 255);

        PropertyValuesHolder propertyTranslateYDeleteAt = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DELETE_AT, 0, -number - 10);
        PropertyValuesHolder propertyAlphaDeleteAt = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DELETE_AT, 255, 0);

        PropertyValuesHolder propertyTranslateYDeleteFirst = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DELETE_FIRST, 0, number + 10);
        PropertyValuesHolder propertyAlphaDeleteFirst = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DELETE_FIRST, 255, 0);

        PropertyValuesHolder propertyTranslateYDeleteLast = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_DELETE_LAST, 0, -200);
        PropertyValuesHolder propertyAlphaDeleteLast = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_DELETE_LAST, 255, 0);

        PropertyValuesHolder propertyTranslateYRandom = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_Y_RANDOM, -200, 0);
        PropertyValuesHolder propertyAlphaRandom = PropertyValuesHolder.ofInt(PROPERTY_ALPHA_RANDOM, 0, 255);

        mAnimatorAppend.setValues(propertyTranslateYAppend, propertyAlphaAppend);
        mAnimatorAppend.setDuration(700);
        mAnimatorAppend.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorAppend.addUpdateListener(this);

        mAnimatorPrepend.setValues(propertyTranslateYPrepend, propertyAlphaPrepend);
        mAnimatorPrepend.setDuration(700);
        mAnimatorPrepend.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorPrepend.addUpdateListener(this);

        mAnimatorInsert.setValues(propertyTranslateYInsert);
        mAnimatorInsert.setDuration(700);
        mAnimatorInsert.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorInsert.addUpdateListener(this);

        mAnimatorInsertAlpha.setValues(propertyAlphaInsert);
        mAnimatorInsertAlpha.setDuration(900);
        mAnimatorInsertAlpha.setInterpolator(new AccelerateInterpolator());
        mAnimatorInsertAlpha.addUpdateListener(this);

        mAnimatorDeleteAt.setValues(propertyTranslateYDeleteAt);
        mAnimatorDeleteAt.setDuration(900);
        mAnimatorDeleteAt.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDeleteAt.addUpdateListener(this);

        mAnimatorDeleteAtAlpha.setValues(propertyAlphaDeleteAt);
        mAnimatorDeleteAtAlpha.setDuration(700);
        mAnimatorDeleteAtAlpha.setInterpolator(new AccelerateInterpolator());
        mAnimatorDeleteAtAlpha.addUpdateListener(this);

        mAnimatorDeleteFirst.setValues(propertyTranslateYDeleteFirst, propertyAlphaDeleteFirst);
        mAnimatorDeleteFirst.setDuration(700);
        mAnimatorDeleteFirst.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDeleteFirst.addUpdateListener(this);

        mAnimatorDeleteLast.setValues(propertyTranslateYDeleteLast, propertyAlphaDeleteLast);
        mAnimatorDeleteLast.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorDeleteLast.addUpdateListener(this);
        mAnimatorDeleteLast.addListener(this);

        mAnimatorGetArea = ValueAnimator.ofObject(new ArgbEvaluator(), mSurfaceColor, mPrimaryColor);
        mAnimatorGetArea.setDuration(500);
        mAnimatorGetArea.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorGetArea.addUpdateListener(this);

        mAnimatorGetText = ValueAnimator.ofObject(new ArgbEvaluator(), mOnSurfaceColor, mOnPrimaryColor);
        mAnimatorGetText.setDuration(500);
        mAnimatorGetText.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorGetText.addUpdateListener(this);
        mAnimatorGetText.addListener(this);

        mAnimatorRandom.setValues(propertyTranslateYRandom, propertyAlphaRandom);
        mAnimatorRandom.setDuration(350);
        mAnimatorRandom.setInterpolator(new AccelerateInterpolator());
        mAnimatorRandom.addUpdateListener(this);
        mAnimatorRandom.addListener(this);
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);

        for(int i = 0; i < mLinkedListNumbers.size(); i++) {

            mLinkedList.get(i).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * i)) * mScale);
            mItemTextPaint.setAlpha(255);
            mItemPaint.setAlpha(255);
            mItemTextPaint.setColor(mOnSurfaceColor);
            mItemPaint.setColor(mPrimaryColor);

            animateOperation(_canvas, i);

            String type;
            if (i == 0 && (mCurrentType == Type.HEAD || mCurrentType == Type.HEAD_TAIL)){
                type = getResources().getString(R.string.LinkedList_Activity_Type_Head);
                drawType(_canvas, i, type);
            } else if (i == mLinkedList.size() - 1 && (mCurrentType == Type.TAIL || mCurrentType == Type.HEAD_TAIL)) {
                type = getResources().getString(R.string.LinkedList_Activity_Type_Tail);
                drawType(_canvas, i, type);
            }
        }

        if (mCurrentOperation == Operation.DELETE_AT && !mAnimatorDeleteAt.isRunning()) {
            mLinkedList.remove(mPosition);
            mLinkedListNumbers.remove(mPosition);
            mActivity.setDelete(false);
        } else if (mCurrentOperation == Operation.DELETE_FIRST && !mAnimatorDeleteFirst.isRunning()) {
            mLinkedList.remove(0);
            mLinkedListNumbers.remove(0);
            mActivity.setDelete(false);
        } else if(mCurrentOperation == Operation.DELETE_LAST && !mAnimatorDeleteLast.isRunning()) {
            mLinkedListNumbers.remove(mLinkedListNumbers.size() - 1);
            mLinkedList.remove(mLinkedList.size() - 1);
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

        float topLeft;

        if (_type.equals(getResources().getString(R.string.LinkedList_Activity_Type_Head))) {
            topLeft = mMaxHeight - ((mMaxWidth / 8) * mScale);
        } else {
            topLeft = (mMaxHeight - ((mMaxWidth / 8) + (mMaxWidth / 4 * _pos)) * mScale);
        }

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
                mLinkedList.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) - mTranslateYPrepend;
            } break;
            case APPEND: {
                if(_pos == mLinkedList.size() - 1) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) + mTranslateYAppend;
                    mItemTextPaint.setAlpha(mAlphaAppend);
                    mItemPaint.setAlpha(mAlphaAppend);
                }
            } break;
            case INSERT_AT: {
                if (mPosition + 1 == mLinkedListNumbers.size()) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) ;
                }
                if (_pos == mPosition) {
                    mItemTextPaint.setAlpha(mAlphaInsert);
                    mItemPaint.setAlpha(mAlphaInsert);
                } else if (_pos > mPosition) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) - mTranslateYInsert;
                    mItemTextPaint.setAlpha(255);
                    mItemPaint.setAlpha(255);
                }
            } break;
            case DELETE_FIRST: {
                if(_pos == 0) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) + mTranslateYDeleteFirst;
                    mItemTextPaint.setAlpha(mAlphaDeleteFirst);
                    mItemPaint.setAlpha(mAlphaDeleteFirst);
                } else {
                    mLinkedList.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) + mTranslateYDeleteFirst;
                    mItemTextPaint.setAlpha(255);
                    mItemPaint.setAlpha(255);
                }
            } break;
            case DELETE_AT: {
                if (_pos == mPosition) {
                    mItemTextPaint.setAlpha(mAlphaDeleteAt);
                    mItemPaint.setAlpha(mAlphaDeleteAt);
                } else if (_pos > mPosition) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) - mTranslateYDeleteAt;
                    mItemTextPaint.setAlpha(255);
                    mItemPaint.setAlpha(255);
                }
            } break;
            case CLEAR :
            case DELETE_LAST: {
                if (_pos == mLinkedListNumbers.size() - 1) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) + mTranslateYDeleteLast;
                    mItemTextPaint.setAlpha(mAlphaDeleteLast);
                    mItemPaint.setAlpha(mAlphaDeleteLast);
                }
            } break;
            case SUCCESSOR:
            case PREDECESSOR: {
                if(mTouched && _pos == mPosition) {
                    drawGet(_canvas, _pos);
                }
            } break;
            case GET_FIRST: { if (_pos == 0) { drawGet(_canvas, _pos); } } break;
            case GET_AT: { if (_pos == mPosition) { drawGet(_canvas, _pos); } } break;
            case GET_LAST: { if (_pos == mLinkedList.size() - 1) { drawGet(_canvas, _pos); } } break;
            case RANDOM: {
                if (_pos == mLinkedList.size() - 1) {
                    mLinkedList.get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mScale) + mTranslateYRandom;
                    mItemTextPaint.setAlpha(mAlphaRandom);
                    mItemPaint.setAlpha(mAlphaRandom);
                }
            } break;
            case GET_SIZE: { if (_pos == mPositionAnimation) { drawGet(_canvas, _pos); } } break;
        }

        // save the size of the text ("box" around the text) in mBounds
        mItemTextPaint.getTextBounds(mLinkedListNumbers.get(_pos).toString(), 0, mLinkedListNumbers.get(_pos).toString().length(), mBounds);

        // set LinkedList item size
        mLinkedList.get(_pos).left = (int) ((mMaxWidth / 1.5) - (mMaxWidth / 8) - (mResize / 2));
        mLinkedList.get(_pos).right = (int) (mLinkedList.get(_pos).left + (mMaxWidth / 4) + mResize);
        mLinkedList.get(_pos).bottom = (int) (mLinkedList.get(_pos).top + ((mMaxWidth / 4) * mScale) - 10);

        if(mSwitch) {
            drawPointers(_canvas, _pos);
        }

        // get BoundingBox from Text & draw Text + LinkedList item
        _canvas.drawRoundRect(mLinkedList.get(_pos), mRadius, mRadius, mItemPaint);
        _canvas.drawText(mLinkedListNumbers.get(_pos).toString(), getExactCenterX(mLinkedList.get(_pos)) - mBounds.exactCenterX(), (getExactCenterY(mLinkedList.get(_pos)) - mBounds.exactCenterY()), mItemTextPaint);

    }

    private void drawPointers(Canvas _canvas, int _pos) {

        float length;
        double angle = Math.toRadians(45);
        boolean end;

        float height = 76 / 2f;
        float lineHeight = 66;
        float lineWidth = 55;

        if (_pos == mLinkedListNumbers.size() - 1) {
            length = 10;
            end = true;
        } else {
            length = 20;
            end = false;
        }

        float y = (float) (Math.sin(angle) * length);
        float x = (float) (Math.cos(angle) * length);

        PointF p = drawLine(_canvas, _pos, height, lineHeight, lineWidth, true, end);
        drawArrow(_canvas, p, x, y,true, end);

        if (_pos == 0) {
            length = 10;
            end = true;
        } else {
            length = 20;
            end = false;
        }

        y = (float) (Math.sin(Math.toRadians(180) - angle) * length);
        x = (float) (Math.cos(Math.toRadians(180) - angle) * length);

        mItemPaint.setColor(mSecondaryColor);
        p = drawLine(_canvas, _pos, height - mLinkedList.get(_pos).height() - 10 , lineHeight, lineWidth, false, end);
        drawArrow(_canvas, p, x, y, false, end);

        mItemPaint.setColor(mPrimaryColor);
    }

    private PointF drawLine(Canvas _canvas, int _pos, float _height, float _lineHeight, float _lineWidth, boolean right, boolean end) {

        float x;
        int y = (int) (mLinkedList.get(_pos).top - _height);

        if (right) { x = mLinkedList.get(_pos).right;
        } else { x = mLinkedList.get(_pos).left; }

        mPath.moveTo(x, y + _lineHeight);

        if (right && end) {
            mPath.cubicTo(x, y + _lineHeight, x , y + _lineHeight, x + (_lineWidth / 2 ), y + _lineHeight);
        } else if (right) {
            mPath.cubicTo(x + _lineWidth, y + _lineHeight, x + _lineWidth, y, x, y);
            animateLines(_pos);
        } else if (end) {
            mPath.moveTo(x, y);
            mPath.cubicTo(x, y, x - _lineWidth, y, x, y);
        } else {
            mPath.moveTo(x, y);
            mPath.cubicTo(x - _lineWidth, y, x - _lineWidth, y + _lineHeight, x, y + _lineHeight);
            animateLines(_pos);
        }

        _canvas.drawPath(mPath, mItemPaint);
        mPath.reset();
        mItemPaint.setPathEffect(null);

        if (right && end) { return new PointF(x + _lineWidth / 2, y + _lineHeight);
        } else if (right) { return new PointF(x, y);
        } else if (end) { return new PointF(x - _lineWidth / 2, y);
        } else { return new PointF(x, y + _lineHeight); }
    }

    private void animateLines(int _pos) {
        if(mSwitch) {
            if(mCurrentOperation == Operation.PREPEND && _pos == 0
                    || mCurrentOperation == Operation.APPEND && _pos == mLinkedListNumbers.size() - 1) {

                if(!mDraw) { startDrawAnimator(); }

                DashPathEffect drawEffect = new DashPathEffect(new float[]{mDrawProcess, getLength(mPath)}, 0);
                mItemPaint.setPathEffect(drawEffect);
            }
        }
    }

    private void startDrawAnimator() {
        float length = getLength(mPath);
            PropertyValuesHolder propertyDrawLine = PropertyValuesHolder.ofInt(PROPERTY_DRAW_LINE, 0, (int) length);
            mAnimatorDrawLine.setValues(propertyDrawLine);
            mAnimatorDrawLine.addUpdateListener(this);
            mAnimatorDrawLine.setDuration(700);
            mAnimatorDrawLine.start();
            mDraw = true;
    }

    private float getLength(Path _path) {
        mPathMeasure.setPath(_path,false);
        return mPathMeasure.getLength();
    }

    private void drawArrow(Canvas _canvas, PointF _p, float x, float y, boolean right, boolean _end) {

        float paddingY;
        float paddingX;
        float paddingX2;

        if(right) {
            paddingY = 4;
            paddingX = 0;
            paddingX2 = 0;
        } else {
            paddingY = 0;
            paddingX = 2;
            paddingX2 = 4;
        }

        if(!_end) {
            _canvas.drawLine(_p.x, _p.y, _p.x + x + paddingX, _p.y - y + paddingY, mItemPaint);
            _canvas.drawLine(_p.x + x + paddingX2, _p.y + y, _p.x, _p.y, mItemPaint);
        } else {
            _canvas.drawLine(_p.x,_p.y + y, _p.x,_p.y - y, mItemPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent _event) {

        float y = _event.getY();
        float x = _event.getX();

        if(_event.getAction() == MotionEvent.ACTION_DOWN){
            if(mCurrentOperation == Operation.PREDECESSOR || mCurrentOperation == Operation.SUCCESSOR) {
                mAnimatorGetText.setRepeatCount(1);
                mAnimatorGetArea.setRepeatCount(1);
                mAnimatorGetArea.start();
                mAnimatorGetText.start();
                for(int i = 0; i < mLinkedList.size(); i++) {
                    RectF r = mLinkedList.get(i);
                    if(x > r.left && x < r.right && y > r.top && y < r.bottom) {
                        mTouched = true;
                        mPosition = i;
                    }
                }
            }
        }
        return true;
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
