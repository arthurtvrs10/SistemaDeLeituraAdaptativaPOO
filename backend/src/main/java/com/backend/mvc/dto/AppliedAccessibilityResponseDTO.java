package com.backend.mvc.dto;

// DTO usado para retornar ao cliente o resultado da aplicação da configuração
public class AppliedAccessibilityResponseDTO {

    private String profileName;
    private String configurationType;
    private String message;

    public AppliedAccessibilityResponseDTO(String profileName, String configurationType, String message) {
        this.profileName = profileName;
        this.configurationType = configurationType;
        this.message = message;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getConfigurationType() {
        return configurationType;
    }

    public String getMessage() {
        return message;
    }
}