package br.com.fuctura.contactura.services;

public class LoginInvalidException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public LoginInvalidException(String msg) {
		super(msg);
	}

}
