package com.cristiano.java.gm.builder.factory;

import java.io.IOException;
import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.product.factory.ProductFactory;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.gm.units.UnitStorageBuilder;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.Log;
import com.jme3.asset.AssetManager;
import com.jme3.export.Savable;
import com.jme3.export.xml.XMLExporter;

public class BuilderFactory extends ProductFactory {

	private ElementManager elm;
	protected XMLExporter exporter=new XMLExporter();

	public BuilderFactory(IManageElements em, EntityManager entMan, AssetManager assetManager) {
		super(em, entMan,assetManager);
		elm = (ElementManager) em;

		adicionaComponentesMap();
	}

	protected void adicionaComponentesMap() {
		if (elm==null){
			Log.error("ELM is null!");
			return;
		}
		List<IGameElement> components = elm.getElementsWithTag(GameComps.TAG_ALL_COMPONENTS);
		for (IGameElement comp : components) {
			IGameElement el=(IGameElement) comp;
			String classe = getClasseFromComponent(el);
			String ident = el.getProperty(Extras.PROPERTY_IDENTIFIER);
			String pack = ((AbstractElement) el).getParamH(Extras.LIST_DOMAIN, Extras.DOMAIN_PACKAGE, true).replace("'", "");
			addClasse(ident, pack + classe);
		}
	}


	@Override
	public IGameEntity createEntityFromTag(String tag) {
		return createEntityFromTag(tag, null);
	}
	
	@Override
	public void loadComponents(IGameEntity entity, String tag){
		IGameElement ge = elm.pickFinal(tag, null, null);
		if (ge == null) {
			Log.error("tag invalida:" + tag);
			return;
		}
		loadComponents(entity, ge);
	}

	@Override
	public IGameEntity createEntityFromTag(String tag, String props) {
		IGameElement ge = elm.pickFinal(tag, null, props);
		if (ge == null) {
			Log.error("tag invalida:" + tag);
			return null;
		}
		return createEntityFrom(ge);
	}

	@Override
	public IGameEntity createEntityFromIdentifier(String identifier) {
		IGameElement ge = elm.createFinalElement(elm.getElementByIdentifier(identifier));
		if (ge == null) {
			Log.error("tag invalida:" + identifier);
			return null;
		}
		String id = ge.id();
		return createEntityFrom(ge);
	}
	

	@Override
	public UnitStorage createUnitStorage( IManageElements em,EntityManager entMan) {
		return new UnitStorageBuilder(em,entMan.getFactory(),entMan);
	}
	
	
	@Override
	public String exportSavable(String entId, String type,Savable node) {
		if (node==null){
			return null;
		}
		String path = GMAssets.getPathForSavableEntity(entId,type);
		try {
			GMAssets.deleteAsset(path);
			exporter.save(node, GMAssets.getOutputStream(path));
			if (!GMAssets.assetExists(path)){
				Log.fatal("Asset '"+path+"' was not found...");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	@Override
	public String exportSavable(int entId, String type,Savable node) {
		return exportSavable(Integer.toString(entId),type,node);
	}

	public void setElementManager(ElementManager em) {
		this.elm=em;
		this.em=em;
		
	}
	
}
