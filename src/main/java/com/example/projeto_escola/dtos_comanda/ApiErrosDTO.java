package com.example.projeto_escola.dtos_comanda;

import java.util.List;

public class ApiErrosDTO {
    private List<String> erros;

    public ApiErrosDTO(String erros) {
        this.erros = List.of(erros);
    }

    public List<String> getErros() {
        return erros;
    }

}
