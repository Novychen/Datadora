// Generated by view binder compiler. Do not edit!
package at.fhooe.mc.datadora.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import at.fhooe.mc.datadora.Queue.QueueView;
import at.fhooe.mc.datadora.R;
import com.google.android.material.slider.Slider;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityQueueBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout QueueActivity;

  @NonNull
  public final ImageView QueueActivityBoxReturn;

  @NonNull
  public final Button QueueActivityButtonClear;

  @NonNull
  public final Button QueueActivityButtonDequeue;

  @NonNull
  public final Button QueueActivityButtonEmpty;

  @NonNull
  public final Button QueueActivityButtonEnqueue;

  @NonNull
  public final Button QueueActivityButtonPeek;

  @NonNull
  public final Button QueueActivityButtonRandom;

  @NonNull
  public final Button QueueActivityButtonSize;

  @NonNull
  public final TextView QueueActivityCurrentValue;

  @NonNull
  public final ImageView QueueActivityFlowIcon;

  @NonNull
  public final TextView QueueActivityFlowText;

  @NonNull
  public final Guideline QueueActivityGuideline;

  @NonNull
  public final Slider QueueActivityInputSlider;

  @NonNull
  public final TextView QueueActivityInputValue;

  @NonNull
  public final ImageView QueueActivityLine;

  @NonNull
  public final QueueView QueueActivityQueueView;

  @NonNull
  public final TextView QueueActivityReturnText;

  @NonNull
  public final TextView QueueActivityReturnValue;

  @NonNull
  public final ScrollView QueueActivityScrollViewButtons;

  @NonNull
  public final Toolbar QueueActivityToolbar;

  @NonNull
  public final FrameLayout QueueActivityViewContainer;

  private ActivityQueueBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout QueueActivity, @NonNull ImageView QueueActivityBoxReturn,
      @NonNull Button QueueActivityButtonClear, @NonNull Button QueueActivityButtonDequeue,
      @NonNull Button QueueActivityButtonEmpty, @NonNull Button QueueActivityButtonEnqueue,
      @NonNull Button QueueActivityButtonPeek, @NonNull Button QueueActivityButtonRandom,
      @NonNull Button QueueActivityButtonSize, @NonNull TextView QueueActivityCurrentValue,
      @NonNull ImageView QueueActivityFlowIcon, @NonNull TextView QueueActivityFlowText,
      @NonNull Guideline QueueActivityGuideline, @NonNull Slider QueueActivityInputSlider,
      @NonNull TextView QueueActivityInputValue, @NonNull ImageView QueueActivityLine,
      @NonNull QueueView QueueActivityQueueView, @NonNull TextView QueueActivityReturnText,
      @NonNull TextView QueueActivityReturnValue,
      @NonNull ScrollView QueueActivityScrollViewButtons, @NonNull Toolbar QueueActivityToolbar,
      @NonNull FrameLayout QueueActivityViewContainer) {
    this.rootView = rootView;
    this.QueueActivity = QueueActivity;
    this.QueueActivityBoxReturn = QueueActivityBoxReturn;
    this.QueueActivityButtonClear = QueueActivityButtonClear;
    this.QueueActivityButtonDequeue = QueueActivityButtonDequeue;
    this.QueueActivityButtonEmpty = QueueActivityButtonEmpty;
    this.QueueActivityButtonEnqueue = QueueActivityButtonEnqueue;
    this.QueueActivityButtonPeek = QueueActivityButtonPeek;
    this.QueueActivityButtonRandom = QueueActivityButtonRandom;
    this.QueueActivityButtonSize = QueueActivityButtonSize;
    this.QueueActivityCurrentValue = QueueActivityCurrentValue;
    this.QueueActivityFlowIcon = QueueActivityFlowIcon;
    this.QueueActivityFlowText = QueueActivityFlowText;
    this.QueueActivityGuideline = QueueActivityGuideline;
    this.QueueActivityInputSlider = QueueActivityInputSlider;
    this.QueueActivityInputValue = QueueActivityInputValue;
    this.QueueActivityLine = QueueActivityLine;
    this.QueueActivityQueueView = QueueActivityQueueView;
    this.QueueActivityReturnText = QueueActivityReturnText;
    this.QueueActivityReturnValue = QueueActivityReturnValue;
    this.QueueActivityScrollViewButtons = QueueActivityScrollViewButtons;
    this.QueueActivityToolbar = QueueActivityToolbar;
    this.QueueActivityViewContainer = QueueActivityViewContainer;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityQueueBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityQueueBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_queue, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityQueueBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout QueueActivity = (ConstraintLayout) rootView;

      id = R.id.Queue_Activity_Box_Return;
      ImageView QueueActivityBoxReturn = rootView.findViewById(id);
      if (QueueActivityBoxReturn == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Button_Clear;
      Button QueueActivityButtonClear = rootView.findViewById(id);
      if (QueueActivityButtonClear == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Button_Dequeue;
      Button QueueActivityButtonDequeue = rootView.findViewById(id);
      if (QueueActivityButtonDequeue == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Button_Empty;
      Button QueueActivityButtonEmpty = rootView.findViewById(id);
      if (QueueActivityButtonEmpty == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Button_Enqueue;
      Button QueueActivityButtonEnqueue = rootView.findViewById(id);
      if (QueueActivityButtonEnqueue == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Button_Peek;
      Button QueueActivityButtonPeek = rootView.findViewById(id);
      if (QueueActivityButtonPeek == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Button_Random;
      Button QueueActivityButtonRandom = rootView.findViewById(id);
      if (QueueActivityButtonRandom == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Button_Size;
      Button QueueActivityButtonSize = rootView.findViewById(id);
      if (QueueActivityButtonSize == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_CurrentValue;
      TextView QueueActivityCurrentValue = rootView.findViewById(id);
      if (QueueActivityCurrentValue == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_FlowIcon;
      ImageView QueueActivityFlowIcon = rootView.findViewById(id);
      if (QueueActivityFlowIcon == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_FlowText;
      TextView QueueActivityFlowText = rootView.findViewById(id);
      if (QueueActivityFlowText == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Guideline;
      Guideline QueueActivityGuideline = rootView.findViewById(id);
      if (QueueActivityGuideline == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_InputSlider;
      Slider QueueActivityInputSlider = rootView.findViewById(id);
      if (QueueActivityInputSlider == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_InputValue;
      TextView QueueActivityInputValue = rootView.findViewById(id);
      if (QueueActivityInputValue == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Line;
      ImageView QueueActivityLine = rootView.findViewById(id);
      if (QueueActivityLine == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_QueueView;
      QueueView QueueActivityQueueView = rootView.findViewById(id);
      if (QueueActivityQueueView == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_ReturnText;
      TextView QueueActivityReturnText = rootView.findViewById(id);
      if (QueueActivityReturnText == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_ReturnValue;
      TextView QueueActivityReturnValue = rootView.findViewById(id);
      if (QueueActivityReturnValue == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_ScrollViewButtons;
      ScrollView QueueActivityScrollViewButtons = rootView.findViewById(id);
      if (QueueActivityScrollViewButtons == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_Toolbar;
      Toolbar QueueActivityToolbar = rootView.findViewById(id);
      if (QueueActivityToolbar == null) {
        break missingId;
      }

      id = R.id.Queue_Activity_ViewContainer;
      FrameLayout QueueActivityViewContainer = rootView.findViewById(id);
      if (QueueActivityViewContainer == null) {
        break missingId;
      }

      return new ActivityQueueBinding((ConstraintLayout) rootView, QueueActivity,
          QueueActivityBoxReturn, QueueActivityButtonClear, QueueActivityButtonDequeue,
          QueueActivityButtonEmpty, QueueActivityButtonEnqueue, QueueActivityButtonPeek,
          QueueActivityButtonRandom, QueueActivityButtonSize, QueueActivityCurrentValue,
          QueueActivityFlowIcon, QueueActivityFlowText, QueueActivityGuideline,
          QueueActivityInputSlider, QueueActivityInputValue, QueueActivityLine,
          QueueActivityQueueView, QueueActivityReturnText, QueueActivityReturnValue,
          QueueActivityScrollViewButtons, QueueActivityToolbar, QueueActivityViewContainer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
