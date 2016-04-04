package com.cristiano.galactic.model;


import java.io.PrintStream;
import java.util.Iterator;

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
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.Entity.Logic.Slot;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.containers.FactionManager;
import com.cristiano.galactic.model.faction.AbstractFaction;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.galactic.model.wares.WareGroup;
import com.cristiano.gamelib.propriedades.Propriedades;



public class XMLBuilder {
	Element elroot;
	DataManager dataManager;
	
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	
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
	
    public XMLBuilder(DataManager dataManager) {
    	this.dataManager=dataManager;
    }
    
    public void createXMLEntities(Sistema sistema) {
		buildXMLEntities(Consts.rootPath + Consts.XMLPath + Consts.XMLEntities,sistema);
	}

    
    public void createXMLFactions() {
		buildXMLFactions(Consts.rootPath + Consts.XMLPath + Consts.XMLFactions);
	}
	public void createXML3DModels() {
		buildXMLModels3D(Consts.rootPath + Consts.XMLPath + Consts.XML3DModels);
	}
	
/*	public void createXMLProperties() {
		buildXMLProperties(Consts.rootPath + Consts.XMLPath + Consts.XMLProperties);
	}*/
	public void createXMLWareGroups() {
		buildXMLGrupos(Consts.rootPath + Consts.XMLPath + Consts.XMLWareGroups);
	}
	public void createXMLWareRefinery() {
		buildXMLRefineInto(Consts.rootPath + Consts.XMLPath + Consts.XMLWareRefinery);
	}

    private void buildXMLEntities(String xmlPath,Sistema sistema) {
    	Document testDoc=startNewXML("entitities");
    		
    		for (int i=0;i<sistema.size();i++){
    			Item item=sistema.getItem(i);
    			elroot.appendChild(item.buildXML(testDoc));
    	    }
    		
    		saveXML(xmlPath,testDoc);
    }
	

    private void buildXMLFactions(String xmlPath) {
    	Document testDoc=startNewXML("factions");
    		FactionManager factionManager=dataManager.getFactionManager();
    		for (int i=0;i<factionManager.getAllFactions().size();i++){
    			AbstractFaction faction=factionManager.getAllFactions().get(i);
    			elroot.appendChild(faction.buildXML(testDoc));
    	    }
    		
    		saveXML(xmlPath,testDoc);
    }

    
    
    private void buildXMLModels3D(String xmlPath) {
    	Document testDoc=startNewXML("models3d");
    		
    		for (int i=0;i<dataManager.getModels3D().getModels().size();i++){
    	    		buildXMLModel3D(testDoc,elroot,dataManager.getModels3D().getModels().get(i));
    	    }
    		
    		saveXML(xmlPath,testDoc);
    }
    
    private void buildXMLModel3D(Document testDoc, Element elthis, ModelData md) {
		Element el;
		
		
		 Element elware = testDoc.createElement("modelData");
		 elware.setAttribute("id", (int)md.getId()+"");
		 elware.setAttribute("path", md.getPath());
		 elware.setAttribute("scale", md.getScale()+"");
		 elware.setAttribute("mass", md.getMass()+"");

		 
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
		 
		 elthis.appendChild(elware);
		/* Element elDetails = testDoc.createElement("details");
		 elthis.appendChild(elDetails);
		 
		buildXMLDetail(testDoc, wg.getDetails(), elDetails);*/
	}



	private void buildXMLWare(Document testDoc, Element elthis, Ware ware) {
		Element el;
		
		 Element elware = testDoc.createElement("ware");
		 
		 elware.setAttribute("id", Integer.toString(ware.getId()));
		 elware.setAttribute("name", ware.getName());
		 elware.setAttribute("idGrupo", Integer.toString(ware.getGrupo().getId()));
		 elware.setAttribute("volume",  Double.toString(ware.getVol()));
		 elware.setAttribute("qtdMinRefine",  Integer.toString(ware.getMinAmountRefine()));

		 elthis.appendChild(elware);
//	         refineTo
		 
		// buildXMLRefineInto(testDoc, wg, elware);
		 
		 Element elDetails = testDoc.createElement("properties");
		 elware.appendChild(elDetails);
		 
		buildXMLPropertyValue(testDoc, ware.getProps(), elDetails);
	}


	private void buildXMLRefineInto(String xmlPath) {
		Document testDoc=startNewXML("refinery");
		Element el;
		Element elRefineTo = testDoc.createElement("refinesInto");
		 elroot.appendChild(elRefineTo);
		 
		 Iterator<String> iterator = dataManager.getWareManager().getWares().keySet().iterator();
		    while (iterator.hasNext()) {
		    	String key = (String) iterator.next();
		    	Ware wg=dataManager.getWareManager().getWares().get(key);
		    	
		    	Propriedades refineTo=wg.getRefineTo();
		    	Iterator<String> iteratorDetail = refineTo.getAllProps().keySet().iterator();
				while (iteratorDetail.hasNext()) {
					String keyRefine=(String) iteratorDetail.next();
					int qtd=refineTo.getPropertyAsInt(keyRefine);

					Element elRefine= testDoc.createElement("refineInto");
					 elRefineTo.appendChild(elRefine);
					 Ware ware=dataManager.getWare(keyRefine);
					 el= testDoc.createElement("idWareFrom");
				     el.setTextContent(Integer.toString(wg.getId()));
				     elRefine.appendChild(el);
				     el= testDoc.createElement("nameWareFrom");
				     el.setTextContent(wg.getName());
				     elRefine.appendChild(el);
					 el= testDoc.createElement("idWareTo");
				     el.setTextContent(Integer.toString(ware.getId()));
				     elRefine.appendChild(el);
				     el= testDoc.createElement("nameWareTo");
				     el.setTextContent(ware.getName());
				     elRefine.appendChild(el);
				     el= testDoc.createElement("qtd");
				     el.setTextContent(Double.toString(qtd));
				     elRefine.appendChild(el);
					 
				 }
		 }
		    saveXML(xmlPath,testDoc);
	}
/*
	private void buildXMLProperties(String xmlPath) {
		Document testDoc=startNewXML("properties");
		
		buildXMLProperty(testDoc,dataManager.getPropertyManager().getProperties(),elroot);
		saveXML(xmlPath,testDoc);
	}
	
	private void buildXMLProperty(Document testDoc, HashMap<String,Property> hashMap, Element elDetails) {
	//	elDetails.appendChild(testDoc.createElement("details"));
		
		Element el;
		Iterator<String> iteratorDetail = hashMap.keySet().iterator();
		while (iteratorDetail.hasNext()) {
			Property p=hashMap.get((String) iteratorDetail.next());
		 
			 Element elDetail= testDoc.createElement("property");
			 
			 elDetail.setAttribute("id", Integer.toString(p.getId()));
			 elDetail.setAttribute("name", p.getName());
			 
		     if (p.getValor()!=0){
		    	 elDetail.setAttribute("vlrPadrao", Double.toString(p.getValor()));
		     }
		
		     elDetails.appendChild(elDetail);
			}
	}*/
	
	private void buildXMLPropertyValue(Document testDoc, Propriedades props, Element elDetails) {
		//	elDetails.appendChild(testDoc.createElement("details"));
			
			//Element el;
			Iterator<String> iteratorDetail = props.getAllProps().keySet().iterator();
			while (iteratorDetail.hasNext()) {
				//PropertyValue p=props.get((String) iteratorDetail.next());
				String key = (String) iteratorDetail.next();
				String d=props.getPropertyAsString(key);
			 
				 Element elDetail= testDoc.createElement("property");
				 
				 //elDetail.setAttribute("id", Integer.toString(p.getId()));
				 elDetail.setAttribute("name", key);
				 
			     
			     elDetail.setAttribute("vlrPadrao", d);
				
			     elDetails.appendChild(elDetail);
				}
		}
	
	private void buildXMLGrupos(String xmlPath) {
		Document testDoc=startNewXML("waregroups");
		
		
		Iterator iterator = dataManager.getWareManager().getWareGroups().keySet().iterator();
	    while (iterator.hasNext()) {
	    	String key = (String) iterator.next();
	    	WareGroup wg=dataManager.getWareManager().getWareGroups().get(key);
	    	if (wg.getGrupoPai()==null){
	    		buildXMLGrupo(testDoc,wg,elroot);
	    	}
	    }
	    saveXML(xmlPath,testDoc);
	}
	
private void buildXMLGrupo(Document testDoc,WareGroup wg,Element elthis)  {
	// This creates a new tag named 'testElem' inside
    // the document and sets its data to 'TestContent'
	Element el;
	//Iterator iterator;
	
	
		Element elgroup = testDoc.createElement("group");
		
		elgroup.setAttribute("id", Integer.toString(wg.getId()));
		elgroup.setAttribute("name", wg.getName());

		
		if (wg.getGrupoPai()!=null){
			elgroup.setAttribute("idGrupoPai", Integer.toString(wg.getGrupoPai().getId()));
		}
		elthis.appendChild(elgroup);
		
		
		Element elDetails = testDoc.createElement("properties");
		elgroup.appendChild(elDetails);
		buildXMLPropertyValue(testDoc, wg.getProps(), elDetails);
		
		Element elWares= testDoc.createElement("wares");
		elgroup.appendChild(elWares);
		for (int i=0;i<wg.getWares().size();i++){
			buildXMLWare(testDoc,elWares,wg.getWares().get(i));
		
		}
		
		if (wg.getDefaultWare()!=null){
			elgroup.setAttribute("idWareDefault", Integer.toString(wg.getDefaultWare().getId()));
			elgroup.setAttribute("nameWareDefault", wg.getDefaultWare().getName());
		}


		
		
		
		for (int i=0;i<wg.getGruposFilhos().size();i++){
			buildXMLGrupo(testDoc,wg.getGruposFilhos().get(i),elgroup);
	    }
    
	}

	
}
