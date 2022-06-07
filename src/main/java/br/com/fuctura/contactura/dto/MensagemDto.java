package br.com.fuctura.contactura.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MensagemDto {
	@ApiModelProperty(value = "Status http code")
	private int status;
	
	@ApiModelProperty(value = "Mensagem informativa")
	private String message;
	
	@ApiModelProperty(value = "Date e Hora da mensagem")
	private String timestamp;
	
	@ApiModelProperty(value = "Endpoint")
	private String path;
	
	public MensagemDto(int status, String msg) {
		this.status = status;
		this.message = msg;
		this.timestamp = LocalDateTime.now().toString();
	}
}
