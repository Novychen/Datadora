package at.fhooe.mc.datadora.BinarySearchTree;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
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

    private Path mPath = new Path();
    private boolean mStartLine = false;

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
        mAdd = new Add(mSurfaceColor,mPrimaryColor, mOnPrimaryColor, mOnSurfaceColor, mValues);
        mAdd.setPointer(false);
        mAdd.addUpdateListener((Animator.AnimatorListener) this);
        mAdd.addUpdateListener((ValueAnimator.AnimatorUpdateListener) this);

        mLine = new Line(mValues);
        mLine.addUpdateValueAnimator((Animator.AnimatorListener) this);
        mLine.addUpdateValueAnimator((ValueAnimator.AnimatorUpdateListener) this);
    }


    public void add(int _value) {
        super.add();
        mStartLine = false;
        mCount = 0;
        mAdd.getAddPath(mActivity.getTree(), _value);
        mAdd.start();
        mValue = _value;
    }

    public void random() {
        mValues.setCurrentOperation(Operation.RANDOM);
        invalidate();
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
    protected void animateOperations(Canvas _canvas, BinaryTreeNode _n) {
        if (_n.isSelected()) {
            if(mValues.getCurrentOperation() == Operation.ADD) {
                mAdd.animate();
                _canvas.drawCircle(_n.getPoint().x, _n.getPoint().y, mValues.getRadius(), mValues.getAnimPaint());
            } else {
                mValues.getItemPaint().setColor(mPrimaryColor);
                mValues.getItemPaint().setStyle(Paint.Style.FILL_AND_STROKE);
                mValues.getItemTextPaint().setColor(mOnPrimaryColor);
            }
        } else {
            mValues.getItemPaint().setColor(mPrimaryColor);
            mValues.getItemPaint().setStrokeWidth(6);
            mValues.getItemPaint().setStyle(Paint.Style.STROKE);
            mValues.getItemTextPaint().setColor(mOnSurfaceColor);
        }
        _canvas.drawCircle(_n.getPoint().x, _n.getPoint().y, mValues.getRadius(), mValues.getItemPaint());
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

        if(_n.getData() == mValue && !mLine.isRunning() && mStartLine) {
            mPath.reset();
            mPath.moveTo(xPre, yPre);
            mPath.lineTo(x, y);
            mLine.setPath(mPath);
            mLine.start();
        }

        if (_n.getData() == mValue && mLine.isRunning()) {
            mLine.animateLines();
            _canvas.drawLine(xPre, yPre, x, y, mValues.getAnimPaint());
        } else if(_n.getData() != mValue && mLine.isRunning()) {
            _canvas.drawLine(xPre, yPre, x, y, mValues.getItemPaint());
        } else {
            _canvas.drawLine(xPre, yPre, x, y, mValues.getItemPaint());
        }
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
            mCount = mAdd.animEnd(_animation, mCount,mActivity,mValue);
            mLine.animEnd(_animation);
            mStartLine = !mStartLine;
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
            if(!mAdd.isRunning()) {
                mLine.update(_animation);
            }
        } else if(mValues.getCurrentOperation() == Operation.INORDER) {
            mInOrder.update(_animation);
        }
        invalidate();
    }
}
