package at.fhooe.mc.datadora.BinarySearchTree;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
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

    private SharedPreferences mSharedPreferences;
    private static final String SP_FILE_KEY = "at.fhooe.mc.datadora.BSTSharedPreferenceFile.BST";
    private static final String SP_VALUE_KEY = "at.fhooe.mc.datadora.BSTKey2020";
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
        mBinding.BSTActivityCheckEmpty.setOnClickListener(this);

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
        mSharedPreferences = getSharedPreferences(SP_FILE_KEY, Context.MODE_PRIVATE);
        mBinding.BSTActivitySwitch.setOnCheckedChangeListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
    @Override
    protected void onPause() {
        super.onPause();
        save();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Vector<Integer> v = loadFromSave();
        if(v != null) {
            mTreeUser.clear();
            mTree.root = null;
            mTree.size = 0;
            // mTreeUser.addAll(v);
            for (int i = 0; i < v.size();i++){
                add(v.elementAt(i));
            }
        }
    }
    /**
     * Saves the current vector (input from the user) into the SharedPreferences.
     */
    private void save() {

        // init the SP object
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        // Convert the vector containing the integers to a string
        Vector<Integer> vector = mTreeUser;
        StringBuilder vectorStr = new StringBuilder();

        // transform the vector into a string
        for (int i = 0; i < vector.size(); i++) {
            if (i != vector.size() - 1) {
                vectorStr.append(vector.get(i)).append(",");
            } else {
                vectorStr.append(vector.get(i));
            }
        }

        editor.putString(SP_VALUE_KEY, String.valueOf(vectorStr));
        editor.apply();
    }



    /**
     * Gets the saved vector (user input) from the SharedPreferences.
     * @return the saved vector or null if there is none
     */
    protected Vector<Integer> loadFromSave() {

        // get the saved string (vector)
        String defaultValue = "empty";
        String vectorStr = mSharedPreferences.getString(SP_VALUE_KEY, defaultValue);
        Vector<Integer> vector = new Vector<>();

        // check if it was successful -> transform to vector, or if not -> return null
        int begin;
        int end = 0;
        int i;
        if(vectorStr == null || vectorStr.contains(defaultValue) || vectorStr.equals("")) {
            return null;
        } else {
            while(end > -1) {
                begin = end;
                end = vectorStr.indexOf(',', begin);
                if(end == -1) {
                    i = Integer.parseInt(vectorStr.substring(begin));
                } else {
                    i = Integer.parseInt(vectorStr.substring(begin, end));
                    end++;
                }
                vector.add(i);
            }
            return vector;
        }
    }









    @Override
    public void onClick(View view) {
        int key = (int) mBinding.BSTActivityInputSlider.getValue();
        if (view == mBinding.BSTActivityInorder) {
            Log.e(TAG, "InOrder");
            mBinding.BSTActivityVectorOutput.setText(ArrayToSting(mTree.toArray(true)));
        } else if (view == mBinding.BSTActivityPostorder) {
            mTree.toArrayPostOrder();
            mBinding.BSTActivityVectorOutput.setText(ArrayToSting(mTree.toArrayPostOrder()));
        } else if (view == mBinding.BSTActivityPreorder) {
            mBinding.BSTActivityVectorOutput.setText(ArrayToSting(mTree.toArrayPreOrder()));

        }
        //Standard
        else if (view == mBinding.BSTActivityAdd) {
            mBinding.BSTActivityAdd.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Add");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", key));
            add(key);
        } else if (view == mBinding.BSTActivityRandom) {
            mBinding.BSTActivityRandom.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Random");
            random();

        } else if (view == mBinding.BSTActivityRemove) {
            mBinding.BSTActivityRemove.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Remove");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", key));
            remove(key);
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
        } else if (view == mBinding.BSTActivityStructureDepth) { //ToDo
            mBinding.BSTActivityStructureDepth.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Clear");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", Depth(key)));
            Log.e(TAG, "Depth");
            mBinding.BSTActivityReturnText.setText("Depth");
        } else if (view == mBinding.BSTActivityStructureHeight) {//ToDo
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
            mBinding.BSTActivityReturnValue.setText(String.format("%s", Parent(key)));
        } else if (view == mBinding.BSTActivityGetLeftChild) {
            mBinding.BSTActivityGetLeftChild.setChecked(false);
            Log.e(TAG, "GetLeftChild");
            mBinding.BSTActivityReturnText.setText("LeftChild");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", LeftChild(key)));
            mBinding.BSTActivityReturnText.setText("LeftChild");
        } else if (view == mBinding.BSTActivityGetRightChild) {
            mBinding.BSTActivityGetRightChild.setChecked(false);
            Log.e(TAG, "GetRightChild");
            mBinding.BSTActivityReturnText.setText("RightChild");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", RightChild(key)));
            mBinding.BSTActivityReturnText.setText("RightChild");
        } else if (view == mBinding.BSTActivityGetRoot) {
            mBinding.BSTActivityGetRoot.setChecked(false);
            mBinding.BSTActivityReturnText.setText("Root");
            if (mTree.root != null) {
                mBinding.BSTActivityReturnValue.setText(String.format("%s", mTree.root.data));
            } else {
                Toast.makeText(this, R.string.BST_Activity_Check_Empty, Toast.LENGTH_SHORT).show();
            }
        } else if (view == mBinding.BSTActivityGetInternal) {
            mBinding.BSTActivityGetInternal.setChecked(false);
            mBinding.BSTActivityVectorOutput.setText(getInternalNodes());
        } else if (view == mBinding.BSTActivityGetExternal) {
            mBinding.BSTActivityGetExternal.setChecked(false);
            Log.e(TAG, "GetExternal");
            mBinding.BSTActivityVectorOutput.setText(getExternalNodes());
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
            if (mTreeUser.isEmpty()) {
                Toast.makeText(this, R.string.BST_Activity_Check_Empty, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.BST_Activity_Check_NotEmpty, Toast.LENGTH_SHORT).show();
            }
        } else if (view == mBinding.BSTActivityCheckParent) {
            mBinding.BSTActivityCheckParent.setChecked(false);
            Log.e(TAG, "CheckParent");
            mBinding.BSTActivityReturnText.setText("Parent");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", Parent(key)));
        } else if (view == mBinding.BSTActivityCheckLeftChild) {
            mBinding.BSTActivityCheckLeftChild.setChecked(false);
            Log.e(TAG, "CheckLeftChild");
            mBinding.BSTActivityReturnText.setText("LeftChild");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", LeftChild(key)));
        } else if (view == mBinding.BSTActivityCheckRightChild) {
            mBinding.BSTActivityCheckRightChild.setChecked(false);
            Log.e(TAG, "CheckRightChild");
            mBinding.BSTActivityReturnText.setText("RightChild");
            mBinding.BSTActivityReturnValue.setText(String.format("%s", RightChild(key)));
        } else if (view == mBinding.BSTActivityCheckRoot) {
            mBinding.BSTActivityCheckRoot.setChecked(false);
            mBinding.BSTActivityReturnValue.setText(String.format("%s", key));
            if (mTree.root != null) {
                if (mTree.root.data == key) {
                    mBinding.BSTActivityReturnText.setText("True");
                } else {
                    mBinding.BSTActivityReturnText.setText("False");
                }
            } else {
                Toast.makeText(this, R.string.BST_Activity_Check_Empty, Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "CheckRoot");

        } else if (view == mBinding.BSTActivityCheckExternal) {//ToDo
            mBinding.BSTActivityCheckExternal.setChecked(false);
            Log.e(TAG, "CheckExternal");
            External(key);
        } else if (view == mBinding.BSTActivityCheckInternal) {//ToDo
            mBinding.BSTActivityCheckInternal.setChecked(false);
            Log.e(TAG, "CheckInternal");
            Internal(key);
        } else {
            Log.i(TAG, "UnKnownButton");
        }
    }

    public boolean Internal(int key) {
        if (mTree.root != null) {
            if (mTree.hasNoChildren(key)) {
                mBinding.BSTActivityReturnText.setText("False");
            } else {
                mBinding.BSTActivityReturnText.setText("True");
            }
            mBinding.BSTActivityReturnValue.setText(String.format("%s", key));
        }
        return false;
    }

    public boolean External(int key) {
        if (mTree.root != null) {
            if (mTree.hasNoChildren(key)) {
                mBinding.BSTActivityReturnText.setText("True");
            } else {
                mBinding.BSTActivityReturnText.setText("False");
            }
            mBinding.BSTActivityReturnValue.setText(String.format("%s", key));
        }
        return false;
    }

    public String getExternalNodes() {
        StringBuilder stringBuilder = new StringBuilder("ExternalNodes : -");
        if (mTree.root != null) {
            for (int i = 0; i < mTreeUser.size(); i++) {
                if (mTree.hasNoChildren(mTreeUser.get(i))) {
                    stringBuilder.append(mTreeUser.get(i) + "-");
                }
            }

        }
        return stringBuilder.toString();
    }

    public String getInternalNodes() {
        StringBuilder stringBuilder = new StringBuilder("InternalNodes : -");
        if (mTree.root != null) {
            for (int i = 0; i < mTreeUser.size(); i++) {
                if (!mTree.hasNoChildren(mTreeUser.get(i))) {
                    stringBuilder.append(mTreeUser.get(i) + "-");
                }
            }

        }
        return stringBuilder.toString();
    }

    private void clear() {
        mTreeUser.clear();
        for (int i = 0 ; i < mTreeUser.size();i++){
            remove(mTreeUser.elementAt(i));
        }

    }

    private void add(int value) {
        mTreeUser.add(value);
        mTree.insert(value);
        mBinding.BSTActivityView.add(value);
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

    private int Depth(int key) {
        if (mTree.root != null) {
            return mTree.getDepth(key);
        }
        return 0;
    }

    private int Height(int key) {
        if (mTree.root != null) {

        }
        return 0;
    }

    private void random() {
        Random r = new Random();
        int low = -100;
        int high = 101;
        int result = r.nextInt(high - low) + low;

        boolean successful = mTree.insert(result);
        if (successful) {
            add(result);
            mBinding.BSTActivityReturnValue.setText(String.format("%s", result));
        }

    }

    private int Parent(int key) {
        if (mTree.root == null) {
            return Integer.MIN_VALUE;
        }
        return mTree.getParent(key);
    }

    private int LeftChild(int key) {
        if (mTree.root == null) {
            return Integer.MIN_VALUE;
        }
        return mTree.getChildNode(key, true);
    }

    private int RightChild(int key) {
        if (mTree.root == null) {
            return Integer.MIN_VALUE;
        }
        return mTree.getChildNode(key, false);
    }

    public void remove(int key) {
        if (!(mTree.root == null)) {
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
        if (_isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        return true;
    }

    private String ArrayToSting(int[] ints) {
        if (ints == null) {
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





































