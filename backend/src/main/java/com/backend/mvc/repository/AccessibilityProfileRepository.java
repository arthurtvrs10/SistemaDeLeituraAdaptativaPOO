package com.backend.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.mvc.model.AccessibilityProfile;

// Repository responsável por acessar o banco de dados
// Ele trabalha com a entidade AccessibilityProfile
// O tipo do ID da entidade é Long
public interface AccessibilityProfileRepository extends JpaRepository<AccessibilityProfile, Long> {

    // Não precisamos criar métodos básicos manualmente
    // O JpaRepository já fornece:
    // save()
    // findAll()
    // findById()
    // delete()
}