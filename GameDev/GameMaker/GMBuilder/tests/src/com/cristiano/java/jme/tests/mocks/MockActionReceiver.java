package com.cristiano.java.jme.tests.mocks;

import com.cristiano.jme3.interfaces.IReceiveAction;

public class MockActionReceiver implements IReceiveAction{
	public final static String action1="act1";
	public final static String action2="act2";
		public float c1=0;
		public float c2=0;

			@Override
			public void processAction(String action, float mult, float tpf) {
				if (action.equals(action1)){
					c1+=mult*tpf;
				}
				if (action.equals(action2)){
					c2+=mult*tpf;
				}
				
			}
			
		}
