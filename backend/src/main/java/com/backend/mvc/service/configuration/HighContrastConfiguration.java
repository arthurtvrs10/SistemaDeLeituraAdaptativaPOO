package com.backend.mvc.service.configuration;

import com.backend.mvc.model.AccessibilityProfile;

// Classe filha focada em alto contraste
public class HighContrastConfiguration extends ReadingConfiguration {

    public HighContrastConfiguration() {
        super(
                "Configuração de alto contraste",
                "Aplica uma configuração voltada para melhor contraste visual."
        );
    }

    // Sobrescrita de método
    // Essa versão aplica uma lógica própria para alto contraste
    @Override
    public String applyConfiguration(AccessibilityProfile profile) {
        return "Alto contraste aplicado ao perfil: " + profile.getName()
                + ". Tema recomendado: DARK"
                + ", alto contraste: " + profile.getHighContrast()
                + ".";
    }
}