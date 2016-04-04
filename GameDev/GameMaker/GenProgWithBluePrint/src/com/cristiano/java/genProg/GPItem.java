package com.cristiano.java.genProg;

import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.genProg.fitness.GPResults;

/*
 * Reformula��o da classe: um gpitem vai contar uma �rvore de elementos provindos do
 * blueprintmanager.  
 * 
 */
public class GPItem {
	GenericElement baseElement;
	GPSettings settings;
	GPResults results;
	
	
	public GPItem(GPSettings settings,GenericElement geBase) {
		this.baseElement=geBase;
		this.settings=settings;
		results=new GPResults(settings);
		
	}


	public GenericElement getBaseElement() {
		return baseElement;
	}

	
}
