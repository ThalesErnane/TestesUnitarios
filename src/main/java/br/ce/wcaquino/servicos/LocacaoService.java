package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;


public class LocacaoService {
	
	// Após a injeção de dependência dos @Mock e @InjectMocks, não será preciso os metodos get e set 
	
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;

	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmesSemEstoqueException, LocadoraException { // lançando as exceções criadas 
		
		if(usuario == null) {
			throw new LocadoraException("Usuário vazio");
			
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");			
		}
		for(Filme filme: filmes) {
			
			if(filme.getEstoque() == 0) {
			throw new FilmesSemEstoqueException();
			// throw new Exception("Filme não possui no estoque");
		}
			}
		
		boolean negativado;
		
		try {
			negativado = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
				throw new LocadoraException("Problemas com SPC, tente novamente");
		}
		
		if(negativado) {
			throw new LocadoraException("Usuário negativado");
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		//locacao.setDataLocacao(new Date()); // obtendo a data atual
		locacao.setDataLocacao(obterData());		
		locacao.setValor(calcularValorLocacao(filmes));

		//Entrega no dia seguinte
		// Date dataEntrega = new Date();
		Date dataEntrega = obterData();
		dataEntrega = adicionarDias(dataEntrega, 1);
		
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1); // joga para segunda feira 
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}

	public Date obterData() {
		return new Date();
	}
		
	private Double calcularValorLocacao(List<Filme> filmes) {
		System.out.println("Estou calculando !!");
		Double valorTotal = 0d;
		for(int i = 0; i < filmes.size(); i++) {
			
			Filme filme = filmes.get(i); // pega os filmes 
			Double valorFilme = filme.getPrecoLocacao(); // soma todos os preços e atribui na var 
			switch (i) {
					case 2: valorFilme = valorFilme * 0.75; break;
					case 3: valorFilme = valorFilme * 0.5; break;
					case 4: valorFilme = valorFilme * 0.25; break;
					case 5: valorFilme = 0d; break;
			}
			 valorTotal += valorFilme;
		}
		return valorTotal;
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocaoesPendentes();
		for(Locacao locacao: locacoes) { // envia o e-mail para cada locacao
			if(locacao.getDataRetorno().before(obterData())) { // irá encaminhar email cuja data esteja anteriores a data atual 
			emailService.notificarAtraso(locacao.getUsuario()); // envia para o usuario da locacao 
		}
			}
	}
	
	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(obterData());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		dao.salvar(novaLocacao);
	}
	
	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}
	
	public void setSPCService(SPCService spc) {
		spcService = spc;
	} 

	public void setEmailService(EmailService email) {
		emailService = email;
	}
	
	
}