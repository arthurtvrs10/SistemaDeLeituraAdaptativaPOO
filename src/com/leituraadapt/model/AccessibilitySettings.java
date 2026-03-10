package com.leituraadapt.domain;

import java.util.Objects;

public final class AccessibilitySettings{

  // ===== Limites "seguros" (você pode ajustar) =====
  public static final int MIN_FONT_SIZE = 12;
  public static final int MAX_FONT_SIZE = 36;

  public static final double MIN_LINE_HEIGHT = 1.0;
  public static final double MAX_LINE_HEIGHT = 2.2;

  public static final int MIN_COLUMN_WIDTH = 40;
  public static final int MAX_COLUMN_WIDTH = 120;

  // ===== Campos (imutáveis) =====
  private final int fontSize;
  private final double lineHeight;
  private final int columnWidth;
  private final Theme theme;

  private final boolean focusMode;
  private final boolean reducedMotion;
  private final boolean dyslexiaFriendlyFont;
  private final boolean colorBlindMode;
  private final boolean keyboardPreferred;

  // ========= Construtor ======
  public AccessibilitySettings(
          int fontSize,
          double lineHeight,
          int columnWidth,
          Theme theme,
          boolean focusMode,
          boolean reducedMotion,
          boolean dyslexiaFriendlyFont,
          boolean colorBlindMode,
          boolean keyboardPreferred
  ) {
    validateFontSize(fontSize);
    validateLineHeight(lineHeight);
    validateColumnWidth(columnWidth);
    this.theme = Objects.requireNonNull(theme, "Theme não pode ser nulo");

    this.fontSize = fontSize;
    this.lineHeight = lineHeight;
    this.columnWidth = columnWidth;

    this.focusMode = focusMode;
    this.reducedMotion = reducedMotion;
    this.dyslexiaFriendlyFont = dyslexiaFriendlyFont;
    this.colorBlindMode = colorBlindMode;
    this.keyboardPreferred = keyboardPreferred;
  }

  // ======= Defaults ===========
  public static AccessibilitySettings defaults() {
    return new AccessibilitySettings(
      15,
      1.4,
      80,
      Theme.LIGHT,
      false,
      true,
      false,
      false,
      true
    );
  }

  // =========== Getters ============

  public int getFontSize(){
    return fontSize;
  }
  public double getLineHeight(){
    return lineHeight;
  }
  public int getColumnWidth(){
    return columnWidth;
  }
  public Theme getTheme(){
    return theme;
  }
  public boolean getFocusMode(){
    return focusMode;
  }
  public boolean getReducedMotion(){
    return reducedMotion;
  }
  public boolean getDyslexiaFriendlyFont(){
    return dyslexiaFriendlyFont;
  }
  public boolean getColorBlindMode() {
    return colorBlindMode;
  }
  public boolean getKeyboardPreferred() {
    return keyboardPreferred;
  }

  // ========== withX() (imutabilidade) ==========
  public AccessibilitySettings withFontSize(int newFontSize){
    return new AccessibilitySettings(
            newFontSize,
            this.lineHeight,
            this.columnWidth,
            this.theme,
            this.focusMode,
            this.reducedMotion,
            this.dyslexiaFriendlyFont,
            this.colorBlindMode,
            this.keyboardPreferred
    );
  }
  public AccessibilitySettings withLineHeight(double newLineHeight) {
    return new AccessibilitySettings(
            this.fontSize,
            newLineHeight,
            this.columnWidth,
            this.theme,
            this.focusMode,
            this.reducedMotion,
            this.dyslexiaFriendlyFont,
            this.colorBlindMode,
            this.keyboardPreferred
    );
  }
  public AccessibilitySettings withColumnWidth(int newColumnWidth) {
    return new AccessibilitySettings(
            this.fontSize,
            this.lineHeight,
            newColumnWidth,
            this.theme,
            this.focusMode,
            this.reducedMotion,
            this.dyslexiaFriendlyFont,
            this.colorBlindMode,
            this.keyboardPreferred
    );
  }

  public AccessibilitySettings withTheme(Theme newTheme) {
    return new AccessibilitySettings(
            this.fontSize,
            this.lineHeight,
            this.columnWidth,
            Objects.requireNonNull(newTheme, "theme não pode ser nulo"),
            this.focusMode,
            this.reducedMotion,
            this.dyslexiaFriendlyFont,
            this.colorBlindMode,
            this.keyboardPreferred
    );
  }

  public AccessibilitySettings withFocusMode(boolean newFocusMode) {
    return new AccessibilitySettings(
            this.fontSize,
            this.lineHeight,
            this.columnWidth,
            this.theme,
            newFocusMode,
            this.reducedMotion,
            this.dyslexiaFriendlyFont,
            this.colorBlindMode,
            this.keyboardPreferred
    );
  }

  public AccessibilitySettings withReducedMotion(boolean newReducedMotion) {
    return new AccessibilitySettings(
            this.fontSize,
            this.lineHeight,
            this.columnWidth,
            this.theme,
            this.focusMode,
            newReducedMotion,
            this.dyslexiaFriendlyFont,
            this.colorBlindMode,
            this.keyboardPreferred
    );
  }

  public AccessibilitySettings withDyslexiaFriendlyFont(boolean newDyslexiaFriendlyFont) {
    return new AccessibilitySettings(
            this.fontSize,
            this.lineHeight,
            this.columnWidth,
            this.theme,
            this.focusMode,
            this.reducedMotion,
            newDyslexiaFriendlyFont,
            this.colorBlindMode,
            this.keyboardPreferred
    );
  }

  public AccessibilitySettings withColorBlindMode(boolean newColorBlindMode) {
    return new AccessibilitySettings(
            this.fontSize,
            this.lineHeight,
            this.columnWidth,
            this.theme,
            this.focusMode,
            this.reducedMotion,
            this.dyslexiaFriendlyFont,
            newColorBlindMode,
            this.keyboardPreferred
    );
  }

  public AccessibilitySettings withKeyboardPreferred(boolean newKeyboardPreferred) {
    return new AccessibilitySettings(
            this.fontSize,
            this.lineHeight,
            this.columnWidth,
            this.theme,
            this.focusMode,
            this.reducedMotion,
            this.dyslexiaFriendlyFont,
            this.colorBlindMode,
            newKeyboardPreferred
    );
  }

  // ================= Validações ==========
  private static void validateFontSize(int fontSize){
    if(fontSize < MIN_FONT_SIZE ||  fontSize > MAX_FONT_SIZE){
      throw new IllegalArgumentException(
              "fontSize fora do intervalo(" + MIN_FONT_SIZE + " - " + MAX_FONT_SIZE + "): " + fontSize
      );
    }
  }

  private static void validateLineHeight(double lineHeight){
    if(lineHeight < MIN_LINE_HEIGHT || lineHeight > MAX_LINE_HEIGHT){
      throw new IllegalArgumentException(
              "lineHeight fora do intervalo(" + MIN_LINE_HEIGHT + " - " + MAX_LINE_HEIGHT + "): " + lineHeight
      );
    }
  }

  private static void validateColumnWidth(int columnWidth){
    if(columnWidth < MIN_COLUMN_WIDTH || columnWidth > MAX_COLUMN_WIDTH){
      throw new IllegalArgumentException(
              "columnWidth fora do intervalo (" + MIN_COLUMN_WIDTH + "-" + MAX_COLUMN_WIDTH + "): " + columnWidth
      );
    }
  }
  @Override
  public String toString(){
    return "AccessibilitySettings{" +
            "fontSize=" + fontSize +
            ", lineHeight=" + lineHeight +
            ", columnWidth=" + columnWidth +
            ", theme=" + theme +
            ", focusMode=" + focusMode +
            ", reducedMotion=" + reducedMotion +
            ", dyslexiaFriendlyFont=" + dyslexiaFriendlyFont +
            ", colorBlindMode=" + colorBlindMode +
            ", keyboardPreferred=" + keyboardPreferred +
            '}';
  }
}
