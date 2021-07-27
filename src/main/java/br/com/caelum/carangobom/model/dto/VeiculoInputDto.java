package br.com.caelum.carangobom.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoInputDto {

	@NotBlank
	private String nome;
	
	@NotNull
	private long marcaId;
	
	@NotNull
	private int ano;
	
	@NotNull
	private double valor;
}
