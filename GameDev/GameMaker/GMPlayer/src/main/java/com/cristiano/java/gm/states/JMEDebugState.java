package com.cristiano.java.gm.states;

import org.json.simple.JSONObject;

import com.cristiano.utils.Log;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.scene.Geometry;

import de.lessvoid.nifty.input.NiftyInputEvent;

public class JMEDebugState extends JMEAbstractState {

	protected static final String MAPPING_PICK = "mapPick";
	private final static Trigger TRIGGER_CLICK =new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);


	@Override
	public void update(float tpf) {
		super.update(tpf);
	}

	@Override
	public void cleanup() {
		super.cleanup();

	}

	public boolean keyEvent(final NiftyInputEvent inputEvent) {
		return true;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		/*Geometry markEnd = snippets.generateBox(ColorRGBA.Yellow, new Vector3f(1, 150, 1), new Vector3f(1024, 0, 1024));
		rootNode.attachChild(markEnd);
		Geometry markWidth = snippets.generateBox(ColorRGBA.Green, new Vector3f(1, 150, 1), new Vector3f(1024, 0, 0));
		rootNode.attachChild(markWidth);*/
		
		app.getInputManager().addMapping(MAPPING_PICK, TRIGGER_CLICK);
		app.getInputManager().addListener(pickListener,new String[]{MAPPING_PICK});
	}

	@Override
	public void resetComponents() {
		internalEntity.resetComponents();
	}
	
	
	private AnalogListener pickListener = new AnalogListener() {
		public void onAnalog(String name, float intensity, float tpf) 
		{
			if (name.equals(MAPPING_PICK)) {
				Geometry target = snippets.getMouseClickTarget();
				
				if (target!=null){
					String nodeName = target.getName();
					Log.info("Clicou em:"+nodeName);
					if (nodeName.startsWith("NODE")){
						getInfoFrom(nodeName);
					}
				}
			}
		}

		
	};


	@Override
	public JSONObject exportToJSON() {
		return null;
	}

	protected void getInfoFrom(String nodeName) {
		String id=nodeName.replace("NODE", "");
		id=id.substring(0,id.indexOf("-"));
		
		
	}

	@Override
	public void importFromJSON(JSONObject json) {
	}
	@Override
	public int size() {
		return internalEntity.size();
	}
}
