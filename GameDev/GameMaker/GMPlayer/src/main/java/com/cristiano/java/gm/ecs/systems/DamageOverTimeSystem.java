package com.cristiano.java.gm.ecs.systems;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.effects.DamageEffectComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.DamageOverTimeComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.DamageReceivedComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.utils.Log;

public class DamageOverTimeSystem extends AbstractSystem {

	public DamageOverTimeSystem() {
		super(GameComps.COMP_DAMAGE_OVER_TIME);

	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent comp, float tpf) {
		DamageOverTimeComponent dot = (DamageOverTimeComponent) comp;

		// Se a entidade n�o possui health ent�o n�o adianta processar dano
		if (!ent.containsComponent(GameComps.COMP_RESOURCE_HEALTH)) {
			dot.damageLeft = 0;
		}

		float damageToApply = dot.damageSecond * tpf;
		if (damageToApply > dot.damageLeft) {
			damageToApply = dot.damageLeft;
		}
		dot.damageLeft -= damageToApply;

		if (damageToApply > 0) {
			
			if (!ent.containsComponent(GameComps.COMP_DMG_EFFECT)) {
				addDmgEffect(ent,dot.damageType);
			}
			
			addDmgReceived(ent,dot.damageType,damageToApply, dot.sentBy);
		}

		if (dot.damageLeft <= 0) {
			ent.removeComponent(dot);
			entMan.removeComponentFromEntity(dot,ent);
			entMan.removeComponentsFromEntity(GameComps.COMP_DMG_EFFECT, ent);
		}

	}

	private void addDmgReceived(IGameEntity ent, String damageType,
			float damageToApply, IGameEntity sentBy) {
		DamageReceivedComponent dmg =(DamageReceivedComponent) entMan.addComponent(GameComps.COMP_DAMAGE_RECEIVED, ent);
		dmg.damageType=damageType;
		dmg.damage=damageToApply;
		dmg.sentBy=sentBy;
		dmg.applied=false;
		Log.debug("Enviando dano:"+damageToApply);
		
	}

	private void addDmgEffect(IGameEntity ent, String damageType) {
		DamageEffectComponent dmg = (DamageEffectComponent) entMan.addComponent(GameComps.COMP_DMG_EFFECT, ent);
		dmg.damageType=damageType;
		
	}

}
