package com.cristiano.java.gm.viewers;

import com.cristiano.java.gm.visualizadores.GMBuilder;

public class GMTerrainTester extends GMBuilder  {
	
	
	public GMTerrainTester(){
		super();
		rootTag = "gamemechanic macroDefinition terrainTestMD leaf";//TerrainTestDefinition
	}
	
	public static void main(String[] args) {
		
		GMTerrainTester app = new GMTerrainTester();
		app.start();
	}
	
}
