package com.cristiano.cyclone.entities.geom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Vector3;



public class GeomBox extends Geom{
	
       

		/**
         * Holds the half-sizes of the box along each of its local axes.
         */
	public Vector3 halfSize;
	
	 public GeomBox(Vector3 halfSize) {
			super(Geom.PrimitiveType.BOX,PhysicsConsts.massaKG);
			this.halfSize=halfSize;
			
     }
	 public GeomBox(Vector3 halfSize,double density) {
			super(Geom.PrimitiveType.BOX,density);
			this.halfSize=halfSize;
			
  }
	
	
	public double getRadius(){
		return Math.max(Math.max(halfSize.x,halfSize.y),halfSize.z);
	}
	
	public double calculateMass(){
		return halfSize.x*2 * halfSize.y*2 * halfSize.z*2 * getDensity();
	}
	
	
	public Element buildXML(Document testDoc) {
		 Element elGeom = super.buildXML(testDoc);
		 elGeom.setAttribute("halfSizeX", Double.toString(halfSize.x));
		 elGeom.setAttribute("halfSizeY", Double.toString(halfSize.y));
		 elGeom.setAttribute("halfSizeZ", Double.toString(halfSize.z));
		 
		return elGeom;
	}
}

