package com.backend.mvc.service.configuration;

import com.backend.mvc.model.AccessibilityProfile;

// Classe abstrata
// Ela representa uma configuração genérica de leitura
public abstract class ReadingConfiguration {

    // Nome da configuração
    protected String configurationName;

    // Descrição da configuração
    protected String description;

    // Construtor da classe abstrata
    public ReadingConfiguration(String configurationName, String description) {
        this.configurationName = configurationName;
        this.description = description;
    }

    // Método comum para todas as configurações
    public String getConfigurationName() {
        return configurationName;
    }

    // Método comum para todas as configurações
    public String getDescription() {
        return description;
    }

    // Método abstrato
    // Toda classe filha será obrigada a implementar esse método
    public abstract String applyConfiguration(AccessibilityProfile profile);
}