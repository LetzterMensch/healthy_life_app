// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDashboardBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView calBurntDuration;

  @NonNull
  public final LinearLayout calProcessContainer;

  @NonNull
  public final TextView caloriesBurnt;

  @NonNull
  public final CircularProgressIndicator caloriesCircle;

  @NonNull
  public final TextView caloriesInput;

  @NonNull
  public final TextView caloriesRemain;

  @NonNull
  public final LinearProgressIndicator carbIndicator;

  @NonNull
  public final CardView cardBurntContainer;

  @NonNull
  public final MaterialCardView cardContainer;

  @NonNull
  public final TextView dashboardCalBurnt;

  @NonNull
  public final TextView dashboardCarb;

  @NonNull
  public final TextView dashboardFat;

  @NonNull
  public final TextView dashboardProtein;

  @NonNull
  public final TextView dashboardSteps;

  @NonNull
  public final LinearProgressIndicator fatIndicator;

  @NonNull
  public final LineChart lineChart;

  @NonNull
  public final ImageButton logWeightBtn;

  @NonNull
  public final LinearProgressIndicator proteinIndicator;

  @NonNull
  public final LinearProgressIndicator stepsBarIndicator;

  @NonNull
  public final AppCompatTextView titleCalories;

  @NonNull
  public final TextView titleGoalSteps;

  @NonNull
  public final AppCompatTextView titleToday;

  @NonNull
  public final CardView weightLineChart;

  private FragmentDashboardBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView calBurntDuration, @NonNull LinearLayout calProcessContainer,
      @NonNull TextView caloriesBurnt, @NonNull CircularProgressIndicator caloriesCircle,
      @NonNull TextView caloriesInput, @NonNull TextView caloriesRemain,
      @NonNull LinearProgressIndicator carbIndicator, @NonNull CardView cardBurntContainer,
      @NonNull MaterialCardView cardContainer, @NonNull TextView dashboardCalBurnt,
      @NonNull TextView dashboardCarb, @NonNull TextView dashboardFat,
      @NonNull TextView dashboardProtein, @NonNull TextView dashboardSteps,
      @NonNull LinearProgressIndicator fatIndicator, @NonNull LineChart lineChart,
      @NonNull ImageButton logWeightBtn, @NonNull LinearProgressIndicator proteinIndicator,
      @NonNull LinearProgressIndicator stepsBarIndicator, @NonNull AppCompatTextView titleCalories,
      @NonNull TextView titleGoalSteps, @NonNull AppCompatTextView titleToday,
      @NonNull CardView weightLineChart) {
    this.rootView = rootView;
    this.calBurntDuration = calBurntDuration;
    this.calProcessContainer = calProcessContainer;
    this.caloriesBurnt = caloriesBurnt;
    this.caloriesCircle = caloriesCircle;
    this.caloriesInput = caloriesInput;
    this.caloriesRemain = caloriesRemain;
    this.carbIndicator = carbIndicator;
    this.cardBurntContainer = cardBurntContainer;
    this.cardContainer = cardContainer;
    this.dashboardCalBurnt = dashboardCalBurnt;
    this.dashboardCarb = dashboardCarb;
    this.dashboardFat = dashboardFat;
    this.dashboardProtein = dashboardProtein;
    this.dashboardSteps = dashboardSteps;
    this.fatIndicator = fatIndicator;
    this.lineChart = lineChart;
    this.logWeightBtn = logWeightBtn;
    this.proteinIndicator = proteinIndicator;
    this.stepsBarIndicator = stepsBarIndicator;
    this.titleCalories = titleCalories;
    this.titleGoalSteps = titleGoalSteps;
    this.titleToday = titleToday;
    this.weightLineChart = weightLineChart;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cal_burnt_duration;
      TextView calBurntDuration = ViewBindings.findChildViewById(rootView, id);
      if (calBurntDuration == null) {
        break missingId;
      }

      id = R.id.cal_process_container;
      LinearLayout calProcessContainer = ViewBindings.findChildViewById(rootView, id);
      if (calProcessContainer == null) {
        break missingId;
      }

      id = R.id.calories_burnt;
      TextView caloriesBurnt = ViewBindings.findChildViewById(rootView, id);
      if (caloriesBurnt == null) {
        break missingId;
      }

      id = R.id.calories_circle;
      CircularProgressIndicator caloriesCircle = ViewBindings.findChildViewById(rootView, id);
      if (caloriesCircle == null) {
        break missingId;
      }

      id = R.id.calories_input;
      TextView caloriesInput = ViewBindings.findChildViewById(rootView, id);
      if (caloriesInput == null) {
        break missingId;
      }

      id = R.id.calories_remain;
      TextView caloriesRemain = ViewBindings.findChildViewById(rootView, id);
      if (caloriesRemain == null) {
        break missingId;
      }

      id = R.id.carb_indicator;
      LinearProgressIndicator carbIndicator = ViewBindings.findChildViewById(rootView, id);
      if (carbIndicator == null) {
        break missingId;
      }

      id = R.id.card_burnt_container;
      CardView cardBurntContainer = ViewBindings.findChildViewById(rootView, id);
      if (cardBurntContainer == null) {
        break missingId;
      }

      id = R.id.card_container;
      MaterialCardView cardContainer = ViewBindings.findChildViewById(rootView, id);
      if (cardContainer == null) {
        break missingId;
      }

      id = R.id.dashboard_cal_burnt;
      TextView dashboardCalBurnt = ViewBindings.findChildViewById(rootView, id);
      if (dashboardCalBurnt == null) {
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

      id = R.id.dashboard_steps;
      TextView dashboardSteps = ViewBindings.findChildViewById(rootView, id);
      if (dashboardSteps == null) {
        break missingId;
      }

      id = R.id.fat_indicator;
      LinearProgressIndicator fatIndicator = ViewBindings.findChildViewById(rootView, id);
      if (fatIndicator == null) {
        break missingId;
      }

      id = R.id.lineChart;
      LineChart lineChart = ViewBindings.findChildViewById(rootView, id);
      if (lineChart == null) {
        break missingId;
      }

      id = R.id.log_weight_btn;
      ImageButton logWeightBtn = ViewBindings.findChildViewById(rootView, id);
      if (logWeightBtn == null) {
        break missingId;
      }

      id = R.id.protein_indicator;
      LinearProgressIndicator proteinIndicator = ViewBindings.findChildViewById(rootView, id);
      if (proteinIndicator == null) {
        break missingId;
      }

      id = R.id.steps_bar_indicator;
      LinearProgressIndicator stepsBarIndicator = ViewBindings.findChildViewById(rootView, id);
      if (stepsBarIndicator == null) {
        break missingId;
      }

      id = R.id.title_calories;
      AppCompatTextView titleCalories = ViewBindings.findChildViewById(rootView, id);
      if (titleCalories == null) {
        break missingId;
      }

      id = R.id.title_goal_steps;
      TextView titleGoalSteps = ViewBindings.findChildViewById(rootView, id);
      if (titleGoalSteps == null) {
        break missingId;
      }

      id = R.id.title_today;
      AppCompatTextView titleToday = ViewBindings.findChildViewById(rootView, id);
      if (titleToday == null) {
        break missingId;
      }

      id = R.id.weight_line_chart;
      CardView weightLineChart = ViewBindings.findChildViewById(rootView, id);
      if (weightLineChart == null) {
        break missingId;
      }

      return new FragmentDashboardBinding((ConstraintLayout) rootView, calBurntDuration,
          calProcessContainer, caloriesBurnt, caloriesCircle, caloriesInput, caloriesRemain,
          carbIndicator, cardBurntContainer, cardContainer, dashboardCalBurnt, dashboardCarb,
          dashboardFat, dashboardProtein, dashboardSteps, fatIndicator, lineChart, logWeightBtn,
          proteinIndicator, stepsBarIndicator, titleCalories, titleGoalSteps, titleToday,
          weightLineChart);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
