package br.com.fuctura.contactura.services;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fuctura.contactura.dto.UsuarioDto;
import br.com.fuctura.contactura.entities.Usuario;
import br.com.fuctura.contactura.repositories.UsuarioRepository;
import br.com.fuctura.contactura.security.SecurePasswordService;
import br.com.fuctura.contactura.security.TokenDeniedException;
import br.com.fuctura.contactura.security.TokenNoExistsException;
import br.com.fuctura.contactura.services.exceptions.UsuarioFieldRequiredException;
import br.com.fuctura.contactura.services.exceptions.UsuarioNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private AutenticadorService autenticadorService;
	
	@Autowired
	private SecurePasswordService securePasswordService;
	
	private static final String TITLE = "[USUARIO SERVICE] ";
	
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
	
	public Usuario update(Usuario usuario) {
		String senha = this.securePasswordService
		           .encode(usuario.getSenha());
		usuario.setSenha(senha);
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
	 * Lista todos os usuarios cadastrados
	 * @return lista de usuarios
	 */
	public List<Usuario> findAll() {
		return this.repository.findAll();
	}
	
	/**
	 * Remover usuario da base
	 * @param usuario
	 */
	public void delete(Usuario usuario) {
		this.repository.delete(usuario);
	}
	
	/**
	 * Verifica se a senha é váida com o usuario do banco
	 * @param senha
	 * @param usuario
	 * @return true / false
	 * @throws NoSuchAlgorithmException 
	 */
	public boolean isValidPassword(String senha, Usuario usuario) {
		String password = this.securePasswordService.encode(senha);
		if (null != usuario && usuario.getSenha().equals(password)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica o cabeçalho enviado a busca da autorizacao
	 * @param authorizationHeader
	 * @return Retorna um usuario
	 */
	public Optional<Usuario> checkHeader(String authorizationHeader) {
		
		// verificar se tem autorização
		try {
			// id do usuario
			Optional<String> id = this.autenticadorService.hasAuthorization(authorizationHeader);
			// usuario no banco
			Optional<Usuario> usuarioDB = this.findById(id.get());
			// caso o usuario nao exista na base
			if (usuarioDB.isPresent()) {
				return Optional.of(usuarioDB.get());
			}				
			
		} catch (TokenDeniedException | TokenNoExistsException | UsuarioNotFoundException e) {
			log.warn(TITLE + e.getMessage());			
			return Optional.empty();	
		}
		
		return Optional.empty();
	}
	
	/**
	 * Gerar o Header com o token JWT
	 * @param usuario
	 * @return Cabeçalho com o Authorization e o JWT inserido
	 */
	public HttpHeaders getAuthHeader(Usuario usuario) {
		return this.autenticadorService.getAuthHeader(usuario);		
	}
	
	/**
	 * Carregar o usuario via e-mail e senha 
	 * @param email
	 * @param senha
	 * @return Usuario instaciado
	 * @throws UsuarioNotFoundException
	 */
	public Usuario loadUserByEmailSenha(String email, String senha) throws UsuarioNotFoundException {
		// obter o usuario
		Optional<Usuario> usuario = this.findByEmail(email);		
		if (usuario.isPresent()) {
			// verificar se a senha é compatível
			if (this.isValidPassword(senha, usuario.get())) {
				return usuario.get();			
			}
		}
		
		throw new UsuarioNotFoundException("Dados do usuário inválido ou não cadastrado!");
	}
	
	/**
	 * Verifica os campos obrigatórios
	 * @param usuarioDto
	 * @throws UsuarioFieldRequiredException
	 */
	public void checkDto(UsuarioDto usuarioDto) throws UsuarioFieldRequiredException {
		
		if (null == usuarioDto.getEmail()) {
			throw new UsuarioFieldRequiredException("Email");
		}
		
		if (null == usuarioDto.getNome()) {
			throw new UsuarioFieldRequiredException("Nome");
		}
		
		if (null == usuarioDto.getSenha()) {
			throw new UsuarioFieldRequiredException("Senha");
		}
	}

	/**
	 * Realiza as atualizações necessárias
	 * @param usuario
	 * @param usuarioDto
	 * @return Instância do Usuario alterado
	 */
	public Usuario updateFromDto(Usuario usuario, UsuarioDto usuarioDto) {
		
		if (!usuario.getEmail().equals(usuarioDto.getEmail())) usuario.setEmail(usuarioDto.getEmail());
		
		if (!usuario.getNome().equals(usuarioDto.getNome())) usuario.setNome(usuarioDto.getNome());
		
		if (usuario.getTelefone() != usuarioDto.getTelefone()) usuario.setTelefone(usuarioDto.getTelefone());
		
		return usuario;
	}
	
}
