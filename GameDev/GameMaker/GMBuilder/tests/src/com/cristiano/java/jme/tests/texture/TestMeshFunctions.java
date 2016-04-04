package com.cristiano.java.jme.tests.texture;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.blueprint.Blueprint;
import com.cristiano.java.gm.builder.factory.BuilderFactory;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.Log;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;


public class TestMeshFunctions {
	static ElementManager em=null;
	//MockFactory factory=null;
	private static EntityManager entMan;
	private static BuilderFactory fact;
	static GameEntity entity;
	String tagCurve=TestStrings.TAG_LINE_CURVED;
	String tagLine=TestStrings.TAG_LINE;
	String tagMulti=TestStrings.TAG_LINE_MULTI;
	
	@BeforeClass
	public static void setUp() throws IOException {
		em=new ElementManager();
		em.loadBlueprintsFromFile();
		entMan=new EntityManager();
		fact=new BuilderFactory(em,entMan,null);
		entity=new GameEntity();
		entMan.addEntity(entity);
	} 
	
	@Test public void testIntersectionFunction() {
		float scale=1;
		//Vector3f offset=new Vector3f(0,0,0);
		Vector3f offset=new Vector3f(150,0,50);
		Vector3f ptA3D=new Vector3f(-0.75818783f, -0.25771004f, 10.354799f);
		Vector3f ptB3D=new Vector3f(0.45491272f, -0.25771004f, 16.503511f);
		Vector3f ptC3D=new Vector3f(0.75818783f, -0.25771004f, 18.040688f);
		ptA3D.multLocal(scale);
		ptB3D.multLocal(scale);
		ptC3D.multLocal(scale);
		ptA3D.addLocal(offset);
		ptB3D.addLocal(offset);
		ptC3D.addLocal(offset);
		
		Vector2f ptA=new Vector2f(ptA3D.x,ptA3D.z);
		Vector2f ptB=new Vector2f(ptB3D.x,ptB3D.z);
		Vector2f ptC=new Vector2f(ptC3D.x,ptC3D.z);
		
		float distAB3d=ptA.distance(ptB);
		float distBC3d=ptB.distance(ptC);
		float distAC3d=ptA.distance(ptC);
		float a=distBC3d;
		float b=distAC3d;
		float c=distAB3d;
		
		CRDebugDraw draw = new CRDebugDraw(500);
		
		draw.drawPoint(ptA,Color.black);
		draw.drawPoint(ptB,Color.red);
		draw.drawPoint(ptC,Color.blue);
		draw.drawCircle(ptA,b,Color.black);
		draw.drawCircle(ptB,a,Color.red);
		draw.drawCircle(ptC,b,Color.blue);
		draw.drawCircle(ptC,a,Color.blue);
		draw.setColor(Color.green);
		draw.drawLine(ptA, ptB);
		draw.drawLine(ptA, ptC);
		draw.drawLine(ptB, ptC);
		
		draw.finishDebugDraw("testIntersect");
		
		Vector2f res0=new Vector2f();
		Vector2f res1=new Vector2f();
		boolean intersect=JMEUtils.circleIntersection(ptA,b,ptB,a,res0,res1);
		assertFalse("No intersection for the points...",intersect);
		
		
		ptA.addLocal(0.1f, 0.1f);
		distAB3d=ptA.distance(ptB);
		 distBC3d=ptB.distance(ptC);
		 distAC3d=ptA.distance(ptC);
		 a=distBC3d;
		 b=distAC3d;
		 c=distAB3d;
		
		
		intersect=JMEUtils.circleIntersection(ptA,b,ptB,a,res0,res1);
		assertTrue("It should Intersect after moving the point...",intersect);
		Log.debug("Intersect? "+intersect+" res0:"+res0+" res1:"+res1);
		//intersect=JME3Utils.circleIntersection(ptA,b,ptB,c,res0,res1);
		//assertTrue("It should Intersect in 3d",intersect);
		
	}
	
	@Test public void testPoligonoFunction() {
		int width=200;
		int depth=100;
		IGameElement elPoli = em.createFinalElementFromTag("poligono xz",null,"width="+width+",depth="+depth+",radius=10");
		assertNotNull(elPoli);
		ArrayList<Vector3f> points = BuilderUtils.extractPointsFromList(elPoli, "pontoPoli", new Vector3f(0,0,0));
		assertNotNull(points);
		for (int i=0;i<points.size();i++){
			Vector3f pt=points.get(i);
			assertTrue("altura tem que ser 0:"+pt.y,pt.y==0);
			assertTrue("width tem que ser <200:"+pt.x,Math.abs(pt.x)<width);
			assertTrue("depth tem que ser <100:"+pt.z,Math.abs(pt.z)<depth);
		}
	}
	
	@Test public void testTextureMat() {
		IGameElement elTextureMat = em.createFinalElementFromTag(TestStrings.MATERIAL_TEST);
		assertNotNull(elTextureMat);
		
		
		//testando diffuseMap
		IGameElement elLayer = elTextureMat.getPropertyAsGE("diffuse");
		String color=elTextureMat.getProperty(GameProperties.COLOR);
		assertNotNull(color);
		assertFalse(color.equals(""));
		
		//Testando Layer
		assertFalse(elLayer.getProperty("blueFunction").equals(""));
		assertFalse(elLayer.getProperty("redFunction").equals(""));
		assertFalse(elLayer.getProperty("greenFunction").equals(""));
		assertFalse(elLayer.getProperty("alphaFunction").equals(""));
		
		
		
		//Testando Blending
		IGameElement blending=elTextureMat.getPropertyAsGE(GameProperties.TEXTURE_BLENDING);
		assertNotNull(blending);
		
	}
	
	@Test public void testWing() {
		IGameElement elWing = em.createFinalElementFromTag("testwing");
		assertNotNull(elWing);
		IGameElement elMesh = elWing.getPropertyAsGE("objectMesh");
		assertNotNull(elMesh);
	}
	
	@Test public void testEsfera() {
		IGameElement elWing = em.createFinalElementFromTag("testesfera");
		assertNotNull(elWing);
		IGameElement elMesh = elWing.getPropertyAsGE("objectMesh");
		assertNotNull(elMesh);
	}
	
	@Test public void testPlaneModel() {
		IGameElement elPlane = em.createFinalElementFromTag(TestStrings.TAG_TEST_PLANE);
		assertNotNull(elPlane);
		IGameElement elMesh = elPlane.getPropertyAsGE("objectMesh");
		assertNotNull(elMesh);
		
		//test body
		IGameElement elBody= elMesh.getParamAsGE(Extras.LIST_OBJECT,"corpo#0");
		assertNotNull(elBody);
		float circulo0BodyZ=elBody.getParamAsFloat(Extras.LIST_POSITION, "circulo#0.z");
		float circulo1BodyZ=elBody.getParamAsFloat(Extras.LIST_POSITION, "circulo#1.z");
		assertTrue(elMesh.getIdentifier()+":: circulo0BodyZ error:"+circulo0BodyZ,circulo0BodyZ==-10);
		assertTrue(circulo1BodyZ+"<>"+10,circulo1BodyZ==10);
		//mesh>body>asa
		//ASA0
		IGameElement elAsa0 = elMesh.getParamAsGE(Extras.LIST_OBJECT,"wing#0");
		assertNotNull(elAsa0);
		float asa0X=elBody.getParamAsFloat(Extras.LIST_POSITION, "wing#0.x");
		float asa1X=elBody.getParamAsFloat(Extras.LIST_POSITION, "wing#1.x");
		//assertTrue("Posicao X das asas na fuselagem s�o iguais: "+asa0X,asa0X!=asa1X);
		
		IGameElement elBaseAsa0 = elAsa0.getParamAsGE(Extras.LIST_OBJECT,"basePoli#0");
		assertNotNull(elBaseAsa0);
		IGameElement elLinha0Asa0 = elAsa0.getParamAsGE(Extras.LIST_OBJECT,"line#0");
		assertNotNull(elLinha0Asa0);
		
		//ASA1
		IGameElement elAsa1 = elMesh.getParamAsGE(Extras.LIST_OBJECT,"wing#1");
		assertNotNull(elAsa1);
		IGameElement elBaseAsa1 = elAsa1.getParamAsGE(Extras.LIST_OBJECT,"basePoli#0");
		assertNotNull(elBaseAsa1);
		IGameElement elLinha0Asa1 = elAsa1.getParamAsGE(Extras.LIST_OBJECT,"line#0");
		assertNotNull(elLinha0Asa1);
		
		assertTrue("identifier linhas diferem",elLinha0Asa0.getIdentifier().equals(elLinha0Asa1.getIdentifier()));
		int qtdPts0=elLinha0Asa0.getPropertyAsInt("qtdPts");
		int qtdPts1=elLinha0Asa1.getPropertyAsInt("qtdPts");
		assertTrue("qtdPts0<>qtdPts1",qtdPts0==qtdPts1);
		for (int i=0;i<qtdPts0;i++){
			float px0=elLinha0Asa0.getParamAsFloat(Extras.LIST_POSITION, "ponto#"+i+".x");
			float px1=elLinha0Asa1.getParamAsFloat(Extras.LIST_POSITION, "ponto#"+i+".x");
			assertTrue("px0<>-px1: "+px0+"<>"+px1+"(neg)",px0==-px1);
		}
	}
	
	@Test public void testMirrorX() {
		IGameElement elPai = em.createFinalElementFromTag(TestStrings.TEST_MIRROR_PAI);
		assertNotNull(elPai);
		List<IGameElement> objectList = elPai.getObjectList("ponto");
		IGameElement ponto0=objectList.get(0);
		IGameElement ponto1=objectList.get(1);
		assertNotNull(ponto0);
		assertNotNull(ponto1);
		validaPontosMirrorX("p0",ponto0,false);
		validaPontosMirrorX("p1",ponto1,true);
		
	}
	
	
	private void validaPontosMirrorX(String name,IGameElement ponto1, boolean flagMirror) {
		List<IGameElement> pointList = ponto1.getObjectList("ponto");
		for (int i=0;i<pointList.size();i++){
			//GenericElement point=pointList.get(i);
			String objName="ponto#"+i;
			float x=ponto1.getParamAsFloat(Extras.LIST_POSITION,objName+".x" );
			float y=ponto1.getParamAsFloat(Extras.LIST_POSITION,objName+".y" );
			float z=ponto1.getParamAsFloat(Extras.LIST_POSITION,objName+".z" );
			float xEsp=-5+i*10;
			float yEsp=-5+i*10;
			float zEsp=-5+i*10;
			if (flagMirror){
				xEsp=-xEsp;
			}
			assertTrue(name+": x difere: "+x+"<>"+xEsp,x==xEsp);
			assertTrue(name+": y difere: "+y+"<>"+yEsp,y==yEsp);
			assertTrue(name+": z difere: "+z+"<>"+zEsp,z==zEsp);
		}
		
	}

	@Test public void testStraightLine() {
		String tag=tagLine;
		int x1=0;
		int y1=0;
		int x2=100;
		int y2=100;
		int pts=10;		
		IGameElement finalElement = createLine(tag, x1, y1, x2, y2, pts);
		
		assertNotNull("elementoFinal Nulo",finalElement);
		verificaPonto(finalElement, 5, 50, 50,0);
		verificaPonto(finalElement, 0, 0, 0,0);
		verificaPonto(finalElement, 9, 90, 90,0);
		
		validaLinhaSimples(x1,y1,0,x2,y2,0,pts,finalElement);
	}
	
	private void validaLinhaSimples(int x1, int y1, int z1, int x2, int y2, int z2,int pts, IGameElement finalElement) {
		//this will validate first and last points for non-multi lines...
		verificaPonto(finalElement,0,x1,y1,z1);
		verificaPonto(finalElement,pts,x2,y2,z2);
		
	}
	
	@Test public void testcurvedLineFunctionT1() {
		String tag="curved line t1 functions leaf";
		validateCurvedLineFromTag(tag);
	}

	@Test public void testcurvedLineFunctionT2() {
		String tag="curved line t2 functions leaf";
		validateCurvedLineFromTag(tag);
	}

	private void validateCurvedLineFromTag(String tag) {
		int x1=2500;
		int z1=3297;
		int x2=3318;
		int z2=3817;
		int pts=18;	
		validaCurvedLine(tag, x1, z1, x2, z2, pts, 0.8f);
		validaCurvedLine(tag, x1, z1, x2, z2, pts, 0.4f);
		validaCurvedLine(tag, x1, z1, x2, z2, pts, 0.2f);
		validaCurvedLine(tag, x1, z1, x2, z2, pts, 0.1f);
		validaCurvedLine(tag, x1, z1, x2, z2, pts, -0.1f);
		validaCurvedLine(tag, x1, z1, x2, z2, pts, -0.2f);
		validaCurvedLine(tag, x1, z1, x2, z2, pts, -0.4f);
		validaCurvedLine(tag, x1, z1, x2, z2, pts, -0.8f);
	}

	private void validaCurvedLine(String tag, int x1, int z1, int x2, int z2, int pts, float fatPerc) {
		Blueprint lineBP = em.createBlueprint(em.pickOne(tag));
		lineBP.setProperty("p0X = "+x1);
		lineBP.setProperty("p0Z = "+z1);
		lineBP.setProperty("p0Y = 0");
		lineBP.setProperty("p1X = "+x2);
		lineBP.setProperty("p1Z = "+z2);
		lineBP.setProperty("p1Y = 0");
		lineBP.setProperty("qtdPts = "+pts);
		lineBP.setProperty("midPoint = 17");
		lineBP.setProperty("fatPerc="+fatPerc);
		IGameElement finalElement = em.createFinalElement(lineBP);
		
		assertNotNull("elementoFinal Nulo",finalElement);
		/*verificaPonto(finalElement, 5, 50, 50);
		verificaPonto(finalElement, 0, 0, 0);
		verificaPonto(finalElement, 9, 90, 90);*/
		float difX=finalElement.getPropertyAsFloat("difX");
		float difY=finalElement.getPropertyAsFloat("difY");
		float difZ=finalElement.getPropertyAsFloat("difZ");
		assertTrue(difX>0);
		assertTrue(difY==0);
		assertTrue(difZ>0);
		
		List<IGameElement> points = finalElement.getObjectList("ponto");
		assertTrue("no points generated",!points.isEmpty());
		IGameElement line0 = points.get(0);
		assertNotNull("point0 Nulo",line0);
		
		float pointLimit=2;
		
		for (int i=0;i<points.size();i++){
			IGameElement point=points.get(i);
			float posX=finalElement.getParamAsFloat(Extras.LIST_POSITION, "ponto#"+i+".x");
			float posZ=finalElement.getParamAsFloat(Extras.LIST_POSITION, "ponto#"+i+".z");
			assertTrue("i:"+i+" posX<pInicialX:"+posX,posX*pointLimit>=x1);
			assertTrue("i:"+i+" posZ<pInicialZ:"+posZ,posZ*pointLimit>=z1);
			assertTrue("i:"+i+" posY>pFimX:"+posX,posX<=x2*pointLimit);
			assertTrue("i:"+i+" posZ>pFimZ:"+posZ,posZ<=z2*pointLimit);
		}
		
		validaLinhaSimples(x1,0,z1,x2,0,z2,pts,finalElement);
	}
	
	@Test public void testMultiLine() {
		String tag=tagMulti;
		int x1=0;
		int y1=0;
		int x2=100;
		int y2=100;
		int pts=10;		
		IGameElement finalElement = createLine(tag, x1, y1, x2, y2, pts);
		
		assertNotNull("elementoFinal Nulo",finalElement);
		/*verificaPonto(finalElement, 5, 50, 50);
		verificaPonto(finalElement, 0, 0, 0);
		verificaPonto(finalElement, 9, 90, 90);*/
		float midDifX=finalElement.getPropertyAsFloat("midDifX");
		float midDifY=finalElement.getPropertyAsFloat("midDifY");
		float difX=finalElement.getPropertyAsFloat("difX");
		float difY=finalElement.getPropertyAsFloat("difY");
		assertTrue(midDifX<difX);
		assertTrue(midDifY<difY);
		
		List<IGameElement> lines = finalElement.getObjectList("line");
		IGameElement line0 = lines.get(0);
		assertNotNull("line0 Nulo",line0);
		//verificaPonto(line0, 9, midDifX*9, midDifY*9);
		IGameElement line1 = lines.get(1);
		assertNotNull("line1 Nulo",line1);	
		//verificaPonto(line1, 9, 90, 90);
		
	}


	private void verificaPonto(IGameElement finalElement, int index,
			float esperadoX, float esperadoY,float esperadoZ) {
		float posX=finalElement.getParamAsFloat(Extras.LIST_POSITION, "ponto#"+index+".x");
		float posY=finalElement.getParamAsFloat(Extras.LIST_POSITION, "ponto#"+index+".y");
		float posZ=finalElement.getParamAsFloat(Extras.LIST_POSITION, "ponto#"+index+".z");
		assertTrue("X#"+index+" difere:"+esperadoX+"<>"+posX,posX==esperadoX);
		assertTrue("Y#"+index+" difere:"+esperadoY+"<>"+posY,posY==esperadoY);
		assertTrue("Z#"+index+" difere:"+esperadoZ+"<>"+posZ,posZ==esperadoZ);
	}


	private IGameElement createLine(String tag, int x1, int y1, int x2, int y2,
			int pts) {
		Blueprint lineBP = em.createBlueprint(em.pickOne(tag));
		lineBP.setProperty("p0X = "+x1);
		lineBP.setProperty("p0Y = "+y1);
		lineBP.setProperty("p0Z = 0");
		lineBP.setProperty("p1X = "+x2);
		lineBP.setProperty("p1Y = "+y2);
		lineBP.setProperty("p1Z = 0");
		lineBP.setProperty("qtdPts = "+pts);
		lineBP.setProperty("midPoint = "+(int)(pts/2));
		IGameElement finalElement = em.createFinalElement(lineBP);
		return finalElement;
	}
	
	
	
	
}
