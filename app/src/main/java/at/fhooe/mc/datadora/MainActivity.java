package at.fhooe.mc.datadora;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTree;
import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;
import at.fhooe.mc.datadora.LinkedList.LinkedListActivity;
import at.fhooe.mc.datadora.Queue.QueueActivity;
import at.fhooe.mc.datadora.Stack.StackActivity;
import at.fhooe.mc.datadora.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity : ";
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        // setup Toolbar
        Toolbar myToolbar = mBinding.MainActivityToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        mBinding.MainActivityStackCard.setOnClickListener(this);
        mBinding.MainActivityQueueCard.setOnClickListener(this);
        mBinding.MainActivityDoubleListCard.setOnClickListener(this);
        mBinding.MainActivityTreeCard.setOnClickListener(this);
        mBinding.MainActivityAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.MainActivityStackCard) {
            Intent i = new Intent(this, StackActivity.class);
            startActivity(i);
        } else if (v == mBinding.MainActivityQueueCard) {
            Intent i = new Intent(this, QueueActivity.class);
            startActivity(i);
        } else if (v == mBinding.MainActivityDoubleListCard) {
            Intent i = new Intent(this, LinkedListActivity.class);
            startActivity(i);
        } else if(v == mBinding.MainActivityTreeCard){
            Intent i = new Intent(this, BinarySearchTreeActivity.class);
            startActivity(i);
        } else if(v == mBinding.MainActivityAbout){
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
        }
    }
}
