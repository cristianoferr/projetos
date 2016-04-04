package com.cristiano.java.gm.viewers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.editors.EditorWindow;
import com.cristiano.java.gm.editors.consts.EditorConsts;
import com.cristiano.java.gm.editors.controllers.EditorController;
import com.cristiano.java.gm.visualizadores.GMBuilder;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

public class GMEditor extends GMBuilder {

	private JFrame frmEditor;
	private JPanel mainPanel;

	private JPanel graphPanel;

	EditorController controller = null;
	private EditorWindow editorWindow;
	public JCheckBox chkHullDebugJoints;
	public JCheckBox chkApplyMaterial;
	public JCheckBox chkAndroidMode;
	public JCheckBox chkCleanView;
	public JCheckBox chkDiscreto;
	private DefaultTreeModel treeModel;
	private JTree elTree;
	private JTabbedPane leftPane;
	private JTextField txtTagSearch;
	private DefaultListModel<String> listModel;
	public String titulo="Editor";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CRJavaUtils.ALLWAYS_EXPORT_ELEMENTS=true;
					GMEditor canvasApplication = new GMEditor("Editor");
					canvasApplication.startEditor();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void startEditor() {
		// create new JME appsettings
		AppSettings settings = new AppSettings(true);
		int width = 1800;
		int height = 1200;
		settings.setWidth(width);
		settings.setHeight(height);

		setSettings(settings);
		createCanvas(); // create canvas!
		JmeCanvasContext ctx = (JmeCanvasContext) getContext();
		ctx.setSystemListener(this);
		Dimension dim = new Dimension(width, height);
		ctx.getCanvas().setPreferredSize(dim);

		frmEditor.setVisible(true);
		setPauseOnLostFocus(false);
		startCanvas();
		
	}

	/**
	 * Create the application.
	 * @param titulo 
	 */
	public GMEditor(String titulo) {
		super();
		this.titulo=titulo;
		initSwing();
		rootTag = "macroDefinition modelEdit leaf";
		controller = new EditorController(this);
		controller.listModel = listModel;
		CRJavaUtils.IS_PHYSICS_ON = false;
		CRJavaUtils.IS_EDITOR=true;
	}

	@Override
	public void simpleInitApp() {
		super.simpleInitApp();
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(50);
		/*
		 * AmbientLight al = new AmbientLight();
		 * al.setColor(ColorRGBA.White.mult(12)); rootNode.addLight(al);
		 */
		flyCam.setEnabled(true);
		snippets.createSkyBox();
		snippets.addSunLight(rootNode);
	}

	public void loadingComplete() {
		super.loadingComplete();

		controller.initContext(mainPanel);
		editorWindow = new EditorWindow(frmEditor, controller);
		loadTreeElements();
		controller.loadPrefs();
		leftPane.setTitleAt(0, "BPs [" + controller.elementCount() + "]");
		popOut();
		controller.em.getLoader().markEnd();
	}

	private void loadTreeElements() {
		controller.loadTreeElements(treeModel);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initSwing() {
		Log.debug("initializing swing components...");

		frmEditor = new JFrame(titulo);
		frmEditor.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controller.saveIfChanged();
			}
		});
		/*
		 * frmEditor.addWindowFocusListener(new WindowFocusListener() { public
		 * void windowGainedFocus(WindowEvent e) {
		 * editorWindow.setVisible(true); } public void
		 * windowLostFocus(WindowEvent e) { editorWindow.setVisible(false); }
		 * });
		 */
		frmEditor.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent arg0) {
				windowChanged(arg0.getComponent().getSize(), arg0.getComponent().getLocation());
			}

			@Override
			public void componentResized(ComponentEvent e) {
				windowChanged(e.getComponent().getSize(), e.getComponent().getLocation());
			}
		});
		frmEditor.setBounds(100, 100, 850, 553);
		frmEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmEditor.getContentPane().setLayout(springLayout);

		JPanel leftPanel = createLeftPanel(springLayout);
		JPanel rightPanel = createRightPanel(springLayout, leftPanel);

		frmEditor.getContentPane().add(leftPanel);
		frmEditor.getContentPane().add(rightPanel);

		/*
		 * JScrollPane scrollEditor = new JScrollPane(elementEditor);
		 * sl_panel.putConstraint(SpringLayout.NORTH, scrollEditor, 0,
		 * SpringLayout.SOUTH, panelOpc);
		 * 
		 * JButton btPopOut = new JButton("Pop Out");
		 * btPopOut.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { popOut(); } });
		 * sl_panelOpc.putConstraint(SpringLayout.SOUTH, btPopOut, 0,
		 * SpringLayout.SOUTH, btnSalva);
		 * sl_panelOpc.putConstraint(SpringLayout.EAST, btPopOut, -10,
		 * SpringLayout.EAST, panelOpc); panelOpc.add(btPopOut);
		 * sl_panel.putConstraint(SpringLayout.WEST, scrollEditor, 0,
		 * SpringLayout.WEST, panel); sl_panel.putConstraint(SpringLayout.SOUTH,
		 * scrollEditor, 0, SpringLayout.SOUTH, panel);
		 * sl_panel.putConstraint(SpringLayout.EAST, scrollEditor, 0,
		 * SpringLayout.EAST, panel); panel.add(scrollEditor);
		 */

	}

	private JPanel createLeftPanel(SpringLayout springLayout) {
		JPanel leftPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, leftPanel, 0, SpringLayout.NORTH, frmEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, leftPanel, 0, SpringLayout.WEST, frmEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, leftPanel, 0, SpringLayout.SOUTH, frmEditor.getContentPane());

		SpringLayout sl_leftPanel = new SpringLayout();
		leftPanel.setLayout(sl_leftPanel);

		leftPane = new JTabbedPane(JTabbedPane.TOP);
		sl_leftPanel.putConstraint(SpringLayout.NORTH, leftPane, 0, SpringLayout.NORTH, leftPanel);
		sl_leftPanel.putConstraint(SpringLayout.WEST, leftPane, 0, SpringLayout.WEST, leftPanel);
		sl_leftPanel.putConstraint(SpringLayout.SOUTH, leftPane, 0, SpringLayout.SOUTH, leftPanel);
		sl_leftPanel.putConstraint(SpringLayout.EAST, leftPane, 0, SpringLayout.EAST, leftPanel);
		leftPanel.add(leftPane);

		elTree = new JTree(new DefaultMutableTreeNode("base"));
		treeModel = (DefaultTreeModel) elTree.getModel();
		// DefaultMutableTreeNode treeRoot =
		// (DefaultMutableTreeNode)treeModel.getRoot();
		JScrollPane scrollTree = createElementTree(leftPanel, sl_leftPanel);
		leftPane.addTab("BPs", null, scrollTree, null);

		createTagsPane();
		
		createConsolePane();

		return leftPanel;
	}

	private void createConsolePane() {
		JPanel searchPanel = new JPanel();
		leftPane.addTab("Console", null, searchPanel, null);
		SpringLayout sl_searchPanel = new SpringLayout();
		searchPanel.setLayout(sl_searchPanel);
		
		JLabel lblTags = new JLabel("Console");
		sl_searchPanel.putConstraint(SpringLayout.NORTH, lblTags, 0, SpringLayout.NORTH, searchPanel);
		sl_searchPanel.putConstraint(SpringLayout.WEST, lblTags, 10, SpringLayout.WEST, searchPanel);
		searchPanel.add(lblTags);
		
		final JTextField txtConsole = new JTextField();

		sl_searchPanel.putConstraint(SpringLayout.NORTH, txtConsole, 6, SpringLayout.SOUTH, lblTags);
		sl_searchPanel.putConstraint(SpringLayout.WEST, txtConsole, 10, SpringLayout.WEST, searchPanel);
		sl_searchPanel.putConstraint(SpringLayout.EAST, txtConsole, -10, SpringLayout.EAST, searchPanel);
		searchPanel.add(txtConsole);
		txtConsole.setColumns(10);
		

		JButton btnSalva = new JButton("Console");
		sl_searchPanel.putConstraint(SpringLayout.NORTH, btnSalva, 6, SpringLayout.SOUTH, txtConsole);
		sl_searchPanel.putConstraint(SpringLayout.WEST, btnSalva, 10, SpringLayout.WEST, searchPanel);
		sl_searchPanel.putConstraint(SpringLayout.EAST, btnSalva, -10, SpringLayout.EAST, searchPanel);
		btnSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.console(txtConsole.getText());
			}

		});
		searchPanel.add(btnSalva);
		
	}

	private void createTagsPane() {
		JPanel searchPanel = new JPanel();
		leftPane.addTab("Tags", null, searchPanel, null);
		SpringLayout sl_searchPanel = new SpringLayout();
		searchPanel.setLayout(sl_searchPanel);

		JLabel lblTags = new JLabel("Tags");
		sl_searchPanel.putConstraint(SpringLayout.NORTH, lblTags, 0, SpringLayout.NORTH, searchPanel);
		sl_searchPanel.putConstraint(SpringLayout.WEST, lblTags, 10, SpringLayout.WEST, searchPanel);
		searchPanel.add(lblTags);

		txtTagSearch = new JTextField();

		sl_searchPanel.putConstraint(SpringLayout.NORTH, txtTagSearch, 6, SpringLayout.SOUTH, lblTags);
		sl_searchPanel.putConstraint(SpringLayout.WEST, txtTagSearch, 10, SpringLayout.WEST, searchPanel);
		sl_searchPanel.putConstraint(SpringLayout.EAST, txtTagSearch, -10, SpringLayout.EAST, searchPanel);
		searchPanel.add(txtTagSearch);
		txtTagSearch.setColumns(10);

		listModel = new DefaultListModel<String>();

		final JList<? extends String> listElements = new JList<String>(listModel);
		listElements.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listElements.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {

				List<? extends String> values = listElements.getSelectedValuesList();
				String value = "";
				for (String s : values) {
					value += s + ";";
				}
				selectElements(value);
				controller.updateSelected(value);
			}
		});
		JScrollPane scrollSearch = new JScrollPane(listElements);
		sl_searchPanel.putConstraint(SpringLayout.NORTH, scrollSearch, 5, SpringLayout.SOUTH, txtTagSearch);
		sl_searchPanel.putConstraint(SpringLayout.WEST, scrollSearch, 0, SpringLayout.WEST, txtTagSearch);
		sl_searchPanel.putConstraint(SpringLayout.SOUTH, scrollSearch, -5, SpringLayout.SOUTH, searchPanel);
		sl_searchPanel.putConstraint(SpringLayout.EAST, scrollSearch, 0, SpringLayout.EAST, txtTagSearch);
		searchPanel.add(scrollSearch);

		txtTagSearch.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				controller.filterBy(txtTagSearch.getText());
			}
		});
	}

	private JPanel createRightPanel(SpringLayout springLayout, JPanel leftPanel) {
		JPanel rightPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.WEST, rightPanel, 250, SpringLayout.WEST, frmEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, rightPanel, 0, SpringLayout.SOUTH, frmEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, leftPanel, 0, SpringLayout.WEST, rightPanel);
		springLayout.putConstraint(SpringLayout.EAST, rightPanel, 0, SpringLayout.EAST, frmEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, rightPanel, 0, SpringLayout.NORTH, frmEditor.getContentPane());

		SpringLayout sl_rightPanel = new SpringLayout();
		rightPanel.setLayout(sl_rightPanel);

		mainPanel = new JPanel();
		sl_rightPanel.putConstraint(SpringLayout.NORTH, mainPanel, 0, SpringLayout.NORTH, rightPanel);
		sl_rightPanel.putConstraint(SpringLayout.SOUTH, mainPanel, 0, SpringLayout.SOUTH, rightPanel);
		mainPanel.setBackground(Color.LIGHT_GRAY);
		sl_rightPanel.putConstraint(SpringLayout.EAST, mainPanel, 0, SpringLayout.EAST, rightPanel);
		sl_rightPanel.putConstraint(SpringLayout.WEST, mainPanel, 0, SpringLayout.WEST, rightPanel);
		rightPanel.add(mainPanel);
		SpringLayout sl_mainPanel = new SpringLayout();
		mainPanel.setLayout(sl_mainPanel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				System.out.println(e);
				JTabbedPane source = (JTabbedPane) e.getSource();
				showGraph(source.getSelectedIndex() == 0);
			}
		});
		sl_mainPanel.putConstraint(SpringLayout.EAST, tabbedPane, 0, SpringLayout.EAST, mainPanel);
		tabbedPane.setToolTipText("");
		sl_mainPanel.putConstraint(SpringLayout.SOUTH, tabbedPane, 0, SpringLayout.SOUTH, mainPanel);
		mainPanel.add(tabbedPane);

		graphPanel = new JPanel();
		tabbedPane.addTab("Grafico", null, graphPanel, null);
		graphPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel panel = new JPanel();
		tabbedPane.addTab("Preferences", null, panel, null);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		JPanel panelOpc = new JPanel();
		sl_mainPanel.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, panelOpc);
		sl_mainPanel.putConstraint(SpringLayout.NORTH, panelOpc, 0, SpringLayout.NORTH, mainPanel);
		sl_mainPanel.putConstraint(SpringLayout.WEST, panelOpc, 0, SpringLayout.WEST, mainPanel);
		sl_mainPanel.putConstraint(SpringLayout.SOUTH, panelOpc, 20, SpringLayout.NORTH, mainPanel);
		sl_mainPanel.putConstraint(SpringLayout.EAST, panelOpc, 0, SpringLayout.EAST, mainPanel);
		sl_mainPanel.putConstraint(SpringLayout.NORTH, tabbedPane, 0, SpringLayout.SOUTH, panelOpc);
		sl_panel.putConstraint(SpringLayout.NORTH, panelOpc, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, panelOpc, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, panelOpc, 30, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panelOpc, 0, SpringLayout.EAST, panel);

		chkHullDebugJoints = new JCheckBox("Hull Debug Info");
		chkHullDebugJoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.booleanPrefChanged(EditorConsts.PREF_HULL_DEBUG, chkHullDebugJoints.isSelected());
			}
		});

		sl_panel.putConstraint(SpringLayout.NORTH, chkHullDebugJoints, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, chkHullDebugJoints, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, chkHullDebugJoints, 179, SpringLayout.WEST, panel);
		panel.add(chkHullDebugJoints);

		// chkDiscreto
		chkDiscreto = new JCheckBox("Modo Discreto");
		chkDiscreto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.booleanPrefChanged(EditorConsts.PREF_MODO_DISCRETO, chkDiscreto.isSelected());
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, chkDiscreto, 6, SpringLayout.SOUTH, chkHullDebugJoints);
		sl_panel.putConstraint(SpringLayout.WEST, chkDiscreto, 0, SpringLayout.WEST, panel);
		panel.add(chkDiscreto);
		// chckbxApplyMaterial
		chkApplyMaterial = new JCheckBox("Apply Material (texture)");
		chkApplyMaterial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.booleanPrefChanged(EditorConsts.PREF_APPLY_MATERIAL, chkApplyMaterial.isSelected());
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, chkApplyMaterial, 6, SpringLayout.SOUTH, chkDiscreto);
		sl_panel.putConstraint(SpringLayout.WEST, chkApplyMaterial, 0, SpringLayout.WEST, panel);
		panel.add(chkApplyMaterial);

		// chkAndroidMode
		chkAndroidMode = new JCheckBox("Android Mode");
		chkAndroidMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.booleanPrefChanged(EditorConsts.PREF_ANDROID_MODE, chkAndroidMode.isSelected());
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, chkAndroidMode, 6, SpringLayout.SOUTH, chkApplyMaterial);
		sl_panel.putConstraint(SpringLayout.WEST, chkAndroidMode, 0, SpringLayout.WEST, panel);
		panel.add(chkAndroidMode);
		
		// chkCleanView
		chkCleanView = new JCheckBox("Clean View");
		chkCleanView.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						controller.booleanPrefChanged(EditorConsts.PREF_CLEAN_VIEW, chkCleanView.isSelected());
					}
				});
				sl_panel.putConstraint(SpringLayout.NORTH, chkCleanView, 6, SpringLayout.SOUTH, chkAndroidMode);
				sl_panel.putConstraint(SpringLayout.WEST, chkCleanView, 0, SpringLayout.WEST, panel);
				panel.add(chkCleanView);

		mainPanel.add(panelOpc);
		SpringLayout sl_panelOpc = new SpringLayout();
		panelOpc.setLayout(sl_panelOpc);

		JButton btnSalva = new JButton("Salva");
		sl_panelOpc.putConstraint(SpringLayout.NORTH, btnSalva, 0, SpringLayout.NORTH, panelOpc);
		sl_panelOpc.putConstraint(SpringLayout.SOUTH, btnSalva, 0, SpringLayout.SOUTH, panelOpc);
		sl_panelOpc.putConstraint(SpringLayout.EAST, btnSalva, 0, SpringLayout.EAST, panelOpc);
		btnSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.salvaElementos();
			}

		});
		panelOpc.add(btnSalva);
		
		JButton btnPopout = new JButton("PopOut");
		btnPopout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				popOut();
			}
		});
		sl_panelOpc.putConstraint(SpringLayout.NORTH, btnPopout, 0, SpringLayout.NORTH, btnSalva);
		sl_panelOpc.putConstraint(SpringLayout.SOUTH, btnPopout, 0, SpringLayout.SOUTH, panelOpc);
		sl_panelOpc.putConstraint(SpringLayout.EAST, btnPopout, -6, SpringLayout.WEST, btnSalva);
		panelOpc.add(btnPopout);
		

		JButton btnClone = new JButton("Exporta Entidade");
		btnClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clonaUltimaEntidade();
			}
		});
		sl_panelOpc.putConstraint(SpringLayout.NORTH, btnClone, 0, SpringLayout.NORTH, btnSalva);
		sl_panelOpc.putConstraint(SpringLayout.SOUTH, btnClone, 0, SpringLayout.SOUTH, panelOpc);
		sl_panelOpc.putConstraint(SpringLayout.EAST, btnClone, -6, SpringLayout.WEST, btnPopout);
		panelOpc.add(btnClone);
		
		JButton btnImporta= new JButton("Importa Entidade");
		btnImporta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				importaEntidade();
			}
		});
		sl_panelOpc.putConstraint(SpringLayout.NORTH, btnImporta, 0, SpringLayout.NORTH, btnSalva);
		sl_panelOpc.putConstraint(SpringLayout.SOUTH, btnImporta, 0, SpringLayout.SOUTH, panelOpc);
		sl_panelOpc.putConstraint(SpringLayout.EAST, btnImporta, -6, SpringLayout.WEST, btnClone);
		panelOpc.add(btnImporta);
		
		
		JButton btnTestaVeiculo= new JButton("Testa Veiculos");
		btnTestaVeiculo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				testaVeiculo();
			}
		});
		sl_panelOpc.putConstraint(SpringLayout.NORTH, btnTestaVeiculo, 0, SpringLayout.NORTH, btnSalva);
		sl_panelOpc.putConstraint(SpringLayout.SOUTH, btnTestaVeiculo, 0, SpringLayout.SOUTH, panelOpc);
		sl_panelOpc.putConstraint(SpringLayout.EAST, btnTestaVeiculo, -6, SpringLayout.WEST, btnImporta);
		panelOpc.add(btnTestaVeiculo);
		return rightPanel;
	}

	protected void testaVeiculo() {
		controller.testaVeiculo();
		
	}

	protected void importaEntidade() {
		controller.importaEntidade();
		
	}

	protected void clonaUltimaEntidade() {
		controller.clonaUltimaEntidade();
		
	}

	private JScrollPane createElementTree(JPanel leftPanel, SpringLayout sl_leftPanel) {
		JScrollPane scrollTree = new JScrollPane(elTree);
		elTree.addTreeSelectionListener(new TreeSelectionListener() {
			private TreePath[] lastSelectionPaths;

			public void valueChanged(TreeSelectionEvent e) {
				TreePath[] selectionPaths = elTree.getSelectionPaths();
				if (selectionPaths == null) {
					selectionPaths = lastSelectionPaths;
					String paths = serializePaths(selectionPaths);
					selectElements(paths);
				}
				if (!isSelectionOK(selectionPaths)){
					return;
				}
				updateEditor(selectionPaths);
				lastSelectionPaths = selectionPaths;

				// controller.changeElement(null,identifier);
			}

			private boolean isSelectionOK(TreePath[] selectionPaths) {
				if (lastSelectionPaths != null) {
					if (selectionPaths.length == lastSelectionPaths.length) {
						if (selectionPaths[selectionPaths.length - 1].getLastPathComponent().equals(
								lastSelectionPaths[lastSelectionPaths.length - 1].getLastPathComponent())) {
							return false;
						}
					}
				}
				return true;
			}
		});
		elTree.setVisibleRowCount(200);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(elTree, popupMenu);

		JMenuItem mntmNovaBlueprint = new JMenuItem("Nova Blueprint");
		mntmNovaBlueprint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.createNewElement("blueprint");
			}
		});
		popupMenu.add(mntmNovaBlueprint);

		JMenuItem mntmNovaFactory = new JMenuItem("Nova Factory");
		mntmNovaFactory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.createNewElement("factory");
			}
		});
		popupMenu.add(mntmNovaFactory);

		JMenuItem mntmNovoMod = new JMenuItem("Novo Mod");
		mntmNovoMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.createNewElement("mod");
			}
		});
		popupMenu.add(mntmNovoMod);
		sl_leftPanel.putConstraint(SpringLayout.NORTH, scrollTree, 14, SpringLayout.NORTH, leftPanel);
		sl_leftPanel.putConstraint(SpringLayout.WEST, scrollTree, 0, SpringLayout.WEST, leftPanel);
		sl_leftPanel.putConstraint(SpringLayout.SOUTH, scrollTree, 0, SpringLayout.SOUTH, leftPanel);
		sl_leftPanel.putConstraint(SpringLayout.EAST, scrollTree, 0, SpringLayout.EAST, leftPanel);
		return scrollTree;
	}

	// deixa visivel ou invisivel o grafico...
	protected void showGraph(boolean b) {
		if (controller == null)
			return;
		controller.showGraph(b);
	}

	protected void windowChanged(Dimension size, Point location) {
		if (controller == null)
			return;
		controller.mainWindowChanged(size, location);

	}

	protected void updateEditor(TreePath[] selectionPaths) {
		if (selectionPaths == null) {
			return;
		}
		editorWindow.updateSelection(selectionPaths);
		String paths = serializePaths(selectionPaths);
		controller.updateSelected(paths);
	}

	private String serializePaths(TreePath[] selectionPaths) {
		String paths = "";
		for (TreePath path : selectionPaths) {
			paths += path.getLastPathComponent() + ";";
		}
		return paths;
	}

	protected void popOut() {
		// editorWindow.pack();
		editorWindow.setVisible(true);
	}

	public JPanel getGraphPanel() {
		return graphPanel;
	}

	public ElementManager getElementManager() {
		return (ElementManager) getIntegrationState().getElementManager();
	}

	public void selectElements(String selected) {
		String[] split = selected.split(";");

		TreePath[] paths = new TreePath[split.length];
		elTree.setExpandsSelectedPaths(true);
		for (int i = 0; i < split.length; i++) {
			String identifier = split[i];

			DefaultMutableTreeNode searchNode = searchNode(identifier);
			if (searchNode != null) {
				TreePath path = new TreePath(searchNode.getPath());
				paths[i] = path;
				elTree.expandPath(path);
				elTree.setSelectionPath(path);
			}
		}
		elTree.setSelectionPaths(paths);
	}

	public DefaultMutableTreeNode searchNode(String nodeStr) {
		DefaultMutableTreeNode node = null;
		DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) treeModel.getRoot();
		Enumeration enumeration = treeRoot.breadthFirstEnumeration();
		while (enumeration.hasMoreElements()) {

			node = (DefaultMutableTreeNode) enumeration.nextElement();
			if (nodeStr.equals(node.getUserObject().toString())) {
				return node;
			}
		}

		// tree node with string node found return null
		return null;
	}

	public void setPosition(Dimension size, Point pos) {
		frmEditor.setSize(size);
		frmEditor.setLocation(pos);

	}

	public void setPopOutPosition(Dimension size, Point pos) {
		editorWindow.setPopOutPosition(size, pos);

	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	
}
