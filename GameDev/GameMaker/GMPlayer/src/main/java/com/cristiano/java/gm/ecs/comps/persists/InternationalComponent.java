package com.cristiano.java.gm.ecs.comps.persists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;


public class InternationalComponent extends GameComponent {

	HashMap<String,TextKey> textKeys=new HashMap<String,TextKey>();
	
	public String currentLanguage;

	public final List<IGameElement> languages=new ArrayList<IGameElement>();

	private String defaultLanguage;


	public InternationalComponent(){
		super(GameComps.COMP_INTERNATIONAL);
	}
	
	@Override
	public void free() {
		super.free();
		languages.clear();
		textKeys.clear();
		
	}
	
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		ge.getObjectList(GameProperties.LANGUAGES,languages);
		this.currentLanguage=ge.getProperty(GameProperties.CURRENT_LANGUAGE);
		this.defaultLanguage=ge.getProperty(GameProperties.DEFAULT_LANGUAGE);
		List<IGameElement> strings = ge.getObjectList(GameProperties.STRINGS);
		
		loadKeys(strings);
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.LANGUAGES,CRJsonUtils.exportList(languages));
		obj.put(GameProperties.CURRENT_LANGUAGE,currentLanguage);
		obj.put(GameProperties.DEFAULT_LANGUAGE,defaultLanguage);
		obj.put(GameProperties.TEXT, CRJsonUtils.exportMap(textKeys));
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
		CRJsonUtils.importList((JSONObject) obj.get(GameProperties.LANGUAGES), factory);
		currentLanguage=(String) obj.get(GameProperties.CURRENT_LANGUAGE);
		defaultLanguage=(String) obj.get(GameProperties.DEFAULT_LANGUAGE);
		
		textKeys=(HashMap<String, TextKey>) CRJsonUtils.importMap((JSONObject) obj.get(GameProperties.TEXT), factory);
		
		if (textKeys.isEmpty()){
			Log.fatal("TextKeys is empty!");
		}
		if (languages.isEmpty()){
			Log.fatal("languages is empty!");
		}
	}

	private void loadKeys(List<IGameElement> strings) {
		for (IGameElement elString:strings){
			String name=elString.getName();
			Log.trace("Loading textKey:"+name);
			textKeys.put(name, new TextKey(elString));
		}
		
	}
	
	private TextKey getTextkey(String key){
		return textKeys.get(key);
	}
	
	public String getValue(String key){
		TextKey textkey = getTextkey(key);
		if (textkey==null){
			//Log.warn("No text key found for: "+key);
			return null;
		}
		return textkey.getText(currentLanguage,defaultLanguage);
	}
	

	@Override
	public IGameComponent clonaComponent() {
		InternationalComponent ret=new InternationalComponent();
		ret.textKeys=textKeys;
		ret.currentLanguage=currentLanguage;
		ret.defaultLanguage=defaultLanguage;
		ret.languages.addAll(languages);
		return ret;
	}
	@Override
	public void resetComponent() {
	}

	public String translate(String value) {
		int pos=value.indexOf("#");
		if (pos>=0){
			int posFim = StringHelper.pegaPosicaoFinalPalavra(value,
					pos + 1, false);
			String var = value.substring(pos+1, posFim);
			String translate = getValue(var);
			if (translate!=null){
				value=value.replace("#"+var, translate);
			}
		}
		return value;
	}

	public void addText(String key, String valor) {
		TextKey textkey = new TextKey(defaultLanguage,valor);
		textKeys.put(key, textkey);
	}

	
}
