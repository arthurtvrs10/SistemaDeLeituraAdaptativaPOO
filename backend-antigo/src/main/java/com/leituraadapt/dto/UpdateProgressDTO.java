package com.leituraadapt.dto;

public class UpdateProgressDTO {

    private String userId;
    private String documentId;
    private int currentPage;
    private int pageSize;

    public UpdateProgressDTO(){
    }

    public UpdateProgressDTO(String userId, String documentId, int currentPage, int pageSize){
        this.userId = userId;
        this.documentId = documentId;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public String getUserId(){
        return userId;
    }

    public String getDocumentId(){
        return documentId;
    }

    public int getCurrentPage(){
        return currentPage;
    }

    public int getPageSize(){
        return pageSize;
    }
}
