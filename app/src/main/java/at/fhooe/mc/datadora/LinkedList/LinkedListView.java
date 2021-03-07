package at.fhooe.mc.datadora.LinkedList;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.Animation.LLValue;
import at.fhooe.mc.datadora.MainActivity;
import at.fhooe.mc.datadora.R;

public class LinkedListView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private static final String TAG = "LinkedListView : ";

    protected final Paint mTypePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected final Paint mTypeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Animator for the get operations
    protected ValueAnimator mAnimatorGetText = new ValueAnimator();

    // Animator for the get operations
    protected ValueAnimator mAnimatorGetArea = new ValueAnimator();

    // the current color value - used for get animation (for the area of one item)
    private int mColorAreaGet;

    // the current color value - used for get animation (for the text of one item)
    private int mColorTextGet;

    // checks if a list item was touched
    protected boolean mTouched;

    // the position for animation of random & size
    private int mPosition = 0;

    enum Filter {SORTED, UNSORTED}

    enum Type {HEAD, TAIL, HEAD_TAIL}

    protected LinkedListActivity mActivity;

    // Rect in order to save the TextBounds from the current number
    protected final Rect mBounds = new Rect();

    // int that specifies the radius of the corners of the drawn rectangles
    protected final int mRadius = 4;

    // factor for the change of width of the LinkedList item boxes, when the number is to big for the current width
    protected final int mResize = 1;

    // the current primary color of the currently used theme
    protected final int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current secondary color of the currently used theme
    protected final int mSecondaryColor = getResources().getColor(R.color.primaryLightColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    protected final int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    protected final int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    protected final int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    // current width of the LinkedListView within the layout
    private float mMaxWidth;

    // current height of the LinkedListView within the layout
    private float mMaxHeight;

    // the current operation
    private int mCurrentOperation;

    // the current type
    protected Type mCurrentType;

    // the current filter (sorted or unsorted)
    protected Filter mCurrentFilter;

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

    protected LLValue mValues;

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

    public void setSwitch(boolean _pointer){
        if(_pointer) {
            mActivity.getBinding().LinkedListActivityView.setVisibility(GONE);
            mActivity.getBinding().LinkedListActivityViewPointer.setVisibility(VISIBLE);
        } else {
            mActivity.getBinding().LinkedListActivityView.setVisibility(VISIBLE);
            mActivity.getBinding().LinkedListActivityViewPointer.setVisibility(GONE);
        }
    }

    public LLValue getValues() {
        return mValues;
    }

    /**
     * Initializes the key components such as Paint
     */
    protected void init() {
        Paint itemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint itemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint positionTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mValues = LLValue.getInstance(itemPaint, itemTextPaint, new Vector<RectF>(), new Vector<Integer>(), new Vector<Integer>(), 1, mMaxHeight, mMaxWidth, mPosition, mCurrentOperation);
        mValues.getItemPaint().setColor(mPrimaryColor);
        mValues.getItemPaint().setStyle(Paint.Style.STROKE);
        mValues.getItemPaint().setStrokeCap(Paint.Cap.ROUND);
        mValues.getItemPaint().setStrokeWidth(6);

        mTypePaint.setColor(mPrimaryColor);
        mTypePaint.setStyle(Paint.Style.FILL);

        mValues.getItemTextPaint().setColor(mOnSurfaceColor);
        mValues.getItemTextPaint().setTextSize(55);

        mTypeTextPaint.setColor(mOnPrimaryColor);
        mTypeTextPaint.setTextSize(40);

        positionTextPaint.setColor(mOnSurfaceColor);
        positionTextPaint.setTextSize(30);
    }

    protected void setActivity(LinkedListActivity _activity) {
        mActivity = _activity;
    }

    protected void sorted() {
        mCurrentFilter = Filter.SORTED;
    }

    protected void unsorted() {
        mCurrentFilter = Filter.UNSORTED;
    }

    protected void head() {
        mCurrentType = Type.HEAD;
        invalidate();
    }

    protected void tail() {
        mCurrentType = Type.TAIL;
        invalidate();
    }

    protected void both() {
        mCurrentType = Type.HEAD_TAIL;
        invalidate();
    }

    public void append(int _value) {
        mCurrentOperation = Operation.APPEND;
        RectF r = new RectF();
        mValues.getLinkedListRec().add(r);
        mValues.getLinkedListNum().add(_value);
        reScale();
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void prepend(int _value) {
        mCurrentOperation = Operation.PREPEND;
        RectF r = new RectF();
        mValues.getLinkedListRec().add(r);
        mValues.getLinkedListNum().add(0, _value);
        reScale();
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void insertAt(int _value, int _pos) {
        mCurrentOperation = Operation.INSERT_AT;
        RectF r = new RectF();
        mValues.getLinkedListRec().add(r);
        mValues.getLinkedListNum().add(_pos, _value);
        reScale();
        mValues.setPosition(_pos);
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void deleteFirst() {
        mCurrentOperation = Operation.DELETE_FIRST;
        reScaleUndo();
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void deleteLast() {
        mCurrentOperation = Operation.DELETE_LAST;
        reScaleUndo();
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void deleteAt(int _pos) {
        mCurrentOperation = Operation.DELETE_AT;
        mValues.setPosition(_pos);
        reScaleUndo();
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void clear() {
        mCurrentOperation = Operation.CLEAR;
        reScaleUndo();
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void predecessor() {
        mCurrentOperation = Operation.PREDECESSOR;
        mValues.setPosition(0);
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void successor() {
        mCurrentOperation = Operation.SUCCESSOR;
        mValues.setPosition(0);
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void getSize() {
        mCurrentOperation = Operation.GET_SIZE;
        mValues.setPosition(0);
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void getFirst() {
        mCurrentOperation = Operation.GET_FIRST;
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void getLast() {
        mCurrentOperation = Operation.GET_LAST;
        mValues.setCurrentOperation(mCurrentOperation);
    }

    public void getAt(int _pos) {
        mCurrentOperation = Operation.GET_AT;
        mValues.setCurrentOperation(mCurrentOperation);
        mValues.setPosition(_pos);
    }

    protected void random(Vector<Integer> _list) {
        mCurrentOperation = Operation.RANDOM;
        mValues.setPosition(0);
        mValues.getLinkedListNum().clear();
        mValues.getLinkedListRec().clear();
        mValues.getLinkedListRand().clear();
        mValues.getLinkedListRand().addAll(_list);
        mValues.setScale(1);
        reScale();
        mValues.setCurrentOperation(mCurrentOperation);
    }

    protected void reScale() {
        while (mMaxHeight <= (mMaxWidth / 4) * mValues.getScale() * mValues.getLinkedListRec().size()) {
            mValues.setScale(mValues.getScale() / 1.2f);
        }
    }

    protected void reScaleUndo() {
        if (mMaxHeight > (mMaxWidth / 4) * (mValues.getScale() * 1.2f) * mValues.getLinkedListRec().size()) {
            mValues.setScale(mValues.getScale() * 1.2f);
            if (mValues.getScale() > 1) {
                mValues.setScale(1);
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
        mValues.setScale(1);

        mValues.setMaxHeight(mMaxHeight);
        mValues.setMaxWidth(mMaxWidth);

        setUpAnimation();
        invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator _animation) {
        if (_animation == mAnimatorGetArea) {
            mColorAreaGet = (int) _animation.getAnimatedValue();
        } else if (_animation == mAnimatorGetText) {
            mColorTextGet = (int) _animation.getAnimatedValue();
        }
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator _animation) {
        if (_animation == mAnimatorGetText) {
            if (mCurrentOperation == Operation.PREDECESSOR || mCurrentOperation == Operation.SUCCESSOR) {
                if (mCurrentOperation == Operation.PREDECESSOR && mPosition < 0) {
                    Toast.makeText(mActivity, R.string.LinkedList_Activity_Pre_Empty, Toast.LENGTH_SHORT).show();
                } else if (mCurrentOperation == Operation.SUCCESSOR && mPosition >= mValues.getLinkedListNum().size()) {
                    Toast.makeText(mActivity, R.string.LinkedList_Activity_Succ_Empty, Toast.LENGTH_SHORT).show();
                } else {
                    mActivity.getBinding().LinkedListActivityReturnValue.setText(String.format("%s", mValues.getLinkedListNum().get(mPosition).toString()));
                }
                mTouched = false;
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator _animation) {
        if (_animation == mAnimatorGetText) {
            if (mCurrentOperation == Operation.PREDECESSOR) {
                mPosition--;
            } else if (mCurrentOperation == Operation.SUCCESSOR) {
                mPosition++;
            } else {
                mPosition++;
            }

            mColorTextGet = 0;
            mColorAreaGet = 0;
        }
    }

    protected void setUpAnimation() {
        mAnimatorGetArea = ValueAnimator.ofObject(new ArgbEvaluator(), mSurfaceColor, mPrimaryColor);
        mAnimatorGetArea.setDuration(500);
        mAnimatorGetArea.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorGetArea.addUpdateListener(this);

        mAnimatorGetText = ValueAnimator.ofObject(new ArgbEvaluator(), mOnSurfaceColor, mOnPrimaryColor);
        mAnimatorGetText.setDuration(500);
        mAnimatorGetText.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorGetText.addUpdateListener(this);
        mAnimatorGetText.addListener(this);
    }

    protected void afterAnimation(Canvas _canvas) {
        if (mCurrentOperation == Operation.GET_SIZE && !mAnimatorGetText.isRunning()) {
            mValues.getItemPaint().setColor(mSurfaceColor);
            mValues.getItemPaint().setStyle(Paint.Style.FILL);
            _canvas.drawRoundRect(mValues.getLinkedListRec().get(mValues.getLinkedListRec().size() - 1), mRadius, mRadius, mValues.getItemPaint());
            mValues.getItemPaint().setStyle(Paint.Style.STROKE);
            mValues.getItemPaint().setColor(mPrimaryColor);
            mValues.getItemTextPaint().setColor(mOnSurfaceColor);
            _canvas.drawRoundRect(mValues.getLinkedListRec().get(mValues.getLinkedListRec().size() - 1), mRadius, mRadius, mValues.getItemPaint());
            int pos = mValues.getLinkedListRec().size() - 1;
            mValues.getItemTextPaint().getTextBounds(mValues.getLinkedListNum().get(pos).toString(), 0, mValues.getLinkedListNum().get(pos).toString().length(), mBounds);
            _canvas.drawText(mValues.getLinkedListNum().get(pos).toString(), getExactCenterX(mValues.getLinkedListRec().get(pos)) - mBounds.exactCenterX(), (getExactCenterY(mValues.getLinkedListRec().get(pos)) - mBounds.exactCenterY()), mValues.getItemTextPaint());
        }
    }

    protected void prepareAnimation(int _pos) {
        mValues.getLinkedListRec().get(_pos).top = (int) (mMaxHeight - ((mMaxWidth / 4) + (mMaxWidth / 4 * _pos)) * mValues.getScale());
        mValues.getItemTextPaint().setAlpha(255);
        mValues.getItemPaint().setAlpha(255);
        mValues.getItemTextPaint().setColor(mOnSurfaceColor);
        mValues.getItemPaint().setColor(mPrimaryColor);
    }

    protected void drawType(Canvas _canvas, int _pos) {
        String type;
        if (_pos == 0 && (mCurrentType == Type.HEAD || mCurrentType == Type.HEAD_TAIL)) {
            type = getResources().getString(R.string.LinkedList_Activity_Type_Head);
            drawTypeIcon(_canvas, _pos, type);
        } else if (_pos == mValues.getLinkedListRec().size() - 1 && (mCurrentType == Type.TAIL || mCurrentType == Type.HEAD_TAIL)) {
            type = getResources().getString(R.string.LinkedList_Activity_Type_Tail);
            drawTypeIcon(_canvas, _pos, type);
        }
    }

    /**
     * draws the head, tail or both of them
     *
     * @param _canvas canvas on which the head and/or tail is painted
     */
    protected void drawTypeIcon(Canvas _canvas, int _pos, String _type) {
        mTypeTextPaint.getTextBounds(_type, 0, _type.length(), mBounds);
        int padding = 15;

        float topLeft;

        if (_type.equals(getResources().getString(R.string.LinkedList_Activity_Type_Head))) {
            topLeft = mMaxHeight - ((mMaxWidth / 8) * mValues.getScale());
        } else {
            topLeft = (mMaxHeight - ((mMaxWidth / 8) + (mMaxWidth / 4 * _pos)) * mValues.getScale());
        }

        mTypeRect.top = topLeft - mBounds.height() - padding;
        mTypeRect.bottom = mTypeRect.top + mBounds.height() + (padding * 2);
        mTypeRect.left = 45;
        mTypeRect.right = mTypeRect.left + mBounds.width() + 6 + (padding * 2);

        int y = (int) (mTypeRect.bottom - (mTypeRect.height() / 2));

        // setup triangle
        int width = 20;
        mFirst.set((int) mTypeRect.right, y - width / 2);
        mSecond.set((int) mTypeRect.right + (width / 2), y);
        mThird.set((int) mTypeRect.right, y + width / 2);

        // draw triangle
        mTriangle.setFillType(Path.FillType.EVEN_ODD);
        mTriangle.moveTo(mFirst.x, mFirst.y);
        mTriangle.lineTo(mSecond.x, mSecond.y);
        mTriangle.lineTo(mThird.x, mThird.y);
        mTriangle.close();

        _canvas.drawPath(mTriangle, mTypePaint);
        mTriangle.reset();
        _canvas.drawRoundRect(mTypeRect, 10, 10, mTypePaint);
        _canvas.drawText(_type, mTypeRect.left + padding, topLeft, mTypeTextPaint);
    }

    /**
     * draws the get animation (for getFirst, getLast, getAt, Successor & Predecessor)
     *
     * @param _canvas canvas on which the animation is painted
     * @param _pos    the current position the loop from the onDraw is in
     */
    protected void drawGet(Canvas _canvas, int _pos) {

        mValues.getItemPaint().setColor(mColorAreaGet);
        mValues.getItemTextPaint().setColor(mColorTextGet);

        mValues.getItemPaint().setStyle(Paint.Style.FILL);
        _canvas.drawRoundRect(mValues.getLinkedListRec().get(_pos), mRadius, mRadius,  mValues.getItemPaint());

        mValues.getItemPaint().setStyle(Paint.Style.STROKE);
        mValues.getItemPaint().setColor(mPrimaryColor);
        _canvas.drawRoundRect(mValues.getLinkedListRec().get(_pos), mRadius, mRadius,  mValues.getItemPaint());
    }

    @Override
    public boolean onTouchEvent(MotionEvent _event) {

        float y = _event.getY();
        float x = _event.getX();

        if (_event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mCurrentOperation == Operation.PREDECESSOR || mCurrentOperation == Operation.SUCCESSOR) {
                mAnimatorGetText.setRepeatCount(1);
                mAnimatorGetArea.setRepeatCount(1);
                mAnimatorGetArea.start();
                mAnimatorGetText.start();
                for (int i = 0; i < mValues.getLinkedListRec().size(); i++) {
                    RectF r = mValues.getLinkedListRec().get(i);
                    if (x > r.left && x < r.right && y > r.top && y < r.bottom) {
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
     *
     * @param _rect the RectF, which center is wanted
     * @return the center of the given RectF
     */
    protected float getExactCenterX(RectF _rect) {
        return ((_rect.right - _rect.left) / 2) + _rect.left;
    }

    /**
     * Get the exact Center from the given RectF in the y-Axis
     *
     * @param _rect the RectF, which center is wanted
     * @return the center of the given RectF
     */
    protected float getExactCenterY(RectF _rect) {
        return ((_rect.bottom - _rect.top) / 2) + _rect.top;
    }
}
