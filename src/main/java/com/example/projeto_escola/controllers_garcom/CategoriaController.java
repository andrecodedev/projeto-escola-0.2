package com.example.projeto_escola.controllers_garcom;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.projeto_escola.dtos_comanda.CategoriaRequestDTO;
import com.example.projeto_escola.services_cozinha.categoria.CategoriaService;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {
    private CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaRequestDTO inserir(@RequestBody CategoriaRequestDTO categoria) {
        return categoriaService.inserir(categoria);
    }

    @PutMapping("{id}")
    public CategoriaRequestDTO editar(@PathVariable Long id,
            @RequestBody CategoriaRequestDTO categoria) {
        return categoriaService.editar(id, categoria);
    }

    @GetMapping()
    public List<CategoriaRequestDTO> obterTodos() {
        return categoriaService.obterTodos();
    }

    @GetMapping("{id}")
    public CategoriaRequestDTO obterPorId(@PathVariable Long id) {
        return categoriaService.obterPorId(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
    }
}
