package com.cristiano.java.jme.tests.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.macro.BestiaryLibraryComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.gm.units.UnitStorageBuilder;

public class TestUnitStorage extends MockAbstractTest {

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	
	@Test
	public void testUnitStorage() {
		
		UnitClassComponent role = mockUnitRole();
		assertNotNull(role);
		
		UnitStorage storage=new UnitStorageBuilder(em, factory,entMan);
		
		BestiaryLibraryComponent bestiary=mockBestiary(role,storage);
		
		int budget = 100;
		
		IGameEntity existingEntity = storage.getExistingEntity();
		assertNull(existingEntity);
		
		IGameEntity ent=role.requestMasterEntity(0.5f);
		assertNotNull(ent);
		assertTrue(ent.size()>0);
		
		existingEntity = storage.getExistingEntity();
		assertNotNull(existingEntity);
		assertTrue(existingEntity==ent);
		
		JSONObject export=new JSONObject();
		int totalExported = entMan.exportEntities(export);
		assertTrue(totalExported>0);
		List<IGameComponent> comps = ent.getAllComponents();
		for (IGameComponent comp:comps){
			String strID = comp.getId()+"";
			if (comp.exportToJSON()!=null){
				assertTrue("Export doesnt contains: "+comp,export.containsKey(strID));
			}
		}
		
		assertTrue("Export doesnt contains bestiary",export.containsKey(bestiary.getId()+""));
	}


	protected BestiaryLibraryComponent mockBestiary(UnitClassComponent role, UnitStorage storage) {
		BestiaryLibraryComponent bestiary = (BestiaryLibraryComponent) entMan.addComponent(GameComps.COMP_BESTIARY_LIB, entity);
		role.storage=storage;
		bestiary.attachComponent(role);
		return bestiary;
	}
	
}
