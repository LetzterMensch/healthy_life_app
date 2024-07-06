package com.example.gr.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gr.controller.fragment.FoodListSearchFragment;
import com.example.gr.controller.fragment.RecipeListSearchFragment;
import com.example.gr.model.Diary;

public class SearchFragmentViewPagerAdapter extends FragmentStateAdapter {
    private int meal;
    private Diary diary;
    public SearchFragmentViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int meal, Diary diary) {
        super(fragmentActivity);
        this.meal = meal;
        this.diary = diary;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return FoodListSearchFragment.newInstance(meal,diary);
            case 1:
                return RecipeListSearchFragment.newInstance(meal, diary);
            default:
                return FoodListSearchFragment.newInstance(meal,diary);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
