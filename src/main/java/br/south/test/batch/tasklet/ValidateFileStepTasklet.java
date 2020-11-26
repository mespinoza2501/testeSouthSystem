package br.south.test.batch.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ValidateFileStepTasklet implements Tasklet, StepExecutionListener{

	
	private String diretorioError = "data/error";
	
	public ValidateFileStepTasklet(String diretorioError) {
		this.diretorioError =  diretorioError;
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		
		
	}
	

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
	
		return null;
	}
	
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
	
		return null;
	}

}
