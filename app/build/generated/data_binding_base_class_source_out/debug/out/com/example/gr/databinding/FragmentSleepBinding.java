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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSleepBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final LinearProgressIndicator REMSleepIndicator;

  @NonNull
  public final SwipeRefreshLayout activitySwipeLayout;

  @NonNull
  public final RelativeLayout calendarToolbar;

  @NonNull
  public final CardView chartCardView;

  @NonNull
  public final TextView date;

  @NonNull
  public final TextView deepSleepHr;

  @NonNull
  public final LinearProgressIndicator deepSleepIndicator;

  @NonNull
  public final TextView deepSleepPercentage;

  @NonNull
  public final NestedScrollView nestedScrollView;

  @NonNull
  public final TextView remSleep;

  @NonNull
  public final LinearProgressIndicator shallowSleepIndicator;

  @NonNull
  public final ImageView sleepFragmentImgBack;

  @NonNull
  public final ImageView sleepFragmentImgNext;

  @NonNull
  public final TextView sleepScore;

  @NonNull
  public final CardView sleepScoreCardView;

  @NonNull
  public final LineChart sleepchart;

  @NonNull
  public final CardView summaryCardView;

  @NonNull
  public final TextView totalSleepTime;

  private FragmentSleepBinding(@NonNull ConstraintLayout rootView,
      @NonNull LinearProgressIndicator REMSleepIndicator,
      @NonNull SwipeRefreshLayout activitySwipeLayout, @NonNull RelativeLayout calendarToolbar,
      @NonNull CardView chartCardView, @NonNull TextView date, @NonNull TextView deepSleepHr,
      @NonNull LinearProgressIndicator deepSleepIndicator, @NonNull TextView deepSleepPercentage,
      @NonNull NestedScrollView nestedScrollView, @NonNull TextView remSleep,
      @NonNull LinearProgressIndicator shallowSleepIndicator,
      @NonNull ImageView sleepFragmentImgBack, @NonNull ImageView sleepFragmentImgNext,
      @NonNull TextView sleepScore, @NonNull CardView sleepScoreCardView,
      @NonNull LineChart sleepchart, @NonNull CardView summaryCardView,
      @NonNull TextView totalSleepTime) {
    this.rootView = rootView;
    this.REMSleepIndicator = REMSleepIndicator;
    this.activitySwipeLayout = activitySwipeLayout;
    this.calendarToolbar = calendarToolbar;
    this.chartCardView = chartCardView;
    this.date = date;
    this.deepSleepHr = deepSleepHr;
    this.deepSleepIndicator = deepSleepIndicator;
    this.deepSleepPercentage = deepSleepPercentage;
    this.nestedScrollView = nestedScrollView;
    this.remSleep = remSleep;
    this.shallowSleepIndicator = shallowSleepIndicator;
    this.sleepFragmentImgBack = sleepFragmentImgBack;
    this.sleepFragmentImgNext = sleepFragmentImgNext;
    this.sleepScore = sleepScore;
    this.sleepScoreCardView = sleepScoreCardView;
    this.sleepchart = sleepchart;
    this.summaryCardView = summaryCardView;
    this.totalSleepTime = totalSleepTime;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSleepBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSleepBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_sleep, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSleepBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.REMSleepIndicator;
      LinearProgressIndicator REMSleepIndicator = ViewBindings.findChildViewById(rootView, id);
      if (REMSleepIndicator == null) {
        break missingId;
      }

      id = R.id.activity_swipe_layout;
      SwipeRefreshLayout activitySwipeLayout = ViewBindings.findChildViewById(rootView, id);
      if (activitySwipeLayout == null) {
        break missingId;
      }

      id = R.id.calendar_toolbar;
      RelativeLayout calendarToolbar = ViewBindings.findChildViewById(rootView, id);
      if (calendarToolbar == null) {
        break missingId;
      }

      id = R.id.chartCardView;
      CardView chartCardView = ViewBindings.findChildViewById(rootView, id);
      if (chartCardView == null) {
        break missingId;
      }

      id = R.id.date;
      TextView date = ViewBindings.findChildViewById(rootView, id);
      if (date == null) {
        break missingId;
      }

      id = R.id.deep_sleep_hr;
      TextView deepSleepHr = ViewBindings.findChildViewById(rootView, id);
      if (deepSleepHr == null) {
        break missingId;
      }

      id = R.id.deepSleepIndicator;
      LinearProgressIndicator deepSleepIndicator = ViewBindings.findChildViewById(rootView, id);
      if (deepSleepIndicator == null) {
        break missingId;
      }

      id = R.id.deep_sleep_percentage;
      TextView deepSleepPercentage = ViewBindings.findChildViewById(rootView, id);
      if (deepSleepPercentage == null) {
        break missingId;
      }

      id = R.id.nestedScrollView;
      NestedScrollView nestedScrollView = ViewBindings.findChildViewById(rootView, id);
      if (nestedScrollView == null) {
        break missingId;
      }

      id = R.id.rem_sleep;
      TextView remSleep = ViewBindings.findChildViewById(rootView, id);
      if (remSleep == null) {
        break missingId;
      }

      id = R.id.shallowSleepIndicator;
      LinearProgressIndicator shallowSleepIndicator = ViewBindings.findChildViewById(rootView, id);
      if (shallowSleepIndicator == null) {
        break missingId;
      }

      id = R.id.sleep_fragment_img_back;
      ImageView sleepFragmentImgBack = ViewBindings.findChildViewById(rootView, id);
      if (sleepFragmentImgBack == null) {
        break missingId;
      }

      id = R.id.sleep_fragment_img_next;
      ImageView sleepFragmentImgNext = ViewBindings.findChildViewById(rootView, id);
      if (sleepFragmentImgNext == null) {
        break missingId;
      }

      id = R.id.sleep_score;
      TextView sleepScore = ViewBindings.findChildViewById(rootView, id);
      if (sleepScore == null) {
        break missingId;
      }

      id = R.id.sleepScoreCardView;
      CardView sleepScoreCardView = ViewBindings.findChildViewById(rootView, id);
      if (sleepScoreCardView == null) {
        break missingId;
      }

      id = R.id.sleepchart;
      LineChart sleepchart = ViewBindings.findChildViewById(rootView, id);
      if (sleepchart == null) {
        break missingId;
      }

      id = R.id.summaryCardView;
      CardView summaryCardView = ViewBindings.findChildViewById(rootView, id);
      if (summaryCardView == null) {
        break missingId;
      }

      id = R.id.total_sleep_time;
      TextView totalSleepTime = ViewBindings.findChildViewById(rootView, id);
      if (totalSleepTime == null) {
        break missingId;
      }

      return new FragmentSleepBinding((ConstraintLayout) rootView, REMSleepIndicator,
          activitySwipeLayout, calendarToolbar, chartCardView, date, deepSleepHr,
          deepSleepIndicator, deepSleepPercentage, nestedScrollView, remSleep,
          shallowSleepIndicator, sleepFragmentImgBack, sleepFragmentImgNext, sleepScore,
          sleepScoreCardView, sleepchart, summaryCardView, totalSleepTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
