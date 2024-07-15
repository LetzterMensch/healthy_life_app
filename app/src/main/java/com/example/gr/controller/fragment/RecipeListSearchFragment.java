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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.gr.ControllerApplication;
import com.example.gr.controller.activity.ScrapeRecipeActivity;
import com.example.gr.controller.activity.RecipeDetailActivity;
import com.example.gr.model.Diary;
import com.example.gr.model.Food;
import com.example.gr.view.adapter.RecipeSearchTabAdapter;
import com.example.gr.utils.constant.Constant;
import com.example.gr.utils.constant.GlobalFunction;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.FragmentRecipeSearchBinding;
import com.example.gr.model.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipeListSearchFragment extends BaseFragment {
    private FragmentRecipeSearchBinding mFragmentRecipeSearchBinding;
    private RecipeSearchTabAdapter mRecipeSearchTabAdapter;
    private String searchkey = "";
    private List<Recipe> mRecipeList;
    private int mMeal;
    private Diary mDiary;

    public static RecipeListSearchFragment newInstance(int meal, Diary diary) {
        RecipeListSearchFragment recipeListSearchFragment = new RecipeListSearchFragment();
        Bundle args = new Bundle();
        args.putInt("key_meal", meal);
        args.putSerializable("key_diary", diary);
        recipeListSearchFragment.setArguments(args);
        return recipeListSearchFragment;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UPDATE_SEARCH_DATA.equals(intent.getAction())) {
                searchkey = intent.getStringExtra("key");
                mRecipeList = LocalDatabase.getInstance(getActivity()).recipeDAO().findRecipeByName("%" + searchkey + "%");
                displayRecipeItems();
            }
            if (UPDATE_MEAL_DATA.equals(intent.getAction())) {
                mMeal = intent.getIntExtra("key_meal", 0);
                System.out.println("receive intent : " + mMeal);
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
        if (bundle != null) {
            mMeal = bundle.getInt("key_meal");
            mDiary = (Diary) bundle.getSerializable("key_diary");
        }
        mRecipeList = new ArrayList<>();
        initUi();

        return mFragmentRecipeSearchBinding.getRoot();
    }

    private void initUi() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentRecipeSearchBinding.rcvRecipeSearchTab.setLayoutManager(gridLayoutManager);
        mRecipeSearchTabAdapter = new RecipeSearchTabAdapter(mRecipeList, this::goToRecipeDetail);
        mFragmentRecipeSearchBinding.rcvRecipeSearchTab.setAdapter(mRecipeSearchTabAdapter);
        mFragmentRecipeSearchBinding.btnImportRecipe.setOnClickListener(v -> {
            goToImportRecipeActivity();
        });
        displayRecipeItems();
    }

    private void getListRecipeFromFirebase() {
        if (getActivity() == null) {
            return;
        }

        if (searchkey.isEmpty()) {
            Query query = ControllerApplication.getApp().getRecipeDatabaseReference()
                    .orderByChild("name")
                    .startAt(searchkey.trim())
                    .endAt(searchkey.trim() + "\uf8ff")
                    .limitToFirst(10);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mRecipeList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mRecipeList.add(snapshot.getValue(Recipe.class));
                    }
                    mRecipeSearchTabAdapter.setmRecipeList(mRecipeList);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(requireActivity(), "Lỗi đã xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ControllerApplication.getApp().getRecipeDatabaseReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println("inside data change");
                    mRecipeList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Recipe recipe = dataSnapshot.getValue(Recipe.class);
                        if (recipe == null) {
                            return;
                        }

                        if (recipe.getName().toLowerCase().trim().contains(searchkey)) {
                            mRecipeList.add(recipe);
                        }
                    }
                    mRecipeSearchTabAdapter.setmRecipeList(mRecipeList);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireActivity(), "Lỗi đã xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            });
        }

        for (Recipe recipe : mRecipeList
        ) {
            System.out.println(recipe.getName());
        }
    }

    private void getListRecipeFromLocalDatabase(String searchkey) {
        if (getActivity() == null) {
            return;
        }
        if (searchkey == null) {
            mRecipeList = LocalDatabase.getInstance(getActivity()).recipeDAO().getAllRecipe();
        } else if (searchkey.isEmpty()) {
            mRecipeList = LocalDatabase.getInstance(getActivity()).recipeDAO().getAllRecipe();
        } else {
            mRecipeList = LocalDatabase.getInstance(getActivity()).recipeDAO().findRecipeByName("%" + searchkey + "%");
        }
    }

    private void displayRecipeItems() {
//        getListRecipeFromLocalDatabase(searchkey);
        getListRecipeFromFirebase();
        mFragmentRecipeSearchBinding.rcvRecipeSearchTab.setAdapter(mRecipeSearchTabAdapter);
    }

    @Override
    public void onResume() {
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
        bundle.putInt("key_meal", mMeal);
        GlobalFunction.startActivity(getActivity(), RecipeDetailActivity.class, bundle);
    }
}
