package at.fhooe.mc.datadora.BinarySearchTree;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


public class BinarySearchTreeActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
        // Buttons
        mBinding.BSTActivityInorder.setOnClickListener(this);
        mBinding.BSTActivityPostorder.setOnClickListener(this);
        mBinding.BSTActivityPreorder.setOnClickListener(this);
        // Standard
        mBinding.BSTActivityAdd.setOnClickListener(this);
        mBinding.BSTActivityRandom.setOnClickListener(this);
        mBinding.BSTActivityRemove.setOnClickListener(this);
        mBinding.BSTActivityClear.setOnClickListener(this);

        //Structure
        mBinding.BSTActivityStructureSize.setOnClickListener(this);
        mBinding.BSTActivityStructureHeight.setOnClickListener(this);
        mBinding.BSTActivityStructureDepth.setOnClickListener(this);
        //Check
        mBinding.BSTActivityCheckEmpty.setOnClickListener(this);
        mBinding.BSTActivityCheckLeftChild.setOnClickListener(this);
        mBinding.BSTActivityCheckRightChild.setOnClickListener(this);
        mBinding.BSTActivityCheckRoot.setOnClickListener(this);
        mBinding.BSTActivityCheckParent.setOnClickListener(this);
        mBinding.BSTActivityCheckExternal.setOnClickListener(this);
        mBinding.BSTActivityCheckInternal.setOnClickListener(this);
        //Get
        mBinding.BSTActivityGetMax.setOnClickListener(this);
        mBinding.BSTActivityGetMin.setOnClickListener(this);
        mBinding.BSTActivityGetParent.setOnClickListener(this);
        mBinding.BSTActivityGetLeftChild.setOnClickListener(this);
        mBinding.BSTActivityGetRightChild.setOnClickListener(this);
        mBinding.BSTActivityGetRoot.setOnClickListener(this);
        mBinding.BSTActivityGetExternal.setOnClickListener(this);
        mBinding.BSTActivityGetInternal.setOnClickListener(this);


        mBinding.BSTActivityCheckEmpty.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int key = (int) mBinding.BSTActivityInputSlider.getValue();
        if (view == mBinding.BSTActivityInorder) {
            Log.e(TAG, "InOrder");
            //mBinding.BSTActivityVectorOutput.setText(mTree.toString());

            mBinding.BSTActivityVectorOutput.setText(ArrayToSting(mTree.toArray(true)));
        } else if (view == mBinding.BSTActivityPostorder) {
            Log.e(TAG, "PostOrder" + mTree.toArrayPostOrder());
            mTree.toArrayPostOrder();
            mBinding.BSTActivityVectorOutput.setText(ArrayToSting(mTree.toArrayPostOrder()));
        } else if (view == mBinding.BSTActivityPreorder) {
            Log.e(TAG, "PreOrder" + mTree.toArrayPreOrder());
            mBinding.BSTActivityVectorOutput.setText(ArrayToSting(mTree.toArrayPreOrder()));

        }
        //Strandard
        else if (view == mBinding.BSTActivityAdd) {
            mBinding.BSTActivityAdd.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Add");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", key));
            add();
        } else if (view == mBinding.BSTActivityRandom) {
            mBinding.BSTActivityRandom.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Random");
            random();

        } else if (view == mBinding.BSTActivityRemove) {
            mBinding.BSTActivityRemove.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Remove");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", key));
            remove();
        } else if (view == mBinding.BSTActivityClear) {
            mBinding.BSTActivityReturnText.setText("Clear");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", 0));
            mBinding.BSTActivityClear.setChecked(false);
            clear();
        }
        //Structure
        else if (view == mBinding.BSTActivityStructureSize) {
            mBinding.BSTActivityStructureSize.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Size");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", mTreeUser.size()));
        } else if (view == mBinding.BSTActivityStructureDepth) {
            mBinding.BSTActivityStructureDepth.setChecked(false);
            Log.e(TAG, "Depth");
            mBinding.BSTActivityReturnText.setText("Depth");
        } else if (view == mBinding.BSTActivityStructureHeight) {
            mBinding.BSTActivityReturnText.setText("Height");
            mBinding.BSTActivityStructureHeight.setChecked(false);
            Log.e(TAG, "Height");
        }
        //Get
        else if (view == mBinding.BSTActivityGetParent) {
            mBinding.BSTActivityGetParent.setChecked(false);
            Log.e(TAG, "GetParent");
            //BinaryTreeNode m = mTree.getParentNode(key);
            mBinding.BSTActivityReturnText.setText("Parent");
           // mBinding.BSTActivityReturnValue.setText(String.format("%s", m.data));
        } else if (view == mBinding.BSTActivityGetLeftChild) {
            mBinding.BSTActivityGetLeftChild.setChecked(false);
            Log.e(TAG, "GetLeftChild");
            mBinding.BSTActivityReturnText.setText("LeftChild");
        } else if (view == mBinding.BSTActivityGetRightChild) {
            mBinding.BSTActivityGetRightChild.setChecked(false);
            Log.e(TAG, "GetRightChild");
            mBinding.BSTActivityReturnText.setText("RightChild");
        } else if (view == mBinding.BSTActivityGetRoot) {
            mBinding.BSTActivityGetRoot.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Root");
            if (mTree.root != null){
                mBinding.BSTActivityReturnValue.setText(String.format("%s", mTree.root.data));
            }else {
                Toast.makeText(this, R.string.BST_Activity_Check_Empty, Toast.LENGTH_SHORT).show();}
            Log.e(TAG, "GetRoot");
        } else if (view == mBinding.BSTActivityGetInternal) {
            mBinding.BSTActivityGetInternal.setChecked(false);
            Log.e(TAG, "GetInternal");
        } else if (view == mBinding.BSTActivityGetExternal) {
            mBinding.BSTActivityGetExternal.setChecked(false);
            Log.e(TAG, "GetExternal");
        } else if (view == mBinding.BSTActivityGetMax) {
            mBinding.BSTActivityGetMax.setChecked(false);
            max();
        } else if (view == mBinding.BSTActivityGetMin) {
            mBinding.BSTActivityGetMin.setChecked(false);
            min();
        }
        //Check
        else if (view == mBinding.BSTActivityCheckEmpty) {
            mBinding.BSTActivityCheckEmpty.setChecked(false);
            Log.e(TAG, "CheckEmpty");
            if (mTreeUser.isEmpty()) {
                Toast.makeText(this, R.string.BST_Activity_Check_Empty, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.BST_Activity_Check_NotEmpty, Toast.LENGTH_SHORT).show();
            }
        } else if (view == mBinding.BSTActivityCheckParent) {
            mBinding.BSTActivityCheckParent.setChecked(false);
            Log.e(TAG, "CheckParent");
        } else if (view == mBinding.BSTActivityCheckLeftChild) {
            mBinding.BSTActivityCheckLeftChild.setChecked(false);
            Log.e(TAG, "CheckLeftChild");
        } else if (view == mBinding.BSTActivityCheckRightChild) {
            mBinding.BSTActivityCheckRightChild.setChecked(false);
            Log.e(TAG, "CheckRightChild");
        } else if (view == mBinding.BSTActivityCheckRoot) {
            mBinding.BSTActivityCheckRoot.setChecked(false);
          //  mBinding.BSTActivityReturnText.setText("Root");
            mBinding.BSTActivityReturnValue.setText(String.format("%s",key));
            if (mTree.root != null){
                if(mTree.root.data == key){
                    mBinding.BSTActivityReturnText.setText("True");
                   // mBinding.BSTActivityReturnValue.setText("True");
                }else{
                    mBinding.BSTActivityReturnText.setText("False");
                   // mBinding.BSTActivityReturnValue.setText("False");
                    }
            }else {
                Toast.makeText(this, R.string.BST_Activity_Check_Empty, Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "CheckRoot");
        } else if (view == mBinding.BSTActivityCheckExternal) {
            mBinding.BSTActivityCheckExternal.setChecked(false);
            Log.e(TAG, "CheckExternal");
        } else if (view == mBinding.BSTActivityCheckInternal) {
            mBinding.BSTActivityCheckInternal.setChecked(false);
            Log.e(TAG, "CheckInternal");
        } else {
            Log.i(TAG, "UnKnownButton");
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
        mBinding.BSTActivityReturnText.setText("Max");
        mBinding.BSTActivityReturnValue.setText(String.format("%s", mTree.max()));
        return mTree.max();
    }

    private int min() {
        mBinding.BSTActivityReturnText.setText("Min");
        mBinding.BSTActivityReturnValue.setText(String.format("%s", mTree.min()));
        return mTree.min();
    }

    private void random() {
        Random r = new Random();
        int low = -100;
        int high = 101;
        int result = r.nextInt(high - low) + low;

        boolean successful = mTree.insert(result);
        if (successful) {
            mTreeUser.add(result);
            mBinding.BSTActivityReturnValue.setText(String.format("%s",result));
        }

    }

    public void remove() {
        if (!(mTree.root == null)) { //TODO: let BinarySearchTree implement isEmpty
            int key = (int) mBinding.BSTActivityInputSlider.getValue();
            if (mTreeUser.contains(key)) {
                mTree.remove(key);
                mTreeUser.removeElement(key);
            } else {
                Toast.makeText(this, R.string.BST_Activity_Standard_NotRemoveable, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.BST_Activity_Check_Empty, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Takes an array and transforms it to a tree
     *
     * @param _array the array that is transformed - needs to be in the same order as the user put their values in
     */
    protected void fromArrayToTree(int[] _array) {

        //TODO: clear mTree to be sure no false data is in it

        for (int i = 0; i < _array.length; i++) {
            mTree.insert(_array[i]);
        }
    }

    /**
     * Takes an array and transforms it to a tree
     *
     * @param _array the array that is transformed - needs to be inorder!
     * @param _begin the begin position of the array
     * @param _end   the end position of the array
     * @return the root node of the generated tree
     */
    protected BinaryTreeNode fromArrayToTree(int[] _array, int _begin, int _end) {

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

    private String ArrayToSting(int[] ints) {
        if(ints==null){
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i < ints.length; i++) {
            stringBuilder.append(ints[i]);
            if (!(ints.length - 1 == i)) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
