package br.south.test.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class VendasDto implements Comparable<VendasDto>{
	
	
	private Long idVenda;
	private List<ItemVendaDto> itens;
	private String nmVendedor;
	private BigDecimal vlTotalVenda;
	
	
	@Override
	public int compareTo(VendasDto o) {
		
		return this.vlTotalVenda.compareTo(o.getVlTotalVenda());
				
	}

}
