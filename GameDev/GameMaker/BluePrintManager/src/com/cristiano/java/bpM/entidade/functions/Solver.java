package com.cristiano.java.bpM.entidade.functions;

import java.io.Serializable;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.params.ParamList;
import com.cristiano.java.bpM.utils.FunctionHelper;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.params.Parametro;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;

public class Solver  implements Serializable{
	private static final long serialVersionUID = 9189513555910408108L;
	private AbstractElement gameElement;
	ParamList vars;
	public Parametro lastParam=null;//lastParam to be solved...

	public Solver(AbstractElement propertyObject) {
		gameElement = propertyObject;
		vars = new ParamList(null);

	}

	public void setVar(String var, String val) {
		vars.setVar(var, val);
	}

	public String getVar(String var) {
		if (var.equals(Extras.REF_THIS))
			return var;
		if (var.equals(Extras.REF_OWNER))
			return var;

		String varName = var.replace("$", "");
		String result = vars.getVar(varName);
		if (result == null)
			result = getElementManager().getVar(varName);

		if (!result.equals("")) {
			result = FunctionHelper.simplificaNotacaoCientifica(result);
			return result;
		}
		if (vars.hasEmptyParam(varName)) {
			return result;
		}

		// Log.debug("Variavel " + var + " n�o encontrada em "
		// + gameElement.getIdentifier());
		return var;
	}

	private String resolveFuncoes(String property) {
		if (!property.contains("(")){
			return property;
		}
		return getElementManager().getFunctionSolver().resolveFuncoes(property,
				this);

	}

	public ElementManager getElementManager() {
		return getGameElement().getElementManager();
	}
	
	public String resolveFunctionOf(String property) {
		return resolveFunctionOf(property,null);
	}

	public String resolveFunctionOf(String property, Parametro paramSource) {
		String value=solveFunction(property, paramSource);
		//Log.trace(property+ ":::>>"+value);
		return value;
	}
	
	private String solveFunction(String toSolve, Parametro paramSource) {
		if (paramSource!=null){
			this.lastParam=paramSource;
		}
		String original=toSolve;
		String property = original;
		/*if (toSolve.contains("$this.splashScreen+$this.startScreen")){
			Log.info("Teste");
		}*/
		//property="addMesh({mesh poligono yz leaf},sizeZ=randf(0.1,0.4),merge=1,sizeRadius=$this.width*randf(0.05,0.2),posY=randf(-0.5,0.5))";
		/*if ((property.contains("randf($this.width/2,$this.width)")) ){
			Log.debug("Prop:"+property);
		}*/
		if (!checkFunctionSolvable(property)) {
			return property;
		}
		try {
			do {
				original = property;
				property = resolvePropriedadesDasVariaveis(property);
			} while (!original.equals(property));
			original = property;
			property = simplificaSinais(property);
			property = resolveFuncoes(property);
			property = resolveParenteses(property);
			property = resolveOperadoresBooleanos(property);
			property = resolveOperadoresMatematicos(property);
		} catch (Exception e) {
			e.printStackTrace();
		}
		property = simplificaSaida(property);
		if (!property.equals(original)) {
			return resolveFunctionOf(property);
		}
		return property;
	}

	private String resolveOperadoresMatematicos(String property) {
		//when its atribution I dont do anything... Error was happening in functions like: 'param=-10'
		if (property.contains("=")){
			return property;
		}
		
		String saida;
		saida = calcOperacao(property, "&");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, "+");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, "-");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, "*");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, "/");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, "%");
		if (saida != "")
			property = saida;
		return property;
	}

	private String resolveOperadoresBooleanos(String property) {
		String saida;
		
		saida = calcOperacao(property, ">=");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, "<=");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, ">");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, "<");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, "==");
		if (saida != "")
			property = saida;
		saida = calcOperacao(property, "!=");
		if (saida != "")
			property = saida;
		/*saida = calcOperacao(property, "=");
		if (saida != "")
			property = saida;*/
		return property;
	}

	private String simplificaSinais(String property) {
		property = property.replace("++", "+");
		property = property.replace("--", "+");
		property = property.replace("+-", "-");
		property = property.replace("-+", "-");
		return property;
	}

	private String simplificaSaida(String property) {

		if (property.endsWith(".0")) {
			property = property.substring(0, property.lastIndexOf(".0"));
		}
		return property;
	}

	private String resolvePropriedadesDasVariaveis(String property) {
		if (property.contains("].")) {
			return resolveAcessoPropriedadeObjeto(property);
		}
		
		if (property.contains(Extras.BP_EOL)){
			return property;
		}
		
		if (property.contains("$")) {
			int pos = property.indexOf("$");
			int posFim = StringHelper.pegaPosicaoFinalPalavra(property,
					pos + 1, false);
			String var = property.substring(pos, posFim);
			if (posFim >= property.length())
				return resolvePropriedade(property, var, pos, posFim);
			int posPonto = posFim;
			
			if (property.charAt(posPonto) == '#') {
				//ex: $this#list1.valor
				int posInicio = property.indexOf(".",posPonto+1);
				String list=property.substring(posPonto+1,posInicio);
				posFim = StringHelper.pegaPosicaoFinalPalavra(property,
						posInicio + 1, false);
				//var = property.substring(posInicio+1, posFim);
				property = resolvePropriedadeDaVariavel(list,property, var,
						posInicio, pos, posFim,false);
			} else if (property.charAt(posPonto) == '.') {
				// Ex: $this.valor
				property = resolvePropriedadeDaVariavel(Extras.LIST_PROPERTY,property, var,
						posPonto, pos, posFim,false);
			} else {
				// Ex: $this
				property = resolvePropriedade(property, var, pos, posFim);
			}

		}

		return property;
	}

	// Resolve "[id].propriedade"
	private String resolveAcessoPropriedadeObjeto(String property) {
		int indexOf = property.indexOf("].");
		int i = indexOf;
		while (property.charAt(i) != '[')
			i--;
		String id = property.substring(i + 1, indexOf);
		IGameElement ge = getElementManager().getElementWithID(id);
		String prop = StringHelper.pegaPalavraNaPosicao(property, indexOf + 2,
				false);
		if (ge != null) {
			String valor = ge.getProperty(prop);
			property = property.substring(0, i) + valor
					+ property.substring(indexOf + 2 + prop.length());
			// property=property.replace("["+id+"]."+prop, valor);

			return property;
		}
		return "";
	}

	private String resolvePropriedade(String property, String var, int pos,
			int posFim) {
		String antes = property.substring(0, pos);
		String depois = property.substring(posFim);
		IGameElement referencedObject = getReferencedObject(var);
		String res = var;
		if (referencedObject == null) {
			res = getVar(var);
		} else {
			res = "[" + referencedObject.id() + "]";
		}

		return antes + res + depois;
	}

	private String resolvePropriedadeDaVariavel(String list,String property, String var,
			 int posPonto, int pos, int posFim, boolean solveH) {
		String prop = StringHelper.pegaPalavraNaPosicao(property, posPonto + 1,
				false);
		String antes = property.substring(0, pos);
		String depois = property.substring(posPonto + prop.length() + 1);
		IGameElement referencedObject = getReferencedObject(var);
		if (referencedObject != null) {
			String solved = ((AbstractElement) referencedObject).getParamH(list, prop, solveH);
			//when there is no property set...
			if ("".equals(solved)){
				return property;
			}
			
			return antes + solved + depois;
		} else
			return property;
	}
	
	public String toString(){
		return gameElement.toString();
	}

	/*
	 * private String resolveObjetosDefines(String property) { if
	 * (property.contains("$")) { int pos = property.indexOf("$"); int posFim =
	 * StringHelper.pegaPosicaoFinalPalavra(property, pos+1, false); if (posFim
	 * >= property.length()) return property; String var =
	 * property.substring(pos , posFim);
	 * 
	 * //if (property.substring(posFim, posFim + 1).equals(".")) { property =
	 * resolveReferenciasPropriedades( var, property); //}
	 * 
	 * } return property; }
	 */

	private String resolveParenteses(String property) {
		try {
			
		if ((property.contains("(")) && (property.contains(")"))) {
			int posIni = property.indexOf("(");
			int posFim = property.lastIndexOf(")");
			// Log.debug("property: "+property);
			String parenteses = property.substring(posIni + 1, posFim);
			if (FunctionHelper.needsSolving(parenteses))
				property = property.substring(0, posIni) + "("
						+ resolveFunctionOf(parenteses)
						+ property.substring(posFim);
		}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Error on resolveParenteses:"+property);
		}
		return property;
	}

	private IGameElement getReferencedObject(String strSource) {
		IGameElement ge = null;
		if (strSource.equals(Extras.REF_SOURCE)) {
			ge = getGameElement().getEstende();
		} else if (strSource.equals(Extras.REF_OWNER)) {
			ge = getGameElement().getCreator();
		} else if (strSource.equals(Extras.REF_THIS)) {
			ge = getGameElement();
		} else if (strSource.equals(Extras.REF_ENTITY)) {
			ge = getGameElement().getEntity();
		} /*else if (getGameElement().hasProperty(strSource.substring(1))) {
			ge = getGameElement().getPropertyAsGE(strSource.substring(1));
		}*/ else {
			String ident = strSource.replace("$", "");
			String valor = getElementManager().getVar(ident);
			if (!valor.equals("")) {
				ge = getElementManager().getElementWithID(valor);
			}
		}
		return ge;
	}

	// redirecionadores

	String[] getParams(String par) {
		return StringHelper.getParams(par);
	}

	private String calcOperacao(String property, String oper) {
		return FunctionHelper.calcOperacao(this, property, oper);
	}

	public AbstractElement getGameElement() {
		return gameElement;
	}

	public boolean checkFunctionSolvable(String property) {
		String prop=property;
		do {
			property=prop;
			prop = resolvePropriedadesDasVariaveis(property);
		} while (!prop.equals(property));
		
		prop = prop.replace(Extras.REF_THIS, " ");
		prop = prop.replace(Extras.REF_OWNER, " ");
		prop = prop.replace(Extras.REF_SOURCE, " ");
		prop = prop.replace(Extras.REF_ENTITY + ".", " ");
		if (prop.contains("$")) {
			int pos = prop.indexOf("$");
			int posFim = StringHelper.pegaPosicaoFinalPalavra(prop, pos + 1,
					false);
			String var = prop.substring(pos, posFim);
			String antes = prop.substring(0, pos);
			String depois = prop.substring(posFim);
			boolean existeVar = !(var).equals(getVar(var));
			boolean existeProp = !gameElement.getProperty(var.substring(1))
					.equals("");

			boolean b = checkFunctionSolvable(antes)
					&& (existeVar || existeProp)
					&& checkFunctionSolvable(depois);
			if (!b)
				return false;
		}
		return true;
	}




}
