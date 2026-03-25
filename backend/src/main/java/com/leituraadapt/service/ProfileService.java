package com.leituraadapt.service;

import com.leituraadapt.model.AccessibilityProfile;

import java.util.*;

public class ProfileService {

  private final Map<String, AccessibilityProfile> profiles = new HashMap<>();

  public void save(AccessibilityProfile profile) {
      Objects.requireNonNull(profile, "profile não pode ser nulo");

      String profileName = profile.getName();
      if(profiles.containsKey(profileName)){
        throw new IllegalArgumentException("Já existe um profile com o nome " + profileName);
      }
      profiles.put(profileName, profile);
  }

  public Optional<AccessibilityProfile> findByName(String name){
    if(name == null) return Optional.empty();

    String trimmed = name.trim();

    if (trimmed.isEmpty()) return Optional.empty();

    return Optional.ofNullable(profiles.get(trimmed));
  }

  public List<AccessibilityProfile> findAll(){
    return List.copyOf(profiles.values());
  }

  public AccessibilityProfile createDefaultProfile(String name){
    AccessibilityProfile profile = AccessibilityProfile.defaultProfile(name);
    save(profile);
    return profile;
  }

}
