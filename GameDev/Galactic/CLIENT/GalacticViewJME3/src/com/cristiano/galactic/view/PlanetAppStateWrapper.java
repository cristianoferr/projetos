package com.cristiano.galactic.view;


/**
 * Essa classe � necess�ria pois h� diferen�as na implementa��o do c�lculo das distancias.
 * Principalmente porque o jogador est� sempre na posi��o 0,0,0 e porque o node do planeta que � atualizado a posi��o.
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
