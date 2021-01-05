// Generated by view binder compiler. Do not edit!
package at.fhooe.mc.datadora.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import at.fhooe.mc.datadora.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout MainActivity;

  @NonNull
  public final TextView MainActivityAbout;

  @NonNull
  public final ImageView MainActivityAppIcon;

  @NonNull
  public final TextView MainActivityAppName;

  @NonNull
  public final ImageButton MainActivityDoubleListCard;

  @NonNull
  public final ImageView MainActivityDoubleListIcon;

  @NonNull
  public final TextView MainActivityDoubleListText;

  @NonNull
  public final ImageButton MainActivityQueueCard;

  @NonNull
  public final ImageView MainActivityQueueIcon;

  @NonNull
  public final TextView MainActivityQueueText;

  @NonNull
  public final ImageButton MainActivitySingleListCard;

  @NonNull
  public final ImageView MainActivitySingleListIcon;

  @NonNull
  public final TextView MainActivitySingleListText;

  @NonNull
  public final ImageButton MainActivityStackCard;

  @NonNull
  public final ImageView MainActivityStackIcon;

  @NonNull
  public final TextView MainActivityStackText;

  @NonNull
  public final Toolbar MainActivityToolbar;

  @NonNull
  public final ImageButton MainActivityTreeCard;

  @NonNull
  public final ImageView MainActivityTreeIcon;

  @NonNull
  public final TextView MainActivityTreeText;

  @NonNull
  public final Guideline guideline;

  @NonNull
  public final Guideline guideline2;

  @NonNull
  public final ScrollView scrollView3;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout MainActivity, @NonNull TextView MainActivityAbout,
      @NonNull ImageView MainActivityAppIcon, @NonNull TextView MainActivityAppName,
      @NonNull ImageButton MainActivityDoubleListCard,
      @NonNull ImageView MainActivityDoubleListIcon, @NonNull TextView MainActivityDoubleListText,
      @NonNull ImageButton MainActivityQueueCard, @NonNull ImageView MainActivityQueueIcon,
      @NonNull TextView MainActivityQueueText, @NonNull ImageButton MainActivitySingleListCard,
      @NonNull ImageView MainActivitySingleListIcon, @NonNull TextView MainActivitySingleListText,
      @NonNull ImageButton MainActivityStackCard, @NonNull ImageView MainActivityStackIcon,
      @NonNull TextView MainActivityStackText, @NonNull Toolbar MainActivityToolbar,
      @NonNull ImageButton MainActivityTreeCard, @NonNull ImageView MainActivityTreeIcon,
      @NonNull TextView MainActivityTreeText, @NonNull Guideline guideline,
      @NonNull Guideline guideline2, @NonNull ScrollView scrollView3) {
    this.rootView = rootView;
    this.MainActivity = MainActivity;
    this.MainActivityAbout = MainActivityAbout;
    this.MainActivityAppIcon = MainActivityAppIcon;
    this.MainActivityAppName = MainActivityAppName;
    this.MainActivityDoubleListCard = MainActivityDoubleListCard;
    this.MainActivityDoubleListIcon = MainActivityDoubleListIcon;
    this.MainActivityDoubleListText = MainActivityDoubleListText;
    this.MainActivityQueueCard = MainActivityQueueCard;
    this.MainActivityQueueIcon = MainActivityQueueIcon;
    this.MainActivityQueueText = MainActivityQueueText;
    this.MainActivitySingleListCard = MainActivitySingleListCard;
    this.MainActivitySingleListIcon = MainActivitySingleListIcon;
    this.MainActivitySingleListText = MainActivitySingleListText;
    this.MainActivityStackCard = MainActivityStackCard;
    this.MainActivityStackIcon = MainActivityStackIcon;
    this.MainActivityStackText = MainActivityStackText;
    this.MainActivityToolbar = MainActivityToolbar;
    this.MainActivityTreeCard = MainActivityTreeCard;
    this.MainActivityTreeIcon = MainActivityTreeIcon;
    this.MainActivityTreeText = MainActivityTreeText;
    this.guideline = guideline;
    this.guideline2 = guideline2;
    this.scrollView3 = scrollView3;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout MainActivity = (ConstraintLayout) rootView;

      id = R.id.Main_Activity_About;
      TextView MainActivityAbout = rootView.findViewById(id);
      if (MainActivityAbout == null) {
        break missingId;
      }

      id = R.id.Main_Activity_App_Icon;
      ImageView MainActivityAppIcon = rootView.findViewById(id);
      if (MainActivityAppIcon == null) {
        break missingId;
      }

      id = R.id.Main_Activity_App_Name;
      TextView MainActivityAppName = rootView.findViewById(id);
      if (MainActivityAppName == null) {
        break missingId;
      }

      id = R.id.Main_Activity_DoubleList_Card;
      ImageButton MainActivityDoubleListCard = rootView.findViewById(id);
      if (MainActivityDoubleListCard == null) {
        break missingId;
      }

      id = R.id.Main_Activity_DoubleList_Icon;
      ImageView MainActivityDoubleListIcon = rootView.findViewById(id);
      if (MainActivityDoubleListIcon == null) {
        break missingId;
      }

      id = R.id.Main_Activity_DoubleList_Text;
      TextView MainActivityDoubleListText = rootView.findViewById(id);
      if (MainActivityDoubleListText == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Queue_Card;
      ImageButton MainActivityQueueCard = rootView.findViewById(id);
      if (MainActivityQueueCard == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Queue_Icon;
      ImageView MainActivityQueueIcon = rootView.findViewById(id);
      if (MainActivityQueueIcon == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Queue_Text;
      TextView MainActivityQueueText = rootView.findViewById(id);
      if (MainActivityQueueText == null) {
        break missingId;
      }

      id = R.id.Main_Activity_SingleList_Card;
      ImageButton MainActivitySingleListCard = rootView.findViewById(id);
      if (MainActivitySingleListCard == null) {
        break missingId;
      }

      id = R.id.Main_Activity_SingleList_Icon;
      ImageView MainActivitySingleListIcon = rootView.findViewById(id);
      if (MainActivitySingleListIcon == null) {
        break missingId;
      }

      id = R.id.Main_Activity_SingleList_Text;
      TextView MainActivitySingleListText = rootView.findViewById(id);
      if (MainActivitySingleListText == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Stack_Card;
      ImageButton MainActivityStackCard = rootView.findViewById(id);
      if (MainActivityStackCard == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Stack_Icon;
      ImageView MainActivityStackIcon = rootView.findViewById(id);
      if (MainActivityStackIcon == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Stack_Text;
      TextView MainActivityStackText = rootView.findViewById(id);
      if (MainActivityStackText == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Toolbar;
      Toolbar MainActivityToolbar = rootView.findViewById(id);
      if (MainActivityToolbar == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Tree_Card;
      ImageButton MainActivityTreeCard = rootView.findViewById(id);
      if (MainActivityTreeCard == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Tree_Icon;
      ImageView MainActivityTreeIcon = rootView.findViewById(id);
      if (MainActivityTreeIcon == null) {
        break missingId;
      }

      id = R.id.Main_Activity_Tree_Text;
      TextView MainActivityTreeText = rootView.findViewById(id);
      if (MainActivityTreeText == null) {
        break missingId;
      }

      id = R.id.guideline;
      Guideline guideline = rootView.findViewById(id);
      if (guideline == null) {
        break missingId;
      }

      id = R.id.guideline2;
      Guideline guideline2 = rootView.findViewById(id);
      if (guideline2 == null) {
        break missingId;
      }

      id = R.id.scrollView3;
      ScrollView scrollView3 = rootView.findViewById(id);
      if (scrollView3 == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, MainActivity, MainActivityAbout,
          MainActivityAppIcon, MainActivityAppName, MainActivityDoubleListCard,
          MainActivityDoubleListIcon, MainActivityDoubleListText, MainActivityQueueCard,
          MainActivityQueueIcon, MainActivityQueueText, MainActivitySingleListCard,
          MainActivitySingleListIcon, MainActivitySingleListText, MainActivityStackCard,
          MainActivityStackIcon, MainActivityStackText, MainActivityToolbar, MainActivityTreeCard,
          MainActivityTreeIcon, MainActivityTreeText, guideline, guideline2, scrollView3);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
