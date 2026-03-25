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

    public ReadingController(DocumentService documentService,
                             ReadingProgressService readingProgressService,
                             AccessibilityProfileService accessibilityProfileService) {
        this.documentService = documentService;
        this.readingProgressService = readingProgressService;
        this.accessibilityProfileService = accessibilityProfileService;
        this.textWrapper = new TextWrapper();
    }

    @GetMapping("/{id}")
    public ReadingResponseDTO read(
            @PathVariable String id,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false) Integer page
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
                    p.setLineHeight(1.5);
                    p.setColumnWidth(40);
                    p.setTheme("LIGHT");
                    p.setFocusMode(false);
                    p.setReducedMotion(true);
                    p.setDyslexiaFriendlyFont(false);
                    p.setColorBlindMode(false);
                    p.setKeyboardPreferred(true);
                    return p;
                });

        List<String> lines = textWrapper.wrap(doc.getContent(), profile.getColumnWidth());

        int resolvedPage = 1;

        if (page != null) {
            resolvedPage = page;
        } else {
            var saved = readingProgressService.findByUserIdAndDocumentId(email, id);
            if (saved.isPresent()) {
                resolvedPage = saved.get().getCurrentPage();
            }
        }

        int totalLines = lines.size();
        int totalPages = (int) Math.ceil((double) totalLines / pageSize);

        if (totalPages == 0) {
            totalPages = 1;
        }

        if (resolvedPage < 1) {
            resolvedPage = 1;
        }

        if (resolvedPage > totalPages) {
            resolvedPage = totalPages;
        }

        int start = (resolvedPage - 1) * pageSize;
        int end = Math.min(totalLines, start + pageSize);

        List<String> pageContent = lines.subList(start, end);

        readingProgressService.saveOrUpdate(email, id, resolvedPage, pageSize);

        boolean lastPage = resolvedPage >= totalPages;
        boolean hasNext = resolvedPage < totalPages;
        boolean hasPrevious = resolvedPage > 1;

        return new ReadingResponseDTO(
                doc.getTitle(),
                pageContent,
                resolvedPage,
                pageSize,
                totalPages,
                totalLines,
                lastPage,
                hasNext,
                hasPrevious
        );
    }
}