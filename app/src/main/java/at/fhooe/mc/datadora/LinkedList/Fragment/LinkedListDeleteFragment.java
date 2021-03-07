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

import java.util.Vector;

import at.fhooe.mc.datadora.LinkedList.LinkedListActivity;
import at.fhooe.mc.datadora.R;

public class LinkedListDeleteFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LLDeleteFragment :: ";

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
        View view = _inflater.inflate(R.layout.fragment_linked_list_delete, _container, false);
        setUpOnClickListeners(view);
        mActivity = (LinkedListActivity) getActivity();

        mSlider = view.findViewById(R.id.LinkedList_Fragment_Delete_Slider);
        mSlider.setVisibility(View.INVISIBLE);
        setUpSlider();

        mInsert = view.findViewById(R.id.LinkedList_Fragment_Delete_At);

        mLinkedList = mActivity.getLinkedList();
        preparePositionSlider();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        preparePositionSlider();
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
                String s = getString(R.string.LinkedList_Activity_Delete_At) + " " + (int) mSlider.getValue();
                mInsert.setText(s);
            }
        });
    }

    private void setUpOnClickListeners(View _view) {

        Button b = _view.findViewById(R.id.LinkedList_Fragment_Clear);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Delete_First);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Delete_Last);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.LinkedList_Fragment_Delete_At);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View _view) {
        mActivity.getBinding().LinkedListActivityReturnValue.setText("");

        if (_view.getId() == R.id.LinkedList_Fragment_Clear) {
            clear();
        } if (_view.getId() == R.id.LinkedList_Fragment_Delete_First) {
            deleteFirst();
        } else if (_view.getId() == R.id.LinkedList_Fragment_Delete_Last) {
            deleteLast();
        } else if (_view.getId() == R.id.LinkedList_Fragment_Delete_At) {
            deleteAt();
        }
        preparePositionSlider();

    }

    private void clear(){
        if(!mLinkedList.isEmpty()){
            if(mPointer) {
                mActivity.getBinding().LinkedListActivityViewPointer.clear();
            } else {
                mActivity.getBinding().LinkedListActivityView.clear();
            }
            mLinkedList.clear();
        } else {
            mActivity.isEmptyMessage();
        }
    }

    private void deleteFirst() {
        if(!mLinkedList.isEmpty()){
            if(mPointer) {
                mActivity.getBinding().LinkedListActivityViewPointer.deleteFirst();
            } else {
                mActivity.getBinding().LinkedListActivityView.deleteFirst();
            }
            mLinkedList.remove(0);

        } else {
            mActivity.isEmptyMessage();
        }
    }

    private void deleteLast() {
        if(!mLinkedList.isEmpty()){
            if(mPointer) {
                mActivity.getBinding().LinkedListActivityViewPointer.deleteLast();
            } else {
                mActivity.getBinding().LinkedListActivityView.deleteLast();
            }
            mLinkedList.remove(mLinkedList.size() - 1);

        } else {
            mActivity.isEmptyMessage();
        }
    }

    private void deleteAt() {
        if (!mLinkedList.isEmpty() && mLinkedList.size() > 1) {
            int pos = (int) mSlider.getValue();
            if(mPointer) {
                mActivity.getBinding().LinkedListActivityViewPointer.deleteAt(pos);
            } else {
                mActivity.getBinding().LinkedListActivityView.deleteAt(pos);
            }
            mLinkedList.remove(pos);
        } else if (mLinkedList.size() == 1) {
            clear();
        } else {
            mActivity.isEmptyMessage();
        }
    }

    /**
     * prepares the position slider of the operations delete.
     *
     * If the linked list is not empty the slider will be shown and its range will be updated according to the size of the list.
     * If the linked list is empty the slider will be hidden and a text field displaying "0" will be shown
     *
     */
    public void preparePositionSlider(){

        String s = getString(R.string.LinkedList_Activity_Delete_At);

        if (mLinkedList.size() == 0) {
            mSlider.setVisibility(View.INVISIBLE);
            s = s + " 0";
            mInsert.setText(s);
        } else if (mLinkedList.size() == 1){
            mSlider.setVisibility(View.INVISIBLE);

            mInsert.setVisibility(View.VISIBLE);
        } else if (mLinkedList.size() == 2) {
            s = s + " " + (int) mSlider.getValue();
            mInsert.setText(s);
            mSlider.setVisibility(View.VISIBLE);
            mSlider.setValueFrom(0);
            mSlider.setStepSize(1);
            if(mSlider.getValue() == mLinkedList.size()) {
                mSlider.setValue(mLinkedList.size() - 1);
            }
            mSlider.setValueTo(mLinkedList.size() - 1);
        } else {
            s = s + " " + (int) mSlider.getValue();
            mInsert.setText(s);
            mSlider.setVisibility(View.VISIBLE);
            mSlider.setValueFrom(0);
            mSlider.setStepSize(1);
            if(mSlider.getValue() == mLinkedList.size()) {
                mSlider.setValue(mLinkedList.size() - 1);
            }
            mSlider.setValueTo(mLinkedList.size() - 1);
        }
    }

}