package com.example.projeto_escola.domains_pratos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    private Integer cargaHoraria;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @ToString.Exclude
    private Categoria categoria;

    public Curso(Long id, String nome, Integer cargaHoraria, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.categoria = categoria;
    }
}
