package com.cristiano.java.gm.viewers;

import com.cristiano.java.gm.visualizadores.GMBuilder;

public class GMAITester extends GMBuilder  {
	
	
	public GMAITester(){
		super();
		rootTag = "gamemechanic macroDefinition testing leaf";//TestMacroDefinitions
	}
	
	public static void main(String[] args) {
		
		GMAITester app = new GMAITester();
		app.start();
	}
	
}
