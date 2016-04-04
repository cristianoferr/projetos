package com.cristiano.java.gm.ecs.systems.unit.actuators;

import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.unit.actuators.AbstractActuatorComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IRunGame;
import com.cristiano.utils.Log;

public abstract class AbstractActuatorSystem extends JMEAbstractSystem {

	public AbstractActuatorSystem(String comp) {
		super(comp);
	}

	@Override
	public void initSystem(EntityManager entMan, IRunGame game) {
		super.initSystem(entMan, game);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		AbstractActuatorComponent comp = (AbstractActuatorComponent) component;
		if (comp.firstTick) {

			linkSpatial(ent, comp);

			if (comp.addToAction > 0) {
				linkActuatorToAction(comp, comp.addToAction);
			}
		}
	}

	public static void linkSpatial(IGameEntity ent, AbstractActuatorComponent actuator) {
		if (!actuator.spatialComponents.isEmpty()) {
			return;
		}
		if (actuator.spatialIDs.isEmpty()) {
			Log.fatal("Actuator has no spatialID!");
		}
			List<IGameComponent> spatials = ent.getComponentsWithIdentifier(GameComps.COMP_SPATIAL);
			for (int i = 0; i < actuator.spatialIDs.size(); i++) {
				int spatialID = actuator.spatialIDs.get(i);
				for (IGameComponent comp : spatials) {
					if (comp.getSourceID() == spatialID) {
						SpatialComponent spatial=(SpatialComponent) comp;
						actuator.addSpatial(spatial,spatial.position);
					}
				}
			/*if (comp.spatialComp == null) {
				Log.fatal("Spatial wasnt found with id: " + comp.spatialID);
			}*/
		}
	}

	private void linkActuatorToAction(AbstractActuatorComponent comp, int addToAction) {

	}

}
