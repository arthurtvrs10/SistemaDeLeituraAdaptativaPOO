package com.leituraadapt.dto;

import java.util.List;

public class ReadingResponseDTO {

    private String documentTitle;
    private List<String> lines;

    private int currentPage;
    private int pageSize;
    private int totalPages;
    private int totalLines;

    private boolean lastPage;
    private boolean hasNext;
    private boolean hasPrevious;

    public ReadingResponseDTO(
            String documentTitle,
            List<String> lines,
            int currentPage,
            int pageSize,
            int totalPages,
            int totalLines,
            boolean lastPage,
            boolean hasNext,
            boolean hasPrevious
    ) {
        this.documentTitle = documentTitle;
        this.lines = lines;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalLines = totalLines;
        this.lastPage = lastPage;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public String getDocumentTitle() { return documentTitle; }
    public List<String> getLines() { return lines; }
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
    public int getTotalPages() { return totalPages; }
    public int getTotalLines() { return totalLines; }
    public boolean isLastPage() { return lastPage; }
    public boolean isHasNext() { return hasNext; }
    public boolean isHasPrevious() { return hasPrevious; }
}