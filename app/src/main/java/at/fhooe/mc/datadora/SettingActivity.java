package at.fhooe.mc.datadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import at.fhooe.mc.datadora.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity : ";

    ActivitySettingBinding mBinding;
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        setUpSlider();
        setUpToolbar();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mBinding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) {
                changeTheme(getResources().getConfiguration());
            }
        });

        boolean isSystem = mPreferences.getBoolean("IS_SYSTEM", true);
        int mode = mPreferences.getInt("MODE", 0);

        if (isSystem) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (mode == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (mode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void setUpToolbar() {
        Toolbar myToolbar = mBinding.SettingActivityToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.Toolbar_Action_Settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpSlider() {
        Slider slider = mBinding.SettingActivityAnimSlider;
        slider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                if(value < 0) {
                    return String.valueOf((value * -1));
                }
                return String.valueOf((value));
            }
        });

        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider _slider, float _value, boolean _fromUser) {
                String v = String.valueOf(_value);
                if(_value > 0) {
                    v = v + " \u00D7 faster";
                } else if (_value < 0) {
                    v = String.valueOf(_value * -1);
                    v = v + " \u00D7 slower";
                } else {
                    v = "in its default state";
                }
                mBinding.SettingActivitySpeedVal.setText(v);
            }
        });
    }

    private void changeTheme(Configuration _config) {
        int currentNightMode = _config.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                mPreferences.edit().putInt("MODE", 1).apply();
                break;

            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                mPreferences.edit().putInt("MODE", 0).apply();
                break;
        }
        mPreferences.edit().putBoolean("IS_SYSTEM", false).apply();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration _newConfig) {
      changeTheme(_newConfig);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true; }
}