package at.fhooe.mc.datadora;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Vector;

public class LinkedListView extends View {

    private static final String TAG = "LinkedListView : ";

    Paint mLinkedListItemPaint = new Paint();
    Paint mLinkedListItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mLinkedListPositionTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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

        mLinkedListItemTextPaint.setColor(mOnSurfaceColor);
        mLinkedListItemTextPaint.setTextSize(55);

        mLinkedListPositionTextPaint.setColor(mOnSurfaceColor);
        mLinkedListPositionTextPaint.setTextSize(30);
    }

    protected void init(LinkedListActivity _activity) {
        mActivity = _activity;
    }

    protected void append(int _value){
        RectF r = new RectF();

        mLinkedList.add(r);
        mLinkedListNumbers.add(_value);

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
            mLinkedList.get(i).left = (int) ((mMaxWidthLinkedList / 2) - (mMaxWidthLinkedList / 8) - (mResize / 2));
            mLinkedList.get(i).right = (int) (mLinkedList.get(i).left + (mMaxWidthLinkedList / 4) + mResize);
            mLinkedList.get(i).bottom = (int) (mLinkedList.get(i).top + ((mMaxWidthLinkedList / 4) * mScale) - 10);

            // get BoundingBox from Text & draw Text + LinkedList item
            _canvas.drawText(mLinkedListNumbers.get(i).toString(), getExactCenterX(mLinkedList.get(i)) - mBounds.exactCenterX(), (getExactCenterY(mLinkedList.get(i)) - mBounds.exactCenterY()), mLinkedListItemTextPaint);
            _canvas.drawRoundRect(mLinkedList.get(i), mRadius, mRadius, mLinkedListItemPaint);
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
