// Generated by view binder compiler. Do not edit!
package com.fastastapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.fastastapp.R;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityTestDataBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final AppBarLayout appBarLayout;

  @NonNull
  public final Button downloadResults;

  @NonNull
  public final Button downloadTasks;

  @NonNull
  public final TextView quantityTask;

  @NonNull
  public final TextView quantityVariants;

  @NonNull
  public final RecyclerView recviewVariant;

  @NonNull
  public final TextView testName;

  @NonNull
  public final ImageView testPhoto;

  @NonNull
  public final Toolbar toolbar;

  private ActivityTestDataBinding(@NonNull LinearLayout rootView,
      @NonNull AppBarLayout appBarLayout, @NonNull Button downloadResults,
      @NonNull Button downloadTasks, @NonNull TextView quantityTask,
      @NonNull TextView quantityVariants, @NonNull RecyclerView recviewVariant,
      @NonNull TextView testName, @NonNull ImageView testPhoto, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.appBarLayout = appBarLayout;
    this.downloadResults = downloadResults;
    this.downloadTasks = downloadTasks;
    this.quantityTask = quantityTask;
    this.quantityVariants = quantityVariants;
    this.recviewVariant = recviewVariant;
    this.testName = testName;
    this.testPhoto = testPhoto;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityTestDataBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityTestDataBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_test_data, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityTestDataBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.app_bar_layout;
      AppBarLayout appBarLayout = ViewBindings.findChildViewById(rootView, id);
      if (appBarLayout == null) {
        break missingId;
      }

      id = R.id.download_results;
      Button downloadResults = ViewBindings.findChildViewById(rootView, id);
      if (downloadResults == null) {
        break missingId;
      }

      id = R.id.download_tasks;
      Button downloadTasks = ViewBindings.findChildViewById(rootView, id);
      if (downloadTasks == null) {
        break missingId;
      }

      id = R.id.quantity_task;
      TextView quantityTask = ViewBindings.findChildViewById(rootView, id);
      if (quantityTask == null) {
        break missingId;
      }

      id = R.id.quantity_variants;
      TextView quantityVariants = ViewBindings.findChildViewById(rootView, id);
      if (quantityVariants == null) {
        break missingId;
      }

      id = R.id.recview_variant;
      RecyclerView recviewVariant = ViewBindings.findChildViewById(rootView, id);
      if (recviewVariant == null) {
        break missingId;
      }

      id = R.id.test_name;
      TextView testName = ViewBindings.findChildViewById(rootView, id);
      if (testName == null) {
        break missingId;
      }

      id = R.id.test_photo;
      ImageView testPhoto = ViewBindings.findChildViewById(rootView, id);
      if (testPhoto == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityTestDataBinding((LinearLayout) rootView, appBarLayout, downloadResults,
          downloadTasks, quantityTask, quantityVariants, recviewVariant, testName, testPhoto,
          toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
