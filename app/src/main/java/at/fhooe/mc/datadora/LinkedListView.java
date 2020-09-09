package at.fhooe.mc.datadora;

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

import androidx.annotation.Nullable;

import java.util.Vector;

public class LinkedListView extends View {

    private static final String TAG = "LinkedListView : ";

    Paint mLinkedListItemPaint = new Paint();
    Paint mLinkedListTypePaint = new Paint();
    Paint mLinkedListItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mLinkedListPositionTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mLinkedListTypeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    enum Operation {
        PREPEND,
        APPEND,
        INSERT_AT,
        DELETE_FIRST,
        DELETE_LAST,
        DELETE_AT,
        PREDECESSOR,
        SUCCESSOR,
        GET_FIRST,
        GET_LAST,
        GET_AT
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
    Filter mCurrentFiler;

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
        mLinkedListItemPaint.setColor(mPrimaryColor);
        mLinkedListItemPaint.setStyle(Paint.Style.STROKE);
        mLinkedListItemPaint.setStrokeWidth(6);

        mLinkedListTypePaint.setColor(mPrimaryColor);
        mLinkedListTypePaint.setStyle(Paint.Style.FILL);

        mLinkedListItemTextPaint.setColor(mOnSurfaceColor);
        mLinkedListItemTextPaint.setTextSize(55);

        mLinkedListTypeTextPaint.setColor(mOnPrimaryColor);
        mLinkedListTypeTextPaint.setTextSize(40);

        mLinkedListPositionTextPaint.setColor(mOnSurfaceColor);
        mLinkedListPositionTextPaint.setTextSize(30);
    }

    protected void init(LinkedListActivity _activity) {
        mActivity = _activity;
    }

    protected void sorted(){
        mCurrentFiler = Filter.SORTED;
    }

    protected void unsorted(){
        mCurrentFiler = Filter.UNSORTED;
    }

    protected void head(){
        mCurrentType = Type.HEAD;
        Log.i(TAG, "TYPE: " + mCurrentType);
        invalidate();
    }

    protected void tail(){
        mCurrentType = Type.TAIL;
        Log.i(TAG, "TYPE: " + mCurrentType);
        invalidate();
    }

    protected void both(){
        mCurrentType = Type.HEAD_TAIL;
        Log.i(TAG, "TYPE: " + mCurrentType);
        invalidate();
    }

    protected void prepend(int _value){
        mCurrentOperation = Operation.PREPEND;

        RectF r = new RectF();
        mLinkedList.add(r);
        mLinkedListNumbers.add(0,_value);
        reScale();
    }

    protected void append(int _value){
        mCurrentOperation = Operation.APPEND;

        RectF r = new RectF();
        mLinkedList.add(r);
        mLinkedListNumbers.add(_value);
        reScale();
    }

    protected void insertAt(int _value, int _pos){
        mCurrentOperation = Operation.INSERT_AT;

        RectF r = new RectF();
        mLinkedList.add(r);
        mLinkedListNumbers.add(_pos,_value);
        reScale();
    }

    protected void deleteFirst() {
        mCurrentOperation = Operation.DELETE_FIRST;
        mLinkedList.remove(0);
        mLinkedListNumbers.remove(0);
        reScale();
    }

    protected void deleteLast() {
        mCurrentOperation = Operation.DELETE_LAST;
        mLinkedList.remove(mLinkedList.size() - 1);
        mLinkedListNumbers.remove(mLinkedListNumbers.size() - 1);
        reScale();
    }

    protected void deleteAt(int _pos) {
        mCurrentOperation = Operation.DELETE_AT;

        mLinkedList.remove(_pos);
        mLinkedListNumbers.remove(_pos);
        reScale();
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

        invalidate();
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);

        for(int i = 0; i < mLinkedListNumbers.size(); i++) {

            // save the size of the text ("box" around the text) in mBounds
            mLinkedListItemTextPaint.getTextBounds(mLinkedListNumbers.get(i).toString(), 0, mLinkedListNumbers.get(i).toString().length(), mBounds);

            // set LinkedList item size
            mLinkedList.get(i).top = (int) (mMaxHeightLinkedList - ((mMaxWidthLinkedList / 4) + (mMaxWidthLinkedList / 4 * i)) * mScale);
            mLinkedList.get(i).left = (int) ((mMaxWidthLinkedList / 1.5) - (mMaxWidthLinkedList / 8) - (mResize / 2));
            mLinkedList.get(i).right = (int) (mLinkedList.get(i).left + (mMaxWidthLinkedList / 4) + mResize);
            mLinkedList.get(i).bottom = (int) (mLinkedList.get(i).top + ((mMaxWidthLinkedList / 4) * mScale) - 10);

            // get BoundingBox from Text & draw Text + LinkedList item
            _canvas.drawText(mLinkedListNumbers.get(i).toString(), getExactCenterX(mLinkedList.get(i)) - mBounds.exactCenterX(), (getExactCenterY(mLinkedList.get(i)) - mBounds.exactCenterY()), mLinkedListItemTextPaint);
            _canvas.drawRoundRect(mLinkedList.get(i), mRadius, mRadius, mLinkedListItemPaint);
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
    }

    /**
     * draws the head of the list
     * @param _canvas
     */
    private void drawType(Canvas _canvas, int _pos, String _type) {
        mLinkedListTypeTextPaint.getTextBounds(_type, 0, _type.length(), mBounds);
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

        _canvas.drawPath(mTriangle, mLinkedListTypePaint);
        mTriangle.reset();
        _canvas.drawRoundRect(mTypeRect, 10, 10, mLinkedListTypePaint);
        _canvas.drawText(_type,mTypeRect.left + padding, topLeft , mLinkedListTypeTextPaint);
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
