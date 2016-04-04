package com.cristiano.java.blueprintManager.tests;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.cristiano.java.bpM.utils.FunctionHelper;
import com.cristiano.java.product.utils.StringHelper;


public class TesteHelpers {
	
	
	
	@Before
	public void tearUp() {
	}
	
	@Test public void testSeparaFuncaoEmElementos() {
		validSeparaFuncao("120>30", ">", "","120","30","");
		validSeparaFuncao("1=2,120>30,20", ">", "1=2,","120","30",",20");
		validSeparaFuncao("if(mod(#index,2)<4/4,0.5,-0.5)","<", "if(", "mod(#index,2)","4/4",",0.5,-0.5)");
		
		
	}

	private void validSeparaFuncao(String property, String oper,  String p0, String p1, String p2, String p3) {
		int pos=property.indexOf(oper);
		String[] pars = FunctionHelper.separaFuncaoEmElementos(property, pos, oper.length());
		assertTrue("Error on 0:'"+pars[0]+"' should be '"+p0+"'",pars[0].equals(p0));
		assertTrue("Error on 1:'"+pars[1]+"' should be '"+p1+"'",pars[1].equals(p1));
		assertTrue("Error on 2:'"+pars[2]+"' should be '"+p2+"'",pars[2].equals(p2));
		assertTrue("Error on 3:'"+pars[3]+"' should be '"+p3+"'",pars[3].equals(p3));
	}
	
	
	
	@Test public void testParamsNumeric() {
		assertTrue(FunctionHelper.areParamsNumeric("1,2"));
		assertTrue(FunctionHelper.areParamsNumeric("1,2,4"));
		assertFalse(FunctionHelper.areParamsNumeric("1,2,a"));
		assertFalse(FunctionHelper.areParamsNumeric("#index,2,3"));
		assertFalse(FunctionHelper.areParamsNumeric("$index,2,3"));
		assertFalse(FunctionHelper.areParamsNumeric("$1,2,3"));
	}
	
	@Test public void testSerializaHashMap() {
		HashMap<String, String> paramData=new HashMap<String, String>();
		paramData.put("par0", "val0");
		paramData.put("par1", "val1");
		paramData.put("par2", "val2");
		String serializaHashMap = StringHelper.serializaHashMap(paramData);
		assertTrue(serializaHashMap.contains("par0=val0"));
		assertTrue(serializaHashMap.contains("par1=val1"));
		assertTrue(serializaHashMap.contains("par2=val2"));
		
		paramData=new HashMap<String, String>();
		 serializaHashMap = StringHelper.serializaHashMap(paramData);
		assertTrue(serializaHashMap.equals(""));
	}
	
	
	@Test public void testcheckIfListContainsItem() {
		String lista="[ID1,ID2,ID3,BP4,BP5]";
		String item="ID1";
		testaChecaIfListaContainsItem(lista, item,true);
		testaChecaIfListaContainsItem("[ID1,ID2,ID3,BP4,BP5]", "ID3",true);
		testaChecaIfListaContainsItem("[ID1,ID2,ID3,BP4,BP5]", "[ID3]",true);
		testaChecaIfListaContainsItem("[ID1,ID2,ID3,BP4,BP5]", "BP5",true);
		testaChecaIfListaContainsItem("[ID1,ID2,ID3,BP4,BP5]", "ID8",false);
		testaChecaIfListaContainsItem("[ID1,ID2,ID3,BP4,BP5]", "",false);
		testaChecaIfListaContainsItem("[ID1,ID2,ID3,BP4,BP5]", "[]",false);
	}
	
	@Test public void testConcatenaLista() {
		
		testaConcatenaLista("[ID1,ID2]", "ID3","[ID1,ID2,ID3]");
		testaConcatenaLista("[ID1,ID2]", "[]","[ID1,ID2]");
		testaConcatenaLista("[ID1,ID2]", "[ID2,ID3]","[ID1,ID2,ID3]");
		testaConcatenaLista("[ID1,ID2]", "[ID3,ID4]","[ID1,ID2,ID3,ID4]");
		
	}
	
	@Test public void testCountItemFromList() {		
		
		
		testaCountLista("[ID1,ID2]", 2);
		testaCountLista("[ID1,ID2,ID4]", 3);
		testaCountLista("[]", 0);
		testaCountLista("", 0);
	}
	
	@Test public void testGetParam() {		
		
		String funcao="123,a+b,(123+234,23),1/2/3";
		int par=0;
		String esperado="123";
		testaGetParam(funcao, par, esperado);
		testaGetParam(funcao, 1, "a+b");
		testaGetParam(funcao, 2, "(123+234,23)");
		testaGetParam(funcao, 3, "1/2/3");
		testaGetParam(funcao, 4, null);
		 
	}
	
	@Test public void testGetParamsFromFuncao() {		
		
		String funcao="func(abc,1+2)";
		
		testaGetParamsFromFuncao(funcao, 0, "abc",true);
		testaGetParamsFromFuncao(funcao, 1, "1+2",true);
		
		testaGetParamsFromFuncao(funcao, 0, "1abc",false);
		testaGetParamsFromFuncao(funcao, 1, "1+22",false);
		testaGetParamsFromFuncao(funcao, 1, "",false);
		
	}
	
	@Test public void testGetPalavraNaPosicao() {		
		
		
		testaGetPalavra("   abcd   ","abcd",3);
		testaGetPalavra("   abcd#4   ","abcd",3);
		testaGetPalavra("   abcd.8   ","abcd",3);
		
		
	}


	private void testaGetPalavra(String prop,String esperado,int pos) {
		String saida = StringHelper.pegaPalavraNaPosicao(prop, pos,false);
		assertTrue(saida,saida.equals(esperado));
	}

	
	private void testaGetParamsFromFuncao(String funcao, int par, String esperado, boolean b) {
		String[] pars = StringHelper.getParamsFromFuncao("func", funcao);
		String saida=pars[par];
		if (saida==null){
			assertTrue(saida==esperado);
			return;
		}
		assertTrue("getParamsFromFuncao :"+saida+","+par+" devia ser ("+b+") "+esperado,saida.equals(esperado)==b);
	}


	private void testaGetParam(String funcao, int par, String esperado) {
		String saida=StringHelper.getParam(par, funcao);
		if (saida==null){
			assertTrue(saida==esperado);
			return;
		}
		assertTrue("GetParam :"+saida+","+par+" devia ser "+esperado,saida.equals(esperado));
	}


	private void testaCountLista(String lista, int esperado) {
		int saida=StringHelper.countItemFromList(lista);
		assertTrue("CountItemFromList ('"+lista+"'):"+saida+" devia ser "+esperado,saida==esperado);
	}


	private void testaConcatenaLista(String l1,String l2,String esperado) {
		String saida=StringHelper.concatenaLista(l1,l2);
		assertTrue("Concatena lista:"+saida,saida.equals(esperado));
	}


	private void testaChecaIfListaContainsItem(String lista, String item,boolean flagContem) {
		boolean result=StringHelper.checkIfListContainsItem(lista, item);
		if (flagContem){
			assertTrue("Lista '"+lista+"' devia conter '"+item+"'",result);
		} else {
			assertTrue("Lista '"+lista+"' n�o devia conter '"+item+"'",!result);
		}
	}

}


