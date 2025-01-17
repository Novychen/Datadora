package at.fhooe.mc.datadora.BinarySearchTree.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTree;
import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;
import at.fhooe.mc.datadora.R;

public class BSTStandardFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "BSTStandardFragment :: ";
    private BinarySearchTreeActivity mActivity;
    private BinarySearchTree mTree;
    private Vector<BinaryTreeNode> mTreeUser;

    private boolean mRandom = false;

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.fragment_bst_standard, _container, false);
        setUpOnClickListeners(view);
        mActivity = (BinarySearchTreeActivity) getActivity();

        assert mActivity != null;
        mTree = mActivity.getTree();
        mTreeUser = mActivity.getTreeUser();
        return view;
    }

    private void setUpOnClickListeners(View _view) {

        Button b = _view.findViewById(R.id.BST_Fragment_Add);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Remove);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Random);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_RandomTree);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Clear);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View _view) {
        mActivity.getBinding().BSTActivityReturnValue.setText("");
        mActivity.getBinding().BSTActivityVectorOutput.setText("");

        if(_view.getId() == R.id.BST_Fragment_Add) {
           add(Integer.parseInt(mActivity.getBinding().BSTActivityInputValue.getText().toString()));
        } else if(_view.getId() == R.id.BST_Fragment_Remove) {
            remove();
        } else if(_view.getId() == R.id.BST_Fragment_Random) {
            random();
        } else if(_view.getId() == R.id.BST_Fragment_RandomTree) {
            randomTree();
        } else if(_view.getId() == R.id.BST_Fragment_Clear) {
            clear();
        }
    }

    /**
     * adds a new node to the tree
     */
    public void add(int _value) {
        if(mTree != null && mTree.findNode(_value) == null) {
            BinaryTreeNode n = mTree.insertNode(_value);
            mTree.updateChildCount(mTree.getRoot());
            mTreeUser.add(n);
            mActivity.getBinding().BSTActivityView.add();
            mRandom = false;
        } else if (!mRandom) {
            Toast.makeText(getContext(), R.string.BST_Activity_Add_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * removes a Node form the tree defined by the key value
     */
    public void remove() {
        if(!mActivity.getBinding().BSTActivityView.remove()) {
            Toast.makeText(getContext(), R.string.BST_Activity_Select_Toast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * adds a random node the value between -100 and 100 to the tree
     */
    private void random() {
        int number = createRandomNumber(-100, 100);
        mRandom = true;
        add(number);
    }

    /**
     * adds a random node the value between -100 and 100 to the tree
     */
    private void randomTree() {
        clear();
        int size = createRandomNumber(5, 10);
        mActivity.getBinding().BSTActivityView.setTranslate(0,0);
        for (int i = 0; i <= size; i++) {
            random();
        }
    }

    /**
     * clears mTree and all of its nodes + the variable mTreeUser
     */
    private void clear() {
        mTree.clear();
        mTreeUser.clear();
        mActivity.getBinding().BSTActivityView.clear();
        mActivity.getBinding().BSTActivityView.setTranslate(0,0);
    }

    private int createRandomNumber(int _min, int _max) {
        _max++;
        Random r = new Random();
        return r.nextInt(_max - _min) + _min;
    }
}