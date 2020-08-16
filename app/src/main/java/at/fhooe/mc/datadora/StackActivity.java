package at.fhooe.mc.datadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.databinding.ActivityStackBinding;

public class StackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StackActivity : ";
    private ActivityStackBinding mBinding;

    //TODO: 2x same data -> Stack!!
    //TODO: Animation better
    //TODO: Stack size (?) fine tuning
    //TODO: Styles, Themes, ...!!
    //TODO: Resize - animation (?) -> Animation clear -> too ugly when many elements
    //TODO: ENUM for operations (?)
    //TODO: Blocked UI while animation -> thats ok?

    /* Stores the integer values, that the user put it - is only for testing purposes,
     * feel free to change the way this data is stored - just remember that you have to
     * give the StackView.push method an integer to work with
     */
    private Vector<Integer> mStack = new Vector<>();
    private boolean mPressedRandom;

    public boolean getPressedRandom() {
        return mPressedRandom;
    }

    public void setPressedRandom(boolean _pressedRandom) {
        mPressedRandom = _pressedRandom;
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

        mBinding.StackActivityStackView.init(this);
        /*
         * The used Icons are NOT final - the toolbar was inserted for defining the right proportions and can be changed
         */

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

        if (v == mBinding.StackActivityButtonPush) {
            if (isInputValid()) {
                // parse the input to an int and store it into the stack (mStack), then let it be drawn by the StackView
                mStack.add((int) mBinding.StackActivityInputSlider.getValue());
                mBinding.StackActivityReturnValue.setText("");
                mBinding.StackActivityStackView.push((int) mBinding.StackActivityInputSlider.getValue());
                makeInVisible();
            }
        } else if (v == mBinding.StackActivityButtonPop) {
            if (!mStack.isEmpty()) {
                //delete the last element of the stack (mStack), then let it be (visually) removed by the StackView
                mBinding.StackActivityReturnValue.setText(String.format("%s", mStack.get(mStack.size() - 1).toString()));
                mStack.removeElementAt(mStack.size() - 1);
                mBinding.StackActivityStackView.prePop();
                makeInVisible();
                if (mStack.isEmpty()) {
                    showEmpty();
                }
            } else {
                Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
                mBinding.StackActivityReturnValue.setText("");
                showEmpty();
            }
        } else if(v == mBinding.StackActivityButtonPeek) {
            if(!mPressedRandom) {
                if (!mStack.isEmpty()) {
                    mBinding.StackActivityStackView.peek();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mBinding.StackActivityReturnValue.setText(String.format("%s",  mBinding.StackActivityStackView.mStackNumbers.get( mBinding.StackActivityStackView.mStackNumbers.size() - 1).toString()));
                        }
                    }, 500);
                } else {
                    Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
                    mBinding.StackActivityReturnValue.setText("");
                    showEmpty();
                }
            } else {
                Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
            }
        } else if (v == mBinding.StackActivityButtonSize) {
            if(!mPressedRandom) {
                mBinding.StackActivityReturnValue.setText("");
                if (!mStack.isEmpty()) {
                    mBinding.StackActivityStackView.size();
                } else {
                    mBinding.StackActivityReturnValue.setText("0");
                    showEmpty();
                }
            } else {
                Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
            }
        } else if (v == mBinding.StackActivityButtonEmpty) {
            if (!mStack.isEmpty()) {
                mBinding.StackActivityReturnValue.setText(R.string.All_Data_Activity_False);
            } else {
                mBinding.StackActivityReturnValue.setText(R.string.All_Data_Activity_True);
            }
        } else if (v == mBinding.StackActivityButtonClear) {
            mBinding.StackActivityReturnValue.setText("");

            if (!mStack.isEmpty()) {
                mBinding.StackActivityStackView.clear();
                mStack.clear();
            } else {
                Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
                showEmpty();
            }
        } else if (v == mBinding.StackActivityButtonRandom) {
            if (!mPressedRandom) {
                mPressedRandom = true;
                mBinding.StackActivityReturnValue.setText("");
                createRandomStack();
                makeInVisible();
                mBinding.StackActivityStackView.random(mStack);
            } else {
                Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * show the size of the stack
     */
    protected void showSize(){
        mBinding.StackActivityReturnValue.setText(String.format("%s", mBinding.StackActivityStackView.mStackNumbers.size()));
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
        if (mStack.size() > 15) {
            Toast.makeText(this, R.string.Overflow, Toast.LENGTH_SHORT).show();
            showFull();
            return false;
        } else if (mStack.size() == 15) {
            showFull();
        }
        return true;
    }

    /**
     * Creates a random stack with its size being min 4 and max 7
     */
    private void createRandomStack(){
        mStack.clear();
        Random r = new Random();
        int size = 4 + r.nextInt(6);

        for(int i = 0; i < size; i++){
            int x = -5 + r.nextInt(500);
            mStack.add(x);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}