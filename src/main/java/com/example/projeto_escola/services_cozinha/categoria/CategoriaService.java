package com.example.projeto_escola.services_cozinha.categoria;

import java.util.List;

import com.example.projeto_escola.dtos_comanda.CategoriaRequestDTO;

public interface CategoriaService {
    CategoriaRequestDTO inserir(CategoriaRequestDTO categoriaRequestDTO);

    CategoriaRequestDTO editar(Long id, CategoriaRequestDTO categoriaRequestDTO);

    List<CategoriaRequestDTO> obterTodos();

    CategoriaRequestDTO obterPorId(Long id);

    void excluir(Long id);
}

//camada de serviço precisa ter camada de dependencia 