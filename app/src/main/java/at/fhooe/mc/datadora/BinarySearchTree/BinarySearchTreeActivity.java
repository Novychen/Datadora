package at.fhooe.mc.datadora.BinarySearchTree;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.databinding.ActivityBinarySearchTreeBinding;


public class BinarySearchTreeActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "BSTActivity :: ";

    private TextView mTextView;
    private BinarySearchTree mTree = new BinarySearchTree();
    private Vector<Integer> mTreeUser = new Vector<>();
    private ActivityBinarySearchTreeBinding mBinding;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivityBinarySearchTreeBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        // array eingeben sachen speichern
        // setup Toolbar
        Toolbar myToolbar = mBinding.BSTActivityToolbar;
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Enables Always-on
        mBinding.BSTActivityAdd.setOnClickListener(this);
        mBinding.BSTActivityClear.setOnClickListener(this);
        mBinding.BSTActivityMax.setOnClickListener(this);
        mBinding.BSTActivityMin.setOnClickListener(this);
        mBinding.BSTActivityRandom.setOnClickListener(this);
        mBinding.BSTActivityRemove.setOnClickListener(this);
        mBinding.BSTActivitySize.setOnClickListener(this);
        mBinding.BSTActivityVector.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.BSTActivitySize) {
            mBinding.BSTActivityReturnText.setText("Size");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", mTree.size()));
        } else if (view == mBinding.BSTActivityAdd) {
            add(12); //TODO: take user input as number
            mTreeUser.add(12);
        } else if (view == mBinding.BSTActivityRandom) {
            random();
        } else if (view == mBinding.BSTActivityMax) {
            max();
        } else if (view == mBinding.BSTActivityMin) {
            min();
        } else if (view == mBinding.BSTActivityRemove) {
            mBinding.BSTActivityReturnText.setText("Remove Last");
            /* if (!mTree.isEmpty()) { //TODO: let BinarySearchTree implement isEmpty
                // mBinding.ActivityBSTValueText.setText(String.format("%s", mBST.lastElement()));
                // mBST.remove(mBST.size() - 1);
            } else {
                Toast.makeText(this, R.string.Stack_Activity_Text_Empty, Toast.LENGTH_SHORT).show();
                mBinding.ActivityBSTValueText.setText("-");
            } */
        } else if (view == mBinding.BSTActivityClear) {
            clear();
        } else if (view == mBinding.BSTActivityVector) {
            mBinding.BSTActivityVectorOutput.setText(mTree.toString());
        }
    }

    private void clear() {
        //TODO: let BinarySearchTree implement clear
    }

    private void add(int _value) {
        mTree.insert(_value);
    }

    private int max() {
        return mTree.max();
    }

    private int min() {
        return mTree.min();
    }

    private void random() {
        Random r = new Random();
        int low = -100;
        int high = 101;
        int result = r.nextInt(high-low) + low;

        mTree.insert(result);
        mTreeUser.add(result);
    }

    /**
     * Takes an array and transforms it to a tree
     * @param _array the array that is transformed - needs to be in the same order as the user put their values in
     */
    protected void fromArrayToTree(int[] _array){

        //TODO: clear mTree to be sure no false data is in it

        for(int i = 0; i < _array.length; i++) {
            mTree.insert(_array[i]);
        }
    }

    /**
     * Takes an array and transforms it to a tree
     * @param _array the array that is transformed - needs to be inorder!
     * @param _begin the begin position of the array
     * @param _end the end position of the array
     * @return the root node of the generated tree
     */
    protected BinaryTreeNode fromArrayToTree(int[] _array, int _begin, int _end){

        if (_begin > _end) {
            return null;
        }

        int mid = (_begin + _end) / 2;
        BinaryTreeNode node = new BinaryTreeNode(_array[mid]);

        node.left = fromArrayToTree(_array, _begin, mid - 1);
        node.right = fromArrayToTree(_array, mid + 1, _end);

        return node;
    }
}
