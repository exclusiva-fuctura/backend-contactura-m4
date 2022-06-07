package br.com.fuctura.contactura.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fuctura.contactura.entities.Usuario;

@Transactional(readOnly = true)
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{

	Optional<Usuario> findByNome(String nome);

	Optional<Usuario> findByEmail(String email);
	
	
}
