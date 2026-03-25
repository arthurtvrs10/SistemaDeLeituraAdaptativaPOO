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

    public DocumentEntity() {}

    public DocumentEntity(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
}