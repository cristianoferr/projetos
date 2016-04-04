package com.cristiano.java.gm.ecs.systems;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.mechanics.ApplyForceComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.jme3.math.Vector3f;


public class ApplyForceSystem extends JMEAbstractSystem {

	

	public ApplyForceSystem() {
		super(GameComps.COMP_APPLY_FORCE);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,float tpf) {
		ApplyForceComponent comp=(ApplyForceComponent) component;
		PhysicsComponent physComp=(PhysicsComponent) ent.getComponentWithIdentifier(GameComps.COMP_PHYSICS);
		if (physComp.physNode==null){
			//Log.warn("No physics node defined for entity: "+ent);
			return;
		}
		applyForce(comp, physComp);
		entMan.removeComponentFromEntity(comp, ent);
	}

	public static void applyForce(ApplyForceComponent comp, PhysicsComponent physComp) {
		if (comp.applyForce){
			physComp.physNode.applyImpulse(comp.force, Vector3f.ZERO);
		}
		if (comp.applyVelocity){
			physComp.physNode.setLinearVelocity(comp.force);
		}
	}

	
}

	