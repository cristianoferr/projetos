package com.cristiano.java.genProg.fitness;

import java.util.HashMap;

import com.cristiano.java.genProg.GPSettings;

public class GPResults {

	private GPSettings settings;
	private HashMap<String, GPFitness> results;

	public GPResults(GPSettings settings) {
		this.settings=settings;
		results=new HashMap<String,GPFitness>();
	}
	
	public void addFitness(GPFitness fitness){
		results.put(fitness.getName(), fitness);
	}

	
}
