package br.com.fuctura.contactura.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="usuarios")
public class UserSystem {

	@Id
	@Column(name="email")
	private String email;

	@Column(name="senha")
	private String password;
	
	@Column(name="nome")
	private String name;
}
