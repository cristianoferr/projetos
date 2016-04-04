package com.cristiano.java.genProg.tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.genProg.GPManager;
import com.cristiano.java.genProg.GPSettings;
import com.cristiano.java.genProg.consts.GPConsts;
import com.cristiano.java.genProg.fitness.GPFitness;


public class TesteGenProg {
	ElementManager em=null;
	GPSettings gps;
	GPManager gpm;
	String rootTag="teste gp pai base";
	
	
	@Before
	public void tearUp() throws IOException {
		em=new ElementManager();
		em.loadBlueprintsFromFile();
		gps=new GPSettings();
		gps.setMaxVariacoes(10);
		gps.setQtdTestesPerInstancia(5);
		gpm=new GPManager(gps,em);
	}
	
	@Test public void testFitness() {
		GPFitness fitness=new GPFitness("teste",GPConsts.PESO_BAIXO); 
		assertNotNull(fitness);
		gps.addFitness(fitness);
	}
	
	
	@Test public void testFuncao() {
		gpm.geraVariacoes(rootTag);
		assertEquals(gpm.size(),gps.getMaxVariacoes());
	}
	
	}


