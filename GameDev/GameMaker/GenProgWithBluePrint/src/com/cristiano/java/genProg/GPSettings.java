package com.cristiano.java.genProg;

import java.util.ArrayList;

import com.cristiano.java.genProg.fitness.GPFitness;


public class GPSettings {

	int maxVariacoes=50;
	private int qtdTestesPerInstancia=5;
	private ArrayList<GPFitness> arrFitness=new ArrayList<GPFitness>();

	public int getMaxVariacoes() {
		return maxVariacoes;
	}

	public void setMaxVariacoes(int maxVariacoes) {
		this.maxVariacoes = maxVariacoes;
	}

	public void setQtdTestesPerInstancia(int i) {
		this.qtdTestesPerInstancia=i;
		
	}

	public int getQtdTestesPerInstancia() {
		return qtdTestesPerInstancia;
	}

	public void addFitness(GPFitness fitness) {
		this.arrFitness.add(fitness);
		
	}
	
}
