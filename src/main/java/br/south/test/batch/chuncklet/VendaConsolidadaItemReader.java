package br.south.test.batch.chuncklet;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import br.south.test.dto.VendaConsolidadaDto;

public class VendaConsolidadaItemReader implements ItemReader<VendaConsolidadaDto>, StepExecutionListener {

	private Iterator<VendaConsolidadaDto> vendasConsolidada;
    private ExitStatus exitStatus;
    private boolean hasError;
	
	
	@Override
	public void beforeStep(StepExecution stepExecution) {    
		System.out.println("ChunkletItemReader");
		
		exitStatus =  stepExecution.getJobExecution().getExitStatus();
		if (!exitStatus.equals(ExitStatus.FAILED)) {
			List<VendaConsolidadaDto> listVendas = (List<VendaConsolidadaDto>) stepExecution.getJobExecution()
					.getExecutionContext().get("listvendasConsolidadaDto");
			this.vendasConsolidada = listVendas.iterator();
		}
	}

	

	@Override
	public VendaConsolidadaDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(this.vendasConsolidada != null && this.vendasConsolidada.hasNext()) {
			return this.vendasConsolidada.next();
		}
		return null;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		return ExitStatus.COMPLETED;
	}

}
