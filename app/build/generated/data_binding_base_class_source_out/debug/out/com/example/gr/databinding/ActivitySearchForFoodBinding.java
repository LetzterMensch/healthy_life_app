// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.gr.R;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySearchForFoodBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final EditText edtSearchName;

  @NonNull
  public final ImageView imgBack;

  @NonNull
  public final ImageView imgSearch;

  @NonNull
  public final RelativeLayout layoutSearchBar;

  @NonNull
  public final RelativeLayout layoutSpinner;

  @NonNull
  public final Spinner mealSpinner;

  @NonNull
  public final TabLayout searchTabLayout;

  @NonNull
  public final ViewPager2 viewPager2;

  private ActivitySearchForFoodBinding(@NonNull ConstraintLayout rootView,
      @NonNull EditText edtSearchName, @NonNull ImageView imgBack, @NonNull ImageView imgSearch,
      @NonNull RelativeLayout layoutSearchBar, @NonNull RelativeLayout layoutSpinner,
      @NonNull Spinner mealSpinner, @NonNull TabLayout searchTabLayout,
      @NonNull ViewPager2 viewPager2) {
    this.rootView = rootView;
    this.edtSearchName = edtSearchName;
    this.imgBack = imgBack;
    this.imgSearch = imgSearch;
    this.layoutSearchBar = layoutSearchBar;
    this.layoutSpinner = layoutSpinner;
    this.mealSpinner = mealSpinner;
    this.searchTabLayout = searchTabLayout;
    this.viewPager2 = viewPager2;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySearchForFoodBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySearchForFoodBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_search_for_food, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySearchForFoodBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.edt_search_name;
      EditText edtSearchName = ViewBindings.findChildViewById(rootView, id);
      if (edtSearchName == null) {
        break missingId;
      }

      id = R.id.img_back;
      ImageView imgBack = ViewBindings.findChildViewById(rootView, id);
      if (imgBack == null) {
        break missingId;
      }

      id = R.id.img_search;
      ImageView imgSearch = ViewBindings.findChildViewById(rootView, id);
      if (imgSearch == null) {
        break missingId;
      }

      id = R.id.layout_search_bar;
      RelativeLayout layoutSearchBar = ViewBindings.findChildViewById(rootView, id);
      if (layoutSearchBar == null) {
        break missingId;
      }

      id = R.id.layout_spinner;
      RelativeLayout layoutSpinner = ViewBindings.findChildViewById(rootView, id);
      if (layoutSpinner == null) {
        break missingId;
      }

      id = R.id.meal_spinner;
      Spinner mealSpinner = ViewBindings.findChildViewById(rootView, id);
      if (mealSpinner == null) {
        break missingId;
      }

      id = R.id.search_tabLayout;
      TabLayout searchTabLayout = ViewBindings.findChildViewById(rootView, id);
      if (searchTabLayout == null) {
        break missingId;
      }

      id = R.id.viewPager2;
      ViewPager2 viewPager2 = ViewBindings.findChildViewById(rootView, id);
      if (viewPager2 == null) {
        break missingId;
      }

      return new ActivitySearchForFoodBinding((ConstraintLayout) rootView, edtSearchName, imgBack,
          imgSearch, layoutSearchBar, layoutSpinner, mealSpinner, searchTabLayout, viewPager2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
