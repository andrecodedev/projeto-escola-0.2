package com.example.projeto_escola.dtos_comanda;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class CursoRequestDTO {
    private Long id;
    private String nome;
    private Integer cargaHoraria;
    private Long categoriaId;

    public CursoRequestDTO(Long id, String nome, Integer cargaHoraria, Long categoriaId) {
        this.id = id;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.categoriaId = categoriaId;
    }
}
