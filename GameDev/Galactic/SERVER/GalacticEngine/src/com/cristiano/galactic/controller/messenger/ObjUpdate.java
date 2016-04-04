package com.cristiano.galactic.controller.messenger;


import java.util.Date;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.Entity.Abstract.Item;


public class ObjUpdate  {
	private Item from;
	private Vector3 coord;
	private int ttl;
	private Date horaEnvio=new Date();
	private int qtdReached=0;//How many players this message has reached? If qty=amount then delete the message
	
	/**
	 * @return the qtdReached
	 */
	public int getQtdReached() {
		return qtdReached;
	}
public void addQtdReached(){
	qtdReached++;
}


	public ObjUpdate(Item from){
		this.from=from;
		this.coord=from.getCoord().clone();
		ttl=10000;
	}
	
	
	
	public Item getFrom() {
		return from;
	}

	public Vector3 getCoord() {
		return coord;
	}
	
	public int getTtl() {
		return ttl*1000-getMsgTime();
	}
	
	public boolean hasReached(Vector3 coord){
		double dist=coord.getSubVector(getCoord()).magnitude();
		dist=dist/PhysicsConsts.C*1000;
		System.out.println("tempo:"+getMsgTime()+" dist:"+dist);
		return (getMsgTime()>dist);
	}
	
	public int getMsgTime(){
		Date now=new Date();
		long i2=now.getTime();
		long i1=horaEnvio.getTime();
		int dif=(int)(i2-i1);
		return dif;
	}
	public Date getHoraEnvio() {
		return horaEnvio;
	}
}
