package com.leituraadapt.repository;

import com.leituraadapt.model.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DocumentRepository {
  private final List<Document> documents = new ArrayList<>();

  // document = atributo, pertence ao objetoo Library
  // d = parãmetro, existe só dentro do método
  public void save(Document document){
    //valida o parãmetro
    Objects.requireNonNull(document, "Document não pode ser nul0o");

    // Evitar ids duplicados (boa prática)
    // stream() = "Crie um fluxo de elementos da lista documents."
    // anyMatch() = verificar condição
    // -> = operador lambda. x -> x * 2, ou seja, "Pegue x e retorne x * 2."
    boolean exists = documents.stream().anyMatch(doc -> doc.getId().equals(document.getId()));
    if(exists){
      throw new IllegalArgumentException("Já existe um documento com esse ID" + document.getId());
    }
    documents.add(document);
  }
  public List<Document> findAll() {
    return Collections.unmodifiableList(documents);
  }

  public Optional<Document> findById(String id){
    if(id == null) return Optional.empty();
    String trimmed = id.trim();
    return documents.stream()
            .filter(d -> d.getId().equals(trimmed))
            .findFirst();
  }
}
