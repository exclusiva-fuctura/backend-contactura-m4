package br.com.fuctura.contactura.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.fuctura.contactura.dto.UsuarioDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="usuarios")
public class Usuario implements Serializable{

	private static final long serialVersionUID = 7952922447803275215L;

	@Id
	@Column(name="usua_id")
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column(name="usua_email", nullable = false)
	private String email;

	@Column(name="usua_senha", nullable = false)
	private String senha;
	
	@Column(name="usua_nome")
	private String nome;
	
	@Column(name="usua_telefone")
	private Long telefone;
	
	public Usuario(UsuarioDto userDto) {
		this.email = userDto.getEmail();
		this.nome = userDto.getNome();
		this.telefone = userDto.getTelefone();
		this.senha = userDto.getSenha();
	}
	
}
