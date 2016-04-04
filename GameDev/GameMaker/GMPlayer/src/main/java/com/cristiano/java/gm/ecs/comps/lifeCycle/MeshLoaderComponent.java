package com.cristiano.java.gm.ecs.comps.lifeCycle;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;


public class MeshLoaderComponent extends GameComponent {

	public IGameElement meshElement=null;
	
	//final HashMap<String,IGameElement> materials=new HashMap<String,IGameElement>();
	
	public MeshLoaderComponent(){
		super(GameComps.COMP_MESH_LOADER);
	}
	
	@Override
	public void free() {
		super.free();
		meshElement=null;
		//materials.clear();;
	}
	
	@Override
	public IGameComponent clonaComponent() {
		MeshLoaderComponent ret = new MeshLoaderComponent();
		ret.meshElement=meshElement;
		return ret;
	}
	@Override
	public void resetComponent() {
	}

	/*
	 * //this method will reuse the same materials for the entity... 
	public IGameElement findMaterial(IGameElement materialType, IManageElements em) {
		IGameElement ge = materials.get(materialType);
		if (ge!=null){
			return ge;
		}
		if (em==null){
			Log.fatal("IManageElements is null");
		}
		String tag = materialType+" "+em.getVar(GameConsts.MATERIAL_ROOT_VAR);
		ge=em.pickFinal(tag,getElement());
		if (ge==null){
			Log.error("No material was found with tag '"+tag+"'");
			return null;
		}
		materials.put(materialType,ge);
		return ge;
	}*/

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return false;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		Log.todo("Verificar como importar meshLoader");
	}
}
