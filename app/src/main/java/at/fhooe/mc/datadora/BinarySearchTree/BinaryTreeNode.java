package at.fhooe.mc.datadora.BinarySearchTree;

/**
 * Inner class for the binary tree node.
 *
 */
public class BinaryTreeNode {

    public BinaryTreeNode left;
    public BinaryTreeNode right;
    public int data;

    public BinaryTreeNode(int elem) {
        data = elem;
        left = null;
        right = null;
    }
}
