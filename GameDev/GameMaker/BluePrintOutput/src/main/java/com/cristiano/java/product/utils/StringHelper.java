package com.cristiano.java.product.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cristiano.consts.Extras;
import com.cristiano.java.product.IGameElement;

public abstract class StringHelper {
	public static String pickRandomItemFromList(String lista) {
		lista = lista.replace("[", "");
		lista = lista.replace("]", "");
		String[] arr = lista.split(",");
		int size = arr.length;
		if (size == 0)
			return null;
		int pos = (int) (size * Math.random());
		return "[" + arr[pos] + "]";
	}

	public static String[] splitList(String lista) {
		lista = lista.replace("[", "").replace("]", "").replace("{", "")
				.replace("}", "").trim();
		if (lista.equals(""))
			return null;
		String[] arr = lista.split(" ");
		for (int i=0;i<arr.length-1;i++){
			for (int j=i+2;j<arr.length;j++){
				if (arr[i].equals(arr[j])){
					arr[j]="";
				}
			}
		}
		return arr;
	}

	public static void sortStringArray(List<String> parms) {
		for (int i = 0; i < parms.size() - 1; i++) {
			for (int j = i + 1; j < parms.size(); j++) {
				String si = parms.get(i);
				String sj = parms.get(j);
				if (si.compareTo(sj) > 0) {
					parms.set(i, sj);
					parms.set(j, si);
				}
			}
		}
	}

	public static String converteFloatToString(double d) {
		String txt;
		txt = Double.toString(d);
		if (txt.endsWith(".0")){
			txt = txt.replace(".0", "");}
		return txt;
	}

	public static int pegaPosicaoFinalPalavra(String property, int posInicial,
			boolean flagIncluiPonto) {
		int posFim = posInicial;
		if (posFim > property.length()) {
			return property.length();
		}
		char c = property.toLowerCase().charAt(posFim);
		while (((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'z'))
				|| ((flagIncluiPonto) && (c == '.'))) {

			posFim++;
			if (posFim >= property.length())
				break;
			c = property.toLowerCase().charAt(posFim);
		}
		return posFim;
	}

	public static String pegaPalavraNaPosicao(String property, int pos,
			boolean flagIncluiPonto) {
		String propName;
		if (pos > property.length()) {
			return "";
		}
		propName = property.substring(pos, StringHelper
				.pegaPosicaoFinalPalavra(property, pos, flagIncluiPonto));
		return propName;
	}

	public static int countItemFromList(String lista) {
		lista = lista.replace("[", "");
		lista = lista.replace("]", "");
		String[] arr = lista.split(",");
		int saida = arr.length;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].trim().equals("")){
				saida--;}
		}
		return saida;
	}

	public static String concatenaLista(String l1, String l2) {
		l1 = removeColchetes(l1);
		l2 = removeColchetes(l2);
		l1 = l1 + "," + l2;
		String[] arr = l1.split(",");
		String saida = "";
		for (int i = 0; i < arr.length; i++) {
			String id = arr[i].trim();
			if (!"".equals(id)) {
				if (!saida.contains(id + ",")){
					saida = saida + id + ",";}
			}
		}

		saida = ordenaLista(saida);

		return saida;
	}

	private static String ordenaLista(String saida) {
		String[] arr;
		arr = saida.split(",");
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = i + 1; j < arr.length - 1; j++) {
				if (arr[i].compareToIgnoreCase(arr[j]) > 0) {
					String aux = arr[i];
					arr[i] = arr[j];
					arr[j] = aux;
				}
			}
		}

		saida = "[";
		for (int i = 0; i < arr.length; i++) {
			saida = saida + arr[i] + ",";
		}

		saida += "]";
		saida = saida.replace(",]", "]");
		return saida;
	}

	public static String[] getParamsFromFuncao(String funcao, String par) {
		if (par.contains(funcao + "(")) {
			par = par.replace(funcao + "(", "");
			par = par.substring(0, par.lastIndexOf(")"));
			return getParams(par);
		}
		return null;
	}

	public static String getParam(int n, String par) {
		String[] pars = getParams(par);
		if (n >= pars.length)
			return null;
		return pars[n];
	}

	public static String[] getParams(String par) {
		String pars = "";
		int p = 0;
		int flagParenteses = 0;
		String s = "";
		for (int i = 0; i < par.length(); i++) {
			if (par.charAt(i) == '(')
				flagParenteses++;
			if (par.charAt(i) == ')') {
				flagParenteses--;

			}
			if ((flagParenteses == 0) && (par.charAt(i) == ',')) {
				pars += s + "~";
				p++;
				s = "";
			} else
				s = s + par.charAt(i);
		}
		pars += s + "~";

		String[] arrPars = pars.split("~");

		for (int i = 0; i < arrPars.length; i++) {
			if (arrPars[i] != null)
				if (arrPars[i].endsWith(".0"))
					arrPars[i] = arrPars[i].replace(".0", "");
		}

		return arrPars;
	}

	public static boolean checkIfListContainsItem(String lista, String item1) {
		item1 = item1.replace("[", "").replace("]", "");
		if (item1.trim().equals(""))
			return false;
		return lista.contains(item1.replace("[", "").replace("]", ""));

	}

	public static String removeChaves(String tagList) {
		if (tagList==null){
			return "";
		}
		return tagList.replace("{", "").replace("}", "").trim();
	}

	public static String removeColchetes(String tagList) {
		return tagList.replace("[", "").replace("]", "").trim();
	}

	public static String removeParenteses(String param) {
		param = param.replace("(", "");
		param = param.replace(")", "");
		return param;
	}

	public static String[] getElementosFuncao(String param) {
		param = param.trim();
		String function = param.substring(0, param.indexOf("("));
		String[] params = getParamsFromFuncao(function, param);
		String[] ret = new String[params.length + 1];
		ret[0] = function;
		for (int i = 0; i < params.length; i++) {
			ret[i + 1] = params[i];
		}
		return ret;

	}

	public static String pegaKeywordTags(String oper1) {
		if (!oper1.contains(Extras.KEYWORD_TAG)) {
			return null;
		}
		String ret = oper1.substring(oper1.indexOf(Extras.KEYWORD_TAG)
				+ Extras.KEYWORD_TAG.length());
		return ret;
	}

	public static String removeKeyword(String oper) {
		if (!oper.contains(Extras.KEYWORD_TAG)) {
			return oper;
		}
		String ret = oper.substring(0, oper.indexOf(Extras.KEYWORD_TAG));
		return ret;
	}

	public static String clear(String prop) {
		return removeColchetes(removeChaves(prop.replace("'", ""))).trim();
	}

	public static String serializaHashMap(HashMap<String, String> paramData) {
		Iterator it = paramData.entrySet().iterator();
		String ret=" ";
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String key = (String) pairs.getKey();
			String iter = paramData.get(key);
			ret+=key+"="+iter+",";
		}
		return ret.substring(0,ret.length()-1).trim();
	}
	
	public static void applyChildPropertiesFromMap(HashMap<String, String> paramData,IGameElement ge,String objname) {
		Iterator it = paramData.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String key = (String) pairs.getKey();
			String iter = paramData.get(key);
			ge.setParam(Extras.LIST_CHILD_PROPERTY,objname+"."+key,iter);
		}
	}

	public static boolean listContainsItem(String[] scopeGame, String identifier) {
		for (String s:scopeGame){
			if (s.equals(identifier)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkList(String identifier, IGameElement opposition, String listName,String scopeGame) {
		String[] list = opposition.getParamAsList(listName,scopeGame);
		for (String st:list){
			if (st.equals(identifier)){
				return true;
			}
		}
		return false;
	}
}
