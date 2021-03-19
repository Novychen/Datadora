package at.fhooe.mc.datadora.LinkedList;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import java.util.Random;
import java.util.Vector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import at.fhooe.mc.datadora.Animation;
import at.fhooe.mc.datadora.R;

import at.fhooe.mc.datadora.databinding.ActivityDoubleLinkedListBinding;

public class DoubleLinkedListActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "DoubleLinkedListActivity : ";

    private ActivityDoubleLinkedListBinding mBinding;

    private boolean mPressedDelete;
    private boolean mPressedRandom;
    private Animation mAnimation;

    public void setPressedDelete(boolean _delete) {
        mPressedDelete = _delete;
    }

    public void setPressedRandom(boolean _random) {
        mPressedRandom = _random;
    }

    protected ActivityDoubleLinkedListBinding getBinding() {
        return mBinding;
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivityDoubleLinkedListBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        View layout = mBinding.LinkedListActivity;
        mAnimation = new Animation(layout, getIntent(), this);

        mBinding.DoubleLinkedListActivityLinkedListView.setActivity(this);
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
                mBinding.DoubleLinkedListActivityLinkedListView.setSwitch(_isChecked);
            }
        });

    }

    private void setUpToolbar() {
        Toolbar myToolbar = mBinding.LinkedListActivityToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.All_Data_Activity_Double_LinkedList);
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
        if (mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() >= 17) {
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

        if (mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() == 0) {

            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);

            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.VISIBLE);

        } else if (mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() == 1){

            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityAddPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityAddPositionSlider.setStepSize(1);

            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.VISIBLE);

            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.INVISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);

        } else if (mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() == 2) {

            mBinding.LinkedListActivityAddPositionSlider.setValueTo(
                    mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size());

            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityDeletePositionSlider.setStepSize(1);
            if(mBinding.LinkedListActivityDeletePositionSlider.getValue() ==
                    mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size()) {
                mBinding.LinkedListActivityDeletePositionSlider.setValue(
                        mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() - 1);
            }
            mBinding.LinkedListActivityDeletePositionSlider.setValueTo(
                    mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() - 1);

            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityGetPositionSlider.setStepSize(1);
            mBinding.LinkedListActivityGetPositionSlider.setValueTo(
                    mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() - 1);

        } else {
            mBinding.LinkedListActivityAddPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityAddPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityAddPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityAddPositionSlider.setStepSize(1);
            mBinding.LinkedListActivityAddPositionSlider.setValueTo(
                    mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() - 1);

            mBinding.LinkedListActivityGetPositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityGetPositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityGetPositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityGetPositionSlider.setStepSize(1);
            mBinding.LinkedListActivityGetPositionSlider.setValueTo(
                    mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() - 1);

            mBinding.LinkedListActivityDeletePositionZero.setVisibility(View.GONE);
            mBinding.LinkedListActivityDeletePositionSlider.setVisibility(View.VISIBLE);
            mBinding.LinkedListActivityDeletePositionSlider.setValueFrom(0);
            mBinding.LinkedListActivityDeletePositionSlider.setStepSize(1);
            if(mBinding.LinkedListActivityDeletePositionSlider.getValue() ==
                    mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size()) {
                mBinding.LinkedListActivityDeletePositionSlider.setValue(
                        mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() - 1);
            }
            mBinding.LinkedListActivityDeletePositionSlider.setValueTo(
                    mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() - 1);
        }
    }

    private void sorted(){

    }

    private void unsorted() {

    }

    private void head(){
        mBinding.DoubleLinkedListActivityLinkedListView.head();
    }

    private void tail(){
        mBinding.DoubleLinkedListActivityLinkedListView.tail();
    }

    private void both(){
        mBinding.DoubleLinkedListActivityLinkedListView.both();
    }

    private void prepend(){
        mBinding.LinkedListActivityAddPrependRadioButton.setChecked(false);

        if(isInputValid()) {
            int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
            mBinding.DoubleLinkedListActivityLinkedListView.prepend(value);
        }
    }

    private void append(){
        mBinding.LinkedListActivityAddAppendRadioButton.setChecked(false);

        if(isInputValid()) {
            int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
            mBinding.DoubleLinkedListActivityLinkedListView.append(value);
        }
    }

    private void insertAt(){
        mBinding.LinkedListActivityAddAtRadioButton.setChecked(false);

        if(isInputValid()) {
            int value = (int) mBinding.LinkedListActivityInputSlider.getValue();
            int pos = (int) mBinding.LinkedListActivityAddPositionSlider.getValue();
            mBinding.DoubleLinkedListActivityLinkedListView.insertAt(value, pos);
        }
    }

    private void clear(){
        mBinding.LinkedListActivityDeleteAllRadioButton.setChecked(false);

        if(!mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().isEmpty()){
            mPressedDelete = true;
            mBinding.DoubleLinkedListActivityLinkedListView.clear();
        } else {
            isEmptyMessage();
        }
    }

    private void deleteFirst() {
        mBinding.LinkedListActivityDeleteFirstRadioButton.setChecked(false);

        if(!mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().isEmpty()){
            mPressedDelete = true;
            mBinding.DoubleLinkedListActivityLinkedListView.deleteFirst();

        } else {
            isEmptyMessage();
        }
    }

    private void deleteLast() {
        mBinding.LinkedListActivityDeleteLastRadioButton.setChecked(false);

        if(!mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().isEmpty()){
            mPressedDelete = true;
            mBinding.DoubleLinkedListActivityLinkedListView.deleteLast();

        } else {
            isEmptyMessage();
        }
    }

    private void deleteAt() {
        mBinding.LinkedListActivityDeleteAtRadioButton.setChecked(false);

        if (!mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().isEmpty() &&
                mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() > 1) {
            mPressedDelete = true;
            int pos = (int) mBinding.LinkedListActivityDeletePositionSlider.getValue();
            mBinding.DoubleLinkedListActivityLinkedListView.deleteAt(pos);
        } else if (mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() == 1) {
            clear();
        } else {
            isEmptyMessage();
        }

    }

    private void getSize(){
        if(!mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().isEmpty()){
            mBinding.DoubleLinkedListActivityLinkedListView.getSize();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.DoubleLinkedListActivityLinkedListView.
                                    getDoubleLinkedListNumbers().size()));
                }
            }, (600 * mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().size() - 1));

        } else {
            isEmptyMessage();
        }
    }

    private void getPredecessor() {
        Toast.makeText(this, R.string.LinkedList_Activity_Pre_Succ_Hint, Toast.LENGTH_SHORT).show();
        mBinding.DoubleLinkedListActivityLinkedListView.predecessor();
    }

    private void getSuccessor() {
        Toast.makeText(this, R.string.LinkedList_Activity_Pre_Succ_Hint, Toast.LENGTH_SHORT).show();
        mBinding.DoubleLinkedListActivityLinkedListView.successor();
    }

    private void getFirst() {

        if(!mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().isEmpty()){
            mBinding.DoubleLinkedListActivityLinkedListView.getFirst();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().get(0).toString()));
                }
            }, 500);

        } else {
            isEmptyMessage();
        }
    }

    private void getLast() {

        if(!mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().isEmpty()){
            mBinding.DoubleLinkedListActivityLinkedListView.getLast();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.DoubleLinkedListActivityLinkedListView.
                                    getDoubleLinkedListNumbers().get(
                                    mBinding.DoubleLinkedListActivityLinkedListView.
                                            getDoubleLinkedListNumbers().size() - 1).toString()));
                }
            }, 500);
        } else {
            isEmptyMessage();
        }
    }

    private void getAt() {
        mBinding.LinkedListActivityGetAtRadioButton.setChecked(false);

        if(!mBinding.DoubleLinkedListActivityLinkedListView.getDoubleLinkedListNumbers().isEmpty()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int pos = (int) mBinding.LinkedListActivityGetPositionSlider.getValue();
                    mBinding.DoubleLinkedListActivityLinkedListView.getAt(pos);

                    mBinding.LinkedListActivityReturnValue.setText(
                            String.format("%s", mBinding.DoubleLinkedListActivityLinkedListView.
                                    getDoubleLinkedListNumbers().get(pos).toString()));
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
        mBinding.DoubleLinkedListActivityLinkedListView.random(createRandomList());
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
