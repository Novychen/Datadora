 package at.fhooe.mc.datadora;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTree;
import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTreeActivity;
import at.fhooe.mc.datadora.BinarySearchTree.BinaryTreeNode;
import at.fhooe.mc.datadora.LinkedList.LinkedListActivity;
import at.fhooe.mc.datadora.Queue.QueueActivity;
import at.fhooe.mc.datadora.Stack.StackActivity;
import at.fhooe.mc.datadora.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnScrollChangeListener {

    private static final String TAG = "MainActivity : ";
    public static final float LINKED_LIST_SINGLE = 1;
    public static final float LINKED_LIST_DOUBLE = 2;
    public static final String LINKED_LIST_TYPE = "LINKED_LIST_TYPE";

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
        mBinding.MainActivitySingleListCard.setOnClickListener(this);
        mBinding.MainActivityTreeCard.setOnClickListener(this);
        mBinding.MainActivityAbout.setOnClickListener(this);
        mBinding.scrollView3.setOnScrollChangeListener(this);
        mBinding.MainActivityGradiantAbove.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, _menu);
        return super.onCreateOptionsMenu(_menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == findViewById(R.id.menu_setting).getId()) {
            Toast.makeText(this, R.string.LinkedList_Activity_Toast_Feature,Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View _v) {

        int[] location = new int[2];
        _v.getLocationInWindow(location);
        int x = location[0] + _v.getWidth() / 2;
        int y = location[1] + _v.getHeight() / 4;

        if (_v == mBinding.MainActivityStackCard) {
            Intent i = new Intent(this, StackActivity.class);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_X, x);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_Y, y);
            startActivity(i);

        } else if (_v == mBinding.MainActivityQueueCard) {
            Intent i = new Intent(this, QueueActivity.class);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_X, x);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_Y, y);
            startActivity(i);

        } else if (_v == mBinding.MainActivitySingleListCard || _v == mBinding.MainActivityDoubleListCard) {
            Intent i = new Intent(this, LinkedListActivity.class);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_X, x);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_Y, y);
            if (_v == mBinding.MainActivitySingleListCard) {
                i.putExtra(LINKED_LIST_TYPE, LINKED_LIST_SINGLE);
            } else {
                i.putExtra(LINKED_LIST_TYPE, LINKED_LIST_DOUBLE);
            }
            startActivity(i);

        } else if(_v == mBinding.MainActivityTreeCard){
            Intent i = new Intent(this, BinarySearchTreeActivity.class);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_X, x);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_Y, y);
            startActivity(i);

        } else if(_v == mBinding.MainActivityAbout){
            Intent i = new Intent(this, AboutActivity.class);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_X, x);
            i.putExtra(Animation.EXTRA_CIRCULAR_REVEAL_Y, y);
            startActivity(i);
        }
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if(scrollY == 0) {
            mBinding.MainActivityGradiantAbove.setVisibility(View.INVISIBLE);
        } else {
            mBinding.MainActivityGradiantAbove.setVisibility(View.VISIBLE);
        }

        View view = mBinding.scrollView3.getChildAt(mBinding.scrollView3.getChildCount() - 1);
        int diff = (view.getBottom() - (mBinding.scrollView3.getHeight() + mBinding.scrollView3.getScrollY()));

        if(diff == 0) {
            mBinding.MainActivityGradiantBelow.setVisibility(View.INVISIBLE);
        } else {
            mBinding.MainActivityGradiantBelow.setVisibility(View.VISIBLE);
        }
    }
}
