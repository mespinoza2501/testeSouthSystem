package br.south.test.batch.chuncklet;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import br.south.test.entity.VendaConsolidada;
import br.south.test.service.VendaConsolidadaService;

public class VendaConsolidadaItemWriter implements ItemWriter<VendaConsolidada>, StepExecutionListener{

	
	private VendaConsolidadaService  vendaConsolidadaService;
	
	private String pathFileOut;
	
	public VendaConsolidadaItemWriter(String pathFileOut) {
		this.pathFileOut =  pathFileOut;
	}
	
	
	
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.vendaConsolidadaService =  new VendaConsolidadaService();
		
	}
	
	
	@Override
	public void write(List<? extends VendaConsolidada> items) throws Exception {
		 this.vendaConsolidadaService.writerVendaConsolidadaFileDone(items, this.pathFileOut);
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		return ExitStatus.COMPLETED;
	}

	

}
