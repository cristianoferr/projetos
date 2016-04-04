package com.cristiano.java.genProg;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.blueprint.Template;

public class GPManager {
	ElementManager emBase;
	GPSettings settings;
	
	ArrayList<GPItem> itemsMutaveis;
	
	
	
	public GPManager(GPSettings gps, ElementManager em) {
		this.emBase=em;
		itemsMutaveis=new ArrayList<GPItem>();
		settings=gps;
	}
	
	/**
	 * Ao passar uma rootTag (tag raiz), eu gero MAX_VARIACOES de varia��es aleat�rias em cima dessa tag
	 * Como blueprint j� faz esse trabalho por conta pr�pria ent�o tudo o que tenho que fazer � gerar elementos finais.
	 * @param rootTag
	 */
	public void geraVariacoes(String rootTag) {
		itemsMutaveis.clear();
		List<AbstractElement> elementsWithTag = emBase.getElementsWithTag(rootTag);
		for (int i=0;i<settings.getMaxVariacoes();i++){
			//System.out.println("i:"+i);
			Template geResolved = emBase.createFinalElementFromTag(rootTag);
			System.out.println("i:"+i+" "+geResolved);
			itemsMutaveis.add(criaGPItem(geResolved));
		}
	}
	
	public int size(){
		return itemsMutaveis.size();
	}

	private GPItem criaGPItem(Template propertyObject) {
		GPItem genProgItem=new GPItem(settings,propertyObject);
		return genProgItem;
	}

	public ArrayList<GPItem> getItemsMutaveis() {
		return itemsMutaveis;
	}

	}
