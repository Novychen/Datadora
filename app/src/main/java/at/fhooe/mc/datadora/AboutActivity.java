package at.fhooe.mc.datadora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import at.fhooe.mc.datadora.databinding.ActivityAboutBinding;


public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding mBinding;
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAboutBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        final View layout = mBinding.AboutActivity;
        mAnimation = new Animation(layout, getIntent(), this);

        // setup Toolbar
        Toolbar myToolbar = mBinding.AboutActivityToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        mAnimation.circularUnreveal();
    }
}