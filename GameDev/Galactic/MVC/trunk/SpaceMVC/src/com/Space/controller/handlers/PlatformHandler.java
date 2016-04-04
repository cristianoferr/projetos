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
import com.Space.controller.GameLogic.ItemHandler;
import com.Space.model.Item;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;

/**
 * Handles creation of platforms.
 * @author Irrisor
 */
public class PlatformHandler extends BasicHandler implements GameLogic.ItemHandler {

    public PlatformHandler( GameLogic gameLogic ) {
    	super(gameLogic);
    }

    public PhysicsNode createPhysicsFor( Item item ) {
        // create dynamic physics node for the item
        setItemPhysics(getGameLogic().getPhysicsSpace().createStaticNode());
        // and create some collision geometry
        // it is recommended that you always do this without using the visual representation, this is especially
        // useful when you want to have a server without visualization
        PhysicsBox box = getItemPhysics().createBox( null );
        // set size f the box
        box.getLocalScale().set( 10, 0.5f, 10 );
        //System.out.println("createPhysicsFor(Platform):"+item);
        return getItemPhysics();
        
    }


}

/*
 * $Log$
 */

