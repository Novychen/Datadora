package at.fhooe.mc.datadora.LinkedList.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.LinkedListActivity;
import at.fhooe.mc.datadora.R;

public class LinkedListAddFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "LinkedListAddFragment :: ";

    LinkedListActivity mActivity;
    Slider mSlider;
    TextView mZero;
    Vector<Integer> mLinkedList;

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.fragment_linked_list_add, _container, false);
        setUpOnClickListeners(view);
        mActivity = (LinkedListActivity) getActivity();

        mSlider = view.findViewById(R.id.LinkedList_Fragment_Add_Slider);
        mSlider.setVisibility(View.INVISIBLE);

        mZero = view.findViewById(R.id.LinkedList_Fragment_Add_Zero);

        mLinkedList = mActivity.getLinkedList();
        preparePositionSlider();

        return view;
    }

    private void setUpOnClickListeners(View _view) {

        Button b = _view.findViewById(R.id.LinkedList_Fragment_Append);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Prepend);
        b.setOnClickListener(this);

        ImageButton i = _view.findViewById(R.id.LinkedList_Fragment_Add_Button);
        i.setOnClickListener(this);
    }

    @Override
    public void onClick(View _view) {
        mActivity.getBinding().LinkedListActivityReturnValue.setText("");

        if(mActivity.isInputValid()) {
            if (_view.getId() == R.id.LinkedList_Fragment_Prepend) {
                prepend();
            } else if (_view.getId() == R.id.LinkedList_Fragment_Append) {
                append();
            }  else if (_view.getId() == R.id.LinkedList_Fragment_Add_Button) {
                insertAt();
            }
            preparePositionSlider();
        }
    }

    private void prepend(){
        int value = (int) mActivity.getBinding().LinkedListActivityInputSlider.getValue();
        mActivity.getLinkedList().add(0,value);
        mActivity.getBinding().LinkedListActivityView.prepend(value);
    }

    private void append(){
        int value = (int) mActivity.getBinding().LinkedListActivityInputSlider.getValue();
        mActivity.getLinkedList().add(value);
        mActivity.getBinding().LinkedListActivityView.append(value);
    }

    private void insertAt(){
        int value = (int) mActivity.getBinding().LinkedListActivityInputSlider.getValue();
        int pos = (int)  mSlider.getValue();
        mActivity.getLinkedList().add(value);
        mActivity.getBinding().LinkedListActivityView.insertAt(value, pos);
    }


    /**
     * prepares the position slider of the operations add.
     *
     * If the linked list is not empty the slider will be shown and its range will be updated according to the size of the list.
     * If the linked list is empty the slider will be hidden and a text field displaying "0" will be shown
     *
     */
    private void preparePositionSlider(){

        if (mLinkedList.size() == 0) {
            mSlider.setVisibility(View.INVISIBLE);
            mZero.setVisibility(View.VISIBLE);
        } else if (mLinkedList.size() == 1){
            mZero.setVisibility(View.GONE);
            mSlider.setVisibility(View.VISIBLE);
            mSlider.setValueFrom(0);
            mSlider.setStepSize(1);
        } else if (mLinkedList.size() == 2) {
            mSlider.setValueTo(mLinkedList.size());
        } else {
            mZero.setVisibility(View.GONE);
            mSlider.setVisibility(View.VISIBLE);
            mSlider.setValueFrom(0);
            mSlider.setStepSize(1);
            mSlider.setValueTo(mLinkedList.size() - 1);
        }
    }

}