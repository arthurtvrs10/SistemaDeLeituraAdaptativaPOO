package com.leituraadapt.model;

import java.util.Objects;

public class AccessibilityProfile {
  private final String name;
  private final AccessibilitySettings settings;

  public AccessibilityProfile(String name, AccessibilitySettings settings) {
    this.name = Objects.requireNonNull(name, "Nome não pode ser nulo.");
    this.settings = Objects.requireNonNull(settings, "Configurações não pode ser nulo");

    if(this.name.isEmpty()){
      throw new IllegalArgumentException("nome não pode ser vazio.");
    }
  }

  public static AccessibilityProfile defaultProfile(String name) {
    return new AccessibilityProfile(name, AccessibilitySettings.defaults());
  }

  // getters
  public String getName(){
    return name;
  }
  public AccessibilitySettings getSettings(){
    return settings;
  }

  public AccessibilityProfile withSettings(AccessibilitySettings newSettings) {
    return new AccessibilityProfile(this.name, newSettings);
  }

  @Override
  public String toString(){
    return "AccessibilityProfile{"+
            "name=1'" + name + '\'' +
            ", settings=" + settings +
            '}';
  }
}
