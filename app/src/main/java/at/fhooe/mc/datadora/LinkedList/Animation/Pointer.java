package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.Operation;

public class Pointer {

    private final Path mPath = new Path();
    private final PathMeasure mPathMeasure = new PathMeasure();
    private boolean mDraw;

    // the current draw process - used for the drawing animation
    private int mDrawProcess;

    private final Paint mItemPaint;
    private int mCurrentOperation;
    private final Vector<RectF> mLinkedList;

    protected static final String PROPERTY_DRAW_LINE = "PROPERTY_DRAW_LINE_RIGHT";

    // Animator for the drawing animation
    private final ValueAnimator mAnimatorDrawLine = new ValueAnimator();

    public Pointer(Paint _paint, int _operation, Vector<RectF> _list) {
        mItemPaint = _paint;
        mCurrentOperation = _operation;
        mLinkedList = _list;
    }

    public int getCurrentOperation() {
        return mCurrentOperation;
    }

    public void setCurrentOperation(int _currentOperation) {
        mCurrentOperation = _currentOperation;
    }

    public void drawPointers(Canvas _canvas, int _pos) {

        float length;
        double angle = Math.toRadians(45);
        boolean end;

        float height = 76 / 2f;
        float lineHeight = 66;
        float lineWidth = 55;

        if (_pos == mLinkedList.size() - 1) {
            length = 10;
            end = true;
        } else {
            length = 20;
            end = false;
        }

        float y = (float) (Math.sin(angle) * length);
        float x = (float) (Math.cos(angle) * length);

        PointF p = drawLine(_canvas, _pos, height, lineHeight, lineWidth, true, end);
        drawArrow(_canvas, p, x, y,true, end);

        if (_pos == 0) {
            length = 10;
            end = true;
        } else {
            length = 20;
            end = false;
        }

        y = (float) (Math.sin(Math.toRadians(180) - angle) * length);
        x = (float) (Math.cos(Math.toRadians(180) - angle) * length);
        //    mItemPaint.setColor(mSecondaryColor);     // If 2 color then need to make new mItemPaint for second one and give it in the methods params otherwise alpha of animation is overwritten
        p = drawLine(_canvas, _pos, height - mLinkedList.get(_pos).height() - 10 , lineHeight, lineWidth, false, end);
        drawArrow(_canvas, p, x, y, false, end);

        // mItemPaint.setColor(mPrimaryColor);
    }

    private PointF drawLine(Canvas _canvas, int _pos, float _height, float _lineHeight, float _lineWidth, boolean right, boolean end) {

        float x;
        int y = (int) (mLinkedList.get(_pos).top - _height);

        if (right) { x = mLinkedList.get(_pos).right;
        } else { x = mLinkedList.get(_pos).left; }

        mPath.moveTo(x, y + _lineHeight);

        if (right && end) {
            mPath.cubicTo(x, y + _lineHeight, x , y + _lineHeight, x + (_lineWidth / 2 ), y + _lineHeight);
        } else if (right) {
            mPath.cubicTo(x + _lineWidth, y + _lineHeight, x + _lineWidth, y, x, y);
            animateLines(_pos);
        } else if (end) {
            mPath.moveTo(x, y);
            mPath.cubicTo(x, y, x - _lineWidth, y, x, y);
        } else {
            mPath.moveTo(x, y);
            mPath.cubicTo(x - _lineWidth, y, x - _lineWidth, y + _lineHeight, x, y + _lineHeight);
            animateLines(_pos);
        }

        _canvas.drawPath(mPath, mItemPaint);
        mPath.reset();
        mItemPaint.setPathEffect(null);

        if (right && end) { return new PointF(x + _lineWidth / 2, y + _lineHeight);
        } else if (right) { return new PointF(x, y);
        } else if (end) { return new PointF(x - _lineWidth / 2, y);
        } else { return new PointF(x, y + _lineHeight); }
    }

    private void drawArrow(Canvas _canvas, PointF _p, float x, float y, boolean right, boolean _end) {

        float paddingY;
        float paddingX;
        float paddingX2;

        if(right) {
            paddingY = 4;
            paddingX = 0;
            paddingX2 = 0;
        } else {
            paddingY = 0;
            paddingX = 2;
            paddingX2 = 4;
        }

        if(!_end) {
            _canvas.drawLine(_p.x, _p.y, _p.x + x + paddingX, _p.y - y + paddingY, mItemPaint);
            _canvas.drawLine(_p.x + x + paddingX2, _p.y + y, _p.x, _p.y, mItemPaint);
        } else {
            _canvas.drawLine(_p.x,_p.y + y, _p.x,_p.y - y, mItemPaint);
        }
    }

    private float getLength(Path _path) {
        mPathMeasure.setPath(_path,false);
        return mPathMeasure.getLength();
    }

    private void startDrawAnimator() {
        float length = getLength(mPath);
        PropertyValuesHolder propertyDrawLine = PropertyValuesHolder.ofInt(PROPERTY_DRAW_LINE, 0, (int) length);
        mAnimatorDrawLine.setValues(propertyDrawLine);
        mAnimatorDrawLine.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                if(_animation == mAnimatorDrawLine){
                    mDrawProcess = (int) _animation.getAnimatedValue(PROPERTY_DRAW_LINE);
                }
            }
        });

        mAnimatorDrawLine.setDuration(700);
        mAnimatorDrawLine.start();
        mDraw = true;
    }

    private void animateLines(int _pos) {
        if(mCurrentOperation == Operation.PREPEND && _pos == 0 || mCurrentOperation == Operation.APPEND && _pos == mLinkedList.size() - 1) {

            if(!mDraw) { startDrawAnimator(); }

            DashPathEffect drawEffect = new DashPathEffect(new float[]{mDrawProcess, getLength(mPath)}, 0);
            mItemPaint.setPathEffect(drawEffect);
        }
    }

}
