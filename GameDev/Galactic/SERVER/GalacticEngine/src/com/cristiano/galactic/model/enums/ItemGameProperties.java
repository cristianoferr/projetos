package com.cristiano.galactic.model.enums;

import java.util.Vector;

/**
 * Enum que substitui o PropertyManager
 * Essas propriedades são diferentes porque preciso calcular o valor base,
 * máximo, multiplicado, etc.
 * 
 * @author cmm4
 *
 */
public enum ItemGameProperties {
	CARGO,//capacidade de carga base para a nave
	ARMOR,//armor base para a nave
	SHIELD,//shield base para a nave
	SHIELDREGEN,//tempo que leva para ir de 0 a 100% o shield
	CAPACITOR,//quanto de energia o capacitor possui
	CAPREGEN,//quanto tempo para o capacitor ir de 0 a 100%
	TURN, //quanto tempo leva para dar uma volta
	PRICE,//Preço base
	ACCEL//Aceleracao base para as naves
	;
	
	public static Vector<String> getAll(){
		Vector<String> v=new Vector<String>();
		v.add(CARGO.toString());
		v.add(ARMOR.toString());
		v.add(SHIELD.toString());
		v.add(SHIELDREGEN.toString());
		v.add(CAPACITOR.toString());
		v.add(TURN.toString());
		v.add(PRICE.toString());
		v.add(ACCEL.toString());
		
		return v;
	}
}
