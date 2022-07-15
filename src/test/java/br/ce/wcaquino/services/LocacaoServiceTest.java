package br.ce.wcaquino.services;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	
	@Test
	public void testeLocacao() {
		
		// cenário de Test
		   LocacaoService service = new LocacaoService();
		   Usuario usuario = new Usuario("Usuario 1");
		  List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		
		// ação de Test
		  Locacao locacao;
		  
		try {
			
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
			   error.checkThat(locacao.getValor(), is(CoreMatchers.is(6.0)));
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Assert.fail();
			Assert.fail("Não deveria lançar exceção");
		}
		   

	}

}
