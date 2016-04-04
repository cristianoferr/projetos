package com.cristiano.cyclone.utils;

import com.cristiano.cyclone.math.PhysicsConsts;



public abstract class Formatacao {
	private static int casas=2;
	
	public static String format(double value){
		double v=MathUtils.Round(value,casas);
		return v+"";
	}
	
	
	public static String formatDistance(double value){
		String r=" m";
		if (Math.abs(value)>PhysicsConsts.AU){
			
			r=" AU";
			return format(value/PhysicsConsts.AU)+r;
		}
		if (Math.abs(value)>PhysicsConsts.C){
			r=" lightSeconds";
			return format(value/PhysicsConsts.C)+r;
		}
		if (Math.abs(value)>PhysicsConsts.MKM){
			r=" kkm";
			return format(value/PhysicsConsts.MKM)+r;
		}
		if (Math.abs(value)>PhysicsConsts.KM){
			r=" km";
			return format(value/PhysicsConsts.KM)+r;
		}
		
		
		return format(value)+r;
	}
	
	public static String formatSpeed(double value){
	
		return formatDistance(value)+"/s";
	}
	
	
	public static String formatMass(double value){
		String r=" g";

		if (Math.abs(value)>PhysicsConsts.massaSOL){
			r=" Sois";
			return format(value/PhysicsConsts.massaSOL)+r;
		}
		if (Math.abs(value)>PhysicsConsts.massaTERRA){
			r=" Terras";
			return format(value/PhysicsConsts.massaTERRA)+r;
		}
		if (Math.abs(value)>PhysicsConsts.massaLUA){
			r=" Luas";
			return format(value/PhysicsConsts.massaLUA)+r;
		}
		if (Math.abs(value)>PhysicsConsts.massaGTON){
			
			r=" gigaTon";
			return format(value/PhysicsConsts.massaGTON)+r;
		}
		if (Math.abs(value)>PhysicsConsts.massaMTON){
			r=" megaTon";
			return format(value/PhysicsConsts.massaMTON)+r;
		}
		if (Math.abs(value)>PhysicsConsts.massaKTON){
			r=" kiloTon";
			return format(value/PhysicsConsts.massaKTON)+r;
		}
		if (Math.abs(value)>PhysicsConsts.massaTON){
			r=" ton";
			return format(value/PhysicsConsts.massaTON)+r;
		}
		if (Math.abs(value)>PhysicsConsts.massaKG){
			r=" kg";
			return format(value/PhysicsConsts.massaKG)+r;
		}
		
		
		return format(value)+r;
	}
	
	
	
	public static String format(Vector3 v){
		return "X="+format(v.x)+" Y="+format(v.y)+" Z="+format(v.z);
	}
	
	public static String formatMass(Vector3 v){
		return "X="+formatMass(v.x)+" Y="+formatMass(v.y)+" Z="+formatMass(v.z);
	}
	public static String formatDistance(Vector3 v){
		return "X="+formatDistance(v.x)+" Y="+formatDistance(v.y)+" Z="+formatDistance(v.z);
	}
	
	public static String format(Quaternion v){
		return "I="+format(v.i)+" J="+format(v.j)+" K="+format(v.k)+" R="+format(v.r);
	}
}
