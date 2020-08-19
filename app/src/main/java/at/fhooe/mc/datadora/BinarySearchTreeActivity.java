package at.fhooe.mc.datadora;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Vector;

import at.fhooe.mc.datadora.databinding.ActivityBinarySearchTreeBinding;
public class BinarySearchTreeActivity extends AppCompatActivity {

    private TextView mTextView;
    private Vector<Integer> mBST = new Vector<>();
    private ActivityBinarySearchTreeBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary_search_tree);
       /* View view = mBinding.getRoot();
        setContentView(view);
        mBinding = ActivityBinarySearchTreeBinding.inflate(getLayoutInflater());

*/


        // array eingeben sachen speichern
        // setup Toolbar
       /* Toolbar myToolbar = mBinding.BTSActivityToolbar;
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        // Enables Always-on

       }
    public int getSize(){return mBST.size();}
    public void addRandom(){add((int)Math.random());}
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
        if(mBST.size() == 0){
            mBST.add(_add);
            return;
        }
        int i = 0;
        while(i < mBST.size()){
            if(mBST.elementAt(i) == null){
                mBST.insertElementAt(_add,i);
                return;
            }
            if (mBST.elementAt(i) > _add){
                i = i*2;
            }
            if (mBST.elementAt(i) < _add){
                i = i*2 + 1;
            }
        }

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
}