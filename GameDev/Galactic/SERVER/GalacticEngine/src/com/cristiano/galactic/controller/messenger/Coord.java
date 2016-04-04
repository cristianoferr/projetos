package com.cristiano.galactic.controller.messenger;


import java.io.Serializable;

import com.cristiano.cyclone.utils.Formatacao;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.Entity.Abstract.AbstractGameObject;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.enums.EntityType;



public class Coord extends AbstractGameObject implements Serializable,Cloneable{
private static final long serialVersionUID = -8655851468665564511L;
//1=1 metro, 1000=1km
private double multi=1;
private Item referenceFrame=null;


private Vector3 vector=new Vector3();





public Coord clone(){
	Coord c=new Coord(vector.x,vector.y,vector.z,referenceFrame);
	c.setMulti(multi);
	return c;
}



public boolean equals(final Coord c){
	if (c.getX()!=getX()) {return false;}
	if (c.getY()!=getY()) {return false;}
	if (c.getZ()!=getZ()) {return false;}
	if (c.getItem()!=getItem()) {return false;}
	
	return true;
}

/*public void copyTo( RigidBody item ) {
	Transform startTransform = new Transform();
	startTransform.setIdentity();
	Vector3d camPos = new Vector3d(getX(),getY(),getZ());
	startTransform.origin.set(camPos);
}*/


public void setMulti(double multi) {
	this.multi = multi;
}
public Coord(double x,double y,double z,Item objRef){
//	super(objRef.getDataManager(),"COORD","COORD");
	super(-1,objRef.getDataManager(),null,EntityType.ET_COORD);
	setX(x);
	setY(y);
	setZ(z);
	this.referenceFrame=objRef;
}
public Coord(Vector3 v){
	super(-1,null,null,EntityType.ET_COORD);
	setX(v.x);
	setY(v.y);
	setZ(v.z);
}
public static double sqr(double x){
	return x*x;
}
public double getDistCoord(Coord c){
	return (double)Math.sqrt(sqr(vector.x-c.getX())+sqr(vector.y-c.getY())+sqr(vector.z-c.getZ()));
}
public double getX() {
	return vector.x;
}

public void set(double x,double y,double z) {
	vector.x = x;
	vector.y = y;
	vector.z = z;
}

public final void setX(double x) {
	vector.x = x;
}

public double getY() {
	return vector.y;
}

public final void setY(double y) {
	vector.y = y;
}

public double getZ() {
	return vector.z;
}

public final void setZ(double z) {
	vector.z = z;
}

public double getMulti() {
	return multi;
}

public double get0(){
	return getX();
}
public double get1(){
	return getY();
}
public double get2(){
	return getZ();
}


public final double[] toDoubleArray4() {
	return new double[]{ (double) get0(), (double) get1(), (double) get2(), 0.0f };
}
public final double[] toDoubleArray() {
	return new double[]{(double) get0(), (double) get1(), (double) get2()};
}

public String toString(){
	return "COORD=> X: "+Formatacao.format(getX())+" Y: "+Formatacao.format(getY())+" Z:"+Formatacao.format(getZ());
}

public Vector3 getVector() {
	return vector;
}

public Item getItem() {
	return referenceFrame;
}



public void setItem(Item item) {
	this.referenceFrame = item;
}
}
