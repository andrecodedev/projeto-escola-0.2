package com.example.projeto_escola.controllers_garcom;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.projeto_escola.dtos_comanda.CursoRequestDTO;
import com.example.projeto_escola.services_cozinha.curso.CursoService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/curso")
public class CursoController {
    private CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CursoRequestDTO inserir(@RequestBody CursoRequestDTO curso) {
        return cursoService.inserir(curso);
    }

    @PutMapping("{id}")
    public CursoRequestDTO editar(@PathVariable Long id,
            @RequestBody CursoRequestDTO curso) {
        return cursoService.editar(id, curso);
    }

    @GetMapping()
    public List<CursoRequestDTO> obterTodos() {
        return cursoService.obterTodos();
    }

    @GetMapping("{id}")
    public CursoRequestDTO obterPorId(@PathVariable Long id) {
        return cursoService.obterPorId(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        cursoService.excluir(id);
    }

}
