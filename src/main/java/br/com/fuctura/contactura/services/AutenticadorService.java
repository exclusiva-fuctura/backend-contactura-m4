package br.com.fuctura.contactura.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import br.com.fuctura.contactura.entities.Usuario;
import br.com.fuctura.contactura.security.TokenDeniedException;
import br.com.fuctura.contactura.security.TokenNoExistsException;
import br.com.fuctura.contactura.security.TokenService;

@Service
public class AutenticadorService {
	

	@Autowired
	private TokenService tokenService;
	

	/**
	 * Verifica se tem autorização
	 * @param authHeader
	 * @return Optional<String> id do usuário extraido do token
	 * @throws TokenDeniedException 
	 * @throws TokenNoExistsException 
	 */
	public Optional<String> hasAuthorization(String authHeader) throws TokenDeniedException, TokenNoExistsException {
		
		// verifica se tem Token
		if (null == authHeader) {
			throw new TokenNoExistsException("Token não informado"); 
		}
		
		// verificar o cabeçalho
		String token = this.tokenService.tokenHeaderVerify(authHeader);
		
		// verificar se o token é válido
		if (!this.tokenService.validaToken(token)) {						
			throw new TokenDeniedException("Token inválido");	
		}

		// obter o id do usuario
		String usuarioID = this.tokenService.getIdUsuaurio(token);
	
		return Optional.of(usuarioID);
	}
	
	public HttpHeaders getAuthHeader(Usuario usuario) {
		return this.tokenService.getAuthHeader(usuario);
	}
	
}
