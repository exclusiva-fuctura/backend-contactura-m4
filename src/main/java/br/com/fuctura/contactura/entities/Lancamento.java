package br.com.fuctura.contactura.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.fuctura.contactura.dto.LancamentoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="lancamentos")
public class Lancamento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@Column()
	private boolean isReceita;
	
	@Column()
	private String tipo;
	
	@Column()
	private String descricao;
	
	@Column()
	private LocalDate data;
	
	@Column()
	private double valor;
	
	@Column()
	private boolean isFixo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	public Lancamento(LancamentoDto dto) {
		this.isReceita = dto.getEhReceita();
		this.isFixo = dto.getEhFixo();
		this.tipo = dto.getTipo();
		this.descricao = dto.getDescricao();
		this.valor = dto.getValor();	
		this.data = LocalDate.parse(dto.getData());
	}
	
	public LancamentoDto toDto() {
		return new LancamentoDto(this);
	}
	
	public boolean equals(Lancamento lancamento) {
		if (this.isReceita != lancamento.isReceita) return false;
		
		if (this.isFixo != lancamento.isFixo) return false;
		
		if (this.valor != lancamento.getValor()) return false;
			
		if (!this.data.equals(lancamento.getData())) return false;
		
		if (!this.descricao.equals(lancamento.getDescricao())) return false;
		
		if (!this.tipo.equals(lancamento.getTipo())) return false;

		return true;
	}
}
