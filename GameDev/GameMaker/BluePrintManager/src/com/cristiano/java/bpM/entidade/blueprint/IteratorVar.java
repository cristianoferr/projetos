package com.cristiano.java.bpM.entidade.blueprint;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.utils.Log;

public class IteratorVar {
	float valorInicial = 0;
	float valorFinal = 0;
	float step = 0;
	String calc = "";

	public float getValue(int index) {
		return valorInicial+step * index;
	}

	public void startVars(int pos, int qtd, String varname,
			GenericElement AbstractElement) {
		if (qtd > 1) {
			step = (valorFinal - valorInicial) / (qtd - 1);
		} else {
			step = (valorFinal - valorInicial);
		}
		String val;
		AbstractElement.setVar(varname + "Initial", Float.toString(valorInicial));
		AbstractElement.setVar(varname + "Final", Float.toString(valorFinal));
		AbstractElement.setVar(varname + "Step", Float.toString(step));

		if (!calc.equals("")) {
			val = calc;
		} else {
			val = Float.toString(valorInicial+step * pos);
		}
		AbstractElement.setVar(varname, val);

	}

	public void clearVars(String varname, GenericElement AbstractElement) {
		AbstractElement.setVar(varname, null);
		AbstractElement.setVar(varname + "Initial", null);
		AbstractElement.setVar(varname + "Final", null);
		AbstractElement.setVar(varname + "Step", null);

	}

	public void data(String aux, String valor, GenericElement AbstractElement) {
		String solved = AbstractElement.resolveFunctionOf(valor);
		
		//TODO: remove this
		if (solved.startsWith("/")){
			Log.error("solved:"+solved);
		}
		if (aux.equals(Extras.ITERATOR_INITIAL)) {
			valorInicial = Float.parseFloat(solved);
		}
		if (aux.equals(Extras.ITERATOR_FINAL)) {
			valorFinal = Float.parseFloat(solved);
		}
		if (aux.equals(Extras.ITERATOR_CALC)) {
			calc = valor;
		}

	}

}
