package br.com.caelum.carangobom.util.validacao;

public class ErroDeParametroOutputDto {

    private String parametro;
    private String mensagem;
    

    public ErroDeParametroOutputDto(String parametro, String mensagem) {
		super();
		this.parametro = parametro;
		this.mensagem = mensagem;
	}

	public String getParametro() {
        return parametro;
    }

    public String getMensagem() {
        return mensagem;
    }

}
