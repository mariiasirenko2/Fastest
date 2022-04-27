// Generated by view binder compiler. Do not edit!
package com.fastastapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.fastastapp.R;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySingUpBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextInputLayout email;

  @NonNull
  public final TextInputLayout fullname;

  @NonNull
  public final Button goLoginB;

  @NonNull
  public final ImageView logoImage;

  @NonNull
  public final TextInputLayout password;

  @NonNull
  public final Button registrationB;

  private ActivitySingUpBinding(@NonNull LinearLayout rootView, @NonNull TextInputLayout email,
      @NonNull TextInputLayout fullname, @NonNull Button goLoginB, @NonNull ImageView logoImage,
      @NonNull TextInputLayout password, @NonNull Button registrationB) {
    this.rootView = rootView;
    this.email = email;
    this.fullname = fullname;
    this.goLoginB = goLoginB;
    this.logoImage = logoImage;
    this.password = password;
    this.registrationB = registrationB;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySingUpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySingUpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sing_up, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySingUpBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.email;
      TextInputLayout email = ViewBindings.findChildViewById(rootView, id);
      if (email == null) {
        break missingId;
      }

      id = R.id.fullname;
      TextInputLayout fullname = ViewBindings.findChildViewById(rootView, id);
      if (fullname == null) {
        break missingId;
      }

      id = R.id.go_login_b;
      Button goLoginB = ViewBindings.findChildViewById(rootView, id);
      if (goLoginB == null) {
        break missingId;
      }

      id = R.id.logoImage;
      ImageView logoImage = ViewBindings.findChildViewById(rootView, id);
      if (logoImage == null) {
        break missingId;
      }

      id = R.id.password;
      TextInputLayout password = ViewBindings.findChildViewById(rootView, id);
      if (password == null) {
        break missingId;
      }

      id = R.id.registration_b;
      Button registrationB = ViewBindings.findChildViewById(rootView, id);
      if (registrationB == null) {
        break missingId;
      }

      return new ActivitySingUpBinding((LinearLayout) rootView, email, fullname, goLoginB,
          logoImage, password, registrationB);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
