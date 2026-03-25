package com.leituraadapt.config;

import com.leituraadapt.dto.RegisterRequestDTO;
import com.leituraadapt.model.DocumentEntity;
import com.leituraadapt.model.ReadingProgressEntity;
import com.leituraadapt.service.DocumentService;
import com.leituraadapt.service.ReadingProgressService;
import com.leituraadapt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDocuments(DocumentService service,
                                    ReadingProgressService readingProgressService,
                                    UserService userService) {
        return args -> {
            service.save(new DocumentEntity(
                    "doc1",
                    "WCAG em um minuto",
                    "WCAG são diretrizes para tornar conteúdo web mais acessível. " +
                            "Elas ajudam no desenvolvimento de aplicações inclusivas. " +
                            "Os princípios são perceptível, operável, compreensível e robusto. " +
                            "A acessibilidade melhora a experiência para diferentes perfis de usuários. " +
                            "Isso inclui baixa visão, dislexia, daltonismo e deficiência motora."
            ));

            service.save(new DocumentEntity(
                    "doc2",
                    "Dicas de Leitura",
                    "Use foco, espaçamento e leitura em blocos."
            ));

            service.save(new DocumentEntity(
                    "doc3",
                    "Texto longo (demo)",
                    "Este é um texto maior para testar leitura e paginação depois. " +
                            "Ele ajuda a validar wrap e progresso. " +
                            "Linha extra 1. Linha extra 2. Linha extra 3. " +
                            "Mais conteúdo para gerar novas páginas no sistema. " +
                            "Mais conteúdo para gerar novas páginas no sistema. " +
                            "Mais conteúdo para gerar novas páginas no sistema. " +
                            "Mais conteúdo para gerar novas páginas no sistema. " +
                            "Mais conteúdo para gerar novas páginas no sistema. " +
                            "Mais conteúdo para gerar novas páginas no sistema. " +
                            "Mais conteúdo para gerar novas páginas no sistema."
            ));

            RegisterRequestDTO registerDTO = new RegisterRequestDTO();
            registerDTO.setId("user1");
            registerDTO.setName("Arthur");
            registerDTO.setEmail("arthur@email.com");
            registerDTO.setPassword("123456");

            userService.register(registerDTO);

            ReadingProgressEntity progress = new ReadingProgressEntity();
            progress.setId("user1-doc3");
            progress.setUserId("user1");
            progress.setDocumentId("doc3");
            progress.setCurrentPage(1);
            progress.setPageSize(5);

            readingProgressService.save(progress);
        };
    }
}