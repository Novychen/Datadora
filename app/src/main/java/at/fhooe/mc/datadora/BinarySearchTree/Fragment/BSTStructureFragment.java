package at.fhooe.mc.datadora.BinarySearchTree.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTree;
import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;
import at.fhooe.mc.datadora.R;

public class BSTStructureFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "BSTStructureFragment ::";
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
        View view = _inflater.inflate(R.layout.fragment_bst_structure, _container, false);
        setUpOnClickListeners(view);
        mActivity = (BinarySearchTreeActivity) getActivity();
        return view;
    }

    private void setUpOnClickListeners(View _view) {

        Button b = _view.findViewById(R.id.BST_Fragment_Size);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Height);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Depth);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Inorder);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Preorder);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Postorder);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View _view) {

        mActivity.getBinding().BSTActivityReturnValue.setText("");
        mActivity.getBinding().BSTActivityVectorOutput.setText("");

        if (_view.getId() == R.id.BST_Fragment_Size) {
            size();
        } else if (_view.getId() == R.id.BST_Fragment_Height) {
            height();
        } else if (_view.getId() == R.id.BST_Fragment_Depth) {
            depth();
        } else if (_view.getId() == R.id.BST_Fragment_Inorder) {
            inorder();
        } else if (_view.getId() == R.id.BST_Fragment_Preorder) {
            preorder();
        } else if (_view.getId() == R.id.BST_Fragment_Postorder) {
            postorder();
        }
    }
    private void size() {
        String size;
        if(mActivity.getTree() != null) {
            size = String.valueOf(mActivity.getTree().size());
        } else {
            size = "0";
        }
        mActivity.getBinding().BSTActivityReturnValue.setText(size);
    }

    private void height() {
        if(!mPointer && !mActivity.getBinding().BSTActivityView.height() || mPointer && !mActivity.getBinding().BSTActivityPointerView.height() ) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void depth() {
        if(!mPointer && !mActivity.getBinding().BSTActivityView.depth() || mPointer && !mActivity.getBinding().BSTActivityPointerView.depth() ) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void inorder() {
        if(mPointer) {
            mActivity.getBinding().BSTActivityPointerView.inOrder();
        } else {
            mActivity.getBinding().BSTActivityView.inOrder();
        }
    }

    private void preorder() {
        Vector<BinaryTreeNode> vector = mActivity.getTree().toArrayPreOrder();
        String tree = vectorToString(vector);
        mActivity.getBinding().BSTActivityVectorOutput.setText(tree);
    }

    private void postorder() {
        Vector<BinaryTreeNode> vector = mActivity.getTree().toArrayPostOrder();
        String tree = vectorToString(vector);
        mActivity.getBinding().BSTActivityVectorOutput.setText(tree);
    }

    private String vectorToString(Vector<BinaryTreeNode> _vector) {
        StringBuilder builder = new StringBuilder();
        for(BinaryTreeNode n : _vector) {
            builder.append(n.getData()).append(", ");
        }

        return builder.toString();
    }
}