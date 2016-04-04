package com.cristiano.java.gm.interfaces;

import java.util.List;

import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.product.IGameElement;

public interface IGameSystem extends IGameEntity{

	void initSystem(EntityManager entMan,IRunGame game);
	void update(float tpf);
	void iterateEntity(IGameEntity ent, IGameComponent comp, float tpf);
	boolean hasIdentifier(String identifier);
	boolean canRunOnRelease();
	boolean canRunOnDev();
	void spawnComponentsFromList(List<IGameElement> from, List<IGameComponent> to);
}
