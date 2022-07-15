package br.ce.wcaquino.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.servicos.Calculadora;

public class CalculadoraMockTest {

	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devoMostrarADiferencaoEntreMockSpy() {
		// Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod(); // chama o metodo real 
		Mockito.when(calcMock.somar(1, 2)).thenReturn(8); // 
		// retorna 8, se os valores forem iguais ao passado no Mack, fora disso retorna 0 
		System.out.println("Mock: " + calcMock.somar(1, 2)); 
		
		Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
		// retorna 8, se os valores forem iguais ao passado no Spy, fora disso retorna o valor do metodo somar(4, 1) = 5 
		System.out.println("Spy: " + calcSpy.somar(1, 2));
		Mockito.doNothing().when(calcSpy).imprime(); // não executa o metodo 
		
	}
	
	
	@Test
	public void  Teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
		
		Assert.assertEquals(5, calc.somar(1, 8));
		
	}
	
	@Test
	public void  calcularUsandoCapture() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCpt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argCpt.capture(), Mockito.anyInt())).thenReturn(5);
		// Mockito.when(calc.somar(argCpt.capture(), argCpt.capture())).thenReturn(5);
		
		Assert.assertEquals(5, calc.somar(1, 100000));
		System.out.println(argCpt.getValue());
		System.out.println(argCpt.getAllValues());
	}
	
	
}
