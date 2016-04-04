package cyclone.contactGenerator;

import java.util.Vector;

import cristiano.math.Vector3;
import cyclone.contact.Contact;
import cyclone.entities.RigidBody;

public class GroundContacts implements ContactGenerator{
	Vector<RigidBody> bodies;

    public void init(Vector<RigidBody> bodies){
    	this.bodies=bodies;
    }
    
public GroundContacts(Vector<RigidBody> bodies){
	this.bodies=bodies;
}

    
    public int addContact(Vector<Contact> contact,int limit) {
		int count = 0;
		for (int i=0;i<bodies.size();i++){
				
			RigidBody p=bodies.elementAt(i);
			double y = p.getPosition().y;
			if (y < 0.0f)
			{
				Contact c=new Contact();
		//	System.out.println("GroundContact: "+p);
			c.setContactNormal(Vector3.UP);
			c.setBody(0,p);
			c.setBody(1,null);
			c.setPenetration(-y);
			c.setRestitution(0.2f);
			contact.add(c);
			count++;
	}
		
		if (count >= limit) {return count;}
		}
		return count;
		}
    
}
