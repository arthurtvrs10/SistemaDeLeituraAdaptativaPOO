package com.leituraadapt.service;

import com.leituraadapt.model.DocumentEntity;
import com.leituraadapt.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public List<DocumentEntity> findAll() {
        return repository.findAll();
    }

    public Optional<DocumentEntity> findById(String id) {
        return repository.findById(id);
    }

    public DocumentEntity save(DocumentEntity document) {
        return repository.save(document);
    }
}