package at.fhooe.mc.datadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArraySet;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Random;
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
        mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.INVISIBLE);
        mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
        mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);

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

        Slider sliderAdd = mBinding.LinkedListActivityAddPositionSlider;
        sliderAdd.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                mBinding.LinkedListActivityAddAtRadioButton.setChecked(true);
            }
        });

        mBinding.LinkedListActivityAddCheck.setOnClickListener(this);
        mBinding.LinkedListActivityDeleteCheck.setOnClickListener(this);
        mBinding.LinkedListActivityGetCheck.setOnClickListener(this);
        mBinding.LinkedListActivityTypeRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivitySwitch.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityRandomBackground.setOnClickListener(this);
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
                preparePositionSlider();
        } else if(_v == mBinding.LinkedListActivityDeleteCheck) {
            if (mBinding.LinkedListActivityDeleteFirstRadioButton.isChecked()) {
                deleteFirst();
            } else if (mBinding.LinkedListActivityDeleteLastRadioButton.isChecked()) {
                deleteLast();
            } else if (mBinding.LinkedListActivityDeleteAtRadioButton.isChecked()) {
                deleteAt();
            }
               preparePositionSlider();
        } else if(_v == mBinding.LinkedListActivityGetCheck) {
            if (mBinding.LinkedListActivityGetPreRadioButton.isChecked()) {
                getPredecessor();
            } else if (mBinding.LinkedListActivityGetSuccRadioButton.isChecked()) {
                getSuccessor();
            } else if (mBinding.LinkedListActivityGetFirstRadioButton.isChecked()) {
                getFirst();
            } else if (mBinding.LinkedListActivityGetLastRadioButton.isChecked()) {
                getLast();
            } else if (mBinding.LinkedListActivityGetAtRadioButton.isChecked()) {
                getAt();
            }
            preparePositionSlider();
        } else if (_v == mBinding.LinkedListActivityRandomBackground) {
            random();
            preparePositionSlider();
        }
    }

    /**
     * prepares the position slider of the operations add, delete and get.
     *
     * If the linked list is not empty the slider will be shown and its range will be updated according to the size of the list.
     * If the linked list is empty the slider will be hidden and a text field displaying "0" will be shown
     *
     */
    private void preparePositionSlider(){

        if(mLinkedList.size() == 1) {
            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityAddPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityAddPositionSlider.setStepSize(1);

            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityDeletePositionSlider.setStepSize(1);

            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityGetPositionSlider.setStepSize(1);

        } else if (mLinkedList.size() == 0) {

            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);

            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.VISIBLE);
        } else {
            //TODO: position slide has 1 value too much
            mBinding.LinkedListActivityAddPositionSlider.setValueTo(mLinkedList.size());
            mBinding.LinkedListActivityDeletePositionSlider.setValueTo(mLinkedList.size() - 1);
            mBinding.LinkedListActivityGetPositionSlider.setValueTo(mLinkedList.size() - 1);
        }
    }

    private void sorted(){

    }

    private void unsorted() {

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

    private void deleteFirst() {
        mBinding.LinkedListActivityLinkedListView.deleteFirst();
        mLinkedList.remove(0);
    }

    private void deleteLast() {
        mBinding.LinkedListActivityLinkedListView.deleteLast();
        mLinkedList.remove(mLinkedList.size() - 1);
    }

    private void deleteAt() {
        int pos = (int) mBinding.LinkedListActivityDeletePositionSlider.getValue();
        mBinding.LinkedListActivityLinkedListView.deleteAt(pos);
        mLinkedList.remove(pos);
    }

    private void getPredecessor() {
        //TODO gerald
    }

    private void getSuccessor() {
        //TODO gerald
    }

    private void getFirst() {

        if(!mLinkedList.isEmpty()){
            mBinding.LinkedListActivityLinkedListView.getFirst();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.LinkedListActivityLinkedListView.mLinkedListNumbers.get(0).toString()));
                }
            }, 500);


        } else {
            Toast.makeText(this, R.string.LinkedList_Activity_Toast_Empty, Toast.LENGTH_SHORT).show();
            mBinding.LinkedListActivityReturnValue.setText("");
            //todo: showEmpty(); like in the stack?
        }




    }

    private void getLast() {

        if(!mLinkedList.isEmpty()){
            mBinding.LinkedListActivityLinkedListView.getLast();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.LinkedListActivityLinkedListView.mLinkedListNumbers.get(
                                    mBinding.LinkedListActivityLinkedListView.mLinkedListNumbers.size() - 1
                            ).toString()));
                }
            }, 500);


        } else {
            Toast.makeText(this, R.string.LinkedList_Activity_Toast_Empty, Toast.LENGTH_SHORT).show();
            mBinding.LinkedListActivityReturnValue.setText("");
            //todo: showEmpty(); like in the stack?
        }


    }

    private void getAt() {

        //TODO: position slider is not working yet ... has 1 range to much (from 0 - 5 when only 5 vals are in list)

        if(!mLinkedList.isEmpty()){
            final int pos = (int) mBinding.LinkedListActivityGetPositionSlider.getValue();
            mBinding.LinkedListActivityLinkedListView.getAt(pos);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.LinkedListActivityLinkedListView.mLinkedListNumbers.get(
                                    mBinding.LinkedListActivityLinkedListView.mLinkedListNumbers.elementAt(pos)).toString())); //TODO check
                }
            }, 500);


        } else {
            Toast.makeText(this, R.string.LinkedList_Activity_Toast_Empty, Toast.LENGTH_SHORT).show();
            mBinding.LinkedListActivityReturnValue.setText("");
        }

    }

    /**
     * This method handles the operation random
     */
    private void random(){
        mBinding.LinkedListActivityReturnValue.setText("");
        createRandomQueue();
        mBinding.LinkedListActivityLinkedListView.random(mLinkedList);
    }

    /**
     * Creates a random queue with its size being min 4 and max 7
     */
    private void createRandomQueue(){
        mLinkedList.clear();
        Random r = new Random();
        int size = 4 + r.nextInt(6);

        for(int i = 0; i < size; i++){
            int x = -5 + r.nextInt(100);
            mLinkedList.add(x);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) {
        if (_buttonView == mBinding.LinkedListActivitySwitch) {
            if (_isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        } else if (_buttonView == mBinding.LinkedListActivityTypeSortedSwitch) {
            if (_isChecked) {
                sorted();
            } else {
                unsorted();
            }
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