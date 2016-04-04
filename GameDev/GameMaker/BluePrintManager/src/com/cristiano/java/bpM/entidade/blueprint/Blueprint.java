package com.cristiano.java.bpM.entidade.blueprint;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;

public class Blueprint extends GenericElement {
	
	protected IGameElement estende;
	

	public Blueprint(ElementManager elementManager,IGameElement estende, boolean add) {
		super(elementManager,add);
		this.estende=estende;
		setObjectType(Extras.OBJECT_TYPE_BLUEPRINT);
	}

	
	public Blueprint(ElementManager elementManager,IGameElement estende) {
		this(elementManager,estende,true);
	}
	
	public Blueprint getBlueprint(){
		if (this.isMastered()){
			return ((Blueprint) estende).getBlueprint();
		}
		return this;
	}
	

	protected void removeDesignTags() {
		removeTag(Extras.TAG_BLUEPRINT);
		removeTag(Extras.TAG_FACTORY);
		removeTag(Extras.TAG_MOD);
		removeTag(Extras.TAG_LEAF);
		removeTag(Extras.TAG_TEMPLATE);
	}
	protected void removeDesignLists() {
		getParams().removeList(Extras.LIST_REPLICATE);
		getParams().removeList(Extras.LIST_COMMENT);
		getParams().removeList(Extras.LIST_OUTPUT);
		getParams().removeList(Extras.LIST_ITERATOR);
		
	}
	
	private String resolve(String value) {
		return value;
	}

	public boolean isMastered(){
		return false;
	}
	
	public String getIdentifier() {
		String ident=getPropertyH("identifier", true).replace("'", "");
		return ident;
		
	}
	
	public String id(){
		return "BP"+super.ID;
	}



	public IGameElement getEstende() {
		return estende;
	}

	//funcao espec�fica que verifica se o item que ser� criado contem domain...
	public boolean createdItemContainsDomain(String domain) {
		String domains=getParamH(Extras.LIST_DOMAIN,"type",true);
		domain=domain.toLowerCase().trim();
		domains=StringHelper.removeChaves(domains.toLowerCase());
		
		if (domains.contains(domain)){
			String[]arrDomains=domains.split(" ");
			for (int i=0;i<arrDomains.length;i++){
				if (arrDomains[i].trim().equals(domain))return true;
			}
		}
		return false;
	}


	



}
