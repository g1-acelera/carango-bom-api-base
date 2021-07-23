package br.com.caelum.carangobom.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.model.entity.Veiculo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeiculoOutputDto {
    private Long id;
    private int ano;
    private Marca marca;
    private String nome;
    private double valor;

    public VeiculoOutputDto(Veiculo veiculo) {
        this.id = veiculo.getId();
        this.ano = veiculo.getAno();
        this.nome = veiculo.getNome();
        this.valor = veiculo.getValor();
        this.marca = veiculo.getMarca();
    }

    public static List<VeiculoOutputDto> convertToDto(List<Veiculo> veiculos) {
        return veiculos.stream().map(VeiculoOutputDto::new).collect(Collectors.toList());
    }
}
