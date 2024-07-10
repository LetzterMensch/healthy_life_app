package com.example.gr.controller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gr.R;
import com.example.gr.utils.constant.Constant;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.ActivityFoodDetailBinding;
import com.example.gr.model.Diary;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;

import java.text.DecimalFormat;

public class FoodDetailActivity extends BaseActivity {
    private ActivityFoodDetailBinding mActivityFoodDetailBinding;
    private Food mFood;
    private FoodLog mFoodLog;
    private Diary mDiary;
    private int meal;
    private float numberOfServings;
    private DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityFoodDetailBinding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityFoodDetailBinding.getRoot());
        initToolbar();
        getDataIntent();
        initUI();

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
    private void saveFoodLog(){ // for editing food log
        String editText = mActivityFoodDetailBinding.editNumberOfServings.getText().toString();
        if (editText.isEmpty()){
            numberOfServings = 0;
        }else{
            numberOfServings = Float.parseFloat(editText);
        }
        mDiary.updateDiaryAfterRemove(mFoodLog);
        mFoodLog.updateFoodLog(numberOfServings);
        mDiary.updateFoodLog(mFoodLog);
        finish();
    }
    private void addFoodLog() { // for add food to diary
        String editText = mActivityFoodDetailBinding.editNumberOfServings.getText().toString();
        if (editText.isEmpty()){
            numberOfServings = 0;
        }else{
            numberOfServings = Float.parseFloat(editText);
        }
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
                numberOfServings = mFood.getNumberOfServings();
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
    private void initUI() {
        if (mFood == null) {
            return;
        }
        mActivityFoodDetailBinding.foodName.setText(mFood.getName());
        mActivityFoodDetailBinding.servingSize.setText(String.valueOf(mFood.getServingSize()));
        mActivityFoodDetailBinding.calories.setText(getString(R.string.unit_calories_burnt, df.format(mFoodLog.getTotalCalories())));
        mActivityFoodDetailBinding.foodCarb.setText(getString(R.string.unit_calories_burnt, df.format(mFoodLog.getTotalCarb())));
        mActivityFoodDetailBinding.foodFat.setText(getString(R.string.unit_calories_burnt, df.format(mFoodLog.getTotalFat())));
        mActivityFoodDetailBinding.foodProtein.setText(getString(R.string.unit_calories_burnt, df.format(mFoodLog.getTotalProtein())));
    }
}
