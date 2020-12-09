package at.fhooe.mc.datadora.BinarySearchTree;

import androidx.annotation.NonNull;

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
    public void remove(int key) {

        if(root == null || findNode(key) == null) {
            return;
        }

        size--;

        if(root.getLeft() == null && root.getRight() == null) {
            root = null;
            return;
        }

        BinaryTreeNode n;
        BinaryTreeNode prev;
        BinaryTreeNode temp;

        n = findNode(key);

        // n is root
        if (root.getData() == n.getData()) {
            if (root.getLeft() == null) {
                n = root.getRight();
                root.setRight(null);
                root = n;
            } else if (root.getLeft().getRight() != null) {
                n = root.getLeft().getRight();
                n.setRight(root.getRight());
                root.getLeft().setRight(null);
                n.setLeft(root.getLeft());
                root = n;
            } else {
                n = root.getLeft();
                n.setRight(root.getRight());
                root.setRight(null);
                root.setLeft(null);
                root = n;
            }
            updateChildCount(root);
            return;
        }

        // no children
        if (n.getLeft() == null && n.getRight() == null) {
            prev = getParentNode(n.getData());
            if (n.getData() < prev.getData()) {
                prev.setLeft(null);
            } else {
                prev.setRight(null);
            }
            updateChildCount(root);
            return;
        }

        // has left child
        if (n.getLeft() != null && n.getRight() == null) {
            prev = getParentNode(n.getData());
            if (prev.getData() > n.getData()) {
                prev.setLeft(n.getLeft());
            } else {
                prev.setRight(n.getLeft());
            }
        // has right child
        } else if (n.getRight() != null && n.getLeft() == null) {
            prev = getParentNode(n.getData());
            if (prev.getData() > n.getData()) {
                prev.setLeft(n.getRight());
            } else {
                prev.setRight(n.getRight());
            }
        // has two children
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
        updateChildCount(root);
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

        Vector<BinaryTreeNode> tree = toArrayPostOrder();
        Vector<BinaryTreeNode> vector = new Vector<>();

        for (BinaryTreeNode n : tree) {
            if (n.getRight() == null && n.getLeft() == null) {
                vector.add(n);
                n.setSelected(true);
            } else {
                n.setSelected(false);
            }
        }

        return vector;
    }

    public Vector<BinaryTreeNode> getInternalNodes() {
        Vector<BinaryTreeNode> tree = toArrayPostOrder();
        Vector<BinaryTreeNode> vector = new Vector<>();

        for (BinaryTreeNode n : tree) {
            if (n.getRight() != null || n.getLeft() != null) {
                vector.add(n);
                n.setSelected(true);
            } else {
                n.setSelected(false);
            }
        }

        return vector;
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

    /**
     * Returns the elements of the tree in ascending (inorder traversal) or
     * descending (reverse inorder traversal) order.
     *
     * @param ascending when false reverse inorder
     * @return the elements of the tree (inorder traversal)
     */
    public Vector<BinaryTreeNode> toInOrder(boolean ascending) {

        if (root == null) {
            return null;
        }

        Vector<BinaryTreeNode> inorder = new Vector<>();
        BinaryTreeNode n = root;

        toInOrder(inorder, ascending, 0, n);

        return inorder;
    }

    private int toInOrder(Vector<BinaryTreeNode> inorder, boolean ascending, int offset, BinaryTreeNode n) {
        if (ascending) {
            if (n.getLeft() != null) {
                offset = toInOrder(inorder, ascending, offset, n.getLeft());
            }
            inorder.add(offset, n);
            offset++;
            if (n.getRight() != null) {
                offset = toInOrder(inorder, ascending, offset, n.getRight());
            }
        } else {
            if (n.getRight() != null) {
                offset = toInOrder(inorder, ascending, offset, n.getRight());
            }
            inorder.add(offset,n);
            offset++;
            if (n.getLeft() != null) {
                offset = toInOrder(inorder, ascending, offset, n.getLeft());
            }
        }
        return offset;
    }

    /**
     * Saves the data from the Nodes into array in postorder traversal
     *
     * @return the elements of the tree (postorder traversal)
     */
    public Vector<BinaryTreeNode> toArrayPostOrder() {
        if (root == null) {
            return null;
        }

        Vector<BinaryTreeNode> postOrder = new Vector<>();
        toArrayPostOrder(postOrder, 0, root);

        return postOrder;
    }

    private int toArrayPostOrder(Vector<BinaryTreeNode> postorder, int offset, BinaryTreeNode n) {

        if (n.getLeft() == null && n.getRight() == null) {
            postorder.add(offset,n);
            offset++;
            return offset;
        }

        if (n.getLeft() != null) {
            offset = toArrayPostOrder(postorder, offset, n.getLeft());
        }
        if (n.getRight() != null) {
            offset = toArrayPostOrder(postorder, offset, n.getRight());
        }
        postorder.add(offset, n);
        offset++;
        return offset;
    }

    /**
     * Saves the data from the Nodes into array in preorder traversal
     *
     * @return the elements of the tree (preorder traversal)
     */
    public Vector<BinaryTreeNode> toArrayPreOrder() {

        if (root == null) {
            return null;
        }

        Vector<BinaryTreeNode> preOrder = new Vector<>();
        toInOrder(preOrder, 0, root);

        return preOrder;
    }

    private int toInOrder(Vector<BinaryTreeNode> _preOrder, int _offset, BinaryTreeNode _n) {

        _preOrder.add(_offset,_n);
        _offset++;

        if (_n.getLeft() != null) {
            _offset = toInOrder(_preOrder, _offset, _n.getLeft());
        }
        if (_n.getRight() != null) {
            _offset = toInOrder(_preOrder, _offset, _n.getRight());
        }
        return _offset;
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

            Vector<BinaryTreeNode> tree = toInOrder(true);
            for (BinaryTreeNode value : tree) {
                string.append(value);
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

            Vector<BinaryTreeNode> tree = toInOrder(true);
            for (BinaryTreeNode value : tree) {
                string = string + value.toStringCount();
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

            Vector<BinaryTreeNode> tree = toInOrder(true);
            for (BinaryTreeNode value : tree) {
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

        if (n == null)
            return 0;

        Vector<BinaryTreeNode> v = new Vector<>();

        v.add(n);
        int height = 0;

        while (true)
        {
            int nodeCount = v.size();
            if (nodeCount == 0) {
                return height;
            }
            height++;

            while (nodeCount > 0)
            {
                BinaryTreeNode newNode = v.get(0);
                v.remove(0);
                if (newNode.getLeft() != null)
                    v.add(newNode.getLeft());
                if (newNode.getRight() != null)
                    v.add(newNode.getRight());
                nodeCount--;
            }
        }
    }
}
