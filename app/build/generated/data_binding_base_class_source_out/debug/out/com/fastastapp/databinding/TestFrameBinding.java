// Generated by view binder compiler. Do not edit!
package com.fastastapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.fastastapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class TestFrameBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final CardView card;

  @NonNull
  public final ImageView img;

  @NonNull
  public final TextView text;

  private TestFrameBinding(@NonNull CardView rootView, @NonNull CardView card,
      @NonNull ImageView img, @NonNull TextView text) {
    this.rootView = rootView;
    this.card = card;
    this.img = img;
    this.text = text;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static TestFrameBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static TestFrameBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.test_frame, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static TestFrameBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      CardView card = (CardView) rootView;

      id = R.id.img;
      ImageView img = ViewBindings.findChildViewById(rootView, id);
      if (img == null) {
        break missingId;
      }

      id = R.id.text;
      TextView text = ViewBindings.findChildViewById(rootView, id);
      if (text == null) {
        break missingId;
      }

      return new TestFrameBinding((CardView) rootView, card, img, text);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
