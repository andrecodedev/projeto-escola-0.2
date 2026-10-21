package com.example.projeto_escola.services_cozinha.curso;

import java.util.List;

import com.example.projeto_escola.dtos_comanda.CursoRequestDTO;

public interface CursoService {
    CursoRequestDTO inserir(CursoRequestDTO cursoRequestDTO);

    CursoRequestDTO editar(Long id, CursoRequestDTO cursoRequestDTO);

    List<CursoRequestDTO> obterTodos();

    CursoRequestDTO obterPorId(Long id);

    void excluir(Long id);
}


// injeção de dependencia o servises faz isso no controllers