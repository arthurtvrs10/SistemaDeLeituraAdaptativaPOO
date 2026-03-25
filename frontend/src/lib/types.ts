export type ThemeType = "LIGHT" | "DARK" | "HIGH_CONTRAST" | string;

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

export interface DocumentItem {
  id: string;
  title: string;
  content?: string;
}

export interface ReadingResponse {
  documentTitle: string;
  lines: string[];
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalLines: number;
  lastPage: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface AccessibilityProfile {
  fontSize?: number;
  lineHeight?: number;
  columnWidth?: number;
  theme?: ThemeType;
  focusMode?: boolean;
  reducedMotion?: boolean;
  dyslexiaFriendlyFont?: boolean;
  colorBlindMode?: boolean;
  keyboardPreferred?: boolean;
}