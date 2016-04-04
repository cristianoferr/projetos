package com.cristiano.java.gm.viewers;

import com.cristiano.java.gm.visualizadores.GMBuilder;

public class GMArena extends GMBuilder  {
	
	
	public GMArena(){
		super();
		rootTag = "gamemechanic macroDefinition arena leaf";
		/*ArenaDefinitions
		 * Genre: GameGenreArena
		 * Location: LandArenaML
		 */
		
	}
	
	public static void main(String[] args) {
		
		GMArena app = new GMArena();
		app.start();
	}
	
}
