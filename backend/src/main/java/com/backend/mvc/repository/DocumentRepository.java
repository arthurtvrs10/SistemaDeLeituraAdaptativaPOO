package com.backend.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.mvc.model.Document;

// import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
