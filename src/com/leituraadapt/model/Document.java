package com.leituraadapt.domain;

import java.util.Objects;

public class Document {
  // 3 atributos privados
  // use "final" para deixar imutável.
  private final String id;
  private final String title;
  private final String content;

  //Criar o construtor
  // trim() retorna uma nova string removendo espaços em branco nas pontas (início e fim).
  // os parametros do construtor são: int id, String title, String content
  // Objects.requireNonNull é quase como if (id == null) throw new NullPointerException("id não pode ser nulo"); return id;
  public Document(String id, String title, String content){
    this.id = Objects.requireNonNull(id, "id não pode ser nulo").trim();
    this.title = Objects.requireNonNull(title, "tittle não pode ser nulo").trim();
    this.content = Objects.requireNonNull(content, "content não pode ser nulo");

    // isEmpty() é um método de String que retorna true se o comprimento for 0.
    //O que é IllegalArgumentException É uma exceção usada quando um método recebe um argumento inválido (mesmo que não seja null).
    if(this.id.isEmpty()) throw new IllegalArgumentException("id não pode ser vazio");
  }
  public String getId(){
    return this.id;
  }
  public String getTitle(){
    return this.title;
  }
  public String getcontent(){
    return this.content;
  }

}
