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

    private static final String TAG = "BSTView : ";
    private Paint mItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mItemPaintArea = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // the current primary color of the currently used theme
    int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    private BinarySearchTreeActivity mActivity;
    private Matrix mMatrix = new Matrix();
    Point mBegin = new Point();
    Point mEnd = new Point();

    PointF mBeginF = new PointF();
    PointF mEndF = new PointF();

    enum Operation {
        ADD,
        REMOVE,
        RANDOM,
        CLEAR,
        SIZE,
        HEIGHT,
        DEPTH,
        HASPARENT,
        HASRIGHTCHILD,
        HASLEFTCHILD,
        ISROOT,
        ISEXTERNAL,
        ISINTERNAL,
        ISEMPTY,
        PARENT,
        RIGHTCHILD,
        LEFTCHILD,
        ROOT,
        EXTERNALNODES,
        INTERNALNODES,
        MAX,
        MIN,
        NONE
    }

    private Operation mCurrentOperation;

    private float mMaxHeight;
    private float mMaxWidth;
    private float mRadius = 50;
    private float mMinDistanceX = 80;
    private float mMinDistanceY = 20;

    // Rect in order to save the TextBounds from the current number
    private Rect mBounds = new Rect();

    private boolean mTouched;
    private int mPosition;

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

    public void setActivity(BinarySearchTreeActivity _activity) {
        mActivity = _activity;
    }

    private void init() {
        mItemPaint.setColor(mPrimaryColor);
        mItemPaint.setStyle(Paint.Style.STROKE);
        mItemPaint.setStrokeWidth(6);

        mItemPaintArea.setColor(mSurfaceColor);
        mItemPaintArea.setStyle(Paint.Style.FILL);

        mItemTextPaint.setColor(mOnSurfaceColor);
        mItemTextPaint.setTextSize(50);


        mBegin.x = -1;
    }

    protected void add() {
        mCurrentOperation = Operation.ADD;
        invalidate();
    }

    protected void remove() {
        mCurrentOperation = Operation.REMOVE;
        invalidate();
    }

    protected void clear() {
        mCurrentOperation = Operation.CLEAR;
        invalidate();
    }

    protected void depth() {
        mCurrentOperation = Operation.DEPTH;
        invalidate();
    }

    protected void height() {
        mCurrentOperation = Operation.HEIGHT;
        invalidate();
    }

    protected void max(int _pos) {
        mCurrentOperation = Operation.MAX;
        mPosition = _pos;
        invalidate();
    }

    protected void min(int _pos) {
        mCurrentOperation = Operation.MIN;
        mPosition = _pos;
        invalidate();
    }

    protected void hasParent() {
        mCurrentOperation = Operation.HASPARENT;
        invalidate();
    }

    protected void hasRightChild () {
        mCurrentOperation = Operation.HASRIGHTCHILD;
        invalidate();
    }

    protected void hasLeftChild () {
        mCurrentOperation = Operation.HASLEFTCHILD;
        invalidate();
    }

    protected void isRoot() {
        mCurrentOperation = Operation.ISROOT;
        invalidate();
    }

    protected void isInternal() {
        mCurrentOperation = Operation.ISINTERNAL;
        invalidate();
    }

    protected void isExternal() {
        mCurrentOperation = Operation.ISEXTERNAL;
        invalidate();
    }

    protected void getParentNode() {
        mCurrentOperation = Operation.PARENT;
        invalidate();
    }

    protected void getRightChild () {
        mCurrentOperation = Operation.RIGHTCHILD;
        invalidate();
    }

    protected void getLeftChild () {
        mCurrentOperation = Operation.LEFTCHILD;
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

        float topSpace = 80;
        BinarySearchTree t = mActivity.getTree();
        Vector<BinaryTreeNode> tv = mActivity.getTreeUser();

        if (mActivity.getTreeUser() != null) {
            for (int i = 0; i < mActivity.getTreeUser().size(); i++) {

                BinaryTreeNode n = tv.get(i);
                BinaryTreeNode nPre = t.getParentNode(n.getData());

                float direction;
                if(nPre != null && n.getData() < nPre.getData()) {
                    direction = -1;
                } else {
                    direction = 1;
                }

                if(i == 0) {
                    n.setPoint(mMaxWidth / 2, topSpace);
                } else {
                    float xPre = nPre.getPoint().x;

                    float x = (n.getChildCount() * mMinDistanceX * direction) + xPre;
                    float y = (mMinDistanceX * t.getDepth(n.getData())) + topSpace;
                    n.setPoint(x,y);
                }

                String s = String.valueOf(n.getData());
                mItemTextPaint.getTextBounds(s, 0, s.length(), mBounds);

                if(i != 0) {

                    float x = calculateX(n, nPre);
                    float y = calculateY(n, nPre);

                    double lengthX = Math.cos(calculateAngle(y,x));
                    double lengthY = Math.cos(calculateAngle(x,y));

                    float xPre = (float) (nPre.getPoint().x + (lengthX * direction) * mRadius);
                    float yPre = (float) (nPre.getPoint().y + lengthY * mRadius);

                    x = (float) (n.getPoint().x - (lengthX * direction) * mRadius);
                    y = (float) (n.getPoint().y - lengthY * mRadius);

                    _canvas.drawLine(xPre, yPre, x, y, mItemPaint);
                }
                _canvas.drawCircle(n.getPoint().x, n.getPoint().y, mRadius, mItemPaint);
                _canvas.drawText(s, n.getPoint().x - 3 - mBounds.width() / 2f, n.getPoint().y + mBounds.height() / 2f, mItemTextPaint);
            }
        }
    }

    private double calculateAngle(float _a, float _b) {
        return Math.atan(_a / _b);
    }

    private float calculateX (BinaryTreeNode _n, BinaryTreeNode _nPre) {
        return _n.getPoint().x - _nPre.getPoint().x;
    }

    private float calculateY (BinaryTreeNode _n, BinaryTreeNode _nPre) {
        return _n.getPoint().y - _nPre.getPoint().y;
    }

    private void setUpOperations() {
        if(mPosition > -1 && mPosition < mActivity.getTreeUser().size() - 1) {
            if(mCurrentOperation == null) {
                mCurrentOperation = Operation.NONE;
            }
            switch (mCurrentOperation) {
                case ADD:
                    break;
                case REMOVE: {
                    int data = mActivity.getTreeUser().get(mPosition).getData();
                    mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", data));
                    mCurrentOperation = Operation.NONE;
                } break;
                case HEIGHT: {
                   // mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mTreeNumbers.getHeight(mTree.get(mPosition))));
                    mCurrentOperation = Operation.NONE;
                } break;
                case DEPTH: {
                    // mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mTreeNumbers.getDepth(mTree.get(mPosition))));
                    mCurrentOperation = Operation.NONE;
                } break;

                case HASPARENT: {
                    BinaryTreeNode parent = mActivity.getTree().getParentNode(mActivity.getTreeUser().get(mPosition).getData());
                    if(parent != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case HASLEFTCHILD: {
                    BinaryTreeNode n = mActivity.getTreeUser().get(mPosition);
                    if(n.getLeft() != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case HASRIGHTCHILD: {
                    BinaryTreeNode n = mActivity.getTreeUser().get(mPosition);
                    if(n.getRight() != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case ISROOT: {
                    int value = mActivity.getTreeUser().get(mPosition).getData();
                    if (mActivity.getTree().getRoot().getData() == value) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case ISINTERNAL: {
                    int key = mActivity.getTreeUser().get(mPosition).getData();
                    if ((mActivity.getTree().getChildNode(key,true)!= Integer.MIN_VALUE) || (mActivity.getTree().getChildNode(key, false)!= Integer.MIN_VALUE)) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case ISEXTERNAL: {
                    int key = mActivity.getTreeUser().get(mPosition).getData();
                    if ((mActivity.getTree().getChildNode(key,true) == Integer.MIN_VALUE) && (mActivity.getTree().getChildNode(key,false) == Integer.MIN_VALUE)) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText( R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case RIGHTCHILD: {
                    BinaryTreeNode n = mActivity.getTreeUser().get(mPosition);
                    if(n.getRight() != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", n.getRight().getData()));
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText("-");
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case LEFTCHILD: {
                    BinaryTreeNode n = mActivity.getTreeUser().get(mPosition);
                    if(n.getLeft() != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", n.getLeft().getData()));
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText("-");
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case PARENT: {
                    BinaryTreeNode parent = mActivity.getTree().getParentNode(mActivity.getTreeUser().get(mPosition).getData());
                    if(parent != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", parent));
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText("-");
                    }
                    mCurrentOperation = Operation.NONE;
                } break;
            }
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent _event) {

        if (_event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = _event.getX();
            float y = _event.getY();
            int i = 0;
           /* for (BinaryTreeNode n : mTree) {
                if(x < n.getPoint().x + mRadius && x > n.getPoint().x - mRadius && y < n.getPoint().y + mRadius && y > n.getPoint().y - mRadius) {
                    mTouched = true;
                    mPosition = i;
                    invalidate();
                    break;
                } else {
                    mPosition = -1;
                }
                i++;
                invalidate();
            }*/
        }

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
