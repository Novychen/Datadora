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

import at.fhooe.mc.datadora.BinarySearchTree.Animation.Add;
import at.fhooe.mc.datadora.BinarySearchTree.Animation.BSTValue;
import at.fhooe.mc.datadora.BinarySearchTree.Animation.InOrder;
import at.fhooe.mc.datadora.Operation;
import at.fhooe.mc.datadora.R;

public class BSTPointerView extends BSTView {

    private static final String TAG = "BSTPointerView : ";

    private float mX;
    private float mY;

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
        mAdd = new Add(mSurfaceColor, mPrimaryColor, mOnPrimaryColor, mOnSurfaceColor,  mValues);
        mAdd.setPointer(true);
        mAdd.setPointerAnim();
        mAdd.addUpdateListener((Animator.AnimatorListener) this);
        mAdd.addUpdateListener((ValueAnimator.AnimatorUpdateListener) this);
        mInOrder = new InOrder();
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


    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        if(mValues.getCurrentOperation() == Operation.ADD) {
            mAdd.animate();
            mAdd.animatePointer();
            mAdd.drawAddedElement(mValue, _canvas);
            mAdd.drawComparision(mValue, mCount, _canvas, mOnSurfaceColor, mPrimaryColor);
        }
        drawTree(_canvas);
    }

    @Override
    protected void animateOperations(Canvas _canvas, BinaryTreeNode _n) {
        if (_n.isSelected()) {
            if(mValues.getCurrentOperation() == Operation.ADD) {
                mAdd.animate();
                _canvas.drawCircle(_n.getPoint().x, _n.getPoint().y, mValues.getRadius(), mValues.getAnimPaint());
            }
        } else {
            mValues.getItemPaint().setColor(mPrimaryColor);
            mValues.getItemPaint().setStrokeWidth(6);
            mValues.getItemPaint().setStyle(Paint.Style.STROKE);
            mValues.getItemTextPaint().setColor(mOnSurfaceColor);
        }
        _canvas.drawCircle(_n.getPoint().x, _n.getPoint().y, mValues.getRadius(), mValues.getItemPaint());
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
            mCount = mAdd.animEnd(_animation, mCount,mActivity, mValue);
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
