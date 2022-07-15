package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {
	
	private Filme filme;
	
	// Todo método retorna uma instância do FilmeBuilder (builder)
	// Padrão Chaning Method 
	// construtor privado 
	private FilmeBuilder(){}
	
	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(2);
		builder.filme.setNome("Filme 1");
		builder.filme.setPrecoLocacao(4.0);
		
		return builder;
	}
	
	
	public static FilmeBuilder umFilmeSemEstoque() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(0);
		builder.filme.setNome("Filme 1");
		builder.filme.setPrecoLocacao(4.0);
		
		return builder;
	}
	
	public FilmeBuilder semExtoque(){
		filme.setEstoque(0);
		return this; // retorna a propria instância 
	}
	
	
	
	public FilmeBuilder comValor(Double valor){
		filme.setPrecoLocacao(valor);
		return this; // retorna a propria instância 
	}
	
	// não retorna uma instância do FilmeBuilder
	public Filme agora() {
		return filme;
	}

}
