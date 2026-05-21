package com.leituraadapt.controller;

import com.leituraadapt.model.DocumentEntity;
import com.leituraadapt.service.DocumentService;
import com.leituraadapt.service.PdfRenderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService service;
    private final PdfRenderService pdfRenderService;

    public DocumentController(
            DocumentService service,
            PdfRenderService pdfRenderService
    ) {
        this.service = service;
        this.pdfRenderService = pdfRenderService;
    }

    @GetMapping
    public List<DocumentEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> findById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DocumentEntity> save(@RequestBody DocumentEntity document) {
        if (document.getFileType() == null || document.getFileType().isBlank()) {
            document.setFileType("TEXT");
        }

        DocumentEntity saved = service.save(document);
        return ResponseEntity.ok(saved);
    }

    @PostMapping(value = "/upload-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentEntity> uploadPdf(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Arquivo PDF vazio");
            }

            String originalFilename = file.getOriginalFilename();

            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
                throw new RuntimeException("O arquivo enviado não é PDF");
            }

            Path uploadDir = Paths.get("uploads", "pdf");
            Files.createDirectories(uploadDir);

            String id = UUID.randomUUID().toString();
            String safeFileName = id + ".pdf";

            Path filePath = uploadDir.resolve(safeFileName);
            Files.write(filePath, file.getBytes());

            int totalPages = pdfRenderService.getTotalPages(filePath.toString());

            DocumentEntity document = new DocumentEntity();
            document.setId(id);
            document.setTitle(title);
            document.setFileType("PDF");
            document.setFilePath(filePath.toString());
            document.setTotalPages(totalPages);
            document.setContent(null);

            DocumentEntity saved = service.save(document);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do PDF", e);
        }
    }
}