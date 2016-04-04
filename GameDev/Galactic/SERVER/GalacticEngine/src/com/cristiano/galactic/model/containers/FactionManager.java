package com.cristiano.galactic.model.containers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.cristiano.galactic.model.faction.AbstractFaction;
import com.cristiano.galactic.model.faction.NPCFaction;
import com.cristiano.galactic.model.faction.PlayerFaction;

/**
 * Essa classe vai gerenciar as facções e suas relações
 * @author cmm4
 *
 */
public class FactionManager {
	HashMap<Integer,AbstractFaction> factions;
	Vector<PlayerFaction> players;
	int count;
	
	public FactionManager(){
		factions=new HashMap<Integer,AbstractFaction>();
		players=new Vector<PlayerFaction>();
		count=0;
	}
	
	public void addFaction(AbstractFaction faction){
		factions.put(faction.getId(),faction);
		if (faction.getId()>=count)count=faction.getId()+1;
	}
	
	public AbstractFaction getFactionByID(int id){
		return factions.get(id);
	}

	public Vector<AbstractFaction> getAllFactions() {
    	Iterator iterator = factions.keySet().iterator();
    	Vector<AbstractFaction> vec=new Vector<AbstractFaction>();
        while (iterator.hasNext()) {
          Integer key = (Integer) iterator.next();
          AbstractFaction f=factions.get(key);
          vec.add(f);
        }
		
		return vec;
	}

	public void addPlayer(String name) {
		PlayerFaction player=new PlayerFaction(count,name);
		addPlayer(player);
	}
	
	public void addPlayer(PlayerFaction player) {
		addFaction(player);
		players.add(player);
	}
	
	public void addNPCFaction(String name) {
		NPCFaction player=new NPCFaction(count,name);
		addFaction(player);
		
	}

	public PlayerFaction getPlayerID(int id) {
		for (int i=0;i<players.size();i++){
			if (players.get(i).getId()==id)return players.get(i);
		}
		return null;
	}
	
}
