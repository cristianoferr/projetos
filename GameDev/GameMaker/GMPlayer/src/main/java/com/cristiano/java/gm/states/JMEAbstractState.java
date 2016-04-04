package com.cristiano.java.gm.states;

import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.state.IGameState;
import com.cristiano.java.gm.visualizadores.IFinalGame;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IRunJMEGame;
import com.cristiano.jme3.utils.JMESnippets;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public abstract class JMEAbstractState  extends AbstractAppState  implements IGameState{ 

	protected IFinalGame game;
	protected Camera cam;
	protected Node rootNode;
	protected AssetManager assetManager;
	protected JMESnippets snippets;
	protected EntityManager entMan;
	private IGameElement element;
	private String classe;
	GameEntity internalEntity=new GameEntity();
	
	@Override
	public void update(float tpf) {
		
	}

	@Override
	public void cleanup() {
	}
	@Override
	public void free() {
	}
	
	public void setEntityManager(EntityManager entMan){
		this.entMan=entMan;
		internalEntity.setEntityManager(entMan);
	}
	
	
	public void initWithEntityManager(EntityManager entMan){
		this.entMan=entMan;
	}
	
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.initializeState( stateManager,  (IRunJMEGame) app);
	}
	
	public void initializeState(AppStateManager stateManager, IRunJMEGame app) {
		this.game = (IFinalGame) app;
		this.cam = this.game.getCamera();
		this.rootNode = this.game.getRootNode();
		this.snippets=this.game.getSnippets();
		this.assetManager = this.game.getAssetManager();
	}
	
	
	
	@Override
	public void loadFromElement(IGameElement ge) {
		this.element=ge;
		this.classe=ge.getParamAsText(Extras.LIST_DOMAIN, Extras.DOMAIN_CLASS);;
	}

	
	
	@Override
	public IGameElement getElement() {
		return element;
	}

	
	
	@Override
	public EntityManager getEntityManager() {
		return entMan;
	}
	

	public String toString(){
		return "State:"+classe;
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
		
	}

	@Override
	public void activate() {
		
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public void attachComponents(List<IGameComponent> components) {
		internalEntity.attachComponents(components);
		
	}
	
	@Override
	public void setProperty(String prop, String value) {
		internalEntity.setProperty(prop,value);
	}

	@Override
	public void setProperty(String prop, int value) {
		internalEntity.setProperty(prop,value);
	}

	@Override
	public void setProperty(String prop, float value) {
		internalEntity.setProperty(prop,value);
	}
	
	@Override
	public List<IGameComponent> getComponents(String compClass, List<IGameComponent> ret) {
		return internalEntity.getComponents(compClass,ret);
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

