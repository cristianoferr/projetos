package com.cristiano.java.gm.interfaces;

import com.cristiano.data.IAssembleJSON;
import com.cristiano.data.ISerializeJSON;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.interfaces.state.IGameState;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.jme3.asset.AssetManager;
import com.jme3.export.Savable;

public interface IGameFactory extends IAssembleJSON,ISerializeJSON {

	//any environment	
	Object instantiateClass(String className,String objectType);
	Object instantiateClass(IGameElement elLight);
	
	IGameState createStateFrom(IGameElement elState);
	IGameSystem createSystemFrom(IGameElement elSystem);
	IGameComponent createComponentFrom(IGameElement compEl);
	void addPackage(String typeEntity, String entityPackage);
	IGameComponent createComponentFromClass(String compIdent);
	IGameEntity clonaEntidade(IGameEntity molde);
	void addClasse(String compHealth, String string);
	void loadComponents(IGameEntity ent, IGameElement elementSource);
	void loadComponents(IGameEntity ent, String tag);
	IGameEntity createEntityFrom(IGameElement el);
	
	//will create a unitStorage based on the current environment
	UnitStorage createUnitStorage( IManageElements em,EntityManager entMan);
	
	//release only
	IGameEntity restoreEntityFromID(String id);
	Savable importSavable(int entId,String type);
	Savable importSavable(String entId,String type);
	
	//dev only
	IGameEntity createEntityFromTag(String tag);
	IGameEntity createEntityFromTag(String unitRootTag, String props);
	IGameEntity createEntityFromIdentifier(String identifier);//identifier=classe do blueprint
	IManageElements getElementStore(); 
	String exportSavable(String entId,String type, Savable node);
	String exportSavable(int id, String type, Savable node);
	void setAssetManager(AssetManager assetManager);
	boolean okToExport(IGameEntity entity);
}
