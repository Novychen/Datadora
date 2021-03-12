package at.fhooe.mc.datadora.BinarySearchTree.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;
import at.fhooe.mc.datadora.R;

public class BSTGetFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "BSTGetFragment :: ";
    private BinarySearchTreeActivity mActivity;

    private boolean mPointer;

    public boolean isPointer() {
        return mPointer;
    }

    public void setPointer(boolean _pointer) {
        mPointer = _pointer;
    }

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

    private void getParent() {
        if(!mActivity.getBinding().BSTActivityView.getParentNode()) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void getRightChild() {
        if(!mPointer && !mActivity.getBinding().BSTActivityView.getRightChild() || mPointer && !mActivity.getBinding().BSTActivityPointerView.getRightChild() ) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLeftChild() {
        if(!mPointer && !mActivity.getBinding().BSTActivityView.getLeftChild() || mPointer && !mActivity.getBinding().BSTActivityPointerView.getLeftChild() ) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void getRoot() { mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", mActivity.getTree().getRoot().getData())); }

    /**
     * returns a String with all external nodes of the tree
     */
    public void getExternalNodes() {
        mActivity.getTree().getExternalNodes();
        if(mPointer) {
            mActivity.getBinding().BSTActivityView.getExternal();
        } else {
            mActivity.getBinding().BSTActivityPointerView.getExternal();
        }
    }

    /**
     * returns a String with all internal nodes of the tree
     */
    public void getInternalNodes() {
        mActivity.getTree().getInternalNodes();
        if(mPointer) {
            mActivity.getBinding().BSTActivityView.getInternal();
        } else {
            mActivity.getBinding().BSTActivityPointerView.getInternal();
        }
    }

    /**
     * returns the max value of the tree
     */
    private void max() {
        int max = mActivity.getTree().max();
        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", max));
        if(mPointer) {
            mActivity.getBinding().BSTActivityView.max(max);
        } else {
            mActivity.getBinding().BSTActivityPointerView.max(max);
        }
    }

    /**
     * returns the min value of the tree
     */
    private void min() {
        int min = mActivity.getTree().min();
        mActivity.getBinding().BSTActivityReturnValue.setText(String.format("%s", min));
        if(mPointer) {
            mActivity.getBinding().BSTActivityView.min(min);
        } else {
            mActivity.getBinding().BSTActivityPointerView.min(min);
        }          }

}