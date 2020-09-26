package at.fhooe.mc.datadora.LinkedList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.databinding.ActivityLinkedListBinding;

public class LinkedListActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "LinkedListActivity : ";

    private ActivityLinkedListBinding mBinding;
    private Vector<Integer> mLinkedList = new Vector<>();

    //Shared Preferences setup
    private static final String SP_FILE_KEY = "at.fhooe.mc.datadora.LinkedListSharedPreferenceFile.LinkedList";
    private static final String SP_VALUE_KEY = "at.fhooe.mc.datadora.LinkedListKey2020";
    private SharedPreferences mSharedPreferences;

    private boolean mDelete;
    private boolean mRandom;

    public void setDelete(boolean _delete) {
        mDelete = _delete;
    }

    public void setRandom(boolean _random) {
        mRandom = _random;
    }

    protected ActivityLinkedListBinding getBinding() {
        return mBinding;
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivityLinkedListBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.LinkedListActivityLinkedListView.setActivity(this);
        mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.INVISIBLE);
        mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
        mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);

        mSharedPreferences = getSharedPreferences(SP_FILE_KEY, Context.MODE_PRIVATE);

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

        mBinding.LinkedListActivityTypeRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityAddRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityDeleteRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityGetRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivitySwitch.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityRandomBackground.setOnClickListener(this);
    }

    /**
     * This method checks if the given user input is correct.
     * If the stack is bigger than a certain size then the user cannot push anymore as the stack is full
     *
     * @return true if the input is valid, false if its not
     */
    private boolean isInputValid() {
        if (mLinkedList.size() >= 17) {
            Toast.makeText(this, R.string.LinkedList_Activity_Toast_Full, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Vector<Integer> v = loadFromSave();
        if(v != null) {
            mLinkedList.clear();
            mLinkedList.addAll(v);
            preparePositionSlider();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    /**
     * Saves the current vector (input from the user) into the SharedPreferences.
     */
    private void save() {

        // init the SP object
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        // Convert the vector containing the integers to a string
        Vector<Integer> vector = mBinding.LinkedListActivityLinkedListView.mLinkedListNumbers;
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
    public void onClick(View _v) {

        if(!mRandom) {
            if (_v == mBinding.LinkedListActivityRandomBackground) {
                random();
                mRandom = true;
                preparePositionSlider();
            }
        } else {
           Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
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


        if (mLinkedList.size() == 0) {

            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);

            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.VISIBLE);

        } else if (mLinkedList.size() == 1){

            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityAddPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityAddPositionSlider.setStepSize(1);

            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.VISIBLE);

            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);

        } else if (mLinkedList.size() == 2) {

            mBinding.LinkedListActivityAddPositionSlider.setValueTo(mLinkedList.size());

            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityDeletePositionSlider.setStepSize(1);
            if(mBinding.LinkedListActivityDeletePositionSlider.getValue() == mLinkedList.size()) {
                mBinding.LinkedListActivityDeletePositionSlider.setValue(mLinkedList.size() - 1);
            }
            mBinding.LinkedListActivityDeletePositionSlider.setValueTo(mLinkedList.size() - 1);

            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityGetPositionSlider.setStepSize(1);
            mBinding.LinkedListActivityGetPositionSlider.setValueTo(mLinkedList.size() - 1);

        } else {
            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityAddPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityAddPositionSlider.setStepSize(1);
            mBinding.LinkedListActivityAddPositionSlider.setValueTo(mLinkedList.size() - 1);

            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityGetPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityGetPositionSlider.setStepSize(1);
            mBinding.LinkedListActivityGetPositionSlider.setValueTo(mLinkedList.size() - 1);

            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityDeletePositionSlider.setStepSize(1);
            if(mBinding.LinkedListActivityDeletePositionSlider.getValue() == mLinkedList.size()) {
                mBinding.LinkedListActivityDeletePositionSlider.setValue(mLinkedList.size() - 1);
            }
            mBinding.LinkedListActivityDeletePositionSlider.setValueTo(mLinkedList.size() - 1);
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
        mBinding.LinkedListActivityAddPrependRadioButton.setChecked(false);

        if(isInputValid()) {
            int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
            mLinkedList.add(0,value);
            mBinding.LinkedListActivityLinkedListView.prepend(value);
        }
    }

    private void append(){
        mBinding.LinkedListActivityAddAppendRadioButton.setChecked(false);

        if(isInputValid()) {
            int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
            mLinkedList.add(value);
            mBinding.LinkedListActivityLinkedListView.append(value);
        }
    }

    private void insertAt(){
        mBinding.LinkedListActivityAddAtRadioButton.setChecked(false);

        if(isInputValid()) {
            int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
            int pos = (int) mBinding.LinkedListActivityAddPositionSlider.getValue();
            mLinkedList.add(value);
            mBinding.LinkedListActivityLinkedListView.insertAt(value, pos);
        }
    }

    private void clear(){
        mBinding.LinkedListActivityDeleteAllRadioButton.setChecked(false);

        if(!mLinkedList.isEmpty()){
            mDelete = true;
            mBinding.LinkedListActivityLinkedListView.clear();
            mLinkedList.clear();
        } else {
            isEmptyMessage();
        }
    }

    private void deleteFirst() {
        mBinding.LinkedListActivityDeleteFirstRadioButton.setChecked(false);

        if(!mLinkedList.isEmpty()){
            mDelete = true;
            mBinding.LinkedListActivityLinkedListView.deleteFirst();
            mLinkedList.remove(0);

        } else {
            isEmptyMessage();
        }
    }

    private void deleteLast() {
        mBinding.LinkedListActivityDeleteLastRadioButton.setChecked(false);

        if(!mLinkedList.isEmpty()){
            mDelete = true;
            mBinding.LinkedListActivityLinkedListView.deleteLast();
            mLinkedList.remove(mLinkedList.size() - 1);

        } else {
            isEmptyMessage();
        }
    }

    private void deleteAt() {
        mBinding.LinkedListActivityDeleteAtRadioButton.setChecked(false);

        if (!mLinkedList.isEmpty() && mLinkedList.size() > 1) {
            mDelete = true;
            int pos = (int) mBinding.LinkedListActivityDeletePositionSlider.getValue();
            mBinding.LinkedListActivityLinkedListView.deleteAt(pos);
            mLinkedList.remove(pos);
        } else if (mLinkedList.size() == 1) {
            clear();
        } else {
            isEmptyMessage();
        }

    }

    private void getSize(){
        if(!mLinkedList.isEmpty()){
            mBinding.LinkedListActivityLinkedListView.getSize();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.LinkedListActivityLinkedListView.mLinkedListNumbers.size()));
                }
            }, (600 * mLinkedList.size() - 1));

        } else {
            isEmptyMessage();
        }
    }

    private void getPredecessor() {
        mBinding.LinkedListActivityLinkedListView.predecessor();
    }

    private void getSuccessor() {
        mBinding.LinkedListActivityLinkedListView.successor();
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
            isEmptyMessage();
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
                                    mBinding.LinkedListActivityLinkedListView.mLinkedListNumbers.size() - 1).toString()));
                }
            }, 500);
        } else {
            isEmptyMessage();
        }
    }

    private void getAt() {
        mBinding.LinkedListActivityGetAtRadioButton.setChecked(false);

        if(!mLinkedList.isEmpty()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int pos = (int) mBinding.LinkedListActivityGetPositionSlider.getValue();
                    mBinding.LinkedListActivityLinkedListView.getAt(pos);

                    Log.d(TAG, "----- position getAt: "+ pos);

                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.LinkedListActivityLinkedListView.mLinkedListNumbers.get(pos).toString()));
                }
            }, 500);

        } else {
            isEmptyMessage();
        }
    }

    public void isEmptyMessage(){
        Toast.makeText(this, R.string.LinkedList_Activity_Toast_Empty, Toast.LENGTH_SHORT).show();
        mBinding.LinkedListActivityReturnValue.setText("");
    }

    /**
     * This method handles the operation random
     */
    private void random(){
        mBinding.LinkedListActivityReturnValue.setText("");
        createRandomList();
        mBinding.LinkedListActivityLinkedListView.random(mLinkedList);
    }

    /**
     * Creates a random queue with its size being min 4 and max 7
     */
    private void createRandomList(){
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
    public void onCheckedChanged(RadioGroup _radioGroup, int _i) {
        if(!mDelete && !mRandom) {
            if (_radioGroup == mBinding.LinkedListActivityTypeRadioGroup) {
                if (mBinding.LinkedListActivityTypeHeadRadioButton.isChecked()) { head();
                } else if (mBinding.LinkedListActivityTypeTailRadioButton.isChecked()) { tail();
                } else if (mBinding.LinkedListActivityTypeBothRadioButton.isChecked()) { both(); }
            } else if (_radioGroup == mBinding.LinkedListActivityAddRadioGroup) {
                if (mBinding.LinkedListActivityAddPrependRadioButton.getId() == _i) { prepend();
                } else if (mBinding.LinkedListActivityAddAppendRadioButton.getId() == _i) { append();
                } else if (mBinding.LinkedListActivityAddAtRadioButton.getId() == _i) { insertAt(); }
                preparePositionSlider();
            } else if (_radioGroup == mBinding.LinkedListActivityDeleteRadioGroup) {
                if (mBinding.LinkedListActivityDeleteAllRadioButton.getId() == _i) { clear();
                } else if (mBinding.LinkedListActivityDeleteFirstRadioButton.getId() == _i) { deleteFirst();
                } else if (mBinding.LinkedListActivityDeleteLastRadioButton.getId() == _i) { deleteLast();
                } else if (mBinding.LinkedListActivityDeleteAtRadioButton.getId() == _i) { deleteAt(); }
                preparePositionSlider();
            } else if (_radioGroup == mBinding.LinkedListActivityGetRadioGroup) {
                if (mBinding.LinkedListActivityGetSizeRadioButton.getId() == _i) { getSize();
                } else if (mBinding.LinkedListActivityGetPreRadioButton.getId() == _i) { getPredecessor();
                } else if (mBinding.LinkedListActivityGetSuccRadioButton.getId() == _i) { getSuccessor();
                } else if (mBinding.LinkedListActivityGetFirstRadioButton.getId() == _i) { getFirst();
                } else if (mBinding.LinkedListActivityGetLastRadioButton.getId() == _i) { getLast();
                } else if (mBinding.LinkedListActivityGetAtRadioButton.getId() == _i) { getAt(); }
                preparePositionSlider();
            }
        } else {
            Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
        }
    }
}