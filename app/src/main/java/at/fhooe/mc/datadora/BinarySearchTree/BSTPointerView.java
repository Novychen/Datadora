package at.fhooe.mc.datadora.BinarySearchTree;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.Animation.Add;
import at.fhooe.mc.datadora.BinarySearchTree.Animation.BSTValue;
import at.fhooe.mc.datadora.BinarySearchTree.Animation.InOrder;
import at.fhooe.mc.datadora.Operation;
import at.fhooe.mc.datadora.R;

public class BSTPointerView extends BSTView {

    private static final String TAG = "BSTPointerView : ";

    private float mX;
    private float mY;

    private BSTValue mValues;
    private Add mAdd;
    private InOrder mInOrder;

    public BSTPointerView(Context context) {
        super(context);
        init();
    }

    public BSTPointerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BSTPointerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        super.init();

        mAdd = new Add(mSurfaceColor, mPrimaryColor, mValues);
        mAdd.addUpdateListener((Animator.AnimatorListener) this);
        mAdd.addUpdateListener((ValueAnimator.AnimatorUpdateListener) this);

        mInOrder = new InOrder();
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
        mCount = 0;
        mAdd.getAddPath(mActivity.getTree(), mActivity.getTreeUser().get(mActivity.getTreeUser().size() - 1).getData());
        mAdd.start();
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
            return true;
        }
        return false;
    }

    public boolean hasRightChild() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getRight() != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean hasLeftChild() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getLeft() != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean isRoot() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (mActivity.getTree().getRoot() == n) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean isInternal() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getRight() != null || n.getLeft() != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean isExternal() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getRight() == null && n.getLeft() == null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
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
            return true;
        }
        return false;
    }

    public boolean getLeftChild() {
        if (mValues.getPosition() != -1 && mActivity.getTreeUser().get(mValues.getPosition()).isSelected()) {

            BinaryTreeNode n = mActivity.getTreeUser().get(mValues.getPosition());

            if (n.getLeft() != null) {
                mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", n.getLeft().getData()));
            } else {
                mActivity.getBinding().BSTActivityReturnValue.setText("-");
            }
            return true;
        }
        return false;
    }

    public void inOrder() {
        mValues.setCurrentOperation(Operation.INORDER);
        mCount = 0;
        mInOrder = new InOrder(mSurfaceColor, mPrimaryColor, mActivity.getTree().toInOrder(true));
        mInOrder.addUpdateListener((Animator.AnimatorListener) this);
        mInOrder.addUpdateListener((ValueAnimator.AnimatorUpdateListener) this);
        mInOrder.start();
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

    private void drawArrows(BinaryTreeNode _n, BinaryTreeNode _nPre, float _x, float _y, Canvas _canvas) {

        float x = calculateDiffX(_n, _nPre);
        float y = calculateDiffY(_n, _nPre);

        double alpha = Math.atan(y / x);

        double beta = Math.toRadians(45) - alpha;
        double yArrow = Math.sin(beta) * mLength;
        double xArrow = Math.cos(beta) * mLength;

        double ceta = Math.toRadians(180) - (alpha + Math.toRadians(45));
        double yArrow2 = Math.sin(ceta) * mLength;
        double xArrow2 = Math.cos(ceta) * mLength;

        float direction = getDirection(_n, _nPre);

        float lengthX = (float) (_x - (xArrow) * direction);
        float lengthY = (float) (_y - yArrow);

        float lengthX2 = (float) (_x + (xArrow2) * direction);
        float lengthY2 = (float) (_y - yArrow2);

        if (alpha != Math.toRadians(-45) && alpha != Math.toRadians(45)) {
            if (direction == 1) {
                lengthY = (float) (_y + yArrow);
            } else {
                lengthY2 = (float) (_y + yArrow2);
                lengthY = (float) (_y - yArrow);
            }
        }
        _canvas.drawLine(_x, _y, lengthX2, lengthY2, mValues.getItemPaint());
        _canvas.drawLine(lengthX, lengthY, _x, _y, mValues.getItemPaint());
    }

    private void drawNull(Canvas _canvas, BinaryTreeNode _n) {
        double angle = Math.toRadians(45);
        float smallLength = mLength / 2f;

        float y = (float) (Math.sin(angle) * mValues.getRadius());
        float x = (float) (Math.cos(angle) * mValues.getRadius());
        y = _n.getPoint().y + y;

        if (_n.getRight() == null) {
            float xEnd = _n.getPoint().x + x;
            _canvas.drawLine(xEnd, y, xEnd + mLength, y + mLength, mValues.getItemPaint());

            float smallY = (float) (Math.sin(angle) * smallLength);
            float smallX = (float) (Math.cos(angle) * smallLength);

            _canvas.drawLine((xEnd + mLength) - smallX, (y + mLength) + smallY, (xEnd + mLength) + smallX, (y + mLength) - smallY, mValues.getItemPaint());
        }

        if (_n.getLeft() == null) {
            float xEnd = _n.getPoint().x - x;
            _canvas.drawLine(xEnd, y, xEnd - mLength, y + mLength, mValues.getItemPaint());

            float smallY = (float) (Math.sin(angle) * smallLength);
            float smallX = (float) (Math.cos(angle) * smallLength);

            _canvas.drawLine((xEnd - mLength) - smallX, (y + mLength) - smallY, (xEnd - mLength) + smallX, (y + mLength) + smallY, mValues.getItemPaint());
        }
    }

    @Override
    protected void setPointOfNode(BinaryTreeNode _n, BinaryTreeNode _nPre) {
        float direction = getDirection(_n, _nPre);

        float xPre = _nPre.getPoint().x;
        float yPre = _nPre.getPoint().y;

        int minDistanceX = 100;

        float x = (_n.getChildCount() * minDistanceX * direction) + xPre;
        float y;
        if (mY == 0) {
            y = (minDistanceX * mTree.getDepth(_n.getData())) + mValues.getTopSpace() + mY;
        } else {
            y = yPre + minDistanceX;
        }
        _n.setPoint(x, y);
    }

    @Override
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

        drawArrows(_n, _nPre, x, y, _canvas);
        drawNull(_canvas, _n);
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
        mInOrder.animStart(_animation, mActivity.getBinding().BSTActivityVectorOutput, mCount);
        mAdd.animStart(_animation, mCount);
        invalidate();
    }

    @Override
    public void onAnimationEnd(Animator _animation) {
        if (mValues.getCurrentOperation() == Operation.ADD) {
            mCount = mAdd.animEnd(_animation, mCount);
        } else if(mValues.getCurrentOperation() == Operation.INORDER) {
            mCount = mInOrder.animEnd(_animation, mCount);
        }
        invalidate();
    }

    @Override
    public void onAnimationCancel(Animator _animation) {

    }

    @Override
    public void onAnimationRepeat(Animator _animation) {
        if (mValues.getCurrentOperation() == Operation.ADD) {
            mCount = mAdd.animRepeat(_animation, mCount);
        } else if(mValues.getCurrentOperation() == Operation.INORDER) {
            mCount = mInOrder.animRepeat(_animation, mActivity.getBinding().BSTActivityVectorOutput, mCount);
        }
        invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator _animation) {
        if (mValues.getCurrentOperation() == Operation.ADD) {
            mAdd.update(_animation);
        } else if(mValues.getCurrentOperation() == Operation.INORDER) {
            mInOrder.update(_animation);
        }
        invalidate();
    }
}
