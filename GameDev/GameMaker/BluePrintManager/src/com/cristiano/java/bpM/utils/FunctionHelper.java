/*
 * Classe para aliviar o Function, s� ter� m�todos p�blicos est�ticos aqui...
 */

package com.cristiano.java.bpM.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.consts.JavaConsts;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.functions.Solver;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.proc.noise.IMakeNoise;
import com.cristiano.proc.noise.SimplexNoise;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;

public class FunctionHelper {

	// usado para validar fun��es via @validate
	public static boolean validateFunctions(AbstractElement ge, String[] params) {
		for (int i = 1; i < params.length; i++) {
			String solved=ge.resolveFunctionOf(params[i]);
			if (solved.contains("$")) {
				if (params[0].equals(Extras.VALIDATE_ISNULL)) {
					return true;
				}
				// parametro n�o deve conter variaveis e referencias
				return false;
			}
		}

		if (params[0].equals(Extras.VALIDATE_ISNOTNULL)) {
			return validateIsNotNull(ge, params);
		}

		if (params[0].equals(Extras.VALIDATE_ISEQUALS)) {
			return validateIsEquals(ge, params);
		}

		if (params[0].equals(Extras.VALIDATE_ISNULL)) {
			return validateIsNull(ge, params);
		}

		if (params[0].equals(Extras.VALIDATE_ISTRUE)) {
			return validateIsTrue(ge, params);
		}

		return false;
	}

	private static boolean validateIsTrue(AbstractElement ge, String[] params) {
		if (params.length == 1)
			return false;
		String par1 = StringHelper.removeChaves(ge.resolveFunctionOf(params[1]));
		par1 = StringHelper.removeColchetes(par1);

		return ((par1.equals("1")) || (par1.equals("true")));
	}

	private static boolean validateIsNull(AbstractElement ge, String[] params) {
		if (params.length == 1)
			return true;
		String par1 = StringHelper.removeChaves(ge.resolveFunctionOf(params[1]));
		par1 = StringHelper.removeColchetes(par1);

		return par1.equals("");
	}

	private static boolean validateIsEquals(AbstractElement ge, String[] params) {
		String par1 = ge.resolveFunctionOf(params[1]);
		String par2 = ge.resolveFunctionOf(params[2]);

		return par1.equals(par2);
	}

	private static boolean validateIsNotNull(AbstractElement ge, String[] params) {
		if (params.length == 1)
			return false;
		String par1 = StringHelper.removeChaves(ge.resolveFunctionOf(params[1]));
		par1 = StringHelper.removeColchetes(par1);
		return !par1.equals("");
	}

	public static String calcOperacao(Solver function, String property, String oper) {

		if (property.contains(oper)) {
			int pos = property.indexOf(oper, 1);
			if (pos == -1)
				pos = property.indexOf(oper, 0);
			return calcFunction(function, property, pos, oper);
		}
		return "";
	}

	public static String resolveFuncaoIF(String function, String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		String par1 = fromFunction.resolveFunctionOf(getParam(1, texto));
		String par2 = fromFunction.resolveFunctionOf(getParam(2, texto));
		int resultado = checaCondicaoIF(par0, fromFunction);
		if (resultado == -1) {
			return function + "(" + texto + ")";
		}
		return ((resultado == 1) ? par1 : par2);
	}

	public static String convertNumber(double value) {
		return convertNumber(value, JavaConsts.DECIMAL_PLACES);
	}

	public static String convertNumber(double value,int places) {
		value = CRMathUtils.round(value, places);
		String val=Double.toString(value);
		if (val.contains("E")){
			Log.error("Value is too big:"+val);
		}
		return val;
	}
	public static String resolveFuncaoHex2Dec(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		return Integer.toString(Integer.parseInt(par0, 16));
	}

	public static String resolveFuncaoSinD(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		return convertNumber(Math.sin(Math.toRadians(Double.parseDouble(par0))));
	}

	public static String resolveFuncaoCosD(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		return convertNumber(Math.cos(Math.toRadians(Double.parseDouble(par0))));
	}

	public static String resolveFuncaoTanD(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		return convertNumber(Math.tan(Math.toRadians(Double.parseDouble(par0))));
	}

	public static String resolveFuncaoSinR(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		return convertNumber(Math.sin(Double.parseDouble(par0)));
	}

	public static String resolveFuncaoCosR(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		return convertNumber(Math.cos(Double.parseDouble(par0)));
	}
	public static String resolveFuncaoRound(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		return convertNumber(Double.parseDouble(par0),0);
	}
	public static String resolveFuncaoTanR(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		return convertNumber(Math.tan(Double.parseDouble(par0)));
	}

	public static int checaCondicaoIF(String par0, Solver fromFunction) throws NumberFormatException {
		// se contem vari�vel ent�o n�o est� pronto para ser resolvido
		if (par0.contains("$"))
			return -1;
		String[] pars = FunctionHelper.getParamsCondicao(par0);
		String par = fromFunction.resolveFunctionOf(pars[0]);
		if (!CRJavaUtils.isNumber(par)) {
			return -1;
		}
		float p0 = Float.parseFloat(par);
		if (pars[1] == null) {
			return (int) p0;
		}
		par = fromFunction.resolveFunctionOf(pars[2]);
		if (!CRJavaUtils.isNumber(par)) {
			return -1;
		}
		float p2 = Float.parseFloat(par);

		if (pars[1].equals("<")) {
			return (p0 < p2 ? 1 : 0);
		}
		if (pars[1].equals("<=")) {
			return (p0 <= p2 ? 1 : 0);
		}
		if (pars[1].equals(">")) {
			return (p0 > p2 ? 1 : 0);
		}
		if (pars[1].equals(">=")) {
			return (p0 >= p2 ? 1 : 0);
		}
		if (pars[1].equals("==")) {
			return (p0 == p2 ? 1 : 0);
		}
		if (pars[1].equals("=")) {
			return (p0 == p2 ? 1 : 0);
		}
		if (pars[1].equals("<>")) {
			return (p0 != p2 ? 1 : 0);
		}
		if (pars[1].equals("!=")) {
			return (p0 != p2 ? 1 : 0);
		}

		return -1;
	}

	public static String resolveFuncaoSum(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		String par1 = fromFunction.resolveFunctionOf(getParam(1, texto));

		List<IGameElement> result = fromFunction.getElementManager().getElementsWithTag(par0);
		float soma = 0;
		for (int i = 0; i < result.size(); i++) {
			try {
				float v = Float.parseFloat(((AbstractElement) result.get(i)).getParamH(Extras.LIST_PROPERTY, par1, true));
				soma += v;
			} catch (Exception e) {
			}

		}
		String txt;
		txt = StringHelper.converteFloatToString(soma);
		return txt;
	}

	public static String resolveFuncaoRandomTag(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		String[] pars = StringHelper.splitList(par0);
		return pars[(int) CRJavaUtils.random(0, pars.length)];
	}

	public static String resolveFuncaoList(String texto, Solver fromFunction) {
		String txt;
		List<IGameElement> result = fromFunction.getElementManager().getElementsWithTag(texto);
		txt = "[";
		for (int i = 0; i < result.size(); i++) {
			txt += result.get(i).id() + ",";
		}
		txt += "]";
		txt = txt.replace(",]", "]");
		return txt;
	}

	public static String resolveFuncaoCount(String texto, Solver fromFunction) {
		// its a list
		if (texto.contains("[")) {
			return Integer.toString(StringHelper.countItemFromList(texto));
		}
		return Integer.toString(fromFunction.getElementManager().countElementos(texto));
	}

	public static String resolveFuncaoPickOne(String texto, Solver fromFunction) {
		// its a list
		if (texto.contains("[")) {
			return StringHelper.pickRandomItemFromList(texto);
		}

		String par0 = getParam(0, texto);
		String[] parms = getParams(texto);
		String par1 = "";
		for (int i = 1; i < parms.length; i++) {
			par1 = par1 + parms[i] + ",";
		}
		if ("".equals(par1)) {
			par1 = null;
		} else {
			par1 = par1.substring(0, par1.length() - 1);
		}

		IGameElement ge = fromFunction.getElementManager().pickOne(par0, par1);
		if (ge == null) {
			return "[]";
		}
		return "[" + ge.id() + "]";
	}

	public static String resolveFuncaoPickFromList(String texto, Solver fromFunction) {
		String[] parms = getParams(texto);
		String[] tagList = StringHelper.splitList(parms[0]);
		if (tagList == null) {
			tagList = new String[] { "leaf" };
			Log.warn("No Filter was used on pickFromList... baseTag is '" + parms[1] + "'");
		}
		int qtd = 1;
		if (parms.length > 2) {
			qtd = Integer.parseInt(parms[2]);
			if (qtd < 1) {
				qtd = 1;
			}
			if (qtd > tagList.length) {
				qtd = tagList.length;
			}
		}
		String saida = "";
		for (int i = 0; i < qtd; i++) {
			String tag = "";
			boolean found = false;
			do {
				found = true;
				int p = (int) (CRJavaUtils.random() * qtd);
				tag = tagList[p];
				if ("".equals(tag)) {
					found = false;
				} else {
					tagList[p] = "";
				}
			} while (!found);
			saida = StringHelper.concatenaLista(saida, resolveFuncaoPickFinal(tag + " " + parms[1], fromFunction));
		}

		return saida;
	}

	public static String resolveFuncaoPickFinal(String texto, Solver fromFunction) {
		boolean isAttribution = texto.contains("=");
		// its a list
		if ((!isAttribution) && (texto.contains("["))) {
			return StringHelper.pickRandomItemFromList(texto);
		}

		String par0 = getParam(0, texto);
		String[] parms = getParams(texto);
		String par1 = "";
		for (int i = 1; i < parms.length; i++) {
			par1 = par1 + parms[i] + ",";
		}
		if ("".equals(par1)) {
			par1 = null;
		} else {
			par1 = par1.substring(0, par1.length() - 1);
		}

		AbstractElement ge = (AbstractElement) fromFunction.getElementManager().pickFinal(par0, fromFunction.getGameElement(), par1);
		if (ge == null) {
			return "[]";
		}
		return "[" + ge.id() + "]";
	}

	public static String resolveFuncaoPickSingle(String texto, Solver fromFunction) {
		// its a list
		if (texto.contains("[")) {
			return StringHelper.pickRandomItemFromList(texto);
		}

		String par0 = getParam(0, texto);
		String[] parms = getParams(texto);
		String par1 = "";
		for (int i = 1; i < parms.length; i++) {
			par1 = par1 + parms[i] + ",";
		}
		if (par1.equals("")) {
			par1 = null;
		} else {
			par1 = par1.substring(0, par1.length() - 1);
		}

		IGameElement ge = fromFunction.getElementManager().pickSingle(par0, fromFunction.getGameElement(), par1);
		if (ge == null)
			return "[]";
		return "[" + ge.id() + "]";
	}

	public static String resolveFuncaoConcat(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		String par1 = fromFunction.resolveFunctionOf(getParam(1, texto));
		if ((par0.contains("'")) || (par1.contains("'"))) {
			par0 = par0.replace("'", "");
			par1 = par1.replace("'", "");
			return "'" + par0 + par1 + "'";
		}

		return par0 + par1;
	}

	public static String resolveFuncaoHex(String texto, Solver fromFunction) {
		String par0 = fromFunction.resolveFunctionOf(getParam(0, texto));
		int v = 0;
		try {
			v = Math.round(Float.parseFloat(par0));
		} catch (Exception e) {
		}

		
		return CRMathUtils.toHexString(v);
		
	}

	public static String resolveFuncaoMod(String texto, Solver function) {
		String[] pars = StringHelper.getParams(texto);
		String par0 = function.resolveFunctionOf(pars[0]);
		String par1 = function.resolveFunctionOf(pars[1]);
		int n0 = Integer.parseInt(par0);
		int n1 = Integer.parseInt(par1);

		return Integer.toString(n0 % n1);
	}

	public static String resolveFuncaoRandf(String texto, Solver fromFunction) {
		String[] pars = StringHelper.getParams(texto);

		String par0 = pars[0];
		String par1 = pars[1];
		double n0 = Double.parseDouble(par0);
		double n1 = Double.parseDouble(par1);

		if (n1 < n0) {
			double n = n0;
			n0 = n1;
			n1 = n;
		}

		double r = n0 + (double) (Math.random() * (double) (n1 - n0));

		String res = new BigDecimal(r).toPlainString();

		// reduzo a quantidade casas decimais
		if (res.length() > res.indexOf(".") + 8) {
			res = res.substring(0, res.indexOf(".") + 8);
		}
		return res;
	}

	public static String resolveFuncaoRand(String texto, Solver fromFunction) {
		String[] pars = StringHelper.getParams(texto);
		int mult = 1;

		String par0 = pars[0];
		String par1 = pars[1];
		if (pars.length >= 3) {
			mult = Integer.parseInt(pars[2]);
		}

		int n0 = Integer.parseInt(par0);
		if (n0 % mult > 0) {
			n0 += -n0 % mult + mult;
		}
		int n1 = Integer.parseInt(par1);
		int conjunto = (n1 - n0) / mult;
		double v = Math.random() * conjunto;
		int r = n0 + (int) Math.round(v) * mult;

		return Integer.toString(r);
	}

	public static String resolveFuncaoAbs(String texto, Solver fromFunction) {
		String[] pars = StringHelper.getParams(texto);

		String par0 = pars[0];
		int n0 = Integer.parseInt(par0);

		return Integer.toString(Math.abs(n0));
	}

	// fun��o que chega a posi��o do quadrante...
	public static String resolveFuncaoTOPQUADRANT(String texto, Solver fromFunction) {
		String[] pars = StringHelper.getParams(texto);
		String par0 = pars[0];
		int n0 = Integer.parseInt(par0);
		n0 = n0 % 4;
		if ((n0 == 0) || (n0 == 3)) {
			return "1";
		}
		return "0";

	}

	public static String resolveFuncaoLog(String texto, Solver fromFunction) {
		String[] pars = StringHelper.getParams(texto);

		String par0 = pars[0];
		float n0 = Float.parseFloat(par0);

		return StringHelper.converteFloatToString(Math.log(n0));
	}

	public static String resolveFuncaoAbsF(String texto, Solver fromFunction) {
		String[] pars = StringHelper.getParams(texto);

		String par0 = pars[0];
		float n0 = Float.parseFloat(par0);
		return StringHelper.converteFloatToString(Math.abs(n0));
	}

	public static String resolveFuncaoSqrt(String texto, Solver fromFunction) {
		String[] pars = StringHelper.getParams(texto);

		String par0 = pars[0];
		float n0 = Float.parseFloat(par0);

		n0 = (float) Math.sqrt(n0);
		return StringHelper.converteFloatToString(n0);
	}

	public static String resolveFuncaoSqr(String texto, Solver fromFunction) {
		String[] pars = StringHelper.getParams(texto);

		String par0 = pars[0];
		float n0 = Float.parseFloat(par0);

		n0 *= n0;
		return StringHelper.converteFloatToString(n0);
	}

	public static String resolveFuncaoIsEmpty(String texto, Solver fromFunction) {
		texto = StringHelper.clear(texto);

		return (texto.equals("") ? "1" : "0");
	}

	public static String resolveFuncaoSimplexNoise2d(String texto, Solver fromFunction,
			HashMap<Integer, IMakeNoise> noiseSolvers) {
		String[] pars = StringHelper.getParams(texto);
		int seed = Integer.parseInt(pars[0]);
		int feature = Integer.parseInt(pars[1]);
		float x = Float.parseFloat(pars[2]);
		float y = Float.parseFloat(pars[3]);
		int min = -1;
		int max = 1;
		if (pars.length > 3) {
			min = Integer.parseInt(pars[4]);
			max = Integer.parseInt(pars[5]);
		}
		IMakeNoise noise = getSimplex(noiseSolvers, seed, feature);
		float ret = noise.getNoise(x, y);
		ret = min + (ret * (max - min));
		return StringHelper.converteFloatToString(ret);
	}

	public static String resolveFuncaoSimplexNoise3d(String texto, Solver fromFunction,
			HashMap<Integer, IMakeNoise> noiseSolvers) {
		String[] pars = StringHelper.getParams(texto);
		int seed = Integer.parseInt(pars[0]);
		int feature = Integer.parseInt(pars[1]);
		float x = Float.parseFloat(pars[2]) * 100;
		float y = Float.parseFloat(pars[3]) * 100;
		float z = Float.parseFloat(pars[4]) * 100;
		int min = -1;
		int max = 1;
		if (pars.length > 4) {
			min = Integer.parseInt(pars[5]);
			max = Integer.parseInt(pars[6]);
		}
		IMakeNoise noise = getSimplex(noiseSolvers, seed, feature);
		float ret = noise.getNoise(x, y, z);
		ret = min + (ret * (max - min));
		return StringHelper.converteFloatToString(ret);
	}

	private static IMakeNoise getSimplex(HashMap<Integer, IMakeNoise> noiseSolvers, int seed, int feature) {
		IMakeNoise noise = noiseSolvers.get(seed * feature);
		if (noise == null) {
			noise = new SimplexNoise(feature, .6);
			noise.init(seed, 0, 1);
			noiseSolvers.put(seed * feature, noise);
		}
		return noise;
	}

	public static String calcFunction(Solver function, String property, int pos, String operador) {
		String prefixo;
		String sufixo;
		if (property.startsWith("+")) {
			property = property.substring(1);
		}
		String[] arrOpers = separaFuncaoEmElementos(property, pos, operador.length());
		String operando1 = "";
		String operando2 = "";
		prefixo = arrOpers[0];
		operando1 = arrOpers[1];
		operando2 = arrOpers[2];
		sufixo = arrOpers[3];
		int x;

		/*
		 * if (operando1.equals("")) { operando1 = "0"; }
		 */
		if ((operando1.equals("")) || (operando2.equals(""))) {
			return property;
		}
		if (!operando2.equals(property)) {
			operando1 = function.resolveFunctionOf(operando1, function.lastParam);
			operando1 = StringHelper.removeParenteses(operando1);
			operando2 = function.resolveFunctionOf(operando2, function.lastParam);
			operando2 = StringHelper.removeParenteses(operando2);
		}
		// Se for string...
		// se for lista de itens...
		if ((prefixo + operando1).startsWith("[")) {
			return FunctionHelper.calcFunctionLista(prefixo + operando1, operando2 + sufixo, operador);
		}

		// se for lista de tags...
		if ((prefixo + operando1).startsWith("{")) {
			return prefixo + FunctionHelper.calcFunctionListaTags(operando1, operando2, operador) + sufixo;
		}

		boolean isNumeric = (CRJavaUtils.isNumber(operando1)) && (CRJavaUtils.isNumber(operando2));
		if (operador.equals("&")) {
			isNumeric = false;
		}
		if ((operando1.contains("'")) || (operando2.contains("'")) || (!isNumeric)) {
			if ((!operador.equals("+")) && (!operador.equals("&"))) {
				return "";
			}
			return calcFunctionString(prefixo, sufixo, operando1, operando2);
		}
		if ((operando1.contains("\"")) || (operando2.contains("\""))) {
			return calcFunctionString(prefixo, sufixo, operando1, operando2);
		}

		if (checaCondicaoBoolean(operador)) {
			return (Integer.toString(checaCondicaoIF(operando1 + operador + operando2, function)));
		}

		// se n�o for nenhum dos anteriores ent�o...
		return calcFunctionNumerico(operador, prefixo, sufixo, operando1, operando2);
	}

	public static String[] separaFuncaoEmElementos(String property, int pos, int size) {
		String[] arrOpers = new String[4];
		arrOpers[0] = "";
		arrOpers[1] = "";
		arrOpers[2] = "";
		arrOpers[3] = "";
		int nivelParenteses = 0;
		int i = 0;

		// calcula o prefixo e o 1o operando...
		for (i = pos - 1; i >= 0; i--) {
			char charAt = property.charAt(i);
			if (charAt == '(')
				nivelParenteses--;
			if (charAt == ')')
				nivelParenteses++;
			if ((nivelParenteses <= 0) && (FunctionHelper.checaParametroNumerico(charAt))) {
				break;
			}
			if ((nivelParenteses < 0) && (charAt == '(')) {
				break;
			}
			arrOpers[1] = charAt + arrOpers[1];
			arrOpers[0] = property.substring(0, i);
		}

		nivelParenteses = 0;

		for (i = pos + size; i < property.length(); i++) {
			char charAt = property.charAt(i);
			if (charAt == '(') {
				nivelParenteses++;
			}
			if (charAt == ')') {
				nivelParenteses--;
			}

			if ((nivelParenteses == 0) && (FunctionHelper.checaParametroNumerico(charAt))) {
				break;
			}

			arrOpers[2] = arrOpers[2] + charAt;
			arrOpers[3] = property.substring(i + 1);
		}
		return arrOpers;
	}

	private static String calcFunctionString(String prefixo, String sufixo, String operando1, String operando2) {
		operando1 = operando1.replace("'", "").replace("\"", "");
		operando2 = operando2.replace("'", "").replace("\"", "");
		return prefixo + "'" + operando1 + operando2 + "'" + sufixo;
	}

	private static String calcFunctionNumerico(String operador, String prefixo, String sufixo, String operando1,
			String operando2) {
		try {

			float o1 = 0;
			if (!operando1.trim().equals(""))
				o1 = Float.parseFloat(operando1);
			float o2 = 0;
			if (!operando2.trim().equals(""))
				o2 = Float.parseFloat(operando2);
			String saida = Float.toString(o1 + o2);
			float result = 0;
			if (operador.equals("*"))
				result = o1 * o2;
			if (operador.equals("-"))
				result = o1 - o2;
			if (operador.equals("+"))
				result = o1 + o2;
			if (operador.equals("/")) {
				if (o2 != 0) {
					result = o1 / o2;
				}
			}
			if (operador.equals("%"))
				result = o1 % o2;
			saida = Float.toString(result);
			saida = simplificaNotacaoCientifica(saida);
			if (saida.endsWith(".0"))
				saida = saida.replace(".0", "");
			return prefixo + saida + sufixo;
		} catch (NumberFormatException e) {
			// System.out.println("oper1:"+oper1+" oper2:"+oper2);
			return prefixo.trim() + operando1.trim() + " " + operador.trim() + " " + operando2.trim() + sufixo.trim(); // n�o
																														// �
																														// num�rico
																														// nem
																														// �
																														// string
		}
	}

	public static String simplificaNotacaoCientifica(String saida) {
		if (!saida.contains("E-")) {
			return saida;
		}
		int casas = Integer.parseInt(saida.substring(saida.indexOf("E-") + 2));
		saida = saida.replace(".", "");
		saida = saida.substring(0, saida.indexOf("E-"));
		String ret = "0.";
		for (int i = 0; i < casas - 1; i++) {
			ret = ret + "0";
		}
		saida = ret + saida;
		while (saida.endsWith("0")) {
			saida = saida.substring(0, saida.length() - 1);
		}
		return saida;
	}

	public static String calcFunctionListaTags(String oper1, String oper2, String oper) {
		String keyword1, keyword2;
		oper1 = StringHelper.removeChaves(oper1);
		keyword1 = StringHelper.pegaKeywordTags(oper1);
		oper2 = StringHelper.removeChaves(oper2);
		keyword2 = StringHelper.pegaKeywordTags(oper2);
		oper1 = StringHelper.removeKeyword(oper1);
		oper2 = StringHelper.removeKeyword(oper2);

		oper1 = oper1 + " " + oper2;

		// remove duplicated
		String[] arr = oper1.split(" ");
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i].equals(arr[j])) {
					arr[j] = "";
				}
			}
		}

		// final tag list
		String saida = "{";
		for (int i = 0; i < arr.length; i++) {
			String id = arr[i].trim();
			if (!id.equals("")) {
				saida = saida + id + " ";
			}
		}
		if ((keyword1 != null) || (keyword2 != null)) {
			saida += Extras.KEYWORD_TAG;
			if (keyword1 != null) {
				saida += keyword1;
			}
			if ((keyword1 != null) && (keyword2 != null)) {
				saida += " ";
			}
			if (keyword2 != null) {
				saida += keyword2;
			}

		}
		saida += "}";
		saida = saida.replace(",}", "}");
		saida = saida.replace(" }", "}");
		saida = saida.replace("  ", " ");
		return saida;
	}

	public static String calcFunctionLista(String oper1, String oper2, String oper) {
		return StringHelper.concatenaLista(oper1, oper2);
	}

	public static String[] getParamsCondicao(String par) {
		String pars[] = new String[3];
		extraiParamsCondicao(par, pars);
		for (int i = 0; i < pars.length; i++) {
			if (pars[i] != null) {
				if (pars[i].endsWith(".0")) {
					pars[i] = pars[i].replace(".0", "");
				}
			}
		}

		return pars;
	}

	private static void extraiParamsCondicao(String par, String[] pars) {
		int p = 0;
		int flagParenteses = 0;
		String s = "";
		for (int i = 0; i < par.length(); i++) {
			if (par.charAt(i) == '(')
				flagParenteses++;
			if (par.charAt(i) == ')') {
				flagParenteses--;

			}
			if ((flagParenteses == 0) && (checaCondicao(par.charAt(i)))) {
				pars[p] = s;
				p++;
				pars[p] = "" + par.charAt(i);
				if (checaCondicao(par.charAt(i + 1))) {
					pars[p] = pars[p] + par.charAt(i + 1);
					i++;
				}
				p++;
				s = "";
			} else
				s = s + par.charAt(i);
		}
		pars[p] = s;
	}

	public static boolean checaCondicao(char c) {
		if (c == '<')
			return true;
		if (c == '>')
			return true;
		if (c == '=')
			return true;
		if (c == '!')
			return true;
		return false;
	}

	public static boolean checaCondicaoBoolean(String s) {
		if (s.equals("<"))
			return true;
		if (s.equals("<="))
			return true;
		if (s.equals(">"))
			return true;
		if (s.equals(">="))
			return true;
		if (s.equals("="))
			return true;
		if (s.equals("=="))
			return true;
		if (s.equals("!="))
			return true;
		return false;
	}

	public static boolean checaParametroNumerico(char c) {
		/*
		 * if (checaCondicao(c)) return true;
		 */
		if (c == ',')
			return true;

		/*
		 * if (!isFirst){ if (needsSolving(c+""))return true; }
		 */
		// if (c=='[')return true;
		// if (c==']')return true;
		return false;
	}

	public static boolean needsSolving(String parenteses) {
		if (parenteses.contains("+")) {
			return true;
		}
		if (parenteses.contains("-")) {
			return true;
		}
		if (parenteses.contains("*")) {
			return true;
		}
		if (parenteses.contains("/")) {
			return true;
		}
		if (parenteses.contains("%")) {
			return true;
		}
		return false;
	}

	private static String getParam(int n, String par) {
		return StringHelper.getParam(n, par);
	}

	private static String[] getParams(String par) {
		return StringHelper.getParams(par);
	}

	public static boolean areParamsNumeric(String params) {
		String[] pars = getParams(params);
		for (int i = 0; i < pars.length; i++) {
			String par = pars[i];
			if (!CRJavaUtils.isNumber(par)) {
				return false;
			}
		}
		return true;
	}

	

}
