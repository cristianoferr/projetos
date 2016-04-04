/*
 * Classe para aliviar o Function, s� ter� m�todos p�blicos est�ticos aqui...
 */

package com.cristiano.java.bpM.utils;

import java.util.HashMap;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.functions.Solver;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;

/*
 * Unit Tests: TestBlueprintFunctions
 * */
public class DomainSpecificFunctionHelper {

	private static final String MATERIAL_TYPE = "material#type";
	private static final String PAR_MERGE = "merge";
	private static final String PAR_REPLICATE = "replicate";
	private static final String DEPTH = "depth";
	private static final String HEIGHT = "height";
	private static final String WIDTH = "width";
	private static final String PAR_MIN_ANGLE = "minAngle";
	private static final String PAR_MAX_ANGLE = "maxAngle";
	private static final String PAR_RADIUS = "radius";
	private static final Object PAR_SIZE_RADIUS = "sizeRadius";
	private static final Object PAR_MATERIAL = "material";
	private static final String PAR_FINAL = "final";//1=pickFinal,0=pickOne

	public static String resolveFuncaoAddMesh(String texto, Solver fromFunction) {
		String[] parms = StringHelper.getParams(texto);
		String meshBase = fromFunction.resolveFunctionOf("$addMeshRoot");
		// fromFunction.getElementManager()
		AbstractElement gameElement = fromFunction.getGameElement();
		
		if (fromFunction.lastParam==null){
			Log.error("No lastParam defined...");
		}
		String meshTag = parms[0];
		String objectName = fromFunction.lastParam.getIdentifier();
		HashMap<String,String> paramData=new HashMap<String,String>();
		
		initMeshElement(gameElement, objectName,paramData);
		
		applyProperties(parms, gameElement, objectName, paramData);
		
		finishMeshElement(gameElement, meshTag, objectName, paramData);

		return "solved";
	}

	private static void finishMeshElement(AbstractElement gameElement, String meshTag, String objectName,
			HashMap<String, String> paramData) {
		boolean isFinal=true;
		String finalPar=paramData.get(PAR_FINAL);
		if (finalPar!=null ){
			isFinal=finalPar.equals("1");
		}
		
		gameElement.setParam("@"+Extras.LIST_ITERATOR+" "+objectName+".angle.initial="+paramData.get(PAR_MIN_ANGLE));
		gameElement.setParam("@"+Extras.LIST_ITERATOR+" "+objectName+".angle.final="+paramData.get(PAR_MAX_ANGLE));
		paramData.remove(PAR_MIN_ANGLE);
		paramData.remove(PAR_MAX_ANGLE);
		paramData.remove(PAR_FINAL);
		
		String serializaHashMap = StringHelper.serializaHashMap(paramData);
		StringHelper.applyChildPropertiesFromMap(paramData,gameElement,objectName);
		
		ElementManager em = gameElement.getElementManager();
		String solvedSerialize = gameElement.resolveFunctionOf(serializaHashMap);
		
		IGameElement finalMesh;
		if (isFinal){
			finalMesh= (AbstractElement) em.pickFinal(meshTag,null,solvedSerialize);
		} else {
			finalMesh= em.pickOne(meshTag,solvedSerialize);
		}
		if (finalMesh==null){
			Log.fatal("No mesh could be generated from: "+meshTag);
			return;
		}
		gameElement.setParam("@"+Extras.LIST_OBJECT+" "+objectName+"=["+finalMesh.id()+"]");
	}


	private static void applyProperties(String[] parms, AbstractElement gameElement, String objectName,
			HashMap<String, String> paramData) {
		for (int i = 1; i < parms.length; i++) {
			/*String[] param = parms[i].replace("#", "$").split("=");
			if (param.length != 2) {
				Log.error("Wrong number of parameters: " + parms[i]);
			}*/
			int posSeparator = parms[i].indexOf("=");
			String ident=parms[i].substring(0,posSeparator);
			String value=parms[i].substring(posSeparator+1).replace("#", "$");

			applyProperty(gameElement, ident, value, objectName,paramData);
		}
	}

	private static void initMeshElement(AbstractElement gameElement, String objectName, HashMap<String, String> paramData) {
		gameElement.setParam("@"+Extras.LIST_POSITION+" "+objectName+".x=0");
		gameElement.setParam("@"+Extras.LIST_POSITION+" "+objectName+".y=0");
		gameElement.setParam("@"+Extras.LIST_POSITION+" "+objectName+".z=0");
		gameElement.setParam("@"+Extras.LIST_POSITION+" "+objectName+".radius=0");
		gameElement.setParam("@"+Extras.LIST_ORIENTATION+" "+objectName+".x=0");
		gameElement.setParam("@"+Extras.LIST_ORIENTATION+" "+objectName+".y=0");
		gameElement.setParam("@"+Extras.LIST_ORIENTATION+" "+objectName+".z=0");
		gameElement.setParam("@"+Extras.LIST_REPLICATE+" "+objectName+"=1");
		//gameElement.setParam("@"+Extras.LIST_OBJECT+" "+objectName+"=$this."+objectName+"Obj");
		gameElement.setParam("@"+Extras.LIST_PROPERTY+" "+Extras.PROPERTY_POINTLIST+"+=' "+objectName+" "+gameElement.getProperty(Extras.PROPERTY_POINTLIST)+"'");
		
		paramData.put(WIDTH,gameElement.resolveFunctionOf(gameElement.getProperty("width")));
		paramData.put(HEIGHT,gameElement.resolveFunctionOf(gameElement.getProperty("height")));
		paramData.put(DEPTH,gameElement.resolveFunctionOf(gameElement.getProperty("depth")));
		paramData.put(PAR_MIN_ANGLE,"0");
		paramData.put(PAR_MAX_ANGLE,"360");
	}

	private static void applyProperty(AbstractElement gameElement, String param, String value, String objectName, HashMap<String, String> paramData) {
		if (checkSizes(gameElement, param, value, objectName, paramData)){
			return;
		}
		if (checkOrientations(gameElement, param, value, objectName)){
			return;
		}
		if (checkRotations(gameElement, param, value, objectName)){
			return;
		}
		if (checkPositions(gameElement, param, value, objectName)){
			return;
		}
		if (checkMirror(gameElement, param, value, objectName)){
			return;
		}
		
		if (param.equals(PAR_REPLICATE)) {
			gameElement.setParam(Extras.LIST_REPLICATE, objectName, value);
			return;
		}
		if (param.equals(PAR_RADIUS)) {
			gameElement.setParam("@"+Extras.LIST_POSITION+" "+objectName+"."+Extras.PROPERTY_RADIUS+"="+value);
			return;
		}
		if (param.equals(PAR_FINAL)) {
			paramData.put(PAR_FINAL, value);
			return;
		}
		if (param.equals(PAR_SIZE_RADIUS)) {
			paramData.put(PAR_RADIUS, value);
			return;
		}
		
		if (param.equals(PAR_MIN_ANGLE)) {
			paramData.put(PAR_MIN_ANGLE,value);
			return;
		}
		if (param.equals(PAR_MATERIAL)) {
			paramData.put(MATERIAL_TYPE,value);
			//gameElement.setParam("@"+Extras.LIST_MATERIAL+" "+Extras.PROPERTY_TYPE+"="+value);
			return;
		}
		
		if (param.equals(PAR_MAX_ANGLE)) {
			paramData.put(PAR_MAX_ANGLE,value);
			return;
		}
		
		if (param.equals(PAR_MERGE)) {
			String val = "newsubnode";
			if (value.equals("1")) {
				val = PAR_MERGE;
			}
			gameElement.setParam(Extras.LIST_NODETYPE, objectName, "enumValue({" + val + " subnodetype})");
			return;
		}
		
		paramData.put(param,value);
		//Log.error("Unknown property:" + param);
	}

	private static boolean checkMirror(AbstractElement gameElement, String param, String value, String objectName) {
		if (checkMirror(param, "x", gameElement, objectName, value)) {
			return true;
		}
		if (checkMirror(param, "y", gameElement, objectName, value)) {
			return true;
		}
		if (checkMirror(param, "z", gameElement, objectName, value)) {
			return true;
		}
		return false;
	}

	private static boolean checkMirror(String param, String axis, AbstractElement gameElement, String objectName, String value) {
		String mirrorProp = "mirror" + axis.toUpperCase();
		if (param.equals(mirrorProp)) {
			gameElement.setParam(Extras.LIST_CHILD_PROPERTY, objectName + "." + mirrorProp, value);
			return true;
		}
		return false;
	}

	private static boolean checkPositions(AbstractElement gameElement, String param, String value, String objectName) {
		if (checkPosition(param, "x", gameElement, objectName, value, WIDTH)) {
			return true;
		}
		if (checkPosition(param, "y", gameElement, objectName, value, HEIGHT)) {
			return true;
		}
		if (checkPosition(param, "z", gameElement, objectName, value, DEPTH)) {
			return true;
		}
		return false;
	}

	private static boolean checkOrientations(AbstractElement gameElement, String param, String value, String objectName) {
		if (checkOrientation(param, "x", gameElement, objectName, value)) {
			return true;
		}
		if (checkOrientation(param, "y", gameElement, objectName, value)) {
			return true;
			
		}
		if (checkOrientation(param, "z", gameElement, objectName, value)) {
			return true;
		}
		return false;
	}
	private static boolean checkRotations(AbstractElement gameElement, String param, String value, String objectName) {
		if (checkRotation(param, "x", gameElement, objectName, value)) {
			return true;
		}
		if (checkRotation(param, "y", gameElement, objectName, value)) {
			return true;
			
		}
		if (checkRotation(param, "z", gameElement, objectName, value)) {
			return true;
		}
		return false;
	}

	private static boolean checkSizes(AbstractElement gameElement, String param, String value, String objectName,
			HashMap<String, String> paramData) {
		if (checkSize(param, "x", gameElement, objectName, value, WIDTH,paramData)) {
			return true;
		}
		if (checkSize(param, "y", gameElement, objectName, value, HEIGHT,paramData)) {
			return true;
		}
		if (checkSize(param, "z", gameElement, objectName, value, DEPTH,paramData)) {
			return true;
		}
		return false;
	}

	private static boolean checkSize(String param, String axis, AbstractElement gameElement, String objectName,
			String value, String absolute, HashMap<String, String> paramData) {
		if (param.equals("size" + axis.toUpperCase())) {
			String finalValue=value+"*" + gameElement.resolveFunctionOf(gameElement.getProperty(absolute));
			paramData.put(absolute,finalValue);
			gameElement.setParam(Extras.LIST_CHILD_PROPERTY, objectName + "." + absolute, finalValue);
			return true;
		}
		return false;
	}

	private static boolean checkPosition(String param, String axis, AbstractElement gameElement, String objectName,
			String value, String absolute) {
		if (param.equals("pos" + axis.toUpperCase())) {
			gameElement.setParam(Extras.LIST_POSITION, objectName + "." + axis, value + "*$this." + absolute + "/2");
			return true;
		}
		return false;
	}
	private static boolean checkOrientation(String param, String axis, AbstractElement gameElement, String objectName,
			String value) {
		if (param.equals("orient" + axis.toUpperCase())) {
			gameElement.setParam(Extras.LIST_ORIENTATION, objectName + "." + axis, value );
			return true;
		}
		return false;
	}
	private static boolean checkRotation(String param, String axis, AbstractElement gameElement, String objectName,
			String value) {
		if (param.equals("rot" + axis.toUpperCase())) {
			gameElement.setParam(Extras.LIST_ROTATION, objectName + "." + axis, value );
			return true;
		}
		return false;
	}

}
