package at.fhooe.mc.datadora.BinarySearchTree;

import android.graphics.PointF;

/**
 * Inner class for the binary tree node.
 *
 */
public class BinaryTreeNode {

    public BinaryTreeNode left;
    public BinaryTreeNode right;
    public PointF point;
    public int data;

    public BinaryTreeNode(int elem) {
        data = elem;
        left = null;
        right = null;
        point = new PointF(80,0);
    }

    public void setPoint(PointF _point) {
        point = _point;
    }
}
