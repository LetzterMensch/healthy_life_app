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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFoodDetailBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView calories;

  @NonNull
  public final RelativeLayout foodDetailToolbar;

  @NonNull
  public final TextView foodName;

  @NonNull
  public final ImageView imgAdd;

  @NonNull
  public final ImageView imgBack;

  @NonNull
  public final TextView numberOfServings;

  @NonNull
  public final TextView servingSize;

  private ActivityFoodDetailBinding(@NonNull ConstraintLayout rootView, @NonNull TextView calories,
      @NonNull RelativeLayout foodDetailToolbar, @NonNull TextView foodName,
      @NonNull ImageView imgAdd, @NonNull ImageView imgBack, @NonNull TextView numberOfServings,
      @NonNull TextView servingSize) {
    this.rootView = rootView;
    this.calories = calories;
    this.foodDetailToolbar = foodDetailToolbar;
    this.foodName = foodName;
    this.imgAdd = imgAdd;
    this.imgBack = imgBack;
    this.numberOfServings = numberOfServings;
    this.servingSize = servingSize;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFoodDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFoodDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_food_detail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFoodDetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.calories;
      TextView calories = ViewBindings.findChildViewById(rootView, id);
      if (calories == null) {
        break missingId;
      }

      id = R.id.food_detail_toolbar;
      RelativeLayout foodDetailToolbar = ViewBindings.findChildViewById(rootView, id);
      if (foodDetailToolbar == null) {
        break missingId;
      }

      id = R.id.food_name;
      TextView foodName = ViewBindings.findChildViewById(rootView, id);
      if (foodName == null) {
        break missingId;
      }

      id = R.id.img_add;
      ImageView imgAdd = ViewBindings.findChildViewById(rootView, id);
      if (imgAdd == null) {
        break missingId;
      }

      id = R.id.img_back;
      ImageView imgBack = ViewBindings.findChildViewById(rootView, id);
      if (imgBack == null) {
        break missingId;
      }

      id = R.id.number_of_servings;
      TextView numberOfServings = ViewBindings.findChildViewById(rootView, id);
      if (numberOfServings == null) {
        break missingId;
      }

      id = R.id.serving_size;
      TextView servingSize = ViewBindings.findChildViewById(rootView, id);
      if (servingSize == null) {
        break missingId;
      }

      return new ActivityFoodDetailBinding((ConstraintLayout) rootView, calories, foodDetailToolbar,
          foodName, imgAdd, imgBack, numberOfServings, servingSize);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
