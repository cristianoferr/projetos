package cyclone.particle.contactGenerator;

import java.util.Vector;

import cyclone.entities.Particle;
import cyclone.math.Vector3;
import cyclone.particle.ParticleContact;

public class ParticleRod extends ParticleLink{
    /**
     * Rods link a pair of particles, generating a contact if they
     * stray too far apart or too close.
     */
    public ParticleRod(Particle p1, Particle p2,double length) {
		super(p1, p2);
		this.length=length;
	}

        /**
         * Holds the length of the rod.
         */
    public double length;

    public int addContact(Vector<ParticleContact> contact, int limit){
        // Find the length of the rod
        double currentLen = currentLength();

        // Check if we're over-extended
        if (currentLen == length)
        {
            return 0;
        }

        ParticleContact c=new ParticleContact(particle[0],particle[1]);
     //   System.out.println("ParticleRod: ");//+particle[0].getVelocity()+" 1:"+particle[1].getVelocity());
        // Otherwise return the contact

        // Calculate the normal
        Vector3 normal = particle[1].getPosition().getSubVector(particle[0].getPosition());
        normal.normalise();

        // The contact normal depends on whether we're extending or compressing
        if (currentLen > length) {
            c.contactNormal = normal;
            c.penetration = currentLen - length;
        } else {
            c.contactNormal = normal.getMultiVector(-1);
            c.penetration = length - currentLen;
        }
        contact.add(c);
        // Always use zero restitution (no bounciness)
        c.restitution = 0;

        return 1;
    }

}
