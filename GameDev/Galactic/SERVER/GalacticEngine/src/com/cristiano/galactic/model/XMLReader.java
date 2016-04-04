package com.cristiano.galactic.model;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Formatacao;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.Entity.Ship;
import com.cristiano.galactic.model.Entity.Star;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.FactionType;
import com.cristiano.galactic.model.faction.NPCFaction;
import com.cristiano.galactic.model.faction.PlayerFaction;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.galactic.model.wares.WareGroup;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;


public class XMLReader {
	
	private DataManager dataManager;
	 public XMLReader(DataManager dataManager) {
		 this.dataManager=dataManager;
		 
	        
    }


	public void loadXML(Sistema world) {
		//parseDetails(loadXML(Consts.rootPath+Consts.XMLPath+Consts.XMLProperties));
		Galactic.log("Loading Groups...");
		parseGroups(loadXML(Consts.rootPath+Consts.XMLPath+Consts.XMLWareGroups));
		Galactic.log("Loading Refines into...");
		parseRefinesInto(loadXML(Consts.rootPath+Consts.XMLPath+Consts.XMLWareRefinery));
		Galactic.log("Loading Models 3D...");
		parseModels3D(loadXML(Consts.rootPath+Consts.XMLPath+Consts.XML3DModels));
		Galactic.log("Loading Factions...");
		parseFactions(loadXML(Consts.rootPath+Consts.XMLPath+Consts.XMLFactions));
		
        if (Consts.LOAD_XML_ENTITIES){
        	Galactic.log("Loading Artificial Entities...");
        	parseEntities(world,loadXML(Consts.rootPath+Consts.XMLPath+Consts.XMLEntities));
        	Galactic.log("Loading Astronomical Entities...");
        	parsePlanets(world,loadXML(Consts.rootPath+Consts.XMLPath+Consts.XMLSolarsystem));
        }
        
        
        
	}


	private void parseFactions(Element rootElement) {
		//Here we get a list of all elements named 'child'
       // NodeList list = rootElement.getElementsByTagName("entities");
        Node node=rootElement.getFirstChild().getFirstChild();
        while (node!=null){
        	parseFaction(node);
        	node=node.getNextSibling();
        }
	}	
	
	private void parseFaction( Node node) {
		int id=0;
		String type="",name="";
		
		
		NamedNodeMap atrs=node.getAttributes();
		
		id=Integer.parseInt(atrs.getNamedItem("id").getNodeValue());
		type=atrs.getNamedItem("type").getNodeValue();
		name=atrs.getNamedItem("name").getNodeValue();
		if (type.equals(FactionType.NPC_CORP.toString())){
			NPCFaction obj=NPCFaction.createFromXML(id,name,node,dataManager);
			dataManager.getFactionManager().addFaction(obj);
		} else if (type.equals(FactionType.PLAYER.toString())){
			PlayerFaction obj=PlayerFaction.createFromXML(id,name,node,dataManager);
			dataManager.getFactionManager().addPlayer(obj);
		} else {
			Galactic.throwError("Error: Faction type not specified.");
		}
		

	}
	
	
	 private void parsePlanets(Sistema world, Element rootElement) {
			//Here we get a list of all elements named 'child'
	       // NodeList list = rootElement.getElementsByTagName("entities");
	        Node node=rootElement.getFirstChild().getFirstChild();
	        while (node!=null){
	        	parsePlanet(world,node);
	        	node=node.getNextSibling();
	        }
		}	
	 
		private void parsePlanet(Sistema world, Node node) {
			int id=0;
			String type="",name="";
			
			
			NamedNodeMap atrs=node.getAttributes();
			
			id=Integer.parseInt(atrs.getNamedItem(PropriedadesObjeto.PROP_ID.toString()).getNodeValue());
			type=atrs.getNamedItem(PropriedadesObjeto.PROP_TYPE.toString()).getNodeValue();
			name=atrs.getNamedItem(PropriedadesObjeto.PROP_NAME.toString()).getNodeValue();
			if (type.equals(EntityType.ET_STAR.toString())){
				Star.createFromXML(id,name,node,world.getDataManager(),world);
			} else if (type.equals(EntityType.ET_PLANET.toString())){
				Galactic.throwError("Lost Planet...");
				
			}
			
			

		}
 
	 private void parseEntities(Sistema world, Element rootElement) {
			//Here we get a list of all elements named 'child'
	       // NodeList list = rootElement.getElementsByTagName("entities");
	        Node node=rootElement.getFirstChild().getFirstChild();
	        while (node!=null){
	        	parseEntity(world,node);
	        	node=node.getNextSibling();
	        }
		}	
	 
	 
		private void parseEntity(Sistema world, Node node) {
			int id=0;
			String type="",name="";
			
			
			NamedNodeMap atrs=node.getAttributes();
			
			id=Integer.parseInt(atrs.getNamedItem("id").getNodeValue());
			type=atrs.getNamedItem("type").getNodeValue();
			name=atrs.getNamedItem("name").getNodeValue();
			if (type.equals(EntityType.ET_SHIP.toString())){
				Ship.createFromXML(id,name,node,world.getDataManager(),world);
				
			}
			

		}
		
	 

	private Element loadXML(String arqName) {
		try {
        //The two lines below are just for getting an
        //instance of DocumentBuilder which we use
        //for parsing XML data
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
		
			builder = factory.newDocumentBuilder();
		

        //Here we do the actual parsing
			
        Document doc = builder.parse(new File(arqName));

        
     // normalize text representation
        doc.getDocumentElement ().normalize ();
        
        //Here we get the root element of XML and print out
        //the value of its 'testAttr' attribute
        Element rootElement = doc.getDocumentElement();
        removeEmptyTextNodes(rootElement);
        return rootElement;
        
		 } catch (ParserConfigurationException e) {
				
			 Galactic.log(e.getMessage());
			} catch (SAXException e) {
			
				Galactic.log(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void removeEmptyTextNodes(Node node) {
	    NodeList nodeList = node.getChildNodes();
	    Node childNode;
	    for (int x = nodeList.getLength() - 1; x >= 0; x--) {
	        childNode = nodeList.item(x);
	        if (childNode.getNodeType() == Node.TEXT_NODE) {
	            if (childNode.getNodeValue().trim().equals("")) {
	                node.removeChild(childNode);
	            }
	        } else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
	            removeEmptyTextNodes(childNode);
	        }
	    }
	}


	 
	/*
		private void parseDetails(Element rootElement) {
			//Here we get a list of all elements named 'child'
	        NodeList list = rootElement.getElementsByTagName("properties");
	        
 
	        //Traversing all the elements from the list and printing
	        //out its data
	        
	        for (int i = 0; i < list.getLength(); i++) {
	            //Getting one node from the list.
	            //BTW, we used method getElementsByTagName so every entry
	            //in the list is effectively of type 'Element', so you could
	            //cast it directly to 'Element' if you needed to.
	            Node childNode = list.item(i);
	            for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
	            	Node node=childNode.getChildNodes().item(j);
	            	parseDetail(node);
	            }
	            
	        }
		}*/
		private void parseModels3D(Element rootElement) {
			//Here we get a list of all elements named 'child'
	        NodeList list = rootElement.getElementsByTagName("models3d");

	        //Traversing all the elements from the list and printing
	        //out its data
	        for (int i = 0; i < list.getLength(); i++) {
	            //Getting one node from the list.
	            //BTW, we used method getElementsByTagName so every entry
	            //in the list is effectively of type 'Element', so you could
	            //cast it directly to 'Element' if you needed to.
	            Node childNode = list.item(i);
	            for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
	            	Node node=childNode.getChildNodes().item(j);
	            	parseModel3D(node);
	            }
	            
	        }
		}	


		private void parseModel3D(Node node) {
			int id=0;
			String path="",geom="BOX";
			float scale=1;
			double density=1;
			ModelData md=null;
			double mass=0;
			
			
			String nameWareTo="";
			NamedNodeMap atrs=node.getAttributes();
			
			id=Integer.parseInt(atrs.getNamedItem("id").getNodeValue());
			path=atrs.getNamedItem("path").getNodeValue();
			scale=Float.parseFloat(atrs.getNamedItem("scale").getNodeValue());
			mass=Double.parseDouble(atrs.getNamedItem("mass").getNodeValue());

			for (int j = 0; j < node.getChildNodes().getLength(); j++) {
				Node n=node.getChildNodes().item(j);
				if (n.getNodeName().equals("slots")){
					md=new ModelData(dataManager,id,path,mass);
					md.setScale(scale);
	/*				md.setTamX(tamX);
					md.setTamY(tamY);
					md.setTamZ(tamZ);*/
					//if (geom.equals("BOX")) md.setPrimType(PrimitiveType.BOX);
					//if (geom.equals("SPHERE")) md.setPrimType(PrimitiveType.SPHERE);
					
					for (int i = 0; i < n.getChildNodes().getLength(); i++) {
		            	Node n2=n.getChildNodes().item(i);
		            	parseModelSlot(n2,md);
					}
					dataManager.getModels3D().addModelData( md);
					md.createSlotGrouping();
				}
			}
		}
		
		
		private void parseModelSlot(Node node,ModelData md) {
			int idWareGroup=0;
			double posX=0,posY=0,posZ=0;
			String defaultWare="";
			String orientation="";
			
			NamedNodeMap atrs=node.getAttributes();
			idWareGroup=Integer.parseInt(atrs.getNamedItem("idWareGroup").getNodeValue());
			posX=Double.parseDouble(atrs.getNamedItem("posX").getNodeValue());
			posY=Double.parseDouble(atrs.getNamedItem("posY").getNodeValue());
			posZ=Double.parseDouble(atrs.getNamedItem("posZ").getNodeValue());
			defaultWare=atrs.getNamedItem("defaultWare").getNodeValue();
			orientation=atrs.getNamedItem("orientation").getNodeValue();
			
			md.addWareGroupAtPos(dataManager.getWareManager().getWareGroup(idWareGroup),new Vector3(posX,posY,posZ),PhysicsConsts.orientations.get(orientation),false,dataManager.getWareManager().getWare(defaultWare));
			
			
		}

		
		private void parseRefinesInto(Element rootElement) {
			//Here we get a list of all elements named 'child'
	        NodeList list = rootElement.getElementsByTagName("refinesInto");

	        //Traversing all the elements from the list and printing
	        //out its data
	        for (int i = 0; i < list.getLength(); i++) {
	            //Getting one node from the list.
	            //BTW, we used method getElementsByTagName so every entry
	            //in the list is effectively of type 'Element', so you could
	            //cast it directly to 'Element' if you needed to.
	            Node childNode = list.item(i);
	            for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
	            	Node node=childNode.getChildNodes().item(j);
	            	parseRefineInto(node);
	            }
	            
	        }
		}		
		
		private void parseRefineInto(Node node) {
			double qtd=0;
			String nameWareFrom="";
			String nameWareTo="";
			for (int j = 0; j < node.getChildNodes().getLength(); j++) {
				Node n=node.getChildNodes().item(j);
				if (n.getNodeName().equals("nameWareFrom")) {
					nameWareFrom=n.getTextContent();
				}
				if (n.getNodeName().equals("nameWareTo")) {
					nameWareTo=n.getTextContent();
				}
				if (n.getNodeName().equals("qtd")) {
					qtd=Double.parseDouble(n.getTextContent());
				}
			}
			dataManager.getWareManager().getWare(nameWareFrom).addRefineTo(dataManager.getWareManager().getWare(nameWareTo), (int)qtd);
			
				 
		}
				
	private void parseGroups(Element rootElement) {
		//Here we get a list of all elements named 'child'
        NodeList list = rootElement.getElementsByTagName("waregroups");

        //Traversing all the elements from the list and printing
        //out its data
        for (int i = 0; i < list.getLength(); i++) {
            //Getting one node from the list.
            //BTW, we used method getElementsByTagName so every entry
            //in the list is effectively of type 'Element', so you could
            //cast it directly to 'Element' if you needed to.
            Node childNode = list.item(i);
            for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
            	Node node=childNode.getChildNodes().item(j);
            	parseGroup(node,null);
            }
            
        }
	}

	private void parseGroup(Node node,WareGroup wg) {
		
		int id=0,idPai=0;
		String name="";
		WareGroup grupo=null;
		

		
		
		for (int j = 0; j < node.getChildNodes().getLength(); j++) {
			Node n=node.getChildNodes().item(j);
			if (n.getNodeName().equals("properties")){
				
				NamedNodeMap atrs=node.getAttributes();
				id=Integer.parseInt(atrs.getNamedItem("id").getNodeValue());
				name=atrs.getNamedItem("name").getNodeValue();
				//Galactic.printDebug("Grupo:"+name);
				if (atrs.getNamedItem("idGrupoPai")!=null)
					idPai=Integer.parseInt(atrs.getNamedItem("idGrupoPai").getNodeValue());

				grupo=new WareGroup(id,dataManager,name,wg);
				
				if (atrs.getNamedItem("idWareDefault")!=null) {
					grupo.setIdDefaultWare(Integer.parseInt(atrs.getNamedItem("idWareDefault").getNodeValue()));
				}
				
				//grupo.setId(id);
				for (int i = 0; i < n.getChildNodes().getLength(); i++) {
	            	Node n2=n.getChildNodes().item(i);
	            	parseGroupProperty(n2,grupo,null);
				}
            }
			if (n.getNodeName().equals("wares"))
				for (int i = 0; i < n.getChildNodes().getLength(); i++) {
	            	Node n2=n.getChildNodes().item(i);
	            	parseGroupWare(n2,grupo);
				}
			if (n.getNodeName().equals("group")){
				parseGroup(n,grupo);
			}
			
			
	//		WareGroup grupo=new WareGroup();	
		}
		dataManager.getWareManager().addWareGroup(grupo);
//		node.getNodeName()+
	}
	/*
	private void parseDetail(Node node) {
		int id=0;
		double aux=0;
		String name="";
		
		NamedNodeMap atrs=node.getAttributes();
		id=Integer.parseInt(atrs.getNamedItem("id").getNodeValue());
		name=atrs.getNamedItem("name").getNodeValue();
		
		Property d=new Property(id,name);
		//d.setId(id);
		if (atrs.getNamedItem("vlrPadrao")!=null){
			aux=Integer.parseInt(atrs.getNamedItem("vlrPadrao").getNodeValue());
			d.setValor(aux);
		}
		
		dataManager.getPropertyManager().addProperty(d);
		
			 
	}*/
	
	private void parseGroupProperty(Node node,WareGroup wg,Ware w) {
		int id=0;
		String aux="";
		String name="";
		
		NamedNodeMap atrs=node.getAttributes();
		
		//id=Integer.parseInt(atrs.getNamedItem(PropriedadesObjeto.PROP_ID.toString()).getNodeValue());
		name=atrs.getNamedItem("name").getNodeValue();
		if (atrs.getNamedItem("vlrPadrao")!=null){
			
			aux=atrs.getNamedItem("vlrPadrao").getNodeValue();
			if (Formatacao.isInteger(aux)){
				if (wg!=null)wg.setProperty(name, Integer.parseInt(aux));
				if (w!=null)w.setProperty(name, Integer.parseInt(aux));
				
			}else if (Formatacao.isNumber(aux)){
				if (wg!=null)wg.setProperty(name, Double.parseDouble(aux));
				if (w!=null)w.setProperty(name, Double.parseDouble(aux));
				
			} else {
				if (wg!=null)wg.setProperty(name, aux);
				if (w!=null)w.setProperty(name, aux);
				
			}
			
		}
		
			 
	}
	private void parseGroupWare(Node node,WareGroup wg) {
		int id=0,qtdMinRefine=1;
		double vol=0;
		String name="";
		Ware ware=null;

		
		for (int j = 0; j < node.getChildNodes().getLength(); j++) {
			Node n=node.getChildNodes().item(j);
			if (n.getNodeName().equals("properties")){
				
				NamedNodeMap atrs=node.getAttributes();
				id=Integer.parseInt(atrs.getNamedItem("id").getNodeValue());
				name=atrs.getNamedItem("name").getNodeValue();
				vol=Double.parseDouble(atrs.getNamedItem("volume").getNodeValue());
				qtdMinRefine=Integer.parseInt(atrs.getNamedItem("qtdMinRefine").getNodeValue());
				
				ware=new Ware(id,dataManager,name,vol,wg);
				ware.setMinAmountRefine(qtdMinRefine);
				//Galactic.printDebug("parseGroupWare:"+ware+" ==>"+name);
				//ware.setId(id);
				dataManager.getWareManager().addWare(ware, false);
				for (int i = 0; i < n.getChildNodes().getLength(); i++) {
	            	Node n2=n.getChildNodes().item(i);
	            	parseGroupProperty(n2,null,ware);
				}
            }
	//		}
			
		}
		
	}
}
