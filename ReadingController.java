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

@RestController // responsável por receber requisições HTTP e retornar respostas JSON
@RequestMapping("/api/reading") // mapeia as requisições para /api/reading, ou seja, todas as rotas dentro deste controller começam com /api/reading
public class ReadingController {

    // injeção de dependências, Aqui temos os serviços que o controller utiliza
    private final DocumentService documentService; // para acessar os documentos
    private final ReadingProgressService readingProgressService; // para acessar e atualizar o progresso de leitura do usuário
    private final AccessibilityProfileService accessibilityProfileService; // para acessar o perfil de acessibilidade do usuário
    private final TextWrapper textWrapper; // para quebrar o texto em linhas de acordo com a largura da coluna

    // construtor que injeta as dependências, seguindo o padrão de arquitetura de camadas, mantendo o controller apenas como orquestrador
    public ReadingController(
            DocumentService documentService,
            ReadingProgressService readingProgressService,
            AccessibilityProfileService accessibilityProfileService
    ) {
        this.documentService = documentService; // para acessar os documentos
        this.readingProgressService = readingProgressService;
        this.accessibilityProfileService = accessibilityProfileService;
        this.textWrapper = new TextWrapper();
    }

    @GetMapping("/{id}") // Partindo aqui pro endpoint principal, 
    public ReadingResponseDTO read( //Aqui temos o endpoint principal de leitura.
            @PathVariable String id, // ele recebe o identificador do documento na URL e paramentros opcionais como:
            @RequestParam(required = false) Integer page, // número da página a ser lida, opcional
            @RequestParam(required = false) Integer fontSize, // tamanho da fonte, opcional
            @RequestParam(required = false) Double lineHeight, // espaçamento entre linhas, opcional
            @RequestParam(required = false) Integer columnWidth, // largura da coluna, opcional
            @RequestParam(required = false, defaultValue = "false") boolean dyslexiaMode // modo de leitura para dislexia, opcional, padrão é false
    ) {
        DocumentEntity doc = documentService.findById(id) /// busca o documento pelo ID, se não encontrar lança uma exceção
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // Aqui o sistema identifica o usuário logado atráves do contexto de segurança, 
        String email = auth.getName();                                                // usando uM token JWT

        AccessibilityProfileEntity profile = accessibilityProfileService.findByUserId(email) // O sistema busca o perfil de acessibilidade do usuário,
                .orElseGet(() -> {                                                           // se não encontrar, cria um perfil padrão com configurações básicas
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
        
                //operador ternario para resolver os valores efetivos a serem usados na leitura, considerando tanto os parâmetros da requisição quanto as configurações do perfil de acessibilidade do usuário
        int resolvedFontSize = fontSize != null ? fontSize : profile.getFontSize(); // Ela resolve qual valor será usado para resolvedFontSize:
                                                                                    //Se fontSize não for null → usa o valor de fontSize
                                                                                    //Se fontSize for null → usa profile.getFontSize()
        double resolvedLineHeight = lineHeight != null ? lineHeight : profile.getLineHeight(); // mesma lógica para espaçamento entre linhas
        int resolvedColumnWidth = columnWidth != null ? columnWidth : profile.getColumnWidth();
        boolean resolvedDyslexiaMode = dyslexiaMode || profile.isDyslexiaFriendlyFont(); // o usuário ativou manualmente (dyslexiaMode) ou pega do perfil de acessibilidade já tem essa configuração (profile)

        List<String> lines = textWrapper.wrap(doc.getContent(), resolvedColumnWidth); // Aqui o conteúdo do doc é quebrado em linhas usando o TextWrapper 
                                                                                        // de acordo com a largura da coluna resolvida e armazenado em um lista de strings
        int linesPerPage = calculateLinesPerPage( // O sistema calcula a quantas linhas
                resolvedFontSize,                   // cabem por página, usando o metodo calculateLinesPerPage,
                resolvedLineHeight,               // considerando o tamanho da fonte, espaçamento entre linhas e se o modo de dislexia está ativado
                resolvedDyslexiaMode
        );

        int resolvedPage;
        if (page != null) { //A página pode vir da requisição, se o usuário especificou, 
        } else { // ou pode ser recuperada do progresso de leitura salvo para aquele documento e usuário
            resolvedPage = readingProgressService.findByUserIdAndDocumentId(email, id)
                    .map(progress -> progress.getCurrentPage())
                    .orElse(1);
        }

        int totalLines = lines.size(); // Aqui ele calcula o total de linhas do documento e garante que o valor seja válido
        int totalPages = (int) Math.ceil((double) totalLines / linesPerPage);
        if (totalPages == 0) {
            totalPages = 1;
        }

        if (resolvedPage < 1) { // “O sistema garante que a página não seja menor que 1 
            resolvedPage = 1;
        }
        if (resolvedPage > totalPages) { // nem maior que o total de páginas.”
            resolvedPage = totalPages;
        }

        int start = (resolvedPage - 1) * linesPerPage;
        int end = Math.min(totalLines, start + linesPerPage);

        List<String> pageContent = lines.subList(start, end); // Aqui ele extrai apenas o conteúdo da página  atual, evitando carregar o texto completo

        readingProgressService.saveOrUpdate(email, id, resolvedPage, linesPerPage); // O progresso de leitura é salbvo automaticamente, permitindo que o usuário continue depois.

        boolean lastPage = resolvedPage >= totalPages;  // O sistema determina se a página atual é maior que o TotalPages para sinalizar se é a última página, o que pode ser útil para a interface do usuário desabilitar o botão de avançar ou mostrar uma mensagem de conclusão
        boolean hasNext = resolvedPage < totalPages; // Aqui o verifica se reolvePage é menor que Totalpage para sinalizar se há uma próxima página
        boolean hasPrevious = resolvedPage > 1;  // E aqui ele verifica se resolvedPage é maior que 1 para sinalizar se há uma página anterior, o que pode ser usado para habilitar ou desabilitar o botão de voltar na interface do usuário

        return new ReadingResponseDTO( // Ele retorna todos os dados necessários para o front-end
                doc.getTitle(),
                pageContent, // o conteúdo da página atual
                resolvedPage, // página atual
                linesPerPage, // quantas linhas cabem por página
                totalPages,
                totalLines,
                lastPage,
                hasNext,
                hasPrevious
        );
    }

    private int calculateLinesPerPage(int fontSize, double lineHeight, boolean dyslexiaMode) { //esse metodo calcula quantas linhas cabem por página com base no tamanho da fonte e espaçamento
        int pageHeightPx = 1100;
        int verticalPaddingPx = 96; // 48 top + 48 bottom
        int usableHeightPx = pageHeightPx - verticalPaddingPx;

        double effectiveLineHeightPx = fontSize * lineHeight;

        if (dyslexiaMode) {
            effectiveLineHeightPx += fontSize * 0.35; //No modo dislexia, ele aumenta o espaçamento para melhorar a leitura
        }

        int linesPerPage = (int) Math.floor(usableHeightPx / effectiveLineHeightPx);
        return Math.max(linesPerPage, 1);
    }
}