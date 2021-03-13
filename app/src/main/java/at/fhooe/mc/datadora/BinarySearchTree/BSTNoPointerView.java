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

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.Animation.Add;
import at.fhooe.mc.datadora.BinarySearchTree.Animation.BSTValue;
import at.fhooe.mc.datadora.BinarySearchTree.Animation.InOrder;
import at.fhooe.mc.datadora.BinarySearchTree.Animation.Line;
import at.fhooe.mc.datadora.Operation;
import at.fhooe.mc.datadora.R;

public class BSTNoPointerView extends BSTView {

    private static final String TAG = "BSTNoPointerView : ";

    private Add mAdd;
    private Line mLine;
    private InOrder mInOrder;

    private int mValue;

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

    protected void init() {
        super.init();
        mAdd = new Add(mSurfaceColor,mPrimaryColor, mValues);
        mAdd.addUpdateListener((Animator.AnimatorListener) this);
        mAdd.addUpdateListener((ValueAnimator.AnimatorUpdateListener) this);

        mLine = new Line(mValues);
        mLine.addUpdateValueAnimator(this);
    }


    public void add(int _value) {
        super.add();
        mCount = 0;
        mAdd.getAddPath(mActivity.getTree(), _value);
        mAdd.start();
        mValue = _value;
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
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        if (mValues.getCurrentOperation() == Operation.ADD && mLine.isStarted() ) {
            mLine.animateLines();
        }
        drawTree(_canvas);
    }

    @Override
    public void onAnimationStart(Animator _animation) {
        if (mValues.getCurrentOperation() == Operation.ADD) {
            mAdd.animStart(_animation, mCount);
        } else if(mValues.getCurrentOperation() == Operation.INORDER) {
            mInOrder.animStart(_animation, mActivity.getBinding().BSTActivityVectorOutput, mCount);
        }
        invalidate();
    }

    @Override
    public void onAnimationEnd(Animator _animation) {
        if (mValues.getCurrentOperation() == Operation.ADD) {
            mCount = mAdd.animEnd(_animation, mCount);

            BinaryTreeNode n = mActivity.getTree().insertNode(mValue);
            mActivity.getTree().updateChildCount(mActivity.getTree().getRoot());
            mActivity.getTreeUser().add(n);

        } else if(mValues.getCurrentOperation() == Operation.INORDER) {
            mCount = mInOrder.animEnd(_animation, mCount);
        }
        invalidate();
    }

    @Override
    public void onAnimationCancel(Animator _animation) {
        if( mValues.getCurrentOperation() == Operation.ADD && !mAdd.isRunning()) {
            mLine.start();
        }
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
