package com.cristiano.java.gm.viewers;

import com.cristiano.java.gm.visualizadores.GMBuilder;

public class GMShooterTester extends GMBuilder {
	
	
	public GMShooterTester(){
		super();
		rootTag = "gamemechanic macroDefinition shooter leaf";//ShooterMacroDefinitions
		/*
		 * MacroDefs: ShooterMacroDefinitions
		 * GameGenre: ShooterGenre
		 * GameOpposition: playerVersus
		 * MapLocation: ShooterML
		 * Maps: shooterMapsGD
		 * */
	}
	
	public static void main(String[] args) {
		
		GMShooterTester app = new GMShooterTester();
		app.start();
	}
	
}
