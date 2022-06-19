package br.com.fuctura.contactura.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fuctura.contactura.config.SwaggerConfigurations;
import br.com.fuctura.contactura.dto.LoginDto;
import br.com.fuctura.contactura.entities.Usuario;
import br.com.fuctura.contactura.security.TokenService;
import br.com.fuctura.contactura.services.UsuarioService;
import br.com.fuctura.contactura.services.exceptions.LoginInvalidException;
import br.com.fuctura.contactura.services.exceptions.UsuarioNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {SwaggerConfigurations.AUTENTICADOR_TAG})
@RestController
@RequestMapping("/api")
public class AutenticadorController {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@ApiOperation(value = "Autentica o usuario para acessar o sistema CONTACTURA")
	@ApiResponses(value = {
	    @ApiResponse(code = 201, message = "Usuario Autenticado"),
	    @ApiResponse(code = 400, message = "Erro na requisição"),
	    @ApiResponse(code = 401, message = "Acesso negado"),
	    @ApiResponse(code = 404, message = "Usuario não encontrado"), 
	})
	@PostMapping(value = "/autenticador", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<LoginDto> store(@RequestBody LoginDto login) {
		
		try {
			// verificar se o dto esta valido
			this.isValidDto(login);
			
			// autenticar usuario
			Usuario usuario = this.usuarioService
					.loadUserByEmailSenha(login.getEmail(), login.getSenha());
			
			
			login.setSenha(null);
			login.setMensagem("Acesso autorizado!");
			return ResponseEntity.status(HttpStatus.CREATED)
					.headers(this.tokenService.getAuthHeader(usuario))
					.body(login);
			
		} catch (LoginInvalidException ex) {			
			login.setMensagem(ex.getMessage());
			login.setSenha(null);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(login);
			
		} catch (UsuarioNotFoundException ex) {
			login.setMensagem(ex.getMessage());
			login.setSenha(null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(login);
		}		
	}
	
	/**
	 * verifica se o dto está integro
	 * @param dto
	 * @throws LoginInvalidException 
	 */
	private void isValidDto(LoginDto dto) throws LoginInvalidException {
		
		if (null == dto.getEmail()) {
			throw new LoginInvalidException("Email precisa ser informado") ;
		}
		
		if (null == dto.getSenha()) {
			throw new LoginInvalidException("Senha precisa ser informada");
		}		
	}
	
}
