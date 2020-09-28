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

    public BinarySearchTree getTree() {
        return mTree;
    }

    public Vector<Integer> getTreeUser() {
        return mTreeUser;
    }

    public ActivityBinarySearchTreeBinding getBinding() {
        return mBinding;
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivityBinarySearchTreeBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.BSTActivityView.setActivity(this);

        // setup Toolbar
        Toolbar myToolbar = mBinding.BSTActivityToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.All_Data_Activity_BST);
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
            mBinding.BSTActivityReturnValue.setText("");
            mBinding.BSTActivityAdd.setChecked(false);
            add(key);
        } else if (view == mBinding.BSTActivityRandom) {
            mBinding.BSTActivityReturnValue.setText("");
            mBinding.BSTActivityRandom.setChecked(false);
            random();
        } else if (view == mBinding.BSTActivityRemove) {
            mBinding.BSTActivityRemove.setChecked(false);
            remove();
        } else if (view == mBinding.BSTActivityClear) {
            mBinding.BSTActivityReturnValue.setText("");
            mBinding.BSTActivityClear.setChecked(false);
            clear();
        }

        //Structure
        else if (view == mBinding.BSTActivityStructureSize) {
            mBinding.BSTActivityStructureSize.setChecked(false);
            mBinding.BSTActivityReturnValue.setText(String.format("%s", mTreeUser.size()));
        } else if (view == mBinding.BSTActivityStructureDepth) {
            mBinding.BSTActivityStructureDepth.setChecked(false);
            depth();
        } else if (view == mBinding.BSTActivityStructureHeight) {
            mBinding.BSTActivityStructureHeight.setChecked(false);
            height();
        }

        //Get
        else if (view == mBinding.BSTActivityGetParent) {
            mBinding.BSTActivityGetParent.setChecked(false);
            getParentNode();
        } else if (view == mBinding.BSTActivityGetLeftChild) {
            mBinding.BSTActivityGetLeftChild.setChecked(false);
            getLeftChild();
        } else if (view == mBinding.BSTActivityGetRightChild) {
            mBinding.BSTActivityGetRightChild.setChecked(false);
            getRightChild();
        } else if (view == mBinding.BSTActivityGetRoot) {
            mBinding.BSTActivityGetRoot.setChecked(false);
            getRoot();
        } else if (view == mBinding.BSTActivityGetInternal) {
            mBinding.BSTActivityGetInternal.setChecked(false);
            mBinding.BSTActivityVectorOutput.setText(getInternalNodes());
        } else if (view == mBinding.BSTActivityGetExternal) {
            mBinding.BSTActivityGetExternal.setChecked(false);
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
            isEmpty();
        } else if (view == mBinding.BSTActivityCheckParent) {
            mBinding.BSTActivityCheckParent.setChecked(false);
            hasParent();
        } else if (view == mBinding.BSTActivityCheckLeftChild) {
            mBinding.BSTActivityCheckLeftChild.setChecked(false);
            hasLeftChild();
        } else if (view == mBinding.BSTActivityCheckRightChild) {
            mBinding.BSTActivityCheckRightChild.setChecked(false);
            hasRightChild();
        } else if (view == mBinding.BSTActivityCheckRoot) {
            mBinding.BSTActivityCheckRoot.setChecked(false);
            isRoot();
        } else if (view == mBinding.BSTActivityCheckExternal) {
            mBinding.BSTActivityCheckExternal.setChecked(false);
            isExternal();
        } else if (view == mBinding.BSTActivityCheckInternal) {
            mBinding.BSTActivityCheckInternal.setChecked(false);
            isInternal();
        }
    }

    private void isEmpty() {
        if (mTree.root == null) {
            mBinding.BSTActivityReturnValue.setText(R.string.All_Data_Activity_True);
        } else {
            mBinding.BSTActivityReturnValue.setText(R.string.All_Data_Activity_False);
        }
    }

    private void isRoot() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.isRoot();
        } else {
            Toast.makeText(this, R.string.BST_Activity_Check_Empty, Toast.LENGTH_SHORT).show();
        }
    }

    private void getParentNode() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.getParentNode();
        }
    }

    private void getRightChild() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.getRightChild();
        }
    }

    private void getLeftChild() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.getLeftChild();
        }
    }

    private void getRoot() {
        if (mTree.root != null) {
            mBinding.BSTActivityReturnValue.setText(String.format("%s", mTree.root.data));
        } else {
            mBinding.BSTActivityReturnValue.setText("-");
        }
    }

    private void hasParent() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.hasParent();
        }
    }

    private void hasLeftChild() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.hasLeftChild();
        }
    }

    private void hasRightChild() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.hasRightChild();
        }
    }

    private void height() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.height();
        }
    }

    private void depth() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.depth();
        }
    }

    /**
     * returns a boolean if the node given by the key is an interanl node
     */
    public void isInternal() {
        if (mTree.root != null) {
            mBinding.BSTActivityView.isInternal();
        }
    }

    /**
     * returns a boolean if the node given by the key is an external node
     */
    public void isExternal() {
        if (mTree.root != null) {
           mBinding.BSTActivityView.isExternal();
        }
    }
    /**
     * returns a String with all external nodes of the tree
     */
    public String getExternalNodes() {
        StringBuilder stringBuilder = new StringBuilder("ExternalNodes : ");
        if (mTree.root != null) {
            for (int i = 0; i < mTreeUser.size(); i++) {
                if ((mTree.getChildNode(mTreeUser.get(i),true) == Integer.MIN_VALUE)&&(mTree.getChildNode(mTreeUser.get(i),false)== Integer.MIN_VALUE)) {
                    if(i == mTreeUser.size() -1) {
                        stringBuilder.append(mTreeUser.get(i) + "");
                    } else {
                        stringBuilder.append(mTreeUser.get(i) + " , ");
                    }
                }
            }

        }
        return stringBuilder.toString();
    }
    /**
     * returns a String with all internal nodes of the tree
     */
    public String getInternalNodes() {
        StringBuilder stringBuilder = new StringBuilder("InternalNodes : ");
        if (mTree.root != null) {
            for (int i = 0; i < mTreeUser.size(); i++) {
                if ((mTree.getChildNode(mTreeUser.get(i),true)!= Integer.MIN_VALUE)||(mTree.getChildNode(mTreeUser.get(i),false)!= Integer.MIN_VALUE)) {
                        stringBuilder.append(mTreeUser.get(i) + " , ");
                }
            }

        }
        return stringBuilder.toString();
    }
    /**
     * clears mTree and all of its nodes + the variable mTreeUser
     */
    private void clear() {
        mTreeUser.clear();
        mTree.clear();
        mBinding.BSTActivityView.clear();
    }
    /**
     * adds a new node to the tree
     */
    private void add(int value) {
        mTreeUser.add(value);
        mTree.insert(value);
        mBinding.BSTActivityView.add(value);
    }
    /**
     * returns the max value of the tree
     */
    private void max() {
        int max = mTree.max();
        mBinding.BSTActivityReturnValue.setText(String.format("%s", max));
        mBinding.BSTActivityView.max(max);
    }

    /**
     * returns the min value of the tree
     */
    private void min() {
        int min = mTree.min();
        mBinding.BSTActivityReturnValue.setText(String.format("%s", min));
        mBinding.BSTActivityView.min(min);
    }

    /**
     * adds a random node the value between -100 and 100 to the tree
     */
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

    /**
     * removes a Node form the tree defined  by the key value
     */
    public void remove() {
        if (!(mTree.root == null)) {
                mBinding.BSTActivityView.remove();
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





































