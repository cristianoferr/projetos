package com.cristiano.java.KeyWordSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;

public class Domains {
	HashMap<String,List<IGameElement>> tagsMap;

	
	public Domains(){
		tagsMap=new HashMap<String,List<IGameElement>>();

	}
	
	public void addToDomain(String domain,AbstractElement PropertyObject){
		PropertyObject.addTag(domain);
	}
	
	public List<IGameElement> getElementsWithTag(String tag){
		tag=StringHelper.removeChaves(tag);
		//se a tag contem espa�o ent�o � uma tag composta, se for composta, ent�o � intersect
		if (tag.contains(" ")){

			String[] parts = tag.split(" ");
			String partAnt=parts[0];
			List<IGameElement> result=getElementsWithTag(partAnt);
			for (int i=1;i<parts.length;i++){
				if (!parts[i].trim().equals("")){
					result = separaTag(parts, result, i);
				}
			}
			
			return result;
		} else { 
			if (tag.startsWith("!")){
				tag=tag.substring(1);
				return exclude(Extras.TAG_ALL,tag);
			}
			return getTags(tag);
		}
		
	}

	private List<IGameElement> separaTag(String[] parts,
			List<IGameElement> result, int i) {
		/*String partAnt;
		partAnt=parts[i-1];*/
		String tagAtual=parts[i];
		
		boolean flagUnion=false;
		if (tagAtual.startsWith("+")){
			tagAtual=tagAtual.substring(1);
			flagUnion=true;
		}
		
		
		List<IGameElement> resultToAdd=getElementsWithTag(tagAtual);
		
		if (flagUnion){
			result=union(result, resultToAdd);
		} else {
			result=intersect(result, resultToAdd);
		}
		return result;
	}
	
	public List<IGameElement> getTags(String tag) {
		tag=StringHelper.removeChaves(tag);
		List<IGameElement>tags=tagsMap.get(tag);
		if (tags==null){
			tags=new ArrayList<IGameElement>();
			tagsMap.put(tag, tags);
		}
		return tags;
	}
	

	public List<IGameElement> intersect(String result, String resultToAdd) {
		List<IGameElement> d1=getElementsWithTag(result);
		List<IGameElement> d2=getElementsWithTag(resultToAdd);
		
		return intersect(d1, d2);
	}

	public List<IGameElement> intersect(List<IGameElement> d1,
			List<IGameElement> d2) {
		List<IGameElement> result=new ArrayList<IGameElement>(d1);
		for (int i=0;i<d1.size();i++){
			IGameElement ge=d1.get(i);
			if (!d2.contains(ge)){
				result.remove(ge);
			}
		}
		
		return result;
	}

	public List<IGameElement> exclude(String source, String excluded) {
		List<IGameElement> d1=getElementsWithTag(source);
		List<IGameElement> d2=getElementsWithTag(excluded);
		
		return exclude(d1, d2);
	}

	private List<IGameElement> exclude(List<IGameElement> d1,
			List<IGameElement> d2) {
		List<IGameElement> result=new ArrayList<IGameElement>(d1);
		result.removeAll(d2);

		return result;
	}

	public List<IGameElement> union(String domain1, String domain2) {
		List<IGameElement> d1=getElementsWithTag(domain1);
		List<IGameElement> d2=getElementsWithTag(domain2);
		
		return union(d1, d2);
	}

	private List<IGameElement> union(List<IGameElement> d1,
			List<IGameElement> d2) {
		List<IGameElement> result=new ArrayList<IGameElement>(d1);
		
		for (int i=0;i<d2.size();i++){
			IGameElement ge=d2.get(i);
			if (!result.contains(ge)){
				result.add(ge);
			}
		}
		
		return result;
	}
}
