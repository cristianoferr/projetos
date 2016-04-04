/*
 * Copyright (c) 2005-2007 jME Physics 2
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of 'jME Physics 2' nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.cristiano.galactic.controller.handlers;

import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Utils.ItemFactory;
import com.cristiano.galactic.controller.GameLogic;
import com.cristiano.galactic.controller.PhysicsLogicCyclone.ItemHandler;
import com.cristiano.galactic.model.Entity.Abstract.Item;


/**
 * Handles creation of actors.
 * @author Irrisor
 */
public abstract class BasicHandler implements ItemHandler {
    private GameLogic gameLogic;
    private PhysicsItem itemPhysics;
    private Item item;

    public BasicHandler( GameLogic gameLogic ) {
        this.gameLogic = gameLogic;
    }

    public PhysicsItem createPhysicsFor( Item item ) {
    //	System.out.println(" createPhysicsFor: "+item+" old item:"+this.item);
    	this.item=item;
        // create dynamic physics node for the item
        itemPhysics = ItemFactory.createPhysicsItem(item);
     //   itemPhysics.addForce(new Vector3(0,0,0));
        
        itemPhysics.addRotation(new Vector3(0,0,.0001));
        return itemPhysics;
    }
    public PhysicsItem getRigidBody(){    
    	return itemPhysics;
    }

   /* public void activateGravity(){
        //Callback for gravity between bodies
        gameLogic.getPhysicsSpace().addToUpdateCallbacks(
                    new PointGravityCallback(getItemPhysics()));

    }*/
	public GameLogic getGameLogic() {
		return gameLogic;
	}

	public PhysicsItem getItemPhysics() {
		return itemPhysics;
	}

	public Item getItem() {
		return item;
	}

	public void setItemPhysics(PhysicsItem itemPhysics) {
		this.itemPhysics = itemPhysics;
	}
    
}

/*
 * $Log$
 */

