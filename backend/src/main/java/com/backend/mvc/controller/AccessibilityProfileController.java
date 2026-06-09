package com.backend.mvc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.mvc.dto.AppliedAccessibilityResponseDTO;
import com.backend.mvc.model.AccessibilityProfile;
import com.backend.mvc.service.AccessibilityProfileService;

// Indica que essa classe é um Controller REST
// Ela recebe requisições HTTP e retorna respostas em JSON
@RestController

// Define o caminho base das rotas desse controller
@RequestMapping("/api/accessibility-profiles")
public class AccessibilityProfileController {

    // Service responsável pelas regras de acessibilidade
    private final AccessibilityProfileService accessibilityProfileService;

    // Injeção de dependência via construtor
    // O Spring entrega automaticamente o service
    public AccessibilityProfileController(AccessibilityProfileService accessibilityProfileService) {
        this.accessibilityProfileService = accessibilityProfileService;
    }

    // Rota para criar um novo perfil de acessibilidade
    // Método HTTP: POST
    // URL: /api/accessibility-profiles
    @PostMapping
    public ResponseEntity<AccessibilityProfile> create(@RequestBody AccessibilityProfile profile) {

        // Recebe o JSON enviado na requisição e pega os dados do objeto profile
        // Depois envia esses dados para o service criar o perfil
        AccessibilityProfile createdProfile = accessibilityProfileService.create(
                profile.getName(),
                profile.getFontSize(),
                profile.getLineSpacing(),
                profile.getHighContrast(),
                profile.getTheme()
        );

        // Retorna o perfil criado com status HTTP 200
        return ResponseEntity.ok(createdProfile);
    }

    // Rota para listar todos os perfis de acessibilidade
    // Método HTTP: GET
    // URL: /api/accessibility-profiles
    @GetMapping
    public ResponseEntity<List<AccessibilityProfile>> findAll() {

        // Chama o service para buscar todos os perfis
        List<AccessibilityProfile> profiles = accessibilityProfileService.findAll();

        // Retorna a lista de perfis em JSON
        return ResponseEntity.ok(profiles);
    }

    // Rota para buscar um perfil pelo ID
    // Método HTTP: GET
    // URL: /api/accessibility-profiles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AccessibilityProfile> findById(@PathVariable Long id) {

        // Pega o ID da URL e manda para o service buscar
        AccessibilityProfile profile = accessibilityProfileService.findById(id);

        // Retorna o perfil encontrado
        return ResponseEntity.ok(profile);
    }

    // Rota para atualizar um perfil existente
    // Método HTTP: PUT
    // URL: /api/accessibility-profiles/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AccessibilityProfile> update(
            @PathVariable Long id,
            @RequestBody AccessibilityProfile profile
    ) {

        // Pega o ID da URL e os novos dados do corpo da requisição
        // Depois manda tudo para o service atualizar
        AccessibilityProfile updatedProfile = accessibilityProfileService.update(
                id,
                profile.getName(),
                profile.getFontSize(),
                profile.getLineSpacing(),
                profile.getHighContrast(),
                profile.getTheme()
        );

        // Retorna o perfil atualizado
        return ResponseEntity.ok(updatedProfile);
    }

    // Rota para excluir um perfil pelo ID
    // Método HTTP: DELETE
    // URL: /api/accessibility-profiles/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        // Chama o service para excluir o perfil
        accessibilityProfileService.delete(id);

        // Retorna HTTP 204 No Content
        // Isso significa: exclusão feita com sucesso, sem corpo na resposta
        return ResponseEntity.noContent().build();
    }

    // Rota para aplicar uma configuração de acessibilidade em um perfil
    // Método HTTP: GET
    // URL: /api/accessibility-profiles/{id}/apply
    @GetMapping("/{id}/apply")
    public ResponseEntity<AppliedAccessibilityResponseDTO> applyConfiguration(@PathVariable Long id) {

        // Pega o ID da URL
        // Depois chama o service para aplicar a configuração correspondente ao perfil
        AppliedAccessibilityResponseDTO response = accessibilityProfileService.applyConfiguration(id);

        // Retorna o resultado da configuração aplicada
        return ResponseEntity.ok(response);
    }
}