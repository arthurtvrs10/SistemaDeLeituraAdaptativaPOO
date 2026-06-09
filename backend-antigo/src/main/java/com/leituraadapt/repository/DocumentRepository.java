package com.leituraadapt.repository;

import com.leituraadapt.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, String> {
}
// Spring cria automaticamente uma implementação dessa interface
// Conecta com o banco (H2 no seu caso)
//  Executa SQL sem você escrever SQL