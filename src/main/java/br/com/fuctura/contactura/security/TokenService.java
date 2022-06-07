package br.com.fuctura.contactura.security;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import br.com.fuctura.contactura.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${fuctura.jwt.secret}")
	private String secret;
	
	@Value("${fuctura.jwt.expiration}")
	private String expiration;
	
	public String gerarToken(String userId) {
		
		Date hoje = new Date();
		Date dataExpiracao = this.convertDate(expiration);
		
		return Jwts.builder()
				.setIssuer("API do Contactura da Fuctura")
				.setSubject(userId)
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, this.secret)
				.compact();
	}
	
	private Date convertDate(String value) {
		String period = value.substring(value.length()-1);
		String time = value.substring(0, value.indexOf(period));
		LocalDateTime date;
		
		switch (period) {
			case "d": date = LocalDateTime.now().plusDays(Long.parseLong(time));
			break;
			case "m": date = LocalDateTime.now().plusMonths(Long.parseLong(time));
			break;
			case "y": date = LocalDateTime.now().plusYears(Long.parseLong(time));
			break;
			default : date = LocalDateTime.now().plusMinutes(Long.parseLong(time));	
		}
		
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}

	public boolean validaToken(String token) {
		
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}	
	}

	public String getIdUsuaurio(String token) {
		Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return body.getSubject();
	}
	
	public String tokenHeaderVerify(String authHeader) throws TokenDeniedException {
		// verify if exists the key Digest
		if (!authHeader.startsWith("Bearer")) {
			throw new TokenDeniedException("Token não aceito");
		}
		
		return authHeader.substring(7);
	}
	
	/**
	 * gerar o header com o token no Authorization
	 * @param usuario
	 * @return response headers
	 */
	public HttpHeaders getAuthHeader(Usuario usuario) {
		HttpHeaders responseHeaders = new HttpHeaders();
	    
		String token = this.gerarToken(usuario.getId());
		responseHeaders.set("Authorization", token);
		
		return responseHeaders;
	}
}
