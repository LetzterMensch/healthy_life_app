package com.example.gr.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gr.fragment.FoodListSearchFragment;
import com.example.gr.fragment.RecipeListSearchFragment;

import java.io.Serializable;
import java.util.List;

public class SearchFragmentViewPagerAdapter extends FragmentStateAdapter {
    String strKey;
    public SearchFragmentViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return FoodListSearchFragment.newInstance(strKey);
        else return new RecipeListSearchFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
