// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.gr.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class MealSpinnerDropdownItemBinding implements ViewBinding {
  @NonNull
  private final CheckedTextView rootView;

  @NonNull
  public final CheckedTextView text1;

  private MealSpinnerDropdownItemBinding(@NonNull CheckedTextView rootView,
      @NonNull CheckedTextView text1) {
    this.rootView = rootView;
    this.text1 = text1;
  }

  @Override
  @NonNull
  public CheckedTextView getRoot() {
    return rootView;
  }

  @NonNull
  public static MealSpinnerDropdownItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static MealSpinnerDropdownItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.meal_spinner_dropdown_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static MealSpinnerDropdownItemBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    CheckedTextView text1 = (CheckedTextView) rootView;

    return new MealSpinnerDropdownItemBinding((CheckedTextView) rootView, text1);
  }
}
