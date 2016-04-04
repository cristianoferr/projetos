package financ;

import geneticProgramming.Node;
import geneticProgramming.Functions.NodeTerminal;
import geneticProgramming.SimpleMath.NodeAddiction;
import geneticProgramming.SimpleMath.NodeDivision;

import java.applet.Applet;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import cristiano.GenProg.GeneticProgramming;
import cristiano.dados.Dado;





public class AppletFinanc  extends Applet implements Runnable,MouseListener
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

	GeneticProgramming gp=new GeneticProgramming();
	Dado var0=new Dado("var0",10);
	//Dado var2=new Dado("var2",20,"");
	//Dado var3=new Dado("var3",20,"");
	Node nx=new NodeAddiction();
	Stocks stockACES4=new Stocks("ACES4");
	Stocks stockUSIM5=new Stocks("USIM5");
	Stocks stockPETR4=new Stocks("PETR4");
	Stocks stockVALE5=new Stocks("VALE5");
	Stocks stockPETR3=new Stocks("PETR3");
	Stocks stockELET6=new Stocks("ELET6");
	Stocks stockEMBR3=new Stocks("EMBR3");
	int count=0;

	

	

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

		Node.gp=gp;
		Node n=new NodeAddiction();
		Node n2=new NodeTerminal(gp,"2");
		Node n3=new NodeTerminal(gp,"3");
		Node n4=new NodeTerminal(gp,"4");
		Node n6=new NodeTerminal(gp,"6");
		Node n7=new NodeTerminal(gp,"7");
		Node n8=new NodeTerminal(gp,"8");
		Node ndiv=new NodeDivision();
		
		n.addNode(n2);
		n.addNode(n3);

		n.addNode(ndiv);
		ndiv.addNode(n4);
		
		
		nx.addNode(ndiv);
		nx.addNode(n6);
		ndiv.addNode(n7);
		ndiv.addNode(n8);
		//setBackground (Color.blue);

		// Laden der Bilddatei
		
		gp.codigoFonte.add(n.getCodigoFonte());

		//startThread();
		gp.addVariavel(var0);
		//gp.addVariavel(var2);
		//gp.addVariavel(var3);
		Cliente.stocks=stockELET6;
		Cliente cli=new Cliente();
		gp.setObj(cli);
		gp.init();
		
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

	private void desenha(Graphics g) {
		Node node0=(Node)gp.getNodes().elementAt(0);	
		g.drawString("Fitness: "+node0.getFitness(),300,40);
		
		/*Node node1=(Node)gp.getNodes().elementAt(1);
		node1.drawImage(g,400,10);
		Node node2=(Node)gp.getNodes().elementAt(2);
		node2.drawImage(g,100,200);
		Node node3=(Node)gp.getNodes().elementAt(30);
		node3.drawImage(g,400,200);*/
		
		gp.initFitness();
	

		double y=0;
		
		Stocks stocks=null;
		Cliente cli=null;
		
		//long s=Math.round(Math.random()*3);
		int totStocks=6;
		for (int s=0;s<totStocks;s++){
		//s=1;
		if (s==0){System.out.println("Usando VALE5..."+stockVALE5.maxDias());stocks=stockVALE5;}
		if (s==1){System.out.println("Usando USIM5..."+stockUSIM5.maxDias());stocks=stockUSIM5;}
		if (s==2){System.out.println("Usando PETR4..."+stockPETR4.maxDias());stocks=stockPETR4;}
		if (s==3){System.out.println("Usando ELET6..."+stockELET6.maxDias());stocks=stockELET6;}
		if (s==4){System.out.println("Usando ACES4..."+stockACES4.maxDias());stocks=stockACES4;}
		if (s==5){System.out.println("Usando EMBR3..."+stockEMBR3.maxDias());stocks=stockEMBR3;}
		//count++;
		//if (count>3) count=0;
		Cliente.setStocks(stocks);
		
		Vector clientes=new Vector();
		for (int i=0;i<gp.nodes.size();i++){
			cli=new Cliente();
			clientes.add(cli);
		}
		
		for (int x=0;x<stocks.maxDias();x++){
			var0.setDouble(x);
			stocks.setDia(x);
			for (int i=0;i<gp.nodes.size();i++){
				cli=(Cliente)clientes.elementAt(i);
				//boolean b=false;
				//if (x>=stocks.maxDias()-15) b=true;
				//if (x<=15) b=true;
				//cli.showLog((i==0)&& (b));
				
				gp.setObj(cli);
				//var2.setDouble(cli.getSaldo());
				//var3.setDouble(cli.getQtdStocks());
				//cli.setSaldo(cli.getSaldo()+Math.random()*100);
				cli.inicioDia();
				gp.calcFitnessSingleAlt(0, i);
				cli.fimDia();
				if (x==stocks.maxDias()-1)
					gp.calcFitnessSingleAlt(cli.getFit()/(totStocks*10), i);
				//g.setColor(Color.RED);
				//g.drawLine(x-1,(int)cli.getFitness()+300,x,(int)cli.getFitness()+300);
			}
			
			
			//cli=(Cliente)clientes.elementAt(0);
			
			
		}
		cli=(Cliente)clientes.elementAt(0);
		g.drawString("Saldo: "+cli.getCredito(),400,80+(30*s));
		System.out.println(cli.getCredito());
		y+=cli.getCredito();
		
		}
		gp.endFitness();
		node0=(Node)gp.getNodes().elementAt(0);
		node0.drawImage(g,200,10);
		
		
		
		System.out.println("Média:"+y/totStocks);
		
		
		testeReal(g);
		
		//System.out.println(node0.getCodigoFonte());
	    Node.gp.codigoFonte.clear();
	    Node.gp.codigoFonte.add(node0.getCodigoFonte());
	    Node.gp.printCodigoFonte();

		
		
	

		
		/*nx.drawImage(g,400,10);
		
		nx=gp.mutate(nx);
		nx.drawImage(g,140,10);*/
 		//nx=nx.copyNode(nxx);	
		}
	
	
	private void testeReal(Graphics g) {
		Stocks stocks;
		Cliente cli=new Cliente();
		//Teste Real da Petr3
		stocks=stockPETR3;
		Cliente.setStocks(stocks);
		//System.out.println("node:"+node0.s)

		for (int x=0;x<stocks.maxDias();x++){
			gp.setObj(cli);
			cli.showLog(true);
			var0.setDouble(x);
			stocks.setDia(x);
			//var2.setDouble(cli.getSaldo());
			//var3.setDouble(cli.getQtdStocks());
			//cli.setSaldo(cli.getSaldo()+Math.random()*100);
			cli.inicioDia();
			gp.calcFitnessSingleAlt(0, 0);
			cli.fimDia();
			if (x==stocks.maxDias()-1)
				gp.calcFitnessSingleAlt(cli.getFit(), 0);
			//g.setColor(Color.RED);
			//g.drawLine(x-1,(int)cli.getFitness()+300,x,(int)cli.getFitness()+300);
		}
		g.drawString("PETR3: "+cli.getCredito(),300,0);
		System.out.println("PETR3: "+cli.getCredito());
		//Node node0=(Node)gp.getNodes().elementAt(0);
	//	node0.drawImage(g,400,100);
	}

	}
	
	

