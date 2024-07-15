package com.example.gr.controller.fragment;

import static com.example.gr.controller.activity.SearchForFoodActivity.UPDATE_MEAL_DATA;
import static com.example.gr.controller.activity.SearchForFoodActivity.UPDATE_SEARCH_DATA;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.controller.activity.CreateFoodActivity;
import com.example.gr.controller.activity.FoodDetailActivity;
import com.example.gr.databinding.FragmentFoodSearchBinding;
import com.example.gr.model.Diary;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.utils.constant.Constant;
import com.example.gr.utils.constant.GlobalFunction;
import com.example.gr.view.adapter.FoodSearchTabAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodListSearchFragment extends BaseFragment{
    private FragmentFoodSearchBinding mFragmentFoodSearchBinding;
    private FoodSearchTabAdapter mFoodSearchTabAdapter;
    private String searchkey = "";
    private List<Food> mfoodList;
    private int mMeal;
    private Diary mDiary;


    // Init for quick add
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
                if(searchkey == null){
                    searchkey = "";
                }
                System.out.println("receive intent : "+searchkey);
//                mfoodList = LocalDatabase.getInstance(getActivity()).foodDAO().findFoodByName("%"+searchkey+"%");
//                getListFoodFromFirebase(searchkey);

                displayFoodItems();
            }
            if (UPDATE_MEAL_DATA.equals(intent.getAction())){
                mMeal = intent.getIntExtra("key_meal",0);
                System.out.println("receive intent : "+mMeal);
                if(searchkey == null){
                    searchkey = "";
                }
                mFoodSearchTabAdapter.setMeal(mMeal);
                displayFoodItems();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFoodSearchBinding = FragmentFoodSearchBinding.inflate(inflater, container, false);
        // Initialize and register the BroadcastReceiver
        mfoodList = new ArrayList<>();
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
        mFoodSearchTabAdapter = new FoodSearchTabAdapter(mfoodList, mMeal, mDiary,this::goToFoodDetail,this::quickAddBtn);
        mFragmentFoodSearchBinding.rcvFoodSearchTab.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentFoodSearchBinding.rcvFoodSearchTab.setAdapter(mFoodSearchTabAdapter);
        mFragmentFoodSearchBinding.btnCreateFood.setOnClickListener(v-> goToCreateFoodActivity());
//        initListener();
//        displayFoodItems();
        return mFragmentFoodSearchBinding.getRoot();
    }
    private void goToCreateFoodActivity(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_ADD_FOOD_OBJECT, mDiary);
        bundle.putInt("key_meal",mMeal);
        GlobalFunction.startActivity(getActivity(), CreateFoodActivity.class, bundle);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the receiver
        requireActivity().unregisterReceiver(receiver);
    }
    @Override
    public void onResume() {
//        displayFoodItems();
        super.onResume();
    }
    private void checkFoodData(){
        for (Food mFood : mfoodList) {
            if(((mFood.getCarb() + mFood.getProtein())*4 + mFood.getFat()*9 > (mFood.getCalories() + 15))
                || ((mFood.getCarb() + mFood.getProtein())*4 + mFood.getFat()*9 < (mFood.getCalories() - 25))){

                System.out.println(mFood.getName()+"##");
            }
        }
    }
    private void getListFoodFromFirebase() {
        if (getActivity() == null) {
            return;
        }

        if (searchkey.isEmpty()){
            Query query = ControllerApplication.getApp().getFoodDatabaseReference()
                    .orderByChild("name")
                    .startAt(searchkey.trim())
                    .endAt(searchkey.trim() + "\uf8ff")
                    .limitToFirst(10);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mfoodList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mfoodList.add(snapshot.getValue(Food.class));
                    }
                    mFoodSearchTabAdapter.setmListFoods(mfoodList);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(requireActivity(),"Lỗi đã xảy ra, vui lòng thử lại sau",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            ControllerApplication.getApp().getFoodDatabaseReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println("inside data change");
                    mfoodList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Food food = dataSnapshot.getValue(Food.class);
                        if (food == null) {
                            return;
                        }

                        if (food.getName().toLowerCase().trim().contains(searchkey)) {
                            mfoodList.add(food);
                        }
                    }
                    mFoodSearchTabAdapter.setmListFoods(mfoodList);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireActivity(),"Lỗi đã xảy ra, vui lòng thử lại sau",Toast.LENGTH_SHORT).show();
                }
            });
        }

        for (Food food : mfoodList
             ) {
            System.out.println(food.getName());
        }
    }
    private void getListFoodFromLocalDatabase(String searchkey){
        if(searchkey == null){
            mfoodList=LocalDatabase.getInstance(getActivity()).foodDAO().getAllFood();
        }else if (searchkey.isEmpty()){
            mfoodList=LocalDatabase.getInstance(getActivity()).foodDAO().getAllFood();
//            checkFoodData();
        }else{
            mfoodList = LocalDatabase.getInstance(getActivity()).foodDAO().findFoodByName("%"+searchkey+"%");
        }
        mFoodSearchTabAdapter.setmListFoods(mfoodList);
    }
    private void displayFoodItems() {
        createProgressDialog();
        showProgressDialog(true);
        getListFoodFromFirebase();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressDialog(false);
            }
        },1000);
//        getListFoodFromLocalDatabase(searchkey);
//        mFoodSearchTabAdapter = new FoodSearchTabAdapter(mfoodList, mMeal, mDiary,this::goToFoodDetail,this::quickAddBtn);
//        mFragmentFoodSearchBinding.rcvFoodSearchTab.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mFragmentFoodSearchBinding.rcvFoodSearchTab.setAdapter(mFoodSearchTabAdapter);
    }
    private void goToFoodDetail(@NonNull Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_ADD_FOOD_LOG_OBJECT, food);
        bundle.putInt("key_meal",mMeal);
        bundle.putSerializable("key_diary",mDiary);
        GlobalFunction.startActivity(getActivity(), FoodDetailActivity.class, bundle);
    }
    private void quickAddBtn(FoodLog foodLog){
        if(LocalDatabase.getInstance(ControllerApplication.getContext()).foodDAO().getFoodById(foodLog.getFoodId()) == null){
            LocalDatabase.getInstance(ControllerApplication.getContext()).foodDAO().insertFood(foodLog.getFood());
        }
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
