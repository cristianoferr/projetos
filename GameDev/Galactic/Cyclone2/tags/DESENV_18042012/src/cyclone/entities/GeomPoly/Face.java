package cyclone.entities.GeomPoly;

import cristiano.math.Vector3;


/**
 * A single face defined on a model. This is a sreies of points
 * each with a position, normal and texture coordinate
 * 
 * @author Kevin Glass
 */
public class Face {
	/** The vertices making up this face */
	private Vector3[] verts;
	/** The normals making up this face */
	private Vector3 normal,central;
	
	/**
	 * Create a new face
	 * 
	 * @param points The number of points building up this face
	 */
	public Face() {
		verts = new Vector3[3];
		//norms = new Vector3[3];
	}
	public Vector3 get(int i){
		return verts[i];
	}
	
	public void set(int i,Vector3 v){
		verts[i]=v;
	}
	
	public Vector3 getStart(){
		return verts[0];
	}
	public Vector3 getCentre(){
		return verts[1];
	}
	
	public Vector3 getEnd(){
		return verts[2];
	}
	

	
	/**
	 * Get the vertex information for a specified point within this face.
	 * 
	 * @param p The index of the vertex information to retrieve
	 * @return The vertex information from this face
	 */
	public Vector3 getVertex(int p) {
		return verts[p];
	}

	/**
	 * Get the normal information for a specified point within this face.
	 * 
	 * @param p The index of the normal information to retrieve
	 * @return The normal information from this face
	 */
	public Vector3 getNormal() {
		return normal;
	}
	public void calcNormal() {
		Vector3 v0=get(0);
		Vector3 v1=get(1);
		Vector3 v2=get(2);
		normal=new Vector3((v0.x+v1.x+v2.x)/3,(v0.y+v1.y+v2.y)/3,(v0.z+v1.z+v2.z)/3);
		central=new Vector3(normal);
		normal.normalise();
		
	}
	public Vector3[] getVerts() {
		return verts;
	}
	public Vector3 getCentral() {
		return central;
	}
}