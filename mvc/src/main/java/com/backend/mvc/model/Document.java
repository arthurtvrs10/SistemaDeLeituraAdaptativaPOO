package com.backend.mvc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.util.UUID;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // é salvo como um VARCHAR(36) OU BINARY(16)
                    // "123e4567-e89b-12d3-a456-426614174000"

    private String title; // representa o nome original do arquivo PDF

    private String filePath; // caminho onde o arquivo foi salvo no servidor

    private int totalPages; // total de paginas do PDF

    private DateTimeAtCreation uploadedAt; // deve guardar data e hora do upload
}
