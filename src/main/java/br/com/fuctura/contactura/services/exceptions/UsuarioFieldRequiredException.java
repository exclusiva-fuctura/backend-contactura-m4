package br.com.fuctura.contactura.services.exceptions;

public class UsuarioFieldRequiredException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UsuarioFieldRequiredException(String field) {
		super("Campo '"+ field + "' não informado.");
	}

}
