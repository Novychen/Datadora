package at.fhooe.mc.datadora.BinarySearchTree.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;
import at.fhooe.mc.datadora.R;

public class BSTGetFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "BSTGetFragment :: ";
    private BinarySearchTreeActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.fragment_bst_get, _container, false);
        setUpOnClickListeners(view);
        mActivity = (BinarySearchTreeActivity) getActivity();
        return view;
    }

    private void setUpOnClickListeners(View _view) {

        Button b = _view.findViewById(R.id.BST_Fragment_Parent);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_RightChild);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_LeftChild);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Root);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_ExternalNodes);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_InternalNodes);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Max);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Min);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View _view) {
        mActivity.getBinding().BSTActivityReturnValue.setText("");
        mActivity.getBinding().BSTActivityVectorOutput.setText("");

        if (_view.getId() == R.id.BST_Fragment_Parent) {
            getParent();
        } else if (_view.getId() == R.id.BST_Fragment_RightChild) {
            getRightChild();
        }  else if (_view.getId() == R.id.BST_Fragment_LeftChild) {
            getLeftChild();
        } else if (_view.getId() == R.id.BST_Fragment_Root) {
            getRoot();
        } else if (_view.getId() == R.id.BST_Fragment_ExternalNodes) {
            getExternalNodes();
        } else if (_view.getId() == R.id.BST_Fragment_InternalNodes) {
            getInternalNodes();
        } else if (_view.getId() == R.id.BST_Fragment_Max) {
            max();
        } else if (_view.getId() == R.id.BST_Fragment_Min) {
            min();
        }
    }

    private void getParent() { mActivity.getBinding().BSTActivityView.getParentNode(); }

    private void getRightChild() { mActivity.getBinding().BSTActivityView.getRightChild(); }

    private void getLeftChild() { mActivity.getBinding().BSTActivityView.getLeftChild(); }

    private void getRoot() { mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mActivity.getTree().getRoot().getData())); }

    /**
     * returns a String with all external nodes of the tree
     */
    public void getExternalNodes() {
        Vector<BinaryTreeNode> external = mActivity.getTree().getExternalNodes();
        mActivity.getBinding().BSTActivityView.getExternal();
    }

    /**
     * returns a String with all internal nodes of the tree
     */
    public void getInternalNodes() {
        Vector<BinaryTreeNode> internal = mActivity.getTree().getInternalNodes();
        mActivity.getBinding().BSTActivityView.getInternal();
    }

    /**
     * returns the max value of the tree
     */
    private void max() {
        int max = mActivity.getTree().max();
        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", max));
        mActivity.getBinding().BSTActivityView.max(max);
    }

    /**
     * returns the min value of the tree
     */
    private void min() {
        int min = mActivity.getTree().min();
        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", min));
        mActivity.getBinding().BSTActivityView.min(min);
    }

}