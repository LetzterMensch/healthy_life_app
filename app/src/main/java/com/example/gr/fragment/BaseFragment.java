package com.example.gr.fragment;

import android.os.Handler;

import androidx.fragment.app.Fragment;

import com.example.gr.R;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        initToolbar();
    }

    protected void showTransientSnackbar(int resource) {
        Snackbar snackbar = Snackbar.make(this.getView(), resource, 2000);
        snackbar.setAnchorView(getActivity().findViewById(R.id.nav_view));
        snackbar.show();
        // Ví dụ cập nhật giao diện của fragment đầu tiên sau 3 giây
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUIAfterShowSnackBar();
            }
        },snackbar.getDuration()+500);
    }
    protected abstract void updateUIAfterShowSnackBar();
    protected abstract void initToolbar();
}
