package at.fhooe.mc.datadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TabLayout tabLayout = findViewById(R.id.LinkedList_TabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_START);
    }
}