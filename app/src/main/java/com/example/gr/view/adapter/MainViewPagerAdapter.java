package com.example.gr.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gr.controller.fragment.DashboardFragment;
import com.example.gr.controller.fragment.DiaryFragment;
import com.example.gr.controller.fragment.ExerciseFragment;
import com.example.gr.controller.fragment.ProfileFragment;
import com.example.gr.controller.fragment.SleepFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DashboardFragment();

            case 1:
                return new DiaryFragment();

            case 2:
                return new ExerciseFragment();

            case 3:
                return new SleepFragment();

            case 4:
                return new ProfileFragment();

            default:
                return new DashboardFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
