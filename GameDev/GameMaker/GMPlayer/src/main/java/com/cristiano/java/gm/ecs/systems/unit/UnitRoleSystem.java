package com.cristiano.java.gm.ecs.systems.unit;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.systems.AbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.utils.Log;


public class UnitRoleSystem extends AbstractSystem {

	public UnitRoleSystem() {
		super(GameComps.COMP_UNIT_CLASS);
		
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,float tpf) {
		if (component.isFirstTick()){
			UnitClassComponent comp=(UnitClassComponent) component;
			comp.firstTick=false;
			ReuseManagerComponent reuseC = initReuseComponent();
			initBullet(ent, comp, reuseC);
		}
	}

	private void initBullet(IGameEntity ent, UnitClassComponent unitClass, ReuseManagerComponent reuseC) {
		if (unitClass.roleIdentifier==null){
			return;
		}
		//Generic dont have bullets...
		if (unitClass.isGeneric){
			return;
		}
		if (reuseC.requestEntityFromElement(unitClass.bulletID)==null){
			Log.debug("Spawning new Bullet entity for: "+unitClass.roleIdentifier);
			createBulletEntity(ent,unitClass,reuseC);
		}
	}

	private void createBulletEntity(IGameEntity ent, UnitClassComponent comp, ReuseManagerComponent reuseC) {
		IGameEntity bulletEnt;
		if (comp.bulletID==null){
			Log.warn("Bullet id is null for unitclass:"+comp.getElement());
			return;
		}
		if (comp.bulletElement==null){
			Log.fatal("BulletElement is null for unitclass!!!");
		}
		/*if (CRJavaUtils.IS_RELEASE){
			bulletEnt= entMan.getFactory().restoreEntityFromID(comp.bulletID);
		} else {
			if (comp.bulletElement==null){
				Log.fatal("Element bullet role is null for unitclass!!!");
			}
		}*/
		bulletEnt= entMan.getFactory().createEntityFrom(comp.bulletElement);
		reuseC.addEntityFromElement(comp.bulletID,bulletEnt);
		
	}
	
	
}
