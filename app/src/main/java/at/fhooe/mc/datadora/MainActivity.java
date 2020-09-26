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
        getSupportActionBar().setTitle("");

        mBinding.MainActivityStackCard.setOnClickListener(this);
        mBinding.MainActivityQueueCard.setOnClickListener(this);
        mBinding.MainActivityListCard.setOnClickListener(this);
        mBinding.MainActivityTreeCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.MainActivityStackCard) {
            Intent i = new Intent(this, StackActivity.class);
            startActivity(i);
        } else if (v == mBinding.MainActivityQueueCard) {
            Intent i = new Intent(this, QueueActivity.class);
            startActivity(i);
        } else if (v == mBinding.MainActivityListCard) {
            Intent i = new Intent(this, LinkedListActivity.class);
            startActivity(i);
        } else if(v == mBinding.MainActivityTreeCard){
            Intent i = new Intent(this, BinarySearchTreeActivity.class);
            startActivity(i);
        }
    }
}
