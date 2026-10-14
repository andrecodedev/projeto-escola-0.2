package com.example.projeto_escola.services;

import java.util.List;

import com.example.projeto_escola.dtos.CursoRequestDTO;

public interface CursoService {
    CursoRequestDTO inserir(CursoRequestDTO cursoRequestDTO);

    CursoRequestDTO editar(Long id, CursoRequestDTO cursoRequestDTO);

    List<CursoRequestDTO> obterTodos();

    CursoRequestDTO obterPorId(Long id);

    void excluir(Long id);
}


// injeção de dependencia o servises faz isso no controllers