package com.leituraadapt.repository;

import com.leituraadapt.model.AccessibilityProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessibilityProfileRepository extends JpaRepository<AccessibilityProfileEntity, String> {
    Optional<AccessibilityProfileEntity> findByUserId(String userId);
}
