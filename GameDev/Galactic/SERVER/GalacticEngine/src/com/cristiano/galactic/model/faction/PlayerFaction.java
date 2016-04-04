package com.cristiano.galactic.model.faction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.FactionType;

/**
 * Essa classe vai gerenciar a facção do jogador.
 * @author cmm4
 *
 */
public class PlayerFaction extends AbstractFaction{
	ArtificialEntity currentPlayerShip=null;
	
	public PlayerFaction(int id, String name) {
		super(id, name,FactionType.PLAYER);
	}
	
	public Element buildXML(Document testDoc) {
		
		 Element element = super.buildXML(testDoc);
		 if (currentPlayerShip!=null){
			 element.setAttribute("currentshipid", Integer.toString(currentPlayerShip.getId()));
		 }
//		 element.setAttribute("name", getName());
//		 element.setAttribute("type", getType());
		 
		return element;
	
}

	public ArtificialEntity getCurrentPlayerShip() {
		if (currentPlayerShip==null){
			for (int i=0;i<ownerShip.size();i++){
				if (ownerShip.get(i).getType().equals(EntityType.ET_SHIP.toString())){
					currentPlayerShip=(ArtificialEntity) ownerShip.get(i);
					return currentPlayerShip;
				}
			}
			
		}
		return currentPlayerShip;
	}
	
	public int getPlayerShipID() {
		if (getCurrentPlayerShip()!=null){
			return getCurrentPlayerShip().getId();
		}
		
		return -1;
	}

	public void setCurrentPlayerShip(ArtificialEntity currentPlayerShip) {
		this.currentPlayerShip = currentPlayerShip;
	}


	
	public static PlayerFaction createFromXML(int id, String name, Node node,
			DataManager dataManager) {
		return new PlayerFaction(id,name);
	}


}
