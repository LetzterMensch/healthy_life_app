// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLogInBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatButton btnLogIn;

  @NonNull
  public final LinearLayout layoutBtnSignUp;

  @NonNull
  public final EditText logInEditEmail;

  @NonNull
  public final EditText logInEditPassword;

  private ActivityLogInBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatButton btnLogIn, @NonNull LinearLayout layoutBtnSignUp,
      @NonNull EditText logInEditEmail, @NonNull EditText logInEditPassword) {
    this.rootView = rootView;
    this.btnLogIn = btnLogIn;
    this.layoutBtnSignUp = layoutBtnSignUp;
    this.logInEditEmail = logInEditEmail;
    this.logInEditPassword = logInEditPassword;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLogInBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLogInBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_log_in, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLogInBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_log_in;
      AppCompatButton btnLogIn = ViewBindings.findChildViewById(rootView, id);
      if (btnLogIn == null) {
        break missingId;
      }

      id = R.id.layout_btn_sign_up;
      LinearLayout layoutBtnSignUp = ViewBindings.findChildViewById(rootView, id);
      if (layoutBtnSignUp == null) {
        break missingId;
      }

      id = R.id.log_in_edit_email;
      EditText logInEditEmail = ViewBindings.findChildViewById(rootView, id);
      if (logInEditEmail == null) {
        break missingId;
      }

      id = R.id.log_in_edit_password;
      EditText logInEditPassword = ViewBindings.findChildViewById(rootView, id);
      if (logInEditPassword == null) {
        break missingId;
      }

      return new ActivityLogInBinding((ConstraintLayout) rootView, btnLogIn, layoutBtnSignUp,
          logInEditEmail, logInEditPassword);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}