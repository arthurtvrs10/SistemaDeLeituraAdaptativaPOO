package com.backend.mvc.repository;

import com.backend.mvc.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
