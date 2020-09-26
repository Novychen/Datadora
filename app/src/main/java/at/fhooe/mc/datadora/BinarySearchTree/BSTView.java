package at.fhooe.mc.datadora.BinarySearchTree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.LinkedListView;
import at.fhooe.mc.datadora.R;

public class BSTView extends View {
    private static final String TAG = "BSTView : ";
    private Paint mItemPaint = new Paint();
    private Paint mItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);;

    // the current primary color of the currently used theme
    int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    private Matrix mMatrix = new Matrix();
    Point mBegin = new Point();
    Point mEnd = new Point();

    enum Operation {
        ADD,
        REMOVE,
        RANDOM,
        CLEAR,
        SIZE
    }

    private Operation mCurrentOperation;

    private float mMaxHeight;
    private float mMaxWidth;
    private float mRadius = 40;
    private float mMinDistanceX = 50;
    private float mMinDistanceY = 20;

    // Rect in order to save the TextBounds from the current number
    private Rect mBounds = new Rect();
    private BinarySearchTree mTree = new BinarySearchTree();

    private int mTranslateX;
    private int mTranslateY;

    public BSTView(Context context) {
        super(context);
        init();
    }

    public BSTView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BSTView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mItemPaint.setColor(mPrimaryColor);
        mItemPaint.setStyle(Paint.Style.STROKE);
        mItemPaint.setStrokeWidth(6);

        mItemTextPaint.setColor(mOnSurfaceColor);
        mItemTextPaint.setTextSize(50);

        mBegin.x = -1;
    }


    protected void add(int _value) {
        mCurrentOperation = Operation.ADD;
        mTree.insert(_value);

        invalidate();
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
    }

    @Override
    protected void onMeasure(int _widthMeasureSpec, int _heightMeasureSpec) {
        super.onMeasure(_widthMeasureSpec, _heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        mMatrix.preTranslate(mTranslateX,mTranslateY);
        _canvas.setMatrix(mMatrix);

        if( mCurrentOperation == Operation.ADD) {
            addAnimation(_canvas);
        }
    }

    private void draw(Canvas _canvas, float _posX, float _posY, String _s) {
        mItemTextPaint.getTextBounds(_s, 0, _s.length(), mBounds);

        _canvas.drawCircle(_posX, _posY, mRadius, mItemPaint);
        _canvas.drawText(_s, _posX - 3 - mBounds.width() / 2 , _posY + mBounds.height() / 2, mItemTextPaint);
    }

    private void addAnimation(Canvas _canvas){

        float positionX = mMaxWidth / 2;
        float positionY = mRadius * 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent _event) {

        if (_event.getAction() == MotionEvent.ACTION_MOVE) {
                if(mBegin.x == -1) {
                    mBegin.y = (int) _event.getY();
                    mBegin.x = (int) _event.getX();
                } else {
                    mEnd.y = (int) _event.getY();
                    mEnd.x = (int) _event.getX();
                    mTranslateY = mEnd.y - mBegin.y;
                    mTranslateX = mEnd.x - mBegin.x;
                    mBegin.x = -1;
                }
                if(mTranslateY < 20 || mTranslateX < 20) {
                    invalidate();
                }
            }
        return true;
    }

}
