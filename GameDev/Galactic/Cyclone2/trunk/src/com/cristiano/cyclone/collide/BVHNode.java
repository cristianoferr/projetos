package com.cristiano.cyclone.collide;

import java.util.Vector;

import com.cristiano.cyclone.Cyclone;
import com.cristiano.cyclone.entities.RigidBody;


public class BVHNode {
	private  static int checks=0;
	private  static int nodesQty=0;
	private  static int errors=0;
	private int id=setNodesQty(getNodesQty() + 1);
	
        /**
         * Holds the child nodes of this node.
         */
	private BVHNode children[];
   	
   	/**
        * Holds a single bounding volume encompassing all the
        * descendents of this node.
        */
       private BoundingSphere volume=null;

        

        /**
         * Holds the rigid body at this node of the hierarchy.
         * Only leaf nodes can have a rigid body defined (see isLeaf).
         * Note that it is possible to rewrite the algorithms in this
         * class to handle objects at all levels of the hierarchy,
         * but the code provided ignores this vector unless firstChild
         * is NULL.
         */
       private RigidBody body=null;

        // ... other BVHNode code as before ...

        /**
         * Holds the node immediately above us in the tree.
         */
        private BVHNode parent=null;

        /**
         * Creates a new node in the hierarchy with the given parameters.
         */
       public BVHNode(BVHNode parent,  BoundingSphere volume,
            RigidBody body){
    	   this(null,null);
            this.parent=parent;
            this.setVolume(volume);
            this.body=body;
          
        }
       
      	public BVHNode(BVHNode b1,BVHNode b2){
      		 children=new BVHNode[2];
      		 children[0]=b1;
      		 children[1]=b2;
      	}
      	
      	public BVHNode getChild(int i){
      		return children[i];
      	}

       /**
       * Deletes this node, removing it first from the hierarchy, along
       * with its associated rigid body and child nodes. This method
       * deletes the node and all its children (but obviously not the
       * rigid bodies). This also has the effect of deleting the sibling
       * of this node, and changing the parent node so that it contains
       * the data currently in that sibling. Finally it forces the
       * hierarchy above the current node to reconsider its bounding
       * volume.
       */
              
       public void removeThis(){
    	// If we don’t have a parent, then we ignore the sibling processing.
    	   if (parent!=null){
    		   BVHNode sibling;
    		   if (parent.children[0]==this) {
    			   sibling=parent.children[1]; 
    		   }
    		   else {
    			   sibling=parent.children[0];
    		   }
    		   
    		// Write its data to our parent.
    		   parent.setVolume(sibling.getVolume());
    		   parent.body = sibling.body;
    		   parent.children[0] = sibling.children[0];
    		   parent.children[1] = sibling.children[1];
    		   // Delete the sibling (we blank its parent and
    		   // children to avoid processing/deleting them).
    		   sibling.parent = null;
    		   sibling.body = null;
    		   sibling.children[0] = null;
    		   sibling.children[1] = null;
    		   
    		   // Recalculate the parent’s bounding volume
    		   parent.recalculateBoundingVolume(true);
    	   }
    	   
    	// Delete our children (again we remove their parent data so
    	// we don’t try to process their siblings as they are deleted).
    	if (children[0]!=null) {
	    	children[0].parent = null;
	    	children[0]=null;
    	}
    	if (children[1]!=null) {
	    	children[1].parent = null;
	    	children[0]=null;
    	}
       }
       

       

   	
   	
        /**
         * Checks if this node is at the bottom of the hierarchy.
         */
        boolean isLeaf() {
            return (body != null);
        }

        /**
         * Checks the potential contacts from this node downwards in
         * the hierarchy, writing them to the given array (up to the
         * given limit). Returns the number of potential contacts it
         * found.
         */
       public int getPotentialContacts(Vector<PotentialContact> contacts) {
        	// Early out if we don't have the room for contacts, or
            // if we're a leaf node.
            if (isLeaf()) {
            	return 0;
            }
            if (children[0]==null) return 0;
            // Get the potential contacts of one of our children with
            // the other
            return children[0].getPotentialContactsWith(
                children[1], contacts,true);
        }

        /**
         * Inserts the given rigid body, with the given bounding volume,
         * into the hierarchy. This may involve the creation of
         * further bounding volume nodes.
         */
      public  void insert(RigidBody body,  BoundingSphere volume){
        	 // If we are a leaf, then the only option is to spawn two
            // new children and place the new body in one.
            if (isLeaf())
            {
                // Child one is a copy of us.
                children[0] = new BVHNode(
                    this, this.getVolume(), this.body
                    );

                // Child two holds the new body
                children[1] = new BVHNode(
                    this, volume, body
                    );

                // And we now loose the body (we're no longer a leaf)
                this.body = null;

                // We need to recalculate our bounding volume
                recalculateBoundingVolume(true);
        } else {
        	if (children[0]==null) { 
        		children[0]=new BVHNode(
                        this, volume, body
                );
        		return;
        	}
        	if (children[1]==null) { 
        		children[1]=new BVHNode(
                        this, volume, body
                );
        		return;
        	}
        	
        	if (children[0].getVolume().getGrowth(volume)<children[1].getVolume().getGrowth(volume)){
        		children[0].insert(body,volume);
        	}
        	else{
        		children[1].insert(body,volume);
        	}
        }
}
       

        /**
         * Checks for overlapping between nodes in the hierarchy. Note
         * that any bounding volume should have an overlaps method implemented
         * that checks for overlapping with another object of its own type.
         */
        boolean overlaps( BVHNode other) {
        	 return getVolume().overlaps(other.getVolume());
        }

        
      /*  public int getPotentialContactsWith2(BVHNode other, Vector<PotentialContact> contacts) 
    	{
    		
    			if(overlaps(other))
    			{
    				if(isLeaf())
    				{
    					if(other.isLeaf())
    					{
    						 PotentialContact contact=new PotentialContact(body,other.body);
    			        	 contacts.add(contact);
    			        	 return 1;
    					}

    					return other.getPotentialContactsWith2(this, contacts);
    				}

    				int nret = 0;
    				BVHNodeList::constant_iterator child_iter = childs.begin();
    				for(; child_iter != childs.end(); child_iter++)
    				{
    					nret += child_iter->getPotentialContactsWith(other, &contacts[nret], limit - nret);
    				}
    				return nret;
    			
    		}
    		return 0;
    	}*/
        
        private boolean alreadyInside(RigidBody b1,RigidBody b2,Vector<PotentialContact> contacts){
        	boolean ret=false;
        	for (int i=0;i<contacts.size();i++){
        		PotentialContact contact=contacts.get(i);
        		if (((contact.body[0]==b1) || (contact.body[1]==b1)) && 
        				((contact.body[0]==b2) || (contact.body[1]==b2))){
        			ret=true;
        		}
        			
        	}
        	
        	return ret;
        }
        /**
         * Checks the potential contacts between this node and the given
         * other node, writing them to the given array (up to the
         * given limit). Returns the number of potential contacts it
         * found.
         */
      private  int getPotentialContactsWith(
             BVHNode other,
            Vector<PotentialContact> contacts,boolean descend) {
    	  
    	  int count=0;
    	  //System.out.println(id+" comparando com "+other.id+" contacts.size:"+contacts.size());
    	  setChecks(getChecks() + 1);
          // Early out if we don't overlap or if we have no room
          // to report contacts
    	  
    	  if ((descend) && (!isLeaf())) {
         	 count += children[0].getPotentialContactsWith(
                   children[1], contacts,descend);
          }
    	  
    	  if ((descend) && (!other.isLeaf())) {
          	 count += other.children[0].getPotentialContactsWith(
          			other.children[1], contacts,descend);
           }
    	  
    	  if(!overlaps(other)) {
    		  return 0;
    	  }

          // If we're both at leaf nodes, then we have a potential contact
          if (isLeaf() && other.isLeaf())
          {
        	  if (!alreadyInside(body,other.body,contacts)){
	        	  PotentialContact contact=new PotentialContact(body,other.body);
	        	  contacts.add(contact);} else {setErrors(getErrors() + 1);}
              return 1;
          }

          
          // Determine which node to descend into. If either is
          // a leaf, then we descend the other. If both are branches,
          // then we use the one with the largest size.
          if (other.isLeaf() ||
              (!isLeaf() && getVolume().getSize() >= other.getVolume().getSize()))
          {
              // Recurse into ourself
              count += children[0].getPotentialContactsWith(
                  other, contacts,false
                  );

              // Check we have enough slots to do the other side too
                  count += children[1].getPotentialContactsWith(
                      other, contacts,false );
          }
          else
          {
              // Recurse into the other node
              count += getPotentialContactsWith(
                  other.children[0], contacts,false);

              // Check we have enough slots to do the other side too
                  count += getPotentialContactsWith(
                      other.children[1], contacts,false
                      );
             
          }
          
                  
          return count;
        }

        /**
         * For non-leaf nodes, this method recalculates the bounding volume
         * based on the bounding volumes of its children.
         */
      public  void recalculateBoundingVolume(boolean recurse){
        	if (isLeaf()) {
        		return;
        	}

            // Use the bounding volume combining constructor.
            setVolume(new BoundingSphere(
                children[0].getVolume(),
                children[1].getVolume()
                ));

            // Recurse up the tree
            	if ((recurse) && (parent!=null)){
            		parent.recalculateBoundingVolume(recurse);
            	}
        }
        
        public void report(String token){
        	String s="";
        	if (token.length()==1){
        		Cyclone.log("INITREPORT");
        	}
        	if (body!=null){ 
        		s=s+body.isAlive();
	        	if (!body.isAlive()) {
	        		removeThis();
	        	}
        	}
        	Cyclone.log(token+"-"+body+" "+s+" "+getVolume());
        	if (children[0]!=null){
        		children[0].report(token+"-");
        	}
        	if (children[1]!=null){
        		children[1].report(token+"-");
        	}
        	if (token.length()==1){
        		Cyclone.log("ENDREPORT");
        	}
        }

		public static int getChecks() {
			return checks;
		}

		public static int getNodesQty() {
			return nodesQty;
		}

		public static int getErrors() {
			return errors;
		}

		public static void setChecks(int checks) {
			BVHNode.checks = checks;
		}

		public static int setNodesQty(int nodesQty) {
			BVHNode.nodesQty = nodesQty;
			return nodesQty;
		}

		public static void setErrors(int errors) {
			BVHNode.errors = errors;
		}

		public void setVolume(BoundingSphere volume) {
			this.volume = volume;
		}

		public BoundingSphere getVolume() {
			return volume;
		}
}
