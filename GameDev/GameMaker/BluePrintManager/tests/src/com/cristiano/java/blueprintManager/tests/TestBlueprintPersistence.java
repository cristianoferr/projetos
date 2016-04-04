package com.cristiano.java.blueprintManager.tests;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.tests.TestBlueprintOutput;
import com.cristiano.java.product.utils.BPUtils;


public class TestBlueprintPersistence {
	
	
	private static ElementManager em;

	@BeforeClass
	public static void tearUp() throws IOException {
		em = new ElementManager();
		em.loadTestBlueprintsFromFile();
		BPUtils.em=em;
}
	
	@Test
	public void testElementManager() {
		TestBlueprintOutput.validataElementManager(BPUtils.em);
	}
	
	@Test public void testElementJSON() {
		GenericElement ge = new GenericElement(em);
		GenericElement geTo = new GenericElement(em);
		TestBlueprintOutput.validateExportImportJSON(ge, geTo,em);
		
		
	}
	
	@Test public void testIGameElement() {
		IGameElement ge=new GenericElement(em);
		IGameElement gePar=new GenericElement(em);
		
		TestBlueprintOutput.validateIGenericElement(ge, gePar);
	}

	
	

}


