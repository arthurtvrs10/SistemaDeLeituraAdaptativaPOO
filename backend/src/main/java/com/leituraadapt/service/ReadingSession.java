package com.leituraadapt.service;

import com.leituraadapt.model.AccessibilityProfile;
import com.leituraadapt.model.Document;

import java.util.List;
import java.util.Objects;

public class ReadingSession {
    private final Document document;
    private final AccessibilityProfile profile;
    private final List<String> lines;
    private final int pageSize;
    private int offset;

    public ReadingSession(Document document, AccessibilityProfile profile, List<String> lines,int pageSize, int startOffSet){
        this.document = Objects.requireNonNull(document, "document nao pode ser nulo");
        this.profile = Objects.requireNonNull(profile, "profile nao pode ser nulo");
        this.lines = Objects.requireNonNull(lines, "lines nao pode ser nulo");

        if(pageSize <= 0){
            throw new IllegalArgumentException("PageSize deve ser maior que zero");
        }
        if(startOffSet < 0){
            throw new IllegalArgumentException("startOffSet nao pode ser negativo");
        }
        this.pageSize = pageSize;
        this.offset = Math.min(startOffSet, lines.size());
    }
    public int nextPage(){
        offset = Math.min( lines.size(), offset + pageSize);
        return offset;
    }
    public int prevPage(){
        offset = Math.max(0, offset - pageSize);
        return offset;
    }
    public List<String> currentPage(){
        int end = Math.min(lines.size(), offset + pageSize);
        return lines.subList(offset, end);
    }
    public int currentOffset(){
        return offset;
    }
    public boolean isFinished(){
        return offset >= lines.size();
    }
    public Document getDocument(){
        return document;
    }
    public AccessibilityProfile getProfile(){
        return profile;
    }
    public int getPageSize(){
        return pageSize;
    }
}
