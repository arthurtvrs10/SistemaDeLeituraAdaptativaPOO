package com.leituraadapt.app;

import com.leituraadapt.model.AccessibilityProfile;
import com.leituraadapt.model.AccessibilitySettings;
import com.leituraadapt.model.Document;
import com.leituraadapt.model.Theme;
import com.leituraadapt.repository.DocumentRepository;

public class App {

  private final DocumentRepository documentRepository = new DocumentRepository();

  public void run() {
    System.out.println("App iniciado com sucesso!");

    // ===== Teste Dia 5 - Imutabilidade =====
    AccessibilitySettings s1 = AccessibilitySettings.defaults();
    AccessibilitySettings s2 = s1.withFontSize(22);

    System.out.println("s1 fontSize = " + s1.getFontSize());
    System.out.println("s2 fontSize = " + s2.getFontSize());

    // ===== Teste repositório =====
    seedDocuments();
    listDocuments();

    // ===== Teste Dia 6 - AccessibilityProfile =====
    AccessibilityProfile profile1 = AccessibilityProfile.defaultProfile("Padrão");

    AccessibilitySettings customSettings = profile1.getSettings()
            .withFontSize(22)
            .withLineHeight(1.8)
            .withTheme(Theme.HIGH_CONTRAST);

    AccessibilityProfile profile2 = profile1.withSettings(customSettings);

    System.out.println(profile1);
    System.out.println(profile2);
  }

  private void seedDocuments() {
    documentRepository.save(new Document(
            "doc1",
            "WCAG em um minuto",
            "WCAG são diretrizes para tornar conteúdo web mais acessível. " +
                    "Princípios: Perceptível, Operável, Compreensível e Robusto."
    ));

    documentRepository.save(new Document(
            "doc2",
            "Dicas de Leitura Focada",
            "Para melhorar foco: reduza distrações, ajuste espaçamento e use largura de coluna confortável. " +
                    "Leia em blocos pequenos e retome do ponto salvo."
    ));

    documentRepository.save(new Document(
            "doc3",
            "Texto longo (demo)",
            "Este é um texto maior para testar leitura e paginação depois. " +
                    "Ele ajuda a validar wrap e progresso. " +
                    "Linha extra 1. Linha extra 2. Linha extra 3."
    ));
  }

  private void listDocuments() {
    System.out.println("Listando documentos com sucesso!");

    documentRepository.findAll()
            .forEach(d -> System.out.println("- " + d.getTitle() + " (id=" + d.getId() + ")"));
  }
}