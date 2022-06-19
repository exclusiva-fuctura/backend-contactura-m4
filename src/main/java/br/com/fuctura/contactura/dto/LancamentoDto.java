package br.com.fuctura.contactura.dto;

import br.com.fuctura.contactura.entities.Lancamento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@ApiModel(description = "Classe representatica do DTO do lancamento.")
public class LancamentoDto {

	@ApiModelProperty(value = "Id do lançamento")
	private Long id;

	@ApiModelProperty(value = "Indica se é uma receita")
	private Boolean ehReceita;
	
	@ApiModelProperty(value = "Tipo do Lançamento")
	private String tipo;
	
	@ApiModelProperty(value = "Descrição do Lancamento")	
	private String descricao;
	
	@ApiModelProperty(value = "Indica se é custo fixo")
	private Boolean ehFixo;
	
	@ApiModelProperty(value = "Data do lançamento no formato AAAA-MM-DD")
	private String data;
	
	@ApiModelProperty(value = "Valor do lançamento no formato 9999.99")	
	private Double valor;
	
	@ApiModelProperty(value = "Mensagem sobre o lançamento")
	@Setter
	private String mensagem;
		
	public LancamentoDto(Lancamento lancamento) {
		this.id = lancamento.getId();
		this.ehReceita = lancamento.isReceita();
		this.tipo = lancamento.getTipo();
		this.descricao = lancamento.getDescricao();
		this.ehFixo = lancamento.isFixo();
		this.data = lancamento.getData().toString();
		this.valor = lancamento.getValor();
	}
}
