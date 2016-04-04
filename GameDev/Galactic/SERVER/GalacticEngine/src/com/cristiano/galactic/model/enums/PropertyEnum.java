package com.cristiano.galactic.model.enums;


public enum PropertyEnum {
	DTL_MODEL_ID,//ID do modelo
	DTL_BASE_TURN,//Tempo que leva para a volta dar uma volta em si mesmo
	DTL_THRUST_POWER_TEMP,//TODO: Substituir essa propriedade por uma calculada
	DTL_ENERGY_USE_SEC,
	DTL_AUTO_ACTIVATE, //1=User Activated (like a laser shot) 0=Passive, like shield recharge
	DTL_LASER_ENERGY_SHOT,
	DTL_COOLDOWN,
	DTL_DMG_SHIELD,
	DTL_DMG_HULL,
	DTL_RANGE,
	DTL_WARE_CLASS,
	DTL_WARE_MIN_PRICE,
	DTL_WARE_MAX_PRICE,
	DTL_CURR_VALUE,
	DTL_MAX_VALUE,
	DTL_SPEED,
	DTL_BLAST_RADIUS,
	DTL_CARGO_HOLD, //Propriedades  com tipo >String <int:
	DTL_REFINE_INTO //Propriedades  com tipo >String <int:

;

	

	public static String getCurr(String prop){
		return "DTL_CURR_"+ prop;
	}
	
	public static String getMax(String prop){
		return "DTL_CURR_MAX_"+ prop;
	}
	
	public static String getMult(String prop){
		return "DTL_MULT_"+ prop;
	}
	
	public static String getBase(String prop){
		return "DTL_BASE_"+ prop;
	}
	
	public static String getCurr(ItemGameProperties prop){
		return "DTL_CURR_"+ prop.toString();
	}
	
	public static String getMax(ItemGameProperties prop){
		return "DTL_CURR_MAX_"+ prop.toString();
	}
	
	public static String getMult(ItemGameProperties prop){
		return "DTL_MULT_"+ prop.toString();
	}
	
	public static String getBase(ItemGameProperties prop){
		return "DTL_BASE_"+ prop.toString();
	}
	
	
}
