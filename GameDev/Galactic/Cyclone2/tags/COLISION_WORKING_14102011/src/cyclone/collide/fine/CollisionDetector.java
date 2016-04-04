package cyclone.collide.fine;

import java.util.Vector;

import cristiano.math.Vector3;

import cyclone.contact.Contact;
import cyclone.entities.geom.GeomBox;
import cyclone.entities.geom.CollisionPlane;
import cyclone.entities.geom.GeomSphere;

public class CollisionDetector {

	
	static double smallestPenetration;
	static int smallestCase;
    /*
      A wrapper class that holds the fine grained collision detection
      routines.
     
      Each of the functions has the same format: it takes the details
      of two objects, and a pointer to a contact array to fill. It
      returns the number of contacts it wrote into the array.
     */
	//ok	
    public static int sphereAndHalfSpace(
             GeomSphere sphere,
             CollisionPlane plane,
            CollisionData data
            ){
        // Make sure we have contacts
        if (data.contactsLeft <= 0) return 0;

        // Cache the sphere position
        Vector3 position = sphere.getAxis(3);

        // Find the distance from the plane
        double ballDistance =
            plane.direction.scalarProduct(position) -
            sphere.radius - plane.offset;

        if (ballDistance >= 0) return 0;

        // Create the contact - it has a normal in the plane direction.
        Contact contact = new Contact();
        contact.contactNormal = plane.direction;
        contact.penetration = -ballDistance;
        contact.contactPoint =
            position.getSubVector(plane.direction.getMultiVector(ballDistance + sphere.radius));
        contact.setBodyData(sphere.body, null,
            data.getFriction(), data.getRestitution());

       // System.out.println("Contact sphereAndHalfSpace criado. pen="+contact.penetration+" contactPoint="+contact.contactPoint);
        data.addContacts(contact);
        return 1;
    }


    public  static int sphereAndSphere(
             GeomSphere one,
             GeomSphere two,
            CollisionData data
            ){
        // Make sure we have contacts
        if (data.contactsLeft <= 0) return 0;

        // Cache the sphere positions
        Vector3 positionOne = one.getAxis(3);
        Vector3 positionTwo = two.getAxis(3);

        // Find the vector between the objects
        Vector3 midline = positionOne.getSubVector(positionTwo);
        double size = midline.magnitude();

        // See if it is large enough.
        if (size <= 0.0f || size >= one.radius+two.radius)
        {
            return 0;
        }

        // We manually create the normal, because we have the
        // size to hand.
        Vector3 normal = midline.getMultiVector(((double)1.0)/size);

        Contact contact=new Contact();
        contact.contactNormal = normal;
        contact.contactPoint = positionOne.getAddVector(midline.getMultiVector((double)0.5));
        contact.penetration = (one.radius+two.radius - size);
        contact.setBodyData(one.body, two.body,
            data.getFriction(), data.getRestitution());

        data.addContacts(contact);
        return 1;
    }

        /*
          Does a collision test on a collision box and a plane representing
          a half-space (i.e. the normal of the plane
          points out of the half-space).
        */
    public static int boxAndHalfSpace(
             GeomBox box,
             CollisionPlane plane,
            CollisionData data
            ){
        // Make sure we have contacts

        // Check for intersection
        if (!IntersectionTests.boxAndHalfSpace(box, plane))
        {
            return 0;
        }

        // We have an intersection, so find the intersection points. We can make
        // do with only checking vertices. If the box is resting on a plane
        // or on an edge, it will be reported as four or two contact points.

        // Go through each combination of + and - for each half-size
        double mults[][]={{1,1,1},{-1,1,1},{1,-1,1},{-1,-1,1},
                                   {1,1,-1},{-1,1,-1},{1,-1,-1},{-1,-1,-1}};

        int contactsUsed = 0;
        for (int i = 0; i < 8; i++) {

            // Calculate the position of each vertex
            Vector3 vertexPos=new Vector3(mults[i][0], mults[i][1], mults[i][2]);
            vertexPos.componentProductUpdate(box.halfSize);
            vertexPos = box.getTransform().transform(vertexPos);

            // Calculate the distance from the plane
            double vertexDistance = vertexPos.scalarProduct(plane.direction);

            // Compare this to the plane's distance
            if (vertexDistance <= plane.offset)
            {
                // Create the contact data.

                // The contact point is halfway between the vertex and the
                // plane - we multiply the direction by half the separation
                // distance and add the vertex location.
            	Contact contact=new Contact();
              //  contact.contactPoint = new Vector3(plane.direction);
              //  contact.contactPoint.multiVectorScalar(vertexDistance-plane.offset);
                contact.contactPoint = vertexPos;
                contact.contactNormal = new Vector3(plane.direction);
                contact.penetration = plane.offset - vertexDistance;

                // Write the appropriate data
                contact.setBodyData(box.body, null,
                    data.getFriction(), data.getRestitution());

                // Move onto the next contact
              //  contact++;
                data.addContacts(contact);
            }
        }

        return contactsUsed;
    }

        public static int boxAndBox(
             GeomBox one,
             GeomBox two,
            CollisionData data
            ){
            //if (!IntersectionTests.boxAndBox(one, two)) return 0;

            // Find the vector between the two centres
            Vector3 toCentre = two.getAxis(3).getSubVector(one.getAxis(3));

            // We start assuming there is no contact
            smallestPenetration = Double.MAX_VALUE;
            smallestCase = 0xffffff;

            
            // Now we check each axes, returning if it gives us
            // a separating axis, and keeping track of the axis with
            // the smallest penetration otherwise.
            

            
             if (!tryAxis(one, two, one.getAxis(0), toCentre, 0)) return 0;
             if (!tryAxis(one, two, one.getAxis(1), toCentre, 1)) return 0;
             if (!tryAxis(one, two, one.getAxis(2), toCentre, 2)) return 0;
            
             if (!tryAxis(one, two, two.getAxis(0), toCentre, 3)) return 0;
             if (!tryAxis(one, two, two.getAxis(1), toCentre, 4)) return 0;
             if (!tryAxis(one, two, two.getAxis(2), toCentre, 5)) return 0;


            // Store the best axis-major, in case we run into almost
            // parallel edge collisions later
            int bestSingleAxis = smallestCase;

            if (!tryAxis(one, two, one.getAxis(0).getVectorProduct(two.getAxis(0)), toCentre, 6)) return 0;
            if (!tryAxis(one, two, one.getAxis(0).getVectorProduct(two.getAxis(1)), toCentre, 7)) return 0;
            if (!tryAxis(one, two, one.getAxis(0).getVectorProduct(two.getAxis(2)), toCentre, 8)) return 0;
            if (!tryAxis(one, two, one.getAxis(1).getVectorProduct(two.getAxis(0)), toCentre, 9)) return 0;
            if (!tryAxis(one, two, one.getAxis(1).getVectorProduct(two.getAxis(1)), toCentre, 10)) return 0;
            if (!tryAxis(one, two, one.getAxis(1).getVectorProduct(two.getAxis(2)), toCentre, 11)) return 0;
            if (!tryAxis(one, two, one.getAxis(2).getVectorProduct(two.getAxis(0)), toCentre, 12)) return 0;
            if (!tryAxis(one, two, one.getAxis(2).getVectorProduct(two.getAxis(1)), toCentre, 13)) return 0;
            if (!tryAxis(one, two, one.getAxis(2).getVectorProduct(two.getAxis(2)), toCentre, 14)) return 0;


            // Make sure we've got a result.
            if (smallestCase == 0xffffff) return 0;

            
            System.out.println("Contact box-box");
            // We now know there's a collision, and we know which
            // of the axes gave the smallest penetration. We now
            // can deal with it in different ways depending on
            // the case.
            if (smallestCase < 3)
            {
                // We've got a vertex of box two on a face of box one.
                fillPointFaceBoxBox(one, two, toCentre, data, smallestCase, smallestPenetration);
                //data.addContacts(1);
                return 1;
            }
            else if (smallestCase < 6)
            {
                // We've got a vertex of box one on a face of box two.
                // We use the same algorithm as above, but swap around
                // one and two (and therefore also the vector between their
                // centres).
                fillPointFaceBoxBox(two, one, toCentre.getMultiVector(-1.0f), data, smallestCase-3, smallestPenetration);
                return 1;
            }
            else
            {
                // We've got an edge-edge contact. Find out which axes
            	smallestCase -= 6;
                int oneAxisIndex = smallestCase / 3;
                int twoAxisIndex = smallestCase % 3;
                Vector3 oneAxis = one.getAxis(oneAxisIndex);
                Vector3 twoAxis = two.getAxis(twoAxisIndex);
                Vector3 axis = oneAxis.getVectorProduct(twoAxis);
                axis.normalise();

                // The axis should point from box one to box two.
                if (axis.scalarProduct(toCentre) > 0) axis.multiVectorScalar(-1.0f);

                // We have the axes, but not the edges: each axis has 4 edges parallel
                // to it, we need to find which of the 4 for each object. We do
                // that by finding the point in the centre of the edge. We know
                // its component in the direction of the box's collision axis is zero
                // (its a mid-point) and we determine which of the extremes in each
                // of the other axes is closest.
                Vector3 ptOnOneEdge = new Vector3(one.halfSize);
                Vector3 ptOnTwoEdge = new Vector3(two.halfSize);
                for (int i = 0; i < 3; i++)
                {
                    if (i == oneAxisIndex) ptOnOneEdge.set(i,0);
                    else if (one.getAxis(i).scalarProduct(axis) > 0) ptOnOneEdge.set(i,-ptOnOneEdge.get(i));

                    if (i == twoAxisIndex) ptOnTwoEdge.set(i,0);
                    else if (two.getAxis(i).scalarProduct(axis) < 0) ptOnTwoEdge.set(i,-ptOnTwoEdge.get(i));
                }

                // Move them into world coordinates (they are already oriented
                // correctly, since they have been derived from the axes).
                ptOnOneEdge = one.getTransform().getMulti(ptOnOneEdge);
                ptOnTwoEdge = two.getTransform().getMulti(ptOnTwoEdge);

                // So we have a point and a direction for the colliding edges.
                // We need to find out point of closest approach of the two
                // line-segments.
                Vector3 vertex = contactPoint(
                    ptOnOneEdge, oneAxis, one.halfSize.get(oneAxisIndex),
                    ptOnTwoEdge, twoAxis, two.halfSize.get(twoAxisIndex),
                    bestSingleAxis > 2
                    );

                // We can fill the contact.
                Contact contact=new Contact();
                contact.penetration = smallestPenetration;
                contact.contactNormal = axis;
                contact.contactPoint = vertex;
                contact.setBodyData(one.body, two.body,
                    data.getFriction(), data.getRestitution());
                data.addContacts(contact);
                return 1;
            }
         //   return 0;
        }

        
        
        static Vector3 contactPoint(
        	     Vector3 pOne,
        	     Vector3 dOne,
        	    double oneSize,
        	     Vector3 pTwo,
        	     Vector3 dTwo,
        	    double twoSize,

        	    // If this is true, and the contact point is outside
        	    // the edge (in the case of an edge-face contact) then
        	    // we use one's midpoint, otherwise we use two's.
        	    boolean useOne)
        	{
        	    Vector3 toSt, cOne, cTwo;
        	    double dpStaOne, dpStaTwo, dpOneTwo, smOne, smTwo;
        	    double denom, mua, mub;

        	    smOne = dOne.squareMagnitude();
        	    smTwo = dTwo.squareMagnitude();
        	    dpOneTwo = dTwo.scalarProduct(dOne);

        	    toSt = pOne.getSubVector(pTwo);
        	    dpStaOne = dOne.scalarProduct(toSt);
        	    dpStaTwo = dTwo.scalarProduct(toSt);

        	    denom = smOne * smTwo - dpOneTwo * dpOneTwo;

        	    // Zero denominator indicates parrallel lines
        	    if (Math.abs(denom) < 0.0001f) {
        	        return useOne?pOne:pTwo;
        	    }

        	    mua = (dpOneTwo * dpStaTwo - smTwo * dpStaOne) / denom;
        	    mub = (smOne * dpStaTwo - dpOneTwo * dpStaOne) / denom;

        	    // If either of the edges has the nearest point out
        	    // of bounds, then the edges aren't crossed, we have
        	    // an edge-face contact. Our point is on the edge, which
        	    // we know from the useOne parameter.
        	    if (mua > oneSize ||
        	        mua < -oneSize ||
        	        mub > twoSize ||
        	        mub < -twoSize)
        	    {
        	        return useOne?pOne:pTwo;
        	    }
        	    else
        	    {
        	        cOne = pOne.getAddVector(dOne.getMultiVector(mua));
        	        cTwo = pTwo.getAddVector(dTwo.getMultiVector(mub));

        	        return cOne.getMultiVector(0.5).getAddVector(cTwo.getMultiVector(0.5));
        	    }
        	}
        
        static void fillPointFaceBoxBox(
        	     GeomBox one,
        	     GeomBox two,
        	     Vector3 toCentre,
        	    CollisionData data,
        	    int best,
        	    double pen
        	    )
        	{
        	
        	
        	    // This method is called when we know that a vertex from
        	    // box two is in contact with box one.


        	    // We know which axis the collision is on (i.e. best),
        	    // but we need to work out which of the two faces on
        	    // this axis.
        	    Vector3 normal = one.getAxis(best);
        	    if (one.getAxis(best).scalarProduct(toCentre) > 0)
        	    {
        	        normal.multiVectorScalar(-1.0f);
        	    }

        	    // Work out which vertex of box two we're colliding with.
        	    // Using toCentre doesn't work!
        	    Vector3 vertex = new Vector3(two.halfSize);
        	    if (two.getAxis(0).scalarProduct(normal) < 0) vertex.x = -vertex.x;
        	    if (two.getAxis(1).scalarProduct(normal) < 0) vertex.y = -vertex.y;
        	    if (two.getAxis(2).scalarProduct(normal) < 0) vertex.z = -vertex.z;

        	    // Create the contact data
        	    Contact contact=new Contact();
        	    contact.contactNormal = normal;
        	    contact.penetration = pen;
        	    contact.contactPoint = two.getTransform().getMulti(vertex);
        	    contact.setBodyData(one.body, two.body,
        	        data.getFriction(), data.getRestitution());
        	    data.addContacts(contact);
        	}        
        
        /*
         * This function checks if the two boxes overlap
         * along the given axis, returning the ammount of overlap.
         * The final parameter toCentre
         * is used to pass in the vector between the boxes centre
         * points, to avoid having to recalculate it each time.
         */
        static double penetrationOnAxis(
             GeomBox one,
             GeomBox two,
             Vector3 axis,
             Vector3 toCentre
            )
        {
            // Project the half-size of one onto axis
        	double oneProject = transformToAxis(one, axis);
        	double twoProject = transformToAxis(two, axis);

            // Project this onto the axis
        	double distance = Math.abs(toCentre.scalarProduct(axis));

            // Return the overlap (i.e. positive indicates
            // overlap, negative indicates separation).
            return oneProject + twoProject - distance;
        }

        
        static double transformToAxis(
        	     GeomBox box,
        	     Vector3 axis
        	    )
        	{
        	    return
        	        box.halfSize.x * Math.abs(axis.scalarProduct(box.getAxis(0))) +
        	        box.halfSize.y * Math.abs(axis.scalarProduct(box.getAxis(1))) +
        	        box.halfSize.z * Math.abs(axis.scalarProduct(box.getAxis(2)));
        	}

        
        static boolean tryAxis(
        	   GeomBox one,
        	   GeomBox two,
        	    Vector3 axis,
        	    Vector3 toCentre,
        	    int index

        	    // These values may be updated
        	/*  ,  double smallestPenetration,
        	    int smallestCase*/
        	    )
        	{
        	    // Make sure we have a normalized axis, and don't check almost parallel axes
        	    if (axis.squareMagnitude() < 0.0001) return true;
        	    axis.normalise();

        	    double penetration = penetrationOnAxis(one, two, axis, toCentre);

        	    if (penetration < 0) return false;
        	    if (penetration < smallestPenetration) {
        	        smallestPenetration = penetration;
        	        smallestCase = index;
        	    }
        	    return true;
        	}
        
        
        public static int boxAndPoint(
             GeomBox box,
             Vector3 point,
            CollisionData data
            ){
            // Transform the point into box coordinates
            Vector3 relPt = box.getTransform().transformInverse(point);

            Vector3 normal;

            // Check each axis, looking for the axis on which the
            // penetration is least deep.
            double min_depth = box.halfSize.x - Math.abs(relPt.x);
            if (min_depth < 0) return 0;
            normal = box.getAxis(0).getMultiVector(((relPt.x < 0)?-1:1));

            double depth = box.halfSize.y - Math.abs(relPt.y);
            if (depth < 0) return 0;
            else if (depth < min_depth)
            {
                min_depth = depth;
                normal = box.getAxis(1).getMultiVector(((relPt.y < 0)?-1:1));
            }

            depth = box.halfSize.z - Math.abs(relPt.z);
            if (depth < 0) return 0;
            else if (depth < min_depth)
            {
                min_depth = depth;
                normal = box.getAxis(2).getMultiVector(((relPt.z < 0)?-1:1));
            }

            // Compile the contact
            Contact contact=new Contact();
            contact.contactNormal = normal;
            contact.contactPoint = point;
            contact.penetration = min_depth;

            // Note that we don't know what rigid body the point
            // belongs to, so we just use NULL. Where this is called
            // this value can be left, or filled in.
            contact.setBodyData(box.body, null,
                data.getFriction(), data.getRestitution());

            data.addContacts(contact);
            return 1;
        }

        public static int boxAndSphere(
             GeomBox box,
             GeomSphere sphere,
            CollisionData data
            ){
            // Transform the centre of the sphere into box coordinates
            Vector3 centre = sphere.getAxis(3);
            Vector3 relCentre = box.getTransform().transformInverse(centre);

            // Early out check to see if we can exclude the contact
          /*  System.out.println("x: "+(Math.abs(relCentre.x) - sphere.radius));
            System.out.println("y: "+(Math.abs(relCentre.y) - sphere.radius));
            System.out.println("z: "+(Math.abs(relCentre.z) - sphere.radius));*/
            if (Math.abs(relCentre.x) - sphere.radius > box.halfSize.x ||
                Math.abs(relCentre.y) - sphere.radius > box.halfSize.y ||
                Math.abs(relCentre.z) - sphere.radius > box.halfSize.z)
            {
                return 0;
            }

            Vector3 closestPt=new Vector3(0,0,0);
            double dist;

            // Clamp each coordinate to the box.
            dist = relCentre.x;
            if (dist > box.halfSize.x) dist = box.halfSize.x;
            if (dist < -box.halfSize.x) dist = -box.halfSize.x;
            closestPt.x = dist;

            dist = relCentre.y;
            if (dist > box.halfSize.y) dist = box.halfSize.y;
            if (dist < -box.halfSize.y) dist = -box.halfSize.y;
            closestPt.y = dist;

            dist = relCentre.z;
            if (dist > box.halfSize.z) dist = box.halfSize.z;
            if (dist < -box.halfSize.z) dist = -box.halfSize.z;
            closestPt.z = dist;

            // Check we're in contact
            dist = (closestPt.getSubVector(relCentre)).squareMagnitude();
            if (dist > sphere.radius * sphere.radius) return 0;

            // Compile the contact
            Vector3 closestPtWorld = box.getTransform().transform(closestPt);

            System.out.println("boxAndSphere contact created");
            Contact contact=new Contact();
            contact.contactNormal = (closestPtWorld.getSubVector(centre));
            contact.contactNormal.normalise();
            contact.contactPoint = closestPtWorld;
            contact.penetration = sphere.radius - Math.sqrt(dist);
            contact.setBodyData(box.body, sphere.body,
                data.getFriction(), data.getRestitution());

            data.addContacts(contact);
            return 1;
        }
    
}

