package com.cristiano.galactic.model.faction;

import org.w3c.dom.Node;

import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.FactionType;

public class NPCFaction extends AbstractFaction {

	public NPCFaction(int id, String name) {
		super(id, name,FactionType.NPC_CORP);
	}

	public static NPCFaction createFromXML(int id, String name, Node node,
			DataManager dataManager) {
		return new NPCFaction(id,name);
	}

}
