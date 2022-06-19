package br.com.fuctura.contactura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fuctura.contactura.entities.Lancamento;
import br.com.fuctura.contactura.entities.Usuario;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

	List<Lancamento> findByUsuario(Usuario usuario);
}
