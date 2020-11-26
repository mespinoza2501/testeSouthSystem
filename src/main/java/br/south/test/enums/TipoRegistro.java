package br.south.test.enums;


public enum TipoRegistro {
	
	VENDEDOR("001"), CLIENTE("002"), VENDAS("003");
	
	String valorRegistro;
	
	
	TipoRegistro(String valor){
		this.valorRegistro = valor;
	}
	
	public String getValor() {
		return valorRegistro;
	}
	
	
	

}
