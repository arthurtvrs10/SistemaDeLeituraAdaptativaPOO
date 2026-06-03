package com.backend.mvc.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.mvc.model.Document;
import com.backend.mvc.repository.DocumentRepository;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final PdfService pdfService;

    // Pasta onde os PDFs enviados serão salvos no servidor
    private final String uploadDir = "uploads/pdfs";

    public DocumentService(DocumentRepository documentRepository, PdfService pdfService) {
        this.documentRepository = documentRepository;
        this.pdfService = pdfService;
    }

    /*
     * Faz o upload de um PDF.
     *
     * Responsabilidades deste método:
     * 1. Validar se o arquivo foi enviado.
     * 2. Validar se o arquivo é um PDF.
     * 3. Criar a pasta de upload, caso ela não exista.
     * 4. Salvar uma cópia do PDF no servidor.
     * 5. Contar a quantidade de páginas usando o PdfService.
     * 6. Criar o objeto Document com os metadados.
     * 7. Salvar os metadados no banco usando o repository.
     */
    public Document uploadPdf(String title, MultipartFile file) throws Exception {

        // Verifica se o arquivo foi enviado
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo PDF é obrigatório.");
        }

        // Recupera o nome original do arquivo enviado
        String originalFileName = file.getOriginalFilename();

        // Verifica se o nome do arquivo é válido
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("O nome do arquivo é inválido.");
        }

        // Verifica se o arquivo possui extensão .pdf
        if (!originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Apenas arquivos PDF são aceitos.");
        }

        // Cria a pasta uploads/pdfs caso ela ainda não exista
        Files.createDirectories(Path.of(uploadDir));

        // Gera um nome único para evitar sobrescrever arquivos com o mesmo nome
        String savedFileName = System.currentTimeMillis() + "_" + originalFileName;

        // Monta o caminho final onde o PDF será salvo
        Path filePath = Path.of(uploadDir, savedFileName);

        // Salva fisicamente o arquivo PDF no servidor
        file.transferTo(filePath.toFile());

        // Usa o PdfService para descobrir a quantidade total de páginas do PDF
        int totalPages = pdfService.getTotalPages(filePath.toFile());

        // Cria a entidade Document com os metadados do PDF
        Document document = new Document(
                title,
                originalFileName,
                filePath.toString(),
                totalPages
        );

        // Salva os metadados no banco de dados e retorna o documento salvo
        return documentRepository.save(document);
    }

    /*
     * Lista todos os documentos cadastrados no banco.
     */
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    /*
     * Busca um documento pelo ID.
     *
     * Se o documento não existir, lança uma exceção.
     */
    public Document findById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado."));
    }

    /*
     * Renderiza uma página específica de um documento.
     *
     * Este método:
     * 1. Busca o documento no banco.
     * 2. Recupera o caminho físico do PDF.
     * 3. Verifica se o arquivo existe no servidor.
     * 4. Chama o PdfService para transformar a página em imagem PNG.
     */
    public byte[] renderPage(Long documentId, int pageNumber, float dpi) throws Exception {

        // Busca os metadados do documento no banco
        Document document = findById(documentId);

        // Recupera o arquivo físico a partir do caminho salvo no banco
        File file = new File(document.getFilePath());

        // Verifica se o PDF ainda existe na pasta local
        if (!file.exists()) {
            throw new RuntimeException("Arquivo físico do documento não encontrado.");
        }

        // Renderiza a página solicitada usando o PdfService
        return pdfService.renderPageAsImage(file, pageNumber, dpi);
    }

    /*
     * Exclui um documento do sistema.
     *
     * Este método remove:
     * 1. O arquivo PDF salvo fisicamente na pasta uploads/pdfs.
     * 2. O registro do documento no banco de dados.
     */
    public void delete(Long id) {

        // Busca o documento antes de excluir
        Document document = findById(id);

        // Recupera o arquivo físico associado ao documento
        File file = new File(document.getFilePath());

        // Se o arquivo existir, tenta removê-lo do disco
        if (file.exists()) {
            boolean deleted = file.delete();

            if (!deleted) {
                throw new RuntimeException("Não foi possível excluir o arquivo físico.");
            }
        }

        // Remove os metadados do documento do banco de dados
        documentRepository.delete(document);
    }
}