package cyclone.collide;

import cyclone.entities.RigidBody;

public class BVHNode  extends BoundingSphere{

	
        /**
         * Holds the child nodes of this node.
         */
       public BVHNode children[];
   	
   	/**
        * Holds a single bounding volume encompassing all the
        * descendents of this node.
        */
       public BoundingSphere volume=null;

        

        /**
         * Holds the rigid body at this node of the hierarchy.
         * Only leaf nodes can have a rigid body defined (see isLeaf).
         * Note that it is possible to rewrite the algorithms in this
         * class to handle objects at all levels of the hierarchy,
         * but the code provided ignores this vector unless firstChild
         * is NULL.
         */
        RigidBody body=null;

        // ... other BVHNode code as before ...

        /**
         * Holds the node immediately above us in the tree.
         */
        BVHNode parent=null;

        /**
         * Creates a new node in the hierarchy with the given parameters.
         */
       public BVHNode(BVHNode parent,  BVHNode volume,
            RigidBody body){
    	   this(null,null);
            this.parent=parent;
            this.volume=volume;
            this.body=body;
          
        }

       

       
   	public BVHNode(BVHNode b1,BVHNode b2){
   		 children=new BVHNode[2];
   		 children[0]=b1;
   		 children[1]=b2;
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
        int getPotentialContacts(PotentialContact contacts,
                                      int limit) {
        	// Early out if we don't have the room for contacts, or
            // if we're a leaf node.
            if (isLeaf() || limit == 0) return 0;

            // Get the potential contacts of one of our children with
            // the other
            return children[0].getPotentialContactsWith(
                children[1], contacts, limit
                );
        }

        /**
         * Inserts the given rigid body, with the given bounding volume,
         * into the hierarchy. This may involve the creation of
         * further bounding volume nodes.
         */
        void insert(RigidBody body,  BVHNode volume){
        	 // If we are a leaf, then the only option is to spawn two
            // new children and place the new body in one.
            if (isLeaf())
            {
                // Child one is a copy of us.
                children[0] = new BVHNode(
                    this, volume, body
                    );

                // Child two holds the new body
                children[1] = new BVHNode(
                    this, volume, body
                    );

                // And we now loose the body (we're no longer a leaf)
                this.body = null;

                // We need to recalculate our bounding volume
                recalculateBoundingVolume(true);
        }
}
       

        /**
         * Checks for overlapping between nodes in the hierarchy. Note
         * that any bounding volume should have an overlaps method implemented
         * that checks for overlapping with another object of its own type.
         */
        boolean overlaps( BVHNode other) {
        	 return volume.overlaps(other.volume);
        }

        /**
         * Checks the potential contacts between this node and the given
         * other node, writing them to the given array (up to the
         * given limit). Returns the number of potential contacts it
         * found.
         */
      public  int getPotentialContactsWith(
             BVHNode other,
            PotentialContact contacts,
            int limit) {
          // Early out if we don't overlap or if we have no room
          // to report contacts
          if (!overlaps(other) || limit == 0) return 0;

          // If we're both at leaf nodes, then we have a potential contact
          if (isLeaf() && other.isLeaf())
          {
              contacts.body[0] = body;
              contacts.body[1] = other.body;
              return 1;
          }

          // Determine which node to descend into. If either is
          // a leaf, then we descend the other. If both are branches,
          // then we use the one with the largest size.
          if (other.isLeaf() ||
              (!isLeaf() && volume.getSize() >= other.volume.getSize()))
          {
              // Recurse into ourself
              int count = children[0].getPotentialContactsWith(
                  other, contacts, limit
                  );

              // Check we have enough slots to do the other side too
              if (limit > count) {
                  return count + children[1].getPotentialContactsWith(
                      other, contacts, limit-count
                      );
              } else {
                  return count;
              }
          }
          else
          {
              // Recurse into the other node
              int count = getPotentialContactsWith(
                  other.children[0], contacts, limit
                  );

              // Check we have enough slots to do the other side too
              if (limit > count) {
                  return count + getPotentialContactsWith(
                      other.children[1], contacts, limit-count
                      );
              } else {
                  return count;
              }
          }
        }

        /**
         * For non-leaf nodes, this method recalculates the bounding volume
         * based on the bounding volumes of its children.
         */
        void recalculateBoundingVolume(boolean recurse){
        	if (isLeaf()) return;

            // Use the bounding volume combining constructor.
            volume = new BoundingSphere(
                children[0].volume,
                children[1].volume
                );

            // Recurse up the tree
            if (recurse)
            	if (parent!=null) parent.recalculateBoundingVolume(recurse);
        }
}
