package cyclone.collide.bsp;

import java.util.Vector;


class BSPElement{}

/*class BSPChild{
	BSPNode.BSPChildType type;
	BSPNode node;
	Vector objects;
}*/

class BSObjectSet extends BSPElement{
	Vector<BSPElement> objects;
}

public class BSPNode extends BSPElement{
	
	public static enum BSPChildType
	{
	    NODE,
	    OBJECTS;
	}	
	
	BSPPlane plane;
	//BSPNode front;
	//BSPNode back;
	BSPElement front;
	BSPElement back;
}
