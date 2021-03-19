package at.fhooe.mc.datadora.Stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.Animation;
import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.databinding.ActivityStackBinding;

public class StackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StackActivity : ";
    private ActivityStackBinding mBinding;

    private boolean mPressedRandom;
    private boolean mPressedPop;
    private Animation mAnimation;

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

        View layout = mBinding.StackActivity;
        mAnimation = new Animation(layout, getIntent(), this);

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
        getSupportActionBar().setTitle(R.string.All_Data_Activity_Stack);
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    @Override
    public void onClick(View v) {
        if (!mPressedPop && !mPressedRandom) {
            if (v == mBinding.StackActivityButtonPush) { push();
            } else if (v == mBinding.StackActivityButtonPop) { pop();
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
     * show the size of the stack
     */
    protected void showSize(){
        mBinding.StackActivityReturnValue.setText(String.format("%s", mBinding.StackActivityStackView.getStackNumbers().size()));
        //Log.i(TAG, "showSize: Stack size was: " + mStack.size());
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
        if (mBinding.StackActivityStackView.getStackNumbers().size() > 14) {
            Toast.makeText(this, R.string.Overflow, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "isInputValid: Stack size >= 15: " + mBinding.StackActivityStackView.getStackNumbers().size());
            showFull();
            return false;
        } else if (mBinding.StackActivityStackView.getStackNumbers().size() == 14) {
            //Log.i(TAG, "isInputValid: Stack size equals 14: " + mStack.size());
            showFull();
        }
        return true;
    }

    /**
     * This method handles the operation push
     */
    private void push(){
        if (isInputValid()) {
            mBinding.StackActivityReturnValue.setText("");
            mBinding.StackActivityStackView.push((int) mBinding.StackActivityInputSlider.getValue());
            makeInVisible();
        }
    }

    /**
     * This method handles the operation pop
     */
    private void pop(){
        if (!mBinding.StackActivityStackView.getStackNumbers().isEmpty()) {
            mPressedPop = true;
            mBinding.StackActivityReturnValue.setText(String.format("%s", mBinding.StackActivityStackView.getStackNumbers().
                    get(mBinding.StackActivityStackView.getStackNumbers().size() - 1).toString()));

            mBinding.StackActivityStackView.pop();
            makeInVisible();
            if (mBinding.StackActivityStackView.getStackNumbers().isEmpty()) {
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
        if (!mBinding.StackActivityStackView.getStackNumbers().isEmpty()) {
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
        if (!mBinding.StackActivityStackView.getStackNumbers().isEmpty()) {
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
        if (!mBinding.StackActivityStackView.getStackNumbers().isEmpty()) {
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
        if (!mBinding.StackActivityStackView.getStackNumbers().isEmpty()) {
            mBinding.StackActivityStackView.clear();
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
        makeInVisible();
        mBinding.StackActivityStackView.random(createRandomStack());
    }

    /**
     * Creates a random stack with its size being min 4 and max 7
     */
    private Vector<Integer> createRandomStack(){

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

}