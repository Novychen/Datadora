package at.fhooe.mc.datadora.BinarySearchTree;

import android.graphics.PointF;

/**
 * Inner class for the binary tree node.
 *
 */
public class BinaryTreeNode {

    // Count of the children of the node (on the opposite side from where the node is in the tree)
    // If the node is in the right subtree then it counts its left children and versa-vista.
    private int mChildCount;
    private PointF mPoint;
    private int mData;
    private BinaryTreeNode mRight;
    private BinaryTreeNode mLeft;

    public BinaryTreeNode(int _data) {
        mData = _data;
        mPoint = new PointF(0, 0);
    }

    @Override
    public String toString() {
        return "BinaryTreeNode { " + mData + " }\n";
    }

    public String toStringAll() {
        return "BinaryTreeNode { " + "Point = " + mPoint.x + ", " + mPoint.y + " || child count = " + mChildCount + " || data = " + mData + "}\n";
    }

    public String toStringCount() {
        return "BinaryTreeNode { child count = " + mChildCount + " || data = " + mData + "}\n";
    }

    public void setChildCount(int _childCount) {
        mChildCount = _childCount;
    }

    public int getChildCount() {
        return mChildCount;
    }

    public void incrementChildToCount () {
        mChildCount++;
    }
    public void decrementChildToCount () {
        mChildCount--;
    }

    public void setPoint(float x, float y) {
        mPoint.set(x,y);
    }

    public PointF getPoint() {
        return mPoint;
    }

    public int getData() {
        return mData;
    }

    public void setData(int _data) {
        mData = _data;
    }

    public BinaryTreeNode getRight() {
        return mRight;
    }

    public void setRight(BinaryTreeNode _right) {
        mRight = _right;
    }

    public BinaryTreeNode getLeft() {
        return mLeft;
    }

    public void setLeft(BinaryTreeNode _left) {
        mLeft = _left;
    }
}
