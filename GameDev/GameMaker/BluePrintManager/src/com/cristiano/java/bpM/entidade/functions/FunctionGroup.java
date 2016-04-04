package com.cristiano.java.bpM.entidade.functions;

import java.util.ArrayList;

public class FunctionGroup implements IFunctionSolving{
ArrayList<IFunctionSolving> functions=new ArrayList<IFunctionSolving>();

public void addFunction(IFunctionSolving function){
	functions.add(function);
}

@Override
public String solveFunction(String texto, Solver fromFunction) {
	for (IFunctionSolving function:functions){
		if (function.canSolve(texto)){
			return function.solveFunction(texto, fromFunction);
		}
	}
	return "";
}

@Override
public boolean canSolve(String params) {
	for (IFunctionSolving function:functions){
		if (function.canSolve(params)){
			return true;
		}
	}
	return false;
}

}
