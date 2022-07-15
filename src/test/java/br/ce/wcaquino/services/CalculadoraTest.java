package br.ce.wcaquino.services;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.servicos.Calculadora;


// @RunWith(BlockJUnit4ClassRunner.class)
// @RunWith(ParallelRunner.class)
public class CalculadoraTest {
	
	public static StringBuffer ordem = new StringBuffer();
	
	private Calculadora calc;
	
	@Before 
	public void setup() {
		calc = new Calculadora();
		System.out.println("iniciando");
		ordem.append("1");
	}
	
	@AfterClass
	public void tearDown() {
		System.out.println("finalizando" + ordem);
	}
	
	
	@Test
	public void deveSomarDoisValores() {
		// cenário 
			int a = 5;
			int b = 3;
	
		// ação 
			int resultado = calc.somar(a , b); // capiturando o resultado da soma e atribuindo em uma var
		
		// verificação 
			Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		// cenario 
		int a = 8;
		int b = 5;
		
		// ação 
		int resultado = calc.subtrair(a , b);
		
		// verificação 
		
		Assert.assertEquals(3, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		// cenário
		int a = 6;
		int b = 3;
	
		// ação 
		
		int resultado = calc.dividir(a,b);
		
		// verificação 
		
		Assert.assertEquals(2, resultado);
		
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class) // exceção esperada 
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		// cenário
		int a = 10;
		int b = 0;
		
		// ação 
		
		calc.dividir(a, b);
		
		// Verificação 
		
	
	}

}
