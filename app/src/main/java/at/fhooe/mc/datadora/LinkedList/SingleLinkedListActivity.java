package at.fhooe.mc.datadora.LinkedList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import at.fhooe.mc.datadora.Animation;
import at.fhooe.mc.datadora.R;

import at.fhooe.mc.datadora.databinding.ActivitySingleLinkedListBinding;



public class SingleLinkedListActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "SingleLinkedListActivity : ";

    private ActivitySingleLinkedListBinding mBinding;

    private boolean mPressedDelete;
    private boolean mPressedRandom;
    private Animation mAnimation;

    public void setPressedDelete(boolean _delete) {
        mPressedDelete = _delete;
    }

    public void setPressedRandom(boolean _random) {
        mPressedRandom = _random;
    }

    protected ActivitySingleLinkedListBinding getBinding() {
        return mBinding;
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivitySingleLinkedListBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        View layout = mBinding.LinkedListActivity;
        mAnimation = new Animation(layout, getIntent(), this);

        mBinding.SingleLinkedListActivityLinkedListView.setActivity(this);
        mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.INVISIBLE);
        mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
        mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);


        head();
        setUpToolbar();
        setUpSlider();

        mBinding.LinkedListActivityTypeRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityAddRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityDeleteRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityGetRadioGroup.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityRandomBackground.setOnClickListener(this);

        mBinding.LinkedListActivitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) {
                mBinding.SingleLinkedListActivityLinkedListView.setSwitch(_isChecked);
            }
        });

    }

    private void setUpToolbar() {
        Toolbar myToolbar = mBinding.LinkedListActivityToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.All_Data_Activity_Single_LinkedList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpSlider() {
        Slider slider = mBinding.LinkedListActivityInputSlider;
        slider.setLabelFormatter(value -> {
            return String.valueOf((int) value);    // converting the float value to an int value
        });
        slider.addOnChangeListener((slider1, value, fromUser) ->
                mBinding.LinkedListActivityInputValue.setText(String.valueOf((int) value)));
    }

    /**
     * This method checks if the given user input is correct.
     * If the list is bigger than a certain size then the user cannot push anymore as the stack is full
     *
     * @return true if the input is valid, false if its not
     */
    private boolean isInputValid() {
        if (mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() >= 17){
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View _v) {
        mBinding.LinkedListActivityReturnText.setText(R.string.All_Data_Activity_Text_Return);

        if(!mPressedRandom) {
            if (_v == mBinding.LinkedListActivityRandomBackground) {
                mPressedRandom = true;
                random();
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

        if (mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() == 0) {

            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);

            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.VISIBLE);

        } else if (mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() == 1){

            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityAddPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityAddPositionSlider.setStepSize(1);

            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.VISIBLE);

            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);

        } else if (mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() == 2) {

            mBinding.LinkedListActivityAddPositionSlider.setValueTo(
                    mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size());

            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityDeletePositionSlider.setStepSize(1);
            if(mBinding.LinkedListActivityDeletePositionSlider.getValue() ==
                    mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size()) {
                mBinding.LinkedListActivityDeletePositionSlider.setValue(
                        mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() - 1);
            }
            mBinding.LinkedListActivityDeletePositionSlider.setValueTo(
                    mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() - 1);

            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityGetPositionSlider.setStepSize(1);
            mBinding.LinkedListActivityGetPositionSlider.setValueTo(
                    mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() - 1);

        } else {
            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityAddPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityAddPositionSlider.setStepSize(1);
            mBinding.LinkedListActivityAddPositionSlider.setValueTo(
                    mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() - 1);

            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityGetPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityGetPositionSlider.setStepSize(1);
            mBinding.LinkedListActivityGetPositionSlider.setValueTo(
                    mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() - 1);

            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityDeletePositionSlider.setStepSize(1);
            if(mBinding.LinkedListActivityDeletePositionSlider.getValue() ==
                    mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size()) {
                mBinding.LinkedListActivityDeletePositionSlider.setValue(
                        mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() - 1);
            }
            mBinding.LinkedListActivityDeletePositionSlider.setValueTo(
                    mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() - 1);
        }
    }

    private void sorted(){

    }

    private void unsorted() {

    }

    private void head(){
        mBinding.SingleLinkedListActivityLinkedListView.head();
    }

    private void tail(){
        mBinding.SingleLinkedListActivityLinkedListView.tail();
    }

    private void both(){
        mBinding.SingleLinkedListActivityLinkedListView.both();
    }

    private void prepend(){
        mBinding.LinkedListActivityAddPrependRadioButton.setChecked(false);

        if(isInputValid()) {
            int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
            mBinding.SingleLinkedListActivityLinkedListView.prepend(value);
        }
    }

    private void append(){
        mBinding.LinkedListActivityAddAppendRadioButton.setChecked(false);

        if(isInputValid()) {
            int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
            mBinding.SingleLinkedListActivityLinkedListView.append(value);
        }
    }

    private void insertAt(){
        mBinding.LinkedListActivityAddAtRadioButton.setChecked(false);

        if(isInputValid()) {
            int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
            int pos = (int) mBinding.LinkedListActivityAddPositionSlider.getValue();
            mBinding.SingleLinkedListActivityLinkedListView.insertAt(value, pos);
        }
    }

    private void clear(){
        mBinding.LinkedListActivityDeleteAllRadioButton.setChecked(false);

        if(!mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().isEmpty()){
            mPressedDelete = true;
            mBinding.SingleLinkedListActivityLinkedListView.clear();
        } else {
            isEmptyMessage();
        }
    }

    private void deleteFirst() {
        mBinding.LinkedListActivityDeleteFirstRadioButton.setChecked(false);

        if(!mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().isEmpty()){
            mPressedDelete = true;
            mBinding.SingleLinkedListActivityLinkedListView.deleteFirst();
        } else {
            isEmptyMessage();
        }
    }

    private void deleteLast() {
        mBinding.LinkedListActivityDeleteLastRadioButton.setChecked(false);

        if(!mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().isEmpty()){
            mPressedDelete = true;
            mBinding.SingleLinkedListActivityLinkedListView.deleteLast();
        } else {
            isEmptyMessage();
        }
    }

    private void deleteAt() {
        mBinding.LinkedListActivityDeleteAtRadioButton.setChecked(false);

        if (!mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().isEmpty() &&
                mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() > 1) {
            mPressedDelete = true;
            int pos = (int) mBinding.LinkedListActivityDeletePositionSlider.getValue();
            mBinding.SingleLinkedListActivityLinkedListView.deleteAt(pos);
        } else if (mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() == 1) {
            clear();
        } else {
            isEmptyMessage();
        }

    }

    private void getSize(){
        if(!mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().isEmpty()){
            mBinding.SingleLinkedListActivityLinkedListView.getSize();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size()));
                }
            }, (600 * mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() - 1));

        } else {
            isEmptyMessage();
        }
    }

    private void getPredecessor() {
        Toast.makeText(this, R.string.LinkedList_Activity_Pre_Succ_Hint, Toast.LENGTH_SHORT).show();
        mBinding.SingleLinkedListActivityLinkedListView.predecessor();
    }

    private void getSuccessor() {
        Toast.makeText(this, R.string.LinkedList_Activity_Pre_Succ_Hint, Toast.LENGTH_SHORT).show();
        mBinding.SingleLinkedListActivityLinkedListView.successor();
    }

    private void getFirst() {

        if(!mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().isEmpty()){
            mBinding.SingleLinkedListActivityLinkedListView.getFirst();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().get(0).toString()));
                }
            }, 500);

        } else {
            isEmptyMessage();
        }
    }

    private void getLast() {

        if(!mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().isEmpty()){
            mBinding.SingleLinkedListActivityLinkedListView.getLast();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().get(
                                    mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().size() - 1).toString()));
                }
            }, 500);
        } else {
            isEmptyMessage();
        }
    }

    private void getAt() {
        mBinding.LinkedListActivityGetAtRadioButton.setChecked(false);

        if(!mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().isEmpty()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int pos = (int) mBinding.LinkedListActivityGetPositionSlider.getValue();
                    mBinding.SingleLinkedListActivityLinkedListView.getAt(pos);

                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.SingleLinkedListActivityLinkedListView.getSingleLinkedListNumbers().get(pos).toString()));
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
        setPressedRandom(true);
        mBinding.LinkedListActivityReturnValue.setText("");
        mBinding.SingleLinkedListActivityLinkedListView.random(createRandomList());
    }

    /**
     * Creates a random queue with its size being min 4 and max 7
     */
    private Vector<Integer> createRandomList(){

        Vector<Integer> v = new Vector<>();
        Random r = new Random();
        int size = 4 + r.nextInt(6);

        for(int i = 0; i < size; i++){
            int x = -5 + r.nextInt(100);
            v.add(x);
        }
        return v;
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

    @Override
    public void onCheckedChanged(RadioGroup _radioGroup, int _i) {
        mBinding.LinkedListActivityReturnText.setText(R.string.All_Data_Activity_Text_Return);
        mBinding.LinkedListActivityReturnValue.setText("");

        if(!mPressedDelete && !mPressedRandom) {
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