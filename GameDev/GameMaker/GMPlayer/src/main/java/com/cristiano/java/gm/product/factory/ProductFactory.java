package com.cristiano.java.gm.product.factory;

import java.io.IOException;
import java.io.InputStream;

import com.cristiano.consts.Extras;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.comps.visual.OrientationComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.factory.AbstractGameFactory;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.Log;
import com.jme3.asset.AssetManager;
import com.jme3.export.Savable;
import com.jme3.export.xml.XMLImporter;

public class ProductFactory extends AbstractGameFactory {
	XMLImporter importer = null;

	public ProductFactory(IManageElements em, EntityManager entMan, AssetManager assetManager) {
		super(em, entMan);
		importer = new XMLImporter();
		importer.setAssetManager(assetManager);
	}

	@Override
	public UnitStorage createUnitStorage( IManageElements em,EntityManager entMan) {
		Log.fatal("UnitStorage can only be imported.");
		return null;//new UnitStorage(role, this);
	}

	private void checkPositionComponent(GameEntity entity) {
		RenderComponent renderC = (RenderComponent) entity.getComponentWithIdentifier(GameComps.COMP_RENDER);
		if (renderC != null) {
			PositionComponent posC = (PositionComponent) entity.getComponentWithIdentifier(GameComps.COMP_POSITION);
			if (posC == null) {
				posC = (PositionComponent) entMan.addComponent(GameComps.COMP_POSITION, entity);
			}
			linkPhyscPosition(entity, posC);
			posC.setNode(renderC.node);
		}
	}

	private void checkOrientationComponent(GameEntity entity) {
		RenderComponent renderC = (RenderComponent) entity.getComponentWithIdentifier(GameComps.COMP_RENDER);
		if (renderC != null) {
			OrientationComponent posC = (OrientationComponent) entity.getComponentWithIdentifier(GameComps.COMP_ORIENTATION);
			if (posC == null) {
				posC = (OrientationComponent) entMan.addComponent(GameComps.COMP_ORIENTATION, entity);
			}
			linkPhyscOrientation(entity, posC);
			posC.setNode(renderC.node);
		}
	}

	private void linkPhyscOrientation(GameEntity entity, OrientationComponent posC) {
		PhysicsComponent physcC = (PhysicsComponent) entity.getComponentWithIdentifier(GameComps.COMP_PHYSICS);
		if (physcC != null) {
			posC.physics = physcC;

		}
	}

	private void linkPhyscPosition(GameEntity entity, PositionComponent posC) {
		PhysicsComponent physcC = (PhysicsComponent) entity.getComponentWithIdentifier(GameComps.COMP_PHYSICS);
		if (physcC != null) {
			posC.physics = physcC;

		}
	}

	protected void checkClonedEntity(GameEntity entity) {
		super.checkClonedEntity(entity);
		checkPositionComponent(entity);
		checkOrientationComponent(entity);
	}

	@Override
	public IGameEntity createEntityFrom(IGameElement el) {
		IGameEntity entity = null;
		String init = null;
		String classe = null;
		String pack = null;
		try {
			init = el.getParamAsText(Extras.LIST_DOMAIN, Extras.DOMAIN_CLASS_NAME);
			if (init.equals("")) {
				init = el.getIdentifier();
			}
			pack = el.getParamAsText(Extras.LIST_DOMAIN, Extras.DOMAIN_PACKAGE);
			init = pack + init;
			classe = el.getParamAsText(Extras.LIST_DOMAIN, Extras.DOMAIN_CLASS);
			entity = createEntityFromClass(init, classe);
			entity.loadFromElement(el);
		} catch (Exception e) {
			if (el == null) {
				Log.error("Error instantiating from a null element! " + e);
			} else {
				Log.error("Erro ao instanciar o objeto " + el.getIdentifier() + " error:" + e);
			}
			e.printStackTrace();
		}
		loadComponents(entity, el);
		return entity;
	}

	@Override
	public IGameEntity createEntityFromTag(String tag) {
		Log.throwUnsupported("Trying to use a builder method:createEntityFromTag");
		return null;
	}

	@Override
	public IGameEntity createEntityFromTag(String unitRootTag, String props) {
		Log.throwUnsupported("Trying to use a builder method:createEntityFromTag");
		return null;
	}

	@Override
	public IGameEntity createEntityFromIdentifier(String identifier) {
		Log.throwUnsupported("Trying to use a builder method:createEntityFromIdentifier");
		return null;
	}

	@Override
	public Savable importSavable(String entId, String type) {
		String path = GMAssets.getPathForSavableEntity(entId, type);
		if (importer.getAssetManager() == null) {
			Log.fatal("AssetManager is null! " + this);
		}
		try {
			InputStream inputStream = GMAssets.getInputStream(path);
			if (inputStream==null){
				Log.warn("Asset: " + path + " with type " + type + " is empty when importing");
				return null;
			}
			return importer.load(inputStream);
		} catch (IOException e) {
			//e.printStackTrace();
			Log.warn("Error when importing savable with id: " + entId + "(" + path + ") Erro:" + e.getMessage());
			// ao exportar verificar se o arquivo foi criado
		}
		return null;
	}

	@Override
	public Savable importSavable(int entId, String type) {
		return importSavable(Integer.toString(entId), type);
	}

	@Override
	public void setAssetManager(AssetManager assetManager) {
		importer.setAssetManager(assetManager);
	}

	

}
