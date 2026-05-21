package com.leituraadapt.controller;

import com.leituraadapt.model.PdfAnnotationEntity;
import com.leituraadapt.service.PdfAnnotationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pdf-annotations")
public class PdfAnnotationController {

    private final PdfAnnotationService service;

    public PdfAnnotationController(PdfAnnotationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PdfAnnotationEntity> save(
            @RequestBody PdfAnnotationEntity annotation
    ) {
        PdfAnnotationEntity saved = service.save(annotation);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<PdfAnnotationEntity>> findByUserAndDocument(
            @RequestParam String userId,
            @RequestParam String documentId
    ) {
        return ResponseEntity.ok(
                service.findByUserAndDocument(userId, documentId)
        );
    }

    @GetMapping("/page")
    public ResponseEntity<List<PdfAnnotationEntity>> findByUserDocumentAndPage(
            @RequestParam String userId,
            @RequestParam String documentId,
            @RequestParam int page
    ) {
        return ResponseEntity.ok(
                service.findByUserDocumentAndPage(userId, documentId, page)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}