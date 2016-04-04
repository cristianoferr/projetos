package com.cristiano.java.gm.ecs.comps.unit.resources;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.ui.HealthIndicator;

public class HealthComponent extends ResourceComponent {

	private static final String LOW_HEALTH_THRESHOLD = "lowHealthThreshold";
	public boolean alive = true;
	public float lowHealthThreshold;

	public HealthIndicator healthBillboard;

	public HealthComponent() {
		super(GameComps.COMP_RESOURCE_HEALTH);

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		loadData(ge);
	}

	private void loadData(IGameElement ge) {
		this.lowHealthThreshold = ge.getPropertyAsFloat(LOW_HEALTH_THRESHOLD);
	}

	@Override
	public IGameComponent clonaComponent() {
		HealthComponent ret = new HealthComponent();
		ret.alive = alive;
		ret.lowHealthThreshold = lowHealthThreshold;
		finishClone(ret);
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	public void setLevel(float value) {
		if (healthBillboard != null) {
			healthBillboard.setLevel(value);
		}
	}
}
