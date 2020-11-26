package br.south.test.validate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ValidateFile {
	

	private String diretorioEntrada;
	
	public ValidateFile(String diretorioEntrada) {
		this.diretorioEntrada =  diretorioEntrada;
	}
	
	
	private boolean ValidaArquivo(File file) throws IOException {
		
		
		if(file.exists()) {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			while (bufferedReader.ready()) {
				String[] arrayRegistroArquivo = bufferedReader.readLine().split("ç");
				
				if(arrayRegistroArquivo.length != 4) {
					bufferedReader.close();
					return false;
				}
			} 
			bufferedReader.close();
		}
		else {
			return false;
		}
		
		return true;
		
	}
	
	public void Validade() {

		if (!diretorioEntrada.isEmpty()) {
			File diretorio = new File(diretorioEntrada);
			if (diretorio.listFiles().length > 0) {
				// valida apenas arquivos .dat ---caso tenha que validar outros deve se
				// implementado dinamicamente esse filtro
				List<File> arquivos = Arrays.asList(diretorio.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {

						return name.toLowerCase().endsWith(".dat");
					}
				}));

				arquivos.stream().forEach(e -> {
					try {
						if(!ValidaArquivo(e)) {
							e.renameTo(new File(diretorioEntrada + "/"+e.getName() + ".error"));
							e.delete();
						}
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
				});
			}

		}

	}
	
	

}
