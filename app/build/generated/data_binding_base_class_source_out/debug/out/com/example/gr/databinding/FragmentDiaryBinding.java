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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDiaryBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final CardView breakfastCard;

  @NonNull
  public final TextView btnBreakfastAdd;

  @NonNull
  public final TextView btnDinnerAdd;

  @NonNull
  public final TextView btnLunchAdd;

  @NonNull
  public final TextView btnSnackAdd;

  @NonNull
  public final TextView calBreakfast;

  @NonNull
  public final TextView calBurnt;

  @NonNull
  public final CardView calCardContainer;

  @NonNull
  public final TextView calDinner;

  @NonNull
  public final TextView calGoal;

  @NonNull
  public final TextView calInput;

  @NonNull
  public final TextView calLunch;

  @NonNull
  public final TextView calRemain;

  @NonNull
  public final TextView calSnack;

  @NonNull
  public final RelativeLayout calendarToolbar;

  @NonNull
  public final LinearProgressIndicator carbIndicator;

  @NonNull
  public final TextView dashboardCarb;

  @NonNull
  public final TextView dashboardFat;

  @NonNull
  public final TextView dashboardProtein;

  @NonNull
  public final TextView date;

  @NonNull
  public final CardView dinnerCard;

  @NonNull
  public final TextView editSearchName;

  @NonNull
  public final LinearProgressIndicator fatIndicator;

  @NonNull
  public final ImageView imgBack;

  @NonNull
  public final ImageView imgNext;

  @NonNull
  public final ImageView imgSearch;

  @NonNull
  public final RelativeLayout layoutSearch;

  @NonNull
  public final CardView lunchCard;

  @NonNull
  public final LinearProgressIndicator proteinIndicator;

  @NonNull
  public final RecyclerView rcvBreakfastFood;

  @NonNull
  public final RecyclerView rcvDinnerFood;

  @NonNull
  public final RecyclerView rcvLunchFood;

  @NonNull
  public final RecyclerView rcvSnackFood;

  @NonNull
  public final CardView snackCard;

  @NonNull
  public final AppCompatTextView titleCaloriesRemain;

  @NonNull
  public final TextView tvAddToCart;

  private FragmentDiaryBinding(@NonNull ConstraintLayout rootView, @NonNull CardView breakfastCard,
      @NonNull TextView btnBreakfastAdd, @NonNull TextView btnDinnerAdd,
      @NonNull TextView btnLunchAdd, @NonNull TextView btnSnackAdd, @NonNull TextView calBreakfast,
      @NonNull TextView calBurnt, @NonNull CardView calCardContainer, @NonNull TextView calDinner,
      @NonNull TextView calGoal, @NonNull TextView calInput, @NonNull TextView calLunch,
      @NonNull TextView calRemain, @NonNull TextView calSnack,
      @NonNull RelativeLayout calendarToolbar, @NonNull LinearProgressIndicator carbIndicator,
      @NonNull TextView dashboardCarb, @NonNull TextView dashboardFat,
      @NonNull TextView dashboardProtein, @NonNull TextView date, @NonNull CardView dinnerCard,
      @NonNull TextView editSearchName, @NonNull LinearProgressIndicator fatIndicator,
      @NonNull ImageView imgBack, @NonNull ImageView imgNext, @NonNull ImageView imgSearch,
      @NonNull RelativeLayout layoutSearch, @NonNull CardView lunchCard,
      @NonNull LinearProgressIndicator proteinIndicator, @NonNull RecyclerView rcvBreakfastFood,
      @NonNull RecyclerView rcvDinnerFood, @NonNull RecyclerView rcvLunchFood,
      @NonNull RecyclerView rcvSnackFood, @NonNull CardView snackCard,
      @NonNull AppCompatTextView titleCaloriesRemain, @NonNull TextView tvAddToCart) {
    this.rootView = rootView;
    this.breakfastCard = breakfastCard;
    this.btnBreakfastAdd = btnBreakfastAdd;
    this.btnDinnerAdd = btnDinnerAdd;
    this.btnLunchAdd = btnLunchAdd;
    this.btnSnackAdd = btnSnackAdd;
    this.calBreakfast = calBreakfast;
    this.calBurnt = calBurnt;
    this.calCardContainer = calCardContainer;
    this.calDinner = calDinner;
    this.calGoal = calGoal;
    this.calInput = calInput;
    this.calLunch = calLunch;
    this.calRemain = calRemain;
    this.calSnack = calSnack;
    this.calendarToolbar = calendarToolbar;
    this.carbIndicator = carbIndicator;
    this.dashboardCarb = dashboardCarb;
    this.dashboardFat = dashboardFat;
    this.dashboardProtein = dashboardProtein;
    this.date = date;
    this.dinnerCard = dinnerCard;
    this.editSearchName = editSearchName;
    this.fatIndicator = fatIndicator;
    this.imgBack = imgBack;
    this.imgNext = imgNext;
    this.imgSearch = imgSearch;
    this.layoutSearch = layoutSearch;
    this.lunchCard = lunchCard;
    this.proteinIndicator = proteinIndicator;
    this.rcvBreakfastFood = rcvBreakfastFood;
    this.rcvDinnerFood = rcvDinnerFood;
    this.rcvLunchFood = rcvLunchFood;
    this.rcvSnackFood = rcvSnackFood;
    this.snackCard = snackCard;
    this.titleCaloriesRemain = titleCaloriesRemain;
    this.tvAddToCart = tvAddToCart;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDiaryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDiaryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_diary, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDiaryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.breakfast_card;
      CardView breakfastCard = ViewBindings.findChildViewById(rootView, id);
      if (breakfastCard == null) {
        break missingId;
      }

      id = R.id.btn_breakfast_add;
      TextView btnBreakfastAdd = ViewBindings.findChildViewById(rootView, id);
      if (btnBreakfastAdd == null) {
        break missingId;
      }

      id = R.id.btn_dinner_add;
      TextView btnDinnerAdd = ViewBindings.findChildViewById(rootView, id);
      if (btnDinnerAdd == null) {
        break missingId;
      }

      id = R.id.btn_lunch_add;
      TextView btnLunchAdd = ViewBindings.findChildViewById(rootView, id);
      if (btnLunchAdd == null) {
        break missingId;
      }

      id = R.id.btn_snack_add;
      TextView btnSnackAdd = ViewBindings.findChildViewById(rootView, id);
      if (btnSnackAdd == null) {
        break missingId;
      }

      id = R.id.cal_breakfast;
      TextView calBreakfast = ViewBindings.findChildViewById(rootView, id);
      if (calBreakfast == null) {
        break missingId;
      }

      id = R.id.cal_burnt;
      TextView calBurnt = ViewBindings.findChildViewById(rootView, id);
      if (calBurnt == null) {
        break missingId;
      }

      id = R.id.cal_card_container;
      CardView calCardContainer = ViewBindings.findChildViewById(rootView, id);
      if (calCardContainer == null) {
        break missingId;
      }

      id = R.id.cal_dinner;
      TextView calDinner = ViewBindings.findChildViewById(rootView, id);
      if (calDinner == null) {
        break missingId;
      }

      id = R.id.cal_goal;
      TextView calGoal = ViewBindings.findChildViewById(rootView, id);
      if (calGoal == null) {
        break missingId;
      }

      id = R.id.cal_input;
      TextView calInput = ViewBindings.findChildViewById(rootView, id);
      if (calInput == null) {
        break missingId;
      }

      id = R.id.cal_lunch;
      TextView calLunch = ViewBindings.findChildViewById(rootView, id);
      if (calLunch == null) {
        break missingId;
      }

      id = R.id.cal_remain;
      TextView calRemain = ViewBindings.findChildViewById(rootView, id);
      if (calRemain == null) {
        break missingId;
      }

      id = R.id.cal_snack;
      TextView calSnack = ViewBindings.findChildViewById(rootView, id);
      if (calSnack == null) {
        break missingId;
      }

      id = R.id.calendar_toolbar;
      RelativeLayout calendarToolbar = ViewBindings.findChildViewById(rootView, id);
      if (calendarToolbar == null) {
        break missingId;
      }

      id = R.id.carb_indicator;
      LinearProgressIndicator carbIndicator = ViewBindings.findChildViewById(rootView, id);
      if (carbIndicator == null) {
        break missingId;
      }

      id = R.id.dashboard_carb;
      TextView dashboardCarb = ViewBindings.findChildViewById(rootView, id);
      if (dashboardCarb == null) {
        break missingId;
      }

      id = R.id.dashboard_fat;
      TextView dashboardFat = ViewBindings.findChildViewById(rootView, id);
      if (dashboardFat == null) {
        break missingId;
      }

      id = R.id.dashboard_protein;
      TextView dashboardProtein = ViewBindings.findChildViewById(rootView, id);
      if (dashboardProtein == null) {
        break missingId;
      }

      id = R.id.date;
      TextView date = ViewBindings.findChildViewById(rootView, id);
      if (date == null) {
        break missingId;
      }

      id = R.id.dinner_card;
      CardView dinnerCard = ViewBindings.findChildViewById(rootView, id);
      if (dinnerCard == null) {
        break missingId;
      }

      id = R.id.edit_search_name;
      TextView editSearchName = ViewBindings.findChildViewById(rootView, id);
      if (editSearchName == null) {
        break missingId;
      }

      id = R.id.fat_indicator;
      LinearProgressIndicator fatIndicator = ViewBindings.findChildViewById(rootView, id);
      if (fatIndicator == null) {
        break missingId;
      }

      id = R.id.img_back;
      ImageView imgBack = ViewBindings.findChildViewById(rootView, id);
      if (imgBack == null) {
        break missingId;
      }

      id = R.id.img_next;
      ImageView imgNext = ViewBindings.findChildViewById(rootView, id);
      if (imgNext == null) {
        break missingId;
      }

      id = R.id.img_search;
      ImageView imgSearch = ViewBindings.findChildViewById(rootView, id);
      if (imgSearch == null) {
        break missingId;
      }

      id = R.id.layout_search;
      RelativeLayout layoutSearch = ViewBindings.findChildViewById(rootView, id);
      if (layoutSearch == null) {
        break missingId;
      }

      id = R.id.lunch_card;
      CardView lunchCard = ViewBindings.findChildViewById(rootView, id);
      if (lunchCard == null) {
        break missingId;
      }

      id = R.id.protein_indicator;
      LinearProgressIndicator proteinIndicator = ViewBindings.findChildViewById(rootView, id);
      if (proteinIndicator == null) {
        break missingId;
      }

      id = R.id.rcv_breakfast_food;
      RecyclerView rcvBreakfastFood = ViewBindings.findChildViewById(rootView, id);
      if (rcvBreakfastFood == null) {
        break missingId;
      }

      id = R.id.rcv_dinner_food;
      RecyclerView rcvDinnerFood = ViewBindings.findChildViewById(rootView, id);
      if (rcvDinnerFood == null) {
        break missingId;
      }

      id = R.id.rcv_lunch_food;
      RecyclerView rcvLunchFood = ViewBindings.findChildViewById(rootView, id);
      if (rcvLunchFood == null) {
        break missingId;
      }

      id = R.id.rcv_snack_food;
      RecyclerView rcvSnackFood = ViewBindings.findChildViewById(rootView, id);
      if (rcvSnackFood == null) {
        break missingId;
      }

      id = R.id.snack_card;
      CardView snackCard = ViewBindings.findChildViewById(rootView, id);
      if (snackCard == null) {
        break missingId;
      }

      id = R.id.title_calories_remain;
      AppCompatTextView titleCaloriesRemain = ViewBindings.findChildViewById(rootView, id);
      if (titleCaloriesRemain == null) {
        break missingId;
      }

      id = R.id.tv_add_to_cart;
      TextView tvAddToCart = ViewBindings.findChildViewById(rootView, id);
      if (tvAddToCart == null) {
        break missingId;
      }

      return new FragmentDiaryBinding((ConstraintLayout) rootView, breakfastCard, btnBreakfastAdd,
          btnDinnerAdd, btnLunchAdd, btnSnackAdd, calBreakfast, calBurnt, calCardContainer,
          calDinner, calGoal, calInput, calLunch, calRemain, calSnack, calendarToolbar,
          carbIndicator, dashboardCarb, dashboardFat, dashboardProtein, date, dinnerCard,
          editSearchName, fatIndicator, imgBack, imgNext, imgSearch, layoutSearch, lunchCard,
          proteinIndicator, rcvBreakfastFood, rcvDinnerFood, rcvLunchFood, rcvSnackFood, snackCard,
          titleCaloriesRemain, tvAddToCart);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}