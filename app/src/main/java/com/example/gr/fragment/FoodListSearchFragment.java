package com.example.gr.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.gr.database.LocalDatabase;
import com.example.gr.databinding.FragmentFoodSearchBinding;
import com.example.gr.model.Food;

import java.util.List;

public class FoodListSearchFragment extends BaseFragment{
    private FragmentFoodSearchBinding mFragmentFoodSearchBinding;
    private FoodSearchTabAdapter mFoodSearchTabAdapter;
    private String searchkey;
    private List<Food> mfoodList;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("update_search_data".equals(intent.getAction())) {
                searchkey = intent.getStringExtra("key");
                mfoodList = LocalDatabase.getInstance(getActivity()).foodDAO().findFoodByName("%"+searchkey+"%");
                displayFoodItems();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFoodSearchBinding = FragmentFoodSearchBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            searchkey = bundle.getString("string");
        }
        displayFoodItems();
        // Initialize and register the BroadcastReceiver

        IntentFilter filter = new IntentFilter("update_search_data");
        requireActivity().registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
//        initListener();

        return mFragmentFoodSearchBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the receiver
        getActivity().unregisterReceiver(receiver);
    }
    private void displayFoodItems() {
        if(searchkey == null){
            mfoodList=LocalDatabase.getInstance(getActivity()).foodDAO().getAllFood();
        }else{
            mfoodList = LocalDatabase.getInstance(getActivity()).foodDAO().findFoodByName("%"+searchkey+"%");
        }
        mFoodSearchTabAdapter = new FoodSearchTabAdapter(mfoodList,this::goToFoodDetail);
        mFragmentFoodSearchBinding.rcvFoodSearchTab.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentFoodSearchBinding.rcvFoodSearchTab.setAdapter(mFoodSearchTabAdapter);
    }
    private void goToFoodDetail(@NonNull Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFunction.startActivity(getActivity(), FoodDetailActivity.class, bundle);
    }

    @Override
    protected void updateUIAfterShowSnackBar() {

    }

    @Override
    protected void initToolbar() {

    }
}
