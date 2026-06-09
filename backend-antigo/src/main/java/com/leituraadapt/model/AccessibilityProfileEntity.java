package com.leituraadapt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Entity
public class AccessibilityProfileEntity {

    @Id
    private String id;

    private String userId;

    private int fontSize;
    private double lineHeight;
    private int columnWidth;

    private String theme;

    private boolean focusMode;
    private boolean reducedMotion;
    private boolean dyslexiaFriendlyFont;
    private boolean colorBlindMode;
    private boolean keyboardPreferred;

    public AccessibilityProfileEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public double getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(double lineHeight) {
        this.lineHeight = lineHeight;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isFocusMode() {
        return focusMode;
    }

    public void setFocusMode(boolean focusMode) {
        this.focusMode = focusMode;
    }

    public boolean isReducedMotion() {
        return reducedMotion;
    }

    public void setReducedMotion(boolean reducedMotion) {
        this.reducedMotion = reducedMotion;
    }

    public boolean isDyslexiaFriendlyFont() {
        return dyslexiaFriendlyFont;
    }

    public void setDyslexiaFriendlyFont(boolean dyslexiaFriendlyFont) {
        this.dyslexiaFriendlyFont = dyslexiaFriendlyFont;
    }

    public boolean isColorBlindMode() {
        return colorBlindMode;
    }

    public void setColorBlindMode(boolean colorBlindMode) {
        this.colorBlindMode = colorBlindMode;
    }

    public boolean isKeyboardPreferred() {
        return keyboardPreferred;
    }

    public void setKeyboardPreferred(boolean keyboardPreferred) {
        this.keyboardPreferred = keyboardPreferred;
    }
}
