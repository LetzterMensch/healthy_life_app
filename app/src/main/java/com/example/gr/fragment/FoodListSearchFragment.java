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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.activity.FoodDetailActivity;
import com.example.gr.adapter.FoodSearchTabAdapter;
import com.example.gr.constant.Constant;
import com.example.gr.constant.GlobalFunction;
import com.example.gr.database.LocalDatabase;
import com.example.gr.databinding.FragmentFoodSearchBinding;
import com.example.gr.model.Diary;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;
import com.example.gr.utils.DateTimeUtils;

import java.util.Calendar;
import java.util.List;

public class FoodListSearchFragment extends BaseFragment{
    private FragmentFoodSearchBinding mFragmentFoodSearchBinding;
    private FoodSearchTabAdapter mFoodSearchTabAdapter;
    private String searchkey;
    private List<Food> mfoodList;
    private int mMeal;
    private Diary mDiary;
    public static final String UPDATE_SEARCH_DATA = "update_search_data";
    public static final String UPDATE_MEAL_DATA = "update_meal_data";

    public static FoodListSearchFragment newInstance(int meal, Diary diary){
        FoodListSearchFragment foodListSearchFragment = new FoodListSearchFragment();
        Bundle args = new Bundle();
        args.putInt("key_meal",meal);
        args.putSerializable("key_diary",diary);
        foodListSearchFragment.setArguments(args);
        return foodListSearchFragment;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UPDATE_SEARCH_DATA.equals(intent.getAction())) {
                searchkey = intent.getStringExtra("key");
                System.out.println("receive intent : "+searchkey);
                mfoodList = LocalDatabase.getInstance(getActivity()).foodDAO().findFoodByName("%"+searchkey+"%");
                displayFoodItems();
            }
            if (UPDATE_MEAL_DATA.equals(intent.getAction())){
                mMeal = intent.getIntExtra("key_meal",0);
                System.out.println("receive intent : "+mMeal);
                displayFoodItems();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFoodSearchBinding = FragmentFoodSearchBinding.inflate(inflater, container, false);
//        mDiary = LocalDatabase.getInstance(getActivity()).diaryDAO().getDiaryByDate(DateTimeUtils.simpleDateFormat(Calendar.getInstance().getTime()));
        // Initialize and register the BroadcastReceiver
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
//        initListener();
//        displayFoodItems();
        return mFragmentFoodSearchBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the receiver
        requireActivity().unregisterReceiver(receiver);
    }
    @Override
    public void onResume() {
        displayFoodItems();
        super.onResume();
    }
    private void getListFoodFromLocalDatabase(String searchkey){
        if(searchkey == null){
            mfoodList=LocalDatabase.getInstance(getActivity()).foodDAO().getAllFood();
        }else if (searchkey.isEmpty()){
            mfoodList=LocalDatabase.getInstance(getActivity()).foodDAO().getAllFood();
        }else{
            mfoodList = LocalDatabase.getInstance(getActivity()).foodDAO().findFoodByName("%"+searchkey+"%");
        }
    }
    private void displayFoodItems() {
        getListFoodFromLocalDatabase(searchkey);
        mFoodSearchTabAdapter = new FoodSearchTabAdapter(mfoodList, mMeal, mDiary,this::goToFoodDetail,this::quickAddBtn);
        mFragmentFoodSearchBinding.rcvFoodSearchTab.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentFoodSearchBinding.rcvFoodSearchTab.setAdapter(mFoodSearchTabAdapter);
    }
    private void goToFoodDetail(@NonNull Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_ADD_FOOD_LOG_OBJECT, food);
        bundle.putInt("key_meal",mMeal);
        bundle.putSerializable("key_diary",mDiary);
        GlobalFunction.startActivity(getActivity(), FoodDetailActivity.class, bundle);
    }
    private void quickAddBtn(FoodLog foodLog){
        mDiary.logFood(foodLog);
        Toast.makeText(getActivity(), "Đã thêm vào nhật ký", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void updateUIAfterShowSnackBar() {

    }

    @Override
    protected void initToolbar() {

    }
}
