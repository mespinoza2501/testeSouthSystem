package br.south.test.service;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.south.test.dto.ClienteDto;
import br.south.test.dto.ItemVendaDto;
import br.south.test.dto.VendaConsolidadaDto;
import br.south.test.dto.VendasDto;
import br.south.test.dto.VendedorDto;
import br.south.test.entity.VendaConsolidada;
import br.south.test.enums.TipoRegistro;


@Service
public class VendaConsolidadaService  {
	
	
	public static final org.slf4j.Logger LOGGER =  LoggerFactory.getLogger(VendaConsolidadaService.class);

	private static final String A_CADA_10_SEGUNDOS =  "*/10 * * * * *";
	
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired 
	private Job job;
	
	
	@Scheduled(cron = A_CADA_10_SEGUNDOS)
	public BatchStatus batchExecute() {
		Map<String, JobParameter> map = new HashMap<>();
		
		map.put("time", new JobParameter(System.currentTimeMillis()));
		
		try {
			JobExecution jobexc =  jobLauncher.run(job, new JobParameters(map));
			while (jobexc.isRunning()) {
				LOGGER.info("Job Em Execução...");				
				
			}
			
			return jobexc.getStatus();
			
		}catch (Exception e) {
			LOGGER.info("Job Falhou");
			return org.springframework.batch.core.BatchStatus.FAILED;
		}
	}
	

	public VendaConsolidada consolidaVenda(VendaConsolidadaDto consolidadaDto) {
		
		VendaConsolidada vendaConsolidadaReturn =  new VendaConsolidada();
		
		
		if(consolidadaDto != null) {
			vendaConsolidadaReturn.setQtClientes(consolidadaDto.getClientesDto().stream().count());
			vendaConsolidadaReturn.setQtVendedores(consolidadaDto.getVendedoresDto().stream().count());
			vendaConsolidadaReturn.setIdMelhorVenda(consolidadaDto
															.getVendasDto()
															.stream()
															.max(Comparator.naturalOrder())
															.get()
															.getIdVenda());
			vendaConsolidadaReturn.setNmPiorVendedor(consolidadaDto
															.getVendasDto()
															.stream()
															.min(Comparator.naturalOrder())
															.get()
															.getNmVendedor());
			System.out.println("consolidaVenda");
			return vendaConsolidadaReturn;
			
		}
		
		return null;
	}


	public void writerVendaConsolidadaFileDone(List<? extends VendaConsolidada> vendaConsolidada, String pathFIleOut) {

		
		SimpleDateFormat dataFormatada = new SimpleDateFormat("yyyyMMddHHmmss");
		File diretorioSaida = new File(pathFIleOut + "/"+ dataFormatada.format(new Date()) +"done.dat");		
		FileWriter fileWriter;
		try {
			
			fileWriter = new FileWriter(diretorioSaida, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);		
		
			bufferedWriter.write(String.valueOf(vendaConsolidada.stream()
																.findFirst()
																.get()
																.getQtVendedores()));
			
			bufferedWriter.write("ç");
			bufferedWriter.write(String.valueOf(vendaConsolidada.stream()
												.findFirst()
												.get()
												.getQtClientes()));
			bufferedWriter.write("ç");
			bufferedWriter.write(String.valueOf(vendaConsolidada.stream()
												 .findFirst()
												 .get()
												 .getIdMelhorVenda()));
			bufferedWriter.write("ç");
			bufferedWriter.write(String.valueOf(vendaConsolidada.stream()
												 .findFirst()
												 .get()
												 .getNmPiorVendedor()));
			bufferedWriter.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
	}


	public List<VendaConsolidadaDto> carregaArquivo(String pathDiretorio) {

		

		File diretorio = new File(pathDiretorio);
		List<VendaConsolidadaDto> vendaConsolidadaDtoListReturn = new ArrayList<VendaConsolidadaDto>();
		VendaConsolidadaDto vendaConsolidadaDto = new VendaConsolidadaDto();

		List<ClienteDto> clientesList = new ArrayList<ClienteDto>();
		List<VendedorDto> vendedoresDtoList = new ArrayList<VendedorDto>();
		List<VendasDto> vendasDtoList = new ArrayList<VendasDto>();

		if (diretorio.listFiles().length > 0) {
			List<File> arquivos = Arrays.asList(diretorio.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					// Filtrando apenas os arquivos .dat
					return name.toLowerCase().endsWith(".dat");
				}
			}));

			// VERIFICANDO SE HA ARQUIVO .DAT
			if (arquivos.stream().count() > 0L) {
				arquivos.stream().forEach(e -> {
					try {

						BufferedReader br = new BufferedReader(new FileReader(e));
						while (br.ready()) {
							String[] arrayRegistroArquivo = br.readLine().split("ç");
							// Clientes
							if (arrayRegistroArquivo[0].equals(TipoRegistro.CLIENTE.getValor())) {
								ClienteDto cliente = new ClienteDto();

								cliente.setCNPJ(arrayRegistroArquivo[1]);
								cliente.setNome(arrayRegistroArquivo[2]);
								cliente.setAreaNegocio(arrayRegistroArquivo[3]);
								clientesList.add(cliente);

							}
							// Vendedor
							if (arrayRegistroArquivo[0].equals(TipoRegistro.VENDEDOR.getValor())) {
								VendedorDto vendedorDto = new VendedorDto();

								vendedorDto.setCPF(Long.parseLong(arrayRegistroArquivo[1]));
								vendedorDto.setNome(arrayRegistroArquivo[2]);
								vendedorDto.setSlario(new BigDecimal(arrayRegistroArquivo[3]));
								vendedoresDtoList.add(vendedorDto);

							}

							// Vendas
							if (arrayRegistroArquivo[0].equals(TipoRegistro.VENDAS.getValor())) {
								VendasDto vendasDto = new VendasDto();
								List<ItemVendaDto> itemVendaDtoList = new ArrayList<ItemVendaDto>();
								BigDecimal vlTotalVenda = BigDecimal.ZERO;

								vendasDto.setIdVenda(Long.parseLong(arrayRegistroArquivo[1]));
								List<String> ItensVendasString = Arrays
										.asList(arrayRegistroArquivo[2].replace("[", "").replace("]", "").split(","));

								// Buscando os Itens de Um venda
								ItensVendasString.stream().forEach(item -> {
									ItemVendaDto itemVendaDto = new ItemVendaDto();
									itemVendaDto.setIdItemVenda(Long.parseLong(item.split("-")[0]));
									itemVendaDto.setQtItem(Long.parseLong(item.split("-")[1]));
									itemVendaDto.setVlItem(new BigDecimal(item.split("-")[2]));

									itemVendaDtoList.add(itemVendaDto);
								});

								// Calculando o Total da Venda
								vlTotalVenda = itemVendaDtoList.stream().map(ItemVendaDto::getVlItem)
										.reduce(BigDecimal.ZERO, BigDecimal::add);

								vendasDto.setNmVendedor(arrayRegistroArquivo[3]);
								vendasDto.setItens(itemVendaDtoList);
								vendasDto.setVlTotalVenda(vlTotalVenda);

								vendasDtoList.add(vendasDto);

							}

						}

					} catch (IOException e1) {

						e1.printStackTrace();
					}
				});

				vendaConsolidadaDto.setClientesDto(clientesList);
				vendaConsolidadaDto.setVendasDto(vendasDtoList);
				vendaConsolidadaDto.setVendedoresDto(vendedoresDtoList);
				vendaConsolidadaDtoListReturn.add(vendaConsolidadaDto);
				return vendaConsolidadaDtoListReturn;
			}
		}

		return null;
	}

}
