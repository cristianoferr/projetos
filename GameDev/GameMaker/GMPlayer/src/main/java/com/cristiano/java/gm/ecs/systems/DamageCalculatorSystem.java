package com.cristiano.java.gm.ecs.systems;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.mechanics.DamageReceivedComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.utils.Log;

/*Esse sistema checa componentes com dano para ver se h� algum para aplicar na entidade atual*/
public class DamageCalculatorSystem extends AbstractSystem{

	

	public DamageCalculatorSystem() {
		super(GameComps.COMP_DAMAGE_RECEIVED);
		
	}
	
	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,float tpf) {
		HealthComponent healthComp = (HealthComponent) ent.getComponentWithIdentifier(GameComps.COMP_RESOURCE_HEALTH);
		DamageReceivedComponent damageComp = (DamageReceivedComponent) component;
		
		//S� aplico se tiver Health
		
		if (healthComp!=null){
			healthComp.addValue(- damageComp.damage);
		}
		
		Log.debug("Aplicando dano:"+damageComp.damage);
		damageComp.damage=0;
		entMan.removeComponentFromEntity(damageComp, ent);
	}


	
}
