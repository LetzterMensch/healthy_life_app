// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentExerciseBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button addWearableBtn;

  @NonNull
  public final RelativeLayout btnContainer;

  @NonNull
  public final AppCompatButton btnGpsStart;

  @NonNull
  public final TextView btnViewMore;

  @NonNull
  public final TextView exCalBurnt;

  @NonNull
  public final TextView exCalBurntHr;

  @NonNull
  public final LinearLayout exStatsLayout;

  @NonNull
  public final TextView exSteps;

  @NonNull
  public final AppCompatButton indoorBtn;

  @NonNull
  public final RecyclerView rcvExHistory;

  @NonNull
  public final LinearProgressIndicator stepsBarIndicator;

  @NonNull
  public final Button syncBtn;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView titleGoalSteps;

  @NonNull
  public final TextView wearableName;

  @NonNull
  public final TextView wearableStatus;

  private FragmentExerciseBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button addWearableBtn, @NonNull RelativeLayout btnContainer,
      @NonNull AppCompatButton btnGpsStart, @NonNull TextView btnViewMore,
      @NonNull TextView exCalBurnt, @NonNull TextView exCalBurntHr,
      @NonNull LinearLayout exStatsLayout, @NonNull TextView exSteps,
      @NonNull AppCompatButton indoorBtn, @NonNull RecyclerView rcvExHistory,
      @NonNull LinearProgressIndicator stepsBarIndicator, @NonNull Button syncBtn,
      @NonNull TextView textView, @NonNull TextView titleGoalSteps, @NonNull TextView wearableName,
      @NonNull TextView wearableStatus) {
    this.rootView = rootView;
    this.addWearableBtn = addWearableBtn;
    this.btnContainer = btnContainer;
    this.btnGpsStart = btnGpsStart;
    this.btnViewMore = btnViewMore;
    this.exCalBurnt = exCalBurnt;
    this.exCalBurntHr = exCalBurntHr;
    this.exStatsLayout = exStatsLayout;
    this.exSteps = exSteps;
    this.indoorBtn = indoorBtn;
    this.rcvExHistory = rcvExHistory;
    this.stepsBarIndicator = stepsBarIndicator;
    this.syncBtn = syncBtn;
    this.textView = textView;
    this.titleGoalSteps = titleGoalSteps;
    this.wearableName = wearableName;
    this.wearableStatus = wearableStatus;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentExerciseBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentExerciseBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_exercise, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentExerciseBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.add_wearable_btn;
      Button addWearableBtn = ViewBindings.findChildViewById(rootView, id);
      if (addWearableBtn == null) {
        break missingId;
      }

      id = R.id.btn_container;
      RelativeLayout btnContainer = ViewBindings.findChildViewById(rootView, id);
      if (btnContainer == null) {
        break missingId;
      }

      id = R.id.btn_gps_start;
      AppCompatButton btnGpsStart = ViewBindings.findChildViewById(rootView, id);
      if (btnGpsStart == null) {
        break missingId;
      }

      id = R.id.btn_view_more;
      TextView btnViewMore = ViewBindings.findChildViewById(rootView, id);
      if (btnViewMore == null) {
        break missingId;
      }

      id = R.id.ex_cal_burnt;
      TextView exCalBurnt = ViewBindings.findChildViewById(rootView, id);
      if (exCalBurnt == null) {
        break missingId;
      }

      id = R.id.ex_cal_burnt_hr;
      TextView exCalBurntHr = ViewBindings.findChildViewById(rootView, id);
      if (exCalBurntHr == null) {
        break missingId;
      }

      id = R.id.ex_stats_layout;
      LinearLayout exStatsLayout = ViewBindings.findChildViewById(rootView, id);
      if (exStatsLayout == null) {
        break missingId;
      }

      id = R.id.ex_steps;
      TextView exSteps = ViewBindings.findChildViewById(rootView, id);
      if (exSteps == null) {
        break missingId;
      }

      id = R.id.indoorBtn;
      AppCompatButton indoorBtn = ViewBindings.findChildViewById(rootView, id);
      if (indoorBtn == null) {
        break missingId;
      }

      id = R.id.rcv_ex_history;
      RecyclerView rcvExHistory = ViewBindings.findChildViewById(rootView, id);
      if (rcvExHistory == null) {
        break missingId;
      }

      id = R.id.steps_bar_indicator;
      LinearProgressIndicator stepsBarIndicator = ViewBindings.findChildViewById(rootView, id);
      if (stepsBarIndicator == null) {
        break missingId;
      }

      id = R.id.sync_btn;
      Button syncBtn = ViewBindings.findChildViewById(rootView, id);
      if (syncBtn == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.title_goal_steps;
      TextView titleGoalSteps = ViewBindings.findChildViewById(rootView, id);
      if (titleGoalSteps == null) {
        break missingId;
      }

      id = R.id.wearable_name;
      TextView wearableName = ViewBindings.findChildViewById(rootView, id);
      if (wearableName == null) {
        break missingId;
      }

      id = R.id.wearable_status;
      TextView wearableStatus = ViewBindings.findChildViewById(rootView, id);
      if (wearableStatus == null) {
        break missingId;
      }

      return new FragmentExerciseBinding((ConstraintLayout) rootView, addWearableBtn, btnContainer,
          btnGpsStart, btnViewMore, exCalBurnt, exCalBurntHr, exStatsLayout, exSteps, indoorBtn,
          rcvExHistory, stepsBarIndicator, syncBtn, textView, titleGoalSteps, wearableName,
          wearableStatus);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
