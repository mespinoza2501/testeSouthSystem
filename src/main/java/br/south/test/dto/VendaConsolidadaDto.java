package br.south.test.dto;


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
public class VendaConsolidadaDto {
	
	private List<ClienteDto> clientesDto;
	private List<VendasDto> vendasDto;
	private List<VendedorDto> vendedoresDto;


}
