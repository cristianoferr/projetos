package com.cristiano.galactic.gen.solarsystem;

import java.io.PrintStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.Utils.Consts;

public class SolarSystemGenerator {

	Element elroot;
	DocumentBuilderFactory factory;
	DocumentBuilder builder;


	StarGen star;
	int MIN_PLANETS=8;
	int MAX_PLANETS=MIN_PLANETS*3;
	Vector<PlanetGen> planetas;
	public static final float MAX_DISTANCE=(float)(5*PhysicsConsts.AU); //Distancia maxima do planeta

	RandomPlanetName rng;
	
	static int nextID=-1000000;
	
	
	public SolarSystemGenerator(){
		/*Vector3 testePos=new Vector3(10,0,0);
		Vector3 centro=new Vector3(0,0,0);
		Vector3 eixo=new Vector3(1,0,0);
		float ang=90;
		Vector3 res=testePos.getRotateBy(centro, eixo, ang);*/
		
		Vector3 systemPos=new Vector3(Math.pow(11, 2),Math.pow(6, 4),Math.pow(2, 5)); //seed?
		
		initializeRNG();
		star=new StarGen("Sun",systemPos);
		planetas=new Vector<PlanetGen>();
		
		/*for (int i=0;i<100;i++){
			System.out.println("i("+i+"):"+rng);
		}*/
		
		int nPlanets=MIN_PLANETS+(int) (star.getRadius()%(MAX_PLANETS-MIN_PLANETS));
		for (int i=0;i<nPlanets;i++){

			double orbitalRadius=Simplex.simplexScaledNoise(1, 0.3, 100, star.getRadius()*2, (MAX_DISTANCE-star.getRadius()*2), systemPos.x, systemPos.y, systemPos.y, i );

			String name=rng.GenPlot();
			while (!isNameOK(name)){
				name=rng.GenPlot();
			}
			PlanetGen planet=new PlanetGen(name,orbitalRadius,star);
			planet.initializePlanet(systemPos,true);

			planetas.add(planet);
		}
		
		buildXML();
		
		System.out.println("Solar System Generated");
		
	}
	
	public boolean isNameOK(String name){
		for (int i=0;i<planetas.size();i++){
			if (planetas.get(i).getName().equals(name)) return false;
		}
		return true;
	}
	
	public static int getNextID(){
		return ++nextID;
	}

	private void initializeRNG() {
		rng=new RandomPlanetName();

		
		/*rng.addSufixo("eridani");
		rng.addSufixo("circinus");
		rng.addSufixo("draco");
		rng.addSufixo("fornax");
		rng.addSufixo("gemini");
		rng.addSufixo("hydrus");
		rng.addSufixo("indus");
		rng.addSufixo("hydra");
		rng.addSufixo("lupus");
		rng.addSufixo("puppis");
		rng.addSufixo("sextans");
		rng.addSufixo("volans");
		rng.addMeio("");
		rng.addSufixo("");*/
		
	}
	
	public static void main(String[] args) {
		SolarSystemGenerator s=new SolarSystemGenerator();
	}
	
	
	  private void buildXML() {
	    	Document testDoc=startNewXML("solarsystem");
	    		
	    	Element elSun=star.buildXML(testDoc);
	    	Element elEntities = testDoc.createElement("entities");
	    	elSun.appendChild(elEntities);
	    	elroot.appendChild(elSun);
	    		for (int i=0;i<planetas.size();i++){
	    			elEntities.appendChild(planetas.get(i).buildXML(testDoc));
//	    	    		buildXMLPlaneta(testDoc,elroot,planetas.get(i));
	    	    }
	    		
	    		saveXML(Consts.rootPath + Consts.XMLPath + Consts.XMLSolarsystem,testDoc);
	    }
	  
	    private void buildXMLPlaneta(Document testDoc, Element elthis, PlanetGen planeta) {
			Element el;
			
			
			 Element elware = testDoc.createElement("astro");
/*			 elware.setAttribute("id", (int)md.getId()+"");
			 elware.setAttribute("path", md.getPath());
			 elware.setAttribute("scale", md.getScale()+"");
			 elware.setAttribute("mass", md.getMass()+"");*/

			/* 
			 Element elSlots = testDoc.createElement("slots");
			 elware.appendChild(elSlots);
			 for (int i=0;i<md.getSlots().size();i++){
				 Slot p=md.getSlots().get(i);
				 Element elSlot= testDoc.createElement("slot");
				 elSlots.appendChild(elSlot);
				 
				 elSlot.setAttribute("idWareGroup", p.getWareGroup().getId()+"");
				 elSlot.setAttribute("posX", p.getPosition().x+"");
				 elSlot.setAttribute("posY", p.getPosition().y+"");
				 elSlot.setAttribute("posZ", p.getPosition().z+"");
				 elSlot.setAttribute("defaultWare", p.getDefaultWare().getName()+"");
			     
			   //Orientation
				 Iterator<String> iterator = PhysicsConsts.orientations.keySet().iterator();
				    while (iterator.hasNext()) {
				    	String key = (String) iterator.next();
				    	Vector3 v=PhysicsConsts.orientations.get(key);
				    	if (v.equals(p.getOrientation())){
				    		elSlot.setAttribute("orientation", key);
				    	}
				     }
				 
			     
			 }
			 */
			 elthis.appendChild(elware);
			/* Element elDetails = testDoc.createElement("details");
			 elthis.appendChild(elDetails);
			 
			buildXMLDetail(testDoc, wg.getDetails(), elDetails);*/
		}


	  
	  
	  
		public Document startNewXML(String name){
			factory =DocumentBuilderFactory.newInstance();
			try {
				builder = factory.newDocumentBuilder();
			    // Here instead of parsing an existing document we want to
		        // create a new one.
		        Document testDoc = builder.newDocument();
		        Element elbase = testDoc.createElement("xml");
		        testDoc.appendChild(elbase);
		        elroot=testDoc.createElement(name);
		        elbase.appendChild(elroot);
		        return testDoc;
			}catch (Exception e) {
				Galactic.log(e.getMessage());
			} 
			return null;			
		}
		
		
		
		
		public void saveXML(String arqName,Document testDoc){
			try {
				
				// The XML document we created above is still in memory
		        // so we have to output it to a real file.
		        // In order to do it we first have to create
		        // an instance of DOMSource
		        DOMSource source = new DOMSource(testDoc);

		        // PrintStream will be responsible for writing
		        // the text data to the file
		        PrintStream ps = new PrintStream(arqName);
		        
		        
		       
	            
		        StreamResult result = new StreamResult(ps);

		        // Once again we are using a factory of some sort,
		        // this time for getting a Transformer instance,
		        // which we use to output the XML
		        TransformerFactory transformerFactory = TransformerFactory
		            .newInstance();
		        transformerFactory.setAttribute("indent-number", 80);
		        Transformer transformer = transformerFactory.newTransformer();
		        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		        // The actual output to a file goes here
		        
		        transformer.transform(source, result);
		        
			}catch (Exception e) {
				Galactic.log(e.getMessage());
			}
				
		}
}
