package com.leituraadapt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PdfAnnotationEntity {

    @Id
    private String id;

    private String userId;

    private String documentId;

    private int page;

    private String type;

    private String selectedText;

    private String note;

    private Double x;

    private Double y;

    private Double width;

    private Double height;

    public PdfAnnotationEntity() {}

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public int getPage() {
        return page;
    }

    public String getType() {
        return type;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public String getNote() {
        return note;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}