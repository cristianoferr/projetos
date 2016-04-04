package com.cristiano.java.jme.tests.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.simple.JSONObject;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.misc.TestComponent1;
import com.cristiano.java.gm.ecs.comps.unit.AIComponent;
import com.cristiano.java.product.IGameElement;

public class TestImportExportJSON extends MockAbstractTest {

	@Test
	public void testComponentsJson() {
		AIComponent aicomp=(AIComponent) entMan.addComponent(GameComps.COMP_AI, entity);
		mockAIElement(entity, aicomp);
		//aicomp.startableStates=new String[]{"abc","cde"};
		JSONObject json = aicomp.exportToJSON();
		AIComponent aicomp2=(AIComponent) entMan.addComponent(GameComps.COMP_AI, entity);
		aicomp2.importFromJSON(json);
		assertTrue(aicomp.startableStates[0].equals(aicomp2.startableStates[0]));
		//assertTrue(aicomp.startableStates[1].equals(aicomp2.startableStates[1]));
		assertFalse(aicomp.startableStates==aicomp2.startableStates);
		
		entMan.removeEntity(aicomp2);
	}
	
	@Test
	public void testAssemblingGameElement() {
		IGameElement ge = em.createElement();
		int val1=1234;
		String prop1 = "prop1";
		ge.setProperty(prop1, val1);
		String val2="val qualquer";
		String prop2 = "prop2";
		ge.setProperty(prop2, val2);
		float val3=123.12198f;
		String prop3 = "prop3";
		ge.setProperty(prop3, val3);
		
		JSONObject json= ge.exportToJSON();
		
		Object obj=factory.assembleJSON(json);
		assertNotNull("Object is null",obj);
		assertTrue("Wrong class:"+obj.getClass().getSimpleName(),obj instanceof IGameElement);
		IGameElement copy=(IGameElement) obj;
		assertTrue(copy.getPropertyAsInt(prop1)==val1);
		assertTrue(copy.getProperty(prop2).equals(val2));
		assertTrue(copy.getPropertyAsFloat(prop3)==val3);
		
		//entMan.removeEntity(copy);
	}
	
	@Test
	public void testAssemblingComponent() {
		TestComponent1 testComp = new TestComponent1();
		testComp.valFloat=1235.45f;
		
		testComp.setEntityManager(entMan);
		testComp.valString="string qualquer";
		
		TestComponent1 testSon1 = new TestComponent1();
		testSon1.valFloat=894563;
		testSon1.setEntityManager(entMan);
		testSon1.valString="filho";
		
		testComp.attachComponent(testSon1);
		
		JSONObject json= testComp.exportToJSON();
		
		Object obj=factory.assembleJSON(json);
		assertNotNull("Object is null",obj);
		assertTrue("Wrong class:"+obj.getClass().getSimpleName(),obj instanceof TestComponent1);
		TestComponent1 copy=(TestComponent1) obj;
		assertTrue(testComp.valFloat==copy.valFloat);
		assertTrue(testComp!=copy);
		assertTrue(testComp.valString.equals(copy.valString));
		assertTrue("ids should be the same...",testComp.getId()==(copy.getId()));
		
		TestComponent1 copy2=(TestComponent1) factory.assembleJSON(json);
		assertTrue(copy2==copy);
		
		TestComponent1 testSonCopy = (TestComponent1) copy.getComponentWithIdentifier(GameComps.COMP_TEST);
		assertNotNull(testSonCopy);
		assertTrue(testSonCopy!=testSon1);
		assertTrue(testSon1.getId()==testSonCopy.getId());
		assertTrue(testSon1.valFloat==testSonCopy.valFloat);
		assertTrue(testSon1.valString.equals(testSonCopy.valString));
		
		//Log.debug("s1:"+testComp.getId()+" testSonCopy:"+copy.getId());
		
		entMan.removeEntity(copy);
	}

}
