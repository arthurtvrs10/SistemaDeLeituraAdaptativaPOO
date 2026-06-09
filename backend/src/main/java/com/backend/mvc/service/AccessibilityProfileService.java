package com.backend.mvc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.mvc.dto.AppliedAccessibilityResponseDTO;
import com.backend.mvc.model.AccessibilityProfile;
import com.backend.mvc.repository.AccessibilityProfileRepository;
import com.backend.mvc.service.configuration.DefaultReadingConfiguration;
import com.backend.mvc.service.configuration.HighContrastConfiguration;
import com.backend.mvc.service.configuration.LargeFontConfiguration;
import com.backend.mvc.service.configuration.ReadingConfiguration;

// Indica que essa classe é uma camada de serviço do Spring
// A camada Service concentra as regras de negócio
@Service
public class AccessibilityProfileService {

    // Repository usado para acessar o banco de dados
    private final AccessibilityProfileRepository accessibilityProfileRepository;

    // Injeção de dependência via construtor
    // O Spring entrega automaticamente uma instância do repository
    public AccessibilityProfileService(AccessibilityProfileRepository accessibilityProfileRepository) {
        this.accessibilityProfileRepository = accessibilityProfileRepository;
    }

    // Cria um novo perfil de acessibilidade
    public AccessibilityProfile create(
            String name,
            Integer fontSize,
            Double lineSpacing,
            Boolean highContrast,
            String theme
    ) {

        // Cria um objeto AccessibilityProfile com os dados recebidos
        AccessibilityProfile profile = new AccessibilityProfile(
                name,
                fontSize,
                lineSpacing,
                highContrast,
                theme
        );

        // Salva o perfil no banco de dados
        // Depois retorna o perfil já salvo
        return accessibilityProfileRepository.save(profile);
    }

    // Lista todos os perfis de acessibilidade cadastrados
    public List<AccessibilityProfile> findAll() {

        // Chama o repository para buscar todos os registros no banco
        return accessibilityProfileRepository.findAll();
    }

    // Busca um perfil de acessibilidade pelo ID
    public AccessibilityProfile findById(Long id) {

        // Tenta buscar o perfil pelo ID
        // Se encontrar, retorna o perfil
        // Se não encontrar, lança uma exceção
        return accessibilityProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil de acessibilidade não encontrado."));
    }

    // Atualiza um perfil de acessibilidade existente
    public AccessibilityProfile update(
            Long id,
            String name,
            Integer fontSize,
            Double lineSpacing,
            Boolean highContrast,
            String theme
    ) {

        // Primeiro busca o perfil pelo ID
        // Isso garante que só vamos atualizar se ele existir
        AccessibilityProfile profile = findById(id);

        // Atualiza o nome do perfil
        profile.setName(name);

        // Atualiza o tamanho da fonte
        profile.setFontSize(fontSize);

        // Atualiza o espaçamento entre linhas
        profile.setLineSpacing(lineSpacing);

        // Atualiza a opção de alto contraste
        profile.setHighContrast(highContrast);

        // Atualiza o tema visual
        profile.setTheme(theme);

        // Salva novamente no banco com os novos dados
        return accessibilityProfileRepository.save(profile);
    }

    // Exclui um perfil de acessibilidade pelo ID
    public void delete(Long id) {

        // Busca o perfil antes de excluir
        // Se ele não existir, o findById lança erro
        AccessibilityProfile profile = findById(id);

        // Remove o perfil do banco de dados
        accessibilityProfileRepository.delete(profile);
    }

    // Aplica uma configuração de acessibilidade ao perfil informado
    // Aqui usamos classe abstrata, sobrescrita, try...catch e thread
    public AppliedAccessibilityResponseDTO applyConfiguration(Long id) {

        try {
            // Busca o perfil no banco
            AccessibilityProfile profile = findById(id);

            // Variável do tipo da classe abstrata
            // Ela pode receber qualquer classe filha de ReadingConfiguration
            ReadingConfiguration configuration;

            // Se o perfil estiver com alto contraste ativado,
            // usa a configuração específica de alto contraste
            if (Boolean.TRUE.equals(profile.getHighContrast())) {
                configuration = new HighContrastConfiguration();
            }

            // Se a fonte for maior ou igual a 24,
            // usa a configuração específica de fonte ampliada
            else if (profile.getFontSize() != null && profile.getFontSize() >= 24) {
                configuration = new LargeFontConfiguration();
            }

            // Caso contrário, usa a configuração padrão
            else {
                configuration = new DefaultReadingConfiguration();
            }

            // Aplica a configuração usando polimorfismo
            // O método chamado será o método sobrescrito da classe concreta
            String message = configuration.applyConfiguration(profile);

            // Cria uma thread para simular processamento em segundo plano
            Thread metricsThread = new Thread(() -> {
                try {
                    // Simula um processamento secundário
                    Thread.sleep(3000);

                    // Simula registro de métrica no terminal
                    System.out.println("Métrica processada em segundo plano.");
                    System.out.println("Perfil aplicado: " + profile.getName());
                    System.out.println("Configuração usada: " + configuration.getConfigurationName());

                } catch (InterruptedException e) {
                    // Trata erro caso a thread seja interrompida
                    System.out.println("Thread de métricas foi interrompida.");

                    // Marca novamente a thread como interrompida
                    Thread.currentThread().interrupt();
                }
            });

            // Inicia a thread
            // A API não precisa esperar essa thread terminar para responder ao cliente
            metricsThread.start();

            // Retorna a resposta principal imediatamente
            return new AppliedAccessibilityResponseDTO(
                    profile.getName(),
                    configuration.getConfigurationName(),
                    message
            );

        } catch (RuntimeException e) {
            // Trata erros de regra de negócio, como perfil não encontrado
            throw new RuntimeException("Erro ao aplicar configuração de acessibilidade: " + e.getMessage());

        } catch (Exception e) {
            // Trata erros inesperados
            throw new RuntimeException("Erro inesperado ao aplicar configuração de acessibilidade.");
        }
    }
}