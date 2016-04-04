package com.cristiano.galactic.model.Entity.Logic.representation;

import java.util.Date;

import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Abstract.Item;


/*
 * Essa classe vai conter todos os dados referentes a uma determinada entidade
 * do ponto de vista da entidade dona.
 * Basicamente, é tudo o que essa entidade sobre sobre outra entidade.
 * Sua posição, status, etc...
 */
public class MemoryItem {
Item refersTo;
Vector3 coord; 
Vector3 velocity;
ArtificialEntity owner;
Vector3 ROCVertical=null;
Vector3 ROCHorizontal=null;
private Date lastResult=new Date();

double rocVert=0;
double rocHoriz=0;


static final double rad90=Math.toRadians(90);



public String toString(){
	return refersTo.getName();
}

public MemoryItem(ArtificialEntity owner,Item refersTo){
	this.refersTo=refersTo;
	this.owner=owner;
	coord=refersTo.getCoord();
	velocity=refersTo.getVelocity().clone();
	
}

public MemoryItem(ArtificialEntity owner,Vector3 refersTo){
	this.refersTo=null;
	this.owner=owner;
	coord=refersTo.clone();
	velocity=null;
	
}

public Item getRefersTo() {
	return refersTo;
}

public Vector3 getCoord() {
	return coord;
}

public double getROCHorizontal(){
return rocHoriz;
}

private void calcROCHorizontal(){
	rocHoriz=0;
	if (refersTo==null) return;
	if (ROCHorizontal==null) return;
	Vector3 fwd=refersTo.getTransformDirection(Vector3.X);
	rocHoriz = 90-Math.toDegrees(fwd.angle(ROCHorizontal));
	 //System.out.println("ROC horiz= dir:"+dir);
}

public double getROCVertical(){
	return rocVert;
	}

private void calcROCVertical(){
	rocVert=0;
	if (refersTo==null) return;
	if (ROCVertical==null) return;
	
	Vector3 fwd=refersTo.getTransformDirection(Vector3.X);
	rocVert = 90-Math.toDegrees(fwd.angle(ROCVertical));//Vector3.getDotProduct( oldVec, fwd );
	// System.out.println("ROC vert= :"+rocVert);
	
}


public Vector3 getDeltaPosition(){
	return getDeltaPosition(owner.getCoord());
}

public Vector3 getDeltaPosition(Vector3 pos){
	return coord.getSubVector(pos);
}

public Vector3 getDeltaVelocity(){
	return velocity.getSubVector(owner.getVelocity());
}

public Vector3 getRelVelocity(){
	return getDeltaVelocity();
}

public double getRelVelocityMag(){
	return getRelVelocity().magnitude();
}
public double getRelVelocityX(){
	return getDeltaVelocity().x;
}
public double getRelVelocityY(){
	return getDeltaVelocity().y;
}
public double getRelVelocityZ(){
	return getDeltaVelocity().z;
}

public double getFrontAngle(){
	Vector3 fwd=owner.getTransformDirection(Vector3.X);
	//Math.toDegrees(fwd.angle(tc))
    return Math.toDegrees(fwd.angle(getDeltaPosition()));//Vector3.angleDir(up, tc, right);
	
}

public double getVerticalAngle(){
	Vector3 up=owner.getTransformDirection(Vector3.UP);
	return Math.toDegrees(up.angle(getDeltaPosition()))-90;
}

public double getHorizontalAngle(){
	Vector3 right=owner.getTransformDirection(Vector3.Z);
	return Math.toDegrees(right.angle(getDeltaPosition()))-90;
}


public double getDistance(){
		return getDeltaPosition().magnitude();
}

public double getDistanceX(){
	return getDeltaPosition().x;
}

public double getDistanceY(){
	return getDeltaPosition().y;
}

public double getDistanceZ(){
	return getDeltaPosition().z;
}


public double getPositionX(){
	return refersTo.getCoord().getX();
}

public double getPositionY(){
	return refersTo.getCoord().getY();
}

public double getPositionZ(){
	return refersTo.getCoord().getZ();
}




/*
 * Essa função atualizará a posição da memória a partir do objeto referenciado.
 */
public void update(){
	
	if (refersTo!=null) {
		coord.set(refersTo.getCoord().getX(), refersTo.getCoord().getY(), refersTo.getCoord().getZ());
		velocity.set(refersTo.getVelocity().x, refersTo.getVelocity().y, refersTo.getVelocity().z);
		
		if (oktoUpdate(50)){
			calcROCVertical();
			calcROCHorizontal();
			//System.out.println("refersto:"+refersTo);
			if (refersTo.getBody()!=null){
				//System.out.println("transformMatrix:"+refersTo);
				ROCVertical=refersTo.getTransformDirection(Vector3.Y);
				ROCHorizontal=refersTo.getTransformDirection(Vector3.Z);
			} else {
				System.out.println("memoryItem null body:"+refersTo+" owner:"+owner+" owner.body="+owner.getBody());
			}
		}
	}
	
}

public Vector3 getVelocity() {
	return velocity;
}

public boolean oktoUpdate(int timeMS){
	Date now=new Date();
	long i2=now.getTime();
	long i1=lastResult.getTime();
	int dif=(int)(i2-i1);
	if (dif>timeMS){
		lastResult=new Date();
		return true;
	}
	return false;
}	
}
