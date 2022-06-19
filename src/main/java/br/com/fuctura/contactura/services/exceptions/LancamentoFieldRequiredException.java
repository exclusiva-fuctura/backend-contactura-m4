package br.com.fuctura.contactura.services.exceptions;

public class LancamentoFieldRequiredException extends Exception {
	
	private static final long serialVersionUID = -3927940123007676370L;

	public LancamentoFieldRequiredException(String field) {
		super("Campo '"+ field + "' não informado.");
	}
}
