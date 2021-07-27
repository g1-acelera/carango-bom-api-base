package br.com.caelum.carangobom.exception;

public class MarcaNotFoundException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	public MarcaNotFoundException(String message) {
        super(message);
    }
}
