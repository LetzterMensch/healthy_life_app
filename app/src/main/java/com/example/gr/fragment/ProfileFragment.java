package com.example.gr.fragment;

import com.example.gr.activity.MainActivity;

public class ProfileFragment extends BaseFragment {
    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Cá nhân");
        }
    }
}
