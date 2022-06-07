package br.com.fuctura.contactura.services;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fuctura.contactura.dto.UsuarioDto;
import br.com.fuctura.contactura.entities.Usuario;
import br.com.fuctura.contactura.repositories.UsuarioRepository;
import br.com.fuctura.contactura.security.SecurePasswordService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private SecurePasswordService securePasswordService;
	
	/**
	 * Persiste o objeto DTO do usuario alterando a senha
	 * @param userDto
	 * @return retorna o objeto usuario persistido
	 */
	@Transactional(rollbackFor = Exception.class) 
	public Usuario saveDto(UsuarioDto userDto) { 
	    userDto.setSenha(this.securePasswordService
	           .encode(userDto.getSenha())); 
	    return save(new Usuario(userDto)); 
	}
	
	/**
	 * Persiste no banco o usuario
	 * @param usuario
	 * @return objeto Usuario persistido
	 */
	public Usuario save(Usuario usuario) {
		return this.repository.save(usuario);
	}
	
	/**
	 * Procura Usuario pelo ID
	 * @param id
	 * @return objeto Optional do usuario
	 */
	public Optional<Usuario> findById(String id) {
		return this.repository.findById(id);
	}
	
	/**
	 * Procura Usuario pelo email
	 * @param email
	 * @return objeto Optional do usuario
	 */
	public Optional<Usuario> findByEmail(String email) {
		return this.repository.findByEmail(email);
	}
	
	/**
	 * Verifica se a senha é váida com o usuario do banco
	 * @param senha
	 * @param usuario
	 * @return true / false
	 * @throws NoSuchAlgorithmException 
	 */
	public boolean isValidPassword(String senha, Usuario usuario) {
		String pass = this.securePasswordService.encode(senha);
		log.debug("==>", this.securePasswordService.encode(senha));
		if (null != usuario && usuario.getSenha().equals(this.securePasswordService.encode(senha))) {
			return true;
		}
		return false;
	}
}
