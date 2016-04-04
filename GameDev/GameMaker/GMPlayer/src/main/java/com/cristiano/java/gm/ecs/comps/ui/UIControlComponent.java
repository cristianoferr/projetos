package com.cristiano.java.gm.ecs.comps.ui;

import java.util.concurrent.Callable;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

public class UIControlComponent extends AbstractUIComponent {
	public int dataSet = 0;
	public TextRenderer textRenderer;
	private String _lastText = null;

	public ElementBuilder _controlBuilder = null;
	//public Future future = null;// create controls
	public Nifty _nifty;

	public UIControlComponent() {
		super(GameComps.COMP_UI_CONTROL);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);

		dataSet = ge.getPropertyAsInt(GameProperties.LABEL_SOURCE);
		isVisible = ge.getPropertyAsBoolean(GameProperties.VISIBLE);
	}

	@Override
	public IGameComponent clonaComponent() {
		UIControlComponent ret = new UIControlComponent();
		ret.dataSet = dataSet;
		super.finishClone(ret);

		return ret;
	}

	// Loaded automatically...
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		super.exportComponentToJSON(obj);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		super.importComponentFromJSON(obj);
		loadFromElement(getElement());
	}

	@Override
	public void resetComponent() {
	}

	public boolean setText(String value) {
		if (textRenderer() != null) {
			if (value.equals(_lastText)) {
				return true;
			}
			_lastText = value;
			textRenderer.setText(value);

			return true;
		}
		return false;
	}

	private Object textRenderer() {
		if (textRenderer == null) {
			if (niftyElement != null) {
				textRenderer = niftyElement.getRenderer(TextRenderer.class);
			} else {
				Log.error("Nifty element is null!");
			}
		}
		return textRenderer;
	}

	// MUlti-thread
	public Callable<Object> createControl = new Callable<Object>() {
		public Object call() throws Exception {
			Element control = _controlBuilder.build(_nifty, screen, parent);
			return control;
		}
	};

}
