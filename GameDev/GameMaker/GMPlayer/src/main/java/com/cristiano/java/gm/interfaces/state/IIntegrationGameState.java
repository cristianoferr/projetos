package com.cristiano.java.gm.interfaces.state;

import com.cristiano.data.ISerializeJSON;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.jme3.app.state.AppState;


public interface IIntegrationGameState extends IGameState,AppState,ISerializeJSON {
	void loadGameStates();
	void loadGameSystems();

	IManageElements getElementManager();
	IGameFactory getFactory();

	void initObjects();

	IGameElement getWorldElement();
	boolean writeState();
	
	
	
	
}
