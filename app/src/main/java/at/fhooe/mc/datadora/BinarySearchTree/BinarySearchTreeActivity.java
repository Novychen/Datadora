package at.fhooe.mc.datadora.BinarySearchTree;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Vector;

import at.fhooe.mc.datadora.Animation;
import at.fhooe.mc.datadora.BinarySearchTree.Fragment.BSTCheckFragment;
import at.fhooe.mc.datadora.BinarySearchTree.Fragment.BSTGetFragment;
import at.fhooe.mc.datadora.BinarySearchTree.Fragment.BSTStandardFragment;
import at.fhooe.mc.datadora.BinarySearchTree.Fragment.BSTStructureFragment;
import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.databinding.ActivityBinarySearchTreeBinding;


public class BinarySearchTreeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BSTActivity :: ";
    private final BinarySearchTree mTree = new BinarySearchTree();
    private final Vector<BinaryTreeNode> mTreeUser = new Vector<>();
    private ActivityBinarySearchTreeBinding mBinding;
    private boolean mSelected = false;
    private final String mDefaultValue = "empty";
    private Animation mAnimation;

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

        View layout = mBinding.BSTActivity;
        mAnimation = new Animation(layout, getIntent(), this);

        mBinding.BSTActivityView.setActivity(this);

        setUpToolbar();
        setUpTabLayout();

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setUpSeekBar();
        } else {
            setUpSlider();
        }

        mBinding.BSTActivityPan.setOnClickListener(this);
        mBinding.BSTActivityCenter.setOnClickListener(this);
        mBinding.BSTActivitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBinding.BSTActivityView.setSwitch(isChecked);
            }
        });

        setTheme(R.style.AppTheme);
    }

    private void setUpSeekBar() {
        assert mBinding.BSTActivitySeekBar != null;
        mBinding.BSTActivitySeekBar.setMax(200);
        mBinding.BSTActivitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mBinding.BSTActivityInputValue.setText(String.valueOf(i - 100));
                Resources.Theme theme = getTheme();
                if(b) {
                    theme.applyStyle(R.style.AppTheme_Mode, true);
                } else {
                    theme.applyStyle(R.style.AppTheme, true);
                }
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
        tabLayout.setTabGravity(TabLayout.GRAVITY_START);

        BinarySearchTreeTabAdapter adapter = new BinarySearchTreeTabAdapter(this);
        BSTStandardFragment fragmentStandard = new BSTStandardFragment();

        adapter.addFragment(fragmentStandard);
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


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
     }




    @Override
    public void onClick(View _view) {

        mBinding.BSTActivityReturnValue.setText("");
        mBinding.BSTActivityVectorOutput.setText("");

        if (mTree == null || mTree.getRoot() == null) {
            mBinding.BSTActivityFlowIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear, this.getTheme()));
            mBinding.BSTActivityFlowText.setText(R.string.BST_Activity_Empty);
            mBinding.BSTActivityFlowText.setVisibility(View.VISIBLE);
            mBinding.BSTActivityFlowIcon.setVisibility(View.VISIBLE);
        } else {
            mBinding.BSTActivityFlowText.setVisibility(View.INVISIBLE);
            mBinding.BSTActivityFlowIcon.setVisibility(View.INVISIBLE);
        }

        if(_view == mBinding.BSTActivityPan) {
            pan();
        } else if (_view == mBinding.BSTActivityCenter) {
            center();
        }
    }

    private void pan() {
        int color;

        if(!mSelected) {
            color = ContextCompat.getColor(this, R.color.primaryColor);
            mBinding.BSTActivityView.move(true);
        } else {
            color = ContextCompat.getColor(this, R.color.secondaryColor);
            mBinding.BSTActivityView.move(false);
        }
        mSelected = !mSelected;
        ImageViewCompat.setImageTintList( mBinding.BSTActivityPan, ColorStateList.valueOf(color));
    }

    private void center() {
        float x = mBinding.BSTActivityView.getTranslateX();
        float y = mBinding.BSTActivityView.getTranslateY();

        if (x == 0 && y == 0) {
            Toast.makeText(this, R.string.BST_Activity_Center, Toast.LENGTH_SHORT).show();
        } else {
            mBinding.BSTActivityView.setTranslate(0,0);
            mBinding.BSTActivityView.invalidate();
        }
    }

    @Override
    public void onBackPressed() {
        mAnimation.circularUnreveal();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}





































