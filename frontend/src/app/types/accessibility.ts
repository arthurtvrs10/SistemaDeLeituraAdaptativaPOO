export interface AccessibilitySettings {
  fontSize: number;
  lineSpacing: number;
  columnWidth: number;
  highContrast: boolean;
  focusMode: boolean;
  dyslexiaMode: boolean;
}

export interface WizardAnswers {
  smallText: boolean | null;
  distraction: boolean | null;
  spacing: boolean | null;
}