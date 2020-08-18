package at.fhooe.mc.datadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.text.NumberFormat;
import java.util.Locale;

import at.fhooe.mc.datadora.databinding.ActivityMainBinding;
import at.fhooe.mc.datadora.databinding.ActivityStackBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        mBinding.MainActivityButtonStack.setOnClickListener(this);
        mBinding.MainActivityButtonQueue.setOnClickListener(this);
        mBinding.MainActivityButtonLinkedList.setOnClickListener(this);

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
        }
    }
}
