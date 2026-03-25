package com.leituraadapt.repository;

import com.leituraadapt.model.ReadingProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgressEntity, String> {
    Optional<ReadingProgressEntity> findByUserIdAndDocumentId(String userId, String documentId);
}