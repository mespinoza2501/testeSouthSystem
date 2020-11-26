package br.south.test.batch.chuncklet;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.south.test.dto.VendaConsolidadaDto;
import br.south.test.entity.VendaConsolidada;
import br.south.test.service.VendaConsolidadaService;


public class VendaConsolidadaItemProcessor implements ItemProcessor<VendaConsolidadaDto, VendaConsolidada>, StepExecutionListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(VendaConsolidadaItemProcessor.class);
	
	
	
	private VendaConsolidadaService vendaConsolidadaService;
	
	
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.vendaConsolidadaService =  new VendaConsolidadaService() ;
		
	}
	
	
	@Override
	public VendaConsolidada process(VendaConsolidadaDto item) throws Exception {		
		
		System.out.println("Chunkletprocessor");
		System.out.println(item);
		return vendaConsolidadaService.consolidaVenda(item);
	}
	
	

	

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		 return ExitStatus.COMPLETED;
	}

}
