package br.com.fuctura.contactura.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.contactura.dto.LancamentoDto;
import br.com.fuctura.contactura.entities.Lancamento;
import br.com.fuctura.contactura.entities.Usuario;
import br.com.fuctura.contactura.repositories.LancamentoRepository;
import br.com.fuctura.contactura.services.exceptions.LancamentoDateInvalidException;
import br.com.fuctura.contactura.services.exceptions.LancamentoFieldRequiredException;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository repository;
	
	public List<Lancamento> findAllUsuario(Usuario usuario) {
		return this.repository.findByUsuario(usuario);
	}
	
	public Optional<Lancamento> findById(Long id) {
		return this.repository.findById(id);
	}
	
	public Lancamento save(Lancamento lancamento) {
		return this.repository.save(lancamento);		
	}
	
	public void checkDto(LancamentoDto dto) throws LancamentoFieldRequiredException, LancamentoDateInvalidException {
		
		if (null == dto.getEhReceita()) {
			throw new LancamentoFieldRequiredException("ehReceita");
		}		
		
		if (null == dto.getDescricao()) {
			throw new LancamentoFieldRequiredException("descricao");
		}		
		
		if (null == dto.getEhFixo()) {
			throw new LancamentoFieldRequiredException("ehFixo");
		}		
		
		if (null == dto.getTipo()) {
			throw new LancamentoFieldRequiredException("tipo");
		}		
		
		if (null == dto.getValor()) {
			throw new LancamentoFieldRequiredException("valor");
		}
		
		if (null == dto.getData()) {
			throw new LancamentoFieldRequiredException("data");
		}		
		
		// verificar se a data é válida
		try {
			LocalDate.parse(dto.getData());
		} catch (Exception e) {
			throw new LancamentoDateInvalidException("Data inválida");
		}
		
	}
	
	public Lancamento updateFromDto(Lancamento lancamento, LancamentoDto dto) {
		
		if (!lancamento.getTipo().equals(dto.getTipo())) lancamento.setTipo(dto.getTipo()); 
		
		if (!lancamento.getDescricao().equals(dto.getDescricao())) lancamento.setDescricao(dto.getDescricao());
		
		if (lancamento.getValor() != dto.getValor()) lancamento.setValor(dto.getValor());
		
		if (lancamento.isFixo() != dto.getEhFixo()) lancamento.setFixo(dto.getEhFixo());
		
		if (lancamento.isReceita() != dto.getEhReceita()) lancamento.setReceita(dto.getEhReceita());
		
		if (!lancamento.getData().equals(LocalDate.parse(dto.getData()))) lancamento.setData(LocalDate.parse(dto.getData()));
		
		return lancamento;
	}
	
	public void delete(Lancamento lancamento) {
		this.repository.delete(lancamento);
	}
}
