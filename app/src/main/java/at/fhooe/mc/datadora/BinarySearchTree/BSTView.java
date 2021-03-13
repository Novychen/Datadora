package at.fhooe.mc.datadora.BinarySearchTree;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.Animation.BSTValue;
import at.fhooe.mc.datadora.Operation;
import at.fhooe.mc.datadora.R;

public class BSTView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private static final String TAG = "BSTView : ";

    // the current primary color of the currently used theme
    protected final int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    protected final int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    protected final int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    protected final int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    protected BinarySearchTreeActivity mActivity;
    protected int mCount = 0;

    protected final PointF mBegin = new PointF();
    protected final PointF mEnd = new PointF();

    protected BinarySearchTree mTree;
    protected float mLength = 25;

    protected float mX;
    protected float mY;

    // Rect in order to save the TextBounds from the current number
    protected final Rect mBounds = new Rect();
    protected boolean mMove = false;
    protected boolean mDown = false;

    protected BSTValue mValues;

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

    public void setSwitch(boolean _pointer){
        if(_pointer) {
            mActivity.getBinding().BSTActivityView.setVisibility(GONE);
            mActivity.getBinding().BSTActivityPointerView.setVisibility(VISIBLE);
        } else {
            mActivity.getBinding().BSTActivityView.setVisibility(VISIBLE);
            mActivity.getBinding().BSTActivityPointerView.setVisibility(GONE);
        }
    }

    protected void init() {
        mActivity = ((BinarySearchTreeActivity) getContext());

        Paint mItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint mItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mItemPaint.setColor(mPrimaryColor);
        mItemPaint.setStyle(Paint.Style.STROKE);
        mItemPaint.setStrokeCap(Paint.Cap.ROUND);
        mItemPaint.setStrokeWidth(6);

        mItemTextPaint.setColor(mOnSurfaceColor);
        mItemTextPaint.setTextSize(50);

        mValues = BSTValue.getInstance(mItemPaint, mItemTextPaint, -1, Operation.NONE);
    }

    public void move(boolean _selected) {
        mMove = _selected;
    }

    public void setTranslate(float _x, float _y) {
        mX = _x;
        mY = _y;
    }

    public float getTranslateX() {
        return mX;
    }

    public float getTranslateY() {
        return mY;
    }

    public void add() {
        mValues.setCurrentOperation(Operation.ADD);
        invalidate();
    }

    public boolean remove() {
        if (mValues.getPosition() != -1) {
            int data = mActivity.getTreeUser().get(mValues.getPosition()).getData();
            mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", data));
            mActivity.getTree().remove(data);
            mActivity.getTreeUser().remove(mValues.getPosition());
            invalidate();
            mValues.setPosition(-1);
            return true;
        }
        return false;
    }

    public void clear() {
        mValues.setCurrentOperation(Operation.CLEAR);
        invalidate();
    }

    public boolean depth() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {
            mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mActivity.getTree().getDepth(mActivity.getTreeUser().get(mValues.getPosition()).getData())));
            return true;
        }
        return false;
    }

    public boolean height() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {
            mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mActivity.getTree().getHeight(mActivity.getTreeUser().get(mValues.getPosition()).getData())));
            return true;
        }
        return false;
    }

    public void getExternal() {
        invalidate();
    }

    public void getInternal() {
        invalidate();
    }

    public void max(int _pos) {
        mValues.setCurrentOperation(Operation.MAX);
        mValues.setPosition(_pos);
        invalidate();
    }

    public void min(int _pos) {
        mValues.setCurrentOperation(Operation.MIN);
        mValues.setPosition(_pos);
        invalidate();
    }

    public boolean hasParent() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode parent = mActivity.getTree().getParentNode(mActivity.getTreeUser().get(mValues.getPosition()).getData());

            if (parent != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return false;
        }
        return true;
    }

    public boolean hasRightChild() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getRight() != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return false;
        }
        return true;
    }

    public boolean hasLeftChild() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getLeft() != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return false;
        }
        return true;
    }

    public boolean isRoot() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (mActivity.getTree().getRoot() == n) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return false;
        }
        return true;
    }

    public boolean isInternal() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getRight() != null || n.getLeft() != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return false;
        }
        return true;
    }

    public boolean isExternal() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getRight() == null && n.getLeft() == null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return false;
        }
        return true;
    }

    public boolean getParentNode() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {
            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());
            BinaryTreeNode parent = mActivity.getTree().getParentNode(n.getData());

            if (parent != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", parent.getData()));
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText("-");
            }
            return true;
        }
        return false;
    }

    public boolean getRightChild() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getRight() != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", n.getRight().getData()));
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText("-");
            }
            return false;
        }
        return true;
    }

    public boolean getLeftChild() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getLeft() != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", n.getLeft().getData()));
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText("-");
            }
            return false;
        }
        return true;
    }

    public void inOrder() {
        mValues.setCurrentOperation(Operation.INORDER);
    }

    @Override
    protected void onSizeChanged(int _w, int _h, int _oldW, int _oldH) {
        super.onSizeChanged(_w, _h, _oldW, _oldH);

        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());

        // size of parent of this view
        mValues.setMaxWidth(_w - xpad - 6);
    }

    @Override
    protected void onMeasure(int _widthMeasureSpec, int _heightMeasureSpec) {
        super.onMeasure(_widthMeasureSpec, _heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        drawTree(_canvas);
    }

    protected void drawTree(Canvas _canvas) {

        mTree = mActivity.getTree();
        Vector<BinaryTreeNode> tv = mTree.toArrayPreOrder();

        if (mTree.getRoot() != null) {
            float x;
            float y;

            if (mX != 0 && mY != 0) {
                x = mTree.getRoot().getPoint().x;
                y = mTree.getRoot().getPoint().y;
            } else {
                x = mValues.getMaxWidth() / 2;
                y = mValues.getTopSpace();
            }

            if (mMove) {
                x = x + (mX);
                y = y + (mY);
            }
            mTree.getRoot().setPoint(x, y);
        }

        if (tv != null) {
            for (int i = 0; i < mActivity.getTreeUser().size(); i++) {

                BinaryTreeNode n = tv.get(i);
                BinaryTreeNode nPre = mTree.getParentNode(n.getData());

                if (n != mTree.getRoot()) {
                    setPointOfNode(n, nPre);
                    prepareAndDrawLine(n, nPre, _canvas);
                }

                String s = String.valueOf(n.getData());
                mValues.getItemTextPaint().getTextBounds(s, 0, s.length(), mBounds);

                if (n.isSelected()) {
                    mValues.getItemPaint().setColor(mPrimaryColor);
                    mValues.getItemPaint().setStyle(Paint.Style.FILL_AND_STROKE);
                    mValues.getItemTextPaint().setColor(mOnPrimaryColor);
                } else {
                    mValues.getItemPaint().setColor(mPrimaryColor);
                    mValues.getItemPaint().setStrokeWidth(6);
                    mValues.getItemPaint().setStyle(Paint.Style.STROKE);

                    mValues.getItemTextPaint().setColor(mOnSurfaceColor);
                }

                _canvas.drawCircle(n.getPoint().x, n.getPoint().y, mValues.getRadius(), mValues.getItemPaint());
                _canvas.drawText(s, n.getPoint().x - 3 - mBounds.width() / 2f, n.getPoint().y + mBounds.height() / 2f, mValues.getItemTextPaint());
            }
        }
    }

    protected void setPointOfNode(BinaryTreeNode _n, BinaryTreeNode _nPre) {
        float direction = getDirection(_n, _nPre);

        float xPre = _nPre.getPoint().x;
        float yPre = _nPre.getPoint().y;

        int minDistanceX = 80;

        float x = (_n.getChildCount() * minDistanceX * direction) + xPre;
        float y;
        if (mY == 0) {
            y = (minDistanceX * mTree.getDepth(_n.getData())) + mValues.getTopSpace() + mY;
        } else {
            y = yPre + minDistanceX;
        }
        _n.setPoint(x, y);
    }

    protected void prepareAndDrawLine(BinaryTreeNode _n, BinaryTreeNode _nPre, Canvas _canvas) {

        float x = calculateDiffX(_n, _nPre);
        float y = calculateDiffY(_n, _nPre);

        float direction = getDirection(_n, _nPre);

        double lengthX = Math.cos(Math.atan(y / x));
        double lengthY = Math.cos(Math.atan(x / y));

        float xPre = (float) (_nPre.getPoint().x + (lengthX * direction) * mValues.getRadius());
        float yPre = (float) (_nPre.getPoint().y + lengthY * mValues.getRadius());

        x = (float) (_n.getPoint().x - (lengthX * direction) * mValues.getRadius());
        y = (float) (_n.getPoint().y - lengthY * mValues.getRadius());

        _canvas.drawLine(xPre, yPre, x, y, mValues.getItemPaint());
    }

    protected float calculateDiffX(BinaryTreeNode _n, BinaryTreeNode _nPre) {
        return _n.getPoint().x - _nPre.getPoint().x;
    }

    protected float calculateDiffY(BinaryTreeNode _n, BinaryTreeNode _nPre) {
        return _n.getPoint().y - _nPre.getPoint().y;
    }

    protected float getDirection(BinaryTreeNode _n, BinaryTreeNode _nPre) {
        if (_nPre != null && _n.getData() < _nPre.getData()) {
            return -1;
        } else {
            return 1;
        }
    }

    protected void selectNode(float _x, float _y) {
        int i = 0;
        float space = mValues.getRadius() / 1.5f;
        for (BinaryTreeNode n : mActivity.getTreeUser()) {
            if (_x < n.getPoint().x + space && _x > n.getPoint().x - space && _y < n.getPoint().y + space && _y > n.getPoint().y - space) {
                n.setSelected(true);
                mValues.setPosition(i);
                invalidate();
            } else {
                n.setSelected(false);
            }
            i++;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent _event) {

        if (_event.getAction() == MotionEvent.ACTION_DOWN) {
            mDown = true;

            float x = _event.getX();
            float y = _event.getY();
            if (!mMove) {
                selectNode(x, y);
            } else {
                mBegin.x = x;
                mBegin.y = y;
            }

            mActivity.getBinding().BSTActivityReturnValue.setText("");
            invalidate();
        } else if (_event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mMove) {
                float x = _event.getX();
                float y = _event.getY();

                if (mDown) {
                    mDown = false;
                } else {
                    mBegin.x = mEnd.x;
                    mBegin.y = mEnd.y;
                }

                mEnd.x = x;
                mEnd.y = y;

                mX = mEnd.x - mBegin.x;
                mY = mEnd.y - mBegin.y;

                invalidate();
            }
        }
        return true;
    }


    @Override
    public void onAnimationStart(Animator _animation) {

    }

    @Override
    public void onAnimationEnd(Animator _animation) {

    }

    @Override
    public void onAnimationCancel(Animator _animation) {

    }

    @Override
    public void onAnimationRepeat(Animator _animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator _animation) {

    }
}
