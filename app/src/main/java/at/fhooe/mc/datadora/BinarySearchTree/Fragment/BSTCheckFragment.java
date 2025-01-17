package at.fhooe.mc.datadora.BinarySearchTree.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
    public void onClick(View _view) {
        mActivity.getBinding().BSTActivityReturnValue.setText("");
        mActivity.getBinding().BSTActivityVectorOutput.setText("");

        if (_view.getId() == R.id.BST_Fragment_HasParent) {
            hasParent();
        } else if (_view.getId() == R.id.BST_Fragment_HasRightChild) {
            hasRightChild();
        } else if (_view.getId() == R.id.BST_Fragment_HasLeftChild) {
            hasLeftChild();
        } else if (_view.getId() == R.id.BST_Fragment_IsRoot) {
            isRoot();
        } else if (_view.getId() == R.id.BST_Fragment_IsExternal) {
            isExternal();
        } else if (_view.getId() == R.id.BST_Fragment_IsInternal) {
            isInternal();
        } else if (_view.getId() == R.id.BST_Fragment_IsEmpty) {
            isEmpty();
        }
    }

    private void hasParent() {
        if(!mActivity.getBinding().BSTActivityView.hasParent()) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
         }
    }

    private void hasRightChild() {
        if(!mActivity.getBinding().BSTActivityView.hasRightChild()) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void hasLeftChild() {
        if(!mActivity.getBinding().BSTActivityView.hasLeftChild()) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void isRoot() {
        if(!mActivity.getBinding().BSTActivityView.isRoot()) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * returns a boolean if the node given by the key is an external node
     */
    public void isExternal() {
        if(!mActivity.getBinding().BSTActivityView.isExternal()) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * returns a boolean if the node given by the key is an internal node
     */
    public void isInternal() {
        if(!mActivity.getBinding().BSTActivityView.isInternal()) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void isEmpty() {
        if (mActivity.getTree() == null || mActivity.getTree().getRoot() == null) {
            mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
        } else {
            mActivity.getBinding().BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
        }
    }

}