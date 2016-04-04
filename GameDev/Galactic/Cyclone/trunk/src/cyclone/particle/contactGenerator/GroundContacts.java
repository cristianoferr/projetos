package cyclone.particle.contactGenerator;

import java.util.Vector;

import cyclone.entities.Particle;
import cyclone.particle.ParticleContact;
import cyclone.math.Vector3;

public class GroundContacts implements ParticleContactGenerator{
	Vector<Particle> particles;

    public void init(Vector<Particle> particles){
    	this.particles=particles;
    }
    
public GroundContacts(Vector<Particle> particles){
	this.particles=particles;
}

    
    public int addContact(Vector<ParticleContact> contact,int limit) {
		int count = 0;
		for (int i=0;i<particles.size();i++){
				
			Particle p=particles.elementAt(i);
			double y = p.getPosition().y;
			if (y < 0.0f)
			{
				ParticleContact c=new ParticleContact(p,null);
		//	System.out.println("GroundContact: "+p);
			c.contactNormal = Vector3.UP;
			c.particle[0] = p;
			c.particle[1] = null;
			c.penetration = -y;
			c.restitution = 0.2f;
			contact.add(c);
			count++;
	}

if (count >= limit) return count;
}
return count;
}
    
}
