package com.cristiano.galactic.view;


import com.cristiano.galactic.controller.GameLogic;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.faction.PlayerFaction;
import com.cristiano.galactic.view.models.AbstractItemView;
import com.cristiano.gamelib.interfaces.IJogo;

/*
 * Essa classe serve como Interface entre todos os viewers conectados
 * ao sistema.
 */

public interface IView extends IJogo {
	
	void setGameLogic(GameLogic gameLogic);
	void updateCamera();
	PlayerFaction getPlayerFaction();
	void print(String message,boolean debug);
	ItemsViewManagerAbstract getItemsView();
	void setItemsView(ItemsViewManagerAbstract itemsView);
	/*
	 * This method is to add a link between the visual model and the internal representation
	 * for easy access (like when I click on an object so I know to who it refers to
	 */
	void backLinkVisualItem(Object objSelectable,AbstractItemView itemView,boolean addToScene);
	void removeVisualObject(Item item);
	//This is to determine if the view layer updates the lower layers (for single-threads)
	void setRunGameLogic(boolean flagRun);
	boolean isRunningGameLogic();
	void createSkyBox();
	

}
