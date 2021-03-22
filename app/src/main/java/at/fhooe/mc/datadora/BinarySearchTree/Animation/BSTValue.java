package at.fhooe.mc.datadora.BinarySearchTree.Animation;

import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;
import at.fhooe.mc.datadora.Operation;

public class BSTValue {

    private Paint mItemPaint;
    private Paint mAnimPaint;
    private Paint mItemTextPaint;
    private int mPosition = -1;
    private int mCurrentOperation = Operation.NONE;
    private float mMaxWidth;
    private float mMaxHeight;

    private PointF mBegin;
    private PointF mEnd;
    private boolean mMove = false;
    private boolean mDown = false;
    private boolean mCenterNode = false;
    private float mX;
    private float mY;

    public static BSTValue mInstance;

    private BSTValue(){

    }

    public static BSTValue getInstance(Paint _item, Paint _text, Paint _anim, PointF _begin, PointF _end) {
        if(mInstance == null) {
            mInstance = new BSTValue(_item, _text, _anim, _begin, _end);
        }
        return mInstance;
    }

    private BSTValue(Paint _item, Paint _text, Paint _anim, PointF _begin, PointF _end) {
        mItemPaint = _item;
        mItemTextPaint = _text;
        mAnimPaint = _anim;
        mBegin = _begin;
        mEnd = _end;
    }

    public Paint getItemPaint() {
        return mItemPaint;
    }

    public void setItemPaint(Paint _itemPaint) {
        mItemPaint = _itemPaint;
    }

    public Paint getAnimPaint() {
        return mAnimPaint;
    }

    public void setAnimPaint(Paint _animPaint) {
        mAnimPaint = _animPaint;
    }

    public Paint getItemTextPaint() {
        return mItemTextPaint;
    }

    public void setItemTextPaint(Paint _itemTextPaint) {
        mItemTextPaint = _itemTextPaint;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int _pos) {
        mPosition = _pos;
    }

    public int getCurrentOperation() {
        return mCurrentOperation;
    }

    public void setCurrentOperation(int _operation) {
        mCurrentOperation = _operation;
    }

    public float getMaxWidth() { return mMaxWidth; }

    public void setMaxWidth(float _maxWidth) { mMaxWidth = _maxWidth; }

    public float getMaxHeight() { return mMaxHeight; }

    public void setMaxHeight(float _maxHeight) { mMaxHeight = _maxHeight; }

    public float getRadius() { return 50f; }

    public float getTopSpace() { return 80f; }

    public PointF getBegin() { return mBegin; }

    public void setBegin(PointF _begin) { mBegin = _begin; }

    public PointF getEnd() { return mEnd; }

    public void setEnd(PointF _end) { mEnd = _end; }

    public boolean isMove() { return mMove; }

    public void setMove(boolean _move) { mMove = _move; }

    public boolean isDown() { return mDown; }

    public void setDown(boolean _down) { mDown = _down; }

    public boolean isCenterNode() { return mCenterNode; }

    public void setCenterNode(boolean _center) { mCenterNode = _center; }

    public float getX() { return mX; }

    public void setX(float _x) { mX = _x; }

    public float getY() { return mY; }

    public void setY(float _y) { mY = _y; }
}
