package com.cristiano.galactic.Utils;


import java.io.File;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.galactic.Galactic;


public class Consts {
	
	public static final String GRP_CAMERA="GRP_CAMERA";
	public static final String WARE_CAMERA="WARE_CAMERA";
	
	public static final double SYSTEM_MAX_WIDTH=10000000000000f;
	public static final double SYSTEM_MAX_HEIGHT=100*PhysicsConsts.KM;

	public static final double PLANET_MIN_RADIUS=PhysicsConsts.raioTERRA*0.3f;
	public static final double PLANET_MAX_RADIUS=PhysicsConsts.raioTERRA*20;

	
	public static final String GAMELIB_PATH="com/cristiano/gamelib/";
	
	public static final String BASIC_AMMO="Basic AMMO";
	
	/*Número "mágico", basicamente ele me diz que eu preciso estar a 700 unidades (metros)
	 * para ver uma unidade ocupando 1 pixel... usado para calcular a visibilidade dos objetos. 
	 */
	
	public static final int MAXDISTANCE_CAMERA=1000;
	
	
	public static final int UNIT2PIXEL=700;
	
	//public static double shipsDefaultDensity=1000*PhysicsConsts.massaKG;//Usado para determinar a massa por M^3 das naves.
	
	public static final String rootPath="../Galactic/";
	public static String absolutePath="../Galactic/";
	
	
	public static final String recursosPath="assets";
	
	public static final String XMLFile="dados.xml";
	public static final String XMLPath="db/";
	//public static final String XMLProperties="details.xml";
	public static final String XMLWareGroups="Grupos.xml";
	public static final String XMLWareRefinery="WareRefinery.xml";
	public static final String XML3DModels="3DModels.xml";
	public static final String XMLFactions="factions.xml";
	
	public static final String XMLEntities="entities.xml";
	public static final String XMLSolarsystem="solarsystem.xml";
	//static public final int scale=1;


	public static final String niftyHUD=rootPath+recursosPath+"/hud/NiftyHUD.xml";

		//F= G*((m1*m2)/r^2)
	//F=mg
	//g=F/m   9.8 m/s^2
	public static final double initPos=PhysicsConsts.AU+PhysicsConsts.C-PhysicsConsts.MKM*5;
    public static final float initForce=    0f;

	
	

	public static final String CTL_GRP_ENGINE="GRP_ENGINE";
	public static final String CTL_GRP_LASER = "GRP_LASER";
	public static final String CTL_GRP_WEAPON = "GRP_WEAPON";
	
	
	
	//Nomes de grupos de wares para facil acesso
	public static final String WARE_GROUP_ENGINE="Engine";
	


	
	
	

	static public final int CND_START_NOW=0;
	static public final int CND_AFTER_CURRENT_MISSION=1;
	static public final int CND_INTERRUPT_CURRENT_MISSION=2;
	public static final boolean LOAD_XML_ENTITIES = true;//False: "Sistema" insere entidades automaticamente TRUE: XML é carregado 
	
	public static void initPath(){
		File f1 = new File (Consts.rootPath);
		 try {
		       System.out.println ("Current directory : " + f1.getCanonicalPath());
		       absolutePath="file:/"+f1.getCanonicalPath().replace("\\", "/")+"/";
		       }
		     catch(Exception e) {
	            Galactic.log(e.getMessage());

		       
		       }
		   /*  recursosPath="/assets";
		     XMLFile="/dados.xml";
		 	 XMLPath="/db/";
		 	 XMLDetails="details.xml";
		 	 XMLWareGroups="Grupos.xml";
		 	 XMLWareRefinery="WareRefinery.xml";
		 	 XML3DModels="3DModels.xml";*/
		}
	
	
	
}
