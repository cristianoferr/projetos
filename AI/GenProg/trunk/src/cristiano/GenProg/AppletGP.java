package cristiano.GenProg;


import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import comum.Objeto;

import cristiano.GenProg.Node.DadoNode;
import cristiano.GenProg.Node.Node;
import cristiano.GenProg.Node.Functions.NodeTerminal;
import cristiano.GenProg.Node.SimpleMath.NodeAddiction;
import cristiano.GenProg.Node.SimpleMath.NodeDivision;
import cristiano.GenProg.rules.FitnessRules;





public class AppletGP  extends Applet implements Runnable,MouseListener
{
	// Initialisierung der Variablen
	int width=120;
	int height=120;
	// Variablen für die Doppelpufferung
	private Image dbImage;
	private Graphics dbg;
	double deg2rad=0.0174532925;
	int mouseX=0;
	int mouseY=0;
	int generation=0;

	GeneticProgramming gp;
	DadoNode varX=new DadoNode("var0",10);
	DadoNode varY=new DadoNode("var1",10);
	//Dado var2=new Dado("var2",20,"");
	//Dado var3=new Dado("var3",20,"");
	Node nx=new NodeAddiction();
	Objeto obj=new Objeto();
	int count=0;

	
	BufferedImage img = null;
	

	

	// Bildvariable für den Hintergrund
//	Image backImage;




	private void printThreadName(String prefix) {
	  String name = Thread.currentThread().getName();
	  System.out.println(prefix + name);
	}
	public boolean mouseMove(Event event, int x, int y){
		mouseX=x;
		mouseY=y;
		return true;
		}

	public void mousePressed(MouseEvent e) 
	  {
		
		Point pt = e.getPoint();
		//System.out.println("mousepressed()");
		
		}	  // end mouseReleased
	  
//	dont use these so leave them empty 
	public void mouseClicked(MouseEvent e){} 
	public void mouseEntered(MouseEvent e){} 
	public void mouseExited(MouseEvent e){} 
	public void mouseReleased(MouseEvent e){} 
	  
	public void init()
	{
		gp=new GeneticProgramming();
		
		gp.addFitnessRule(FitnessRules.CHECK_NOT_CONTAINS_CODE, "var0", 10000000);
		gp.addFitnessRule(FitnessRules.CHECK_NOT_CONTAINS_CODE, "var1", 10000000);
		gp.addFitnessRule(FitnessRules.CHECK_SIZE, GeneticProgramming.MAX_SIZE*2, 1000000);
		gp.defaultNodes();
		gp.setSortMethod(SortMethod.GREATER);
/*		Node n=new NodeAddiction();
		Node n2=new NodeTerminal(gp,"2");
		Node n3=new NodeTerminal(gp,"3");
		Node n4=new NodeTerminal(gp,"4");
		Node n6=new NodeTerminal(gp,"6");
		Node n7=new NodeTerminal(gp,"7");
		Node n8=new NodeTerminal(gp,"8");
		Node ndiv=new NodeDivision();
		n.setGP(gp);
		n.addNode(n2);
		n.addNode(n3);

		n.addNode(ndiv);
		ndiv.addNode(n4);*/
		
		
		try {
		    img = ImageIO.read(new File("../assets/smile.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*nx.addNode(ndiv);
		nx.addNode(n6);
		ndiv.addNode(n7);
		ndiv.addNode(n8);*/
		//setBackground (Color.blue);

		// Laden der Bilddatei
		
		//gp.getCodigoFonte().add(n.getCodigoFonte());

		//startThread();
		gp.addVariavel(varX);
		gp.addVariavel(varY);
		//gp.addVariavel(var2);
		//gp.addVariavel(var3);
		gp.setObj(obj);
		gp.initNodes();
		
		//out("getNode:"+gp.getNode().getValue());
	
		addMouseListener(this);

	}

	public void start ()
	{
		// Schaffen eines neuen Threads, in dem das Spiel läuft
		Thread th = new Thread (this);
		// Starten des Threads
		th.start ();
	}

	public void stop()
	{

	}

	public void destroy()
	{
		//net.dbSave();
	}

	public void run ()
	{
		// Erniedrigen der ThreadPriority um zeichnen zu erleichtern
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		// Solange true ist läuft der Thread weiter
		
		while (true)
		{
			repaint();	
			try
			{
				// Stoppen des Threads für in Klammern angegebene Millisekunden
				Thread.sleep (1);
			}
			catch (InterruptedException ex)
			{
				// do nothing
			}

			// Zurücksetzen der ThreadPriority auf Maximalwert
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	/** Update - Methode, Realisierung der Doppelpufferung zur Reduzierung des Bildschirmflackerns */
	/*static public  Image createImage2(int x,int y){
		return createImage (x,y);
	}*/
	public void update (Graphics g)
	{
        //if (back==null) back=g;
       // g=back;
		
		dbImage = createImage (this.getSize().width, this.getSize().height);
		paint (dbImage.getGraphics());
		g.drawImage (dbImage, 0, 0, this);
		//dbImage.getGraphics() drawOval(100,100,30,30);
		
	}

	public void paint (Graphics g)
	{
		// Zeichnen des Bildes
		
		//savePixelColor(g);
		
		desenha(g);
	}
	private boolean hasData(int i){
		if (i>100) return true;
		return false;
	}

	private void desenha(Graphics g) {
		Node node0=(Node)gp.getNode(0);	
		g.drawString("Fitness: "+node0.getFitness(),300,40);
		g.drawString("Geração: "+generation,300,60);
		
		gp.resetFitness();
	

		double z=0;
		
		//long s=Math.round(Math.random()*3);
		z=1;
		int size=20;
		int px=500;
		int py=200;
		
		
		int tam=8;
		int opx=px;
		int opy=py+size*tam;

		
		
		for (int x=-size/2;x<size/2;x++){
			for (int y=-size/2;y<size/2;y++){
		
		//for (int x=0;x<img.getWidth();x++){
		//	for (int y=0;y<img.getHeight();y++){
				varX.setInt(x);
				varY.setInt(y);
				z=2*x+y;//(Math.abs(x)+Math.abs(y));//x*y;
				z*=10;
				//z=img.getRGB(x, y);
				//System.out.println("z:"+z);
				/*if (hasData(img.getRGB(x, y))){  
					z=1000;  
				} */ 
				//z=-x*y+50;
				/*if (z>0) {
					z=v;
				} else {
					z=-v;
				}*/
				
				/*
				}
				if (z<0) {
					z=-1;
				}
				if (z==0) {
					z=0;
				}*/
				
				
				//if (z>0){
					//g.setColor(Color.getHSBColor((float)z, (float)z, (float)z));
					g.setColor(new Color((int)z));
					//g.setColor(Color.black);
					g.fillRect(opx+x*tam, opy+y*tam, tam, tam);
				//}

				gp.calcFitness(z);
				node0=gp.getNode(0);
				z=node0.getValue();
				
				
				//if (z>0){
					
					//g.setColor(Color.getHSBColor((float)z, (float)z, (float)z));
					//g.setColor(Color.blue);
					g.setColor(new Color((int)z));
					g.fillRect(px+x*tam, py+y*tam, tam, tam);
				//}

			}
		}
		
		/*
		for (int x=-size;x<size;x++){
			var0.setInt(x);
			
			//y=-x*x+2*x+20;
			y=0;
			y=-Math.abs(x)+20;
			//if (x<0)y=x+20;
			if ((x>-50) && (x<50)) y=-300;
		
			gp.calcFitness(y);
			node0=(Node)gp.getNodes().elementAt(0);
			node0.drawGraph(g,500,200,x,(int)y);
			
		}*/
		generation++;
		
		System.out.println("Geração: "+generation);
		gp.endFitness();
		node0=(Node)gp.getNode(0);
		node0.drawImage(g,200,10);
		
		
		
		
		//System.out.println(node0.getCodigoFonte());
	    gp.getCodigoFonte().clear();
	    gp.getCodigoFonte().add(node0.getCodigoFonte());
	    //gp.printCodigoFonte();

		
		gp.endTurn();
	 

		
		/*nx.drawImage(g,400,10);
		
		nx=gp.mutate(nx);
		nx.drawImage(g,140,10);*/
 		//nx=nx.copyNode(nxx);	
		}
	
	


	}
	
	

