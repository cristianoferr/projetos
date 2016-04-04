package cyclone.collide;

import cyclone.entities.RigidBody;

public class PotentialContact {
    /**
     * Stores a potential contact to check later.
     */
        /**
         * Holds the bodies that might be in contact.
         */
        RigidBody body[];
        
        public PotentialContact(RigidBody p1,RigidBody p2){
        	body=new RigidBody[2];
        	body[0]=p1;
        	body[1]=p2;
     //   	System.out.println("Potential contact between "+p1+" and "+p2);
        }
        
        public RigidBody getBody(int i){
        	return body[i];
        }
    

}
