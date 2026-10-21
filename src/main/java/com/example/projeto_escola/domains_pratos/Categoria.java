package com.example.projeto_escola.domains_pratos;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @OneToMany(mappedBy = "categoria"/* , fetch = FetchType.EAGER */)

    private List<Curso> cursos;

    public Categoria(Long id, String nome, List<Curso> cursos) {
        this.id = id;
        this.nome = nome;
        this.cursos = cursos;
    }
}
