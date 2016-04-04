package com.cristiano.java.bpM.entidade.functions;

import com.cristiano.java.product.utils.StringHelper;

public class UserFunction implements IFunctionSolving{

	private String function;
	private String val;
	private String[] parms;
	
	public UserFunction(String function, String params, String val) {
		this.function=function;
		this.val=val;
		if (params.trim().equals("")){
		parms=new String[0];
		} else {
		parms=params.split(",");
		}
	}

	@Override
	public String solveFunction(String texto, Solver fromFunction) {
		String[] split = StringHelper.getParams(texto);
		String calc=val;
		for (int i=0;i<split.length;i++){
			calc=calc.replace(parms[i], split[i]);
		}
		String ret = fromFunction.resolveFunctionOf(calc);
		//Log.debug("userFunction: "+function+"("+texto+")="+ret);
		return ret;
	}

	@Override
	public boolean canSolve(String params) {
		String[] split = StringHelper.getParams(params);
		return (split.length==parms.length);
	}

}
