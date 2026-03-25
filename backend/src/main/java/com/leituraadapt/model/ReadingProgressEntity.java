package com.leituraadapt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ReadingProgressEntity {

    @Id
    private String id;

    private String userId;
    private String documentId;
    private int currentPage;
    private int pageSize;

    public ReadingProgressEntity() {
    }

    // GETTERS E SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}