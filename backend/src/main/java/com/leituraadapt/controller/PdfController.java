package com.leituraadapt.controller;

import com.leituraadapt.model.DocumentEntity;
import com.leituraadapt.service.DocumentService;
import com.leituraadapt.service.PdfRenderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final DocumentService documentService;
    private final PdfRenderService pdfRenderService;

    public PdfController(
            DocumentService documentService,
            PdfRenderService pdfRenderService
    ) {
        this.documentService = documentService;
        this.pdfRenderService = pdfRenderService;
    }

    @GetMapping(value = "/{documentId}/page/{page}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> renderPage(
            @PathVariable String documentId,
            @PathVariable int page,
            @RequestParam(defaultValue = "144") float dpi
    ) {
        DocumentEntity document = documentService.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        validarDocumentoPdf(document);

        byte[] image = pdfRenderService.renderPageAsPng(
                document.getFilePath(),
                page,
                dpi
        );

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    @GetMapping("/{documentId}/total-pages")
    public ResponseEntity<Integer> getTotalPages(@PathVariable String documentId) {
        DocumentEntity document = documentService.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        validarDocumentoPdf(document);

        int totalPages = pdfRenderService.getTotalPages(document.getFilePath());

        return ResponseEntity.ok(totalPages);
    }

    @GetMapping(value = "/{documentId}/text", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> extractText(@PathVariable String documentId) {
        DocumentEntity document = documentService.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        validarDocumentoPdf(document);

        String text = pdfRenderService.extractText(document.getFilePath());

        return ResponseEntity.ok(text);
    }

    @GetMapping(value = "/{documentId}/page/{page}/text", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> extractTextFromPage(
            @PathVariable String documentId,
            @PathVariable int page
    ) {
        DocumentEntity document = documentService.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        validarDocumentoPdf(document);

        String text = pdfRenderService.extractTextFromPage(
                document.getFilePath(),
                page
        );

        return ResponseEntity.ok(text);
    }

    private void validarDocumentoPdf(DocumentEntity document) {
        if (!"PDF".equalsIgnoreCase(document.getFileType())) {
            throw new RuntimeException("Documento não é do tipo PDF");
        }

        if (document.getFilePath() == null || document.getFilePath().isBlank()) {
            throw new RuntimeException("Documento PDF não possui caminho de arquivo");
        }
    }
}