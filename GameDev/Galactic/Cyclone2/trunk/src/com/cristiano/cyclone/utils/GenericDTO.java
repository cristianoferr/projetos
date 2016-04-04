package com.cristiano.cyclone.utils;

import java.util.Vector;



public class GenericDTO {
Vector objects=new Vector();


public void addInteger(int i){
	objects.add(new Integer(i));
}

public void addDouble(double i){
	objects.add(new Double(i));
}

public void addVector(Vector3 item){
	objects.add(item);
}

public void addString(String item){
	objects.add(item);
}
public void addParam(Object item){
	objects.add(item);
}

public Object getParam(int i){
	return objects.get(i);
}

public int getInt(int i){
	if (objects.get(i) instanceof Integer)
	return ((Integer)objects.get(i)).intValue();
	return 0;
}

public double getDouble(int i){
	if (objects.get(i) instanceof Integer)
	return ((Double)objects.get(i)).doubleValue();
	return 0;
}

public Vector3 getVector(int i){
	if (objects.get(i) instanceof Vector3)
	return (Vector3)objects.get(i);
	return null;
}

public String getString(int i){
	if (objects.get(i) instanceof Vector3)
	return (String)objects.get(i);
	return null;
}
public Vector getObjects() {
	return objects;
}

public void setObjects(Vector objects) {
	this.objects = objects;
}

public int size() {
	return objects.size();
}
}
