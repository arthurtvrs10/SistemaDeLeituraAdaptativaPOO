package com.backend.mvc.service.configuration;

import com.backend.mvc.model.AccessibilityProfile;

// Classe filha focada em fonte ampliada
public class LargeFontConfiguration extends ReadingConfiguration {

    public LargeFontConfiguration() {
        super(
                "Configuração de fonte ampliada",
                "Aplica uma configuração voltada para leitura com fonte maior."
        );
    }

    // Sobrescrita de método
    // Essa versão aplica uma lógica própria para fonte ampliada
    @Override
    public String applyConfiguration(AccessibilityProfile profile) {
        return "Fonte ampliada aplicada ao perfil: " + profile.getName()
                + ". Tamanho da fonte: " + profile.getFontSize()
                + ".";
    }
}