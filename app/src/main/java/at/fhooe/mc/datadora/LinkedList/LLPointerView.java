package at.fhooe.mc.datadora.LinkedList;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.Animation.Append;
import at.fhooe.mc.datadora.LinkedList.Animation.Pointer;
import at.fhooe.mc.datadora.Operation;

public class LLPointerView extends LinkedListView {

    private static final String TAG = "LLPointerView : ";

    private Append mAppend;
    private boolean mAppendEnd = false;
    private boolean mAppendItemEnd = false;

    private RectF mNewItem = new RectF();
    private Integer mCurrentNum;

    private Pointer mPointer;
    private boolean mSingleList;

    public void setSingleList(boolean _list) {
        mSingleList = _list;
    }

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
        setUp();
    }

    private void setUp() {
        mPointer = new Pointer(mValues);
        mPointer.addUpdateValueAnimator(this);
        mPointer.setSingle(mSingleList);
        setUpAnimationWithPointer();

        mAppend = new Append(mValues);
        mAppend.setPointer(true);
        mAppend.setUp();
        mAppend.addUpdateAnimator(this);
        mAppend.addUpdateValueAnimator(this);
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        drawAnimation(_canvas);
        afterAnimation(_canvas);
    }

    private void drawAnimation(Canvas _canvas) {
        for (int i = 0; i < mValues.getLinkedListNum().size(); i++) {
            prepareAnimation(i);
            animateOperation(_canvas, i);
            drawType(_canvas, i);
        }
        afterAnimationList(_canvas);
    }

    private void afterAnimationList(Canvas _canvas) {
        switch (mValues.getCurrentOperation()) {
            case Operation.APPEND: {
               // if(mAppendEnd) {
                    mAppend.animateNewElement(mValues.getLinkedListNum().size(), mNewItem);

                    // save the size of the text ("box" around the text) in mBounds
                    mValues.getItemTextPaint().getTextBounds(mCurrentNum.toString(), 0, mCurrentNum.toString().length(), mBounds);

                    // set LinkedList item size
                    mNewItem.left = (int) ((mValues.getMaxWidth() / 1.5) - (mValues.getMaxWidth() / 8) - (mResize / 2));
                    mNewItem.right = (int) (mNewItem.left + (mValues.getMaxWidth() / 4) + mResize);
                    mNewItem.bottom = (int) (mNewItem.top + ((mValues.getMaxWidth() / 4) * mValues.getScale()) - 10);

                    mPointer.drawPointerForSingleItem(_canvas, mNewItem);
                    mPointer.drawExternalPointer(_canvas, mNewItem, "n",0);

                    // get BoundingBox from Text & draw Text + LinkedList item
                    _canvas.drawRoundRect(mNewItem, mRadius, mRadius, mValues.getItemPaint());
                    _canvas.drawText(mCurrentNum.toString(), getExactCenterX(mNewItem) - mBounds.exactCenterX(), (getExactCenterY(mNewItem) - mBounds.exactCenterY()), mValues.getItemTextPaint());
                    mAppend.animateOperation(mPointer, _canvas, mNewItem);
                //}
            }
        }
    }

    /**
     * draws the get animation all operations
     *
     * @param _canvas canvas on which the animation is painted
     * @param _pos    the current position the loop from the onDraw is in
     */
    protected void animateOperation(Canvas _canvas, int _pos) {

        switch (mValues.getCurrentOperation()) {
            case Operation.APPEND: {
                if (_pos == mValues.getPosition()) {
                    mAppend.animateWidth(mPointer, _canvas, _pos);
                    _canvas.drawRoundRect(mValues.getLinkedListRec().get(_pos), mRadius, mRadius, mValues.getItemPaint());
                }
            }
            break;
        }
        mValues.getItemPaint().setStrokeWidth(6);

        // save the size of the text ("box" around the text) in mBounds
        mValues.getItemTextPaint().getTextBounds(mValues.getLinkedListNum().get(_pos).toString(), 0, mValues.getLinkedListNum().get(_pos).toString().length(), mBounds);

        // set LinkedList item size
        mValues.getLinkedListRec().get(_pos).left = (int) ((mValues.getMaxWidth() / 1.5) - (mValues.getMaxWidth() / 8) - (mResize / 2));
        mValues.getLinkedListRec().get(_pos).right = (int) (mValues.getLinkedListRec().get(_pos).left + (mValues.getMaxWidth() / 4) + mResize);
        mValues.getLinkedListRec().get(_pos).bottom = (int) (mValues.getLinkedListRec().get(_pos).top + ((mValues.getMaxWidth() / 4) * mValues.getScale()) - 10);

        mPointer.drawPointers(_canvas, _pos);

        // get BoundingBox from Text & draw Text + LinkedList item
        _canvas.drawRoundRect(mValues.getLinkedListRec().get(_pos), mRadius, mRadius, mValues.getItemPaint());
        _canvas.drawText(mValues.getLinkedListNum().get(_pos).toString(), getExactCenterX(mValues.getLinkedListRec().get(_pos)) - mBounds.exactCenterX(), (getExactCenterY(mValues.getLinkedListRec().get(_pos)) - mBounds.exactCenterY()), mValues.getItemTextPaint());
    }

    private void setUpAnimationWithPointer() {
        mAnimatorGetArea.setRepeatCount(mValues.getLinkedListNum().size() - 1);
        mAnimatorGetText.setRepeatCount(mValues.getLinkedListNum().size() - 1);
        mAnimatorGetArea.setDuration(600);
        mAnimatorGetText.setDuration(600);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator _animation) {
        super.onAnimationUpdate(_animation);
        mAppend.update(_animation);
        mPointer.update(_animation);

    }

    @Override
    public void onAnimationRepeat(Animator _animation) {
        super.onAnimationRepeat(_animation);
        mAppend.animRepeat(_animation);
    }

    @Override
    public void onAnimationEnd(Animator _animation) {
        super.onAnimationEnd(_animation);
        mAppendEnd = mAppend.animEnd(_animation, mCurrentNum);
    }

    protected void afterAnimation(Canvas _canvas) {
        super.afterAnimation(_canvas);
    }

    protected void head() {
        super.head();
        invalidate();
    }

    protected void tail() {
        super.tail();
        invalidate();
    }

    protected void both() {
        super.both();
        invalidate();
    }

    public void append(int _value) {
        super.append(_value);
        mAppendEnd = false;
        mAppendItemEnd = false;
        mCurrentNum = _value;
        mAppend.start();
        mValues.setPosition(0);
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
