package com.cristiano.galactic.view;


/**
 * Essa classe é necessária pois há diferenças na implementação do cálculo das distancias.
 * Principalmente porque o jogador está sempre na posição 0,0,0 e porque o node do planeta que é atualizado a posição.
 * @author Cristiano
 *
 */
public class PlanetAppStateWrapper extends PlanetAppState {

	
	 @Override
	    public void update(float tpf) {
	        this.nearestPlanet = findNearestPlanet();
	        

	        updateFog();
	    }
}
