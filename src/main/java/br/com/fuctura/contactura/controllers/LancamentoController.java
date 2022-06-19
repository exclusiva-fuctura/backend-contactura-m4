package br.com.fuctura.contactura.controllers;

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
import br.com.fuctura.contactura.dto.LancamentoDto;
import br.com.fuctura.contactura.entities.Lancamento;
import br.com.fuctura.contactura.entities.Usuario;
import br.com.fuctura.contactura.services.LancamentoService;
import br.com.fuctura.contactura.services.UsuarioService;
import br.com.fuctura.contactura.services.exceptions.LancamentoDateInvalidException;
import br.com.fuctura.contactura.services.exceptions.LancamentoFieldRequiredException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(tags = {SwaggerConfigurations.LANCAMENTO_TAG})
public class LancamentoController {

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private UsuarioService usuarioService;
	
	@ApiOperation(value = "Lista todos os lançamentos do usuário")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Requisição executado com sucesso"),
		@ApiResponse(code = 400, message = "Erro na requisição"),
	    @ApiResponse(code = 401, message = "Acesso negado"),
	})
	@GetMapping(value = "/lancamento", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<LancamentoDto>> index(@RequestHeader("Authorization") String authHeader) {
		
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
				
		// obter os lancamentos do usuario
		List<Lancamento> lancamentos = this.lancamentoService.findAllUsuario(usuario.get());
		// converter os lancamentos em lancamentosDTO
		List<LancamentoDto> lancamentosDto = lancamentos.stream().map(Lancamento::toDto)
				.collect(Collectors.toList());				
		
		return ResponseEntity.status(HttpStatus.OK)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(lancamentosDto);	
	}
	
	
	@ApiOperation(value = "Obter um lançamento do usuário")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Requisição executado com sucesso"),
		@ApiResponse(code = 400, message = "Erro na requisição"),
	    @ApiResponse(code = 401, message = "Acesso negado"),
	    @ApiResponse(code = 404, message = "Lançamento não encontrado"),
	})
	@GetMapping(value = "/lancamento/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<LancamentoDto> show(@PathVariable("id") Long id, @RequestHeader("Authorization") String authHeader) {
		
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}		
		
		LancamentoDto lancamento;
		Optional<Lancamento> lancamentoDB = this.lancamentoService.findById(id);
		if (lancamentoDB.isEmpty()) {
			lancamento = new LancamentoDto();
			lancamento.setMensagem("Lançamento não encontrado");			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(lancamento);
		}
		lancamento = lancamentoDB.get().toDto();
		return ResponseEntity.status(HttpStatus.OK)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(lancamento);	
	}
	
	
	@ApiOperation(value = "Cadastramento de um lançamento do usuário")
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "Lancamento criado com sucesso"),
	    @ApiResponse(code = 400, message = "Erro na requisição"),
	    @ApiResponse(code = 401, message = "Acesso negado"),
	})
	@PostMapping(value = "/lancamento", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<LancamentoDto> store(@RequestBody LancamentoDto lancamentoDto, 
			@RequestHeader("Authorization") String authHeader) {
		
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		try {
			this.lancamentoService.checkDto(lancamentoDto);
		} catch (LancamentoFieldRequiredException | LancamentoDateInvalidException e) {
			lancamentoDto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(lancamentoDto);	
		}
		
		Lancamento novoLancamento = new Lancamento(lancamentoDto);
		novoLancamento.setUsuario(usuario.get());
		Lancamento lancamento = this.lancamentoService.save(novoLancamento);
		
		return ResponseEntity.status(HttpStatus.OK)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(lancamento.toDto());
	}
	
	@ApiOperation(value = "Atualizar um lancamento de usuario")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Alteração de um lançamento com sucesso"),
	    @ApiResponse(code = 400, message = "Erro na requisição"),
	    @ApiResponse(code = 401, message = "Acesso negado"),
	    @ApiResponse(code = 404, message = "Lançamento não encontrado"),
	})
	@PutMapping(value = "/lancamento/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<LancamentoDto> update(@PathVariable("id") Long id, @RequestBody LancamentoDto lancamentoDto,
			@RequestHeader("Authorization") String authHeader) {
		
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		try {
			this.lancamentoService.checkDto(lancamentoDto);
		} catch (LancamentoFieldRequiredException | LancamentoDateInvalidException e) {
			lancamentoDto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(lancamentoDto);	
		}

		// verificar se o payload tem o mesmo id informado
		if (lancamentoDto.getId() != id) {
			lancamentoDto.setMensagem("Ids informados incompatíveis");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(lancamentoDto);	
		}		
		
		// verificar se o lancamento é do usuario
		Optional<Lancamento> lancamentoDB = usuario.get().getLancamentos().stream()
				.filter( l -> l.getId() == id).findFirst();
		if (lancamentoDB.isEmpty()) {
			lancamentoDto.setMensagem("Lançamento não encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(lancamentoDto);	
		}
		
		// verifica se o objeto é igual
		Lancamento lancamento = this.lancamentoService.updateFromDto(lancamentoDB.get(), lancamentoDto);
		if (lancamentoDB.get().equals(lancamento)) {
			lancamentoDto.setMensagem("Lançamento idêntico já existente");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(lancamentoDto);	
		}
			
		this.lancamentoService.save(lancamento);
				
		lancamentoDto = lancamentoDB.get().toDto();
		return ResponseEntity.status(HttpStatus.OK)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(lancamentoDto);	
	}
	
	@ApiOperation(value = "Excluir um lancamento de usuario")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "exclusão de um lançamento com sucesso"),
	    @ApiResponse(code = 400, message = "Erro na requisição"),
	    @ApiResponse(code = 401, message = "Acesso negado"),
	    @ApiResponse(code = 404, message = "Lançamento não encontrado"),
	})
	@DeleteMapping(value = "/lancamento/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<LancamentoDto> delete(@PathVariable("id") Long id, @RequestHeader("Authorization") String authHeader) {
		
		LancamentoDto lancamentoDto = new LancamentoDto();
		
		// verificar autorização
		Optional<Usuario> usuario = this.usuarioService.checkHeader(authHeader);
		if (usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		// verificar se o lancamento é do usuario
		Optional<Lancamento> lancamentoDB = usuario.get().getLancamentos().stream()
				.filter( l -> l.getId() == id).findFirst();
		if (lancamentoDB.isEmpty()) {
			lancamentoDto.setMensagem("Lançamento não encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(lancamentoDto);	
		}
		
		// excluir o lancamento
		this.lancamentoService.delete(lancamentoDB.get());
				
		lancamentoDto = lancamentoDB.get().toDto();
		lancamentoDto.setMensagem("Lancamento excluído");
		return ResponseEntity.status(HttpStatus.OK)
				.headers(this.usuarioService.getAuthHeader(usuario.get()))
				.body(lancamentoDto);	
	}
		
}
