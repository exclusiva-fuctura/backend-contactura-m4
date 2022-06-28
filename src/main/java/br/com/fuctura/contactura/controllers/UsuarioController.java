package br.com.fuctura.contactura.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fuctura.contactura.config.SwaggerConfigurations;
import br.com.fuctura.contactura.dto.UsuarioDto;
import br.com.fuctura.contactura.entities.Usuario;
import br.com.fuctura.contactura.services.UsuarioService;
import br.com.fuctura.contactura.services.exceptions.UsuarioFieldRequiredException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {SwaggerConfigurations.USUARIO_TAG})
@RestController
@RequestMapping("/api")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@ApiOperation(value = "Lista usuarios")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Requisição executada com sucesso"),	
	    @ApiResponse(code = 401, message = "Acesso negado"),
	})
	@GetMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<UsuarioDto>> index(@RequestHeader("Authorization") String authHeader) {
					
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		List<Usuario> usuarios = this.usuarioService.findAll();
		List<UsuarioDto> usuariosDto = new ArrayList<>();
		if (usuarios.isEmpty()) {			
			return ResponseEntity.status(HttpStatus.OK).body(usuariosDto);	
		}
		
		usuariosDto = usuarios.stream().map(Usuario::toDto).collect(Collectors.toList()); 
				
		return ResponseEntity.status(HttpStatus.OK)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(usuariosDto);	
	}
	
	
	@ApiOperation(value = "Obter usuario")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Requisição executada com sucesso"),
	    @ApiResponse(code = 401, message = "Acesso negado"),	    
	    @ApiResponse(code = 404, message = "Usuário não encontrado"),
	    
	})
	@GetMapping(value = "/usuario/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<UsuarioDto> show(@PathVariable("id") String id, 
			@RequestHeader("Authorization") String authHeader) {
					
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		Optional<Usuario> usuarioDB = this.usuarioService.findById(id);		
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);	
		}
				
		return ResponseEntity.status(HttpStatus.OK)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(usuarioDB.get().toDto());	
	}
	
	@ApiOperation(value = "Alterar usuario")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Requisição executada com sucesso"),
	    @ApiResponse(code = 400, message = "Ocorreu erro na requisição"),
	    @ApiResponse(code = 401, message = "Acesso negado"),	    
	    @ApiResponse(code = 404, message = "Usuário não encontrado"),	    
	})
	@PutMapping(value = "/usuario/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<UsuarioDto> update(@PathVariable("id") String id, 
			@RequestBody UsuarioDto usuarioDto, 
			@RequestHeader("Authorization") String authHeader) {
					
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		try {
			// para nao cair na checagem da senha nao informada
			if (null == usuarioDto.getSenha()) usuarioDto.setSenha("");
			
			this.usuarioService.checkDto(usuarioDto);		

		} catch (UsuarioFieldRequiredException e) {
			usuarioDto.setMensagem(e.getMessage());
			usuarioDto.setSenha(null);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(usuarioDto);	
		}		
		
		Optional<Usuario> usuarioDB = this.usuarioService.findById(id);		
		if (usuarioDB.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);	
		}

		// efetuar as laterações necessárias
		Usuario usuarioUpdated = this.usuarioService.updateFromDto(usuario.get(), usuarioDto);
		
		// incluir a nova senha se informado
		if (!usuarioDto.getSenha().isEmpty()) {
			usuarioUpdated.setSenha(usuarioDto.getSenha());
		}
				
		this.usuarioService.update(usuarioUpdated);
		
		return ResponseEntity.status(HttpStatus.OK)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(usuarioUpdated.toDto());	
	}
	

	@ApiOperation(value = "Criar usuario")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Requisição executada com sucesso"),
	    @ApiResponse(code = 400, message = "Ocorreu erro na requisição"),
	    @ApiResponse(code = 401, message = "Acesso negado"),	
	    @ApiResponse(code = 404, message = "Usuário não encontrado"),
	    
	})
	@PostMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<UsuarioDto> store(@RequestBody UsuarioDto usuarioDto, 
			@RequestHeader("Authorization") String authHeader) {
					
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		try {
			this.usuarioService.checkDto(usuarioDto);
		} catch (UsuarioFieldRequiredException e) {
			usuarioDto.setMensagem(e.getMessage());
			usuarioDto.setSenha(null);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(usuarioDto);	
		}		
		
		Optional<Usuario> usuarioDB = this.usuarioService.findByEmail(usuarioDto.getEmail());		
		if (usuarioDB.isPresent()) {
			usuarioDto.setMensagem("Email já cadastrado");
			usuarioDto.setSenha(null);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(usuarioDto);	
		}

		// criar o usuario
		Usuario saved = this.usuarioService.saveDto(usuarioDto);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(saved.toDto());	
	}
	
	@ApiOperation(value = "Excluir usuario")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Requisição executada com sucesso"),
	    @ApiResponse(code = 401, message = "Acesso negado"),	    
	    @ApiResponse(code = 404, message = "Usuário não encontrado"),
	    
	})
	@DeleteMapping(value = "/usuario/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<UsuarioDto> delete(@PathVariable("id") String id, 
			@RequestHeader("Authorization") String authHeader) {
					
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
			
		Optional<Usuario> usuarioDB = this.usuarioService.findById(id);		
		if (usuarioDB.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);	
		}

		UsuarioDto dto = usuarioDB.get().toDto();
		
		this.usuarioService.delete(usuarioDB.get());
		
		return ResponseEntity.status(HttpStatus.OK)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(dto);	
	}
}
