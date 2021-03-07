package at.fhooe.mc.datadora.LinkedList;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.Animation.LLValue;
import at.fhooe.mc.datadora.LinkedList.Animation.Pointer;

public class LLPointerView extends LinkedListView{

    private static final String TAG = "LLPointerView : ";

    // Animator for the appended element with pointers
    private final AnimatorSet mAnimatorAppendPointer = new AnimatorSet();

    Pointer mPointer;

    public LLPointerView(Context context) {
        super(context);
        super.init();
    }

    public LLPointerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        super.init();
    }

    public LLPointerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.init();
    }

    @Override
    protected void onSizeChanged(int _w, int _h, int _oldW, int _oldH) {
        super.onSizeChanged(_w, _h, _oldW, _oldH);
       // mValues.setMaxHeight(mMaxHeight);
       // mValues.setMaxWidth(mMaxWidth);

        setUp();
    }

    private void setUp() {
        mPointer = new Pointer(mValues.getItemPaint(), mValues.getCurrentOperation(), mValues.getLinkedListRec());
        setUpAnimationWithPointer();
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        drawAnimation(_canvas);
    }

    private void drawAnimation(Canvas _canvas) {
        for (int i = 0; i < mValues.getLinkedListNum().size(); i++) {
            prepareAnimation(i);
            animateOperation(_canvas, i);
            drawType(_canvas, i);
        }
    }

        /**
         * draws the get animation all operations
         * @param _canvas canvas on which the animation is painted
         * @param _pos the current position the loop from the onDraw is in
         */
    protected void animateOperation(Canvas _canvas, int _pos) {
        switch (mValues.getCurrentOperation()) {
            case Operation.APPEND: { } break;
            case Operation.PREPEND: { } break;
            case Operation.INSERT_AT: { } break;
            case Operation.DELETE_FIRST: { } break;
            case Operation.DELETE_AT: { } break;
            case Operation.CLEAR :
            case Operation.DELETE_LAST: { } break;
            case Operation.SUCCESSOR:
            case Operation.PREDECESSOR: { } break;
            case Operation.GET_FIRST: { } break;
            case Operation.GET_AT: { } break;
            case Operation.GET_LAST: { } break;
            case Operation.RANDOM: { } break;
            case Operation.GET_SIZE: { } break;
        }

        // save the size of the text ("box" around the text) in mBounds
        mValues.getItemTextPaint().getTextBounds(mValues.getLinkedListNum().get(_pos).toString(), 0, mValues.getLinkedListNum().get(_pos).toString().length(), mBounds);

        // set LinkedList item size
        mValues.getLinkedListRec().get(_pos).left = (int) ((mValues.getMaxWidth() / 1.5) - (mValues.getMaxWidth() / 8) - (mResize / 2));
        mValues.getLinkedListRec().get(_pos).right = (int) (mValues.getLinkedListRec().get(_pos).left + (mValues.getMaxWidth() / 4) + mResize);
        mValues.getLinkedListRec().get(_pos).bottom = (int) (mValues.getLinkedListRec().get(_pos).top + ((mValues.getMaxWidth() / 4) * mValues.getScale()) - 10);

        mPointer.drawPointers(_canvas, _pos);

        // get BoundingBox from Text & draw Text + LinkedList item
        _canvas.drawRoundRect(mValues.getLinkedListRec().get(_pos), mRadius, mRadius,  mValues.getItemPaint());
        _canvas.drawText(mValues.getLinkedListNum().get(_pos).toString(), getExactCenterX(mValues.getLinkedListRec().get(_pos)) - mBounds.exactCenterX(), (getExactCenterY(mValues.getLinkedListRec().get(_pos)) - mBounds.exactCenterY()),  mValues.getItemTextPaint());

    }

    private void setUpAnimationWithPointer() {
        mAnimatorGetArea.setRepeatCount(mValues.getLinkedListNum().size()-1);
        mAnimatorGetText.setRepeatCount(mValues.getLinkedListNum().size()-1);
        mAnimatorGetArea.setDuration(600);
        mAnimatorGetText.setDuration(600);

        mAnimatorAppendPointer.play(mAnimatorGetArea).with(mAnimatorGetText);
        mAnimatorAppendPointer.addListener(this);
    }

    public void append(int _value) {
        super.append(_value);
    }

    public void prepend(int _value) {
        super.prepend(_value);
    }

    public void insertAt(int _value, int _pos) {
        super.insertAt(_value, _pos);
    }

    public void deleteFirst() {
        super.deleteFirst();
    }

    public void deleteLast() {
        super.deleteLast();
    }

    public void deleteAt(int _pos) {
        super.deleteAt(_pos);
    }

    public void clear() {
        super.clear();
    }

    public void predecessor() {
        super.predecessor();
    }

    public void successor() {
        super.successor();
    }

    public void getSize() {
        super.getSize();
    }

    public void getFirst() {
        super.getFirst();
    }

    public void getLast() {
        super.getLast();
    }

    public void getAt(int _pos) {
        super.getAt(_pos);
    }

    protected void random(Vector<Integer> _list) {
        super.random(_list);
    }

}
