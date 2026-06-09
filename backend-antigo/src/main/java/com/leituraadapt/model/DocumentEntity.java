package com.leituraadapt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class DocumentEntity {

    @Id
    private String id;

    private String title;

    @Lob
    private String content;

    private String filePath; //ICEPDF

    private String fileType; //ICEPDF

    private Integer totalPages; //ICEPDF

    public DocumentEntity() {}

    // Construtor antigo, usado pelo DataLoader
    public DocumentEntity(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.fileType = "TEXT";
        this.filePath = null;
        this.totalPages = null;
    }

    // Construtor novo, caso você queira informar o tipo
    public DocumentEntity(String id, String title, String content, String fileType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.fileType = fileType;
        this.filePath = null;
        this.totalPages = null;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getFilePath() { return filePath; }
    public String getFileType() { return fileType; }
    public Integer getTotalPages() { return totalPages; }
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
}