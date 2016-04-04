package com.cristiano.java.gm.ecs.systems.unit.resources;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.DeathComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.comps.visual.BillboardComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.jme3.ui.HealthIndicator;
import com.cristiano.utils.Log;


/*
 * Unit Test: TestEntitySystem
 * */
public class HealthSystem extends JMEAbstractSystem {
	public final static String HEALTH_BILL_BOARD = "healthBillBoard";

	public HealthSystem() {
		super(GameComps.COMP_RESOURCE_HEALTH);

	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		HealthComponent health = (HealthComponent) component;
		if (!health.alive) {
			return;
		}
		if (ent.containsComponent(GameComps.COMP_MASTER)){
			return;
		}
		if (health.getMaxValue() == 0) {
			Log.warn("Health MaxValue is 0. Owner is:"+ent);
			return;
		}
		checkIfItsAlive(ent, health);

		updateAndCheckBillBoard(ent, health);

		checkLowHealthStatus(ent, health);

	}

	private void updateAndCheckBillBoard(IGameEntity ent, HealthComponent health) {

		if (health.healthBillboard == null) {
			BillboardComponent billC = ECS.getBillboardComponent(ent);
			if (billC == null) {
				Log.warn("Entity has no billboard component, creating one...");
				entMan.addComponent(GameComps.COMP_BILLBOARD, ent);
				return;
			} else {
				health.healthBillboard = createHealthIndicator(ent);
				billC.addBillboard(HEALTH_BILL_BOARD, health.healthBillboard);
			}
		}
		/*
		 * health.healthBillboard.getSpatial().removeFromParent();
		 * health.healthBillboard=null;
		 */

		float value = health.getPercValue();
		if (value < 0) {
			Log.warn("Health perc < 0:" + value);
			value = 0;
		}
		if (value > 1) {
			Log.warn("Health perc > 1:" + value);
			value = 1;
		}
		health.setLevel(value);

	}

	private HealthIndicator createHealthIndicator(IGameEntity ent) {

		HealthIndicator health = new HealthIndicator(game.getAssetManager());
		health.setRelativePosition(0, 3, 0);
		return health;
	}

	private void checkIfItsAlive(IGameEntity ent, HealthComponent health) {
		if (health.getCurrValue() <= 0) {
			health.alive = false;
			DeathComponent deathComponent = new DeathComponent();
			// TODO:uncoment this
			// entMan.addComponent(deathComponent, ent);
		}
	}

	private void checkLowHealthStatus(IGameEntity ent, HealthComponent health) {
		if (health.getPercValue() < health.lowHealthThreshold) {
			addStatus(ent, GameComps.COMP_NPC_STATUS_LOW_HEALTH);
		} else {
			removeStatus(ent, GameComps.COMP_NPC_STATUS_LOW_HEALTH);
		}
	}

}
