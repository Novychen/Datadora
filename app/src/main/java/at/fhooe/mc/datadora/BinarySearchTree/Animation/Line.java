package at.fhooe.mc.datadora.BinarySearchTree.Animation;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.DashPathEffect;
import android.graphics.Path;
import android.graphics.PathMeasure;

import at.fhooe.mc.datadora.Operation;

public class Line {

    private static final String TAG = "Line : ";

    protected static final String PROPERTY_DRAW_LINE = "PROPERTY_DRAW_LINE_RIGHT";

    private Path mPath = new Path();
    private final PathMeasure mPathMeasure = new PathMeasure();

    // the current draw process - used for the drawing animation
    private float mDrawProcess;

    private final BSTValue mValues;

    // Animator for the drawing animation
    private final ValueAnimator mAnimatorDrawLine = new ValueAnimator();

    public void setPath(Path _path) {
        mPath = _path;
    }

    public Line(BSTValue _values) {
        mValues = _values;
    }

    private float getLength(Path _path) {
        mPathMeasure.setPath(_path, false);
        return mPathMeasure.getLength();
    }

    public void start() {
        float length = getLength(mPath);
        PropertyValuesHolder propertyDrawLine = PropertyValuesHolder.ofFloat(PROPERTY_DRAW_LINE, 0, length);
        mAnimatorDrawLine.setValues(propertyDrawLine);
        mAnimatorDrawLine.setDuration(300);
        mAnimatorDrawLine.start();
        animateLines();
    }

    public void addUpdateValueAnimator(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorDrawLine.addUpdateListener(_listener);
    }

    public void addUpdateValueAnimator(Animator.AnimatorListener _listener) {
        mAnimatorDrawLine.addListener(_listener);
    }

    public void update(ValueAnimator _animation) {
        if (_animation == mAnimatorDrawLine) {
            mDrawProcess = (float) _animation.getAnimatedValue(PROPERTY_DRAW_LINE);
        }
    }

    public void animateLines() {
        DashPathEffect drawEffect = new DashPathEffect(new float[]{mDrawProcess, getLength(mPath)}, 0);
        mValues.getAnimPaint().setPathEffect(drawEffect);
    }


    public void animEnd(Animator _animator) {
        if(_animator == mAnimatorDrawLine) {
            mValues.setCurrentOperation(Operation.NONE);
        }
    }


    public boolean isRunning() {
        return mAnimatorDrawLine.isRunning();
    }
}
