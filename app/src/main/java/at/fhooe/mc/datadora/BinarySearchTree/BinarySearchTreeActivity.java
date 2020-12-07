package at.fhooe.mc.datadora.BinarySearchTree;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Vector;

import at.fhooe.mc.datadora.BinarySearchTree.Fragment.BSTCheckFragment;
import at.fhooe.mc.datadora.BinarySearchTree.Fragment.BSTGetFragment;
import at.fhooe.mc.datadora.BinarySearchTree.Fragment.BSTStandardFragment;
import at.fhooe.mc.datadora.BinarySearchTree.Fragment.BSTStructureFragment;
import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.databinding.ActivityBinarySearchTreeBinding;


public class BinarySearchTreeActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ChipGroup.OnCheckedChangeListener {

    private static final String TAG = "BSTActivity :: ";
    private final BinarySearchTree mTree = new BinarySearchTree();
    private final Vector<BinaryTreeNode> mTreeUser = new Vector<>();
    private ActivityBinarySearchTreeBinding mBinding;
    private BSTStandardFragment mFragmentStandard;

    private String mDefaultValue = "empty";

    private SharedPreferences mSharedPreferences;
    private static final String SP_FILE_KEY = "at.fhooe.mc.datadora.BSTSharedPreferenceFile.BST";
    private static final String SP_VALUE_KEY = "at.fhooe.mc.datadora.BSTKey2020";

    public BinarySearchTree getTree() {
        return mTree;
    }

    public Vector<BinaryTreeNode> getTreeUser() {
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

        setUpToolbar();
        setUpTabLayout();
        setOnClickListener();

        mSharedPreferences = getSharedPreferences(SP_FILE_KEY, Context.MODE_PRIVATE);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setUpSeekBar();
        } else {
            setUpSlider();
        }
    }

    private void setUpSeekBar() {
        mBinding.BSTActivitySeekBar.setMax(200);
        mBinding.BSTActivitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mBinding.BSTActivityInputValue.setText(String.valueOf(i - 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setUpSlider() {
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
    }

    private void setUpToolbar() {
        // setup Toolbar
        Toolbar myToolbar = mBinding.BSTActivityToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.All_Data_Activity_BST);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpTabLayout() {
        TabLayout tabLayout = findViewById(R.id.BST_Activity_TabLayout);

        BinarySearchTreeTabAdapter adapter = new BinarySearchTreeTabAdapter(this);
        mFragmentStandard = new BSTStandardFragment();

        adapter.addFragment(mFragmentStandard);
        adapter.addFragment(new BSTStructureFragment());
        adapter.addFragment(new BSTCheckFragment());
        adapter.addFragment(new BSTGetFragment());

        ViewPager2 viewPager = findViewById(R.id.BST_Activity_ViewPager);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab _tab, int _position) {
                if(_position == 0) {
                    _tab.setText(R.string.BST_Activity_Standard_Title);
                } else if(_position == 1) {
                    _tab.setText(R.string.BST_Activity_Structure_Title);
                } else if(_position == 2) {
                    _tab.setText(R.string.BST_Activity_Check_Title);
                } else {
                    _tab.setText(R.string.BST_Activity_Get_Title);
                }
            }
        }).attach();
    }

    private void setOnClickListener() {

        mBinding.BSTActivityChipGroup.setSelectionRequired(true);
        mBinding.BSTActivityChipGroup.setOnCheckedChangeListener(this);

        mBinding.BSTActivitySwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        String treeString = transformVectorToString();
        savedInstanceState.putString(SP_VALUE_KEY, treeString);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        loadData(savedInstanceState.getString(SP_VALUE_KEY));
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

        String treeString = mSharedPreferences.getString(SP_VALUE_KEY, mDefaultValue);
        loadData(treeString);
     }


    /**
     * loads the saved data into the {@link BinarySearchTreeActivity#mTreeUser} and in the {@link BinarySearchTreeActivity#mTree} and displays it.
     * @param _treeString the string that contains the saved data, which will be used to fill the tree
     */
     private void loadData(String _treeString) {
         Vector<Integer> v = transformStringToVector(_treeString);
         if(v != null) {
             mTreeUser.clear();
             mTree.setRoot(null);
             mTree.setSize(0);
             for (int i = 0; i < v.size(); i++){
                 if(mTree.findNode(v.get(i)) == null) {
                     BinaryTreeNode n = mTree.insertNode(v.get(i));
                     mTree.updateChildCount(mTree.getRoot());
                     mTreeUser.add(n);
                     getBinding().BSTActivityView.add();
                 }
             }
         }
     }


    /**
     * Takes the {@link BinarySearchTreeActivity#mTreeUser} and transforms it into a string
     * @return the transformed string
     */
    private String transformVectorToString() {
        // Convert the vector containing the integers to a string
        StringBuilder vectorStr = new StringBuilder();

        // transform the vector into a string
        for (int i = 0; i < mTreeUser.size(); i++) {
            if (i != mTreeUser.size() - 1) {
                vectorStr.append(mTreeUser.get(i).getData()).append(",");
            } else {
                vectorStr.append(mTreeUser.get(i).getData());
            }
        }

        return String.valueOf(vectorStr);
    }


    /**
     * Saves the current vector (input from the user) into the SharedPreferences.
     */
    private void save() {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String treeString = transformVectorToString();

        editor.putString(SP_VALUE_KEY, treeString);
        editor.apply();
    }


    /**
     * Transforms the given String into an Vector<Integer>
     * @param _treeString the string that will be transformed
     * @return the transformed vector, which represents the data, that the user put into this data structure.
     */
    private Vector<Integer> transformStringToVector(String _treeString) {

        if(_treeString == null || _treeString.contains(mDefaultValue) || _treeString.equals("")) {
            return null;
        }

        Vector<Integer> vector = new Vector<>();

        int begin;
        int end = 0;
        int i;

        while(end > -1) {
            begin = end;
            end = _treeString.indexOf(',', begin);
            if(end == -1) {
                i = Integer.parseInt(_treeString.substring(begin));
            } else {
                i = Integer.parseInt(_treeString.substring(begin, end));
                end++;
            }
            vector.add(i);
        }
        return vector;
    }


    @Override
    public void onClick(View view) {

        if (mTree == null || mTree.getRoot() == null) {
            mBinding.BSTActivityFlowIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear, this.getTheme()));
            mBinding.BSTActivityFlowText.setText(R.string.BST_Activity_Empty);
            mBinding.BSTActivityFlowText.setVisibility(View.VISIBLE);
            mBinding.BSTActivityFlowIcon.setVisibility(View.VISIBLE);
        } else {
            mBinding.BSTActivityFlowText.setVisibility(View.INVISIBLE);
            mBinding.BSTActivityFlowIcon.setVisibility(View.INVISIBLE);
        }

        assert mBinding.BSTActivityVectorOutput != null;

        if (view == mBinding.BSTActivityInorder) {
            mBinding.BSTActivityVectorOutput.setText(ArrayToSting(mTree.toArray(true)));
        } else if (view == mBinding.BSTActivityPostorder) {
            mTree.toArrayPostOrder();
            mBinding.BSTActivityVectorOutput.setText(ArrayToSting(mTree.toArrayPostOrder()));
        } else if (view == mBinding.BSTActivityPreorder) {
            mBinding.BSTActivityVectorOutput.setText(ArrayToSting(mTree.toArrayPreOrder()));
        }
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

    @Override
    public void onCheckedChanged(ChipGroup _group, int _checkedId) {

    }
}





































