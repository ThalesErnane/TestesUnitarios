package br.ce.wcaquino.test;

import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

import org.junit.Assert;

public class AssertTest {
	
	@Test
	public void test() {
		Assert.assertTrue(true); // verifica as expressões true
		Assert.assertFalse(false); // verifica as expressões false
	
		Assert.assertEquals(1, 1); // verifica se os valores são iguais 
		Assert.assertEquals(0.51, 0.51, 0.01); // verifica se é igual validado a quantidade de casas depois do 0.00
		
		// tipos de dados (Primitivo ou Wrappers)
		int i = 5;
		Integer i2 = 5;
		
		Assert.assertEquals(Integer.valueOf(i), i2); // converte o primitivo para obj
		Assert.assertEquals(i, i2.intValue()); // converte o obj para primitivo
		
		// Comparando Strings 
		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "bola");
		// Assert.assertEquals("bola", "Bola"); // False da erro 
		Assert.assertTrue("bola".equalsIgnoreCase("Bola")); // ignora maiusculas ou minusculas 
		Assert.assertTrue("bola".startsWith("bo")); // valida se começa com bo
		
		// verificando igualdade de objs 
		Usuario us1 = new Usuario("Usuario 1");
		Usuario us2 = new Usuario("Usuario 1");
		Usuario us3 = us2; // aponta para mesma instância  
		Usuario us4 = null;
		
		
		Assert.assertEquals(us1, us2); // falha os objs não são iguais, 
		// se o método iguais for implementado na class, ele verifica o conteudo do obj Usuário (Usuario 1)  
		
		// validando se os objs são da mesma instância 
		// Assert.assertSame(us1, us2);
		Assert.assertSame(us3, us2); // true, aponta para mesma instância 
		Assert.assertNotSame(us3, us2);
		Assert.assertTrue( us4 == null); 
		Assert.assertNull(us4);
		Assert.assertNotNull(us4);
	}

}
