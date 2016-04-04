package com.cristiano.java.gm.editors;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.tree.TreePath;

import com.cristiano.java.gm.editors.controllers.EditorController;
import com.cristiano.utils.Log;

public class EditorWindow  {
	ArrayList<EditorPanel> panels = new ArrayList<EditorPanel>();
	private JDialog dialog; 
	

	private EditorController controller;

	/**
	 * Create the dialog.
	 * @param frmEditor 
	 * 
	 * @param controller
	 */
	public EditorWindow(JFrame frmEditor, EditorController controller) {
		dialog=new JDialog(frmEditor);
		
		dialog.setAlwaysOnTop(false);
		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				for (EditorPanel panel:panels){
					panel.resize(arg0.getComponent().getSize(),panels.size());
				}
				windowChanged(arg0.getComponent().getSize(),arg0.getComponent().getLocation());
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				windowChanged(arg0.getComponent().getSize(),arg0.getComponent().getLocation());
			}
		});
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.controller = controller;
		dialog.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		
	}

	protected void windowChanged(Dimension size, Point location) {
		controller.popoutWindowChanged(size,location);
	}

	public void setEditorText(String identifier, String sortText) {
		for (EditorPanel panel:panels){
			if (panel.getIdentifier().equals(identifier)){
				panel.setText(sortText);
			}
		}
	}

	public void updateSelection(TreePath[] selectionPaths) {
		if (selectionPaths==null){
			return;
		}
		for (EditorPanel panel:panels){
			dialog.getContentPane().remove(panel.getPanel());
		}
		panels.clear();
		for (TreePath path : selectionPaths) {
			String identifier = path.getLastPathComponent().toString();
			Log.debug("Identifier:" + identifier);
			EditorPanel panel = new EditorPanel(identifier, controller); 
			panels.add(panel);
			dialog.getContentPane().add(panel.getPanel());
			panel.resize(dialog.getSize(),selectionPaths.length);
		}
		dialog.validate();
		//dialog.resize(dialog.getSize());
		//dialog.repaint();
	}

	public void setVisible(boolean b) {
		dialog.setVisible(b);
	}

	public void setPopOutPosition(Dimension size, Point pos) {
		dialog.setSize(size);
		dialog.setLocation(pos);
	}

	public Window getWindow() {
		return dialog;
	}
}
