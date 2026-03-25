/**package com.leituraadapt.app;

import com.leituraadapt.dto.WizardAnswers;
import com.leituraadapt.model.AccessibilityProfile;
import com.leituraadapt.model.AccessibilitySettings;
import com.leituraadapt.model.Document;
import com.leituraadapt.model.Theme;
import com.leituraadapt.repository.DocumentRepository;
import com.leituraadapt.service.ProfileService;
import com.leituraadapt.service.ReadingService;
import com.leituraadapt.service.ReadingSession;
import com.leituraadapt.service.TextWrapper;
import com.leituraadapt.service.WizardService;

import java.util.List;

public class App {

  private final DocumentRepository documentRepository = new DocumentRepository();

  public void run() {
    System.out.println("App iniciado com sucesso!");

    seedDocuments();

    testDay5Settings();
    testRepository();
    testDay6AccessibilityProfile();
    testDay7WizardService();
    testDay8ProfileService();
    testDay9ReadingSession();
    testDay10TextWrapper();
    testDay11ReadingService();
  }

  private void testDay5Settings() {
    System.out.println("\n\n\n===== Teste Dia 5 - SETTINGS =====");

    AccessibilitySettings s1 = AccessibilitySettings.defaults();
    AccessibilitySettings s2 = s1.withFontSize(22);

    System.out.println("s1 fontSize = " + s1.getFontSize());
    System.out.println("s2 fontSize = " + s2.getFontSize());
  }

  private void testRepository() {
    System.out.println("\n\n\n===== Teste Repositório =====");
    listDocuments();
  }

  private void testDay6AccessibilityProfile() {
    System.out.println("\n\n\n===== Teste Dia 6 - AccessibilityProfile =====");

    AccessibilityProfile profile1 = AccessibilityProfile.defaultProfile("Padrão");

    AccessibilitySettings customSettings = profile1.getSettings()
            .withFontSize(22)
            .withLineHeight(1.8)
            .withTheme(Theme.HIGH_CONTRAST);

    AccessibilityProfile profile2 = profile1.withSettings(customSettings);

    System.out.println(profile1);
    System.out.println(profile2);
  }

  private void testDay7WizardService() {
    System.out.println("\n\n\n===== Teste Dia 7 - WizardService =====");

    WizardService wizardService = new WizardService();

    WizardAnswers answers = new WizardAnswers(
            false, // screenReader
            true,  // lowVision
            true,  // dyslexia
            true,  // attentionDifficulty
            true,  // reducedMotionPreference
            true   // keyboardPreferred
    );

    AccessibilityProfile recommendedProfile =
            wizardService.recommendProfile("Perfil Recomendado", answers);

    System.out.println("Perfil recomendado pelo wizard:");
    System.out.println(recommendedProfile);
  }

  private void testDay8ProfileService() {
    System.out.println("\n\n\n===== Teste Dia 8 - ProfileService =====");

    ProfileService profileService = new ProfileService();

    AccessibilityProfile baseProfile = AccessibilityProfile.defaultProfile("Padrão");
    AccessibilitySettings customSettings = baseProfile.getSettings()
            .withFontSize(22)
            .withLineHeight(1.8)
            .withTheme(Theme.HIGH_CONTRAST);

    profileService.createDefaultProfile("Perfil Padrão");
    profileService.save(new AccessibilityProfile("Baixa Visão", customSettings));

    System.out.println("Perfis cadastrados:");
    profileService.findAll().forEach(System.out::println);

    System.out.println("Buscando perfil pelo nome:");
    System.out.println(profileService.findByName("Baixa Visão"));
  }

  private void testDay9ReadingSession() {
    System.out.println("\n\n\n===== Teste Dia 9 - ReadingSession =====");

    Document document = documentRepository.findById("doc1")
            .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

    AccessibilityProfile profile = AccessibilityProfile.defaultProfile("Teste");

    List<String> lines = List.of(
            "Linha 1", "Linha 2", "Linha 3", "Linha 4", "Linha 5",
            "Linha 6", "Linha 7", "Linha 8", "Linha 9", "Linha 10",
            "Linha 11", "Linha 12"
    );

    ReadingSession session = new ReadingSession(document, profile, lines, 5, 0);

    System.out.println("Página atual:");
    System.out.println(session.currentPage());

    session.nextPage();
    System.out.println("Após nextPage():");
    System.out.println(session.currentPage());

    session.prevPage();
    System.out.println("Após prevPage():");
    System.out.println(session.currentPage());
  }

  private void testDay10TextWrapper() {
    System.out.println("\n\n\n===== Teste Dia 10 - TextWrapper =====");

    TextWrapper wrapper = new TextWrapper();

    Document doc = new Document(
            "1",
            "Texto de Teste",
            "Este é um exemplo de conteúdo para validar a quebra de linhas no leitor adaptativo. " +
                    "Este é um exemplo de conteúdo para validar a quebra de linhas no leitor adaptativo. " +
                    "Este é um exemplo de conteúdo para validar a quebra de linhas no leitor adaptativo. " +
                    "Este é um exemplo de conteúdo para validar a quebra de linhas no leitor adaptativo."
    );

    AccessibilityProfile profileWrapper = new AccessibilityProfile(
            "Perfil Padrão",
            AccessibilitySettings.defaults()
    );

    List<String> lines = wrapper.wrap(
            doc.getContent(),
            profileWrapper.getSettings().getColumnWidth()
    );

    System.out.println("Linhas geradas pelo TextWrapper:");
    lines.forEach(System.out::println);
  }

  private void testDay11ReadingService() {
    System.out.println("\n\n\n===== Teste Dia 11 - ReadingService =====");

    ReadingService readingService = new ReadingService();

    Document document = documentRepository.findById("doc3")
            .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

    AccessibilityProfile profile = AccessibilityProfile.defaultProfile("Teste");

    ReadingSession session = readingService.startSession(document, profile);

    System.out.println("Primeira página:");
    session.currentPage().forEach(System.out::println);

    System.out.println("\nPróxima página:");
    session.nextPage();
    session.currentPage().forEach(System.out::println);
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

 */