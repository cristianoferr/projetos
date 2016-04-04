package com.cristiano.java.jme.tests.unit;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.systems.unit.PlayerSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;

public class TestPlayerSystem extends MockAbstractTest {
	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}

	@Test
	public void testPlayerSystem() {
		PlayerSystem playerS = initPlayerSystem();
		IGameEntity ent = mockPlayer();

		PlayerComponent player = (PlayerComponent) ent.getComponentWithIdentifier(GameComps.COMP_PLAYER);
		playerS.iterateEntity(ent, player, 0);
	}

}
