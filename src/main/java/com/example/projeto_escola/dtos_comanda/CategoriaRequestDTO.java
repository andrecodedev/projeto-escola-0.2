package com.example.projeto_escola.dtos_comanda;

import lombok.Builder;

@Builder
public class CategoriaRequestDTO {
    private Long id;
    private String nome;

    public CategoriaRequestDTO() {
    }

    public CategoriaRequestDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
}
