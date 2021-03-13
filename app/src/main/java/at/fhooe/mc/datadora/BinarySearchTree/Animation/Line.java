package at.fhooe.mc.datadora.BinarySearchTree.Animation;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.DashPathEffect;
import android.graphics.Path;
import android.graphics.PathMeasure;

import at.fhooe.mc.datadora.LinkedList.Animation.LLValue;

public class Line {

    private static final String TAG = "Line : ";

    protected static final String PROPERTY_DRAW_LINE = "PROPERTY_DRAW_LINE_RIGHT";
    protected static final String PROPERTY_DRAW_LINE_LONG = "PROPERTY_DRAW_LINE_LONG";

    private Path mPath = new Path();
    private final PathMeasure mPathMeasure = new PathMeasure();

    // the current draw process - used for the drawing animation
    private int mDrawProcess;

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
        PropertyValuesHolder propertyDrawLine = PropertyValuesHolder.ofInt(PROPERTY_DRAW_LINE, 0, (int) length);
        mAnimatorDrawLine.setValues(propertyDrawLine);
        mAnimatorDrawLine.setDuration(700);
        mAnimatorDrawLine.start();
    }

    public void addUpdateValueAnimator(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorDrawLine.addUpdateListener(_listener);
    }

    public void update(ValueAnimator _animation) {
        if (_animation == mAnimatorDrawLine) {
            mDrawProcess = (int) _animation.getAnimatedValue(PROPERTY_DRAW_LINE);
        }
    }

    public void animateLines() {
        DashPathEffect drawEffect = new DashPathEffect(new float[]{mDrawProcess, getLength(mPath)}, 0);
        mValues.getItemPaint().setPathEffect(drawEffect);
    }

    public boolean isStarted() {
        return mAnimatorDrawLine.isStarted();
    }
}
