// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentProfileBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button disconnectButton;

  @NonNull
  public final Button removeButton;

  private FragmentProfileBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button disconnectButton, @NonNull Button removeButton) {
    this.rootView = rootView;
    this.disconnectButton = disconnectButton;
    this.removeButton = removeButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.disconnect_button;
      Button disconnectButton = ViewBindings.findChildViewById(rootView, id);
      if (disconnectButton == null) {
        break missingId;
      }

      id = R.id.remove_button;
      Button removeButton = ViewBindings.findChildViewById(rootView, id);
      if (removeButton == null) {
        break missingId;
      }

      return new FragmentProfileBinding((ConstraintLayout) rootView, disconnectButton,
          removeButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
