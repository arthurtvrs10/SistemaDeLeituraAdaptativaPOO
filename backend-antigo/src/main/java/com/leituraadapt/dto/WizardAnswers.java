package com.leituraadapt.dto;

public class WizardAnswers {
  private final boolean screenReader;
  private final boolean lowVision;
  private final boolean dyslexia;
  private final boolean attetionDifficulty;
  private final boolean reducedMotionPreference;
  private final boolean keyboardPreferred;

  public WizardAnswers(
          boolean screenReader,
          boolean lowVision,
          boolean dyslexia,
          boolean attentionDifficulty,
          boolean reducedMotionPreference,
          boolean KeyboardPreferred
  ){
    this.screenReader = screenReader;
    this.lowVision = lowVision;
    this.dyslexia = dyslexia;
    this.attetionDifficulty = attentionDifficulty;
    this.reducedMotionPreference = reducedMotionPreference;
    this.keyboardPreferred = KeyboardPreferred;
  }

  public boolean getScreenReader() {
    return screenReader;
  }
  public boolean getLowVision() {
    return lowVision;
  }
  public boolean getDyslexia() {
    return dyslexia;
  }
  public boolean getAttettionDifficulty() {
    return attetionDifficulty;
  }
  public boolean getReducedMotionPreference() {
    return reducedMotionPreference;
  }
  public boolean getKeyboardPreferred() {
    return keyboardPreferred;
  }
}
