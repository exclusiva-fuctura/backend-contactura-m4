package br.com.fuctura.contactura.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fuctura.contactura.config.SwaggerConfigurations;
import br.com.fuctura.contactura.dto.MensagemDto;
import br.com.fuctura.contactura.dto.UsuarioDto;
import br.com.fuctura.contactura.entities.Usuario;
import br.com.fuctura.contactura.services.UsuarioService;
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
	})
	@GetMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<UsuarioDto>> index() {
					
		List<Usuario> usuarios = this.usuarioService.findAll();
		List<UsuarioDto> usuariosDto = new ArrayList<>();
		if (usuarios.isEmpty()) {			
			return ResponseEntity.status(HttpStatus.OK).body(usuariosDto);	
		}
		
		usuariosDto = usuarios.stream().map(Usuario::toDto).collect(Collectors.toList()); 
				
		return ResponseEntity.status(HttpStatus.OK).body(usuariosDto);	
	}

}
