package br.south.test.batch.tasklet;


import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import br.south.test.dto.VendaConsolidadaDto;
import br.south.test.service.VendaConsolidadaService;
import br.south.test.validate.ValidateFile;




public class LoadFileTasklet implements Tasklet, StepExecutionListener {


	private List<VendaConsolidadaDto>listVendaConsolidadaDto;
	private String diretorioEntrada;
	
	
	
	private VendaConsolidadaService vendaConsolidadaService;
	
	private ValidateFile validateFile;
	
	
	
	public LoadFileTasklet(String diretorioEntrada) {
		this.diretorioEntrada = diretorioEntrada;
		
	}
	
	

	
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
	
	
		this.listVendaConsolidadaDto =  new ArrayList<VendaConsolidadaDto>();
		this.vendaConsolidadaService = new VendaConsolidadaService() ;
		this.validateFile =  new ValidateFile(diretorioEntrada);
		this.validateFile.Validade();
	
		
	}
	
	

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		this.listVendaConsolidadaDto = this.vendaConsolidadaService.carregaArquivo(this.diretorioEntrada);
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution
		.getJobExecution()		
		.getExecutionContext()
		.put("listvendasConsolidadaDto", this.listVendaConsolidadaDto);
		
		if(this.listVendaConsolidadaDto == null) {			
			stepExecution.getJobExecution().setExitStatus(ExitStatus.FAILED);			
		}
		else {
			stepExecution.getJobExecution().setExitStatus(ExitStatus.EXECUTING);
		}
		
		return ExitStatus.COMPLETED;
	}


}
