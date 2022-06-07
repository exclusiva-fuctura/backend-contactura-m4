package br.com.fuctura.contactura.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
	
	@ApiModelProperty(value = "Email de acesso do usuario")
	private String email;
	
	@ApiModelProperty(value = "Senha do usuario")
	private String senha;
	
	@ApiModelProperty(value = "Mensagem do sistema quando existir")	
	private String mensagem;
	
}
