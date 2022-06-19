package br.com.fuctura.contactura.services.exceptions;

public class UsuarioNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UsuarioNotFoundException(String msg) {
		super(msg);
	}
}
