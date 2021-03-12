package at.fhooe.mc.datadora.BinarySearchTree.Animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTree;
import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;
import at.fhooe.mc.datadora.Operation;


public class Add {

    private static final String TAG = "Add : ";

    private static final String PROPERTY_ADD_AREA = "PROPERTY_ADD_AREA";

    private AnimatorSet mAddSet = new AnimatorSet();

    private ValueAnimator mAddArea;
    private int mColorArea;
    private BSTValue mValues;

    private Vector<BinaryTreeNode> mAddPath;

    public Vector<BinaryTreeNode> getAddPath() {
        return mAddPath;
    }

    public void setAddPath(Vector<BinaryTreeNode> _addPath) {
        mAddPath = _addPath;
    }

    public Add(int _SurfaceColor, int _primaryColor, BSTValue _values) {
        mValues = _values;

        mAddArea = ValueAnimator.ofObject(new ArgbEvaluator(), _SurfaceColor, _primaryColor, _SurfaceColor);
        mAddArea.setInterpolator(new AccelerateDecelerateInterpolator());
        mAddSet.play(mAddArea);
    }

    public void addUpdateListener(ValueAnimator.AnimatorUpdateListener _listener) {
        mAddArea.addUpdateListener(_listener);
    }

    public void addUpdateListener(Animator.AnimatorListener _listener) {
        mAddArea.addListener(_listener);
    }

    public void update(ValueAnimator _animation) {
        mColorArea = (int) _animation.getAnimatedValue();
    }

    public void start() {
        mAddArea.setDuration(200 * mAddPath.size() - 1);
        mAddArea.setRepeatCount(mAddPath.size() - 1);
        mAddArea.start();
    }

    public void animate() {

    }

    public void getAddPath(BinarySearchTree _tree, int _data){

       mAddPath = new Vector<>();

       BinaryTreeNode n = _tree.getRoot();

        while (n != null) {

            if (_data == n.getData()) {
                mAddPath.add(n);
                return;
            }

            if (_data > n.getData()) {
                mAddPath.add(n);
                if (n.getRight() == null) {
                    return;
                }
                n = n.getRight();
            } else {
                mAddPath.add(n);
                if (n.getLeft() == null) {
                    return;
                }
                n = n.getLeft();
            }
        }
    }

    public void animStart(Animator _animation, int _count) {
        if( _animation == mAddArea) {
            mAddPath.get(_count).setSelected(true);
        }
    }

    public int animRepeat(Animator _animation, int _count) {
        if(_animation == mAddArea) {
            mAddPath.get(_count).setSelected(false);
            _count++;
            mAddPath.get(_count).setSelected(true);
        }
        return _count;
    }

    public int animEnd(Animator _animation, int _count) {
        if(_animation == mAddArea) {
            mAddPath.get(_count).setSelected(false);
            _count = 0;
        }
        return _count;
    }
}
