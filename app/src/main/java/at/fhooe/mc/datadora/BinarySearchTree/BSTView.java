package at.fhooe.mc.datadora.BinarySearchTree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.R;

public class BSTView extends View {

    int level;

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
    private float mRadius = 50;
    private float mMinDistanceX = 80;
    private float mMinDistanceY = 20;

    // Rect in order to save the TextBounds from the current number
    private Rect mBounds = new Rect();
    private BinarySearchTree mTreeNumbers = new BinarySearchTree();
    private Vector<PointF> mTree = new Vector<>();

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
        mTreeNumbers.insert(_value);

        invalidate();
    }

    protected void clear() {
        mTreeNumbers.clear();
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

        if(mCurrentOperation == Operation.ADD) {
            addAnimation(_canvas, mTreeNumbers.root, mTreeNumbers.root, new PointF(0,mRadius * 4), new PointF(0 ,mRadius * 2));
        }
    }

    private void drawCircle(Canvas _canvas, PointF _a, PointF _b, String _s, boolean _right) {

        float posX = _a.x;
        float posY = _a.y;

        if(_right) {
            posX = mMaxWidth/2 + posX;
        } else {
            posX = mMaxWidth/2 - posX;
        }

        mTree.add(new PointF(posX, posY));

        mItemTextPaint.getTextBounds(_s, 0, _s.length(), mBounds);
        _canvas.drawCircle(posX, posY, mRadius, mItemPaint);
       // _canvas.drawLine(_a.x, _a.y, _b.x, _b.y, mItemPaint);
        _canvas.drawText(_s, posX - 3 - mBounds.width() / 2 , posY + mBounds.height() / 2, mItemTextPaint);
    }

    private void addAnimation(Canvas _canvas, BinaryTreeNode _node, BinaryTreeNode _old, PointF _currPoint, PointF _oldPoint) {

        if (_node == null)
            return;

        int height = mTreeNumbers.getHeight(_node);
        int heightOld = mTreeNumbers.getHeight(_old);
        int depth = mTreeNumbers.getDepth(_node.data);

        Log.i(TAG, "PRINT: " + _node.data + " NODE OLD: " + _old.data + " , DEPTH : " + mTreeNumbers.getDepth(_node.data));
        if( _old.data > _node.data) {
            _oldPoint.x = _currPoint.x;
            _oldPoint.y = _currPoint.y;
            _currPoint.y = (mRadius * 4) + ((mRadius * 2) * depth + (mMinDistanceY * depth));
            if(_node.right == null) {
                _currPoint.x = _currPoint.x + mMinDistanceX;
            } else {
                _currPoint.x = _currPoint.x + (mMinDistanceX * (height));
            }
            drawCircle(_canvas, _currPoint, _oldPoint, String.valueOf(_node.data), false);

        } else if (_old.data < _node.data) {
            _oldPoint.x = _currPoint.x;
            _currPoint.y = (mRadius * 4) + ((mRadius * 2) * depth) + (mMinDistanceY * depth);
            if(mTreeNumbers.root.data > _node.data) {
                _currPoint.x = _currPoint.x - (mMinDistanceX * (heightOld));
                drawCircle(_canvas, _currPoint, _oldPoint, String.valueOf(_node.data), false);
            } else {
                _currPoint.x = _currPoint.x + mMinDistanceX;
                drawCircle(_canvas, _currPoint, _oldPoint, String.valueOf(_node.data), true);
            }
        } else {
            drawCircle(_canvas, _currPoint, _oldPoint, String.valueOf(_node.data), true);
        }
        Log.i(TAG, "PRINT: " + _node.data + " X: " + _currPoint.x);

        addAnimation(_canvas, _node.left, _node, _currPoint, _oldPoint);
        addAnimation(_canvas, _node.right, _node, _currPoint, _oldPoint);
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
