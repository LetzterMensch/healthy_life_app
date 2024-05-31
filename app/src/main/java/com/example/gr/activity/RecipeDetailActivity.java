package com.example.gr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gr.constant.Constant;
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
            mRecipe = (Recipe) bundle.get(Constant.KEY_INTENT_RECIPE_OBJECT);
        }
    }
    private void setDataRecipeDetail(){
        if(mRecipe == null){
            return;
        }
        GlideUtils.loadUrlBanner(mRecipe.getBanner(), mActivityRecipeDetailBinding.recipeImg);
        mActivityRecipeDetailBinding.tvFoodName.setText(mRecipe.getName());
        mActivityRecipeDetailBinding.tvFoodDescription.setText(mRecipe.getDescription());
    }
}
