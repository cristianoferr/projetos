package com.cristiano.java.gm.states;

import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.mechanics.PhysicsSpaceComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.state.IGameState;
import com.cristiano.java.gm.visualizadores.IFinalGame;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;

public class JMEPhysicsSpaceState extends BulletAppState implements IGameState {

	private IGameElement ge;
	private EntityManager entMan;
	private IFinalGame game;
	GameEntity internalEntity = new GameEntity();
	private PhysicsSpaceComponent physSpace;

	@Override
	public void setEntityManager(EntityManager entMan) {
		this.entMan = entMan;
		internalEntity.setEntityManager(entMan);
	}
	
	@Override
	public void free() {
	}

	public JMEPhysicsSpaceState() {
		super();
		// this.setThreadingType(BulletAppState.ThreadingType.PARALLEL);//for
		// bullets
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		game = ((IFinalGame) app);
		physSpace = (PhysicsSpaceComponent) game.getGameEntity().getComponentWithIdentifier(GameComps.COMP_PHYSICS_SPACE);
		if (physSpace == null) {
			Log.error("PhysicsSpace is null!");
		} else {
			physSpace.physics = this;
			physSpace.checkState();
			if (physSpace.gravity==null){
				Log.error("The gravity is null!!");
			} else {
				getPhysicsSpace().setGravity(physSpace.gravity);
			}
		}

	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		if (physSpace==null){
			return;
		}
		int size=physSpace.controlsToAdd.size();
		for (int i=0;i<size;i++) {
			Object control = physSpace.controlsToAdd.get(i);
			getPhysicsSpace().add(control);
		}
		physSpace.controlsToAdd.clear();
	}

	@Override
	public IGameElement getElement() {
		return ge;
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		this.ge = ge;

	}

	@Override
	public void initWithEntityManager(EntityManager entMan) {
		this.entMan = entMan;
	}


	@Override
	public EntityManager getEntityManager() {
		return entMan;
	}

	@Override
	public void attachComponent(IGameComponent comp) {
		internalEntity.attachComponent(comp);

	}

	@Override
	public boolean containsComponent(String compIdent) {
		return internalEntity.containsComponent(compIdent);
	}

	@Override
	public boolean containsComponent(IGameComponent comp) {
		return internalEntity.containsComponent(comp);
	}

	@Override
	public IGameComponent getComponentWithTag(String ident) {
		return internalEntity.getComponentWithTag(ident);
	}

	@Override
	public void removeComponent(IGameComponent comp) {
		internalEntity.removeComponent(comp);
	}

	@Override
	public void removeComponent(String comp) {
		internalEntity.removeComponent(comp);
	}

	@Override
	public int countsComponent(String ident) {
		return internalEntity.countsComponent(ident);
	}

	@Override
	public List<IGameComponent> getComponents(String compClass) {
		return internalEntity.getComponents(compClass);
	}

	@Override
	public List<IGameComponent> getComponents(String compClass, List<IGameComponent> ret) {
		return internalEntity.getComponents(compClass,ret);
	}
	
	@Override
	public int getId() {
		return internalEntity.getId();
	}

	@Override
	public void removeAllComponents() {
		internalEntity.removeAllComponents();
	}

	@Override
	public boolean hasTag(String compIdent) {
		return internalEntity.hasTag(compIdent);
	}

	@Override
	public void deactivate() {
		Log.info("Deactivating physics...");
		setSpeed(0);
	}

	@Override
	public void activate() {
		Log.info("Activating physics...");
		setSpeed(1);
	}

	@Override
	public boolean isActive() {
		return getSpeed()==1;
	}

	@Override
	public void resetComponents() {
		internalEntity.resetComponents();
	}

	@Override
	public void attachComponents(List<IGameComponent> components) {
		internalEntity.attachComponents(components);

	}

	@Override
	public void setProperty(String prop, String value) {
		internalEntity.setProperty(prop, value);
	}

	@Override
	public void setProperty(String prop, int value) {
		internalEntity.setProperty(prop, value);
	}

	@Override
	public void setProperty(String prop, float value) {
		internalEntity.setProperty(prop, value);
	}

	@Override
	public JSONObject exportToJSON() {
		return null;
	}

	@Override
	public void importFromJSON(JSONObject json) {
	}

	@Override
	public int size() {
		return internalEntity.size();
	}
	
	@Override
	public IGameComponent getComponentWithIdentifier(String compClass) {
		return internalEntity.getComponentWithIdentifier(compClass);
	}
	
	@Override
	public List<IGameComponent> getComponentsWithIdentifier(String compIdent,
			List<IGameComponent> ret) {
		return internalEntity.getComponentsWithIdentifier(compIdent,ret);
	}
	
	@Override
	public List<IGameComponent> getComponentsWithIdentifier(String compIdent) {
		return internalEntity.getComponentsWithIdentifier(compIdent);
	}
	
	@Override
	public boolean containsComponentWithTag(String compClass) {
		return internalEntity.containsComponentWithTag(compClass);
	}
	
	@Override
	public boolean isComponent() {
		return internalEntity.isComponent();
	}
	
	@Override
	public void setListed() {
		internalEntity.setListed();
		
	}

	@Override
	public void setUnlisted() {
		internalEntity.setUnlisted();
		
	}

	@Override
	public boolean isListed() {
		return internalEntity.isListed();
	}
	
	@Override
	public void removeComponentSimple(IGameComponent comp) {
		internalEntity.removeComponentSimple(comp);
	}
	
	@Override
	public void report(List<IGameEntity> alreadyShown,String spaces) {
		internalEntity.report(alreadyShown,spaces);
	}
	
	@Override
	public List<IGameComponent> getAllComponents() {
		return internalEntity.getAllComponents();
	}
	
	@Override
	public List<IGameComponent> getAllComponents(List<IGameComponent> ret) {
		return internalEntity.getAllComponents(ret);
	}
}
