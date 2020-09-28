package at.fhooe.mc.datadora.BinarySearchTree;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * Implements a binary search tree. Is the value bigger it is saved on the right side, if not - on the left side. Same values are not allowed
 *
 */
public class BinarySearchTree {

    /** Root node of the tree. **/
    protected BinaryTreeNode root;

    /** Number of elements stored in the tree. */
    protected int size;

    public void setRoot(BinaryTreeNode _n) {
        root = _n;
    }

    public void setSize(int _size) {
        size = _size;
    }

    /**
     * Inserts the given element. Duplicate elements are not allowed.
     *
     * @param elem insert a Node with the elem as data
     * @return Returns true if insertion was successful, false otherwise.
     */
    public boolean insert(int elem) {

        BinaryTreeNode newNode = new BinaryTreeNode(elem);
        if (root == null) {
            root = newNode;
            size++;
            return true;
        }
        BinarySearchTree t = new BinarySearchTree();
        BinaryTreeNode n = new BinaryTreeNode(root.data);
        n.left = root.left;
        n.right = root.right;
        t.root = n;

        while (n != null) {

            if (elem == n.data) {
                return false;
            }
            if (elem > n.data) {
                if (n.right == null) {
                    n.right = newNode;
                    this.root = t.root;
                    size++;
                    return true;
                }
                n = n.right;
            } else if (elem < n.data) {
                if (n.left == null) {
                    n.left = newNode;
                    this.root = t.root;
                    size++;
                    return true;
                }
                n = n.left;
            }
        }
        return false;
    }

    /**
     * Searches for the (first) element with the given key. Returns true if it could
     * be found, false otherwise.
     */
    public boolean contains(int key) {
        if (root.data == key) {
            return true;
        }
        if (findNode(key) != null) {
            return true;
        }
        return false;
    }

    /**
     * Searches for the node of the given key
     * @param key of the searched node
     * @return the node of the given key if found or null if not
     */
    public BinaryTreeNode findNode(int key) {
        BinarySearchTree t = new BinarySearchTree();
        BinaryTreeNode n = new BinaryTreeNode(root.data);
        n.left = root.left;
        n.right = root.right;
        t.root = n;

        while (n != null) {
            if (key == n.data) {
                return n;
            }
            if (key > n.data) {
                n = n.right;
            } else if (key < n.data) {
                n = n.left;
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
        if(root.left == null && root.right == null) {
            root = null;
            return true;
        }

        BinarySearchTree t = new BinarySearchTree();
        BinaryTreeNode n = new BinaryTreeNode(root.data);
        BinaryTreeNode prev = new BinaryTreeNode(0);
        BinaryTreeNode temp = new BinaryTreeNode(0);

        n.left = root.left;
        n.right = root.right;
        t.root = n;

        if (!contains(key)) {
            return false;
        }
        n = findNode(key);
        size--;
        if (n.left == null && n.right == null) {
            prev = getParentNode(n.data);
            if (n.data < prev.data) {
                prev.left = null;
            } else {
                prev.right = null;
            }
            return true;
        } else if (root.data == n.data) {

            if (n.left.right == null) {
                n = root.left;
                n.right = root.right;
                root = n;
            } else {
                temp = n.left.right;
                while (temp.right != null) {
                    temp = temp.right;
                }
                prev = getParentNode(temp.data);
                prev.right = null;
                temp.left = root.left;
                temp.right = root.right;
                root = temp;
            }
            return true;
        } else if (n.left != null && n.right == null) {
            prev = getParentNode(n.data);
            if (prev.data > n.data) {
                prev.left = n.left;
            } else {
                prev.right = n.left;
            }
        } else if (n.right != null && n.left == null) {
            prev = getParentNode(n.data);
            if (prev.data > n.data) {
                prev.left = n.right;
            } else {
                prev.right = n.right;
            }
        } else {
            prev = getParentNode(n.data);
            if (prev.data > n.data) {
                prev.left = n.right;
                temp = n.right;
                while (temp.left != null) {
                    temp = temp.left;
                }
                temp.left = n.left;
            }
            if (prev.data < n.data) {
                prev.right = n.left;
                temp = n.left;
                while (temp.right != null) {
                    temp = temp.right;
                }
                temp.right = n.right;
            }
        }

        while (prev.data != root.data) {
            prev = getParentNode(prev.data);
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
     * Returns the parent element of the given key. Integer.MIN_VALUE if no parent
     * can be found.
     */
    public int getParent(int key) {

        if (root.data == key || !this.contains(key)) {
            return Integer.MIN_VALUE;
        }

        if (getParentNode(key) == null) {
            return Integer.MIN_VALUE;
        }

        return getParentNode(key).data;
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
                if(n.left != null) {
                    ret = n.left.data;
                }

            } else {
                if(n.right!= null) {
                    ret = n.right.data;
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
        if((root != null)&& (findNode(key) != null)){
          return (findNode(key).right != null)&&(findNode(key).left!=null);
        }
        return false;
    }

    /**
     * Gets the Parent node from the node with the given key
     * @param key of the node which parent is searched
     * @return the parent nod if found or null if it has no parent / the tree doesn't contain such a key
     */
    public BinaryTreeNode getParentNode(int key) {

        BinarySearchTree t = new BinarySearchTree();
        BinaryTreeNode n = new BinaryTreeNode(root.data);
        n.left = root.left;
        n.right = root.right;
        t.root = n;

        if (root.data == key || !contains(key)) {
            return null;
        }

        while (n != null) {

            if ((n.right != null && key == n.right.data) || (n.left != null && key == n.left.data)) {
                return n;
            }
            if (key > n.data) {
                n = n.right;
            } else if (key < n.data) {
                n = n.left;
            }
        }
        return null;
    }

    /**
     * Gets the Depth from the given node. The depth of root is 0
     * @param key from node which depth is searched
     * @return the depth from the given node or -1 if it doesn't exist within the tree
     */
    public int getDepth(int key){
        int result = 0;
        if(root != null) {
            while (getParent(key) != Integer.MIN_VALUE) {
                key = getParent(key);
                result++;
            }
        }
        return result;
    }

    public BinaryTreeNode findNodeByPoint(BinaryTreeNode _node, PointF _point) {

        if (_node == null) {
            return null;
        }

        if (_node.point == _point) {
            return _node;
        }

        findNodeByPoint(_node.left, _point);
        findNodeByPoint(_node.right, _point);
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
            if (n.left != null) {
                offset = toArray(inorder, ascending, offset, n.left);
            }
            inorder[offset] = n.data;
            offset++;
            if (n.right != null) {
                offset = toArray(inorder, ascending, offset, n.right);
            }
            return offset;
        } else {
            if (n.right != null) {
                offset = toArray(inorder, ascending, offset, n.right);
            }
            inorder[offset] = n.data;
            offset++;
            if (n.left != null) {
                offset = toArray(inorder, ascending, offset, n.left);
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

        if (n.left == null && n.right == null) {
            postorder[offset] = n.data;
            offset++;
            return offset;
        }

        if (n.left != null) {
            offset = toArrayPostOrder(postorder, offset, n.left);
        }
        if (n.right != null) {
            offset = toArrayPostOrder(postorder, offset, n.right);
        }
        postorder[offset] = n.data;
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

        preorder[offset] = n.data;
        offset++;

        if (n.left != null) {
            offset = toArray(preorder, offset, n.left);
        }
        if (n.right != null) {
            offset = toArray(preorder, offset, n.right);
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
        }
        if (root.right == null) {
            return root.data;
        }

        BinarySearchTree t = new BinarySearchTree();
        BinaryTreeNode n = new BinaryTreeNode(root.data);
        n.left = root.left;
        n.right = root.right;
        t.root = n;

        while (n.right != null) {
            n = n.right;
        }
        return n.data;
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
        }
        if (root.left == null) {
            return root.data;
        }

        BinarySearchTree t = new BinarySearchTree();
        BinaryTreeNode n = new BinaryTreeNode(root.data);
        n.left = root.left;
        n.right = root.right;
        t.root = n;

        while (n.left != null) {
            n = n.left;
        }
        return n.data;
    }

    /** Represents the tree in a human readable form. */
    public String toString() {

        if(root != null) {
            String string = "";

            int[] tree = toArray(true);
            for (int i = 0; i < tree.length; i++) {
                string = string + tree[i] + ", ";
            }
            return string;
        } else {
            return "";
        }
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
                if (newnode.left != null)
                    q.add(newnode.left);
                if (newnode.right != null)
                    q.add(newnode.right);
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
            offset = getArrayLevel(array, n.left, level -1, offset);
            offset = getArrayLevel(array, n.right, level -1, offset);
        }
        return offset;
    }
}
