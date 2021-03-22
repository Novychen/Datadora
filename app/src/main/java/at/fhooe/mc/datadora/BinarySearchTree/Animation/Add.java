package at.fhooe.mc.datadora.BinarySearchTree.Animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTree;
import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;
import at.fhooe.mc.datadora.Operation;


public class Add {

    private static final String TAG = "Add : ";

    private final AnimatorSet mAddSet = new AnimatorSet();

    private final ValueAnimator mAreaColorAnim;
    private final ValueAnimator mTextColorAnim;
    private ValueAnimator mAlphaElementAnim;
    private ValueAnimator mAlphaCompAnim;

    private int mColorArea;
    private int mColorText;
    private int mAlphaElement;
    private int mAlphaCompare;

    private final BSTValue mValues;
    private final Rect mBoundsAddedElement = new Rect();
    private boolean mPointer;

    private Vector<BinaryTreeNode> mAddPath = new Vector<>();

    public void setPointer(boolean _pointer) {
        mPointer = _pointer;
    }

    public void setPointerAnim() {
        mAlphaElementAnim = ValueAnimator.ofInt(255, 0);
        mAlphaElementAnim.setDuration(700);
        mAlphaElementAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        mAlphaCompAnim = ValueAnimator.ofInt(0, 255);
        mAlphaCompAnim.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public Add(int _surfaceColor, int _primaryColor, int _onPrimaryColor, int _onSurfaceColor, BSTValue _values) {
        mValues = _values;

        mAreaColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), _surfaceColor, _primaryColor, _surfaceColor);
        mAreaColorAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        mTextColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), _onSurfaceColor, _onPrimaryColor, _onSurfaceColor);
        mTextColorAnim.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void addUpdateListener(ValueAnimator.AnimatorUpdateListener _listener) {
        if(mPointer) { mAlphaElementAnim.addUpdateListener(_listener); }
        mAreaColorAnim.addUpdateListener(_listener);
        mTextColorAnim.addUpdateListener(_listener);
    }

    public void addUpdateListener(Animator.AnimatorListener _listener) {
        mAreaColorAnim.addListener(_listener);
        if(mPointer) { mAlphaElementAnim.addListener(_listener); }
    }

    public void update(ValueAnimator _animation) {
        if(_animation == mAreaColorAnim) {
            mColorArea = (int) _animation.getAnimatedValue();
        } else if(_animation == mAlphaElementAnim) {
            mAlphaElement = (int) _animation.getAnimatedValue();
        } else if(_animation == mTextColorAnim) {
            mColorText = (int) _animation.getAnimatedValue();
        }
    }

    public void animate() {
        mValues.getAnimPaint().setColor(mColorArea);
        mValues.getAnimPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        mValues.getItemTextPaint().setColor(mColorText);
    }

    public void animatePointer() {
        if(mAlphaElementAnim.isRunning()) {
            mValues.getAnimPaint().setAlpha(mAlphaElement);
            mValues.getItemTextPaint().setAlpha(mAlphaElement);
        }
    }

    public void start() {
        int duration;
        int repeat;

        if (mAddPath.isEmpty()) {
           repeat = 0;
           duration = 200;
        } else {
           repeat = mAddPath.size() - 1;
            if(mPointer) { duration = 700 * mAddPath.size() - 1;
            } else { duration = 500 * mAddPath.size() - 1; }
        }

        mAreaColorAnim.setDuration(duration);
        mTextColorAnim.setDuration(duration);

        mAreaColorAnim.setRepeatCount(repeat);
        mTextColorAnim.setRepeatCount(repeat);

        if(mPointer) {
            mAlphaCompAnim.setDuration(duration);
            mAlphaCompAnim.setRepeatCount(repeat);

            mAddSet.play(mAreaColorAnim).with(mTextColorAnim).with(mAlphaCompAnim).before(mAlphaElementAnim);
            mAddSet.start();
        } else {
            mAreaColorAnim.start();
            mTextColorAnim.start();
        }
    }

    public void drawComparision(int _added, int _count, Canvas _canvas, int _onSurface, int _primary) {
        if(!mAddPath.isEmpty()) {
            mValues.getItemTextPaint().setColor(_onSurface);
            String s = _added + " > " + mAddPath.get(_count).getData();
            mValues.getItemTextPaint().getTextBounds(s, 0, s.length(), mBoundsAddedElement);

            float y = mValues.getTopSpace() + (mBoundsAddedElement.height() / 2f);
            float x = mValues.getTopSpace() + ((mValues.getMaxWidth() / 2) - mValues.getTopSpace()) / 2 - (mBoundsAddedElement.width() / 2f);

            mValues.getItemPaint().setColor(Color.WHITE);
            mValues.getItemPaint().setStyle(Paint.Style.FILL_AND_STROKE);
            _canvas.drawRoundRect(x - 20, y - mBoundsAddedElement.height() - 20, x + mBoundsAddedElement.width() + 20, y + 20, 8, 8, mValues.getItemPaint());
            _canvas.drawText(s, x, y, mValues.getItemTextPaint());

            mValues.getItemPaint().setColor(_primary);
            mValues.getItemPaint().setStyle(Paint.Style.STROKE);
        }
    }

    public void drawAddedElement(int _value,  Canvas _canvas) {

        String s = String.valueOf(_value);
        mValues.getItemTextPaint().getTextBounds(s, 0, s.length(), mBoundsAddedElement);

        _canvas.drawCircle(mValues.getTopSpace(), mValues.getTopSpace(), mValues.getRadius(), mValues.getAnimPaint());
        _canvas.drawText(s, mValues.getTopSpace() - 3 - mBoundsAddedElement.width() / 2f, mValues.getTopSpace() + mBoundsAddedElement.height() / 2f, mValues.getItemTextPaint());

        if(mAlphaElementAnim.isRunning()) {
            _canvas.drawCircle(mValues.getTopSpace(), mValues.getTopSpace(), mValues.getRadius(), mValues.getAnimPaint());
        } else {
            _canvas.drawCircle(mValues.getTopSpace(), mValues.getTopSpace(), mValues.getRadius(), mValues.getItemPaint());
        }
    }

    public void getAddPath(BinarySearchTree _tree, int _data){

       BinaryTreeNode n = _tree.getRoot();

        while (n != null) {

            if (_data == n.getData()) {
                mAddPath.add(n);
                return;
            }

            if (_data > n.getData()) {
                mAddPath.add(n);
                if (n.getRight() == null) {
                    return;
                }
                n = n.getRight();
            } else {
                mAddPath.add(n);
                if (n.getLeft() == null) {
                    return;
                }
                n = n.getLeft();
            }
        }
    }

    public void animStart(Animator _animation, int _count) {
        if( _animation == mAreaColorAnim && !mAddPath.isEmpty()) {
            mAddPath.get(_count).setSelected(true);
        }
    }

    public int animRepeat(Animator _animation, int _count) {
        if(_animation == mAreaColorAnim && !mAddPath.isEmpty()) {
            mAddPath.get(_count).setSelected(false);
            _count++;
            mAddPath.get(_count).setSelected(true);
        }
        return _count;
    }

    public int animEnd(Animator _animation, int _count, BinarySearchTreeActivity _activity, int _value) {
        if(_animation == mAreaColorAnim && !mAddPath.isEmpty()) {
            mAddPath.get(_count).setSelected(false);
            _count = 0;
        }

        if((_animation == mAreaColorAnim && !mPointer) || (_animation == mAlphaElementAnim && mPointer)) {
            BinaryTreeNode n = _activity.getTree().insertNode(_value);
            _activity.getTree().updateChildCount(_activity.getTree().getRoot());
            _activity.getTreeUser().add(n);
            if(_animation == mAlphaElementAnim) { mValues.setCurrentOperation(Operation.NONE); }
        }

        return _count;
    }



    public boolean isRunning() {
        return mAreaColorAnim.isRunning();
    }
}
