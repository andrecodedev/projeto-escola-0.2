package com.example.projeto_escola.services_cozinha.curso;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.projeto_escola.configs_gerente.exceptions.RegraNegocioException;
import com.example.projeto_escola.domains_pratos.Categoria;
import com.example.projeto_escola.domains_pratos.Curso;
import com.example.projeto_escola.dtos_comanda.CursoRequestDTO;
import com.example.projeto_escola.repositories_estoque.CategoriaRepository;
import com.example.projeto_escola.repositories_estoque.CursoRepository;

@Service
public class CursoServiceImpl implements CursoService {
    private CursoRepository cursoRepository;
    private CategoriaRepository categoriaRepository;

    public CursoServiceImpl(CursoRepository cursoRepository,
            CategoriaRepository categoriaRepository) {
        this.cursoRepository = cursoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CursoRequestDTO inserir(CursoRequestDTO dto) {
        Categoria categ = categoriaRepository
                .findById(dto.getCategoriaId())
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrada"));
        Curso c = Curso.builder()
                .nome(dto.getNome())
                .cargaHoraria(dto.getCargaHoraria())
                .categoria(categ)
                .build();
        c = cursoRepository.save(c);
        return new CursoRequestDTO(c.getId(), c.getNome(),
                c.getCargaHoraria(), c.getCategoria().getId());
    }

    @Override
    public List<CursoRequestDTO> obterTodos() {
        return cursoRepository.findAll().stream()
                .map(c -> new CursoRequestDTO(c.getId(), c.getNome(),
                        c.getCargaHoraria(), c.getCategoria().getId()))
                .toList();
    }

    @Override
    public CursoRequestDTO obterPorId(Long id) {
        Curso c = cursoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Curso não encontrado"));

        return new CursoRequestDTO(c.getId(), c.getNome(),
                c.getCargaHoraria(), c.getCategoria().getId());
    }

    @Override
    public void excluir(Long id) {
        Curso c = cursoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Curso não encontrado"));
        cursoRepository.delete(c);
    }

    @Override
    public CursoRequestDTO editar(Long id, CursoRequestDTO dto) {
        Curso c = cursoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Curso não encontrado"));
        Categoria categ = categoriaRepository
                .findById(dto.getCategoriaId())
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrada"));
        c.setNome(dto.getNome());
        c.setCargaHoraria(dto.getCargaHoraria());
        c.setCategoria(categ);
        c = cursoRepository.save(c);
        return new CursoRequestDTO(c.getId(), c.getNome(),
                c.getCargaHoraria(), c.getCategoria().getId());
    }
}
