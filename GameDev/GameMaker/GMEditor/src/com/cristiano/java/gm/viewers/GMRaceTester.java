package com.cristiano.java.gm.viewers;

import com.cristiano.java.gm.visualizadores.GMBuilder;

public class GMRaceTester extends GMBuilder {
	
	
	public GMRaceTester(){
		super();
		rootTag = "gamemechanic macroDefinition raceTest leaf";//RacingTestGameDefinitions
		/*
		 * MacroDefs: RacingTestGammeDefinitions
		 * GameGenre: GameGenreTestRace
		 * GameOpposition: RaceOppositionMulti / RaceOppositionDual
		 * MapLocation: RaceTestLocationML
		 * Maps: raceTestMapsGD
		 * */
	}
	
	public static void main(String[] args) {
		
		GMRaceTester app = new GMRaceTester();
		app.start();
	}
	
}
