package at.fhooe.mc.datadora.BinarySearchTree;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.Vector;

import androidx.lifecycle.ViewModelProvider;
import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.RoomDatabase.BinarySearchTreeRoom;
import at.fhooe.mc.datadora.RoomDatabase.DatadoraViewModel;
import at.fhooe.mc.datadora.RoomDatabase.StackRoom;
import at.fhooe.mc.datadora.Stack.StackView;

public class BSTView extends View {

    private static final String TAG = "BSTView : ";
    private final Paint mItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mItemPaintArea = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mItemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // the current primary color of the currently used theme
    private final int mPrimaryColor = getResources().getColor(R.color.primaryColor, this.getContext().getTheme());

    // the current surface color of the currently used theme
    private final int mSurfaceColor = getResources().getColor(R.color.colorSurface, this.getContext().getTheme());

    // the current colorOnPrimary color of the currently used theme - for text
    private final int mOnPrimaryColor = getResources().getColor(R.color.colorOnPrimary, this.getContext().getTheme());

    // the current colorOnSurface color of the currently used theme - for text
    private final int mOnSurfaceColor = getResources().getColor(R.color.colorOnSurface, this.getContext().getTheme());

    private BinarySearchTreeActivity mBSTActivity;
    private int mCount = 0;
    private boolean mSwitch;

    private enum Operation {
        ADD, REMOVE, RANDOM, CLEAR,

        SIZE, HEIGHT, DEPTH,

        HASPARENT, HASRIGHTCHILD, HASLEFTCHILD, ISROOT, ISEXTERNAL, ISINTERNAL, ISEMPTY,

        PARENT, RIGHTCHILD, LEFTCHILD, ROOT, EXTERNALNODES, INTERNALNODES, MAX, MIN,

        NONE, SAVE
    }

    private Operation mCurrentOperation;

    private float mMaxWidth;

    private final float mRadius = 50;

    private int mColor;

    private final PointF mBegin = new PointF();
    private final PointF mEnd = new PointF();

    private final float mTopSpace = 80;
    private BinarySearchTree mTree;
    float mLength = 25;

    private float mX;
    private float mY;

    // Rect in order to save the TextBounds from the current number
    private final Rect mBounds = new Rect();
    private boolean mMove = false;
    private boolean mDown = false;
    private int mPosition = -1;

    //Room database setup
    private DatadoraViewModel mDatadoraViewModel;

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

    public void setActivity(BinarySearchTreeActivity _activity) {
        mBSTActivity = _activity;
        mDatadoraViewModel = new ViewModelProvider(mBSTActivity).get(DatadoraViewModel.class);
    }

    private void init() {
        mItemPaint.setColor(mPrimaryColor);
        mItemPaint.setStyle(Paint.Style.STROKE);
        mItemPaint.setStrokeCap(Paint.Cap.ROUND);
        mItemPaint.setStrokeWidth(6);

        mItemPaintArea.setColor(mSurfaceColor);
        mItemPaintArea.setStyle(Paint.Style.FILL);

        mItemTextPaint.setColor(mOnSurfaceColor);
        mItemTextPaint.setTextSize(50);
    }


    /**
     * New entry in the BST table
     * @param value the value to be added into the database
     */
    public void sendInputBSTValuesToDatabase(int value){
        mDatadoraViewModel.insert(new BinarySearchTreeRoom(value));
    }

    /**
     * Loads the saved integers from the database
     * There is an observer that observes the LiveData from
     * the database and is notified when they change.
     */
    private void loadBinarySearchTreeFromDatabase(){

        mDatadoraViewModel.getmAllBinarySearchTreeValuesValues().observe(mBSTActivity, treeRooms -> {

            if(mTree.size() == 0) {
                for (BinarySearchTreeRoom tr : treeRooms) {

                    mTree.insertNode(tr.getVal());
                    mBSTActivity.getTree().insertNode(tr.getVal());
                    mBSTActivity.getTreeUser().add(new BinaryTreeNode(tr.getVal()));
                }
                Log.i(TAG, "BSTView: loadBinarySearchTreeFromDatabase mTree was: " + mTree);
                Log.i(TAG, "BSTView: loadBinarySearchTreeFromDatabase mTree size was: " + mTree.size());
                Log.i(TAG, "BSTView: loadBinarySearchTreeFromDatabase mBSTActivity.getTreeUser() was: " + mBSTActivity.getTreeUser());
                Log.i(TAG, "BSTView: loadBinarySearchTreeFromDatabase mBSTActivity.getTreeUser() size was: " + mBSTActivity.getTreeUser().size());

            }
            invalidate();
        });
    }




    public void move(boolean _selected) {
        mMove = _selected;
    }

    public void setTranslate(float _x, float _y) {
        mX = _x;
        mY = _y;
    }

    public float getTranslateX() {
       return mX;
    }

    public float getTranslateY() {
        return mY;
    }

    public void setSwitch(boolean isChecked) {
        mSwitch = isChecked;
        invalidate();
    }

    public void add() {
        mCurrentOperation = Operation.ADD;
        invalidate();
    }

    public boolean remove() {
        if(mPosition != -1) {
            int data = mBSTActivity.getTreeUser().get(mPosition).getData();
            mBSTActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", data));
            mDatadoraViewModel.deleteByIdBST(data); //remove from database
            mBSTActivity.getTree().remove(data);
            mBSTActivity.getTreeUser().remove(mPosition);
            invalidate();
            mPosition = -1;
            return true;
        }
        return false;
    }

    public void clear() {
        mCurrentOperation = Operation.CLEAR;
        mDatadoraViewModel.deleteAllBSTDatabaseEntries();
        invalidate();
    }

    public boolean depth() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {
            mBSTActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mBSTActivity.getTree().getDepth(mBSTActivity.getTreeUser().get(mPosition).getData())));
            return true;
        }
        return false;
    }

    public boolean height() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {
            mBSTActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mBSTActivity.getTree().getHeight(mBSTActivity.getTreeUser().get(mPosition).getData())));
            return true;
        }
        return false;
    }

    public void getExternal() {
        invalidate();
    }

    public void getInternal() {
        invalidate();
    }

    public void max(int _pos) {
        mCurrentOperation = Operation.MAX;
        mPosition = _pos;
        invalidate();
    }

    public void min(int _pos) {
        mCurrentOperation = Operation.MIN;
        mPosition = _pos;
        invalidate();
    }

    public boolean hasParent() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {

            BinaryTreeNode parent = mBSTActivity.getTree().getParentNode(mBSTActivity.getTreeUser().get(mPosition).getData());

            if (parent != null) {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean hasRightChild() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {

            BinaryTreeNode n = mBSTActivity.getTreeUser().get(mPosition);

            if (n.getRight() != null) {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean hasLeftChild() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {

            BinaryTreeNode n = mBSTActivity.getTreeUser().get(mPosition);

            if (n.getLeft() != null) {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean isRoot() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {

            BinaryTreeNode n = mBSTActivity.getTreeUser().get(mPosition);

            if (mBSTActivity.getTree().getRoot() == n) {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean isInternal() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {

            BinaryTreeNode n = mBSTActivity.getTreeUser().get(mPosition);

            if (n.getRight() != null || n.getLeft() != null) {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean isExternal() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {

            BinaryTreeNode n = mBSTActivity.getTreeUser().get(mPosition);

            if (n.getRight() == null && n.getLeft() == null) {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
            } else {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
            }
            return true;
        }
        return false;
    }

    public boolean getParentNode() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {
            BinaryTreeNode n = mBSTActivity.getTreeUser().get(mPosition);
            BinaryTreeNode parent = mBSTActivity.getTree().getParentNode(n.getData());

            if (parent != null) {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", parent.getData()));
            } else {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText("-");
            }
            return true;
        }
        return false;
    }

    public boolean getRightChild() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {

            BinaryTreeNode n = mBSTActivity.getTreeUser().get(mPosition);

            if (n.getRight() != null) {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", n.getRight().getData()));
            } else {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText("-");
            }
            return true;
        }
        return false;
    }

    public boolean getLeftChild() {
        if(mPosition != -1 && mBSTActivity.getTreeUser().get(mPosition).isSelected()) {

            BinaryTreeNode n = mBSTActivity.getTreeUser().get(mPosition);

            if (n.getLeft() != null) {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", n.getLeft().getData()));
            } else {
                mBSTActivity.getBinding().BSTActivityReturnValue.setText("-");
            }
            return true;
        }
        return false;
    }

    public void inOrder() {
        final Vector<BinaryTreeNode> inOrder = mBSTActivity.getTree().toInOrder(true);
        final StringBuilder builder = new StringBuilder();

        ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), mSurfaceColor, mPrimaryColor);
        animator.setRepeatCount(inOrder.size() - 1);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColor = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                inOrder.get(mCount).setSelected(true);
                builder.append(inOrder.get(mCount).getData());
                mBSTActivity.getBinding().BSTActivityVectorOutput.setText(builder.toString());
                invalidate();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                inOrder.get(mCount).setSelected(false);
                invalidate();
                mCount = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                inOrder.get(mCount).setSelected(false);
                mCount ++;
                inOrder.get(mCount).setSelected(true);
                builder.append("   ").append(inOrder.get(mCount).getData());
                mBSTActivity.getBinding().BSTActivityVectorOutput.setText(builder.toString());
            }
        });


        animator.start();

    }

    @Override
    protected void onSizeChanged(int _w, int _h, int _oldW, int _oldH) {
        super.onSizeChanged(_w, _h, _oldW, _oldH);

        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());

        // size of parent of this view
        mMaxWidth = (float) _w - xpad - 6;


        Log.i(TAG, "BSTView: onSizeChanged mTree was: " + mBSTActivity.getTree());
        Log.i(TAG, "BSTView: onSizeChanged mBSTActivity.getTreeUser() was: " + mBSTActivity.getTreeUser());

        Log.i(TAG, "------onSizeChanged was called, mTree and mTreeUser was cleared and vals loadedFromDatabase again-------");

        mBSTActivity.getTree().clear();
        mBSTActivity.getTreeUser().clear();
        loadBinarySearchTreeFromDatabase();
        mCurrentOperation = Operation.SAVE;
        invalidate();

    }

    @Override
    protected void onMeasure(int _widthMeasureSpec, int _heightMeasureSpec) {
        super.onMeasure(_widthMeasureSpec, _heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);
        drawTree(_canvas);
    }

    private void drawTree(Canvas _canvas) {
        mTree = mBSTActivity.getTree();
        Vector<BinaryTreeNode> tv = mTree.toArrayPreOrder();

        if(mTree.getRoot() != null) {
            float x;
            float y;

            if (mX != 0 && mY != 0) {
                x = mTree.getRoot().getPoint().x;
                y = mTree.getRoot().getPoint().y;
            } else {
                x = mMaxWidth / 2;
                y = mTopSpace;
            }

            if(mMove) {
                x = x + (mX);
                y = y + (mY);
            }

            mTree.getRoot().setPoint(x, y);
        }

        if (tv != null) {
            for (int i = 0; i < mBSTActivity.getTreeUser().size(); i++) {

                BinaryTreeNode n = tv.get(i);
                BinaryTreeNode nPre = mTree.getParentNode(n.getData());

                if(n != mTree.getRoot()) {
                    setPointOfNode(n, nPre);
                    prepareAndDrawLine(n, nPre, _canvas);
                }

                String s = String.valueOf(n.getData());
                mItemTextPaint.getTextBounds(s, 0, s.length(), mBounds);

                if(n.isSelected()) {
                    mItemPaint.setColor(mPrimaryColor);
                    mItemPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    mItemTextPaint.setColor(mOnPrimaryColor);
                } else {
                    mItemPaint.setColor(mPrimaryColor);
                    mItemPaint.setStrokeWidth(6);
                    mItemPaint.setStyle(Paint.Style.STROKE);

                    mItemTextPaint.setColor(mOnSurfaceColor);
                }

                _canvas.drawCircle(n.getPoint().x, n.getPoint().y, mRadius, mItemPaint);
                _canvas.drawText(s, n.getPoint().x - 3 - mBounds.width() / 2f, n.getPoint().y + mBounds.height() / 2f, mItemTextPaint);
            }
        }
    }

    private void drawArrows(BinaryTreeNode _n, BinaryTreeNode _nPre, float _x, float _y, Canvas _canvas) {

        float x = calculateDiffX(_n, _nPre);
        float y = calculateDiffY(_n, _nPre);

        double alpha = Math.atan(y/x);

        double beta = Math.toRadians(45) - alpha;
        double yArrow = Math.sin(beta) * mLength;
        double xArrow = Math.cos(beta) * mLength;

        double ceta = Math.toRadians(180) - (alpha + Math.toRadians(45));
        double yArrow2 = Math.sin(ceta) * mLength;
        double xArrow2 = Math.cos(ceta) * mLength;

        float direction = getDirection(_n, _nPre);

        float lengthX = (float) (_x - (xArrow) * direction);
        float lengthY = (float) (_y - yArrow);

        float lengthX2 = (float) (_x + (xArrow2) * direction);
        float lengthY2 = (float) (_y - yArrow2);

        if(alpha != Math.toRadians(-45) && alpha != Math.toRadians(45)) {
            if(direction == 1) {
                lengthY = (float) (_y + yArrow);
            } else {
                lengthY2 = (float) (_y + yArrow2);
                lengthY = (float) (_y - yArrow);
            }
        }
            _canvas.drawLine(_x, _y, lengthX2, lengthY2, mItemPaint);
            _canvas.drawLine(lengthX, lengthY, _x, _y, mItemPaint);

    }

    private void setPointOfNode(BinaryTreeNode _n, BinaryTreeNode _nPre) {
        float direction = getDirection(_n, _nPre);

        float xPre = _nPre.getPoint().x;
        float yPre = _nPre.getPoint().y;

        int minDistanceX;
        if(mSwitch) {
            minDistanceX = 100;
        } else {
            minDistanceX = 80;
        }
        float x = (_n.getChildCount() * minDistanceX * direction) + xPre;
        float y;
        if(mY == 0) {
            y = (minDistanceX * mTree.getDepth(_n.getData())) + mTopSpace + mY;
        } else {
            y = yPre + minDistanceX;
        }
        _n.setPoint(x,y);
    }

    private void prepareAndDrawLine(BinaryTreeNode _n, BinaryTreeNode _nPre, Canvas _canvas) {

        float x = calculateDiffX(_n, _nPre);
        float y = calculateDiffY(_n, _nPre);

        float direction = getDirection(_n, _nPre);

        double lengthX = Math.cos(Math.atan(y/x));
        double lengthY = Math.cos(Math.atan(x/y));

        float xPre = (float) (_nPre.getPoint().x + (lengthX * direction) * mRadius);
        float yPre = (float) (_nPre.getPoint().y + lengthY * mRadius);

        x = (float) (_n.getPoint().x - (lengthX * direction) * mRadius);
        y = (float) (_n.getPoint().y - lengthY * mRadius);

        _canvas.drawLine(xPre, yPre, x, y, mItemPaint);

        if(mSwitch) {
            drawArrows(_n, _nPre, x, y, _canvas);

            double angle = Math.toRadians(45);
            float smallLength = mLength/2f;

            y = (float) (Math.sin(angle) * mRadius);
            x = (float) (Math.cos(angle) * mRadius);
            y = _n.getPoint().y + y;

            if (_n.getRight() == null) {
                float xEnd = _n.getPoint().x + x;
                _canvas.drawLine(xEnd, y, xEnd + mLength, y + mLength, mItemPaint);

                float smallY = (float) (Math.sin(angle) * smallLength);
                float smallX = (float) (Math.cos(angle) * smallLength);

                _canvas.drawLine((xEnd + mLength) - smallX, (y + mLength) + smallY, (xEnd + mLength) + smallX, (y + mLength) - smallY, mItemPaint);
            }

            if (_n.getLeft() == null) {
                float xEnd = _n.getPoint().x - x;
                _canvas.drawLine(xEnd, y, xEnd - mLength, y + mLength, mItemPaint);

                float smallY = (float) (Math.sin(angle) * smallLength);
                float smallX = (float) (Math.cos(angle) * smallLength);

                _canvas.drawLine((xEnd - mLength) - smallX, (y + mLength) - smallY, (xEnd - mLength) + smallX, (y + mLength) + smallY, mItemPaint);
            }
        }
    }

    private float calculateDiffX(BinaryTreeNode _n, BinaryTreeNode _nPre) {
        return _n.getPoint().x - _nPre.getPoint().x;
    }

    private float calculateDiffY(BinaryTreeNode _n, BinaryTreeNode _nPre) {
        return _n.getPoint().y - _nPre.getPoint().y;
    }

    private float getDirection (BinaryTreeNode _n, BinaryTreeNode _nPre) {
        if(_nPre != null && _n.getData() < _nPre.getData()) {
            return -1;
        } else {
            return 1;
        }
    }

    private void selectNode(float _x, float _y) {

        int i = 0;
        float space = mRadius / 1.5f;
        for (BinaryTreeNode n : mBSTActivity.getTreeUser()) {
            if(_x < n.getPoint().x + space && _x > n.getPoint().x - space && _y < n.getPoint().y + space && _y > n.getPoint().y - space) {
                n.setSelected(true);
                mPosition = i;
                invalidate();
            } else {
                n.setSelected(false);
            }
            i++;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent _event) {

        if (_event.getAction() == MotionEvent.ACTION_DOWN) {
            mDown = true;

            float x = _event.getX();
            float y = _event.getY();
            if(!mMove) {
                selectNode(x, y);
            } else {
                mBegin.x = x;
                mBegin.y = y;
            }

            mBSTActivity.getBinding().BSTActivityReturnValue.setText("");
            invalidate();
        } else if (_event.getAction() == MotionEvent.ACTION_MOVE) {
            if(mMove) {
                float x = _event.getX();
                float y = _event.getY();

                if(mDown) {
                    mDown = false;
                } else {
                    mBegin.x = mEnd.x;
                    mBegin.y = mEnd.y;
                }

                mEnd.x = x;
                mEnd.y = y;

                mX = mEnd.x - mBegin.x;
                mY = mEnd.y - mBegin.y;

                invalidate();
            }
        }
        return true;
    }

}
