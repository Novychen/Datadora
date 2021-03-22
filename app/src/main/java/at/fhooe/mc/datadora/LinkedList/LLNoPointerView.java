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
import at.fhooe.mc.datadora.LinkedList.Animation.DeleteAt;
import at.fhooe.mc.datadora.LinkedList.Animation.DeleteFirst;
import at.fhooe.mc.datadora.LinkedList.Animation.DeleteLast;
import at.fhooe.mc.datadora.LinkedList.Animation.InsertAt;
import at.fhooe.mc.datadora.LinkedList.Animation.Prepend;
import at.fhooe.mc.datadora.LinkedList.Animation.Random;
import at.fhooe.mc.datadora.Operation;

public class LLNoPointerView extends LinkedListView {

    private static final String TAG = "LLNoPointerView : ";

    private Append mAppend;
    private Prepend mPrepend;
    private InsertAt mInsertAt;
    private DeleteFirst mDeleteFirst;
    private DeleteAt mDeleteAt;
    private DeleteLast mDeleteLast;
    private Random mRandom;

    public LLNoPointerView(Context context) {
        super(context);
        super.init();
    }

    public LLNoPointerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        super.init();
    }

    public LLNoPointerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.init();
    }

    @Override
    protected void onSizeChanged(int _w, int _h, int _oldW, int _oldH) {
        super.onSizeChanged(_w, _h, _oldW, _oldH);

        Vector<Integer> v = null;
        if(mActivity != null) {
            //Visualize the vector in the Shared Preferences
            v = mActivity.loadFromSave();
        }
        if (v != null) {
            mValues.getLinkedListNum().clear();
            mValues.getLinkedListRec().clear();
            for (int i = 0; i < v.size(); i++) {
                mValues.getLinkedListNum().add(v.get(i));
                mValues.getLinkedListRec().add(new RectF());
            }
            mValues.setCurrentOperation(Operation.NONE);
            reScale();
        }
        setUp();
    }

    private void setUp() {
        mAppend = new Append(mValues);
        mAppend.addUpdateValueAnimator((ValueAnimator.AnimatorUpdateListener) this);
        mAppend.setPointer(false);

        mPrepend = new Prepend(mValues);
        mPrepend.addUpdateValueAnimator(this);

        mInsertAt = new InsertAt(mValues);
        mInsertAt.addUpdateValueAnimator(this);

        mDeleteFirst = new DeleteFirst(mValues);
        mDeleteFirst.addUpdateValueAnimator(this);

        mDeleteLast = new DeleteLast(mValues);
        mDeleteLast.addUpdateValueAnimator(this);

        mDeleteAt = new DeleteAt(mValues);
        mDeleteAt.addUpdateValueAnimator(this);

        mRandom = new Random(mValues);
        mRandom.addUpdateValueAnimator((ValueAnimator.AnimatorUpdateListener) this);
        mRandom.addUpdate((Animator.AnimatorListener) this);
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
                mAppend.animateNoPointer(_pos);
            }
            break;
            case Operation.PREPEND: {
                mPrepend.animateNoPointer(_pos);
            }
            break;
            case Operation.INSERT_AT: {
                mInsertAt.animateNoPointer(_pos);
            }
            break;
            case Operation.DELETE_FIRST: {
                mDeleteFirst.animateNoPointer(_pos);
            }
            break;
            case Operation.DELETE_AT: {
                mDeleteAt.animateNoPointer(_pos);
            }
            break;
            case Operation.CLEAR:
            case Operation.DELETE_LAST: {
                mDeleteLast.animateNoPointer(_pos);
            }
            break;
            case Operation.SUCCESSOR:
            case Operation.PREDECESSOR: {
                if (mTouched && _pos == mValues.getPosition()) {
                    drawGet(_canvas, _pos);
                }
            }
            break;
            case Operation.GET_FIRST: {
                if (_pos == 0) {
                    drawGet(_canvas, _pos);
                }
            }
            break;
            case Operation.GET_AT:
            case Operation.GET_SIZE: {
                if (_pos == mValues.getPosition()) {
                    drawGet(_canvas, _pos);
                }
            }
            break;
            case Operation.GET_LAST: {
                if (_pos == mValues.getLinkedListRec().size() - 1) {
                    drawGet(_canvas, _pos);
                }
            }
            break;
            case Operation.RANDOM: {
                mRandom.animateNoPointer(_pos);
            }
            break;
        }

        // save the size of the text ("box" around the text) in mBounds
        mValues.getItemTextPaint().getTextBounds(mValues.getLinkedListNum().get(_pos).toString(), 0, mValues.getLinkedListNum().get(_pos).toString().length(), mBounds);

        // set LinkedList item size
        mValues.getLinkedListRec().get(_pos).left = (int) ((mValues.getMaxWidth() / 1.5) - (mValues.getMaxWidth() / 8) - (mResize / 2));
        mValues.getLinkedListRec().get(_pos).right = (int) (mValues.getLinkedListRec().get(_pos).left + (mValues.getMaxWidth() / 4) + mResize);
        mValues.getLinkedListRec().get(_pos).bottom = (int) (mValues.getLinkedListRec().get(_pos).top + ((mValues.getMaxWidth() / 4) * mValues.getScale()) - 10);

        // get BoundingBox from Text & draw Text + LinkedList item
        _canvas.drawRoundRect(mValues.getLinkedListRec().get(_pos), mRadius, mRadius,  mValues.getItemPaint());
        _canvas.drawText(mValues.getLinkedListNum().get(_pos).toString(), getExactCenterX(mValues.getLinkedListRec().get(_pos)) - mBounds.exactCenterX(), (getExactCenterY(mValues.getLinkedListRec().get(_pos)) - mBounds.exactCenterY()),  mValues.getItemTextPaint());
    }

    protected void afterAnimation(Canvas _canvas) {
        super.afterAnimation(_canvas);
        if (mValues.getCurrentOperation() == Operation.DELETE_FIRST) {
            mDeleteFirst.afterAnimation();
        } else if (mValues.getCurrentOperation() == Operation.DELETE_AT) {
            mDeleteAt.afterAnimation();
        } else if (mValues.getCurrentOperation() == Operation.DELETE_LAST) {
            mDeleteLast.afterAnimation();
        }
    }

    public void append(int _value) {
        super.append(_value);
        RectF r = new RectF();
        mValues.getLinkedListRec().add(r);
        mValues.getLinkedListNum().add(_value);
        reScale();
        mAppend.start();
    }

    public void prepend(int _value) {
        super.prepend(_value);
        mPrepend.start();
    }

    public void insertAt(int _value, int _pos) {
        super.insertAt(_value, _pos);
        mInsertAt.start();
    }

    public void deleteFirst() {
        super.deleteFirst();
        mDeleteFirst.start();
    }

    public void deleteLast() {
        super.deleteLast();
        mDeleteLast.start();
    }

    public void deleteAt(int _pos) {
        super.deleteAt(_pos);
        mDeleteAt.start();
    }

    public void clear() {
        super.clear();
        mDeleteLast.startAsClear();
    }

    public void predecessor() {
        super.predecessor();
    }

    public void successor() {
        super.successor();
    }

    public void getSize() {
        super.getSize();
        mAnimatorGetArea.setRepeatCount(mValues.getLinkedListNum().size() - 1);
        mAnimatorGetText.setRepeatCount(mValues.getLinkedListNum().size() - 1);
        mAnimatorGetArea.setDuration(600);
        mAnimatorGetText.setDuration(600);

        mAnimatorGetArea.start();
        mAnimatorGetText.start();
    }

    public void getFirst() {
        super.getFirst();
        mAnimatorGetText.setRepeatCount(0);
        mAnimatorGetArea.setRepeatCount(0);
        mAnimatorGetArea.start();
        mAnimatorGetText.start();
    }

    public void getLast() {
        super.getLast();
        mAnimatorGetText.setRepeatCount(0);
        mAnimatorGetArea.setRepeatCount(0);
        mAnimatorGetArea.start();
        mAnimatorGetText.start();
    }

    public void getAt(int _pos) {
        super.getAt(_pos);
        mAnimatorGetArea.setRepeatCount(0);
        mAnimatorGetText.setRepeatCount(0);
        mAnimatorGetArea.start();
        mAnimatorGetText.start();
    }

    protected void random(Vector<Integer> _list) {
        super.random(_list);
        mRandom.start(_list);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator _animation) {
        super.onAnimationUpdate(_animation);
        mAppend.update(_animation);
        mPrepend.update(_animation);
        mInsertAt.update(_animation);
        mDeleteFirst.update(_animation);
        mDeleteLast.update(_animation);
        mDeleteAt.update(_animation);
        mRandom.update(_animation);
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator _animation) {
        super.onAnimationStart(_animation);
        mRandom.animStart(_animation);
    }

    @Override
    public void onAnimationRepeat(Animator _animation) {
        super.onAnimationRepeat(_animation);
        mRandom.animRepeat(_animation);
    }
}
