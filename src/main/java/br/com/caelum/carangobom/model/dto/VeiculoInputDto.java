package br.com.caelum.carangobom.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.caelum.carangobom.model.entity.Marca;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeiculoInputDto {

	@NotBlank
	private String nome;
	
	@NotNull
	private Marca marca;
	
	@NotNull
	private int ano;
	
	@NotNull
	private double valor;
}
