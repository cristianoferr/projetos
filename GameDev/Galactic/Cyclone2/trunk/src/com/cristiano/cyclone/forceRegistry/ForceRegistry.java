package com.cristiano.cyclone.forceRegistry;

import java.util.Vector;

import com.cristiano.cyclone.entities.RigidBody;
import com.cristiano.cyclone.forceGenerator.ForceGenerator;


class ForceRegistration
{
    RigidBody particle;
    ForceGenerator fg;
    ForceRegistration(RigidBody particle,ForceGenerator fg){
    	this.particle=particle;
    	this.fg=fg;
    }
}

public class ForceRegistry {
	/**
     * Holds all the force generators and the particles they apply to.
     */

        /**
         * Keeps track of one force generator and the particle it
         * applies to.
         */
        

        /**
         * Holds the list of registrations.
         */
        Vector<ForceRegistration> registrations=new Vector<ForceRegistration>();
        

    public void add(RigidBody particle, ForceGenerator fg){
    	registrations.add(new ForceRegistration(particle,fg));
    }

        /**
         * Removes the given registered pair from the registry.
         * If the pair is not registered, this method will have
         * no effect.
         */
    public void remove(RigidBody particle, ForceGenerator fg){
    	for (int i=0;i<registrations.size();i++)    {
    		ForceRegistration pfr=registrations.elementAt(i);
    		if ((pfr.particle==particle) && (pfr.fg==fg)) registrations.remove(pfr);
    	}
    }
    
    public void remove(RigidBody particle){
    	for (int i=0;i<registrations.size();i++)    {
    		ForceRegistration pfr=registrations.elementAt(i);
    		if (pfr.particle==particle) registrations.remove(pfr);
    	}
    }

        /**
         * Clears all registrations from the registry. This will
         * not delete the particles or the force generators
         * themselves, just the records of their connection.
         */
    public void clear(){
    	registrations.clear();
    }

        /**
         * Calls all the force generators to update the forces of
         * their corresponding particles.
         */
    public void updateForces(double duration)    {

        for (int i=0;i<registrations.size();i++)    {
        	ForceRegistration p=registrations.elementAt(i);
        	p.fg.updateForce(p.particle, duration);
        	//if (!p.particle.isAlive()) remove(p.particle);
//            i->fg->updateForce(i->particle, duration);
        }
    }    
}
