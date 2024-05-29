package com.example.gr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gr.R;
import com.example.gr.adapter.FoodSearchTabAdapter;
import com.example.gr.adapter.SearchFragmentViewPagerAdapter;
import com.example.gr.databinding.ActivitySearchForFoodBinding;
import com.example.gr.model.Food;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class SearchForFoodActivity extends BaseActivity {

    private ActivitySearchForFoodBinding mActivitySearchForFoodBinding;
    private int meal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchForFoodBinding = ActivitySearchForFoodBinding.inflate(getLayoutInflater());
        setContentView(mActivitySearchForFoodBinding.getRoot());
        getDataIntent();
        initToolbar();
        initUi();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        this.meal = bundle.getInt("meal");
    }

    private void initUi() {
        SearchFragmentViewPagerAdapter searchFragmentViewPagerAdapter = new SearchFragmentViewPagerAdapter(this);
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
