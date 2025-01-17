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

public final class ItemExerciseHistoryBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView deleteHistoryBtn;

  @NonNull
  public final TextView historyDate;

  @NonNull
  public final TextView historyDuration;

  @NonNull
  public final TextView historyTitle;

  @NonNull
  public final ImageView iconHistory;

  @NonNull
  public final LinearLayout itemExHistoryLayout;

  private ItemExerciseHistoryBinding(@NonNull LinearLayout rootView,
      @NonNull ImageView deleteHistoryBtn, @NonNull TextView historyDate,
      @NonNull TextView historyDuration, @NonNull TextView historyTitle,
      @NonNull ImageView iconHistory, @NonNull LinearLayout itemExHistoryLayout) {
    this.rootView = rootView;
    this.deleteHistoryBtn = deleteHistoryBtn;
    this.historyDate = historyDate;
    this.historyDuration = historyDuration;
    this.historyTitle = historyTitle;
    this.iconHistory = iconHistory;
    this.itemExHistoryLayout = itemExHistoryLayout;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemExerciseHistoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemExerciseHistoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_exercise_history, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemExerciseHistoryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.delete_history_btn;
      ImageView deleteHistoryBtn = ViewBindings.findChildViewById(rootView, id);
      if (deleteHistoryBtn == null) {
        break missingId;
      }

      id = R.id.history_date;
      TextView historyDate = ViewBindings.findChildViewById(rootView, id);
      if (historyDate == null) {
        break missingId;
      }

      id = R.id.history_duration;
      TextView historyDuration = ViewBindings.findChildViewById(rootView, id);
      if (historyDuration == null) {
        break missingId;
      }

      id = R.id.history_title;
      TextView historyTitle = ViewBindings.findChildViewById(rootView, id);
      if (historyTitle == null) {
        break missingId;
      }

      id = R.id.icon_history;
      ImageView iconHistory = ViewBindings.findChildViewById(rootView, id);
      if (iconHistory == null) {
        break missingId;
      }

      LinearLayout itemExHistoryLayout = (LinearLayout) rootView;

      return new ItemExerciseHistoryBinding((LinearLayout) rootView, deleteHistoryBtn, historyDate,
          historyDuration, historyTitle, iconHistory, itemExHistoryLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
