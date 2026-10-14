package com.example.projeto_escola.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.projeto_escola.domains.Categoria;

public interface CategoriaRepository
        extends JpaRepository<Categoria, Long> {

    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.cursos")
    public List<Categoria> obterCategoriasECursos();

}
