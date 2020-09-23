package at.fhooe.mc.datadora;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.LinkedList.LinkedListActivity;
import at.fhooe.mc.datadora.Queue.QueueActivity;
import at.fhooe.mc.datadora.Stack.StackActivity;
import at.fhooe.mc.datadora.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mBinding;

    //TODO: Sync all animations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        // setup Toolbar
        Toolbar myToolbar = mBinding.MainActivityToolbar;
        setSupportActionBar(myToolbar);

        mBinding.MainActivityButtonStack.setOnClickListener(this);
        mBinding.MainActivityButtonQueue.setOnClickListener(this);
        mBinding.MainActivityButtonLinkedList.setOnClickListener(this);
        mBinding.mainActivityButtonBST.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.MainActivityButtonStack) {
            Intent i = new Intent(this, StackActivity.class);
            startActivity(i);
        } else if (v == mBinding.MainActivityButtonQueue) {
            Intent i = new Intent(this, QueueActivity.class);
            startActivity(i);
        } else if (v == mBinding.MainActivityButtonLinkedList) {
            Intent i = new Intent(this, LinkedListActivity.class);
            startActivity(i);
        }else if(v == mBinding.mainActivityButtonBST){
            Intent i = new Intent(this, BinarySearchTreeActivity.class);
            startActivity(i);
        }
    }
}
