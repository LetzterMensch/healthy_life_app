// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.gr.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final BottomNavigationView navView;

  @NonNull
  public final LayoutToolbarBinding toolbar;

  @NonNull
  public final ViewPager2 viewpager2;

  private ActivityMainBinding(@NonNull RelativeLayout rootView,
      @NonNull BottomNavigationView navView, @NonNull LayoutToolbarBinding toolbar,
      @NonNull ViewPager2 viewpager2) {
    this.rootView = rootView;
    this.navView = navView;
    this.toolbar = toolbar;
    this.viewpager2 = viewpager2;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.nav_view;
      BottomNavigationView navView = ViewBindings.findChildViewById(rootView, id);
      if (navView == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      LayoutToolbarBinding binding_toolbar = LayoutToolbarBinding.bind(toolbar);

      id = R.id.viewpager_2;
      ViewPager2 viewpager2 = ViewBindings.findChildViewById(rootView, id);
      if (viewpager2 == null) {
        break missingId;
      }

      return new ActivityMainBinding((RelativeLayout) rootView, navView, binding_toolbar,
          viewpager2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
