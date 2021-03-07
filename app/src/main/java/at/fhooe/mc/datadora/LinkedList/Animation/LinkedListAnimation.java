package at.fhooe.mc.datadora.LinkedList.Animation;

import android.animation.ValueAnimator;

public interface LinkedListAnimation
{
    void start();
    void animate(int _pos);
    void addUpdate(ValueAnimator.AnimatorUpdateListener _listener);
    void update(ValueAnimator _animation);
}
