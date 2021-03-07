package at.fhooe.mc.datadora.LinkedList.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.LinkedListActivity;
import at.fhooe.mc.datadora.R;

public class LinkedListAddFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "LLAddFragment :: ";

    private LinkedListActivity mActivity;
    private Slider mSlider;
    private Button mInsert;
    private Vector<Integer> mLinkedList;

    private boolean mPointer;

    public boolean isPointer() {
        return mPointer;
    }

    public void setPointer(boolean _pointer) {
        mPointer = _pointer;
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.fragment_linked_list_add, _container, false);
        setUpOnClickListeners(view);
        mActivity = (LinkedListActivity) getActivity();

        mSlider = view.findViewById(R.id.LinkedList_Fragment_Add_Slider);
        setUpSlider();
        mSlider.setVisibility(View.INVISIBLE);

        mInsert = view.findViewById(R.id.LinkedList_Fragment_Add_At);

        mLinkedList = mActivity.getLinkedList();
        preparePositionSlider();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        preparePositionSlider();
    }

    private void setUpOnClickListeners(View _view) {

        Button b = _view.findViewById(R.id.LinkedList_Fragment_Append);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Prepend);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Add_At);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View _view) {
        mActivity.getBinding().LinkedListActivityReturnValue.setText("");

        if(mActivity.isInputValid()) {
            if (_view.getId() == R.id.LinkedList_Fragment_Prepend) {
                prepend();
            } else if (_view.getId() == R.id.LinkedList_Fragment_Append) {
                append();
            }  else if (_view.getId() == R.id.LinkedList_Fragment_Add_At) {
                insertAt();
            }
            preparePositionSlider();
        }
    }

    private void prepend(){
        int value = (int) mActivity.getBinding().LinkedListActivityInputSlider.getValue();
        mActivity.getLinkedList().add(0,value);
        if(mPointer) {
            mActivity.getBinding().LinkedListActivityViewPointer.prepend(value);
        } else {
            mActivity.getBinding().LinkedListActivityView.prepend(value);
        }
    }

    private void append(){
        int value = (int) mActivity.getBinding().LinkedListActivityInputSlider.getValue();
        mActivity.getLinkedList().add(value);
        if(mPointer) {
            mActivity.getBinding().LinkedListActivityViewPointer.append(value);
        } else {
            mActivity.getBinding().LinkedListActivityView.append(value);
        }
    }

    private void insertAt(){
        int value = (int) mActivity.getBinding().LinkedListActivityInputSlider.getValue();
        int pos = (int)  mSlider.getValue();
        mActivity.getLinkedList().add(value);
        if(mPointer) {
            mActivity.getBinding().LinkedListActivityViewPointer.insertAt(value, pos);
        } else {
            mActivity.getBinding().LinkedListActivityView.insertAt(value, pos);
        }
    }

    private void setUpSlider() {
        mSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);    // converting the float value to an int value
            }
        });
        mSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                String s = getString(R.string.LinkedList_Activity_Add_At) + " " + (int) mSlider.getValue();
                mInsert.setText(s);
            }
        });
    }

    /**
     * prepares the position slider of the operations add.
     *
     * If the linked list is not empty the slider will be shown and its range will be updated according to the size of the list.
     * If the linked list is empty the slider will be hidden and a text field displaying "0" will be shown
     *
     */
    public void preparePositionSlider(){

        String s = getString(R.string.LinkedList_Activity_Add_At);

        if (mLinkedList.size() == 0) {
            mSlider.setVisibility(View.INVISIBLE);
            s = s + " 0" ;
            mInsert.setText(s);
        } else if (mLinkedList.size() == 1){
            s = s + " " + (int) mSlider.getValue();
            mInsert.setText(s);
            mSlider.setVisibility(View.VISIBLE);
            mSlider.setValueFrom(0);
            mSlider.setStepSize(1);
            mSlider.setValueTo(mLinkedList.size());
        } else if (mLinkedList.size() == 2) {
            mSlider.setValueTo(mLinkedList.size());
        } else {
            s = s + " " + (int) mSlider.getValue();
            mInsert.setText(s);
            mSlider.setVisibility(View.VISIBLE);
            mSlider.setValueFrom(0);
            mSlider.setStepSize(1);
            mSlider.setValueTo(mLinkedList.size() - 1);
        }
    }

}