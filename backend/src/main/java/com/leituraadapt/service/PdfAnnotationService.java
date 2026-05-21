package com.leituraadapt.service;

import com.leituraadapt.model.PdfAnnotationEntity;
import com.leituraadapt.repository.PdfAnnotationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PdfAnnotationService {

    private final PdfAnnotationRepository repository;

    public PdfAnnotationService(PdfAnnotationRepository repository) {
        this.repository = repository;
    }

    public PdfAnnotationEntity save(PdfAnnotationEntity annotation) {
        if (annotation.getId() == null || annotation.getId().isBlank()) {
            annotation.setId(UUID.randomUUID().toString());
        }

        return repository.save(annotation);
    }

    public List<PdfAnnotationEntity> findByUserAndDocument(
            String userId,
            String documentId
    ) {
        return repository.findByUserIdAndDocumentId(userId, documentId);
    }

    public List<PdfAnnotationEntity> findByUserDocumentAndPage(
            String userId,
            String documentId,
            int page
    ) {
        return repository.findByUserIdAndDocumentIdAndPage(userId, documentId, page);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}