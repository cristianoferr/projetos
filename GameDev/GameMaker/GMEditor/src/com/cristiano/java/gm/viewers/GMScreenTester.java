package com.cristiano.java.gm.viewers;

import com.cristiano.java.gm.visualizadores.GMBuilder;
import com.cristiano.utils.CRJavaUtils;

/*
 * Essa camada foca no produto final, considerando que os elementos foram carregados
 * */
public class GMScreenTester extends GMBuilder  {
	
	
	public GMScreenTester(){
		super();
		rootTag = "gamemechanic macroDefinition screenNavigation leaf";
	}
	
	public static void main(String[] args) {
		
		GMScreenTester app = new GMScreenTester();
		CRJavaUtils.NIFTY_DEBUG=false;
		app.start();
		
	}
	
}
