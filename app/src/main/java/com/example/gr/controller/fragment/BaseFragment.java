package com.example.gr.controller.fragment;

import android.os.Handler;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseFragment extends Fragment {
    protected MaterialDialog progressDialog, alertDialog;

    @Override
    public void onResume() {
        super.onResume();
        initToolbar();
    }

    public void createProgressDialog() {
        progressDialog = new MaterialDialog.Builder(requireActivity())
                .content(R.string.waiting_message)
                .progress(true, 0)
                .build();
    }

    public void showProgressDialog(boolean value) {
        if (value) {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setCancelable(false);
            }
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void createAlertDialog() {
        alertDialog = new MaterialDialog.Builder(requireActivity())
                .title(R.string.app_name)
                .positiveText(R.string.action_ok)
                .cancelable(false)
                .build();
    }

    public void showAlertDialog(String errorMessage) {
        alertDialog.setContent(errorMessage);
        alertDialog.show();
    }

    public void showAlertDialog(@StringRes int resourceId) {
        alertDialog.setContent(resourceId);
        alertDialog.show();
    }

    public void setCancelProgress(boolean isCancel) {
        if (progressDialog != null) {
            progressDialog.setCancelable(isCancel);
        }
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onDestroy();
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
