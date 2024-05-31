package com.example.gr.fragment;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        initToolbar();
    }

    protected void showTransientSnackbar(int resource) {
        Snackbar snackbar = Snackbar.make(this.getView(), resource, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    protected abstract void initToolbar();
}
