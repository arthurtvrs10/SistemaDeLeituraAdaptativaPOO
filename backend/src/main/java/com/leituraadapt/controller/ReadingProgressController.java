package com.leituraadapt.controller;

import com.leituraadapt.model.ReadingProgressEntity;
import com.leituraadapt.service.ReadingProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ReadingProgressController {

    private final ReadingProgressService service;

    public ReadingProgressController(ReadingProgressService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReadingProgressEntity> save(@RequestBody ReadingProgressEntity progress) {
        if (progress.getId() == null || progress.getId().isBlank()) {
            progress.setId("progress-" + progress.getUserId() + "-" + progress.getDocumentId());
        }

        ReadingProgressEntity saved = service.save(progress);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadingProgressEntity> findById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}