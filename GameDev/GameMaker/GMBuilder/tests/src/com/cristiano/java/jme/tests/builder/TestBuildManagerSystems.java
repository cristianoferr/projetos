package com.cristiano.java.jme.tests.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.ecs.comps.macro.Resolution;
import com.cristiano.java.gm.ecs.comps.macro.ScreenShotOrganizer;
import com.cristiano.java.gm.ecs.comps.macro.ScreenShotSituation;
import com.jme3.math.Vector3f;

public class TestBuildManagerSystems extends MockAbstractTest{

	
	@Test
	public void testScreenShotOrganizer() {
		ScreenShotOrganizer org=new ScreenShotOrganizer();
		int res0 = 10;
		int res1 = 20;
		Resolution r0=org.addResolution("res0", res0, res0);
		Resolution r1=org.addResolution("res1", res1, res1);
		
		ScreenShotSituation s0 = org.addSituation("screen0", Vector3f.ZERO);
		ScreenShotSituation s1 = org.addSituation("screen1", Vector3f.UNIT_XYZ);
		
		assertTrue(org.size()==4);
		//Posicao
		assertTrue(org.pos()==0);
		
		assertTrue(org.getResolution()==r0);
		assertTrue(org.getSituation()==s0);
		
		assertTrue(org.next());
		assertTrue(org.pos()==1);
		
		assertTrue(org.getResolution()==r0);
		assertTrue(org.getSituation()==s1);
		
		assertTrue(org.next());
		assertTrue(org.pos()==2);
		
		assertTrue(org.getResolution()==r1);
		assertTrue(org.getSituation()==s0);
		
		assertTrue(org.next());
		assertTrue(org.pos()==3);
		assertFalse(org.next());
		assertTrue(org.pos()==3);
		org.rewind();
		assertTrue(org.pos()==0);
	}


}
