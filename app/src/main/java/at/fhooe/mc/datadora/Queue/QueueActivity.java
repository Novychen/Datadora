package at.fhooe.mc.datadora.Queue;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.Animation;
import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.databinding.ActivityQueueBinding;

public class QueueActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "QueueActivity : ";
    private ActivityQueueBinding mBinding;

    private boolean mPressedRandom;
    private boolean mPressedDequeue;
    private Animation mAnimation;


    public boolean getPressedRandom() {
        return mPressedRandom;
    }

    public void setPressedRandom(boolean _pressedRandom) {
        mPressedRandom = _pressedRandom;
    }

    public boolean gePressedDequeue() {
        return mPressedDequeue;
    }

    public void setPressedDequeue(boolean _pressedDequeue) {
        mPressedDequeue = _pressedDequeue;
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivityQueueBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        mBinding.QueueActivityQueueView.setActivity(this);

        View layout = mBinding.QueueActivity;
        mAnimation = new Animation(layout, getIntent(), this);

        // set up the slider
        Slider slider = mBinding.QueueActivityInputSlider;
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
                mBinding.QueueActivityInputValue.setText(String.valueOf((int) value));
            }
        });

       setUpToolbar();

        // set FlowIcon & Text invisible
        makeInVisible();

        mBinding.QueueActivityButtonDequeue.setOnClickListener(this);
        mBinding.QueueActivityButtonEnqueue.setOnClickListener(this);
        mBinding.QueueActivityButtonPeek.setOnClickListener(this);
        mBinding.QueueActivityButtonSize.setOnClickListener(this);
        mBinding.QueueActivityButtonEmpty.setOnClickListener(this);
        mBinding.QueueActivityButtonClear.setOnClickListener(this);
        mBinding.QueueActivityButtonRandom.setOnClickListener(this);
    }

    private void setUpToolbar() {
        final Toolbar myToolbar = mBinding.QueueActivityToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.All_Data_Activity_Queue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        if (!mPressedDequeue && !mPressedRandom) {
            if (v == mBinding.QueueActivityButtonEnqueue) { enqueue();
            } else if (v == mBinding.QueueActivityButtonDequeue) { dequeue();
            } else if(v == mBinding.QueueActivityButtonPeek) { peek();
            } else if (v == mBinding.QueueActivityButtonSize) { size();
            } else if (v == mBinding.QueueActivityButtonEmpty) { isEmpty();
            } else if (v == mBinding.QueueActivityButtonClear) { clear();
            } else if (v == mBinding.QueueActivityButtonRandom) {
                mPressedRandom = true;
                random();
            }
        } else {
            Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * show the size of the queue
     */
    protected void showSize(){
        mBinding.QueueActivityReturnValue.setText(String.format("%s", mBinding.QueueActivityQueueView.getQueueNumbers().size()));
    }

    /**
     * show that the queue is currently empty
     */
    protected void showEmpty() {
        mBinding.QueueActivityFlowIcon.setVisibility(View.VISIBLE);
        mBinding.QueueActivityFlowText.setVisibility(View.VISIBLE);
        mBinding.QueueActivityFlowIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear, this.getTheme()));
        mBinding.QueueActivityFlowText.setText(R.string.Queue_Activity_Empty);
    }

    /**
     * Makes the ImageView and the TextView used for displaying R.string.Queue_Activity_Empty and R.string.Queue_Activity_Full invisible
     */
    protected void makeInVisible(){
        mBinding.QueueActivityFlowIcon.setVisibility(View.INVISIBLE);
        mBinding.QueueActivityFlowText.setVisibility(View.INVISIBLE);
    }

    /**
     * show that the queue is currently full
     */
    private void showFull() {
        mBinding.QueueActivityFlowIcon.setVisibility(View.VISIBLE);
        mBinding.QueueActivityFlowText.setVisibility(View.VISIBLE);
        mBinding.QueueActivityFlowIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, this.getTheme()));
        mBinding.QueueActivityFlowText.setText(R.string.Queue_Activity_Full);
    }

    /**
     * This method checks if the given user input is correct.
     * If the queue is bigger than a certain size then the user cannot push anymore as the queue is full
     *
     * @return true if the input is valid, false if its not
     */
    private boolean isInputValid() {
        if (mBinding.QueueActivityQueueView.getQueueNumbers().size() > 14) {
            Toast.makeText(this, R.string.Overflow, Toast.LENGTH_SHORT).show();
            showFull();
            return false;
        } else if (mBinding.QueueActivityQueueView.getQueueNumbers().size() == 14) {
            showFull();
        }
        return true;
    }


    /**
     * This method handles the operation enqueue
     */
    private void enqueue(){
        if (isInputValid()) {
            // parse the input to an int and store it into the Queue (mQueue), then let it be drawn by the QueueView
            mBinding.QueueActivityReturnValue.setText("");
            mBinding.QueueActivityQueueView.enqueue((int) mBinding.QueueActivityInputSlider.getValue());
            makeInVisible();
        }
    }


    /**
     * This method handles the operation dequeue
     */
    private void dequeue() {
        if (!mBinding.QueueActivityQueueView.getQueueNumbers().isEmpty()) {
            mPressedDequeue = true;
            //delete the last element of the Queue (mQueue), then let it be (visually) removed by the QueueView
            mBinding.QueueActivityReturnValue.setText(String.format("%s",
                    mBinding.QueueActivityQueueView.getQueueNumbers().get(0)));

            mBinding.QueueActivityQueueView.dequeue();
            makeInVisible();
            if (mBinding.QueueActivityQueueView.getQueueNumbers().isEmpty()) {
                showEmpty();
            }
        } else {
            Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
            mBinding.QueueActivityReturnValue.setText("");
            showEmpty();
        }
    }

    /**
     * This method handles the operation peek
     */
    private void peek(){
        if (!mBinding.QueueActivityQueueView.getQueueNumbers().isEmpty()) {
            mBinding.QueueActivityQueueView.peek();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mBinding.QueueActivityReturnValue.setText(String.format("%s",  mBinding.QueueActivityQueueView.getQueueNumbers().get(0).toString()));
                }
            }, 500);
        } else {
            Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
            mBinding.QueueActivityReturnValue.setText("");
            showEmpty();
        }
    }

    /**
     * This method handles the operation size
     */
    private void size(){
        mBinding.QueueActivityReturnValue.setText("");
        if (!mBinding.QueueActivityQueueView.getQueueNumbers().isEmpty()) {
            showSize();
        } else {
            mBinding.QueueActivityReturnValue.setText("0");
            showEmpty();
        }
    }

    /**
     * This method handles the operation isEmpty
     */
    private void isEmpty(){
        if (!mBinding.QueueActivityQueueView.getQueueNumbers().isEmpty()) {
            mBinding.QueueActivityReturnValue.setText(R.string.All_Data_Activity_False);
        } else {
            mBinding.QueueActivityReturnValue.setText(R.string.All_Data_Activity_True);
        }
    }

    /**
     * This method handles the operation clear
     */
    private void clear(){
        mBinding.QueueActivityReturnValue.setText("");
        if (!mBinding.QueueActivityQueueView.getQueueNumbers().isEmpty()) {
            mBinding.QueueActivityQueueView.clear();
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
        mBinding.QueueActivityReturnValue.setText("");
        makeInVisible();
        mBinding.QueueActivityQueueView.random(createRandomQueue());
    }

    /**
     * Creates a random queue with its size being min 4 and max 7
     */
    private Vector<Integer> createRandomQueue(){
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