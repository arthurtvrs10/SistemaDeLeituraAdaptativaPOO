package com.backend.mvc.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Indica que essa classe será uma entidade do banco de dados
@Entity

// Define o nome da tabela que será criada no banco
@Table(name = "accessibility_profiles")
public class AccessibilityProfile {

    // Define o campo id como chave primária da tabela
    @Id

    // Faz o banco gerar o ID automaticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do perfil de acessibilidade
    // Exemplo: "Leitura confortável"
    private String name;

    // Tamanho da fonte que será usado na leitura
    // Exemplo: 22
    private Integer fontSize;

    // Espaçamento entre linhas
    // Exemplo: 1.5
    private Double lineSpacing;

    // Indica se o perfil usa alto contraste
    // true = usa alto contraste
    // false = não usa alto contraste
    private Boolean highContrast;

    // Tema visual do perfil
    // Exemplo: "DARK" ou "LIGHT"
    private String theme;

    // Data e hora em que o perfil foi criado
    private LocalDateTime createdAt;

    // Data e hora da última atualização do perfil
    private LocalDateTime updatedAt;

    // Construtor vazio obrigatório para o JPA
    // O JPA usa esse construtor quando busca os dados no banco
    public AccessibilityProfile() {
    }

    // Sobrecarga de construtor
    // Permite criar um perfil informando apenas o nome
    public AccessibilityProfile(String name) {
        this.name = name;
        this.fontSize = 16;
        this.lineSpacing = 1.0;
        this.highContrast = false;
        this.theme = "LIGHT";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Sobrecarga de construtor
    // Permite criar um perfil informando nome e tamanho da fonte
    public AccessibilityProfile(String name, Integer fontSize) {
        this.name = name;
        this.fontSize = fontSize;
        this.lineSpacing = 1.0;
        this.highContrast = false;
        this.theme = "LIGHT";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Sobrecarga de construtor
    // Permite criar um perfil informando nome, tamanho da fonte e alto contraste
    public AccessibilityProfile(String name, Integer fontSize, Boolean highContrast) {
        this.name = name;
        this.fontSize = fontSize;
        this.lineSpacing = 1.0;
        this.highContrast = highContrast;
        this.theme = highContrast ? "DARK" : "LIGHT";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Construtor completo
    // Construtor usado para criar um novo perfil com todos os dados principais
    public AccessibilityProfile(String name, Integer fontSize, Double lineSpacing, Boolean highContrast, String theme) {
        // Define o nome do perfil
        this.name = name;

        // Define o tamanho da fonte
        this.fontSize = fontSize;

        // Define o espaçamento entre linhas
        this.lineSpacing = lineSpacing;

        // Define se o alto contraste está ativado
        this.highContrast = highContrast;

        // Define o tema do perfil
        this.theme = theme;

        // Define a data de criação como a data/hora atual
        this.createdAt = LocalDateTime.now();

        // Define a data de atualização inicial como a data/hora atual
        this.updatedAt = LocalDateTime.now();
    }

    // Retorna o ID do perfil
    public Long getId() {
        return id;
    }

    // Retorna o nome do perfil
    public String getName() {
        return name;
    }

    // Retorna o tamanho da fonte
    public Integer getFontSize() {
        return fontSize;
    }

    // Retorna o espaçamento entre linhas
    public Double getLineSpacing() {
        return lineSpacing;
    }

    // Retorna se o alto contraste está ativado
    public Boolean getHighContrast() {
        return highContrast;
    }

    // Retorna o tema do perfil
    public String getTheme() {
        return theme;
    }

    // Retorna a data de criação
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Retorna a data da última atualização
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Atualiza o nome do perfil
    public void setName(String name) {
        this.name = name;

        // Sempre que alterar algo, atualiza a data de modificação
        this.updatedAt = LocalDateTime.now();
    }

    // Atualiza o tamanho da fonte
    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;

        // Atualiza a data de modificação
        this.updatedAt = LocalDateTime.now();
    }

    // Atualiza o espaçamento entre linhas
    public void setLineSpacing(Double lineSpacing) {
        this.lineSpacing = lineSpacing;

        // Atualiza a data de modificação
        this.updatedAt = LocalDateTime.now();
    }

    // Atualiza a configuração de alto contraste
    public void setHighContrast(Boolean highContrast) {
        this.highContrast = highContrast;

        // Atualiza a data de modificação
        this.updatedAt = LocalDateTime.now();
    }

    // Atualiza o tema visual
    public void setTheme(String theme) {
        this.theme = theme;

        // Atualiza a data de modificação
        this.updatedAt = LocalDateTime.now();
    }
}