package com.example.gr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gr.constant.Constant;
import com.example.gr.database.LocalDatabase;
import com.example.gr.databinding.ActivityFoodDetailBinding;
import com.example.gr.model.Diary;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;
import com.example.gr.utils.DateTimeUtils;

import java.util.Calendar;

public class FoodDetailActivity extends BaseActivity {
    private ActivityFoodDetailBinding mActivityFoodDetailBinding;
    private Food mFood;
    private FoodLog mFoodLog;
    private Diary mDiary;
    private int meal;
    private float numberOfServings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityFoodDetailBinding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityFoodDetailBinding.getRoot());
        initToolbar();
        getDataIntent();
        displayFoodDetail();

//        initListener();
    }

    private void initToolbar() {
        mActivityFoodDetailBinding.imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        mActivityFoodDetailBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoodDetailActivity.this, "Đã thêm vào nhật ký", Toast.LENGTH_SHORT).show();
                addFoodLog();
            }
        });
        mActivityFoodDetailBinding.imgSave.setOnClickListener(v->{
            Toast.makeText(FoodDetailActivity.this, "Đã lưu lại chỉnh sửa", Toast.LENGTH_SHORT).show();
            saveFoodLog();
        });
    }
    private void saveFoodLog(){
        numberOfServings = Float.parseFloat(String.valueOf(mActivityFoodDetailBinding.editNumberOfServings.getText()));
        mFoodLog.updateFoodLog(numberOfServings);
        mDiary.updateDiaryAfterRemove(mFoodLog);
        mDiary.updateFoodLog(mFoodLog);
        finish();
    }
    private void addFoodLog() {
        numberOfServings = Float.parseFloat(String.valueOf(mActivityFoodDetailBinding.editNumberOfServings.getText()));
        mFoodLog.updateFoodLog(numberOfServings);
        mDiary.logFood(mFoodLog);
        finish();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get(Constant.KEY_INTENT_FOOD_OBJECT) != null) {
                mFood = (Food) bundle.get(Constant.KEY_INTENT_FOOD_OBJECT);
                mActivityFoodDetailBinding.editNumberOfServings.setVisibility(View.GONE);
                mActivityFoodDetailBinding.numberOfServings.setText(String.valueOf(mFood.getNumberOfServings()));
                mActivityFoodDetailBinding.imgAdd.setVisibility(View.GONE);
            }
            else if (bundle.get(Constant.KEY_INTENT_ADD_FOOD_LOG_OBJECT) != null) {
                mFood = (Food) bundle.get(Constant.KEY_INTENT_ADD_FOOD_LOG_OBJECT);
                mDiary = (Diary) bundle.getSerializable("key_diary");
                meal = bundle.getInt("key_meal");
                numberOfServings = Float.parseFloat(String.valueOf(mActivityFoodDetailBinding.editNumberOfServings.getText()));
                mFoodLog = new FoodLog(mFood, meal, numberOfServings,mDiary.getId());
                mActivityFoodDetailBinding.numberOfServings.setVisibility(View.GONE);
                mActivityFoodDetailBinding.editNumberOfServings.setVisibility(View.VISIBLE);
                mActivityFoodDetailBinding.editNumberOfServings.setText(String.valueOf(mFoodLog.getFood().getNumberOfServings()));
                mActivityFoodDetailBinding.imgAdd.setVisibility(View.VISIBLE);
            }else if (bundle.get(Constant.KEY_INTENT_EDIT_FOOD_LOG_OBJECT) != null) {
                mFoodLog = (FoodLog) bundle.get(Constant.KEY_INTENT_EDIT_FOOD_LOG_OBJECT);
                mFood = LocalDatabase.getInstance(this).foodDAO().getFoodById(mFoodLog.getFoodId());
                mFoodLog.setFood(mFood);
                mDiary = (Diary) bundle.getSerializable("key_diary");
                meal = bundle.getInt("key_meal");
                mActivityFoodDetailBinding.numberOfServings.setVisibility(View.GONE);
                mActivityFoodDetailBinding.editNumberOfServings.setVisibility(View.VISIBLE);
                mActivityFoodDetailBinding.imgAdd.setVisibility(View.GONE);
                mActivityFoodDetailBinding.imgSave.setVisibility(View.VISIBLE);
                mActivityFoodDetailBinding.editNumberOfServings.setText(String.valueOf(mFoodLog.getNumberOfServings()));

            }
        }
    }

    // Display general information about the food
    private void displayFoodDetail() {
        if (mFood == null) {
            return;
        }
        mActivityFoodDetailBinding.foodName.setText(mFood.getName());
        mActivityFoodDetailBinding.servingSize.setText(String.valueOf(mFood.getServingSize()));
        mActivityFoodDetailBinding.calories.setText(String.valueOf(mFood.getCalories()));
        mActivityFoodDetailBinding.foodCarb.setText(String.valueOf(mFood.getCarb()*4));
        mActivityFoodDetailBinding.foodProtein.setText(String.valueOf(mFood.getProtein()*4));
        mActivityFoodDetailBinding.foodFat.setText(String.valueOf(mFood.getFat()*9));
    }
}
