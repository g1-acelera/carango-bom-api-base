package br.com.caelum.carangobom.model.dto;

import br.com.caelum.carangobom.model.entity.Marca;

import java.util.List;
import java.util.stream.Collectors;

public class MarcaOutputDto {
    private Long id;
    private String nome;

    public MarcaOutputDto(Marca marca) {
        this.id = marca.getId();
        this.nome = marca.getNome();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static List<MarcaOutputDto> convertToDto(List<Marca> marcas) {
        return marcas.stream().map(MarcaOutputDto::new).collect(Collectors.toList());
    }
}
