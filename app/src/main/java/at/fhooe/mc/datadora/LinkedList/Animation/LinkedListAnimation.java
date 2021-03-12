package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.ValueAnimator;

public interface LinkedListAnimation
{
    void start();
    void animateNoPointer(int _pos);
    void addUpdateValueAnimator(ValueAnimator.AnimatorUpdateListener _listener);
    void update(ValueAnimator _animation);
}
