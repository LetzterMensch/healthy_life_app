// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutActionbarBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView actionBarBackImg;

  @NonNull
  public final TextView actionBarTitle;

  @NonNull
  public final RelativeLayout foodDetailToolbar;

  @NonNull
  public final RelativeLayout layoutToolbar;

  private LayoutActionbarBinding(@NonNull RelativeLayout rootView,
      @NonNull ImageView actionBarBackImg, @NonNull TextView actionBarTitle,
      @NonNull RelativeLayout foodDetailToolbar, @NonNull RelativeLayout layoutToolbar) {
    this.rootView = rootView;
    this.actionBarBackImg = actionBarBackImg;
    this.actionBarTitle = actionBarTitle;
    this.foodDetailToolbar = foodDetailToolbar;
    this.layoutToolbar = layoutToolbar;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutActionbarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutActionbarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_actionbar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutActionbarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.action_bar_back_img;
      ImageView actionBarBackImg = ViewBindings.findChildViewById(rootView, id);
      if (actionBarBackImg == null) {
        break missingId;
      }

      id = R.id.action_bar_title;
      TextView actionBarTitle = ViewBindings.findChildViewById(rootView, id);
      if (actionBarTitle == null) {
        break missingId;
      }

      id = R.id.food_detail_toolbar;
      RelativeLayout foodDetailToolbar = ViewBindings.findChildViewById(rootView, id);
      if (foodDetailToolbar == null) {
        break missingId;
      }

      RelativeLayout layoutToolbar = (RelativeLayout) rootView;

      return new LayoutActionbarBinding((RelativeLayout) rootView, actionBarBackImg, actionBarTitle,
          foodDetailToolbar, layoutToolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}