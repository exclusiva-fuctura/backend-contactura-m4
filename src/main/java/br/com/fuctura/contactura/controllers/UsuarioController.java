package br.com.fuctura.contactura.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fuctura.contactura.dto.UsuarioDto;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api")
public class UsuarioController {
	
	@ApiIgnore
	@PostMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<UsuarioDto> store(@RequestBody UsuarioDto dto, @RequestHeader("Authorization") String authHeader) {
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
	}

}
