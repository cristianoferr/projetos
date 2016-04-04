package com.Space.model;

import com.Space.controller.ships.BasicShip;

public class Ship extends Body {

BasicShip bs=null;
public Ship(String name){
	super(name);
}
public BasicShip getBs() {
	return bs;
}
public void setBs(BasicShip bs) {
	this.bs = bs;
}

}
