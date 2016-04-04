package com.cristiano.galactic.view.hud;

import com.cristiano.galactic.view.IView;

import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;

public interface IShipHud {

	void setHudController(HUDController hudController);
	void sendConsoleCommand(ConsoleExecuteCommandEvent command);
	void update(IView view);
	IView getView();
}
