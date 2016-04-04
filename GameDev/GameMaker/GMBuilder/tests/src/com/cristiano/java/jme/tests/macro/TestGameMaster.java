package com.cristiano.java.jme.tests.macro;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.macro.GameOppositionComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamComponent;
import com.cristiano.java.gm.ecs.systems.map.MapLoaderSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.utils.Log;

public class TestGameMaster extends MockAbstractTest {

	private MapComponent mapC;
	private MapLoaderSystem mapS;
	private MapWorldComponent mapWC;

	@BeforeClass
	public static void setUpTest() throws IOException {
		startHeadless();
	}

	@Test
	public void testTeamsFreeForAll() {
		String type = GameOppositionComponent.TYPE_OPPOSITION_FREEFORALL;
		prepareTestScenario(type);
		validateTeamFromOpposition(mapC, type);
	}

	@Test
	public void testMultiTeams() {
		String type = GameOppositionComponent.TYPE_OPPOSITION_MULTI_TEAMS;
		prepareTestScenario(type);
		validateTeamFromOpposition( mapC,  type);
	}

	@Test
	public void testTeamsPlayerVersus() {
		String type = GameOppositionComponent.TYPE_OPPOSITION_PLAYER_VERSUS;
		prepareTestScenario(type);
		validateTeamFromOpposition( mapC, type);
	}

	private void prepareTestScenario(String type) {
		mapS = initMapLoaderSystem();
		// startGameGenreComponent();
		startBestiaryLibComponent();
		// startUnitRolesComponent();
		startUnitResourcesComponent();

		mapC = startMapComponent();
		mockOpposition(mapC);
		mapS = initMapLoaderSystem();
		mapWC = startMapWorldComponent();

		// IGameElement elOpposition=defineMapOpposition(mapC,type);
		assertNotNull("mapS null", mapS);
		mapC.setStage(MapComponent.LOAD_OPPOSITION);
		mapS.iterateEntity(entity, mapC, 0);
		assertFalse("Map State wrong: " + mapC.getStage(),
				mapC.isOnStage(MapComponent.LOAD_OPPOSITION));
		assertTrue("No opposition defined... should be: " + type,
				mapC.containsComponent(GameComps.COMP_GAME_OPPOSITION));

	}

	private void validateTeamFromOpposition(MapComponent mapC,
			String tipoOpposition) {
		GameOppositionComponent opposC = (GameOppositionComponent) mapC
				.getComponentWithIdentifier(GameComps.COMP_GAME_OPPOSITION);
		if (tipoOpposition
				.equals(GameOppositionComponent.TYPE_OPPOSITION_MULTI_TEAMS)) {
			opposC.minTeams = 3;
			opposC.maxTeams = 5;
		}
		// defineMapOpposition(mapC,tipoOpposition);
		entity.removeComponent(GameComps.COMP_TEAM);
		mapC.setStage(MapComponent.GENERATING_TEAMS);
		mapS.iterateEntity(mapWC,mapC,  0);
		assertFalse("Current State wrong: " + mapC.getStage(),
				mapC.isOnStage(MapComponent.GENERATING_TEAMS));
		List<IGameComponent> teams = mapC
				.getComponentsWithIdentifier(GameComps.COMP_TEAM);
		assertTrue("Teams not generated:" + teams.size(), teams.size() > 0);
		assertNotNull("oppositionComponent is null", opposC);

		for (int i = 0; i < teams.size(); i++) {
			TeamComponent team = (TeamComponent) teams.get(i);
			validateTeamOpposition(i, team, opposC, teams.size(),
					tipoOpposition);
		}
	}

	private void validateTeamOpposition(int index, TeamComponent team,
			GameOppositionComponent opposC, int size, String tipoOpposition) {
		assertNotNull("Team is null", team);
		assertTrue(team.idTeam == index);
		assertTrue(team.isPlayerTeam == (index == 0));
		assertTrue(team.sameTeamRelation == opposC.sameTeamRelation);
		if (size == 1) {
			Log.debug("tipoOpposition:" + tipoOpposition);
			Log.debug("team:" + team);
			assertTrue(
					tipoOpposition
							+ ":: if only one team then all entities should be enemies...",
					team.sameTeamRelation == LogicConsts.RELATION_ENEMY);
		}

		if (tipoOpposition
				.equals(GameOppositionComponent.TYPE_OPPOSITION_PLAYER_VERSUS)) {
			if (index == 0) {
				assertTrue(tipoOpposition
						+ ":: player should be alone on his own team...",
						team.maxUnits == 1);
				assertTrue(
						tipoOpposition
								+ ":: respawn should be active (player must be able to come back)",
						team.hasRespawn);
			} else {
				assertFalse(
						tipoOpposition
								+ ":: respawn should be inactive (npcs dont respawn in this type of game)",
						team.hasRespawn);
			}
		}
		if (tipoOpposition
				.equals(GameOppositionComponent.TYPE_OPPOSITION_MULTI_TEAMS)) {
			assertTrue(tipoOpposition
					+ ":: a multiteam must have more than 2 teams..." + size,
					size > 2);
		}
	}

}
