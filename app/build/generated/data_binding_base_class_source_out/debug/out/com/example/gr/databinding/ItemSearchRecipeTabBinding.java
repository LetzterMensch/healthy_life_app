// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemSearchRecipeTabBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView imgFood;

  @NonNull
  public final LinearLayout layoutItem;

  @NonNull
  public final TextView recipeName;

  private ItemSearchRecipeTabBinding(@NonNull LinearLayout rootView, @NonNull ImageView imgFood,
      @NonNull LinearLayout layoutItem, @NonNull TextView recipeName) {
    this.rootView = rootView;
    this.imgFood = imgFood;
    this.layoutItem = layoutItem;
    this.recipeName = recipeName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemSearchRecipeTabBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemSearchRecipeTabBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_search_recipe_tab, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemSearchRecipeTabBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.img_food;
      ImageView imgFood = ViewBindings.findChildViewById(rootView, id);
      if (imgFood == null) {
        break missingId;
      }

      LinearLayout layoutItem = (LinearLayout) rootView;

      id = R.id.recipe_name;
      TextView recipeName = ViewBindings.findChildViewById(rootView, id);
      if (recipeName == null) {
        break missingId;
      }

      return new ItemSearchRecipeTabBinding((LinearLayout) rootView, imgFood, layoutItem,
          recipeName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}