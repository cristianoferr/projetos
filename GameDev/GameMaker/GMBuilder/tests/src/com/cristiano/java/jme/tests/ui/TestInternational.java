package com.cristiano.java.jme.tests.ui;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.json.simple.JSONObject;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.persists.InternationalComponent;

public class TestInternational extends MockAbstractTest{
	

	
	@Test
	public void testInternationalComponent() {
		InternationalComponent interC= startInternationalComponent();
		validateInternationalComponent(interC);
		String key = "chave";
		String valor = "valor";
		interC.addText(key,valor);
		String ret=interC.translate("#"+key);
		assertTrue(ret+"<>"+valor,ret.equals(valor));
		
		JSONObject json = interC.exportToJSON();
		
		InternationalComponent interImport=(InternationalComponent) entMan.spawnComponent(GameComps.COMP_INTERNATIONAL);
		interImport.importFromJSON(json);
		validateInternationalComponent(interImport);
		ret=interImport.translate("#"+key);
		assertTrue(ret+"<>"+valor,ret.equals(valor));
		
	}

	private void validateInternationalComponent(InternationalComponent interC) {
		assertTrue(interC.currentLanguage.equals("en"));
		String key="testText";
		String valEN="Testing";
		String valPT="Testando";
		String test1=interC.getValue(key);
		assertTrue(test1.equals(valEN));
		interC.currentLanguage="ptbr";
		test1=interC.getValue(key);
		assertTrue(test1.equals(valPT));
		
		test1=interC.getValue("inexiste");
		assertNull(test1);
		
		interC.currentLanguage="en";
		
		String val1=interC.translate("#"+key);
		assertTrue(val1.equals(valEN));
		String chaveErrada = "#"+key+"not";
		
		String val2=interC.translate(chaveErrada);
		assertTrue(val2.equals(chaveErrada));
		
		String val3=interC.translate(key);
		assertTrue(val3.equals(val3));
	}
	
	
}
