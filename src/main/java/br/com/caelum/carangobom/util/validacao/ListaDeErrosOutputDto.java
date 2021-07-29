package br.com.caelum.carangobom.util.validacao;

import java.util.List;

public class ListaDeErrosOutputDto {

    private List<ErroDeParametroOutputDto> erros;

    public ListaDeErrosOutputDto(List<ErroDeParametroOutputDto> erros) {
		super();
		this.erros = erros;
	}

	public int getQuantidadeDeErros() {
        return erros.size();
    }
}
