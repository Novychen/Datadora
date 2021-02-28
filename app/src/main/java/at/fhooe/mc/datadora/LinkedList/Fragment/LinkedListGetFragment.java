package at.fhooe.mc.datadora.LinkedList.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;

import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.LinkedListActivity;
import at.fhooe.mc.datadora.R;

public class LinkedListGetFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "LinkedListGetFragment :: ";

    LinkedListActivity mActivity;
    Slider mSlider;
    TextView mZero;
    Vector<Integer> mLinkedList;

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.fragment_linked_list_get, _container, false);
        setUpOnClickListeners(view);
        mActivity = (LinkedListActivity) getActivity();

        mSlider = view.findViewById(R.id.LinkedList_Fragment_Get_Slider);
        mSlider.setVisibility(View.INVISIBLE);

        mZero = view.findViewById(R.id.LinkedList_Fragment_Get_Zero);

        mLinkedList = mActivity.getLinkedList();
        preparePositionSlider();

        return view;
    }

    private void setUpOnClickListeners(View _view) {

        Button b = _view.findViewById(R.id.LinkedList_Fragment_Size);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Predec);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Succ);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Get_First);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Get_Last);
        b.setOnClickListener(this);

        ImageButton i = _view.findViewById(R.id.LinkedList_Fragment_Get_Button);
        i.setOnClickListener(this);
    }

    @Override
    public void onClick(View _view) {
        mActivity.getBinding().LinkedListActivityReturnValue.setText("");

        if(mActivity.isInputValid()) {
            if (_view.getId() == R.id.LinkedList_Fragment_Size) {
                getSize();
            } else if (_view.getId() == R.id.LinkedList_Fragment_Predec) {
                getPredecessor();
            } else if (_view.getId() == R.id.LinkedList_Fragment_Succ) {
                getSuccessor();
            } else if (_view.getId() == R.id.LinkedList_Fragment_Get_First) {
                getFirst();
            } else if (_view.getId() == R.id.LinkedList_Fragment_Get_Last) {
                getLast();
            } else if (_view.getId() == R.id.LinkedList_Fragment_Get_Button) {
                getAt();
            }
            preparePositionSlider();
        }
    }

    private void getSize(){
        if(!mLinkedList.isEmpty()){
            mActivity.getBinding().LinkedListActivityView.getSize();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mActivity.getBinding().LinkedListActivityReturnValue.setText(
                            String.format("%s",  mActivity.getBinding().LinkedListActivityView.getLinkedListNumbers().size()));
                }
            }, (600 * mLinkedList.size() - 1));

        } else {
            mActivity.isEmptyMessage();
        }
    }

    private void getPredecessor() {
        Toast.makeText(mActivity, R.string.LinkedList_Activity_Pre_Succ_Hint, Toast.LENGTH_SHORT).show();
        mActivity.getBinding().LinkedListActivityView.predecessor();
    }

    private void getSuccessor() {
        Toast.makeText(mActivity, R.string.LinkedList_Activity_Pre_Succ_Hint, Toast.LENGTH_SHORT).show();
        mActivity.getBinding().LinkedListActivityView.successor();
    }

    private void getFirst() {

        if(!mLinkedList.isEmpty()){
            mActivity.getBinding().LinkedListActivityView.getFirst();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mActivity.getBinding().LinkedListActivityReturnValue.setText(
                            String.format("%s", mActivity.getBinding().LinkedListActivityView.getLinkedListNumbers().get(0).toString()));
                }
            }, 500);

        } else {
            mActivity.isEmptyMessage();
        }
    }

    private void getLast() {

        if(!mLinkedList.isEmpty()){
            mActivity.getBinding().LinkedListActivityView.getLast();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mActivity.getBinding().LinkedListActivityReturnValue.setText(
                            String.format("%s", mActivity.getBinding().LinkedListActivityView.getLinkedListNumbers().get(
                                    mActivity.getBinding().LinkedListActivityView.getLinkedListNumbers().size() - 1).toString()));
                }
            }, 500);
        } else {
            mActivity.isEmptyMessage();
        }
    }


    private void getAt() {
        if(!mLinkedList.isEmpty()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int pos = (int) mSlider.getValue();
                    mActivity.getBinding().LinkedListActivityView.getAt(pos);
                    mActivity.getBinding().LinkedListActivityReturnValue.setText(
                            String.format("%s", mActivity.getBinding().LinkedListActivityView.getLinkedListNumbers().get(pos).toString()));
                }
            }, 500);

        } else {
            mActivity.isEmptyMessage();
        }
    }

    /**
     * prepares the position slider of the operations get.
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
            mSlider.setVisibility(View.INVISIBLE);
            mZero.setVisibility(View.GONE);

        } else if (mLinkedList.size() == 2) {

            mZero.setVisibility(View.GONE);
            mSlider.setVisibility(View.VISIBLE);
            mSlider.setValueFrom(0);
            mSlider.setStepSize(1);
            mSlider.setValueTo(mLinkedList.size() - 1);

        } else {
            mSlider.setVisibility(View.VISIBLE);
            mZero.setVisibility(View.GONE);
            mSlider.setValueFrom(0);
            mSlider.setStepSize(1);
            mSlider.setValueTo(mLinkedList.size() - 1);
        }
    }

}