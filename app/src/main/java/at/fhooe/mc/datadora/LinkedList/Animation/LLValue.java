package at.fhooe.mc.datadora.LinkedList.Animation;

import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LLValue {

    private Vector<RectF> mLinkedListRec;
    private Vector<Integer> mLinkedListNum;
    private Vector<Integer> mLinkedListRand;
    private Paint mItemPaint;
    private Paint mItemTextPaint;

    private float mScale;
    private float mMaxHeight;
    private float mMaxWidth;
    private int mPosition;
    private int mCurrentOperation;

    public static LLValue mInstance;

    public LLValue(){

    }

    public static LLValue getInstance(Paint _item, Paint _text, Vector<RectF> _recs, Vector<Integer> _num, Vector<Integer> _rand, float _scale, float _height, float _width, int _pos, int _operation) {
        if(mInstance == null) {
            mInstance = new LLValue(_item, _text, _recs, _num, _rand, _scale, _height, _width, _pos,_operation);
        }
        return mInstance;
    }

    private LLValue(Paint _item, Paint _text, Vector<RectF> _recs, Vector<Integer> _num, Vector<Integer> _rand, float _scale, float _height, float _width, int _pos, int _operation) {
            mItemPaint = _item;
            mItemTextPaint = _text;
            mLinkedListRec = _recs;
            mLinkedListNum = _num;
            mLinkedListRand = _rand;

            mScale = _scale;
            mMaxHeight = _height;
            mMaxWidth = _width;
            mPosition = _pos;
            mCurrentOperation = _operation;
    }

    public Vector<RectF> getLinkedListRec() {
        return mLinkedListRec;
    }

    public void setLinkedListRec(Vector<RectF> _linkedList) {
        mLinkedListRec = _linkedList;
    }

    public Vector<Integer> getLinkedListNum() {
        return mLinkedListNum;
    }

    public void setLinkedListNum(Vector<Integer> _linkedList) {
        mLinkedListNum = _linkedList;
    }

    public Vector<Integer> getLinkedListRand() {
        return mLinkedListRand;
    }

    public void setLinkedListRand(Vector<Integer> _linkedList) {
        mLinkedListRand = _linkedList;
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

    public float getScale() {
        return mScale;
    }

    public void setScale(float _scale) {
        mScale = _scale;
    }

    public float getMaxHeight() {
        return mMaxHeight;
    }

    public void setMaxHeight(float _maxHeight) {
        mMaxHeight = _maxHeight;
    }

    public float getMaxWidth() {
        return mMaxWidth;
    }

    public void setMaxWidth(float _maxWidth) {
        mMaxWidth = _maxWidth;
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
}