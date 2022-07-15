package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {
	
	private Usuario usuario;
	
	// construtor privado, para que ninguem possa criar instancias do builder externamente do proprio builder  
	private UsuarioBuilder() {} 
	
	
	// metodo  public static para q possa ser acessado externamente sem a necessidade de uma instância 
	// este metodo sera a porta de entrada para criação de um usuario 
	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder builder = new UsuarioBuilder(); // cria a instância do builder 
		builder.usuario = new Usuario(); // inicializa um usuario 
		builder.usuario.setNome("Usuario 1"); // povoa o usuario com o dado qualquer 
		
		return builder; // retorna a instância do usuario 
	}
	
	public UsuarioBuilder comNome(String nome) {
		usuario.setNome(nome);
		return this;
	}
	
	public Usuario agora() {
		return usuario;
	}

}
