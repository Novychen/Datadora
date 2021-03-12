package at.fhooe.mc.datadora.BinarySearchTree.Animation;

import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;

public class BSTValue {

    private Paint mItemPaint;
    private Paint mItemTextPaint;
    private int mPosition;
    private int mCurrentOperation;
    private float mMaxWidth;
    private final float mRadius = 50;
    private final float mTopSpace = 80;

    public static BSTValue mInstance;

    private BSTValue(){

    }

    public static BSTValue getInstance(Paint _item, Paint _text, int _pos, int _operation) {
        if(mInstance == null) {
            mInstance = new BSTValue(_item, _text, _pos,_operation);
        }
        return mInstance;
    }

    private BSTValue(Paint _item, Paint _text, int _pos, int _operation) {
            mItemPaint = _item;
            mItemTextPaint = _text;
            mPosition = _pos;
            mCurrentOperation = _operation;
    }

    public Paint getItemPaint() {
        return mItemPaint;
    }

    public void setItemPaint(Paint _itemPaint) {
        mItemPaint = _itemPaint;
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

    public float getRadius() { return mRadius; }

    public float getTopSpace() { return mTopSpace; }
}