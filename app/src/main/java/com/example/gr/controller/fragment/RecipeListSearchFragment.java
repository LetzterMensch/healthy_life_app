package com.example.gr.controller.fragment;

import static com.example.gr.controller.activity.SearchForFoodActivity.UPDATE_MEAL_DATA;
import static com.example.gr.controller.activity.SearchForFoodActivity.UPDATE_SEARCH_DATA;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.gr.controller.activity.ScrapeRecipeActivity;
import com.example.gr.controller.activity.RecipeDetailActivity;
import com.example.gr.model.Diary;
import com.example.gr.view.adapter.RecipeSearchTabAdapter;
import com.example.gr.utils.constant.Constant;
import com.example.gr.utils.constant.GlobalFunction;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.FragmentRecipeSearchBinding;
import com.example.gr.model.Recipe;

import java.util.List;

public class RecipeListSearchFragment extends BaseFragment{
    private FragmentRecipeSearchBinding mFragmentRecipeSearchBinding;
    private RecipeSearchTabAdapter mRecipeSearchTabAdapter;
    private String searchkey;
    private List<Recipe> mRecipeList;
    private int mMeal;
    private Diary mDiary;
    public static RecipeListSearchFragment newInstance(int meal, Diary diary){
        RecipeListSearchFragment recipeListSearchFragment = new RecipeListSearchFragment();
        Bundle args = new Bundle();
        args.putInt("key_meal",meal);
        args.putSerializable("key_diary",diary);
        recipeListSearchFragment.setArguments(args);
        return recipeListSearchFragment;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UPDATE_SEARCH_DATA.equals(intent.getAction())) {
                searchkey = intent.getStringExtra("key");
                mRecipeList = LocalDatabase.getInstance(getActivity()).recipeDAO().findRecipeByName("%"+searchkey+"%");
                displayRecipeItems();
            }
            if (UPDATE_MEAL_DATA.equals(intent.getAction())){
                mMeal = intent.getIntExtra("key_meal",0);
                System.out.println("receive intent : "+mMeal);
                displayRecipeItems();
            }
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentRecipeSearchBinding = FragmentRecipeSearchBinding.inflate(inflater, container, false);
        IntentFilter filter = new IntentFilter(UPDATE_SEARCH_DATA);
        filter.addAction(UPDATE_MEAL_DATA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
        }
        Bundle bundle = getArguments();
        if(bundle != null){
            mMeal = bundle.getInt("key_meal");
            mDiary = (Diary) bundle.getSerializable("key_diary");
        }
        initUi();
//        initListener();

        return mFragmentRecipeSearchBinding.getRoot();
    }
    private void initUi(){
     mFragmentRecipeSearchBinding.btnImportRecipe.setOnClickListener(v->{
            goToImportRecipeActivity();
        });
        displayRecipeItems();
    }
    private void getListRecipeFromLocalDatabase(String searchkey){
        if (getActivity() == null) {
            return;
        }
        if(searchkey == null){
            mRecipeList=LocalDatabase.getInstance(getActivity()).recipeDAO().getAllRecipe();
        }else if(searchkey.isEmpty()){
            mRecipeList=LocalDatabase.getInstance(getActivity()).recipeDAO().getAllRecipe();
        }
        else{
            mRecipeList = LocalDatabase.getInstance(getActivity()).recipeDAO().findRecipeByName("%"+searchkey+"%");
        }
    }
    private void displayRecipeItems(){
        getListRecipeFromLocalDatabase(searchkey);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentRecipeSearchBinding.rcvRecipeSearchTab.setLayoutManager(gridLayoutManager);
        mRecipeSearchTabAdapter = new RecipeSearchTabAdapter(mRecipeList,this::goToRecipeDetail);
        mFragmentRecipeSearchBinding.rcvRecipeSearchTab.setAdapter(mRecipeSearchTabAdapter);
    }

    @Override
    public void onResume() {
        displayRecipeItems();
        super.onResume();
    }
    @Override
    protected void updateUIAfterShowSnackBar() {

    }

    private void goToImportRecipeActivity() {
        Bundle bundle = new Bundle();
        GlobalFunction.startActivity(getActivity(), ScrapeRecipeActivity.class, bundle);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the receiver
        requireActivity().unregisterReceiver(receiver);
    }
    @Override
    protected void initToolbar() {

    }
    private void goToRecipeDetail(@NonNull Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_RECIPE_OBJECT, recipe);
        bundle.putSerializable("key_diary", mDiary);
        bundle.putInt("key_meal",mMeal);
        GlobalFunction.startActivity(getActivity(), RecipeDetailActivity.class, bundle);
    }
}
