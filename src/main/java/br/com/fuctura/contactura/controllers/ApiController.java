package br.com.fuctura.contactura.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import br.com.fuctura.contactura.config.SwaggerConfigurations;
import br.com.fuctura.contactura.dto.MensagemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {SwaggerConfigurations.API_TAG})
@RestController
@RequestMapping("/")
public class ApiController {

	@ApiOperation(value = "Checa disponibilidade do serviço API do CONTACTURA")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Api do Sistema Contactura está online"),	
	})
	@GetMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<MensagemDto> index() {
					
		MensagemDto dto = new MensagemDto(HttpStatus.OK.value(), "Api do Sistema Contactura está online");
		dto.setPath("/api");
		
		return ResponseEntity.status(HttpStatus.OK).body(dto);	
	}
	
	@ApiIgnore
	@GetMapping()
	public RedirectView redirectWagger() {							
		return new RedirectView("/swagger-ui.html");
	}
}
