package com.cristiano.java.gm.viewers;

import com.cristiano.java.gm.visualizadores.GMBuilder;

public class GMRacingGame extends GMBuilder  {
	
	
	public GMRacingGame(){
		super();
		rootTag = "gamemechanic macroDefinition race leaf";
	}
	
	public static void main(String[] args) {
		
		GMRacingGame app = new GMRacingGame();
		app.start();
	}
	
}
