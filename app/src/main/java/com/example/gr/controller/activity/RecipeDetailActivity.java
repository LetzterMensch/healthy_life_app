package com.example.gr.controller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gr.utils.constant.Constant;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.ActivityRecipeDetailBinding;
import com.example.gr.model.Recipe;
import com.example.gr.utils.GlideUtils;

public class RecipeDetailActivity extends BaseActivity {
    private ActivityRecipeDetailBinding mActivityRecipeDetailBinding;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRecipeDetailBinding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityRecipeDetailBinding.getRoot());

        getDataIntent();
        initToolbar();
        setDataRecipeDetail();
    }
    private void initToolbar(){
        mActivityRecipeDetailBinding.imgBack.setOnClickListener(v->getOnBackPressedDispatcher().onBackPressed());
        mActivityRecipeDetailBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(RecipeDetailActivity.this,"Added to diary",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if(bundle.get(Constant.KEY_INTENT_RECIPE_OBJECT) != null){
                mRecipe = (Recipe) bundle.get(Constant.KEY_INTENT_RECIPE_OBJECT);
            }
            if(bundle.get(Constant.KEY_INTENT_CREATE_RECIPE_OBJECT) != null){
                mRecipe = (Recipe) bundle.get(Constant.KEY_INTENT_CREATE_RECIPE_OBJECT);
                mActivityRecipeDetailBinding.btnSaveRecipe.setVisibility(View.VISIBLE);
                mActivityRecipeDetailBinding.btnSaveRecipe.setOnClickListener(v->{
                    saveRecipe();
                });
            }
        }
    }
    private void saveRecipe(){
        LocalDatabase.getInstance(this).recipeDAO().insertRecipe(mRecipe);
        LocalDatabase.getInstance(this).foodDAO().insertFood(mRecipe.getFood());
        finish();
    }
    private void setDataRecipeDetail(){
        if(mRecipe == null){
            return;
        }
        GlideUtils.loadUrl(mRecipe.getImage(), mActivityRecipeDetailBinding.recipeImg);
        mActivityRecipeDetailBinding.tvFoodName.setText(mRecipe.getName());
        mActivityRecipeDetailBinding.tvFoodDescription.setText(mRecipe.getDescription());
        mActivityRecipeDetailBinding.tvRecipeIngredients.setText(mRecipe.getIngredients());
        mActivityRecipeDetailBinding.tvCalories.setText(String.valueOf(mRecipe.getCalories()));
        float progress;
        // Set indicator
        progress = mRecipe.getCarbs()*100/mRecipe.getCalories();
        System.out.println(progress);
        mActivityRecipeDetailBinding.carbIndicator.setProgress((int)progress);
        mActivityRecipeDetailBinding.foodCarb.setText(String.valueOf((int)mRecipe.getCarbs()));

        progress = mRecipe.getProtein()*100/mRecipe.getCalories();
        mActivityRecipeDetailBinding.proteinIndicator.setProgress((int)progress);
        mActivityRecipeDetailBinding.foodProtein.setText(String.valueOf((int)mRecipe.getProtein()));

        progress = mRecipe.getFat()*100/mRecipe.getCalories();
        mActivityRecipeDetailBinding.fatIndicator.setProgress((int)progress);
        mActivityRecipeDetailBinding.foodFat.setText(String.valueOf((int)mRecipe.getFat()));
    }
}
