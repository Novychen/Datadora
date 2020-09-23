package at.fhooe.mc.datadora.BinarySearchTree;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.databinding.ActivityBinarySearchTreeBinding;


public class BinarySearchTreeActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener  {

    private static String TAG = "BSTActivity :: ";
    private BinarySearchTree mTree = new BinarySearchTree();
    private Vector<Integer> mTreeUser = new Vector<>();
    private ActivityBinarySearchTreeBinding mBinding;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivityBinarySearchTreeBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        // setup Toolbar
        Toolbar myToolbar = mBinding.BSTActivityToolbar;
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set up the slider
        Slider slider = mBinding.BSTActivityInputSlider;
        slider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);    // converting the float value to an int value
            }
        });
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                mBinding.BSTActivityInputValue.setText(String.valueOf((int) value));
            }
        });

        // Enables Always-on
        mBinding.BSTActivityAdd.setOnClickListener(this);
        mBinding.BSTActivityClear.setOnClickListener(this);
        mBinding.BSTActivityGetMax.setOnClickListener(this);
        mBinding.BSTActivityGetMin.setOnClickListener(this);
        mBinding.BSTActivityRandom.setOnClickListener(this);
        mBinding.BSTActivityRemove.setOnClickListener(this);
        mBinding.BSTActivityStructureSize.setOnClickListener(this);
        mBinding.BSTActivityInorder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.BSTActivityStructureSize) {
            mBinding.BSTActivityReturnText.setText("Size");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", mTree.size()));
        } else if (view == mBinding.BSTActivityAdd) {
            mBinding.BSTActivityAdd.setChecked(false);
            add();
        } else if (view == mBinding.BSTActivityRandom) {
            mBinding.BSTActivityRandom.setChecked(false);
            random();
        } else if (view == mBinding.BSTActivityGetMax) {
            mBinding.BSTActivityGetMax.setChecked(false);
            max();
        } else if (view == mBinding.BSTActivityGetMin) {
            mBinding.BSTActivityGetMin.setChecked(false);
            min();
        } else if (view == mBinding.BSTActivityRemove) {
            mBinding.BSTActivityReturnText.setText("Remove Last");
            /* if (!mTree.isEmpty()) { //TODO: let BinarySearchTree implement isEmpty
                // mBinding.ActivityBSTValueText.setText(String.format("%s", mBST.lastElement()));
                // mBST.remove(mBST.size() - 1);
                 int key = 0;
                 if(mTreeUser.contains(key)){
                     mTree.remove(key);
                 }
            } else {
                Toast.makeText(this, R.string.Stack_Activity_Text_Empty, Toast.LENGTH_SHORT).show();
                mBinding.ActivityBSTValueText.setText("-");
            } */
        } else if (view == mBinding.BSTActivityClear) {
            clear();
        } else if (view == mBinding.BSTActivityInorder) {
            mBinding.BSTActivityVectorOutput.setText(mTree.toString());
        }
    }

    private void clear() {
        //TODO: let BinarySearchTree implement clear
        mTree.setRoot(null); // Do you intend to change this? Because thats not clearing
        mTreeUser.clear();
        mTree.setSize(0); // Do you intend to change this? Because thats not clearing
    }

    private void add() {
        int value = (int) mBinding.BSTActivityInputSlider.getValue();
        mTreeUser.add(value);
        mTree.insert(value);
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

        boolean successful = mTree.insert(result);
        if (successful){mTreeUser.add(result);}
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

    @Override
    public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) {
        if (_buttonView == mBinding.BSTActivitySwitch) {
            if (_isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        return true;
    }
}
