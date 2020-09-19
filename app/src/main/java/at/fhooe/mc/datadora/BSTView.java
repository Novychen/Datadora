package at.fhooe.mc.datadora;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BSTView extends View {
    private static final String TAG = "BSTView : ";
    private Paint mBSTItemPaint = new Paint();
    private Paint mBSTItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);;

    // the current primary color of the currently used theme
    int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    public BSTView(Context context) {
        super(context);
        init();
    }

    public BSTView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BSTView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBSTItemPaint.setColor(mPrimaryColor);
        mBSTItemPaint.setStyle(Paint.Style.STROKE);
        mBSTItemPaint.setStrokeWidth(6);


        mBSTItemTextPaint.setColor(mOnSurfaceColor);
        mBSTItemTextPaint.setTextSize(55);
    }
    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);

    }
}
