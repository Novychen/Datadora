package at.fhooe.mc.datadora.BinarySearchTree.Animation;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;

public class InOrder {

    private ValueAnimator mAnimator;
    private Vector<BinaryTreeNode> mTree;
    private StringBuilder mBuilder = new StringBuilder();

    public InOrder() {

    }

    public InOrder(int _SurfaceColor, int _primaryColor, Vector<BinaryTreeNode> _vector) {
        mTree = _vector;

        mAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), _SurfaceColor, _primaryColor, _SurfaceColor);
        mAnimator.setRepeatCount(_vector.size() - 1);
        mAnimator.setDuration(100 * _vector.size() - 1);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void start() {
        mAnimator.start();
    }

    public void addUpdateListener(ValueAnimator.AnimatorUpdateListener _listener) {
        mAnimator.addUpdateListener(_listener);
    }

    public void addUpdateListener(Animator.AnimatorListener _listener) {
        mAnimator.addListener(_listener);
    }

    public void update(ValueAnimator _animation) {
        if(_animation == mAnimator) {
            // mColorArea = (int) _animation.getAnimatedValue();
        }
    }

    public void animStart(Animator _animation, TextView _text, int _count) {
        if(_animation == mAnimator) {
            mTree.get(_count).setSelected(true);
            mBuilder.append(mTree.get(_count).getData());
            _text.setText(mBuilder.toString());
        }
    }

    public int animRepeat(Animator _animation, TextView _text, int _count) {
        if(_animation == mAnimator) {
            mTree.get(_count).setSelected(false);
            _count++;
            mTree.get(_count).setSelected(true);
            mBuilder.append("   ").append(mTree.get(_count).getData());
            _text.setText(mBuilder.toString());
        }
        return _count;
    }

    public int animEnd(Animator _animation, int _count) {
        if(_animation == mAnimator) {
            mTree.get(_count).setSelected(false);
            _count = 0;
        }
        return _count;
    }




}
