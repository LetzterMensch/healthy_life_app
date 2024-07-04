// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCreateFoodBinding implements ViewBinding {
  @NonNull
  private final NestedScrollView rootView;

  @NonNull
  public final LinearLayout addNewFoodIngredients;

  @NonNull
  public final Button btnSaveNewFood;

  @NonNull
  public final TextView calories;

  @NonNull
  public final LinearProgressIndicator carbIndicator;

  @NonNull
  public final RelativeLayout createFoodToolbar;

  @NonNull
  public final EditText editNewFoodName;

  @NonNull
  public final EditText edtSearchName;

  @NonNull
  public final LinearProgressIndicator fatIndicator;

  @NonNull
  public final TextView foodCarb;

  @NonNull
  public final TextView foodFat;

  @NonNull
  public final TextView foodProtein;

  @NonNull
  public final ImageView imgClose;

  @NonNull
  public final ImageView imgSearch;

  @NonNull
  public final TextView labelAddIngredients;

  @NonNull
  public final TextView labelNutritionInfo;

  @NonNull
  public final RelativeLayout layoutSearchBar;

  @NonNull
  public final RelativeLayout layoutSpinner;

  @NonNull
  public final CardView newFoodName;

  @NonNull
  public final LinearLayout newFoodNutritionInfo;

  @NonNull
  public final LinearProgressIndicator proteinIndicator;

  @NonNull
  public final RecyclerView rcvFoodList;

  @NonNull
  public final RecyclerView rcvIngredients;

  @NonNull
  public final TextView tvNewFoodIngredients;

  @NonNull
  public final TextView tvNewFoodName;

  private ActivityCreateFoodBinding(@NonNull NestedScrollView rootView,
      @NonNull LinearLayout addNewFoodIngredients, @NonNull Button btnSaveNewFood,
      @NonNull TextView calories, @NonNull LinearProgressIndicator carbIndicator,
      @NonNull RelativeLayout createFoodToolbar, @NonNull EditText editNewFoodName,
      @NonNull EditText edtSearchName, @NonNull LinearProgressIndicator fatIndicator,
      @NonNull TextView foodCarb, @NonNull TextView foodFat, @NonNull TextView foodProtein,
      @NonNull ImageView imgClose, @NonNull ImageView imgSearch,
      @NonNull TextView labelAddIngredients, @NonNull TextView labelNutritionInfo,
      @NonNull RelativeLayout layoutSearchBar, @NonNull RelativeLayout layoutSpinner,
      @NonNull CardView newFoodName, @NonNull LinearLayout newFoodNutritionInfo,
      @NonNull LinearProgressIndicator proteinIndicator, @NonNull RecyclerView rcvFoodList,
      @NonNull RecyclerView rcvIngredients, @NonNull TextView tvNewFoodIngredients,
      @NonNull TextView tvNewFoodName) {
    this.rootView = rootView;
    this.addNewFoodIngredients = addNewFoodIngredients;
    this.btnSaveNewFood = btnSaveNewFood;
    this.calories = calories;
    this.carbIndicator = carbIndicator;
    this.createFoodToolbar = createFoodToolbar;
    this.editNewFoodName = editNewFoodName;
    this.edtSearchName = edtSearchName;
    this.fatIndicator = fatIndicator;
    this.foodCarb = foodCarb;
    this.foodFat = foodFat;
    this.foodProtein = foodProtein;
    this.imgClose = imgClose;
    this.imgSearch = imgSearch;
    this.labelAddIngredients = labelAddIngredients;
    this.labelNutritionInfo = labelNutritionInfo;
    this.layoutSearchBar = layoutSearchBar;
    this.layoutSpinner = layoutSpinner;
    this.newFoodName = newFoodName;
    this.newFoodNutritionInfo = newFoodNutritionInfo;
    this.proteinIndicator = proteinIndicator;
    this.rcvFoodList = rcvFoodList;
    this.rcvIngredients = rcvIngredients;
    this.tvNewFoodIngredients = tvNewFoodIngredients;
    this.tvNewFoodName = tvNewFoodName;
  }

  @Override
  @NonNull
  public NestedScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCreateFoodBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCreateFoodBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_create_food, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCreateFoodBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.add_new_food_ingredients;
      LinearLayout addNewFoodIngredients = ViewBindings.findChildViewById(rootView, id);
      if (addNewFoodIngredients == null) {
        break missingId;
      }

      id = R.id.btn_save_new_food;
      Button btnSaveNewFood = ViewBindings.findChildViewById(rootView, id);
      if (btnSaveNewFood == null) {
        break missingId;
      }

      id = R.id.calories;
      TextView calories = ViewBindings.findChildViewById(rootView, id);
      if (calories == null) {
        break missingId;
      }

      id = R.id.carb_indicator;
      LinearProgressIndicator carbIndicator = ViewBindings.findChildViewById(rootView, id);
      if (carbIndicator == null) {
        break missingId;
      }

      id = R.id.create_food_toolbar;
      RelativeLayout createFoodToolbar = ViewBindings.findChildViewById(rootView, id);
      if (createFoodToolbar == null) {
        break missingId;
      }

      id = R.id.edit_new_food_name;
      EditText editNewFoodName = ViewBindings.findChildViewById(rootView, id);
      if (editNewFoodName == null) {
        break missingId;
      }

      id = R.id.edt_search_name;
      EditText edtSearchName = ViewBindings.findChildViewById(rootView, id);
      if (edtSearchName == null) {
        break missingId;
      }

      id = R.id.fat_indicator;
      LinearProgressIndicator fatIndicator = ViewBindings.findChildViewById(rootView, id);
      if (fatIndicator == null) {
        break missingId;
      }

      id = R.id.food_carb;
      TextView foodCarb = ViewBindings.findChildViewById(rootView, id);
      if (foodCarb == null) {
        break missingId;
      }

      id = R.id.food_fat;
      TextView foodFat = ViewBindings.findChildViewById(rootView, id);
      if (foodFat == null) {
        break missingId;
      }

      id = R.id.food_protein;
      TextView foodProtein = ViewBindings.findChildViewById(rootView, id);
      if (foodProtein == null) {
        break missingId;
      }

      id = R.id.img_close;
      ImageView imgClose = ViewBindings.findChildViewById(rootView, id);
      if (imgClose == null) {
        break missingId;
      }

      id = R.id.img_search;
      ImageView imgSearch = ViewBindings.findChildViewById(rootView, id);
      if (imgSearch == null) {
        break missingId;
      }

      id = R.id.label_add_ingredients;
      TextView labelAddIngredients = ViewBindings.findChildViewById(rootView, id);
      if (labelAddIngredients == null) {
        break missingId;
      }

      id = R.id.label_nutrition_info;
      TextView labelNutritionInfo = ViewBindings.findChildViewById(rootView, id);
      if (labelNutritionInfo == null) {
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

      id = R.id.new_food_name;
      CardView newFoodName = ViewBindings.findChildViewById(rootView, id);
      if (newFoodName == null) {
        break missingId;
      }

      id = R.id.new_food_nutrition_info;
      LinearLayout newFoodNutritionInfo = ViewBindings.findChildViewById(rootView, id);
      if (newFoodNutritionInfo == null) {
        break missingId;
      }

      id = R.id.protein_indicator;
      LinearProgressIndicator proteinIndicator = ViewBindings.findChildViewById(rootView, id);
      if (proteinIndicator == null) {
        break missingId;
      }

      id = R.id.rcv_food_list;
      RecyclerView rcvFoodList = ViewBindings.findChildViewById(rootView, id);
      if (rcvFoodList == null) {
        break missingId;
      }

      id = R.id.rcv_ingredients;
      RecyclerView rcvIngredients = ViewBindings.findChildViewById(rootView, id);
      if (rcvIngredients == null) {
        break missingId;
      }

      id = R.id.tv_new_food_ingredients;
      TextView tvNewFoodIngredients = ViewBindings.findChildViewById(rootView, id);
      if (tvNewFoodIngredients == null) {
        break missingId;
      }

      id = R.id.tv_new_food_name;
      TextView tvNewFoodName = ViewBindings.findChildViewById(rootView, id);
      if (tvNewFoodName == null) {
        break missingId;
      }

      return new ActivityCreateFoodBinding((NestedScrollView) rootView, addNewFoodIngredients,
          btnSaveNewFood, calories, carbIndicator, createFoodToolbar, editNewFoodName,
          edtSearchName, fatIndicator, foodCarb, foodFat, foodProtein, imgClose, imgSearch,
          labelAddIngredients, labelNutritionInfo, layoutSearchBar, layoutSpinner, newFoodName,
          newFoodNutritionInfo, proteinIndicator, rcvFoodList, rcvIngredients, tvNewFoodIngredients,
          tvNewFoodName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}