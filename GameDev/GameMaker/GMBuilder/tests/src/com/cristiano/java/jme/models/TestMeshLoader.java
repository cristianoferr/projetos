package com.cristiano.java.jme.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.lifeCycle.MeshLoaderComponent;
import com.cristiano.java.gm.ecs.systems.MeshLoaderSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

class ExpectedMesh{
	public Vector3f pos;
	public ArrayList<Vector3f> points=new ArrayList<Vector3f>();
	
	public ExpectedMesh(Vector3f pos) {
		this.pos=pos;
	}

	public void addPoint(Vector3f pt){
		points.add(pt);
	}
	
	public void validate(String meshName, SpatialComponent spatial){
		Log.info("Validating mesh:"+meshName);
		assertTrue(meshName+":: Position differ: "+spatial.position+" should be "+pos,spatial.position.equals(pos));
		
		for (Vector3f point:points){
			validaPoint(meshName,point,spatial);
		}
	}

	private void validaPoint(String meshName, Vector3f point, SpatialComponent spatial) {
		boolean found=false;
		for (Vector3f pointSpatial:spatial.sourcePoints){
			if (pointSpatial.equals(point)){
				found=true;
			}
		}
		assertTrue(meshName+":: Point not found ("+point+"):"+spatial.sourcePoints,found);
		
	}
}

public class TestMeshLoader extends MockAbstractTest {

	private static final int HEIGHT = 2;
	private static final int WIDTH = 5;
	private static final int DEPTH = 7;
	private String mesh1;
	private String mesh2;
	private ExpectedMesh expMesh1;
	private ExpectedMesh expMesh2;
	private ExpectedMesh expMesh3;
	private String mesh3;
	private String meshWheel;
	private ExpectedMesh expMeshWheel;
	private String meshCirculo;
	private ExpectedMesh expMeshCirculo;

	


	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}

	@Test
	public void testEffectLibrary() {
		IGameEntity ent = entMan.createEntity();
		//mockUnit(ent);
		MeshLoaderSystem meshLS = initMeshLoaderSystem();
		MeshLoaderComponent meshLC = mockMeshLoaderComponent(ent);
		meshLS.iterateEntity(ent, meshLC, 1);
	}

	private MeshLoaderComponent mockMeshLoaderComponent(IGameEntity ent) {
		MeshLoaderComponent meshLC = (MeshLoaderComponent) entMan.addComponent(
				GameComps.COMP_MESH_LOADER, ent);
		IGameElement elMesh=em.createBlueprint(null);
		elMesh.setProperty(GameProperties.WIDTH, WIDTH);
		elMesh.setProperty(GameProperties.HEIGHT, HEIGHT);
		elMesh.setProperty(GameProperties.DEPTH, DEPTH);
		elMesh.setProperty(GameProperties.HULL_MAKER, "pickSingle({convex hullmaker leaf})");
		
		mesh1 = "mesh1";
		elMesh.setParam("mesh",mesh1,"addMesh({mesh box leaf},posX=0,posY=0,posZ=0,merge=1,orientX=0,orientZ=0,orientX=0,replicate=1,minAngle=0,radius=0,maxAngle=360)");
		mesh2 = "poli2";
		mesh3 = "mesh3";
		
		float px=2;//2
		float py=0;
		float relSizeX=0.5f;
		float relSizeY=3;
		float relSizeZ=3;
		
		
		
		configuraWheel(elMesh);
		//configuraCirculo(elMesh);
		
		elMesh.setParam("mesh",mesh2,"addMesh({mesh testMesh1 leaf},posX="+px+",posY="+py+",posZ=0,merge=0,sizeX="+relSizeX+",sizeY="+relSizeY+",sizeZ="+relSizeZ+",orientX=0,orientZ=0,orientX=0,replicate=1,radius=0)");
		
		expMesh1 = new ExpectedMesh(new Vector3f(0,0,0));
		addPoints(expMesh1,WIDTH,HEIGHT,DEPTH);
		
		//mesh2.x=5, width=2.5
		expMesh2 = new ExpectedMesh(new Vector3f(WIDTH/2f*px,HEIGHT/2*py,0));
		float mesh2SizeX=WIDTH*relSizeX;
		float mesh2SizeY = HEIGHT*relSizeY;
		float mesh2SizeZ = DEPTH*relSizeZ;
		addPoints(expMesh2,mesh2SizeX,mesh2SizeY,mesh2SizeZ);
		
		//mesh3.x=mesh2.x+2*mesh2.width/2=7.5
		expMesh3 = new ExpectedMesh(new Vector3f(7.5f,HEIGHT/2*py,0));
		addPoints(expMesh3,mesh2SizeX*1f,mesh2SizeY*2f,mesh2SizeZ*3f);
		
		meshLC.meshElement=em.createFinalElement(elMesh);
		assertNotNull(meshLC);
		return meshLC;
	}

	private void configuraWheel(IGameElement elMesh) {
		meshWheel = "meshWheel";
		float wheelX=4;
		elMesh.setParam("mesh",meshWheel,"addMesh({mesh cilindro leaf},posX="+wheelX+",posY=0,posZ=0,merge=0,orientX=0,orientZ=0,orientX=0,replicate=1,radius=0)");
		
		expMeshWheel= new ExpectedMesh(new Vector3f(wheelX*WIDTH/2,0,0));
		float radius=10;
		float angle=0;
		float depth=DEPTH;
		
		int pts = 5;
		for (int i=0;i<pts;i++){
			
			float rotX = angle * FastMath.DEG_TO_RAD;
			float rotY = 0;
			float x = (float) (radius * Math.cos(rotX) * Math.cos(rotY)) ;
			float y = (float) (radius * Math.sin(rotX)) ;
			float z = (float) (radius * Math.cos(rotX) * Math.sin(rotY)) ;
			angle+=360f/pts;
			expMeshWheel.addPoint(new Vector3f(x,y,z-depth/2));
			expMeshWheel.addPoint(new Vector3f(x,y,z+depth/2));
		}
	}

	private void configuraCirculo(IGameElement elMesh) {
		meshCirculo = "meshCirculo";
		float wheelX=3;
		elMesh.setParam("mesh",meshCirculo,"addMesh({mesh circulo leaf},posX="+wheelX+",posY=0,posZ=0,merge=0,orientX=0,orientZ=0,orientX=0,replicate=1,radius=0)");
		
		expMeshCirculo= new ExpectedMesh(new Vector3f(wheelX*WIDTH/2,0,0));
		
	}
	

	private void addPoints(ExpectedMesh mesh,float width, float height, float depth) {
		mesh.addPoint(new Vector3f(width/2f,height/2f,depth/2f));
		mesh.addPoint(new Vector3f(width/2f,height/2f,-depth/2f));
		mesh.addPoint(new Vector3f(width/2f,-height/2f,depth/2f));
		mesh.addPoint(new Vector3f(width/2f,-height/2f,-depth/2f));
		mesh.addPoint(new Vector3f(-width/2f,height/2f,depth/2f));
		mesh.addPoint(new Vector3f(-width/2f,height/2f,-depth/2f));
		mesh.addPoint(new Vector3f(-width/2f,-height/2f,depth/2f));
		mesh.addPoint(new Vector3f(-width/2f,-height/2f,-depth/2f));
		
	}

	@Test
	public void testMeshLoaderSystem() {
		MeshLoaderSystem meshLS = initMeshLoaderSystem();
		assertNotNull(meshLS);
		IGameEntity ent = entMan.createEntity();
		mockUnit(ent);
		MeshLoaderComponent meshLC = mockMeshLoaderComponent(ent);
		
		meshLS.iterateEntity(ent, meshLC, 1);
		
		List<IGameComponent> spatials = ent.getComponentsWithIdentifier(GameComps.COMP_SPATIAL);
		assertNotNull(spatials);
		assertFalse(spatials.isEmpty());
		assertTrue("Spatials generated:"+spatials.size(),spatials.size()==4);
		
		for (IGameComponent comp:spatials){
			SpatialComponent spatial=(SpatialComponent) comp;
			//assertNotNull("Name for spatial is undefined",spatial.objName);
			if (spatial.objName==null){
				spatial.objName=mesh1;	
			}
			
			if (spatial.objName.contains(mesh1) ){
				validaMesh(mesh1,expMesh1,spatial);
			}
			if (spatial.objName.contains(mesh2)){
				validaMesh(mesh2,expMesh2,spatial);
			}
			if (spatial.objName.contains(mesh3)){
				validaMesh(mesh3,expMesh3,spatial);
			}
			if (spatial.objName.contains(meshWheel)){
				validaMesh(meshWheel,expMeshWheel,spatial);
			}
		/*	if (spatial.objName.contains(meshCirculo)){
				validaMesh(meshCirculo,expMeshCirculo,spatial);
			}*/
		}
		
	}

	private void validaMesh(String meshName,ExpectedMesh expMesh, SpatialComponent spatial) {
		expMesh.validate(meshName,spatial);
		
	}


}
