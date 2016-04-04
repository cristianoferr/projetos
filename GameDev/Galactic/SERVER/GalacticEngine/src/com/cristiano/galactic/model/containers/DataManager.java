package com.cristiano.galactic.model.containers;


import com.cristiano.cyclone.entities.GeomPoly.ModelContainer;
import com.cristiano.cyclone.entities.geom.Geom;
import com.cristiano.galactic.controller.handlers.CommandHandler;
import com.cristiano.galactic.model.Sistema;
import com.cristiano.galactic.model.XMLBuilder;
import com.cristiano.galactic.model.XMLReader;
import com.cristiano.galactic.model.bt.library.ItemBTLibrary;
import com.cristiano.galactic.model.wares.Ware;

public class DataManager {
	private WareManager wareManager;
//	LayoutManager layoutManager;
	private Model3DManager models3D;
	private ControlManager controlManager;
	private CommandHandler commandManager;
	/* First of all, we create the BT library. */
	private ItemBTLibrary btLibrary;
	
	private FactionManager factionManager;
	
	//Container de geoms carregados a partir de arquivos .obj
	private ModelContainer modelContainer;
	
		
	private XMLBuilder xmlBuilder;

	private DataManager dm;

	private int id_ware_detail=0;
	private int id_ware_refine_into=0;
	


	/**
	 * @return the wareManager
	 */
	public  WareManager getWareManager() {
		return getDM().wareManager;
	}

	/**
	 * @return the AbstractElementManager
	 */
/*	public  LayoutManager getAbstractElementManager() {
		return getDM().layoutManager;
	}*/

	
	
	public DataManager(){
		dm=this;
				
		
		
		commandManager=new CommandHandler(this);
		
		
		wareManager=new WareManager(this);
		models3D=new Model3DManager(this);
		
		
		controlManager=new ControlManager();
	//	layoutManager=new LayoutManager();
		
		btLibrary=new ItemBTLibrary();		
		factionManager=new FactionManager();
	}

	public void initialize(Sistema world) {
		populate(world);
	}
	
	public IContext createBTContext(){
		return ContextFactory.createContext(btLibrary);
	}
	
	
	public void populate(Sistema world){
	//	sqlDetail();
	//	sqlGrupoWare();
	//	sqlWare();
	//	sqlRefineInto();
		
	xmlBuilder=new XMLBuilder(this);
	//xmlBuilder.createXML3DModels();
	//xmlBuilder.createXMLDetails();
	//xmlBuilder.createXMLWareGroups();
	//xmlBuilder.createXMLWareRefinery();
	
		
	getWareManager().getWareGroups().clear();
	getWareManager().getWares().clear();
	getModels3D().getModels().clear();
	XMLReader xmlReader=new XMLReader(this);
	xmlReader.loadXML(world);

	getModels3D().initSlotGrouping();
	
	
	}
	
	public  DataManager getDM(){
		return dm;
	}

	/**
	 * @return the models3D
	 */
	public  Model3DManager getModels3D() {
		return getDM().models3D;
	}

	/**
	 * @return the controlManager
	 */
	public  ControlManager getControlManager() {
		return getDM().controlManager;
	}

	public CommandHandler getCommandManager() {
		return commandManager;
	}

	public ItemBTLibrary getBtLibrary() {
		return btLibrary;
	}

	public void setWareManager(WareManager wareManager) {
		this.wareManager = wareManager;
	}

	public void setModels3D(Model3DManager models3d) {
		models3D = models3d;
	}
	public ModelContainer getModelContainer() {
		return modelContainer;
	}

	public void setModelContainer(ModelContainer modelContainer) {
		this.modelContainer = modelContainer;
	}

	public Geom getGeomFromWare(String shipWare) {
		return getWareManager().getWare(shipWare).getModelData().getGeom();
		
	}
	
	public Geom getGeomFromWare(Ware shipWare) {
		return shipWare.getModelData().getGeom();
		
	}

	public XMLBuilder getXmlBuilder() {
		return xmlBuilder;
	}

	public FactionManager getFactionManager() {
		return factionManager;
	}
	
	public  Ware getWare(String ware){
		return wareManager.getWare(ware);
	}
}
