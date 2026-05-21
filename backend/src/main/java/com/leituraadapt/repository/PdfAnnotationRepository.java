package com.leituraadapt.repository;

import com.leituraadapt.model.PdfAnnotationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PdfAnnotationRepository extends JpaRepository<PdfAnnotationEntity, String> {

    List<PdfAnnotationEntity> findByUserIdAndDocumentId(String userId, String documentId);

    List<PdfAnnotationEntity> findByUserIdAndDocumentIdAndPage(
            String userId,
            String documentId,
            int page
    );
}