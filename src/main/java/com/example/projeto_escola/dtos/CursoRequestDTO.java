package com.example.projeto_escola.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CursoRequestDTO {
    private Long id;
    private String nome;
    private Integer cargaHoraria;
    private Long categoriaId;
}
