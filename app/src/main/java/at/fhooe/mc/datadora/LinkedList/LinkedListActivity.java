package at.fhooe.mc.datadora.LinkedList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Random;
import java.util.Vector;

import at.fhooe.mc.datadora.Animation;
import at.fhooe.mc.datadora.LinkedList.Fragment.LinkedListAddFragment;
import at.fhooe.mc.datadora.LinkedList.Fragment.LinkedListDeleteFragment;
import at.fhooe.mc.datadora.LinkedList.Fragment.LinkedListGetFragment;
import at.fhooe.mc.datadora.MainActivity;
import at.fhooe.mc.datadora.R;
import at.fhooe.mc.datadora.databinding.ActivityLinkedListBinding;

public class LinkedListActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "LinkedListActivity : ";

    private ActivityLinkedListBinding mBinding;
    private final Vector<Integer> mLinkedList = new Vector<>();

    //Shared Preferences setup
    private static final String SP_FILE_KEY = "at.fhooe.mc.datadora.LinkedListSharedPreferenceFile.LinkedList";
    private static final String SP_VALUE_KEY = "at.fhooe.mc.datadora.LinkedListKey2020";
    private SharedPreferences mSharedPreferences;

    private Animation mAnimation;

    TabLayout mTabLayout;
    LinkedListAddFragment mAdd = new LinkedListAddFragment();
    LinkedListDeleteFragment mDelete = new LinkedListDeleteFragment();
    LinkedListGetFragment mGet = new LinkedListGetFragment();

    private boolean mHasPointer;

    public Vector<Integer> getLinkedList() { return mLinkedList; }

    public ActivityLinkedListBinding getBinding() {
        return mBinding;
    }

    @Override
    protected void onCreate(final Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        mBinding = ActivityLinkedListBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        View layout = mBinding.LinkedListActivity;
        mAnimation = new Animation(layout, getIntent(), this);
        mBinding.LinkedListActivityView.setActivity(this);
        mBinding.LinkedListActivityViewPointer.setActivity(this);

        mSharedPreferences = getSharedPreferences(SP_FILE_KEY, Context.MODE_PRIVATE);
        mBinding.LinkedListActivityViewPointer.setVisibility(View.GONE);

        head();
        setUpToolbar();
        setUpTabLayout();
        setUpSlider();

        mBinding.LinkedListActivityRandom.setOnClickListener(this);
        mBinding.LinkedListActivityRadioGroupType.setOnCheckedChangeListener(this);
        mBinding.LinkedListActivityHead.setChecked(true);

        mBinding.LinkedListActivitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) {
                mAdd.setPointer(_isChecked);
                mDelete.setPointer(_isChecked);
                mGet.setPointer(_isChecked);
                mHasPointer = _isChecked;
                mBinding.LinkedListActivityView.setSwitch(_isChecked);
            }
        });
    }

    private void setUpToolbar() {
        Toolbar myToolbar = mBinding.LinkedListActivityToolbar;
        setSupportActionBar(myToolbar);
        float type = getIntent().getExtras().getFloat(MainActivity.LINKED_LIST_TYPE);

        if(type == MainActivity.LINKED_LIST_SINGLE) {
            getSupportActionBar().setTitle(R.string.All_Data_Activity_Single_LinkedList);
            mBinding.LinkedListActivityViewPointer.setSingleList(true);
        } else {
            mBinding.LinkedListActivityViewPointer.setSingleList(false);
            getSupportActionBar().setTitle(R.string.All_Data_Activity_Double_LinkedList);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void setUpTabLayout() {
        mTabLayout = findViewById(R.id.LinkedList_Activity_TabLayout);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_START);

        LinkedListTabAdapter adapter = new LinkedListTabAdapter(this);

        adapter.addFragment(mAdd);
        adapter.addFragment(mDelete);
        adapter.addFragment(mGet);

        ViewPager2 viewPager = findViewById(R.id.LinkedList_Activity_ViewPager);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(mTabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab _tab, int _position) {
                if(_position == 0) {
                    _tab.setText(R.string.LinkedList_Activity_Add_Title);
                } else if(_position == 1) {
                    _tab.setText(R.string.LinkedList_Activity_Delete_Title);
                } else
                    _tab.setText(R.string.BST_Activity_Get_Title);
                }
        }).attach();
    }

    private void setUpSlider() {
        Slider slider = mBinding.LinkedListActivityInputSlider;
        slider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);    // converting the float value to an int value
            }
        });
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                mBinding.LinkedListActivityInputValue.setText(String.valueOf((int) value));
            }
        });
    }

    /**
     * This method checks if the given user input is correct.
     * If the stack is bigger than a certain size then the user cannot push anymore as the stack is full
     *
     * @return true if the input is valid, false if its not
     */
    public boolean isInputValid() {
        if (mLinkedList.size() >= 17) {
            Toast.makeText(this, R.string.LinkedList_Activity_Toast_Full, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Vector<Integer> v = loadFromSave();
        if(v != null) {
            mLinkedList.clear();
            mLinkedList.addAll(v);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    /**
     * Saves the current vector (input from the user) into the SharedPreferences.
     */
    private void save() {

        // init the SP object
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        // Convert the vector containing the integers to a string
        Vector<Integer> vector = mBinding.LinkedListActivityView.getValues().getLinkedListNum();
        StringBuilder vectorStr = new StringBuilder();

        // transform the vector into a string
        for (int i = 0; i < vector.size(); i++) {
            if (i != vector.size() - 1) {
                vectorStr.append(vector.get(i)).append(",");
            } else {
                vectorStr.append(vector.get(i));
            }
        }

        editor.putString(SP_VALUE_KEY, String.valueOf(vectorStr));
        editor.apply();
    }

    /**
     * Gets the saved vector (user input) from the SharedPreferences.
     * @return the saved vector or null if there is none
     */
    protected Vector<Integer> loadFromSave() {

        // get the saved string (vector)
        String defaultValue = "empty";
        String vectorStr = mSharedPreferences.getString(SP_VALUE_KEY, defaultValue);
        Vector<Integer> vector = new Vector<>();

        // check if it was successful -> transform to vector, or if not -> return null
        int begin;
        int end = 0;
        int i;
        if(vectorStr == null || vectorStr.contains(defaultValue) || vectorStr.equals("")) {
            return null;
        } else {
            while(end > -1) {
                begin = end;
                end = vectorStr.indexOf(',', begin);
                if(end == -1) {
                    i = Integer.parseInt(vectorStr.substring(begin));
                } else {
                    i = Integer.parseInt(vectorStr.substring(begin, end));
                    end++;
                }
                vector.add(i);
            }
            return vector;
        }
    }

    @Override
    public void onClick(View _v) {
        if(_v == mBinding.LinkedListActivityRandom) {
            random();
        }
    }

    /**
     * This method handles the operation random
     */
    private void random(){
        mBinding.LinkedListActivityReturnValue.setText("");
        createRandomList();
        mBinding.LinkedListActivityView.random(mLinkedList);

        int i = mTabLayout.getSelectedTabPosition();
        if(i == 0) {
            mAdd.preparePositionSlider();
        } else if (i == 1) {
            mDelete.preparePositionSlider();
        } else {
            mGet.preparePositionSlider();
        }
    }

    /**
     * Creates a random queue with its size being min 4 and max 7
     */
    private void createRandomList(){
        mLinkedList.clear();
        Random r = new Random();
        int size = 4 + r.nextInt(6);

        for(int i = 0; i < size; i++){
            int x = -5 + r.nextInt(100);
            mLinkedList.add(x);
        }
    }

    private void sorted(){ }

    private void unsorted() { }

    private void head(){
        if (mHasPointer) {
            mBinding.LinkedListActivityViewPointer.head();
        } else {
            mBinding.LinkedListActivityView.head();
        }
    }

    private void tail(){
        if (mHasPointer) {
            mBinding.LinkedListActivityViewPointer.tail();
        } else {
            mBinding.LinkedListActivityView.tail();
        }
    }

    private void both(){
        if (mHasPointer) {
            mBinding.LinkedListActivityViewPointer.both();
        } else {
            mBinding.LinkedListActivityView.both();
        }
    }

    public void isEmptyMessage(){
        Toast.makeText(this, R.string.LinkedList_Activity_Toast_Empty, Toast.LENGTH_SHORT).show();
        mBinding.LinkedListActivityReturnValue.setText("");
    }

    @Override
    public void onBackPressed() {
        mAnimation.circularUnreveal();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup _group, int _checkedId) {
        if(_checkedId == R.id.LinkedList_Activity_Head) {
            head();
        } else if(_checkedId == R.id.LinkedList_Activity_Tail) {
            tail();
        } else if(_checkedId == R.id.LinkedList_Activity_Head_Tail) {
            both();
        }
    }
}