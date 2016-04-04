package com.cristiano.java.gm.viewers;

import java.awt.EventQueue;

import com.cristiano.utils.CRJavaUtils;

public class GMPhysEditor extends GMEditor{

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GMEditor canvasApplication = new GMPhysEditor();
					canvasApplication.startEditor();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public GMPhysEditor() {
		super("PhysEditor");
		rootTag = "macroDefinition physEdit leaf";//ModelPhysEditMacroDefinitions
		CRJavaUtils.IS_PHYSICS_ON = true;
	}
}
