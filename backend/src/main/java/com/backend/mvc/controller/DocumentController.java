package com.backend.mvc.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.mvc.model.Document;
import com.backend.mvc.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /*
     * Rota para realizar upload de PDF.
     *
     * O upload não é enviado como JSON.
     * Ele deve ser enviado como multipart/form-data,
     * porque estamos enviando um arquivo junto com outros dados.
     *
     * Exemplo no Bruno:
     *
     * POST http://localhost:8080/api/documents/upload
     *
     * Body → Multipart Form:
     * title = Apostila Java
     * file  = apostila.pdf
     */
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Document> uploadPdf(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        // Envia os dados recebidos para o service fazer a regra de negócio
        Document document = documentService.uploadPdf(title, file);

        // Retorna o documento cadastrado com status HTTP 200
        return ResponseEntity.ok(document);
    }

    /*
     * Rota para listar todos os documentos cadastrados.
     *
     * Exemplo:
     *
     * GET http://localhost:8080/api/documents
     */
    @GetMapping
    public ResponseEntity<List<Document>> findAll() {

        // Busca todos os documentos usando o service
        List<Document> documents = documentService.findAll();

        // Retorna a lista de documentos
        return ResponseEntity.ok(documents);
    }

    /*
     * Rota para buscar um documento específico pelo ID.
     *
     * Exemplo:
     *
     * GET http://localhost:8080/api/documents/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Document> findById(@PathVariable Long id) {

        // Busca o documento pelo ID usando o service
        Document document = documentService.findById(id);

        // Retorna o documento encontrado
        return ResponseEntity.ok(document);
    }

    /*
     * Rota para renderizar uma página do PDF como imagem PNG.
     *
     * Exemplo:
     *
     * GET http://localhost:8080/api/documents/1/page/1?dpi=144
     *
     * Onde:
     * id = ID do documento
     * pageNumber = número da página desejada
     * dpi = qualidade da imagem renderizada
     */
    @GetMapping(
            value = "/{id}/page/{pageNumber}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> renderPage(
            @PathVariable Long id,
            @PathVariable int pageNumber,
            @RequestParam(defaultValue = "144") float dpi
    ) throws Exception {

        // Chama o service para renderizar a página como imagem
        byte[] image = documentService.renderPage(id, pageNumber, dpi);

        // Retorna os bytes da imagem com Content-Type image/png
        return ResponseEntity.ok(image);
    }

    /*
     * Rota para excluir um documento.
     *
     * Essa exclusão remove:
     * 1. O arquivo PDF salvo na pasta local.
     * 2. O registro do documento no banco.
     *
     * Exemplo:
     *
     * DELETE http://localhost:8080/api/documents/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        // Chama o service para excluir o documento
        documentService.delete(id);

        // Retorna HTTP 204 No Content
        return ResponseEntity.noContent().build();
    }
}