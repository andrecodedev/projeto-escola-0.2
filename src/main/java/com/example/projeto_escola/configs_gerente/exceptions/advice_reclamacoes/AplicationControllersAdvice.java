package com.example.projeto_escola.configs_gerente.exceptions.advice_reclamacoes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.projeto_escola.configs_gerente.exceptions.RegraNegocioException;
import com.example.projeto_escola.dtos_comanda.ApiErrosDTO;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AplicationControllersAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrosDTO handleRegraDeNegocioException(RegraNegocioException ex) {
        String msg = ex.getMessage();
        return new ApiErrosDTO(msg);
    }
}
