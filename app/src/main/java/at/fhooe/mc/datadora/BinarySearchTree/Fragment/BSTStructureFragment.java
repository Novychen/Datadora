package at.fhooe.mc.datadora.BinarySearchTree.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.R;

public class BSTStructureFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "BSTStructureFragment ::";
    private BinarySearchTreeActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.fragment_bst_structure, _container, false);
        setUpOnClickListeners(view);
        mActivity = (BinarySearchTreeActivity) getActivity();
        return view;
    }

    private void setUpOnClickListeners(View _view) {

        Button b = _view.findViewById(R.id.BST_Fragment_Size);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Height);
        b.setOnClickListener(this);

        b = _view.findViewById(R.id.BST_Fragment_Depth);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View _view) {

        if (_view.getId() == R.id.BST_Fragment_Size) {
            size();
        } else if (_view.getId() == R.id.BST_Fragment_Height) {
            height();
        }  else if (_view.getId() == R.id.BST_Fragment_Depth) {
            depth();
        }
    }
    private void size() { }

    private void height() {
        mActivity.getBinding().BSTActivityView.height();
    }

    private void depth() {
        mActivity.getBinding().BSTActivityView.depth();
    }

}