package com.example.projeto_escola.repositories_estoque;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projeto_escola.domains_pratos.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

}
