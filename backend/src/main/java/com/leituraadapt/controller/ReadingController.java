package com.leituraadapt.controller;

import com.leituraadapt.dto.ReadingResponseDTO;
import com.leituraadapt.model.AccessibilityProfileEntity;
import com.leituraadapt.model.DocumentEntity;
import com.leituraadapt.service.AccessibilityProfileService;
import com.leituraadapt.service.DocumentService;
import com.leituraadapt.service.ReadingProgressService;
import com.leituraadapt.service.TextWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reading")
public class ReadingController {

    private final DocumentService documentService;
    private final ReadingProgressService readingProgressService;
    private final AccessibilityProfileService accessibilityProfileService;
    private final TextWrapper textWrapper;

    public ReadingController(
            DocumentService documentService,
            ReadingProgressService readingProgressService,
            AccessibilityProfileService accessibilityProfileService
    ) {
        this.documentService = documentService;
        this.readingProgressService = readingProgressService;
        this.accessibilityProfileService = accessibilityProfileService;
        this.textWrapper = new TextWrapper();
    }

    @GetMapping("/{id}")
    public ReadingResponseDTO read(
            @PathVariable String id,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer fontSize,
            @RequestParam(required = false) Double lineHeight,
            @RequestParam(required = false) Integer columnWidth,
            @RequestParam(required = false, defaultValue = "false") boolean dyslexiaMode
    ) {
        DocumentEntity doc = documentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        AccessibilityProfileEntity profile = accessibilityProfileService.findByUserId(email)
                .orElseGet(() -> {
                    AccessibilityProfileEntity p = new AccessibilityProfileEntity();
                    p.setId("default-" + email);
                    p.setUserId(email);
                    p.setFontSize(18);
                    p.setLineHeight(1.6);
                    p.setColumnWidth(78);
                    p.setTheme("LIGHT");
                    p.setFocusMode(false);
                    p.setReducedMotion(true);
                    p.setDyslexiaFriendlyFont(false);
                    p.setColorBlindMode(false);
                    p.setKeyboardPreferred(true);
                    return p;
                });

        int resolvedFontSize = fontSize != null ? fontSize : profile.getFontSize();
        double resolvedLineHeight = lineHeight != null ? lineHeight : profile.getLineHeight();
        int resolvedColumnWidth = columnWidth != null ? columnWidth : profile.getColumnWidth();
        boolean resolvedDyslexiaMode = dyslexiaMode || profile.isDyslexiaFriendlyFont();

        List<String> lines = textWrapper.wrap(doc.getContent(), resolvedColumnWidth);

        int linesPerPage = calculateLinesPerPage(
                resolvedFontSize,
                resolvedLineHeight,
                resolvedDyslexiaMode
        );

        int resolvedPage;
        if (page != null) {
            resolvedPage = page;
        } else {
            resolvedPage = readingProgressService.findByUserIdAndDocumentId(email, id)
                    .map(progress -> progress.getCurrentPage())
                    .orElse(1);
        }

        int totalLines = lines.size();
        int totalPages = (int) Math.ceil((double) totalLines / linesPerPage);
        if (totalPages == 0) {
            totalPages = 1;
        }

        if (resolvedPage < 1) {
            resolvedPage = 1;
        }
        if (resolvedPage > totalPages) {
            resolvedPage = totalPages;
        }

        int start = (resolvedPage - 1) * linesPerPage;
        int end = Math.min(totalLines, start + linesPerPage);

        List<String> pageContent = lines.subList(start, end);

        readingProgressService.saveOrUpdate(email, id, resolvedPage, linesPerPage);

        boolean lastPage = resolvedPage >= totalPages;
        boolean hasNext = resolvedPage < totalPages;
        boolean hasPrevious = resolvedPage > 1;

        return new ReadingResponseDTO(
                doc.getTitle(),
                pageContent,
                resolvedPage,
                linesPerPage,
                totalPages,
                totalLines,
                lastPage,
                hasNext,
                hasPrevious
        );
    }

    private int calculateLinesPerPage(int fontSize, double lineHeight, boolean dyslexiaMode) {
        int pageHeightPx = 1100;
        int verticalPaddingPx = 96; // 48 top + 48 bottom
        int usableHeightPx = pageHeightPx - verticalPaddingPx;

        double effectiveLineHeightPx = fontSize * lineHeight;

        if (dyslexiaMode) {
            effectiveLineHeightPx += fontSize * 0.35;
        }

        int linesPerPage = (int) Math.floor(usableHeightPx / effectiveLineHeightPx);
        return Math.max(linesPerPage, 1);
    }
}