package com.example.projeto_escola.services_cozinha.categoria;

import org.springframework.stereotype.Service;

import com.example.projeto_escola.domains_pratos.Categoria;
import com.example.projeto_escola.dtos_comanda.CategoriaRequestDTO;

import com.example.projeto_escola.repositories_estoque.CategoriaRepository;
import com.example.projeto_escola.configs_gerente.exceptions.RegraNegocioException;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    private CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CategoriaRequestDTO inserir(CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoria = new Categoria(null, categoriaRequestDTO.getNome(), null);
        categoriaRepository.save(categoria);
        return new CategoriaRequestDTO(categoria.getId(), categoria.getNome());
    }

    @Override
    public CategoriaRequestDTO editar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrado"));
        categoria.setNome(dto.getNome());
        categoriaRepository.save(categoria);
        return new CategoriaRequestDTO(categoria.getId(), categoria.getNome());
    }

    @Override
    public List<CategoriaRequestDTO> obterTodos() {
        return categoriaRepository.findAll().stream()
                .map(c -> new CategoriaRequestDTO(c.getId(), c.getNome()))
                .toList();
    }

    @Override
    public CategoriaRequestDTO obterPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrado"));
        return new CategoriaRequestDTO(categoria.getId(), categoria.getNome());
    }

    @Override
    public void excluir(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrado"));
        categoriaRepository.delete(categoria);
    }
}

//implementar categoria controller
//implementar categoria service
//implementar categoria service impl
//crud: inserir, editar, excluir, obter todos, obter por id

//controller e servições falta criar
//deixar o ambiente pronto para rodar pra o professor - 2 testes no postman inserir e editar