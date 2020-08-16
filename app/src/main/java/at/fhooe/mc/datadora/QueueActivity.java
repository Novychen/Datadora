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

import at.fhooe.mc.datadora.databinding.ActivityQueueBinding;

public class QueueActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "QueueActivity : ";
    private ActivityQueueBinding mBinding;

    //TODO: 2x same data -> Queue!!
    //TODO: Animation better
    //TODO: Queue size (?) fine tuning
    //TODO: Styles, Themes, ...!!
    //TODO: Resize - animation (?) -> Animation clear -> too ugly when many elements
    //TODO: ENUM for operations (?)
    //TODO: Blocked UI while animation -> thats ok?

    /* Stores the integer values, that the user put it - is only for testing purposes,
     * feel free to change the way this data is stored - just remember that you have to
     * give the QueueView.push method an integer to work with
     */
    private Vector<Integer> mQueue = new Vector<>();
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
        mBinding = ActivityQueueBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        mBinding.QueueActivityQueueView.init(this);
        /*
         * The used Icons are NOT final - the toolbar was inserted for defining the right proportions and can be changed
         */

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

        // setup Toolbar
        Toolbar myToolbar = mBinding.QueueActivityToolbar;
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        if (v == mBinding.QueueActivityButtonEnqueue) {
            if (isInputValid()) {
                // parse the input to an int and store it into the Queue (mQueue), then let it be drawn by the QueueView
                mQueue.add((int) mBinding.QueueActivityInputSlider.getValue());
                mBinding.QueueActivityReturnValue.setText("");
                mBinding.QueueActivityQueueView.enqueue((int) mBinding.QueueActivityInputSlider.getValue());
                makeInVisible();
            }
        } else if (v == mBinding.QueueActivityButtonDequeue) {
            if (!mQueue.isEmpty()) {
                //delete the last element of the Queue (mQueue), then let it be (visually) removed by the QueueView
                mBinding.QueueActivityReturnValue.setText(String.format("%s", mQueue.get(0)));
                mQueue.removeElementAt(0);
                mBinding.QueueActivityQueueView.preDequeue();
                makeInVisible();
                if (mQueue.isEmpty()) {
                    showEmpty();
                }
            } else {
                Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
                mBinding.QueueActivityReturnValue.setText("");
                showEmpty();
            }
        } else if(v == mBinding.QueueActivityButtonPeek) {
            if(!mPressedRandom) {
                if (!mQueue.isEmpty()) {
                    mBinding.QueueActivityQueueView.peek();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mBinding.QueueActivityReturnValue.setText(String.format("%s",  mBinding.QueueActivityQueueView.mQueueNumbers.get(0).toString()));
                        }
                    }, 500);
                } else {
                    Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
                    mBinding.QueueActivityReturnValue.setText("");
                    showEmpty();
                }
            } else {
                Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
            }
        } else if (v == mBinding.QueueActivityButtonSize) {
            if(!mPressedRandom) {
                mBinding.QueueActivityReturnValue.setText("");
                if (!mQueue.isEmpty()) {
                    mBinding.QueueActivityQueueView.size();
                } else {
                    mBinding.QueueActivityReturnValue.setText("0");
                    showEmpty();
                }
            } else {
                Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
            }
        } else if (v == mBinding.QueueActivityButtonEmpty) {
            if (!mQueue.isEmpty()) {
                mBinding.QueueActivityReturnValue.setText(R.string.All_Data_Activity_False);
            } else {
                mBinding.QueueActivityReturnValue.setText(R.string.All_Data_Activity_True);
            }
        } else if (v == mBinding.QueueActivityButtonClear) {
            mBinding.QueueActivityReturnValue.setText("");

            if (!mQueue.isEmpty()) {
                mBinding.QueueActivityQueueView.clear();
                mQueue.clear();
            } else {
                Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
                showEmpty();
            }
        } else if (v == mBinding.QueueActivityButtonRandom) {
            if (!mPressedRandom) {
                mPressedRandom = true;
                mBinding.QueueActivityReturnValue.setText("");
                createRandomQueue();
                makeInVisible();
                mBinding.QueueActivityQueueView.random(mQueue);
            } else {
                Toast.makeText(this, R.string.All_Data_Activity_Text_Animation, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * show the size of the queue
     */
    protected void showSize(){
        mBinding.QueueActivityReturnValue.setText(String.format("%s", mBinding.QueueActivityQueueView.mQueueNumbers.size()));
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
        if (mQueue.size() > 15) {
            Toast.makeText(this, R.string.Overflow, Toast.LENGTH_SHORT).show();
            showFull();
            return false;
        } else if (mQueue.size() == 15) {
            showFull();
        }
        return true;
    }

    /**
     * Creates a random queue with its size being min 4 and max 7
     */
    private void createRandomQueue(){
        mQueue.clear();
        Random r = new Random();
        int size = 4 + r.nextInt(6);

        for(int i = 0; i < size; i++){
            int x = -5 + r.nextInt(500);
            mQueue.add(x);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}