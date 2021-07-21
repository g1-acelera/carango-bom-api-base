package br.com.caelum.carangobom.model.dto;

import com.sun.istack.NotNull;

import javax.validation.Valid;

public class MarcaInputDto {

    @NotNull
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
