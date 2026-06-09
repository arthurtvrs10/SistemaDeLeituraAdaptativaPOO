package com.backend.mvc.service.configuration;

import com.backend.mvc.model.AccessibilityProfile;

// Classe filha que herda de ReadingConfiguration
public class DefaultReadingConfiguration extends ReadingConfiguration {

    public DefaultReadingConfiguration() {
        super(
                "Configuração padrão",
                "Aplica uma configuração comum de leitura."
        );
    }

    // Sobrescrita de método
    // Aqui implementamos o método abstrato da classe pai
    @Override
    public String applyConfiguration(AccessibilityProfile profile) {
        return "Configuração padrão aplicada ao perfil: " + profile.getName()
                + ". Fonte: " + profile.getFontSize()
                + ", espaçamento: " + profile.getLineSpacing()
                + ", tema: " + profile.getTheme() + ".";
    }
}