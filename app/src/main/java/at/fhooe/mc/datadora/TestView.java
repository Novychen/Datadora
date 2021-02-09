package at.fhooe.mc.datadora;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import static java.lang.Math.PI;

public class TestView extends View {

    private static final String PROPERTY_TRANSLATE_1 = "PROPERTY_TRANSLATE_1";
    private static final String PROPERTY_TRANSLATE_2 = "PROPERTY_TRANSLATE_2";
    private static final String PROPERTY_TRANSLATE_3 = "PROPERTY_TRANSLATE_3";
    private static final String PROPERTY_TRANSLATE_4 = "PROPERTY_TRANSLATE_4";

    private final Path mPath = new Path();
    private final Paint mItemPaint = new Paint();

    int progress = 1;


    int mTranslate1;
    int mTranslate2;
    int mTranslate3;
    int mTranslate4;
    PathDashPathEffect d;

    boolean start = false;
    Polygon polygon = new Polygon(5, 0xffe84c65, 362f, 2);


    public TestView(Context context) {
        super(context);

    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mItemPaint.setColor(Color.BLACK);
        mItemPaint.setStyle(Paint.Style.STROKE);
        mItemPaint.setStrokeCap(Paint.Cap.ROUND);
        mItemPaint.setStrokeWidth(10);
        mTranslate2 = (int) polygon.getLength();
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void start() {

        final ValueAnimator animator = new ValueAnimator();

        PropertyValuesHolder property1 = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_1, 0, 100);
        PropertyValuesHolder property2 = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_2,  0 , (int) polygon.getLength());
        PropertyValuesHolder property3 = PropertyValuesHolder.ofInt(PROPERTY_TRANSLATE_3, 400, 200);

        animator.setValues(property1, property2, property3);
        animator.setDuration(6000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_1);
                mTranslate2 = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_2);
             //   mTranslate3 = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_3);
             //   mTranslate4 = (int) animation.getAnimatedValue(PROPERTY_TRANSLATE_4);
                invalidate();
            }
        });
        start = true;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);

        int beginX = 200;
        int beginY = 400;

        int firstBezierX = 300;
        int firstBezierY = 400;

        int secondBezierX = 300;
        int secondBezierY = 300;

        int endX = 200;
        int endY = 300;

        beginY = 300;

        firstBezierY = 300;

        secondBezierY = 400;

        endY = 400;

        mPath.moveTo(beginX, beginY);
        mPath.cubicTo(firstBezierX, firstBezierY, secondBezierX, secondBezierY, endX, endY);

        DashPathEffect progressEffect = new DashPathEffect(new float[] {mTranslate2, polygon.getLength()}, 0);
        mItemPaint.setPathEffect(progressEffect);
        _canvas.drawPath(mPath, mItemPaint);
        mPath.reset();
    }

    private class Polygon {

        Path mPath;
        int sides;
        float length;

        public Polygon(int sides, int color, float radius, int laps) {
            this.sides = sides;
            mPath = createPath(sides, radius);
        }

        public Path getPath() {
            return mPath;
        }

        public float whatever() {
            return (1f - (1f / (2 * sides))) * length;
        }

        public float getLength() {
            PathMeasure p = new PathMeasure();
            p.setPath(mPath,false);
            length = p.getLength();
            return length;
        }

        private Path createPath(int sides, float radius) {
            Path path = new Path();
            
            float cx = 1080 / 2f;
            float cy = 1080 / 2f;

            double angle = 2.0 * PI / sides;
            double startAngle = PI / 2.0 + Math.toRadians(360.0 / (2 * sides));
            path.moveTo(cx + (float) (radius * Math.cos(startAngle)), cy + (float) (radius * Math.sin(startAngle)));
            for (int i = 1; i <= sides; i++) {
                path.lineTo(cx + (float) (radius * Math.cos(startAngle - angle * i)), cy + (float) (radius * Math.sin(startAngle - angle * i)));
            }
            path.close();
            return path;
        }
    }
}
