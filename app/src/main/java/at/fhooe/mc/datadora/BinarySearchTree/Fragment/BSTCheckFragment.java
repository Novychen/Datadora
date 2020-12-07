package at.fhooe.mc.datadora.BinarySearchTree.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.R;

public class BSTCheckFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "BSTCheckFragment :: ";

    BinarySearchTreeActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.fragment_bst_check, _container, false);
        setUpOnClickListeners(view);
        mActivity = (BinarySearchTreeActivity) getActivity();
        return view;
    }

    private void setUpOnClickListeners(View _view) {

        Button b = _view.findViewById(R.id.BST_Fragment_HasParent);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_HasRightChild);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_HasLeftChild);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_IsRoot);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_IsExternal);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_IsInternal);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_IsEmpty);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    private void hasParent() { mActivity.getBinding().BSTActivityView.hasParent(); }

    private void hasRightChild() {  mActivity.getBinding().BSTActivityView.hasRightChild(); }

    private void hasLeftChild() {  mActivity.getBinding().BSTActivityView.hasLeftChild(); }

    private void isRoot() {  mActivity.getBinding().BSTActivityView.isRoot(); }

    /**
     * returns a boolean if the node given by the key is an external node
     */
    public void isExternal() {  mActivity.getBinding().BSTActivityView.isExternal(); }

    /**
     * returns a boolean if the node given by the key is an internal node
     */
    public void isInternal() {  mActivity.getBinding().BSTActivityView.isInternal(); }

    private void isEmpty() {
        if (mActivity.getTree() == null || mActivity.getTree().getRoot() == null) {
            mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
        } else {
            mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
        }
    }

}