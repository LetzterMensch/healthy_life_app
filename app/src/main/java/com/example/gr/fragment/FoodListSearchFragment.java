package com.example.gr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.activity.FoodDetailActivity;
import com.example.gr.adapter.FoodAdapter;
import com.example.gr.adapter.FoodSearchTabAdapter;
import com.example.gr.constant.Constant;
import com.example.gr.constant.GlobalFunction;
import com.example.gr.databinding.FragmentFoodSearchBinding;
import com.example.gr.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodListSearchFragment extends BaseFragment{
    private FragmentFoodSearchBinding mFragmentFoodSearchBinding;
    private FoodSearchTabAdapter mFoodSearchTabAdapter;
    private List<Food> foodList;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFoodSearchBinding = FragmentFoodSearchBinding.inflate(inflater, container, false);

        initUi();
//        initListener();

        return mFragmentFoodSearchBinding.getRoot();
    }
    private void initUi() {
        ArrayList<Food> foodArrayList = new ArrayList<>();
        foodList = new ArrayList<>();
        foodList.add(new Food("French fires", 1.0f, 118));
        foodList.add(new Food("Hamburger", 1.0f, 118));
        foodList.add(new Food("Brisket", 1.0f, 118));
        foodList.add(new Food("Buffalo chicken", 1.0f, 118));
        mFoodSearchTabAdapter = new FoodSearchTabAdapter(foodList,this::goToFoodDetail);
        mFragmentFoodSearchBinding.rcvFoodSearchTab.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentFoodSearchBinding.rcvFoodSearchTab.setAdapter(mFoodSearchTabAdapter);
    }
    private void goToFoodDetail(@NonNull Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFunction.startActivity(getActivity(), FoodDetailActivity.class, bundle);
    }
        @Override
    protected void initToolbar() {

    }
}
