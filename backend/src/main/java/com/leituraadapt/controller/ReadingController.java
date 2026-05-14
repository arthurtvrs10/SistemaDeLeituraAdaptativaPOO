package com.leituraadapt.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leituraadapt.dto.ReadingResponseDTO;
import com.leituraadapt.model.AccessibilityProfileEntity;
import com.leituraadapt.model.DocumentEntity;
import com.leituraadapt.service.AccessibilityProfileService;
import com.leituraadapt.service.DocumentService;
import com.leituraadapt.service.ReadingProgressService;
import com.leituraadapt.service.TextWrapper;

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

        try {

            DocumentEntity doc = documentService.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Documento não encontrado")
                    );

            Authentication auth = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            String email = auth.getName();

            AccessibilityProfileEntity profile =
                    accessibilityProfileService.findByUserId(email)
                            .orElseGet(() -> {

                                AccessibilityProfileEntity p =
                                        new AccessibilityProfileEntity();

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

            int resolvedFontSize =
                    fontSize != null
                            ? fontSize
                            : profile.getFontSize();

            double resolvedLineHeight =
                    lineHeight != null
                            ? lineHeight
                            : profile.getLineHeight();

            int resolvedColumnWidth =
                    columnWidth != null
                            ? columnWidth
                            : profile.getColumnWidth();

            boolean resolvedDyslexiaMode =
                    dyslexiaMode
                            || profile.isDyslexiaFriendlyFont();

            List<String> lines = textWrapper.wrap(
                    doc.getContent(),
                    resolvedColumnWidth
            );

            int linesPerPage = calculateLinesPerPage(
                    resolvedFontSize,
                    resolvedLineHeight,
                    resolvedDyslexiaMode
            );

            int resolvedPage;

            if (page != null) {

                resolvedPage = page;

            } else {

                resolvedPage = readingProgressService
                        .findByUserIdAndDocumentId(email, id)
                        .map(progress ->
                                progress.getCurrentPage()
                        )
                        .orElse(1);
            }

            int totalLines = lines.size();

            int totalPages = (int) Math.ceil(
                    (double) totalLines / linesPerPage
            );

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

            int end = Math.min(
                    totalLines,
                    start + linesPerPage
            );

            List<String> pageContent =
                    lines.subList(start, end);

            try{
                readingProgressService.saveOrUpdate(
                    email,
                    id,
                    resolvedPage,
                    linesPerPage
                );

                // Thread para processamento assíncrono
                Thread thread = new Thread(() -> {

                    try {

                        System.out.println(
                                "Processando métricas de leitura..."
                        );

                        Thread.sleep(3000);

                        System.out.println(
                                "Métricas processadas com sucesso."
                        );

                    } catch (InterruptedException e) {

                        System.out.println(
                                "Erro na thread: "
                                        + e.getMessage()
                        );
                    }
                });

                thread.start();
            }catch (Exception e) {

                System.out.println(
                        "Erro ao salvar progresso da leitura: "
                                + e.getMessage()
                );

                throw new RuntimeException(
                        "Falha ao processar leitura."
                );
            }

            

            boolean lastPage =
                    resolvedPage >= totalPages;

            boolean hasNext =
                    resolvedPage < totalPages;

            boolean hasPrevious =
                    resolvedPage > 1;

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

        } catch (Exception e) {

            System.out.println(
                    "Erro na sessão de leitura: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "Falha ao processar leitura."
            );
        }
    }

    private int calculateLinesPerPage(
            int fontSize,
            double lineHeight,
            boolean dyslexiaMode
    ) {

        int pageHeightPx = 1100;

        int verticalPaddingPx = 96;

        int usableHeightPx =
                pageHeightPx - verticalPaddingPx;

        double effectiveLineHeightPx =
                fontSize * lineHeight;

        if (dyslexiaMode) {

            effectiveLineHeightPx +=
                    fontSize * 0.35;
        }

        int linesPerPage = (int) Math.floor(
                usableHeightPx / effectiveLineHeightPx
        );

        return Math.max(linesPerPage, 1);
    }
}