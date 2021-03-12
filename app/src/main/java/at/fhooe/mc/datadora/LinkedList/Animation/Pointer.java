package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Pointer {

    private static final String TAG = "Pointer : ";

    protected static final String PROPERTY_DRAW_LINE = "PROPERTY_DRAW_LINE_RIGHT";
    protected static final String PROPERTY_DRAW_LINE_LONG = "PROPERTY_DRAW_LINE_LONG";

    private final Path mPath = new Path();
    private final PathMeasure mPathMeasure = new PathMeasure();
    private boolean mDraw;

    // the current draw process - used for the drawing animation
    private int mDrawProcess;
    private int mDrawProcessLong;

    private final LLValue mValues;

    private float mStrokeWidth;
    private final float height = 76 / 2f;
    private final float lineHeight = 66;
    private final float lineWidth = 55;
    private final double angle = Math.toRadians(45);

    private boolean mBigPointer;
    private boolean mSingle;

    // Animator for the drawing animation
    private final ValueAnimator mAnimatorDrawLine = new ValueAnimator();

    private final ValueAnimator mAnimatorDrawLongLine = new ValueAnimator();

    public Pointer(LLValue _values) {
        mValues = _values;
    }


    public boolean isBigPointer() {
        return mBigPointer;
    }

    public void setBigPointer(boolean _bigPointer) {
        mBigPointer = _bigPointer;
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float _strokeWidth) {
        mStrokeWidth = _strokeWidth;
    }


    public boolean isSingle() {
        return mSingle;
    }

    public void setSingle(boolean _single) {
        mSingle = _single;
    }

    public void drawPointerForSingleItem(Canvas _canvas, RectF _rect) {
        float length = 10;
        boolean end = true;
        double angle = Math.toRadians(45);

        float y = (float) (Math.sin(angle) * length);
        float x = (float) (Math.cos(angle) * length);

        PointF p = drawLine(_canvas, height, lineHeight, lineWidth, true, end, _rect);
        drawArrow(_canvas, p, x, y, true, end);

        y = (float) (Math.sin(Math.toRadians(180) - angle) * length);
        x = (float) (Math.cos(Math.toRadians(180) - angle) * length);

        drawExternalPointer(_canvas, mValues.getLinkedListRec().get(mValues.getLinkedListRec().size() - 1), "p", 25);

        if (!mBigPointer && !mSingle) {
            p = drawLine(_canvas, height - _rect.height() - 10, lineHeight, lineWidth, false, end, _rect);
            drawArrow(_canvas, p, x, y, false, end);
        }
    }

    public void drawBigPointer(Canvas _canvas, RectF _rect) {
        float length = 10;
        boolean end = false;
        double angle = Math.toRadians(45);

        startLong();
        float y = (float) (Math.sin(Math.toRadians(180) - angle) * length);
        float x = (float) (Math.cos(Math.toRadians(180) - angle) * length);

        PointF p = drawLine(_canvas, height - _rect.height() - 10, lineHeight + (mValues.getMaxWidth() / 8), lineWidth, false, end, _rect);
        drawArrow(_canvas, p, x, y, false, end);
        animateLongLines();
    }

    public void drawExternalPointer(Canvas _canvas, RectF _rect, String _text, float _offset) {
        Rect bounds = new Rect();
        mValues.getItemTextPaint().getTextBounds(_text, 0, _text.length(), bounds);

        float y = (_rect.top + height - 10) + (bounds.height() / 2f);
        float x = _rect.left - 100 - (bounds.width() * 2);

        _canvas.drawText(_text, x, y + _offset, mValues.getItemTextPaint());
        _canvas.drawLine(_rect.left - 100, _rect.top + height - 10 + _offset, _rect.left, _rect.top + height - 10 + _offset, mValues.getItemPaint());
    }

    public void drawPointers(Canvas _canvas, int _pos) {
        float length;
        boolean end;

        if (_pos == mValues.getLinkedListRec().size() - 1) {
            length = 10;
            end = true;
        } else {
            length = 20;
            end = false;
        }

        float y = (float) (Math.sin(angle) * length);
        float x = (float) (Math.cos(angle) * length);

        PointF p = drawLine(_canvas, height, lineHeight, lineWidth, true, end, mValues.getLinkedListRec().get(_pos));
        drawArrow(_canvas, p, x, y, true, end);
        mValues.getItemPaint().setStrokeWidth(6);

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
        if(!mSingle) {
            p = drawLine(_canvas, height - mValues.getLinkedListRec().get(_pos).height() - 10, lineHeight, lineWidth, false, end, mValues.getLinkedListRec().get(_pos));
            drawArrow(_canvas, p, x, y, false, end);
        }
        // mItemPaint.setColor(mPrimaryColor);
    }

    private PointF drawLine(Canvas _canvas, float _height, float _lineHeight, float _lineWidth, boolean right, boolean end, RectF _rect) {

        float x;
        int y = (int) (_rect.top - _height);

        if (right) {
            x = _rect.right;
        } else {
            x = _rect.left;
        }

        mPath.moveTo(x, y + _lineHeight);

        if (right && end) {
            mPath.cubicTo(x, y + _lineHeight, x, y + _lineHeight, x + (_lineWidth / 2), y + _lineHeight);
        } else if (right) {
            mPath.cubicTo(x + _lineWidth, y + _lineHeight, x + _lineWidth, y, x, y);
            animateLines();
        } else if (end) {
            mPath.moveTo(x, y);
            mPath.cubicTo(x, y, x - _lineWidth, y, x, y);
        } else {
            mPath.moveTo(x, y);
            mPath.cubicTo(x - _lineWidth, y, x - _lineWidth, y + _lineHeight, x, y + _lineHeight);
            animateLines();
        }

        _canvas.drawPath(mPath, mValues.getItemPaint());
        mPath.reset();
        mValues.getItemPaint().setPathEffect(null);

        if (right && end) {
            return new PointF(x + _lineWidth / 2, y + _lineHeight);
        } else if (right) {
            return new PointF(x, y);
        } else if (end) {
            return new PointF(x - _lineWidth / 2, y);
        } else {
            return new PointF(x, y + _lineHeight);
        }
    }

    private void drawArrow(Canvas _canvas, PointF _p, float x, float y, boolean right, boolean _end) {

        float paddingY;
        float paddingX;
        float paddingX2;

        if (right) {
            paddingY = 4;
            paddingX = 0;
            paddingX2 = 0;
        } else {
            paddingY = 0;
            paddingX = 2;
            paddingX2 = 4;
        }

        if (!_end) {
            _canvas.drawLine(_p.x, _p.y, _p.x + x + paddingX, _p.y - y + paddingY, mValues.getItemPaint());
            _canvas.drawLine(_p.x + x + paddingX2, _p.y + y, _p.x, _p.y, mValues.getItemPaint());
        } else {
            _canvas.drawLine(_p.x, _p.y + y, _p.x, _p.y - y, mValues.getItemPaint());
        }
    }

    private float getLength(Path _path) {
        mPathMeasure.setPath(_path, false);
        return mPathMeasure.getLength();
    }

    private void startLong() {
        float length = getLength(mPath);
        PropertyValuesHolder propertyDrawLine = PropertyValuesHolder.ofInt(PROPERTY_DRAW_LINE_LONG, 0, (int) length);
        mAnimatorDrawLongLine.setValues(propertyDrawLine);
        mAnimatorDrawLongLine.setDuration(700);
        mAnimatorDrawLongLine.start();
    }

    private void start() {
        float length = getLength(mPath);
        PropertyValuesHolder propertyDrawLine = PropertyValuesHolder.ofInt(PROPERTY_DRAW_LINE, 0, (int) length);
        mAnimatorDrawLine.setValues(propertyDrawLine);
        mAnimatorDrawLine.setDuration(700);
        mAnimatorDrawLine.start();
        mDraw = true;
    }

    public void addUpdateValueAnimator(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimatorDrawLine.addUpdateListener(_listener);
        mAnimatorDrawLongLine.addUpdateListener(_listener);
    }

    public void update(ValueAnimator _animation) {
        if (_animation == mAnimatorDrawLine) {
            mDrawProcess = (int) _animation.getAnimatedValue(PROPERTY_DRAW_LINE);
        } else if (_animation == mAnimatorDrawLongLine) {
            mDrawProcessLong = (int) _animation.getAnimatedValue(PROPERTY_DRAW_LINE_LONG);
        }
    }

    public void animateLines() {
        if (!mDraw) {
            start();
        }

        DashPathEffect drawEffect = new DashPathEffect(new float[]{mDrawProcess, getLength(mPath)}, 0);
        mValues.getItemPaint().setPathEffect(drawEffect);
    }

    public void animateLongLines() {
        DashPathEffect drawEffect = new DashPathEffect(new float[]{mDrawProcessLong, getLength(mPath)}, 0);
        mValues.getItemPaint().setPathEffect(drawEffect);
    }

}
