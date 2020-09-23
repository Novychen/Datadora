package at.fhooe.mc.datadora;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import at.fhooe.mc.datadora.databinding.ActivityBinarySearchTreeTestBinding;

public class Test extends AppCompatActivity {

    private ActivityBinarySearchTreeTestBinding mBinding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBinarySearchTreeTestBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
    }
}
