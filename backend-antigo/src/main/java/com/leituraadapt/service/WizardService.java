package com.leituraadapt.service;

import com.leituraadapt.dto.WizardAnswers;
import com.leituraadapt.model.AccessibilityProfile;
import com.leituraadapt.model.AccessibilitySettings;
import com.leituraadapt.model.Theme;

import java.util.Objects;

public class WizardService {
  public AccessibilityProfile recommendProfile(String profileName, WizardAnswers answers) {
    Objects.requireNonNull(profileName, "profileName não pode ser nulo.");
    Objects.requireNonNull(answers, "answers não pode ser nulo.");

    AccessibilitySettings settings = AccessibilitySettings.defaults();

    if (answers.getLowVision()){
      settings = settings
              .withFontSize(22)
              .withLineHeight(1.8)
              .withTheme(Theme.HIGH_CONTRAST);
    }
    if (answers.getDyslexia()){
      settings = settings
              .withLineHeight(1.8)
              .withColumnWidth(70)
              .withDyslexiaFriendlyFont(true);
    }
    if (answers.getAttettionDifficulty()){
      settings = settings
              .withFocusMode(true)
              .withColumnWidth(65);
    }
    if (answers.getReducedMotionPreference()){
      settings = settings.withReducedMotion(true);
    }
    if (answers.getKeyboardPreferred()){
      settings = settings.withKeyboardPreferred(true);
    }
    return new AccessibilityProfile(profileName, settings);
  }
}
