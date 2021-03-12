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

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.Animation.Add;
import at.fhooe.mc.datadora.BinarySearchTree.Animation.BSTValue;
import at.fhooe.mc.datadora.BinarySearchTree.Animation.InOrder;
import at.fhooe.mc.datadora.Operation;
import at.fhooe.mc.datadora.R;

public class BSTNoPointerView extends BSTView {

    private static final String TAG = "BSTNoPointerView : ";

    private Add mAdd;
    private InOrder mInOrder;

    public BSTNoPointerView(Context context) {
        super(context);
        init();
    }

    public BSTNoPointerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BSTNoPointerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
