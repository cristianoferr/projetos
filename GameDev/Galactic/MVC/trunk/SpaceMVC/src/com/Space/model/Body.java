package com.Space.model;

public class Body extends Item {
private float mass;
private float radius;
private String name="";

public Body(String name){
	this.name=name;
}

public float getRadius() {
	return radius;
}

public void setRadius(float radius) {
	this.radius = radius;
}

public float getMass() {
	return mass;
}

public void setMass(float mass) {
	this.mass = mass;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}
}
