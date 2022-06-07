package br.com.fuctura.contactura.security;

public class TokenNoExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public TokenNoExistsException(String msg) {
		super(msg);
	}

}
