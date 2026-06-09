package com.leituraadapt.controller;

import com.leituraadapt.model.AccessibilityProfileEntity;
import com.leituraadapt.service.AccessibilityProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accessibility")
public class AccessibilityProfileController {

    private final AccessibilityProfileService service;

    public AccessibilityProfileController(AccessibilityProfileService service) {
        this.service = service;
    }

    @GetMapping("/profile/me")
    public ResponseEntity<AccessibilityProfileEntity> getMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return service.findByUserId(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/profile")
    public ResponseEntity<AccessibilityProfileEntity> save(@RequestBody AccessibilityProfileEntity profile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        profile.setUserId(email);
        profile.setId("profile-" + email); // gera id manualmente

        AccessibilityProfileEntity saved = service.save(profile);

        return ResponseEntity.ok(saved);
    }
}