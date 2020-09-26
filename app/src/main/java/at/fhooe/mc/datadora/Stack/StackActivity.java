package at.fhooe.mc.datadora.Stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.databinding.ActivityStackBinding;

public class StackActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "StackActivity : ";
    private ActivityStackBinding mBinding;

    private Vector<Integer> mStack = new Vector<>();
    private boolean mPressedRandom;
    private boolean mPressedPop;

    public boolean getPressedRandom() {
        return mPressedRandom;
    }

    public void setPressedRandom(boolean _pressedRandom) {
        mPressedRandom = _pressedRandom;
    }

    public boolean gePressedPop() {
        return mPressedPop;
    }

    public void setPressedPop(boolean _pressedPop) {
        mPressedPop = _pressedPop;
    }

    //Shared Preferences setup
    private static final String SP_FILE_KEY = "at.fhooe.mc.datadora.StackSharedPreferenceFile.Stack";
    private static final String SP_VALUE_KEY = "at.fhooe.mc.datadora.StackKey2020";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityStackBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        mBinding.StackActivityStackView.setActivity(this);

        mSharedPreferences = getSharedPreferences(SP_FILE_KEY, Context.MODE_PRIVATE);

        // set up the slider
        Slider slider = mBinding.StackActivityInputSlider;
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
                mBinding.StackActivityInputValue.setText(String.valueOf((int) value));
            }
        });

        // setup Toolbar
        Toolbar myToolbar = mBinding.StackActivityToolbar;
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set FlowIcon & Text invisible
        makeInVisible();

        mBinding.StackActivityButtonPop.setOnClickListener(this);
        mBinding.StackActivityButtonPush.setOnClickListener(this);
        mBinding.StackActivityButtonPeek.setOnClickListener(this);
        mBinding.StackActivityButtonSize.setOnClickListener(this);
        mBinding.StackActivityButtonEmpty.setOnClickListener(this);
        mBinding.StackActivityButtonClear.setOnClickListener(this);
        mBinding.StackActivityButtonRandom.setOnClickListener(this);
        mBinding.StackActivitySwitch.setOnCheckedChangeListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }



    @Override
    protected void onResume() {
        super.onResume();

        Vector<Integer> v = loadFromSave();
        if(v != null) {
            mStack.clear();
            mStack.addAll(v);
        }

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    @Override
    protected void onStop() { super.onStop(); }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    @Override
    public void onClick(View v) {
        if (!mPressedPop && !mPressedRandom) {
            if (v == mBinding.StackActivityButtonPush) {
                push();
            } else if (v == mBinding.StackActivityButtonPop) {
                pop();
            } else if(v == mBinding.StackActivityButtonPeek) { peek();
            } else if (v == mBinding.StackActivityButtonSize) { size();
            } else if (v == mBinding.StackActivityButtonEmpty) { isEmpty();
            } else if (v == mBinding.StackActivityButtonClear) { clear();
            } else if (v == mBinding.StackActivityButtonRandom) {
                mPressedRandom = true;
                random();
            }
        } else {
            Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Saves the current vector (input from the user) into the SharedPreferences.
     */
    private void save() {

        // init the SP object
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        // Convert the vector containing the integers to a string
        Vector<Integer> vector = mBinding.StackActivityStackView.getStackNumbers();
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

    /**
     * show the size of the stack
     */
    protected void showSize(){
        mBinding.StackActivityReturnValue.setText(String.format("%s", mBinding.StackActivityStackView.getStackNumbers().size()));
    }

    /**
     * show that the stack is currently empty
     */
    protected void showEmpty() {
        mBinding.StackActivityFlowIcon.setVisibility(View.VISIBLE);
        mBinding.StackActivityFlowText.setVisibility(View.VISIBLE);
        mBinding.StackActivityFlowIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear, this.getTheme()));
        mBinding.StackActivityFlowText.setText(R.string.Stack_Activity_Empty);
    }

    /**
     * Makes the ImageView and the TextView used for displaying R.string.Stack_Activity_Empty and R.string.Stack_Activity_Full invisible
     */
    protected void makeInVisible(){
        mBinding.StackActivityFlowIcon.setVisibility(View.INVISIBLE);
        mBinding.StackActivityFlowText.setVisibility(View.INVISIBLE);
    }

    /**
     * show that the stack is currently full
     */
    private void showFull() {
        mBinding.StackActivityFlowIcon.setVisibility(View.VISIBLE);
        mBinding.StackActivityFlowText.setVisibility(View.VISIBLE);
        mBinding.StackActivityFlowIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, this.getTheme()));
        mBinding.StackActivityFlowText.setText(R.string.Stack_Activity_Full);
    }

    /**
     * This method checks if the given user input is correct.
     * If the stack is bigger than a certain size then the user cannot push anymore as the stack is full
     *
     * @return true if the input is valid, false if its not
     */
    private boolean isInputValid() {
        if (mStack.size() > 14) {
            Toast.makeText(this, R.string.Overflow, Toast.LENGTH_SHORT).show();
            showFull();
            return false;
        } else if (mStack.size() == 14) {
            showFull();
        }
        return true;
    }

    /**
     * This method handles the operation push
     */
    private void push(){
        if (isInputValid()) {
            // parse the input to an int and store it into the stack (mStack), then let it be drawn by the StackView
            mStack.add((int) mBinding.StackActivityInputSlider.getValue());
            mBinding.StackActivityReturnValue.setText("");
            mBinding.StackActivityStackView.push((int) mBinding.StackActivityInputSlider.getValue());
            makeInVisible();
        }
    }

    /**
     * This method handles the operation pop
     */
    private void pop(){
        if (!mStack.isEmpty()) {
            mPressedPop = true;
            //delete the last element of the stack (mStack), then let it be (visually) removed by the StackView
            mBinding.StackActivityReturnValue.setText(String.format("%s", mStack.get(mStack.size() - 1).toString()));
            mStack.removeElementAt(mStack.size() - 1);
            mBinding.StackActivityStackView.pop();
            makeInVisible();
            if (mStack.isEmpty()) {
                showEmpty();
            }
        } else {
            Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
            mBinding.StackActivityReturnValue.setText("");
            showEmpty();
        }
    }

    /**
     * This method handles the operation peek
     */
    private void peek(){
        if (!mStack.isEmpty()) {
            mBinding.StackActivityStackView.peek();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mBinding.StackActivityReturnValue.setText(String.format("%s",  mBinding.StackActivityStackView.getStackNumbers().get(
                            mBinding.StackActivityStackView.getStackNumbers().size() - 1).toString()));
                }
            }, 500);
        } else {
            Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
            mBinding.StackActivityReturnValue.setText("");
            showEmpty();
        }
    }

    /**
     * This method handles the operation size
     */
    private void size(){
        mBinding.StackActivityReturnValue.setText("");
        if (!mStack.isEmpty()) {
            showSize();
        } else {
            mBinding.StackActivityReturnValue.setText("0");
            showEmpty();
        }
    }

    /**
     * This method handles the operation isEmpty
     */
    private void isEmpty(){
        if (!mStack.isEmpty()) {
            mBinding.StackActivityReturnValue.setText(R.string.All_Data_Activity_False);
        } else {
            mBinding.StackActivityReturnValue.setText(R.string.All_Data_Activity_True);
        }
    }

    /**
     * This method handles the operation clear
     */
    private void clear(){
        mBinding.StackActivityReturnValue.setText("");
        if (!mStack.isEmpty()) {
            mBinding.StackActivityStackView.clear();
            mStack.clear();
        } else {
            Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
            showEmpty();
        }
    }

    /**
     * This method handles the operation random
     */
    private void random(){
        mPressedRandom = true;
        mBinding.StackActivityReturnValue.setText("");
        createRandomStack();
        makeInVisible();
        mBinding.StackActivityStackView.random(mStack);
    }

    /**
     * Creates a random stack with its size being min 4 and max 7
     */
    private void createRandomStack(){
        mStack.clear();
        Random r = new Random();
        int size = 4 + r.nextInt(6);

        for(int i = 0; i < size; i++){
            int x = -5 + r.nextInt(100);
            mStack.add(x);
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
        if (_isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}