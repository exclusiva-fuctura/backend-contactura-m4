package br.com.fuctura.contactura.dto;

import br.com.fuctura.contactura.entities.Usuario;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UsuarioDto {

	@ApiModelProperty(value = "Email do usuario")
	private String email;
	
	@ApiModelProperty(value = "Nome do usuario")
	private String nome;
	
	@ApiModelProperty(value = "Senha do usuario")
	private String senha;
	
	@ApiModelProperty(value = "Telefone do usuario")
	private Long telefone;
	
	@ApiModelProperty(value = "Mensagem quando existir")
	private String mensagem;
	
	public UsuarioDto(Usuario usuario) {
		this.email = usuario.getEmail();
		this.nome = usuario.getNome();
		this.telefone = usuario.getTelefone();
		this.senha = usuario.getSenha();
	}
	
	public UsuarioDto(LoginDto login) {
		this.email = login.getEmail();
	}
	
}
