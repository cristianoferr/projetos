package com.cristiano.java.gm.ecs.systems.unit;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.utils.Log;


/*
 * TestPlayerSystem
 * */
public class PlayerSystem extends JMEAbstractSystem {
	public boolean hasJoystick = false; // to avoid multiple registrations...

	public PlayerSystem() {
		super(GameComps.COMP_PLAYER);

	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		PlayerComponent comp = (PlayerComponent) component;
		
		//TODO: remover
		if (!ent.containsComponent(GameComps.COMP_RESOURCE_HEALTH)){
			Log.fatal("Player has no health");
		}
		
		if (comp.isFirstTick()) {
			PhysicsComponent physC=ECS.getPhysicsComponent(ent);
			if (physC==null){
				return;
			}
			if (physC.physNode==null){
				return;
			}
			attachCamera(ent);
			attachJoystick(ent,comp);
			comp.setActionController(physC.controlBody.getActionController());
			
			comp.firstTick = false;
		}
		
	}


	private void attachJoystick(IGameEntity ent, PlayerComponent playerComp) {
		IGameComponent comp = entMan.addIfNotExistsComponent(GameComps.COMP_JOYSTICKS, ent);
		comp.loadFromElement(getElementManager().pickFinal(GameComps.COMP_JOYSTICKS));
	}

	private void attachCamera(IGameEntity ent) {
		CamComponent camC=extractCamComponent(ent);
		camC.firstTick=true;
	}

	

	

}
