package com.cristiano.cyclone.math;

import java.util.HashMap;

import com.cristiano.cyclone.utils.Vector3;


public class PhysicsConsts {
	static public final float deg2rad=0.0174532925f;
	static public final double Gforce=(float)(6.6742*Math.pow(10, -11));
	static public final double KM=1000;
	static public final double MKM=1000*KM;
	static public final double AU=150000*MKM;
	static public final double C=300*MKM;
	static public final double massaKG=1000;
	static public final double massaTON=1000*massaKG;
	static public final double massaKTON=1000*massaTON;
	static public final double massaMTON=1000*massaKTON;
	static public final double massaGTON=1000*massaMTON;

	static public final double massaTERRA=(5.9736*Math.pow(10,24)*massaKG);
	static public final double massaSOL=(1.9891*Math.pow(10,30)*massaKG);
	static public final double massaLUA=(7.3477*Math.pow(10,22)*massaKG);

	static public final double raioSOL=1.392E6*KM;
	static public final double raioTERRA=(6.38*Math.pow(10,6));
	static public final double raioLUA=(float)1737*KM; 

	static public final double gravityThreshold=(Math.pow(10,14)*massaKG);

	static public final double massaPADRAO=massaKTON;
	static public final double relRaioMassa=raioTERRA/massaTERRA;

	//static public final float raioLUA=massaLUA*relRaioMassa;

	
	static public final Vector3 ORIENT_NORTH=new Vector3(1,0,0);
	static public final Vector3 ORIENT_SOUTH=new Vector3(-1,0,0);
	static public final Vector3 ORIENT_WEST=new Vector3(0,0,-1);//Esquerda
	static public final Vector3 ORIENT_EAST=new Vector3(0,0,1);//Direita
	static public final Vector3 ORIENT_UP=new Vector3(0,1,0);
	static public final Vector3 ORIENT_DOWN=new Vector3(0,-1,0);
	public static HashMap<String,Vector3>orientations= new HashMap<String,Vector3>();
	
	static{
		orientations.put("NORTH",PhysicsConsts.ORIENT_NORTH);
		orientations.put("SOUTH",PhysicsConsts.ORIENT_SOUTH);
		orientations.put("WEST",PhysicsConsts.ORIENT_WEST);
		orientations.put("EAST",PhysicsConsts.ORIENT_EAST);
		orientations.put("UP",PhysicsConsts.ORIENT_UP);
		orientations.put("DOWN",PhysicsConsts.ORIENT_DOWN);
	}
	
}
