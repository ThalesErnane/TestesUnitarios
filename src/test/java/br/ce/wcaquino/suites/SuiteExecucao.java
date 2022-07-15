package br.ce.wcaquino.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.services.CalculoValorLocacaoTest;
import br.ce.wcaquino.services.LocacaoServiceTest2;

@RunWith(Suite.class)
@SuiteClasses({ // define todas as class que seram executadas no package 
	// CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest2.class
})
public class SuiteExecucao {
	// remova se puder !
	
//	@BeforeClass
//	public static void before() {
//		System.out.println("before");
//	}
//	
//	@AfterClass
//	public static void after() {
//		System.out.println("after");
//	}
}
