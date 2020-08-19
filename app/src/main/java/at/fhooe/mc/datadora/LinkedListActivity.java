package at.fhooe.mc.datadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArraySet;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Vector;

import at.fhooe.mc.datadora.databinding.ActivityLinkedListBinding;

public class LinkedListActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "LinkedListActivity : ";

    private ActivityLinkedListBinding mBinding;
    private Vector<Integer> mLinkedList = new Vector<>();

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivityLinkedListBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.LinkedListActivityLinkedListView.init(this);
        mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.GONE);

        head();

        // setup Toolbar
        Toolbar myToolbar = mBinding.LinkedListActivityToolbar;
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set up the slider
        Slider slider = mBinding.LinkedListActivityInputSlider;
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
                mBinding.LinkedListActivityInputValue.setText(String.valueOf((int) value));
            }
        });

        mBinding.LinkedListActivityAddCheck.setOnClickListener(this);
        mBinding.LinkedListActivityTypeRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivitySwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View _v) {

        if(_v == mBinding.LinkedListActivityAddCheck) {
            if (mBinding.LinkedListActivityAddPrependRadioButton.isChecked()) {
                prepend();
            } else if (mBinding.LinkedListActivityAddAppendRadioButton.isChecked()) {
                append();
            } else if (mBinding.LinkedListActivityAddAtRadioButton.isChecked()) {
                insertAt();
            }
            if(mLinkedList.size() == 1){
                preparePositionSlider();
            }
            mBinding.LinkedListActivityAddPositionSlider.setValueTo(mLinkedList.size());
        }
    }

    private void preparePositionSlider(){
        mBinding.LinkedListActivityAddPositionZero.setVisibility(View.GONE);
        mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.VISIBLE);
        mBinding.LinkedListActivityAddPositionSlider.setValueFrom(0);
        mBinding.LinkedListActivityAddPositionSlider.setStepSize(1);
    }

    private void head(){
        mBinding.LinkedListActivityLinkedListView.head();
    }

    private void tail(){
        mBinding.LinkedListActivityLinkedListView.tail();
    }

    private void both(){
        mBinding.LinkedListActivityLinkedListView.both();
    }

    private void prepend(){
        int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
        mLinkedList.add(0,value);
        mBinding.LinkedListActivityLinkedListView.prepend(value);
    }

    private void append(){
        int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
        mLinkedList.add(value);
        mBinding.LinkedListActivityLinkedListView.append(value);
    }

    private void insertAt(){
        int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
        int pos = (int) mBinding.LinkedListActivityAddPositionSlider.getValue();
        mLinkedList.add(value);
        mBinding.LinkedListActivityLinkedListView.insertAt(value, pos);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        return true;
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (mBinding.LinkedListActivityTypeHeadRadioButton.isChecked()) {
            head();
        } else if (mBinding.LinkedListActivityTypeTailRadioButton.isChecked()) {
            tail();
        } else if (mBinding.LinkedListActivityTypeBothRadioButton.isChecked()) {
            both();
        }
    }
}