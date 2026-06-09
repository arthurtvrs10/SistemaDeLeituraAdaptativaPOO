package com.backend.mvc.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    // Pasta onde as páginas renderizadas em segundo plano serão salvas
    private final String cacheDir = "uploads/cache";

    public DocumentService(DocumentRepository documentRepository, PdfService pdfService) {
        this.documentRepository = documentRepository;
        this.pdfService = pdfService;
    }

    /*
     * Faz o upload de um PDF.
     *
     * Responsabilidades:
     * 1. Validar se o arquivo foi enviado.
     * 2. Validar se o arquivo é PDF.
     * 3. Criar a pasta de upload.
     * 4. Salvar uma cópia do PDF no servidor.
     * 5. Contar as páginas usando PdfService.
     * 6. Criar o objeto Document.
     * 7. Salvar os metadados no banco.
     */
    public Document uploadPdf(String title, MultipartFile file) throws Exception {

        // Verifica se o arquivo foi enviado
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo PDF é obrigatório.");
        }

        // Recupera o nome original do arquivo
        String originalFileName = file.getOriginalFilename();

        // Verifica se o nome do arquivo é válido
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("O nome do arquivo é inválido.");
        }

        // Verifica se o arquivo possui extensão .pdf
        if (!originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Apenas arquivos PDF são aceitos.");
        }

        // Transforma o caminho da pasta em caminho absoluto
        Path uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();

        // Cria a pasta caso ela não exista
        Files.createDirectories(uploadPath);

        // Gera um nome único para evitar sobrescrever arquivos
        String savedFileName = System.currentTimeMillis() + "_" + originalFileName;

        // Monta o caminho completo do arquivo
        Path filePath = uploadPath.resolve(savedFileName);

        // Salva o PDF fisicamente no servidor
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Conta a quantidade de páginas do PDF
        int totalPages = pdfService.getTotalPages(filePath.toFile());

        // Cria o documento com os metadados
        Document document = new Document(
                title,
                originalFileName,
                filePath.toString(),
                totalPages
        );

        // Salva os metadados no banco
        return documentRepository.save(document);
    }

    /*
     * Lista todos os documentos cadastrados.
     */
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    /*
     * Busca um documento pelo ID.
     */
    public Document findById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado."));
    }

    /*
     * Renderiza uma página específica do PDF.
     *
     * Agora esse método também usa cache e thread.
     *
     * Fluxo:
     * 1. Busca o documento.
     * 2. Verifica se o arquivo físico existe.
     * 3. Verifica se a página atual já está no cache.
     * 4. Se estiver, retorna a imagem do cache.
     * 5. Se não estiver, renderiza usando PdfService.
     * 6. Salva a página atual no cache.
     * 7. Inicia uma Thread para preparar as próximas páginas.
     */
    public byte[] renderPage(Long documentId, int pageNumber, float dpi) throws Exception {

        // Busca os metadados do documento no banco
        Document document = findById(documentId);

        // Recupera o arquivo físico a partir do caminho salvo no banco
        File file = new File(document.getFilePath());

        // Verifica se o arquivo PDF ainda existe no servidor
        if (!file.exists()) {
            throw new RuntimeException("Arquivo físico do documento não encontrado.");
        }

        // Monta o caminho onde a página renderizada ficaria salva no cache
        Path currentPageCachePath = getCachePath(documentId, pageNumber, dpi);

        // Se a página já estiver no cache, retorna direto do cache
        // Isso melhora o desempenho porque evita renderizar o PDF novamente
        if (Files.exists(currentPageCachePath)) {
            return Files.readAllBytes(currentPageCachePath);
        }

        // Se a página não estiver no cache, renderiza usando o PdfService
        byte[] image = pdfService.renderPageAsImage(file, pageNumber, dpi);

        // Cria a pasta de cache se ela ainda não existir
        Files.createDirectories(currentPageCachePath.getParent());

        // Salva a página atual no cache para próximas requisições
        Files.write(currentPageCachePath, image);

        // Inicia uma Thread para preparar as próximas páginas em segundo plano
        startPreRenderThread(document, pageNumber, dpi);

        // Retorna a página atual imediatamente para o cliente
        return image;
    }

    /*
     * Cria uma Thread para pré-renderizar as próximas páginas.
     *
     * Essa Thread melhora o desempenho da leitura porque prepara páginas
     * que provavelmente serão acessadas logo em seguida.
     */
    private void startPreRenderThread(Document document, int currentPage, float dpi) {

        // Cria uma nova Thread
        Thread preRenderThread = new Thread(() -> {

            try {
                // Recupera o arquivo PDF físico
                File file = new File(document.getFilePath());

                // Define a primeira próxima página
                int nextPage = currentPage + 1;

                // Define até qual página será pré-renderizada
                // Aqui vamos preparar as próximas 2 páginas
                int lastPageToRender = currentPage + 2;

                // Percorre as próximas páginas
                for (int page = nextPage; page <= lastPageToRender; page++) {

                    // Se a página passar do total de páginas, para o loop
                    if (page > document.getTotalPages()) {
                        break;
                    }

                    // Monta o caminho do cache da página
                    Path cachePath = getCachePath(document.getId(), page, dpi);

                    // Se a página já estiver no cache, não renderiza de novo
                    if (Files.exists(cachePath)) {
                        continue;
                    }

                    // Cria a pasta de cache se necessário
                    Files.createDirectories(cachePath.getParent());

                    // Renderiza a página em segundo plano
                    byte[] image = pdfService.renderPageAsImage(file, page, dpi);

                    // Salva a imagem renderizada no cache
                    Files.write(cachePath, image);

                    // Mostra no terminal que a página foi preparada
                    System.out.println("Página " + page + " pré-renderizada em segundo plano.");
                }

            } catch (Exception e) {
                // Se der erro na Thread, não quebra a resposta principal da API
                // Apenas registra o erro no terminal
                System.out.println("Erro ao pré-renderizar páginas em segundo plano: " + e.getMessage());
            }
        });

        // Define um nome para facilitar identificar a Thread no terminal/debug
        preRenderThread.setName("pdf-pre-render-thread");

        // Inicia a Thread
        // A API continua respondendo sem esperar essa tarefa terminar
        preRenderThread.start();
    }

    /*
     * Monta o caminho onde uma página renderizada será salva no cache.
     *
     * Exemplo de caminho:
     * uploads/cache/document-1/page-2-dpi-144.png
     */
    private Path getCachePath(Long documentId, int pageNumber, float dpi) {

        // Converte o DPI para inteiro para usar no nome do arquivo
        int dpiValue = Math.round(dpi);

        // Monta o caminho da pasta do documento dentro do cache
        return Path.of(cacheDir)
                .toAbsolutePath()
                .normalize()
                .resolve("document-" + documentId)
                .resolve("page-" + pageNumber + "-dpi-" + dpiValue + ".png");
    }

    /*
     * Exclui um documento do sistema.
     *
     * Além de excluir o PDF físico e os metadados do banco,
     * também remove o cache das páginas renderizadas.
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

        // Remove o cache das páginas renderizadas desse documento
        deleteCacheFolder(id);

        // Remove os metadados do documento do banco
        documentRepository.delete(document);
    }

    /*
     * Remove a pasta de cache de um documento.
     */
    private void deleteCacheFolder(Long documentId) {

        try {
            // Monta o caminho da pasta de cache do documento
            Path documentCachePath = Path.of(cacheDir)
                    .toAbsolutePath()
                    .normalize()
                    .resolve("document-" + documentId);

            // Se a pasta de cache não existir, não precisa fazer nada
            if (!Files.exists(documentCachePath)) {
                return;
            }

            // Percorre os arquivos da pasta e exclui todos
            Files.walk(documentCachePath)
                    .sorted((path1, path2) -> path2.compareTo(path1))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (Exception e) {
                            System.out.println("Erro ao excluir cache: " + e.getMessage());
                        }
                    });

        } catch (Exception e) {
            // Se der erro ao excluir o cache, apenas registra no terminal
            // A exclusão do documento ainda pode continuar
            System.out.println("Erro ao limpar cache do documento: " + e.getMessage());
        }
    }
}