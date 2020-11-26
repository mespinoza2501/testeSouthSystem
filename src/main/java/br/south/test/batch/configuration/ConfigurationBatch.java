package br.south.test.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.south.test.batch.chuncklet.VendaConsolidadaItemProcessor;
import br.south.test.batch.chuncklet.VendaConsolidadaItemReader;
import br.south.test.batch.chuncklet.VendaConsolidadaItemWriter;
import br.south.test.batch.tasklet.LoadFileTasklet;
import br.south.test.dto.VendaConsolidadaDto;
import br.south.test.entity.VendaConsolidada;

@Configuration
@EnableBatchProcessing
public class ConfigurationBatch  {
	
	
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired 
	private StepBuilderFactory stepBuilderFactory;	
	
	@Value("${pathFiles.In}")
	private String pahtFileIn;
	
	@Value("${pathFiles.Out}")
	private String pahtFileOut;
	
	@Bean
	public Job job() {
		
		
		
	
		return jobBuilderFactory
				.get("analiseVendasJob")
				.start(loadFileTeskletStep())				
				.next(consolidaVendaChuckletStep(vendasConsolidadaItemReader(),
												vendasConsolidadaItemProcessor(),
												vendasConsolidadaItemWriter()))
				
				.build();
				
				
				
				
	}
	
	
	
	
	
	@Bean
	public Step loadFileTeskletStep() {
		
	
		return stepBuilderFactory
				.get("LoadFileTasklet")
				.tasklet(new LoadFileTasklet(pahtFileIn))
				.build();
	}

	
	@Bean
	public Step consolidaVendaChuckletStep(ItemReader<VendaConsolidadaDto> vendasConsolidadaItemReader,
											ItemProcessor<VendaConsolidadaDto,VendaConsolidada> vendasConsolidadaItemProcessor,
											 ItemWriter<VendaConsolidada> vendasConsolidadaItemWriter) {
		
		
		System.out.println("Chunklet");
		
		return stepBuilderFactory
				.get("consolidaVendaChuckletStep")
				.<VendaConsolidadaDto, VendaConsolidada>chunk(10)
				.reader(vendasConsolidadaItemReader)
				.processor(vendasConsolidadaItemProcessor)
				.writer(vendasConsolidadaItemWriter)
				.build()
				;
		

	}
	
	@Bean
	public ItemReader<VendaConsolidadaDto> vendasConsolidadaItemReader(){
		return new VendaConsolidadaItemReader();
	}
	
	@Bean
	public ItemProcessor<VendaConsolidadaDto,VendaConsolidada> vendasConsolidadaItemProcessor(){
		return new VendaConsolidadaItemProcessor();
	}
	
	@Bean
	public ItemWriter<VendaConsolidada> vendasConsolidadaItemWriter(){
		return new VendaConsolidadaItemWriter(this.pahtFileOut);
	}

}
