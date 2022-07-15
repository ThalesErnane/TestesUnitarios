package br.ce.wcaquino.services;


import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.servicos.EmailService;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.servicos.SPCService;
import br.ce.wcaquino.utils.DataUtils;

 @RunWith(PowerMockRunner.class)
 @PrepareForTest({
	LocacaoService.class 
	// DataUtils.class
	})
public class LocacaoServiceTest2 {
		
	// organizando os imports ctrl + shift + O
	// var global 
	@InjectMocks  @Spy
	private LocacaoService service;
	
	@Mock
	private SPCService spc;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService email;
	
	// definição do contador 
	private static int contador = 0; // var instância da class, valor sera mantido o JUnit não irá reiniciar a var sendo static 
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// criando Spy do PowerMock
		service = PowerMockito.spy(service);
		
		// Após injetar a annotation @Mock e @InjectMocks nas classes 
		// não será preciso Mockar as classes abaixo 
//		service = new LocacaoService();
//		dao = Mockito.mock(LocacaoDAO.class);
//		service.setLocacaoDAO(dao);
//		spc = Mockito.mock(SPCService.class);
//		service.setSPCService(spc);
//		email = Mockito.mock(EmailService.class);
//		service.setEmailService(email);
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
	public void deveAlugarFilme() throws Exception { // lançando a exceção para JUnit
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY)); // verifica se o dia da semana de hj, não pode ser um sabado
		
		// cenário de Test
		  //  LocacaoService service = new LocacaoService();
		   Usuario usuario = new Usuario("Usuario 1");
		   List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
		
		   System.out.println("Teste");	
		   
		// ação de Test
		  Locacao locacao;
		  
			
			   locacao = service.alugarFilme(usuario, filmes);
			
			
			// verificação do Test
			   Assert.assertEquals(5.0, locacao.getValor(),  0.01);
			   // System.out.println(locacao.getDataLocacao());
			   Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date())); // Verifica se é igual a data atual
			   // System.out.println(locacao.getDataRetorno());
			   Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
		    // Ultilizando AssertThat 	
			   Assert.assertThat(locacao.getValor(),  CoreMatchers.is(5.0)); // verificaque q o valor da locação é igual a 5.0
			   assertThat(locacao.getValor(),  is(5.0)); // ultilizando import static > bot'ao direito do mouse, source > add import 
		
			   Assert.assertThat(locacao.getValor(),  CoreMatchers.equalTo(5.0)); // verificaque q o valor da locação é igual a 5.0
			   // Assert.assertThat(locacao.getValor(),  CoreMatchers.not(5.0)); // verificaque q o valor da locação não é igual a 5.0
			   Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
			   Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		
			   
		    // Ultilizando Rule, obs se houver mais de um erro na checagem ele mostra todos os erro 
			   error.checkThat(locacao.getValor(), is(CoreMatchers.is(5.0)));
		   

	}
	
	@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		// cenario 
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// mockando metodos privados com Power Mock 
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes); // não é impresso o sout do metodo
		
		// acao 
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		// verificacao 
		Assert.assertThat(locacao.getValor(), is(1.0));
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
		

	}
	
	@Test  // executando metodos privados diretamente
	public void deveCalcularValorLocacao() throws Exception {
		// cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// acao 
		Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);
		
		//verificacao 
		Assert.assertThat(valor, is(4.0));
		
	} 
	
	@Test
	public void deveAlugarFilme2() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
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
	public void deveAlugarFilmeComPowerMock() throws Exception {		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());
		
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(06, 06, 2022));
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
		//error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getValor(), CoreMatchers.equalTo(5.0));
		error.checkThat(locacao.getDataLocacao(), ehHoje()); // import static ctrl + shift + M
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(06, 06, 2022)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(07, 06, 2022)), is(true));
		PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments(); // verificar quantas vezes o new Date é chamado 
	}
	
	@Test
	public void deveAlugarFilmeUsandoComPowerMockComMetodosStaticos() throws Exception {		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 06);
		calendar.set(Calendar.MONTH, Calendar.JUNE);
		calendar.set(Calendar.YEAR, 2022);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
		assertThat(locacao.getDataRetorno(), MatchersProprios.caiNumaSegunda());
		
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();
	}
	
	
	// Forma Elegante 
	// ou @Test(expected = FilmeSemEstoqueException.class)
	@Test(expected = Exception.class) // informando a exceção experada, forma elegante 
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		
		// cenário de Test
		   LocacaoService service = new LocacaoService();
		   Usuario usuario = new Usuario("Usuario 1");
		   List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		
		// ação de Test
		  service.alugarFilme(usuario, filmes);
	}
	
	/* Forma Robusta 
	@Test
	public void testeLocacao_filmeSemEstoque_2() {
		
		// cenário de Test
		   LocacaoService service = new LocacaoService();
		   Usuario usuario = new Usuario("Usuario 1");
		   Filme filme = new Filme("Filme 1", 3, 5.0);
		
		// ação de Test
		  try {
			  
			service.alugarFilme(usuario, filme);
			// Assert.fail("Deveria lançar uma exceção"); // se resguardando esperando uma exceção 
			
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), is("Filme não possui no estoque"));
		}
	}
*/
	
	/* Forma nova
	@Test
	public void testeLocacao_filmeSemEstoque_3() throws Exception {
		
		// cenário de Test
		  // LocacaoService service = new LocacaoService();
		   Usuario usuario = new Usuario("Usuario 1");
		   Filme filme = new Filme("Filme 1", 0, 5.0);
		
		   // tem que ser declarada antes da execução da ação, as exception
		   exception.expect(Exception.class);
		   exception.expectMessage("Filme não possui no estoque");
		   
		// ação de Test
		  service.alugarFilme(usuario, filme);
		  
		  }
	*/
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmesSemEstoqueException { // lançando a FilmesSemEstoqueException, para que o JUnit trate 
		// cenario
		// LocacaoService service = new LocacaoService();
		List <Filme> filmes = Arrays.asList(new Filme("Filme 2", 1, 4.0));
		// Usuario usuario = new Usuario("Usuario 1");
		
		// ação 
	
			try {
				
				service .alugarFilme(null, filmes);
				Assert.fail(); // esperando lançar uma exceção 
				
			} catch (LocadoraException e) { // tratamento da LocadoraException

			Assert.assertThat(e.getMessage(), is("Usuário vazio")); // valida a mensagem enviada se combina com ("Usuario vazio")
			
			}
			
			System.out.println("Forma robusta");
	}
	
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmesSemEstoqueException, LocadoraException {
		// cenario 
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(LocadoraException.class); // esta esperando o erro 
		exception.expectMessage("Filme vazio"); // espera a mensagem 
		
		// ação 
		service.alugarFilme(usuario, null); // apresenta erro NullPointerException, pois o filme precisa estar em estoque 
		
		System.out.println("Forma nova");
	
	}
	
	
	@Test
	public void devePagar0PctNoFilme3() throws FilmesSemEstoqueException, LocadoraException {
		// cenário 
		Usuario usuario = new Usuario("Usuario 1");
		List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0), new Filme("Filme 6", 2, 4.0) );
		
		// ação 
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// verificação 
		// 4+4+3+2+1+0 = 14
		
		assertThat(resultado.getValor(), is(14.0)); // verifique que o valor da locação é 11
		
	}
	
	
	@Test
	public void devePagar25PctNoFilme3() throws FilmesSemEstoqueException, LocadoraException {
		// cenário 
		Usuario usuario = new Usuario("Usuario 1");
		List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0) );
		
		// ação 
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// verificação 
		// 4+4+3+2+1 = 14
		
		assertThat(resultado.getValor(), is(14.0)); // verifique que o valor da locação é 11
		
	}
	
	@Test
	public void devePagar50PctNoFilme3() throws FilmesSemEstoqueException, LocadoraException {
		// cenário 
		Usuario usuario = new Usuario("Usuario 1");
		List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0) );
		
		// ação 
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// verificação 
		// 4+4+3+2 = 13
		
		assertThat(resultado.getValor(), is(13.0)); // verifique que o valor da locação é 11
		
	}
	
	@Test
	public void devePagar75PctNoFilme3() throws FilmesSemEstoqueException, LocadoraException {
		// cenário 
		Usuario usuario = new Usuario("Usuario 1");
		List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0));
		
		// ação 
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// verificação 
		// 4+4+3 = 11
		
		assertThat(resultado.getValor(), is(11.0)); // verifique que o valor da locação é 11
		
	}
	
	@Test
	// @Ignore // ignora o método abaixo e não executa em teste 
	public void naoDeveDevolverFilmeNoDomingo() throws FilmesSemEstoqueException, LocadoraException {
		
		// executa o teste somente se o dia for num sábado 
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// cenario 
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		// acao 
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		// verificacao 
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
	
	}
	
	
	@Test 
	public void devoDevolverNaSegundaAlugadoNoSabado() throws FilmesSemEstoqueException, LocadoraException {
		
		// executa o teste somente se o dia for num sábado 
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// cenario 
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		// acao 
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		// verificacao 
		// assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		 assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.SUNDAY)); // verifique que o dia do retorno cai num domingo 
		 assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}
	
	
	@Test
	public void naoDeveAlugarFilmeParaNegativado() throws Exception {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// quando o spc possuiNegativacao for chamado passando um usuario, retorne true
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuário negativado");
		// acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoComOutroUsuario() throws Exception {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// quando o spc possuiNegativacao for chamado passando um usuario, retorne true
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuário negativado");
		// acao
		service.alugarFilme(usuario2, filmes);
		
		// Erro 
	}
	
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoComTryCatch() throws Exception {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// quando o spc possuiNegativacao for chamado passando um usuario, retorne true
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		// acao
		
		try {
			service.alugarFilme(usuario, filmes);
			// verificacao 
			Assert.fail();
		} catch (LocadoraException e) {
	
			Assert.assertThat(e.getMessage(), is("Usuário negativado"));
		}
		

		Mockito.verify(spc).possuiNegativacao(usuario2);
	}
	
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPCComOutroUsuario() throws Exception {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// quando o spc possuiNegativacao for chamado passando um usuario, retorne true
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		// acao
		try {
		
			service.alugarFilme(usuario, filmes);
		
		// verificacao 
			Assert.fail(); // caso a exception não seja lançado o fail vai garantir que não seja gerado um falso positivo
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário negativado"));
		}

		Mockito.verify(spc).possuiNegativacao(usuario2); // espera o usuario 2 
		// Test fail 
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// quando o spc possuiNegativacao for chamado passando um usuario, retorne true
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		// retorna true quando o metodo possuiNegativacao for chamado passando qualquer usuario
		// Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuário negativado");
		// acao
		service.alugarFilme(usuario, filmes);
		
		// verificacao 
		Mockito.verify(spc).possuiNegativacao(usuario);
	}
	
	
	@Test
	public void deveEnviarEmailParaLocacaoesAtrasadas() {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Locacao> locacoes = 
		Arrays.asList(LocacaoBuilder
				.umLocacao()
				.comUsuario(usuario)
				.comDataRetorno(DataUtils.obterDataComDiferencaDias(-2)).agora()); // atrasado 

		Mockito.when(dao.obterLocaoesPendentes()).thenReturn(locacoes);
		
		// acao 
		service.notificarAtrasos();
		
		// verificacao 
		Mockito.verify(email).notificarAtraso(usuario);
		
	}
	
	@Test
	public void deveEnviarEmailParaLocacaoesAtrasadasComOutroUsuario() {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
		
		List<Locacao> locacoes = 
		Arrays.asList(LocacaoBuilder
				.umLocacao()
				.comUsuario(usuario)
				.comDataRetorno(DataUtils.obterDataComDiferencaDias(-2)).agora()); // atrasado 

		Mockito.when(dao.obterLocaoesPendentes()).thenReturn(locacoes);
		
		// acao 
		service.notificarAtrasos();
		
		// verificacao 
		Mockito.verify(email).notificarAtraso(usuario2);
		
	}
	
	
	@Test
	public void deveEnviarEmailParaLocacaoesAtrasadasComOutroUsuario2() {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("outro Atrasado").agora();
		List<Locacao> locacoes = 
		Arrays.asList(LocacaoBuilder
				.umLocacao().atrasado().comUsuario(usuario).agora(),
				LocacaoBuilder
				.umLocacao().comUsuario(usuario2).agora(),
				LocacaoBuilder
				.umLocacao().atrasado().comUsuario(usuario3).agora()); // atrasado, caso for necessario enviar 2 emails repetir a mesma linha  

		Mockito.when(dao.obterLocaoesPendentes()).thenReturn(locacoes);
		
		// acao 
		service.notificarAtrasos();
		
		// verificacao 
		
	
		// foi realizado 2 verificações ao metodo notificarAtraso passando como parametro qualquer instancia de Usuario
		Mockito.verify(email, Mockito.times(2)).notificarAtraso(Mockito.any(Usuario.class));	// verifica quantos emails foi enviado não importa para quem 
		Mockito.verify(email).notificarAtraso(usuario);
		Mockito.verify(email).notificarAtraso(usuario3);
		// Mockito.verify(email, Mockito.times(2)).notificarAtraso(usuario3); // verificação para validar que o usuario 3, recebeu 2 emails
		Mockito.verify(email, Mockito.never()).notificarAtraso(usuario2); // usuario 2 não recebe email, fail
		Mockito.verifyNoMoreInteractions(email); // valida se não foi enviado nenhum email 
		// Mockito.verifyZeroInteractions(spc); // garantir que o serviço spc nunca for executado durante a execução deste cenario
	}
	
	
	@Test
	public void deveEnviarEmailParaLocacaoesAtrasadasCorrigido() {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();
		
		List<Locacao> locacoes = 
		Arrays.asList(
				LocacaoBuilder
				.umLocacao().atrasado().comUsuario(usuario).agora(),
				LocacaoBuilder
				.umLocacao().comUsuario(usuario2).agora(),
				LocacaoBuilder
				.umLocacao().atrasado().comUsuario(usuario3).agora());
				
		Mockito.when(dao.obterLocaoesPendentes()).thenReturn(locacoes);
		
		// acao 
		service.notificarAtrasos();
		
		// verificacao 
		Mockito.verify(email).notificarAtraso(usuario);
		Mockito.verify(email).notificarAtraso(usuario3);
		Mockito.verify(email, Mockito.never()).notificarAtraso(usuario2);
		Mockito.verifyNoMoreInteractions(email);
	}
	
	
	@Test
	public void deveTratarErroNoSPC() throws Exception {
		// cenario 
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// chama o matcher de negativação para esse user, então lança a Exception
		Mockito.when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha Catastrófica"));
		//Mockito.when(spc.possuiNegativacao(usuario)).thenThrow(new RuntimeException("Falha Catastrófica"));
		
		
		// verificação 
		exception.expect(LocadoraException.class);
		// exception.expect(RuntimeException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");
		// exception.expectMessage("Falha Catastrófica");
		
		// açao 
		service.alugarFilme(usuario, filmes);
		
	}
	
	
	@Test
	public void deveProrrogarUmaLocacao() {
		// cenario
		Locacao locacao = LocacaoBuilder.umLocacao().agora();
		
		// acao 
		service.prorrogarLocacao(locacao, 3);
		
		// verificacao
		// Mockito.verify(dao).salvar(Mockito.any(Locacao.class));
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		
		Locacao locacaoRetornada = argCapt.getValue();// pega o valor que foi passado ao salvar
		
		// validando mais de um match 
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
	}
	
	@Test
	public void deveDevolverNaSegundaAlugadoNoSabado() throws Exception {
		// Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cenario 
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(11, 06, 2022));
	
		// acao 
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		// verificacao 
		
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda()); // cai numa segunda 
		
	}
}
