import java.awt.Graphics;
import java.util.Vector;

/*
 * Created on 18/11/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author CMM4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class World extends Objeto{

	/**
	 * 
	 */
	//public ObjetoPlano plano[][];
	private int maxObjetos=20;
	private int maxX=400;
	private int maxY=400;
	public Vector objetos=new Vector();
	public int totObjeto=0;
	static int run=0;
	
	public int mousex=0;
	public int mousey=0;
	public int button1=0;
	public int button2=0;
	RobotLearn robot;
	
	private int updatePlano=0;
	
	public World() {
		super();
	//	plano=new ObjetoPlano[maxX][maxY];
		for (int i=0;i<maxX;i++){
			for (int j=0;j<maxY;j++){
			//   plano[i][j]=null;
			}
			
		}
		// 
		robot=new RobotLearn(this,200,200);
		out("Robo Gerado");
		robot.colocaComponents();
		out("Componentes colocados");
		addObjeto(robot);
		
		Bola bola=new Bola(this,50,50);
		addObjeto(bola);

		RegiaoSolar regiaoSolar=new RegiaoSolar(this,150,150);
		addObjeto(regiaoSolar);
		
		updatePlano=1;
		
	}
	public void addObjeto(ObjetoPlano o){
		objetos.add(o);
		totObjeto++;
	}
	public ObjetoPlano getObjeto(int i){
		return (ObjetoPlano)objetos.elementAt(i);
	}	
/*	public void marcaObjetosPlano(){
		//Futuramente considerar angulos
		
		for (int o=0;o<totObjeto;o++) if (objetos.elementAt(o)!=null){
			ObjetoPlano obj=(ObjetoPlano)objetos.elementAt(o);
			if (obj.updatePlano==1)
				updatePlano=1;
			    obj.updatePlano=0;
		}
		
		if (updatePlano==1){
		for (int i=0;i<maxX;i++){
			for (int j=0;j<maxY;j++){
			//   plano[i][j]=null;
			}
			
		}
		for (int o=0;o<totObjeto;o++) if (objetos.elementAt(o)!=null){
			ObjetoPlano obj=(ObjetoPlano)objetos.elementAt(o);
			for (int i=obj.getPosx()-obj.getWidth()/2;i<obj.getPosx()+obj.getWidth()/2;i++){
				for (int j=obj.getPosx()-obj.getWidth()/2;j<obj.getPosx()+obj.getWidth()/2;j++){
					out("i:"+i+" j:"+j);
				//	if ((i>0) && (j>0) &&(i<maxX) &&(j<maxY)) 
				//   plano[i][j]=obj;
				}
			}
		}
		}
	*/

	
	public void desenha(Graphics g){
		//System.out.println("mundo.desenha()");
		if (button1==1){
			getObjeto(1).setX(mousex);
			getObjeto(1).setY(mousey);
			//robot.moveTrazDir();
			
		}
		if (button2==1){ 
				//robot.moveEsq();
			getObjeto(0).setX(mousex);
			getObjeto(0).setY(mousey);
			
			}		
		
		run++;
		
		for (int o=0;o<totObjeto;o++) if (objetos.elementAt(o)!=null){
			ObjetoPlano obj=(ObjetoPlano)objetos.elementAt(o);
			obj.turn(g);
			obj.desenha(g);
			
			//out("Objeto "+o+" desenhado!");
		}	
	}
	/**
	 * @return
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * @return
	 */
	public int getMaxY() {
		return maxY;
	}

	}

