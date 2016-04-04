package com.cristiano.galactic.model;



import java.util.ArrayList;
import java.util.List;

import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Utils.AssocList;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.controller.handlers.CommandHandler;
import com.cristiano.galactic.controller.messenger.EvtUpdate;
import com.cristiano.galactic.controller.messenger.Message;
import com.cristiano.galactic.controller.messenger.StarMessenger;
import com.cristiano.galactic.model.Entity.Ship;
import com.cristiano.galactic.model.Entity.Star;
import com.cristiano.galactic.model.Entity.Station;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.Entity.Abstract.PropertyChangeSource;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.containers.FactionManager;
import com.cristiano.galactic.model.enums.MVCProperties;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.gamelib.propriedades.AbstractModel;


/**
 * This is a central data model class. It represents our entire virtual world. Thus it contains references to all
 * {@link Item}s that are located in it. Like the Item class, this class extends {@link PropertyChangeSource} which
 * is a little helper class to fire {@link java.beans.PropertyChangeEvent}s. E.g. the list of items fires them
 * whenever an Item gets added or removed to/from it.
 * <p/>
 * The most important idea that should be followed when deciding what to put into the model is: The model should contain
 * the whole state of the game - meaning if you save and restore the model your game state should be restored. It should
 * not contain (much) more.
 * <br>
 * This allows easy saving/storing as well as client-server or peer-to-peer applications.
 * <p/>
 * (You can now switch back to the previous file you were watching or read about model implementation here.)
 * <p/>
 * An important concept when designing the model for your application is the concept of <i>associations</i>.
 * Associations between your model classes allow you to create links between model elements and thus make it possible
 * to navigate in your model. Associations can be modelled by simple attributes containing a reference to another
 * object, or a simple list/set of other objects. Though most of the times (if not always) it is better to have more
 * sophisticated association implementations:
 * <p/>
 * First encapsulation is an issue: the fields containing the references should not be directly accessible from outside
 * a class. Instead access methods are used. This allows to have readonly references, firing change events and ensure
 * link integrity. <br>
 * Second I recommend to use bidirectional associations. Meaning each object that is linked to another object also links
 * to that other object. This bidirectional links should always be consistent, that is: there shouldn't be any 'half'
 * links! Thus this should be ensured in the accessmethods already.
 * @author Irrisor
 */
public class Sistema  extends AbstractModel {
	//static Vector<Star> sistemas=new Vector<Star>();
	private Star star;
	private String name="";
	private StarMessenger messenger;
	private DataManager dataManager;
	

	//Um número grande por hora
	private Vector3 galacticPos=new Vector3(Math.pow(10,50),Math.pow(10,10),Math.pow(10,12));
	
	public void addEntity(Item entity){
	//	System.out.println("Sistema.AddEntity():"+entity);
		items.add(entity);
	}

	public void removeEntity(Item entity){
		items.remove(entity);
	}
	

	
    private final List<Item> items = new AssocList<Item>( new ArrayList<Item>(), new AssocList.ModificationHandler<Item>() {
        public void added( Item element ) {
            element.setWorld( Sistema.this );
            firePropertyChange2( MVCProperties.ITEMS_PROPERTY, null, element );
        }

        public void removed( Item element ) {
            element.setWorld( null );
            firePropertyChange2( MVCProperties.ITEMS_PROPERTY, element, null );
        }

        public boolean canAdd( Item element ) {
            return element.getWorld() != Sistema.this;
        }
    } );
	private boolean flagReady=false;



    /**
     * Get the list of items to add/remove or query items of this World.
     *
     * @return list of items
     */
    public int size() {
        return items.size();
    }
	
	
	public Sistema(String name,DataManager dataManager){
	//	sistemas.clear();
		this.name=name;
    	messenger=new StarMessenger(this);
    	this.dataManager=dataManager;

	}
	
	public void addMessage(Message msg){
		messenger.addMessage(msg);
	}
		public void addMessage(EvtUpdate msg){
		messenger.addMessage(msg);
	}

	
	public void start(){
        if (!Consts.LOAD_XML_ENTITIES){
			//star=new Star();//ItemFactory.createStar("Estrela", this);
			testInsertion();
        }
	}
	public String toString(){
		return "Sistema: "+name;
	}
	public void turn(){
	//	System.out.println("sistema.turn()");
		messenger.turn();

		
	}

	
	/*
	public SphereItem shoot(Item owner,Vector3 pos, Vector3 velocity){
		Bullet s=new Bullet(dataManager,new GeomSphere(1),"bullet",null);
		addEntity(s);
		s.getBody().setMass(Math.pow(10, 3));
		s.getBody().setTTL(10000);
		s.getBody().setPosition(owner.getBody().getPosition().getAddVector(pos));
		
		s.setVelocity(owner.getVelocity().getAddVector(velocity));
		
		return s;
		
	}*/
	
	public ArtificialEntity createEntityFromWare(int id,String shipWare,String name,Vector3 pos){
		Ware ware=dataManager.getWareManager().getWare(shipWare);
		String groupName=ware.getGrupo().getName();
		ArtificialEntity item=null;
		if (groupName.equals("Ship")){
			item=new Ship(id,dataManager,ware,name);
			
		}
		if (groupName.equals("Station")){
			item=new Station(id,dataManager,ware,name);
		}
		if (item!=null){
			item.getCoord().set(pos.x,pos.y,pos.z);
			addEntity(item);
		}
		return item;
	}
	
	
	private void testInsertion() { 
		//createAsteroidField();
		//insertTestShips();
		
		/*ArtificialEntity ship7=createEntityFromWare(Ship.getNextID(),"HigaranBattleCruiser",  "HigaranBattleCruiser",new Vector3(Consts.initPos+10000,0,4000));
		//addEntity(ship7);
		//ship7.setRotation(new Vector3(1,1,1));
		ship7.setVelocity(Consts.initForce, 0, 0);*/
		
		FactionManager fm=dataManager.getFactionManager();
		
		ArtificialEntity ship8=createEntityFromWare(Ship.getNextID(),"Orca",  "Orca",new Vector3(Consts.initPos+1000,0,2000));
		//addEntity(ship8);
		ship8.setVelocity(Consts.initForce, 0, 0);
		ship8.setOwner2(fm.getFactionByID(1));
		
		ArtificialEntity shipProv=createEntityFromWare(Ship.getNextID(),"Providence",  "Providence",new Vector3(Consts.initPos+1000,1500,2000));
		//addEntity(ship8);
		shipProv.setVelocity(Consts.initForce, 0, 0);
		shipProv.setOwner2(fm.getFactionByID(1));
		
		ArtificialEntity ship9=createEntityFromWare(Ship.getNextID(),"Megathron",  "Megathron",new Vector3(Consts.initPos+1000,500,1000));
		//addEntity(ship8);
		ship9.setVelocity(Consts.initForce, 0, 0);
		ship9.setOwner2(fm.getFactionByID(1));
		
		ArtificialEntity shipDrake=createEntityFromWare(Ship.getNextID(),"Drake",  "Drake",new Vector3(Consts.initPos+1000,0,1000));
		//addEntity(shipDrake);
		shipDrake.setVelocity(Consts.initForce, 0, 0);
		shipDrake.setOwner2(fm.getFactionByID(1));
		
		ArtificialEntity shipRookie=createEntityFromWare(Ship.getNextID(),"Velator",  "Velator",new Vector3(Consts.initPos+15,150, 0));
		//addEntity(shipRookie);
		shipRookie.setVelocity(Consts.initForce, 0, 0);
		shipRookie.setOwner2(fm.getFactionByID(1));
		
		ArtificialEntity shipNova=createEntityFromWare(Ship.getNextID(),"Nova",  "Nova",new Vector3(Consts.initPos+15,50, 0));
		//addEntity(shipRookie);
		shipNova.setVelocity(Consts.initForce, 0, 0);
		shipNova.setOwner2(fm.getFactionByID(1));
		
		ArtificialEntity shipBuster=createEntityFromWare(Ship.getNextID(),"Buster",  "Buster",new Vector3(Consts.initPos+25,50, 0));
		//addEntity(shipRookie);
		shipBuster.setVelocity(Consts.initForce+10, 0, 0);
		shipBuster.setOwner2(fm.getFactionByID(1));
		
		ArtificialEntity shipPlayer=createEntityFromWare(Ship.getNextID(),"Discoverer",  "Nave Jogador",new Vector3(Consts.initPos,0, 0));
		//addEntity(shipPlayer);
		shipPlayer.setVelocity(Consts.initForce, 0, 0);
		shipPlayer.setOwner2(fm.getPlayerID(0));
		//Consts.IDPlayer=shipPlayer.getId();
		
		CommandHandler.startCMD(CommandHandler.CMD_MOVE_TO_TARGET,shipNova,shipPlayer);
		
		
		ArtificialEntity shipAvatar=createEntityFromWare(Ship.getNextID(),"Avatar",  "Avatar",new Vector3(Consts.initPos+15000,0,-2000));
		//addEntity(shipAvatar);
		//shipAvatar.setRotation(new Vector3(1,1,1));
		shipAvatar.setVelocity(Consts.initForce, 0, 0);
		shipAvatar.setOwner2(fm.getFactionByID(1));
		
		ArtificialEntity shipErebus=createEntityFromWare(Ship.getNextID(),"Erebus",  "Erebus",new Vector3(Consts.initPos+15000,0,-11000));
		shipErebus.setVelocity(Consts.initForce, 0,00);
		shipErebus.setOwner2(fm.getFactionByID(1));

		ArtificialEntity shipRagnarok=createEntityFromWare(Ship.getNextID(),"Ragnarok",  "Ragnarok",new Vector3(Consts.initPos+15000,0,-15000));
		shipRagnarok.setVelocity(Consts.initForce, 0,00);
		shipRagnarok.setOwner2(fm.getFactionByID(1));

		
		/*shipTeste=new Ship(dataManager,dataManager.getWareManager().getWare("ArgonM5Pirate").getName(),  "Teste1");
		shipTeste.getCoord().set(Consts.initPos+150,15, 0);
		addEntity(shipTeste); 
		shipTeste.setVelocity(Consts.initForce, 0, 0);*/
		
		/*Ship shipTeste2=new Ship(dataManager,dataManager.getWareManager().getWare("Player Ship").getName(),  "Teste2");
		shipTeste2.getCoord().set(View.initPos+260,-30, 0);
		addEntity(shipTeste2);
		shipTeste2.setVelocity(View.initForce, 0, 0);
		*/
		
		
		/*shipTo.getBody().addForce(new Vector3(-1000,-500,0));
		shipFrom.getBody().addForce(new Vector3(+1000,500,0));*/
		
		//ship1.SetCMD(dataManager.getCommandManager().createCMD(CommandManager.CMD_ORIENTTO,ship2));
		//ship2.SetCMD(dataManager.getCommandManager().createCMD(CommandManager.CMD_ORIENTTO,ship1));
		
		//shipTeste.SetCMD(dataManager.getCommandManager().createCMD(CommandManager.CMD_ORIENTTO,shipTeste2));
		//shipTeste2.SetCMD(dataManager.getCommandManager().createCMD(CommandManager.CMD_ORIENTTO,shipTeste));
		
		
		/*System.out.println("Cargo free:"+shipTeste.getCargoFree()+" Ware used:"+shipTeste.getWareCargo(dataManager.getWareManager().getWare("Plagioclase")) );
		shipTeste.addCargo(dataManager.getWareManager().getWare("Plagioclase"), 1000);
		System.out.println("Cargo free:"+shipTeste.getCargoFree()+" Ware used:"+shipTeste.getWareCargo(dataManager.getWareManager().getWare("Plagioclase")) );
		shipTeste.addCargo(dataManager.getWareManager().getWare("Plagioclase"), 1000);
		System.out.println("Cargo free:"+shipTeste.getCargoFree()+" Ware used:"+shipTeste.getWareCargo(dataManager.getWareManager().getWare("Plagioclase")) );
		shipTeste.addCargo(dataManager.getWareManager().getWare("Plagioclase"), -1000);
		System.out.println("Cargo free:"+shipTeste.getCargoFree()+" Ware used:"+shipTeste.getWareCargo(dataManager.getWareManager().getWare("Plagioclase")) );
		*/
		//Date now=new Date();
		//long i2=now.getTime();
		/*out("i2:"+(int)(i2/1000));
		out("class:"+CommandMoveTo.class);*/
		
		//Coord coord=new Coord(0,0,0,getStar());
		//CommandMoveTo cmdMoveTo=new CommandMoveTo(dataManager,coord);
		
		//Message msg=new Message(ship1,ship2,CommandManager.CMD_MOVETO,0);
		//addMessage(msg);
	}


	private void insertTestShips() {/*
		Ship ship1=new Ship(dataManager,dataManager.getWareManager().getWare("NormandySR1"),  "NormandySR1");
		ship1.getCoord().set(Consts.initPos, -55, 0);
		addEntity(ship1);
		
		Ship ship2=new Ship(dataManager,dataManager.getWareManager().getWare("Anna-V"),  "Anna-V");
		ship2.getCoord().set(Consts.initPos,55, 0);
		addEntity(ship2);
		
		Ship ship3=new Ship(dataManager,dataManager.getWareManager().getWare("SpaceShipSpecular"),  "SpaceShipSpecular");
		ship3.getCoord().set(Consts.initPos+330,0, 0);
		addEntity(ship3);
		
		Ship ship4=new Ship(dataManager,dataManager.getWareManager().getWare("GhoulFighter"),  "GhoulFighter");
		ship4.getCoord().set(Consts.initPos+130,0, 0);
		addEntity(ship4);
		
		Ship ship5=new Ship(dataManager,dataManager.getWareManager().getWare("BattleCruiser"),  "BattleCruiser");
		ship5.getCoord().set(Consts.initPos+130,3500, 0);
		addEntity(ship5);
		
		Ship ship6=new Ship(dataManager,dataManager.getWareManager().getWare("FeroxBomber"),  "FeroxBomber");
		ship6.getCoord().set(Consts.initPos+230,0, 0);
		addEntity(ship6);
		ship6.setVelocity(Consts.initForce, 0, 0);
		
		Ship ship7=new Ship(dataManager,dataManager.getWareManager().getWare("HigaranBattleCruiser"),  "HigaranBattleCruiser");
		ship7.getCoord().set(Consts.initPos,200, 0);
		addEntity(ship7);
		ship7.setVelocity(Consts.initForce, 0, 0);
		
		ship1.setVelocity(Consts.initForce, 0, 0);
		ship2.setVelocity(Consts.initForce, 0, 0);
		ship3.setVelocity(Consts.initForce, 0, 0);
		ship4.setVelocity(Consts.initForce, 0, 0);
		ship5.setVelocity(Consts.initForce, 0, 0);
		
		Station station1=new Station(dataManager,dataManager.getWareManager().getWare("DriftStation"),  "DriftStation");
		station1.getCoord().set(Consts.initPos,1650, 0);
		addEntity(station1);
		station1.setVelocity(Consts.initForce, 0, 0);*/
	}
	
	

	public Star getStar(){
		return star;
	}


	/**
	 * @return the dataManager
	 */
	public DataManager getDataManager() {
		return dataManager;
	}

	//Essa função será substituida eventualmente pela posição do sistema solar na galaxia.
	public Vector3 getGalacticPos() {
		return galacticPos;
	}

	public Item getItem(int i) {
		return items.get(i);
	}
	public Item getEntityByID(int id) {
		for (int i=0;i<size();i++){
			if (items.get(i).getId()==id){
				return getItem(i);
			}
		}
		return null;
	}
	
	public void removeItem(Item item){
		items.remove(item);
	}

	public boolean isReady() {
		
		return flagReady;
	}
	
	public void setReady(boolean b) {
		
		flagReady=b;
	}
}
