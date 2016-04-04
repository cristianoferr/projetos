package com.cristiano.cyclone.entities.geom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cristiano.cyclone.math.PhysicsConsts;




public class GeomSphere extends Geom{
	
	/**
     * The radius of the sphere.
     */
public double radius;


        public GeomSphere(double radius) {
        	super(Geom.PrimitiveType.SPHERE,PhysicsConsts.massaKG);
		this.radius=radius;
	}

		
	
	
	public double getRadius() {
			return radius;
		}

	
	public double calculateMass(){
		return (double)(4/3*(Math.PI)*Math.pow(radius,3) * getDensity());
	}
	
	public Element buildXML(Document testDoc) {
		 Element elGeom = super.buildXML(testDoc);
		 elGeom.setAttribute("radius", radius+"");
		return elGeom;
	}




	public void setRadius(double radius) {
		this.radius = radius;
	}
	
}
