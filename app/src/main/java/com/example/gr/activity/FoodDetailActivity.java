package com.example.gr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gr.constant.Constant;
import com.example.gr.databinding.ActivityFoodDetailBinding;
import com.example.gr.model.Food;

public class FoodDetailActivity extends BaseActivity{
    private ActivityFoodDetailBinding mActivityFoodDetailBinding;
    private Food mFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityFoodDetailBinding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityFoodDetailBinding.getRoot());

        getDataIntent();
        initToolbar();
        setDataFoodDetail();
//        initListener();
    }
    private void initToolbar(){
        mActivityFoodDetailBinding.imgBack.setOnClickListener(v->getOnBackPressedDispatcher().onBackPressed());
        mActivityFoodDetailBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoodDetailActivity.this,"Added to diary",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFood = (Food) bundle.get(Constant.KEY_INTENT_FOOD_OBJECT);
        }
    }
    private void setDataFoodDetail(){
        if(mFood == null){
            return;
        }
        mActivityFoodDetailBinding.foodName.setText(mFood.getName());
        mActivityFoodDetailBinding.numberOfServings.setText(String.valueOf(mFood.getNumberOfServings()));
        mActivityFoodDetailBinding.servingSize.setText(String.valueOf(mFood.getServingSize()));
        mActivityFoodDetailBinding.calories.setText(String.valueOf(mFood.getCalories()));
    }
}
