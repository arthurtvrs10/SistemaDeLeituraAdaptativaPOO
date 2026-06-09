package com.leituraadapt.service;

import com.leituraadapt.model.AccessibilityProfileEntity;
import com.leituraadapt.repository.AccessibilityProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessibilityProfileService {

    private final AccessibilityProfileRepository repository;

    public AccessibilityProfileService(AccessibilityProfileRepository repository) {
        this.repository = repository;
    }

    public AccessibilityProfileEntity save(AccessibilityProfileEntity profile) {
        return repository.save(profile);
    }

    public Optional<AccessibilityProfileEntity> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }
}