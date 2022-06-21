package br.com.fuctura.contactura.dto;

import br.com.fuctura.contactura.entities.Usuario;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "Classe representatica do DTO do usuario.")
public class UsuarioDto {

	@ApiModelProperty(value = "Id do usuario")
	private String id;

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
		this.id = usuario.getId();
		this.email = usuario.getEmail();
		this.nome = usuario.getNome();
		this.telefone = usuario.getTelefone();		
	}
	
	public UsuarioDto(LoginDto login) {
		this.email = login.getEmail();
	}
	
	public Usuario toUsuario() {
		return new Usuario(this);
	}
	
}
