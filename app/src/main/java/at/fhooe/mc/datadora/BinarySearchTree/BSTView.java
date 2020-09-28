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
import android.widget.Toast;

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
    private BinarySearchTree mTreeNumbers = new BinarySearchTree();
    private Vector<BinaryTreeNode> mTree = new Vector<>();

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


    protected void add(int _value) {
        mCurrentOperation = Operation.ADD;
        mTreeNumbers.insert(_value);
        invalidate();
    }

    protected void remove() {
        mCurrentOperation = Operation.REMOVE;
        invalidate();
    }

    protected void clear() {
        mCurrentOperation = Operation.CLEAR;
        mTreeNumbers.clear();
        mTree.clear();
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
        mMatrix.preTranslate(mTranslateX,mTranslateY);
        _canvas.setMatrix(mMatrix);

        setUpOperations();
        mBeginF.set(0,mRadius * 4);
        mEndF.set(0 ,mRadius * 4);
        drawAnimation(_canvas, mTreeNumbers.root, mTreeNumbers.root, mBeginF, mEndF);
    }

    private void setUpOperations() {
        if(mPosition > -1) {
            switch (mCurrentOperation) {
                case ADD:
                    break;
                case REMOVE: {
                    Integer data = mTree.get(mPosition).data;
                    mTreeNumbers.remove(data);
                    mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", data));
                    mActivity.getTree().remove(data);
                    mActivity.getTreeUser().remove(data);
                    mTree.remove(mPosition);
                    mCurrentOperation = Operation.NONE;
                } break;
                case HEIGHT: {
                    mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mTreeNumbers.getHeight(mTree.get(mPosition).data)));
                    mCurrentOperation = Operation.NONE;
                } break;
                case DEPTH: {
                    mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mTreeNumbers.getDepth(mTree.get(mPosition).data)));
                    mCurrentOperation = Operation.NONE;
                } break;

                case HASPARENT: {
                    int parent = mTreeNumbers.getParent(mTree.get(mPosition).data);
                    if(parent > -1) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case HASLEFTCHILD: {
                    BinaryTreeNode n = mTreeNumbers.findNode(mTree.get(mPosition).data);
                    if(n.left != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case HASRIGHTCHILD: {
                    BinaryTreeNode n = mTreeNumbers.findNode(mTree.get(mPosition).data);
                    if(n.right != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case ISROOT: {
                    int value = mTree.get(mPosition).data;
                    if (mTreeNumbers.root.data == value) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case ISINTERNAL: {
                    int key = mTree.get(mPosition).data;
                    if ((mTreeNumbers.getChildNode(key,true)!= Integer.MIN_VALUE) || (mTreeNumbers.getChildNode(key, false)!= Integer.MIN_VALUE)) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case ISEXTERNAL: {
                    int key = mTree.get(mPosition).data;
                    if ((mTreeNumbers.getChildNode(key,true) == Integer.MIN_VALUE) && (mTreeNumbers.getChildNode(key,false) == Integer.MIN_VALUE)) {
                        mActivity.getBinding().BSTActivityReturnText.setText(R.string.All_Data_Activity_True);
                    } else {
                        mActivity.getBinding().BSTActivityReturnText.setText( R.string.All_Data_Activity_False);
                    }
                    mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", key));
                }

                case RIGHTCHILD: {
                    BinaryTreeNode n = mTreeNumbers.findNode(mTree.get(mPosition).data);
                    if(n.right != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", n.right.data));
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText("-");
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case LEFTCHILD: {
                    BinaryTreeNode n = mTreeNumbers.findNode(mTree.get(mPosition).data);
                    if(n.left != null) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", n.left.data));
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText("-");
                    }
                    mCurrentOperation = Operation.NONE;
                } break;

                case PARENT: {
                    int parent = mTreeNumbers.getParent(mTree.get(mPosition).data);
                    if(parent > - 1) {
                        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", parent));
                    } else {
                        mActivity.getBinding().BSTActivityReturnValue.setText("-");
                    }
                    mCurrentOperation = Operation.NONE;
                } break;
            }
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

        BinaryTreeNode n = new BinaryTreeNode(Integer.parseInt(_s));
        n.setPoint(new PointF(posX, posY));
        mTree.add(n);

        mItemTextPaint.getTextBounds(_s, 0, _s.length(), mBounds);
        if(mPosition != -1 && ((mPosition == Integer.parseInt(_s)) ||(mTouched &&  mTree.get(mPosition).point.x == n.point.x && mTree.get(mPosition).point.y == n.point.y))) {
            mItemPaintArea.setColor(mPrimaryColor);
            mItemTextPaint.setColor(mOnPrimaryColor);
        } else {
            mItemPaintArea.setColor(mSurfaceColor);
            mItemTextPaint.setColor(mOnSurfaceColor);
        }
        _canvas.drawCircle(posX, posY, mRadius, mItemPaintArea);
        _canvas.drawCircle(posX, posY, mRadius, mItemPaint);
        _canvas.drawText(_s, posX - 3 - mBounds.width() / 2 , posY + mBounds.height() / 2, mItemTextPaint);
    }

    private void drawLine(Canvas _canvas, PointF _a, PointF _b, int _times) {

        float xA = _a.x;
        float yA = _a.y;
        float xB = 0;
        float yB = 0;
        float x = (float) (Math.cos(45) * mRadius);

        if(_times < 5) {
            if (_times == 1) {
                xB = _b.x - x;
                yB = _b.y + x + (mMinDistanceY);
            } else if (_times == 2) {
                xB = _b.x + x + (mMinDistanceX / 8);
                yB = _b.y + x + mMinDistanceY / 8;
            } else if (_times == 3) {
                xB = _b.x - (x - (x * 2));
                yB = _b.y + x + (mMinDistanceY);
            } else if (_times == 4) {
                xB = (_b.x - x) + (x * 2);
                yB = _b.y + x + (mMinDistanceY);
            } else if (_times == -1) {
                xB = 0;
                yB = 0;
            }

            xB = xB + mMaxWidth / 2;
            xA = xA + mMaxWidth / 2;

        } else {
            if(_times == 5) {
                xB = (_b.x - x) + (x * 2);
                yB = _b.y + x + (mMinDistanceY);
            } else if (_times == 6) {
                xB = _b.x + x + (mMinDistanceX / 8);
                yB = _b.y + x + mMinDistanceY / 8;
            } else if (_times == 7) {
                xB = _b.x - (x - (x * 2));
                yB = _b.y + x + mMinDistanceY;
            } else if (_times == 8) {
                xB = _b.x + x + (mMinDistanceX / 4);
                yB = _b.y + x;
            } else if (_times == 9) {
                xB = _b.x + x;
                yB = _b.y + x + (mMinDistanceY);
            } else if (_times == 10) {
                xB = _b.x - x;
                yB = _b.y + x + (mMinDistanceY);
            }

            xB = mMaxWidth / 2 - xB;
            xA = mMaxWidth / 2 - xA;
        }

        _canvas.drawLine(xA, yA, xB, yB, mItemPaint);
    }

    private void drawAnimation(Canvas _canvas, BinaryTreeNode _node, BinaryTreeNode _old, PointF _currPoint, PointF _oldPoint) {

        if (_node == null)
            return;

        int height = mTreeNumbers.getHeight(_node.data);
        int times = 1;
        int depth = mTreeNumbers.getDepth(_node.data);

        Log.i(TAG, "PRINT: " + _node.data + " NODE OLD: " + _old.data + " , DEPTH : " + mTreeNumbers.getHeight(_node.data));
        if( _old.data > _node.data) { // go in left tree
            _currPoint.y = (mRadius * 4) + ((mRadius * 2) * depth + (mMinDistanceY * depth));
            if(mTreeNumbers.root.data < _node.data) { // go in left subtree from right tree
                _currPoint.x = _oldPoint.x - mMinDistanceX;

                drawLine(_canvas,_currPoint, _oldPoint, times);
                drawCircle(_canvas, _currPoint, _oldPoint, String.valueOf(_node.data), true);
            } else {
                if (_node.right == null && _node.left == null) {
                    _currPoint.x = _oldPoint.x + mMinDistanceX;
                    times = 9;
                } else if (_node.right == null && _node.left != null) {
                    _currPoint.x = _oldPoint.x + mMinDistanceX;
                    times = 7;
                } else if (_node.right != null && _node.left != null && _old.right == null) {
                    _currPoint.x = _currPoint.x + mMinDistanceX * (depth + 1);
                } else if (_node.right != null && mTreeNumbers.root == _old) {
                    _currPoint.x = mMinDistanceX * 2;
                    times = 8;
                }  else if (_node.right != null && _node.left != null && _old.right != null) {
                    _currPoint.x = _oldPoint.x + mMinDistanceX * depth;
                    times = 6;
                } else if (_node.right != null && _node.left != null) {
                    _currPoint.x = mMinDistanceX * (depth + 1);
                } else if (_node.right != null) {
                    _currPoint.x = mMinDistanceX;
                }
                drawLine(_canvas,_currPoint, _oldPoint, times);
                drawCircle(_canvas, _currPoint, _oldPoint, String.valueOf(_node.data), false);
            }
            _node.setPoint(_currPoint);

        } else if (_old.data < _node.data) { // go in right tree
            _currPoint.y = (mRadius * 4) + ((mRadius * 2) * depth) + (mMinDistanceY * depth);
            if(mTreeNumbers.root.data > _node.data) { // go in right subtree from left tree
                times = 7;
                if(_node.left != null && _node.right != null) {
                    _currPoint.x = _currPoint.x - (mMinDistanceX * (depth));
                } else if(_node.left == null && _node.right == null) {
                    times = 10;
                    _currPoint.x = _oldPoint.x - mMinDistanceX;
                } else if (_node.left != null && _node.right == null) {
                    times = 10;
                }
                _node.setPoint(_currPoint);
                drawLine(_canvas,_currPoint, _oldPoint,times);
                drawCircle(_canvas, _currPoint, _oldPoint, String.valueOf(_node.data), false);
            } else { // go in right tree
                if(_node.left == null && mTreeNumbers.root == _old) {
                    times = 4;
                } else if(_node.left != null && mTreeNumbers.root == _old) {
                    _currPoint.x = mMinDistanceX * 2;
                    times = 2;
                } else if(_node.left == null && _node.right == null) {
                    _currPoint.x = _oldPoint.x + mMinDistanceX;
                    times = 4;
                }  else if(_node.left != null && _node.right == null) {
                    _currPoint.x = _oldPoint.x + (mMinDistanceX * (height - 1));
                    times = 4;
                } else if(_node.left == null && _node.right != null) {
                    _currPoint.x = _oldPoint.x + mMinDistanceX;
                    times = 3;
                } else {
                    _currPoint.x = mMinDistanceX * (depth);
                    times = 3;
                }
                _node.setPoint(_currPoint);
                drawLine(_canvas,_currPoint, _oldPoint,times);
                drawCircle(_canvas, _currPoint, _oldPoint, String.valueOf(_node.data), true);
            }
        } else {
            _node.setPoint(_currPoint);
            drawCircle(_canvas, _currPoint, _oldPoint, String.valueOf(_node.data), true);
        }

        if(_node.left != null) {
            drawAnimation(_canvas, _node.left, _node, _node.left.point, _node.point);
        }

        if(_node.right != null) {
            drawAnimation(_canvas, _node.right, _node, _node.right.point, _node.point);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent _event) {

        if (_event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = _event.getX();
            float y = _event.getY();
            int i = 0;
            for (BinaryTreeNode n : mTree) {
                if(x < n.point.x + mRadius && x > n.point.x - mRadius && y < n.point.y + mRadius && y > n.point.y - mRadius) {
                    mTouched = true;
                    mPosition = i;
                    invalidate();
                    break;
                } else {
                    mPosition = -1;
                }
                i++;
                invalidate();
            }
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
