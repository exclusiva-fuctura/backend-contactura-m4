package br.com.fuctura.contactura.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.contactura.entities.Usuario;
import br.com.fuctura.contactura.repositories.UsuarioRepository;

@Service
public class AutenticadorService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usarioService;
	
	public Usuario loadUserByEmailSenha(String email, String senha) throws UsuarioNotFoundException {
		// obter o usuario
		Optional<Usuario> usuario = this.usuarioRepository.findByEmail(email);		
		if (usuario.isPresent()) {
			// verificar se a senha é compatível
			if (this.usarioService.isValidPassword(senha, usuario.get())) {
				return usuario.get();			
			}
		}
		
		throw new UsuarioNotFoundException("Dados do usuário inválido ou não cadastrado!");
	}

	
}
