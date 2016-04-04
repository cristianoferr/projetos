package com.cristiano.java.bpM.entidade.blueprint;

import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class Factory extends Blueprint{

	public Factory(ElementManager elementManager,IGameElement estende) {
		super(elementManager,estende);
		setObjectType(Extras.OBJECT_TYPE_FACTORY);
	}

	public AbstractElement fabricaItem() {
		String idBase=getParamH(Extras.LIST_GENERATOR, "base", true);
		IGameElement geBase=elementManager.getElementWithID(idBase);
		//System.out.println("base:"+idBase);
		if (geBase==null) {
			Log.error("Factory recebeu um item base nulo, idBase:"+idBase);
		}
		AbstractElement geIntermediario;
		geIntermediario= elementManager.createAbstractElement(geBase,false);
		geIntermediario.setVar(Extras.VAR_FINAL, "");
		this.setVar(Extras.VAR_FINAL, "");
		aplicaPropertiesDaFactory(geIntermediario); 
		AbstractElement geFinal=elementManager.createFinalElement(geIntermediario);
		
		return geFinal;
	}

	public String id(){
		return "FACT"+super.ID;
	}
	
	private void aplicaPropertiesDaFactory(AbstractElement geIntermediario) {
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_PROPERTY);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_POSITION);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_DOMAIN);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_ORIENTATION);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_NODETYPE);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_ITERATOR);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_ROTATION);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_CHILD_PROPERTY);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_REPLICATE);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_MOD);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_MESH);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_MATERIAL);
		aplicaPropriedadesDaLista(geIntermediario, Extras.LIST_OBJECT);
	}

	private void aplicaPropriedadesDaLista(AbstractElement geIntermediario, String list) {
		List<String> arr=getAllParams(list,true);
		for (int i=0;i<arr.size();i++){	
			String param=arr.get(i);
			String value=getParamH(list,param,false);
			String modif="=";
			if (!param.equals(Extras.PROPERTY_IDENTIFIER)){
				geIntermediario.setParam(list, param + " " + modif
						+ " " + value);
			}
		}
	}

	
}
