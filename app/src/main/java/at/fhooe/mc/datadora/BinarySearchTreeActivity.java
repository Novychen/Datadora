package at.fhooe.mc.datadora;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Node;

import java.util.Vector;

import at.fhooe.mc.datadora.databinding.ActivityBinarySearchTreeBinding;


public class BinarySearchTreeActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "BSTActivity :: ";

    private TextView mTextView;
    private Vector<Integer> mBST = new Vector<>();
    private Vector<Integer> mBSTOrder = new Vector<>();
    private BSTNode root;
    public class BSTNode{
        BSTNode left;
        BSTNode right;
        int element;
        public BSTNode(){
            left = null;
            right = null;
            element = Integer.MIN_VALUE;
        }
        public BSTNode(int _element){
            left = null;
            right = null;
            element = _element;
        }

        public void setLeft(BSTNode left) {
            this.left = left;
        }

        public void setRight(BSTNode right) {
            this.right = right;
        }
    };


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
    }

 /*   private void init(int point, int count) {
        for (int i = point; i < count; i++) {
            mBST.add(-1);
        }
    }

    public int getSize() {
        return mBST.size();
    }*/

    public void addRandom() {
        int i = (int) (Math.random() * 100);
        mBinding.activityBSTTitelText.setText("Random");
        mBinding.ActivityBSTValueText.setText(String.format("%s", i));
        addNode(i);
    }

    private boolean hasLeftChild(int _position) {
        return mBST.elementAt(_position * 2) != null;
    }

    private boolean hasRightChild(int _position) {
        return mBST.elementAt(_position * 2 + 1) != null;
    }

    private boolean hasChild(int _position) {
        return hasLeftChild(_position) || hasRightChild(_position);
    }




    //adds content;
    //alt
  /*  public void addint(int _add) {
        if (mBST.contains(_add)) {
            return;
        }
        Log.e(TAG, "add ::" + _add);
        if (mBST.contains(_add)) {
            return;
        }
        System.out.println("size" + mBST.size());
        if (mBST.size() == 0) {
            mBST.add(_add);
            init(0, 4);
            return;
        }


        int size = mBST.size();
        int j = 1;
        while (j < mBST.size()) {
            System.out.println(j + " -element :: " + mBST.elementAt(j));
            if (mBST.elementAt(j) < 0) {
                mBST.add(-1);
                mBST.add(-1);
                mBST.removeElementAt(j);
                mBST.insertElementAt(_add, j);

                return;
            }
            if (mBST.elementAt(j) > _add) {
                j = j * 2;
                System.out.println("left" + j);
            } else if (mBST.elementAt(j) < _add) {
                j = j * 2 + 1;
                System.out.println("right" + j);
            }

        }
        Log.e(TAG, "Adding after ");

        return;
    }*/
    //neu
    public void addNode(int _add){
        buildTree(new BSTNode(_add));
        mBST.add(_add);
    }
    public void buildTree(BSTNode _node){
        if(root == null){
            root = _node;
            Log.e(TAG,"root");
        }else{

            BSTNode temp = root;
            while (temp != null){
                Log.e(TAG,"BuildTree :: " + temp.element + "node" + _node.element);
                if (_node.element > temp.element) {
                    temp = temp.right;
                    Log.i(TAG,"right");
                } else if (_node.element < temp.element) {
                    temp = temp.left;
                    Log.i(TAG,"left");}
            }
            temp = _node;
            Log.e(TAG,"BuildTree");
        }

    }

    // fertrig machen
    public void removeNode(int _element){
        if(mBST.contains(_element)){return;}
    }

    private int getMin() {
        mBinding.activityBSTTitelText.setText("min");
        if (mBST.size() == 0) {
            mBinding.ActivityBSTValueText.setText("-");
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < mBST.size(); i++) {
            if (mBST.elementAt(i) < min) {
                min = mBST.elementAt(i);
            }
        }

        mBinding.ActivityBSTValueText.setText(String.format("%s", min));
        return 0;
    }

    private int getMax() {
        mBinding.activityBSTTitelText.setText("max");
        if (mBST.size() == 0) {
            mBinding.ActivityBSTValueText.setText("-");
            return 0;
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < mBST.size(); i++) {
            if (mBST.elementAt(i) > max) {
                max = mBST.elementAt(i);
            }
        }
        mBinding.ActivityBSTValueText.setText(String.format("%s", max));
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
        if (view == mBinding.activityBSTSize) {
            Log.i(TAG, "Size " + mBST.size());
            fillOder();
            mBinding.activityBSTTitelText.setText("Size");
            mBinding.ActivityBSTValueText.setText(String.format("%s", mBSTOrder.size()));
        }
        if (view == mBinding.activityBSTAdd) {
            Log.i(TAG, "Add");
            //add;
        }
        if (view == mBinding.activityBSTRandom) {
            Log.i(TAG, "Random");
            addRandom();
        }
        if (view == mBinding.activityBSTMax) {
            Log.i(TAG, "Max");
            getMax();
        }
        if (view == mBinding.activityBSTMin) {
            Log.i(TAG, "Min");
            getMin();
        }
        if (view == mBinding.activityBSTRemove) {
            Log.i(TAG, "Remove");
            mBinding.activityBSTTitelText.setText("Remove Last");
            if (!mBST.isEmpty()) {
               // mBinding.ActivityBSTValueText.setText(String.format("%s", mBST.lastElement()));
               // mBST.remove(mBST.size() - 1);
            } else {
                Toast.makeText(this, R.string.Stack_Activity_Text_Empty, Toast.LENGTH_SHORT).show();
                mBinding.ActivityBSTValueText.setText("-");
            }

        }
        if (view == mBinding.activityBSTClear) {
            Log.i(TAG, "Clear");
            mBST.clear();
        }
        if (view == mBinding.activityBSTVector) {
            Log.i(TAG, "Inorder :: ");
            fillOder();
            mBinding.ActivityBSTOderText.setText(mBSTOrder.toString());


        }


    }

    public void fillOder() {
        mBSTOrder.clear();
        for (int i = 0; i < mBST.size(); i++) {
            if (mBST.elementAt(i) > -1) {
                mBSTOrder.add(mBST.elementAt(i));
            }
        }
    }


}
