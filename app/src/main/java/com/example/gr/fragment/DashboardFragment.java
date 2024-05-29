package com.example.gr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gr.activity.MainActivity;
import com.example.gr.databinding.FragmentDashboardBinding;

public class DashboardFragment extends BaseFragment{
    private FragmentDashboardBinding mfragmentDashboardBinding;
//    private ContactAdapter mContactAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mfragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false);

        initUi();
//        initListener();

        return mfragmentDashboardBinding.getRoot();
    }
    private void initUi() {
        mfragmentDashboardBinding.textHome.setText("This is the dashboard screen");
    }
    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Dashboard");
        }
    }
}
