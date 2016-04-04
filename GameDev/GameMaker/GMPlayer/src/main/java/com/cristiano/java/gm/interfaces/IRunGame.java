package com.cristiano.java.gm.interfaces;

import com.cristiano.java.gm.interfaces.state.IIntegrationGameState;

public interface IRunGame {

	void initBlueprintIntegration();
	IIntegrationGameState getIntegrationState();
	IGameEntity getGameEntity();
	void setGameEntity(IGameEntity entity);
	void startHeadless();
	String getMacroDefinitionTag();
}
