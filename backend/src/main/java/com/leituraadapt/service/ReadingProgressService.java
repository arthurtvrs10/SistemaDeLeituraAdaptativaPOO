package com.leituraadapt.service;

import com.leituraadapt.model.ReadingProgressEntity;
import com.leituraadapt.repository.ReadingProgressRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReadingProgressService {

    private final ReadingProgressRepository repository;

    public ReadingProgressService(ReadingProgressRepository repository) {
        this.repository = repository;
    }

    public ReadingProgressEntity save(ReadingProgressEntity progress) {
        return repository.save(progress);
    }

    public Optional<ReadingProgressEntity> findById(String id) {
        return repository.findById(id);
    }

    public Optional<ReadingProgressEntity> findByUserIdAndDocumentId(String userId, String documentId) {
        return repository.findByUserIdAndDocumentId(userId, documentId);
    }

    public ReadingProgressEntity saveOrUpdate(String userId, String documentId, int currentPage, int pageSize) {
        String id = "progress-" + userId + "-" + documentId;

        ReadingProgressEntity progress = repository.findById(id)
                .orElseGet(() -> {
                    ReadingProgressEntity p = new ReadingProgressEntity();
                    p.setId(id);
                    p.setUserId(userId);
                    p.setDocumentId(documentId);
                    return p;
                });

        progress.setCurrentPage(currentPage);
        progress.setPageSize(pageSize);

        return repository.save(progress);
    }
}