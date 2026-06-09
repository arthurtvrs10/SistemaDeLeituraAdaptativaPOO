package com.backend.mvc.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // é salvo como um VARCHAR(36) OU BINARY(16)
                    // "123e4567-e89b-12d3-a456-426614174000"  -

    private String originalFileName;

    private String title; // representa o nome original do arquivo PDF

    private String filePath; // caminho onde o arquivo foi salvo no servidor

    private int totalPages; // total de paginas do PDF

    private LocalDateTime uploadedAt; // deve guardar data e hora do upload

    public Document(){
    }

    public Document(String title, String originalFileName, String filePath, Integer totalPages) {
        this.title = title;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.totalPages = totalPages;
        this.uploadedAt = LocalDateTime.now();
    }

     public long getId() {
        return id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
