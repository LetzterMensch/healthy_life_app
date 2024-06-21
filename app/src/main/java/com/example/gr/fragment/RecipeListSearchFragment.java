package com.example.gr.fragment;

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

import com.example.gr.activity.FoodDetailActivity;
import com.example.gr.activity.ImportRecipeFromWebActivity;
import com.example.gr.activity.RecipeDetailActivity;
import com.example.gr.adapter.RecipeSearchTabAdapter;
import com.example.gr.constant.Constant;
import com.example.gr.constant.GlobalFunction;
import com.example.gr.database.LocalDatabase;
import com.example.gr.databinding.FragmentRecipeSearchBinding;
import com.example.gr.model.Food;
import com.example.gr.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeListSearchFragment extends BaseFragment{
    private FragmentRecipeSearchBinding mFragmentRecipeSearchBinding;
    private RecipeSearchTabAdapter mRecipeSearchTabAdapter;
    private String searchkey;
    private List<Recipe> mRecipeList;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("update_search_data".equals(intent.getAction())) {
                searchkey = intent.getStringExtra("key");
                mRecipeList = LocalDatabase.getInstance(getActivity()).recipeDAO().findRecipeByName("%"+searchkey+"%");
                displayRecipeItems();
            }
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentRecipeSearchBinding = FragmentRecipeSearchBinding.inflate(inflater, container, false);
//        if (getArguments() != null) {
//            Bundle bundle = getArguments();
//            searchkey = bundle.getString("string");
//        }
        IntentFilter filter = new IntentFilter("update_search_data");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
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
        GlobalFunction.startActivity(getActivity(), ImportRecipeFromWebActivity.class, bundle);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the receiver
        getActivity().unregisterReceiver(receiver);
    }
    @Override
    protected void initToolbar() {

    }
    private void goToRecipeDetail(@NonNull Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_RECIPE_OBJECT, recipe);
        GlobalFunction.startActivity(getActivity(), RecipeDetailActivity.class, bundle);
    }
}
