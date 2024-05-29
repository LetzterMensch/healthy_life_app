// Generated by view binder compiler. Do not edit!
package com.example.gr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gr.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMiBandPairingBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView mibandPairHint;

  @NonNull
  public final TextView mibandPairMessage;

  @NonNull
  public final ProgressBar progressBar;

  private ActivityMiBandPairingBinding(@NonNull RelativeLayout rootView,
      @NonNull TextView mibandPairHint, @NonNull TextView mibandPairMessage,
      @NonNull ProgressBar progressBar) {
    this.rootView = rootView;
    this.mibandPairHint = mibandPairHint;
    this.mibandPairMessage = mibandPairMessage;
    this.progressBar = progressBar;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMiBandPairingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMiBandPairingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_mi_band_pairing, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMiBandPairingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.miband_pair_hint;
      TextView mibandPairHint = ViewBindings.findChildViewById(rootView, id);
      if (mibandPairHint == null) {
        break missingId;
      }

      id = R.id.miband_pair_message;
      TextView mibandPairMessage = ViewBindings.findChildViewById(rootView, id);
      if (mibandPairMessage == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      return new ActivityMiBandPairingBinding((RelativeLayout) rootView, mibandPairHint,
          mibandPairMessage, progressBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
