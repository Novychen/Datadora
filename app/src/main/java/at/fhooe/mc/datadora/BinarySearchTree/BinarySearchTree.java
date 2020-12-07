package at.fhooe.mc.datadora.BinarySearchTree;

import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * Implements a binary search tree. Is the value bigger it is saved on the right side, if not - on the left side. Same values are not allowed
 *
 */
public class BinarySearchTree {

    private static final String TAG = "BinarySearchTree : ";

    /** Root node of the tree. **/
    private BinaryTreeNode root;

    /** Number of elements stored in the tree. */
    private int size;

    public BinaryTreeNode getRoot() { return root; }
    public void setRoot(BinaryTreeNode _n) {
        root = _n;
    }

    public int getSize() {return size; }
    public void setSize(int _size) {
        size = _size;
    }

    /**
     * Inserts the given element. Duplicate elements are not allowed.
     *
     * @param data insert a Node with the data as data
     * @return Returns true if insertion was successful, false otherwise.
     */
    public BinaryTreeNode insertNode (int data) {

        BinaryTreeNode newNode = new BinaryTreeNode(data);
        if (root == null) {
            root = newNode;
            size++;
            return root;
        }

        BinaryTreeNode n = root;
        while (n != null) {

            if (data == n.getData()) {
                return null;
            }

            if (data > n.getData()) {
                if (n.getRight() == null) {
                    n.setRight(newNode);
                    size++;
                    if (n.getData() < root.getData()) {
                        n.incrementChildToCount();
                    }
                    return newNode;
                }
                n = n.getRight();
            } else {
                if (n.getLeft() == null) {
                    n.setLeft(newNode);
                    size++;
                    if (n.getData() > root.getData()) {
                        n.incrementChildToCount();
                    }
                    return newNode;
                }
                n = n.getLeft();
            }
        }
        return null;
    }

    /**
     * Searches for the node of the given key
     * @param key of the searched node
     * @return the node of the given key if found or null if not
     */
    public BinaryTreeNode findNode(int key) {
        if(root == null) {
            return null;
        }

        BinaryTreeNode n = new BinaryTreeNode(root.getData());
        n.setLeft(root.getLeft());
        n.setRight(root.getRight());

        while (n != null) {
            if (key == n.getData()) {
                return n;
            }
            if (key > n.getData()) {
                n = n.getRight();
            } else {
                n = n.getLeft();
            }
        }
        return null;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Removes the element with the given key. Returns true if the key could be
     * found (and removed), false otherwise.
     */
    public boolean remove(int key) {

        if(root == null) {
            return false;
        }
        if(root.getLeft() == null && root.getRight() == null) {
            root = null;
            return true;
        }

        BinaryTreeNode n = new BinaryTreeNode(root.getData());
        BinaryTreeNode prev;
        BinaryTreeNode temp;

        n.setLeft(root.getLeft());
        n.setRight(root.getRight());

        if (findNode(key) != null) {
            return false;
        }
        n = findNode(key);
        size--;
        if (n.getLeft() == null && n.getRight() == null) {
            prev = getParentNode(n.getData());
            if (n.getData() < prev.getData()) {
                prev.setLeft(null);
            } else {
                prev.setRight(null);
            }
            return true;
        } else if (root.getData() == n.getData()) {

            if (n.getLeft().getRight() == null) {
                n = root.getLeft();
                n.setRight(root.getRight());
                root = n;
            } else {
                temp = n.getLeft().getRight();
                while (temp.getRight() != null) {
                    temp = temp.getRight();
                }
                prev = getParentNode(temp.getData());
                prev.setRight(null);
                temp.setLeft(root.getLeft());
                temp.setRight(root.getRight());
                root = temp;
            }
            return true;
        } else if (n.getLeft() != null && n.getRight() == null) {
            prev = getParentNode(n.getData());
            if (prev.getData() > n.getData()) {
                prev.setLeft(n.getLeft());
            } else {
                prev.setRight(n.getLeft());
            }
        } else if (n.getRight() != null && n.getLeft() == null) {
            prev = getParentNode(n.getData());
            if (prev.getData() > n.getData()) {
                prev.setLeft(n.getRight());
            } else {
                prev.setRight(n.getRight());
            }
        } else {
            prev = getParentNode(n.getData());
            if (prev.getData() > n.getData()) {
                prev.setLeft(n.getRight());
                temp = n.getRight();
                while (temp.getLeft() != null) {
                    temp = temp.getLeft();
                }
                temp.setLeft(n.getLeft());
            }
            if (prev.getData() < n.getData()) {
                prev.setRight(n.getLeft());
                temp = n.getLeft();
                while (temp.getRight() != null) {
                    temp = temp.getRight();
                }
                temp.setRight(n.getRight());
            }
        }

        while (prev.getData() != root.getData()) {
            prev = getParentNode(prev.getData());
        }
        root = prev;
        return true;
    }

    /**
     * Gets the size of the tree
     *
     * @return Returns the number of elements stored in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Returns the child data of the given key and the boolean side. Integer.MIN_VALUE if no child
     * can be found.
     */
    public int getChildNode(int _key, boolean _side){
        if(root == null) {
            return Integer.MIN_VALUE;
        }

        BinaryTreeNode n = findNode(_key);
        int ret = Integer.MIN_VALUE;
        if(n != null) {
            if (_side) {
                if(n.getLeft() != null) {
                    ret = n.getLeft().getData();
                }

            } else {
                if(n.getRight()!= null) {
                    ret = n.getRight().getData();
                }
            }
        }
        return ret;
    }

    /**
     * Check if the node from the given key has children
     * @param key of the node that will be checked
     * @return true if it has at least one child or false if not
     */
    public boolean hasNoChildren(int key){
        if((root != null) && (findNode(key) != null)){
          return (findNode(key).getRight() != null) &&(findNode(key).getLeft() != null);
        }
        return false;
    }

    /**
     * Gets the Parent node from the node with the given key
     * @param key of the node which parent is searched
     * @return the parent nod if found or null if it has no parent / the tree doesn't contain such a key
     */
    public BinaryTreeNode getParentNode(int key) {

        BinaryTreeNode n = root;

        if (root.getData() == key || findNode(key) == null) {
            return null;
        }

        while (n != null) {

            if ((n.getRight() != null && key == n.getRight().getData()) || (n.getLeft() != null && key == n.getLeft().getData())) {
                return n;
            }

            if (key > n.getData()) {
                n = n.getRight();
            } else if (key < n.getData()) {
                n = n.getLeft();
            }
        }
        return null;
    }

    public Vector<BinaryTreeNode> getExternalNodes() {
        return null;
    }

    public Vector<BinaryTreeNode> getInternalNodes() {
        return null;
    }

    public boolean isExternal(BinaryTreeNode n) {
        return n.getLeft() != null && n.getRight() == null;
    }

    /**
     * Gets the Depth from the given node. The depth of root is 0
     * @param key from node which depth is searched
     * @return the depth from the given node or -1 if it doesn't exist within the tree
     */
    public int getDepth(int key){
        int result = 0;
        if(root != null) {
            BinaryTreeNode n = getParentNode(key);
            while (n != null) {
                key = n.getData();
                result++;
                n = getParentNode(key);
            }
        }
        return result;
    }

    public BinaryTreeNode findNodeByPoint(BinaryTreeNode _node, PointF _point) {

        if (_node == null) {
            return null;
        }

        if (_node.getPoint() == _point) {
            return _node;
        }

        findNodeByPoint(_node.getLeft(), _point);
        findNodeByPoint(_node.getRight(), _point);
        return null;
    }

    /**
     * Returns the elements of the tree in ascending (inorder traversal) or
     * descending (reverse inorder traversal) order.
     *
     * @param ascending when false reverse inorder
     * @return the elements of the tree (inorder traversal)
     */
    public int[] toArray(boolean ascending) {

        if (root == null) {
            return null;
        }

        int[] inorder = new int[size];
        BinaryTreeNode n = this.root;

        this.toArray(inorder, ascending, 0, n);

        return inorder;
    }

    private int toArray(int[] inorder, boolean ascending, int offset, BinaryTreeNode n) {
        if (ascending) {
            if (n.getLeft() != null) {
                offset = toArray(inorder, ascending, offset, n.getLeft());
            }
            inorder[offset] = n.getData();
            offset++;
            if (n.getRight() != null) {
                offset = toArray(inorder, ascending, offset, n.getRight());
            }
            return offset;
        } else {
            if (n.getRight() != null) {
                offset = toArray(inorder, ascending, offset, n.getRight());
            }
            inorder[offset] = n.getData();
            offset++;
            if (n.getLeft() != null) {
                offset = toArray(inorder, ascending, offset, n.getLeft());
            }
            return offset;
        }
    }

    /**
     * Saves the data from the Nodes into array in postorder traversal
     *
     * @return the elements of the tree (postorder traversal)
     */
    public int[] toArrayPostOrder() {
        if (root == null) {
            return null;
        }

        int[] postorder = new int[size];
        BinaryTreeNode n = this.root;

        toArrayPostOrder(postorder, 0, n);

        return postorder;
    }

    private int toArrayPostOrder(int[] postorder, int offset, BinaryTreeNode n) {

        if (n.getLeft() == null && n.getRight() == null) {
            postorder[offset] = n.getData();
            offset++;
            return offset;
        }

        if (n.getLeft() != null) {
            offset = toArrayPostOrder(postorder, offset, n.getLeft());
        }
        if (n.getRight() != null) {
            offset = toArrayPostOrder(postorder, offset, n.getRight());
        }
        postorder[offset] = n.getData();
        offset++;
        return offset;
    }

    /**
     * Saves the data from the Nodes into array in preorder traversal
     *
     * @return the elements of the tree (preorder traversal)
     */
    public int[] toArrayPreOrder() {

        if (root == null) {
            return null;
        }

        int[] preorder = new int[size];
        BinaryTreeNode n = this.root;

        toArray(preorder, 0, n);

        return preorder;
    }

    private int toArray(int[] preorder, int offset, BinaryTreeNode n) {

        preorder[offset] = n.getData();
        offset++;

        if (n.getLeft() != null) {
            offset = toArray(preorder, offset, n.getLeft());
        }
        if (n.getRight() != null) {
            offset = toArray(preorder, offset, n.getRight());
        }
        return offset;
    }

    /**
     * Searches for the biggest number in the tree
     *
     * @return biggest number found in the tree or Integer.MIN_VALUE if biggest
     *         number can not be found
     */
    public int max() {
        if (root == null) {
            return Integer.MIN_VALUE;
        } else if (root.getRight() == null) {
            return root.getData();
        }

        BinaryTreeNode n = root;

        while (n.getRight() != null) {
            n = n.getRight();
        }
        return n.getData();
    }

    /**
     * Searches for the smallest number in the tree
     *
     * @return smallest number found in the tree or Integer.MIN_VALUE if biggest
     *         number can not be found
     */
    public int min() {
        if (root == null) {
            return Integer.MIN_VALUE;
        } else if (root.getLeft() == null) {
            return root.getData();
        }

        BinaryTreeNode n = root;

        while (n.getLeft() != null) {
            n = n.getLeft();
        }

        return n.getData();
    }

    /** Represents the tree in a human readable form. */
    @NonNull
    public String toString() {

        if(root != null) {
            StringBuilder string = new StringBuilder();

            int[] tree = toArray(true);
            for (int value : tree) {
                string.append(findNode(value));
            }
            return string.toString();
        } else {
            return "";
        }
    }

    /** Represents the tree in a human readable form. */
    public String toStringCount() {

        if(root != null) {
            String string = "";

            int[] tree = toArray(true);
            for (int value : tree) {
                string = string + findNode(value).toStringCount();
            }
            return string;
        } else {
            return "";
        }
    }

    /** Represents the tree in a human readable form. Where only the data is printed */
    public String toStringData() {

        if(root != null) {
            StringBuilder string = new StringBuilder();

            int[] tree = toArray(true);
            for (int value : tree) {
                string.append(value).append(", ");
            }
            return string.toString();
        } else {
            return "";
        }
    }

    /**
     * Updates the {@link BinaryTreeNode#setChildCount(int)}
     * @param _current the {@link BinaryTreeNode} which {@link BinaryTreeNode#setChildCount(int)} will be updated
     */
    public void updateChildCount(BinaryTreeNode _current) {

        if(_current.getLeft() != null) {
            _current.getLeft().setChildCount(getChildCount(_current.getLeft(), 0, 0) + 1);
           updateChildCount(_current.getLeft());
        }

        if(_current.getRight() != null) {
            _current.getRight().setChildCount(getChildCount(_current.getRight(), 0, 1) + 1);
            updateChildCount(_current.getRight());
        }
    }

    /**
     * Counts the children of the given {@link BinaryTreeNode}. All Nodes count that are below {@param _parent}
     * @param _parent the {@link BinaryTreeNode} which children will be counted
     * @param _count the current count of the {@param _parent}
     * @param _right an integer that checks if the {@param _parent} (from the beginning) was the left (0) or the right (1) child of its parent.
     *               2 means that it doesn't matter - as this is only important in the first iteration of this method
     * @return returns the {@param _count} which represents the amount of children the given {@link BinaryTreeNode} has
     */
    private int getChildCount(BinaryTreeNode _parent, int _count, int _right) {
        if(_parent.getLeft() != null) {
            if(_right == 1 || _right == 2) {
                _count = getChildCount(_parent.getLeft(), ++_count, 2);
            }
        }

        if(_parent.getRight() != null) {
            if(_right == 0 || _right == 2) {
                _count = getChildCount(_parent.getRight(), ++_count, 2);
            }
        }

        return _count;
    }

    /**
     * returns the height of the given node - leaves have a height of 0
     *
     * @param key of Node which is checked of its height
     * @return height of the given key (node)
     */
    public int getHeight(int key) {
        BinaryTreeNode n = findNode(key);
        // Base Case
        if (n == null)
            return 0;

        // Create an empty queue for level order tarversal
        Queue<BinaryTreeNode> q = new LinkedList();

        // Enqueue Root and initialize height
        q.add(n);
        int height = 0;

        while (1 == 1)
        {
            // nodeCount (queue size) indicates number of nodes
            // at current lelvel.
            int nodeCount = q.size();
            if (nodeCount == 0)
                return height;
            height++;

            // Dequeue all nodes of current level and Enqueue all
            // nodes of next level
            while (nodeCount > 0)
            {
                BinaryTreeNode newnode = q.peek();
                q.remove();
                if (newnode.getLeft() != null)
                    q.add(newnode.getLeft());
                if (newnode.getRight() != null)
                    q.add(newnode.getRight());
                nodeCount--;
            }
        }
    }

    /**
     * returns an array, that contains all values of a certain tree level
     *
     * @param array  where the values are saved
     * @param n      node which value is saved
     * @param level  level of the tree
     * @param offset offset of the array
     * @return returns the offset
     */
    protected int getArrayLevel(Vector<BinaryTreeNode> array, BinaryTreeNode n, int level, int offset) {
        if (n == null) {
            array.add(offset, null);
            offset++;
            return offset;
        }
        if (level == 1) {
            array.add(offset, n);
            offset++;
        } else if (level > 1) {
            offset = getArrayLevel(array, n.getLeft(), level -1, offset);
            offset = getArrayLevel(array, n.getRight(), level -1, offset);
        }
        return offset;
    }

    public boolean isEmpty() {
        return root == null;
    }
}
