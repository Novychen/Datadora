package at.fhooe.mc.datadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

        mBinding.MainActivityButtonStack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.MainActivityButtonStack) {
            Intent i = new Intent();
            startActivity(i);
        }
    }
}
