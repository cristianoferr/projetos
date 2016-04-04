package com.cristiano.java.gm.builder.utils;

import java.util.List;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.IConsole;
import com.cristiano.java.product.IManageElements;

public class ConsoleParserBuilder implements IConsole {

	private static final String CMD_WALL = "wall";

	public void console(String auxInfo, EntityManager entMan, IManageElements em) {
		String cmd = auxInfo;
		String[] pars = null;
		if (cmd.contains("(")) {
			cmd = auxInfo.substring(0, auxInfo.indexOf("("));
			String params = auxInfo.substring(auxInfo.indexOf("(") + 1, auxInfo.length() - 1);
			pars = params.split(",");
		}

		if (cmd.equals(CMD_WALL)) {
			addWall(entMan, pars, em);
		}

		if (cmd.equals("removeRender")) {
			List<IGameEntity> ents = entMan.getEntitiesWithComponent(GameComps.COMP_SPATIAL);
			for (IGameEntity ent : ents) {
				List<IGameComponent> comps = ent.getComponents(GameComps.COMP_SPATIAL);
				for (IGameComponent comp : comps) {
					SpatialComponent spatial = (SpatialComponent) comp;
					spatial.spatial().removeFromParent();
				}
			}

		}

	}

	private static void addWall(EntityManager entMan, String[] pars, IManageElements em) {
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(GameComps.COMP_MAP);
		IGameEntity ent = ents.get(0).getComponentWithIdentifier(GameComps.COMP_MAP);
		float w = Float.parseFloat(pars[0]);
		float h = Float.parseFloat(pars[1]);
		float d = Float.parseFloat(pars[2]);
		float x = Float.parseFloat(pars[3]);
		float y = Float.parseFloat(pars[4]);
		float z = Float.parseFloat(pars[5]);

		BuilderUtils.addWall(entMan, (ElementManager) em, ent, w, h, d, x, y, z);
	}

}
