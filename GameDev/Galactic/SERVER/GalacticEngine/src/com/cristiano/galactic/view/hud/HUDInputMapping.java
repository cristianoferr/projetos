package com.cristiano.galactic.view.hud;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;


public class HUDInputMapping implements NiftyInputMapping {

	
	
	  /**
	   * convert the given KeyboardInputEvent into a neutralized NiftyInputEvent.
	   * @param inputEvent input event
	   * @return NiftInputEvent
	   */
	  public NiftyInputEvent convert(final KeyboardInputEvent inputEvent) {
	    if (inputEvent.isKeyDown()) {
	    	//System.out.println("key:"+inputEvent.getKey());
	      if (inputEvent.getKey() == KeyboardInputEvent.KEY_F1) {
	        return NiftyInputEvent.ConsoleToggle;
	    /*  } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RETURN) {
	        return NiftyInputEvent.Activate;
	      /*} else if (inputEvent.getKey() == KeyboardInputEvent.KEY_SPACE) {
	        return NiftyInputEvent.Activate;*/
	      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_TAB) {
	        if (inputEvent.isShiftDown()) {
	          return NiftyInputEvent.PrevInputElement;
	        } else {
	          return NiftyInputEvent.NextInputElement;
	        }
	      }
	    }
	    return null;
	  }
}
