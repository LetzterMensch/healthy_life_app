package com.example.gr.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.gr.constant.GlobalFunction;
import androidx.annotation.Nullable;

import com.example.gr.R;
import com.example.gr.adapter.SearchFragmentViewPagerAdapter;
import com.example.gr.database.LocalDatabase;
import com.example.gr.databinding.ActivitySearchForFoodBinding;
import com.example.gr.model.Food;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.Serializable;
import java.util.List;

public class SearchForFoodActivity extends BaseActivity {

    private ActivitySearchForFoodBinding mActivitySearchForFoodBinding;
    private BroadcastReceiver receiver;

    private int meal;
    private SearchFragmentViewPagerAdapter searchFragmentViewPagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchForFoodBinding = ActivitySearchForFoodBinding.inflate(getLayoutInflater());
        setContentView(mActivitySearchForFoodBinding.getRoot());
        // Initialize and register the BroadcastReceiver
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("update_search_data".equals(intent.getAction())) {
                    String data = intent.getStringExtra("key");
                    // Handle data received from Fragment
                }
            }
        };
        IntentFilter filter = new IntentFilter("update_search_data");
        registerReceiver(receiver, filter);
        getDataIntent();
        initToolbar();
        initUi();
        initListener();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    private void sendDataToFragment(String data) {
        Intent intent = new Intent("update_search_data");
        intent.putExtra("key", data);
        sendBroadcast(intent);
    }
    private void initListener() {
        mActivitySearchForFoodBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
                String strKey = s.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mActivitySearchForFoodBinding.imgSearch.setOnClickListener(view -> searchFood());

        mActivitySearchForFoodBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood();
                return true;
            }
            return false;
        });
    }
    private void searchFood() {
        Toast.makeText(this,"searching",Toast.LENGTH_SHORT).show();
        String strKey = mActivitySearchForFoodBinding.edtSearchName.getText().toString().trim();
        GlobalFunction.hideSoftKeyboard(this);
        sendDataToFragment(strKey);
    }
    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        this.meal = bundle.getInt("meal");
    }

    private void initUi() {
        searchFragmentViewPagerAdapter = new SearchFragmentViewPagerAdapter(this);
        mActivitySearchForFoodBinding.viewPager2.setAdapter(searchFragmentViewPagerAdapter);
        new TabLayoutMediator(mActivitySearchForFoodBinding.searchTabLayout, mActivitySearchForFoodBinding.viewPager2,
                (tab, position) -> {
                    if (position == 0) tab.setText("Food");
                    else tab.setText("Recipe");
                }
        ).attach();
    }

    private void initToolbar() {
        mActivitySearchForFoodBinding.imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        initSpinner(this.meal);
    }
    private void initSpinner(int meal) {
        Spinner mealSpinner = mActivitySearchForFoodBinding.mealSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.meals_array, R.layout.meal_spinner_item);
        adapter.setDropDownViewResource(R.layout.meal_spinner_dropdown_item);
        mealSpinner.setAdapter(adapter);
        mealSpinner.setDropDownVerticalOffset(150);
        if(meal != 4){
            mealSpinner.setSelection(meal);
        }else{
            mealSpinner.setContentDescription("Chọn một bữa ăn");
        }
        mealSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(SearchForFoodActivity.this, "Selected Item : " + item, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
