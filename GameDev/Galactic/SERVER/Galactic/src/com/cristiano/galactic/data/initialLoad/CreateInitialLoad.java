package com.cristiano.galactic.data.initialLoad;

import com.cristiano.galactic.model.XMLBuilder;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.containers.FactionManager;

public class CreateInitialLoad {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DataManager dm=new DataManager();
		
		FactionManager fm=dm.getFactionManager();
		fm.addPlayer("Player1");
		fm.addNPCFaction("Argon");
		fm.addNPCFaction("Boron");
		fm.addNPCFaction("Split");
		fm.addNPCFaction("Teladi");
		fm.addNPCFaction("Terran");
		fm.addNPCFaction("Xenon");
		fm.addNPCFaction("Paranid");
		
		WareGroupsLoad wg=new WareGroupsLoad(dm);
		Models3DLoad m3D=new Models3DLoad(dm);
		
		XMLBuilder xmlBuilder=new XMLBuilder(dm);
		
		xmlBuilder.createXML3DModels();
		//xmlBuilder.createXMLProperties();
		xmlBuilder.createXMLWareGroups();
		xmlBuilder.createXMLWareRefinery();
		xmlBuilder.createXMLFactions();

		System.out.println("XML Generated");
	}

}
