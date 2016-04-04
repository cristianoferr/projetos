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
package com.Space.controller.handlers;

import com.Space.controller.GameLogic;
import com.Space.controller.ships.BasicShip;
import com.Space.model.Body;
import com.Space.model.Item;
import com.Space.model.Ship;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsNode;

/**
 * Handles creation of actors.
 * @author Irrisor
 */
public class ShipHandler extends BodyHandler implements GameLogic.ItemHandler {

    public ShipHandler( GameLogic gameLogic ) {
    	super(gameLogic);
    }

    public PhysicsNode createPhysicsFor( Item item ) {
    	
    	setItemPhysics((DynamicPhysicsNode)super.createPhysicsFor(item));
    	BasicShip bs=new BasicShip(((DynamicPhysicsNode)getItemPhysics()));
//        PhysicsSphere sphere = getItemPhysics().createSphere( ((Body)item).getName() );
//        sphere.setMaterial(Material.GLASS);
        //PhysicsCylinder sphere = getItemPhysics().createCylinder(null);
        // set a sphere radius of 2
       // activateGravity();
    	((Ship)item).setBs(bs);
        bs.getChassis().setLocalScale( ((Body)item).getRadius() );
        ((DynamicPhysicsNode)getItemPhysics()).setMass(((Body)item).getMass());
        
        
        return getItemPhysics();
    }
    
}

/*
 * $Log$
 */

