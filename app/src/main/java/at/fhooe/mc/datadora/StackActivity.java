package at.fhooe.mc.datadora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

import at.fhooe.mc.datadora.databinding.ActivityStackBinding;

public class StackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StackActivity :: ";
    private ActivityStackBinding mBinding;

    /* Stores the integer values, that the user put it - is only for testing purposed,
    * feel free to change the way this data is stored - just remember that you have to
    * give the StackView.push method an integer to work with
    */
    private Vector<Integer> mStack = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityStackBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        /**
         * The used Icons are NOT final - the toolbar was inserted for defining the right proportions and can be changed
         */

        // setup Toolbar
        Toolbar myToolbar = mBinding.StackActivityToolbar;
        setSupportActionBar(myToolbar);

        // set FlowIcon & Text invisible
        mBinding.StackActivityFlowIcon.setVisibility(View.INVISIBLE);
        mBinding.StackActivityFlowText.setVisibility(View.INVISIBLE);

        mBinding.StackActivityButtonPop.setOnClickListener(this);
        mBinding.StackActivityButtonPush.setOnClickListener(this);

        // Listens for the keyboard and checks if "Done" or "Return" was pressed
        mBinding.StackActivityInputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (isInputValid()) {
                        mStack.add(Integer.parseInt(mBinding.StackActivityInputField.getText().toString()));
                        mBinding.StackActivityStackView.push(Integer.parseInt(mBinding.StackActivityInputField.getText().toString()));
                        mBinding.StackActivityInputField.getText().clear();
                    }
                }
                return false;
            }
        });

    }

    /**
     * show that the stack is currently empty
     */
    private void showEmpty() {
        mBinding.StackActivityFlowIcon.setVisibility(View.VISIBLE);
        mBinding.StackActivityFlowText.setVisibility(View.VISIBLE);

        mBinding.StackActivityFlowIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_24, this.getTheme()));
        mBinding.StackActivityFlowText.setText(R.string.Stack_Activity_Empty);
    }

    /**
     * show that the stack is currently full
     */
    private void showFull() {
        mBinding.StackActivityFlowIcon.setVisibility(View.VISIBLE);
        mBinding.StackActivityFlowText.setVisibility(View.VISIBLE);

        mBinding.StackActivityFlowIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_24, this.getTheme()));
        mBinding.StackActivityFlowText.setText(R.string.Stack_Activity_Full);
    }

    /**
     * This method checks if the given user input is correct. In this case the checked cases are
     * is the input empty
     * is the input higher than a specific value (currently Integer.MAXVALUE);
     * is the input lower than a specific value (currently - Integer.MAXVALUE);
     * If one of these cases occurs a toast is displayed to the user explaining what went wrong
     *
     * @return true if the input is valid, false if its not
     */
    private boolean isInputValid() {
        int inputLimit = 99999;
        if (mBinding.StackActivityInputField.getText().toString().equals("")) {
            Toast.makeText(this, R.string.EmptyNumber, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mStack.size() > 20) {
            Toast.makeText(this, R.string.Overflow, Toast.LENGTH_SHORT).show();
            showFull();
            return false;
        } else if (mStack.size() == 20) {
            showFull();
        }
        try {
            int value = Integer.parseInt(mBinding.StackActivityInputField.getText().toString());
            if (value < inputLimit && value > -inputLimit) {
                mBinding.StackActivityFlowIcon.setVisibility(View.INVISIBLE);
                mBinding.StackActivityFlowText.setVisibility(View.INVISIBLE);
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.NumberInvalid, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == mBinding.StackActivityButtonPush) {
            if (isInputValid()) {
                // parse the input to an int and store it into the stack (mStack), then let it be drawn by the StackView
                mStack.add(Integer.parseInt(mBinding.StackActivityInputField.getText().toString()));
                mBinding.StackActivityStackView.push(Integer.parseInt(mBinding.StackActivityInputField.getText().toString()));
                mBinding.StackActivityInputField.getText().clear();
            } else {
                Log.i(TAG, ":: onClick :: error");
            }
        } else if (v == mBinding.StackActivityButtonPop) {
            if (!mStack.isEmpty()) {
                //delete the last element of the stack (mStack), then let it be (visually) removed by the StackView
                mBinding.StackActivityReturnValue.setText(String.format("%s", mStack.get(mStack.size() - 1).toString()));
                mStack.removeElementAt(mStack.size() - 1);
                mBinding.StackActivityStackView.pop();
                mBinding.StackActivityFlowIcon.setVisibility(View.INVISIBLE);
                mBinding.StackActivityFlowText.setVisibility(View.INVISIBLE);
                if (mStack.isEmpty()) {
                    showEmpty();
                }
            } else {
                Toast.makeText(this, R.string.Underflow, Toast.LENGTH_SHORT).show();
                mBinding.StackActivityReturnValue.setText("");
                showEmpty();
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }


}