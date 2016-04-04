package com.cristiano.java.gm.interfaces.state;

import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.interfaces.IGameEntity;


/*
 * Essa interface lida com métodos do jogo, responsavel por adicionar e inicializar entidades 
 * */
public interface IGameState extends IGameEntity  {

	void initWithEntityManager(EntityManager entMan);
	EntityManager getEntityManager();

}
