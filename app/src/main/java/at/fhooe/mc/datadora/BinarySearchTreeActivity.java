package at.fhooe.mc.datadora;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

import at.fhooe.mc.datadora.databinding.ActivityBinarySearchTreeBinding;


public class BinarySearchTreeActivity extends AppCompatActivity implements View.OnClickListener{
    private static String TAG = "BSTActivity :: ";
    private TextView mTextView;
    private Vector<Integer> mBST = new Vector<>();
    private ActivityBinarySearchTreeBinding mBinding;


    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivityBinarySearchTreeBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        // array eingeben sachen speichern
        // setup Toolbar
        Toolbar myToolbar = mBinding.BTSActivityToolbar;
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Enables Always-on
        mBinding.activityBSTAdd.setOnClickListener(this);
        mBinding.activityBSTClear.setOnClickListener(this);
        mBinding.activityBSTMax.setOnClickListener(this);
        mBinding.activityBSTMin.setOnClickListener(this);
        mBinding.activityBSTRandom.setOnClickListener(this);
        mBinding.activityBSTRemove.setOnClickListener(this);
        mBinding.activityBSTSize.setOnClickListener(this);
        mBinding.activityBSTVector.setOnClickListener(this);

        add(1);
        add(4);
        add(7);

       }
    public int getSize(){return mBST.size();}
    public void addRandom() {
        int i = (int) (Math.random() * 10);
        mBinding.activityBSTTitelText.setText("Random");
        mBinding.ActivityBSTValueText.setText(String.format("%s", i));
        mBST.add(i);
    }
    private boolean hasLeftChild(int _position){
        return mBST.elementAt(_position*2) != null;
    }
    private boolean hasRightChild(int _position){
        return mBST.elementAt(_position*2+1) != null;
    }
    private boolean hasChild(int _position) {
        return hasLeftChild(_position)|| hasRightChild(_position);
    }
    private boolean hasParent(int _position){
        if (_position< 4){
            return false;
        }
        if (_position % 2 == 0) {
            return mBST.elementAt(_position/2) != null;
        } else {
            return mBST.elementAt(_position/2 - 1) != null;
        }
    }
    private int getLeftChild(int _position){
        if (hasLeftChild(_position)){
            return mBST.elementAt(_position*2);
        }
        return Integer.MIN_VALUE;
    }
    private int getRightChild(int _position){
        if (hasRightChild(_position)){
            return mBST.elementAt(_position*2+1);
        }
        return Integer.MIN_VALUE;
    }
    private int getParent(int _position){
        if(!hasParent(_position)){
            return Integer.MIN_VALUE;
        }
        if(hasParent(_position)) {
            if (_position % 2 == 0) {
                return mBST.elementAt(_position/2);
            } else {
                return mBST.elementAt(_position/2 - 1);
            }
        }return Integer.MIN_VALUE;
    }

    //adds content;
    public void add(int _add){
        Log.e(TAG,"Adding"+ _add);
        if(mBST.size() == 0){
            Log.e(TAG,"Sizing " + mBST.size());
            mBST.add(_add);
            return;
        }
        Log.e(TAG,"Adding bevor ");
        int i = 0;
        while (i < mBST.size()) {
            if (mBST.elementAt(i) == null) {
                mBST.insertElementAt(i,_add);
                return;
            }
            if (mBST.elementAt(i) > _add) {
                i = i*2;
            }
            if (mBST.elementAt(i) < _add) {
                i = i*2 + 1;
            }
            Log.e(TAG,"i=" + i);
        }
        Log.e(TAG,"Adding after ");

        return;
    }
    // fertrig machen
    public boolean remove(int _rem){
        if (!mBST.contains(_rem)){
            // print: object doesn't exist
            return false;
        }
        int i = 0;
        while (i < mBST.size() && mBST.elementAt(i) != _rem){
            if(_rem < mBST.elementAt(i) ){
                i = i * 2 ;
            }
            if(_rem > mBST.elementAt(i) ){
                i = i * 2 +1;
            }
        }
        if (!hasChild(i)){
            mBST.removeElementAt(i);
            return true;
        }




        return true;
    }

    private int getMin(){
        mBinding.activityBSTTitelText.setText("min");
        if (mBST.size()==0){
            mBinding.ActivityBSTValueText.setText("-");
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0 ;i< mBST.size(); i++){
            if(mBST.elementAt(i) < min){
                min = mBST.elementAt(i);
            }
        }

        mBinding.ActivityBSTValueText.setText( String.format("%s", min));
        return 0;
    }
    private int getMax(){
        mBinding.activityBSTTitelText.setText("max");
        if (mBST.size()==0){
            mBinding.ActivityBSTValueText.setText("-");
            return 0;
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0 ;i< mBST.size(); i++){
            if(mBST.elementAt(i) > max){
                max = mBST.elementAt(i);
            }
        }
        mBinding.ActivityBSTValueText.setText( String.format("%s", max));
        return 0;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == mBinding.activityBSTSize){
            Log.i(TAG, "Size "+  mBST.size());
            mBinding.activityBSTTitelText.setText("Size");
            mBinding.ActivityBSTValueText.setText( String.format("%s", mBST.size()));
        }
        if(view == mBinding.activityBSTAdd){
            Log.i(TAG, "Add");
            //add;
        }
        if(view == mBinding.activityBSTRandom){
            Log.i(TAG, "Random");
            addRandom();
        }
        if(view == mBinding.activityBSTMax){
            Log.i(TAG, "Max");
            getMax();
        }
        if(view == mBinding.activityBSTMin){
            Log.i(TAG, "Min");
            getMin();
        }
        if(view == mBinding.activityBSTRemove){
            Log.i(TAG, "Remove");
            mBinding.activityBSTTitelText.setText("Remove Last");
            if (!mBST.isEmpty()){
                mBinding.ActivityBSTValueText.setText( String.format("%s", mBST.lastElement()));
                mBST.remove(mBST.size()-1);
            }else{
                Toast.makeText(this, R.string.Stack_Activity_Text_Empty, Toast.LENGTH_SHORT).show();
                mBinding.ActivityBSTValueText.setText( "-");}

        }
        if(view == mBinding.activityBSTClear){
            Log.i(TAG, "Clear");
            mBST.clear();
        }
        if(view == mBinding.activityBSTVector){
            Log.i(TAG, "Inorder :: ");
            mBinding.ActivityBSTOderText.setText(mBST.toString()+ ", capacity : " +   mBST.capacity());

        }
    }





}
