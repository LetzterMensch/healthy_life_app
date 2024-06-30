package com.example.gr.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.gr.R;
import com.example.gr.view.adapter.SearchFragmentViewPagerAdapter;
import com.example.gr.utils.constant.Constant;
import com.example.gr.utils.constant.GlobalFunction;
import com.example.gr.databinding.ActivitySearchForFoodBinding;
import com.example.gr.controller.fragment.FoodListSearchFragment;
import com.example.gr.model.Diary;
import com.google.android.material.tabs.TabLayoutMediator;

public class SearchForFoodActivity extends BaseActivity {

    private ActivitySearchForFoodBinding mActivitySearchForFoodBinding;
    private int meal;
    private Diary mDiary;
    private SearchFragmentViewPagerAdapter searchFragmentViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchForFoodBinding = ActivitySearchForFoodBinding.inflate(getLayoutInflater());
        setContentView(mActivitySearchForFoodBinding.getRoot());
        getDataIntent();
        initToolbar();
        searchFood();
        initUi();
        initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendDataToFragment(String searchKey) {
        Intent intent = new Intent(FoodListSearchFragment.UPDATE_SEARCH_DATA);
        intent.putExtra("key", searchKey);
        sendBroadcast(intent);
    }
    private void sendMealDataToFragment(int meal){
        Intent intent = new Intent(FoodListSearchFragment.UPDATE_MEAL_DATA);
        intent.putExtra("key_meal",meal);
        sendBroadcast(intent);
        System.out.println("sent meal intent");
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
        Toast.makeText(this, "searching", Toast.LENGTH_SHORT).show();
        String strKey = mActivitySearchForFoodBinding.edtSearchName.getText().toString().trim();
        GlobalFunction.hideSoftKeyboard(this);
        sendDataToFragment(strKey);
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        meal = bundle.getInt("key_meal");
        System.out.println("get data "+meal);
        mDiary = (Diary) bundle.get(Constant.KEY_INTENT_DIARY_OBJECT);
    }

    private void initUi() {
        searchFragmentViewPagerAdapter = new SearchFragmentViewPagerAdapter(this,meal,mDiary);
        mActivitySearchForFoodBinding.viewPager2.setAdapter(searchFragmentViewPagerAdapter);
        new TabLayoutMediator(mActivitySearchForFoodBinding.searchTabLayout, mActivitySearchForFoodBinding.viewPager2,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Food");
                            break;
                        case 1:
                            tab.setText("Recipe");
                            break;
                    }
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
        if (meal != 4) {
            mealSpinner.setSelection(meal);
        } else {
            mealSpinner.setContentDescription("Chọn một bữa ăn");
        }
        mealSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                updateMeal(i);
                Toast.makeText(SearchForFoodActivity.this, "Selected Item : " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void updateMeal(int meal){
        this.meal = meal;
        System.out.println("selected "+this.meal);
        sendMealDataToFragment(this.meal);
    }
}
