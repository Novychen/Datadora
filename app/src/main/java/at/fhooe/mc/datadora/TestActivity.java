package at.fhooe.mc.datadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import at.fhooe.mc.datadora.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    ActivityTestBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityTestBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
    }
}