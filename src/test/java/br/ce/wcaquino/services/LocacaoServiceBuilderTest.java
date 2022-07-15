package br.ce.wcaquino.services;

import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;

public class LocacaoServiceBuilderTest {
	
	// https://github.com/wcaquino/BuilderMaster
	// organizando os imports ctrl + shift + O
	
	private LocacaoService service;
	
	// definição do contador 
	private static int contador = 0; // var instância da class, valor sera mantido o JUnit não irá reiniciar a var sendo static 
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		System.out.println("Before");
		service = new LocacaoService();
		// incremento 
		contador++;
		System.out.println(contador);
	}
	
	@After
	public void tearDown() {
		System.out.println("After");
	}
	
	// executam antes ou após a classe ser inicializada ou finalizada 
	
	@BeforeClass
	public static void setupClass() {
		System.out.println("Before Class");
		
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println("After Class");
	}
	
	
	@Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
		//error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getValor(), CoreMatchers.equalTo(5.0));
		error.checkThat(locacao.getDataLocacao(), ehHoje()); // import static ctrl + shift + M
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
	}
	
	
	@Test
	public void deveAlugarFilmeExemploComFilmeBuilder() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
		//error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getValor(), CoreMatchers.equalTo(5.0));
		error.checkThat(locacao.getDataLocacao(), ehHoje()); // import static ctrl + shift + M
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
	}
	
	
	
	// Forma Elegante 
	@Test(expected = FilmesSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		
		// cenário de Test
		   LocacaoService service = new LocacaoService();
		   Usuario usuario = new Usuario("Usuario 1");
			List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semExtoque().agora());
		
		// ação de Test
		  service.alugarFilme(usuario, filmes);
	}
	
	
	@Test
	public void deveAlugarFilmePassandoValorAoMethodBuilder() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
		//error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getValor(), CoreMatchers.equalTo(5.0));
		error.checkThat(locacao.getDataLocacao(), ehHoje()); // import static ctrl + shift + M
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
	}
	
	
	// Forma Elegante 
	@Test(expected = FilmesSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoquePorChamadaDeMethod() throws Exception {
		
		// cenário de Test
		   LocacaoService service = new LocacaoService();
		   Usuario usuario = new Usuario("Usuario 1");
			List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora());
		
		// ação de Test
		  service.alugarFilme(usuario, filmes);
	}
	
	// executa como uma aplicação java 
	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}
	

}
