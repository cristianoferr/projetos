package com.cristiano.java.bpM.entidade;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.params.ParamList;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.params.Parametro;
import com.cristiano.utils.Log;

public class GenericElement extends AbstractElement {

	// private ArrayList<GenericElement> children;
	private int children = 0;

	private boolean isAdded;

	public GenericElement(ElementManager elementManager) {
		this(elementManager, true);
	}

	public GenericElement(ElementManager elementManager, boolean add) {
		super();
		this.elementManager = elementManager;
		isAdded = add;
		if (add) {
			if (elementManager != null) {
				elementManager.addElement(this);
			}
		}
		setObjectType(Extras.OBJECT_TYPE_TEMPLATE);
	}

	@Override
	public void addTag(String tag) {
		String solvedTag = resolveFunctionOf(tag);
		super.addTag(solvedTag);
		addTagToManager(solvedTag);
	}

	protected void copyParamsTo(IGameElement itemAmbos, String list, ParamList pl, boolean hierarchical) {
		List<String> keys;
		if (hierarchical) {
			keys = getAllParams(list, true);
		} else {
			keys = pl.getAllKeys();
		}

		copyList(itemAmbos, list, hierarchical, keys);

		for (int i = 0; i < keys.size(); i++) {
			String ident = keys.get(i);
			Parametro pbNovo = getParametro(list, ident);
			if (pbNovo != null) {
				String value = pbNovo.getValue();
				if (((AbstractElement) itemAmbos).checkFunctionSolvable(value)) {
					value = ((AbstractElement) itemAmbos).resolveFunctionOf(value, pbNovo);
					if ((!hierarchical) || (isOkCopyIdentifier(ident))) {
						itemAmbos.setParam(list, ident, value);
					}
				}
			}

		}
	}

	private void copyList(IGameElement itemAmbos, String list, boolean hierarchical, List<String> keys) {
		for (int i = 0; i < keys.size(); i++) {
			String ident = keys.get(i);
			Parametro pbNovo = getParametro(list, ident);
			String value;

			if (pbNovo != null) {
				value = ((AbstractElement) itemAmbos).getParams().aplicaModificadores(list, ident, true, pbNovo, pbNovo.getValue());
				/*
				 * if (checkFunctionSolvable(value)){ Log.info("value:"+value);
				 * value=resolveFunctionOf(value); }
				 */

				if (((AbstractElement) itemAmbos).checkFunctionSolvable(value)) {
					value = ((AbstractElement) itemAmbos).resolveFunctionOf(value, pbNovo);
				}

			} else {
				value = getParamH(list, ident, false);
			}
			if ((!hierarchical) || (isOkCopyIdentifier(ident))) {
				itemAmbos.setParam(list, ident, value);
			}
		}
	}

	private boolean isOkCopyIdentifier(String ident) {
		if (ident.equals(Extras.PROPERTY_IDENTIFIER)) {
			return false;
		}
		return true;
	}

	private void addTagToManager(String tag) {
		if (!isAdded) {
			return;
		}
		if (elementManager != null) {
			String arrTags[] = tag.split(" ");
			for (int i = 0; i < arrTags.length; i++) {
				String t = arrTags[i].trim();
				if ((!t.equals(""))) {
					elementManager.addObjectToTag(this, t);
				}
			}
		}
	}

	public boolean isMastered() {
		return true;
	}

	@Override
	public IGameElement getEstende() {
		return null;
	}

	public void addLeaf(IGameElement bp) {
		children++;
	}

	public boolean hasLeafs() {
		return children > 0;
	}

	public void aplicaParametrosEm(IGameElement itemAmbos) {
		ArrayList<String> arrLists = getParams().getKeysH();
		// property deve ir sempre primeiro...
		String identifier = getIdentifier();
		removeProperty(Extras.PROPERTY_IDENTIFIER);
		aplicaParametrosDaListaNoObjeto(itemAmbos, Extras.LIST_PROPERTY, true);
		for (int i = 0; i < arrLists.size(); i++) {
			String list = arrLists.get(i);
			if (isListOk(list))
				aplicaParametrosDaListaNoObjeto(itemAmbos, list, false);
		}
		setProperty(Extras.PROPERTY_IDENTIFIER, identifier);

	}

	private boolean isListOk(String list) {
		if (list.equals(Extras.LIST_PROPERTY)) {
			return false;
		}
		if (list.equals(Extras.LIST_DOMAIN)) {
			return false;
		}
		return true;
	}

	private void aplicaParametrosDaListaNoObjeto(IGameElement itemAmbos, String list, boolean hierarchical) {
		ParamList pl;
		pl = getParams().getListWithKey(list);
		copyParamsTo(itemAmbos, list, pl, hierarchical);
	}

}
