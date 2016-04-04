package com.cristiano.galactic.controller.messenger;


import java.util.Vector;

import com.cristiano.cyclone.utils.ObjetoBasico;
import com.cristiano.galactic.model.Sistema;


public class StarMessenger extends ObjetoBasico {
Vector<ObjUpdate> messages =new Vector<ObjUpdate>();
Sistema world;

public StarMessenger(Sistema world){
	this.world=world;
}
public boolean turn(){
	for (int i = 0; i < messages.size(); i++) {
		ObjUpdate oupd=messages.elementAt(i);
		if (oupd instanceof Message){
			Message msg=(Message)(messages.elementAt(i));
			if (msg.isDelivered()){
				System.out.println("Message delivered:"+msg);
				messages.remove(i);
			}
		}
		
		if (oupd.getTtl()<0){
			System.out.println("Message couldnt be delivered:"+oupd);
			messages.remove(i);
		}
	}
	
	return (messages.size()>0);
}


public void addMessage(ObjUpdate msg){
	messages.add(msg);
	//System.out.println("StarMessenger.addMessenger... total: "+messages.size());
}
}
