// Generated by view binder compiler. Do not edit!
package at.fhooe.mc.datadora.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import at.fhooe.mc.datadora.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAboutBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout AboutActivity;

  @NonNull
  public final ImageView AboutActivityAppIcon;

  @NonNull
  public final TextView AboutActivityAppName;

  @NonNull
  public final TextView AboutActivityAppSlogan;

  @NonNull
  public final TextView AboutActivityBlockText;

  @NonNull
  public final TextView AboutActivityDeveloper;

  @NonNull
  public final TextView AboutActivityGerald;

  @NonNull
  public final TextView AboutActivityInes;

  @NonNull
  public final ImageView AboutActivityLine1;

  @NonNull
  public final ImageView AboutActivityLine2;

  @NonNull
  public final Toolbar AboutActivityToolbar;

  @NonNull
  public final TextView AboutActivityUniversity;

  @NonNull
  public final TextView AboutActivityYvonne;

  private ActivityAboutBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout AboutActivity, @NonNull ImageView AboutActivityAppIcon,
      @NonNull TextView AboutActivityAppName, @NonNull TextView AboutActivityAppSlogan,
      @NonNull TextView AboutActivityBlockText, @NonNull TextView AboutActivityDeveloper,
      @NonNull TextView AboutActivityGerald, @NonNull TextView AboutActivityInes,
      @NonNull ImageView AboutActivityLine1, @NonNull ImageView AboutActivityLine2,
      @NonNull Toolbar AboutActivityToolbar, @NonNull TextView AboutActivityUniversity,
      @NonNull TextView AboutActivityYvonne) {
    this.rootView = rootView;
    this.AboutActivity = AboutActivity;
    this.AboutActivityAppIcon = AboutActivityAppIcon;
    this.AboutActivityAppName = AboutActivityAppName;
    this.AboutActivityAppSlogan = AboutActivityAppSlogan;
    this.AboutActivityBlockText = AboutActivityBlockText;
    this.AboutActivityDeveloper = AboutActivityDeveloper;
    this.AboutActivityGerald = AboutActivityGerald;
    this.AboutActivityInes = AboutActivityInes;
    this.AboutActivityLine1 = AboutActivityLine1;
    this.AboutActivityLine2 = AboutActivityLine2;
    this.AboutActivityToolbar = AboutActivityToolbar;
    this.AboutActivityUniversity = AboutActivityUniversity;
    this.AboutActivityYvonne = AboutActivityYvonne;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAboutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAboutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_about, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAboutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout AboutActivity = (ConstraintLayout) rootView;

      id = R.id.About_Activity_App_Icon;
      ImageView AboutActivityAppIcon = rootView.findViewById(id);
      if (AboutActivityAppIcon == null) {
        break missingId;
      }

      id = R.id.About_Activity_App_Name;
      TextView AboutActivityAppName = rootView.findViewById(id);
      if (AboutActivityAppName == null) {
        break missingId;
      }

      id = R.id.About_Activity_App_Slogan;
      TextView AboutActivityAppSlogan = rootView.findViewById(id);
      if (AboutActivityAppSlogan == null) {
        break missingId;
      }

      id = R.id.About_Activity_BlockText;
      TextView AboutActivityBlockText = rootView.findViewById(id);
      if (AboutActivityBlockText == null) {
        break missingId;
      }

      id = R.id.About_Activity_Developer;
      TextView AboutActivityDeveloper = rootView.findViewById(id);
      if (AboutActivityDeveloper == null) {
        break missingId;
      }

      id = R.id.About_Activity_Gerald;
      TextView AboutActivityGerald = rootView.findViewById(id);
      if (AboutActivityGerald == null) {
        break missingId;
      }

      id = R.id.About_Activity_Ines;
      TextView AboutActivityInes = rootView.findViewById(id);
      if (AboutActivityInes == null) {
        break missingId;
      }

      id = R.id.About_Activity_Line_1;
      ImageView AboutActivityLine1 = rootView.findViewById(id);
      if (AboutActivityLine1 == null) {
        break missingId;
      }

      id = R.id.About_Activity_Line_2;
      ImageView AboutActivityLine2 = rootView.findViewById(id);
      if (AboutActivityLine2 == null) {
        break missingId;
      }

      id = R.id.About_Activity_Toolbar;
      Toolbar AboutActivityToolbar = rootView.findViewById(id);
      if (AboutActivityToolbar == null) {
        break missingId;
      }

      id = R.id.About_Activity_University;
      TextView AboutActivityUniversity = rootView.findViewById(id);
      if (AboutActivityUniversity == null) {
        break missingId;
      }

      id = R.id.About_Activity_Yvonne;
      TextView AboutActivityYvonne = rootView.findViewById(id);
      if (AboutActivityYvonne == null) {
        break missingId;
      }

      return new ActivityAboutBinding((ConstraintLayout) rootView, AboutActivity,
          AboutActivityAppIcon, AboutActivityAppName, AboutActivityAppSlogan,
          AboutActivityBlockText, AboutActivityDeveloper, AboutActivityGerald, AboutActivityInes,
          AboutActivityLine1, AboutActivityLine2, AboutActivityToolbar, AboutActivityUniversity,
          AboutActivityYvonne);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
