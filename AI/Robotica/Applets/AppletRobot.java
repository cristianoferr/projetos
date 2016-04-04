package Applets;
import World;

import java.applet.Applet;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class AppletRobot extends Applet implements Runnable,MouseListener
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
	World mundo=new World();
	

	

	// Bildvariable für den Hintergrund
	Image backImage;


	private void printThreadName(String prefix) {
	  String name = Thread.currentThread().getName();
	  System.out.println(prefix + name);
	}
	public boolean mouseMove(Event event, int x, int y){
		mouseX=x;
		mouseY=y;
		mundo.mousex=x;
		mundo.mousey=y;
		return true;
		}

	public void mousePressed(MouseEvent e) 
	  {
		
		Point pt = e.getPoint();
		//System.out.println("mousepressed()");
		mundo.mousex=pt.x;
		mundo.mousey=pt.y;
		
		if (e.getButton()==MouseEvent.BUTTON1) {
			if (mundo.button1==0){
			   mundo.button1=1;} else {mundo.button1=0;}
		}
		if (e.getButton()==MouseEvent.BUTTON3) {
			if (mundo.button2==0){
			   mundo.button2=1;} else {mundo.button2=0;}
		}	  // end mouseReleased
	System.out.println("e:"+e.getButton()+" b1:"+mundo.button1+" b2:"+mundo.button2);
	  }
//	dont use these so leave them empty 
	public void mouseClicked(MouseEvent e){} 
	public void mouseEntered(MouseEvent e){} 
	public void mouseExited(MouseEvent e){} 
	public void mouseReleased(MouseEvent e){} 
	  
	public void init()
	{
		//setBackground (Color.blue);

		// Laden der Bilddatei

		//startThread();
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
	public void update (Graphics g)
	{
        //if (back==null) back=g;
       // g=back;
		
		dbImage = createImage (this.getSize().width, this.getSize().height);
		mundo.desenha(dbImage.getGraphics());
		g.drawImage (dbImage, 0, 0, this);
		//dbImage.getGraphics() drawOval(100,100,30,30);
		paint (g);
	}

	public void paint (Graphics g)
	{
		// Zeichnen des Bildes
		
		//savePixelColor(g);
		
		desenha(g);
		mundo.button1=0;
	 	mundo.button2=0;	
	}

	private void desenha(Graphics g) {

		double ang=0;
       // if (back==null) back=g;
	    
	  //  back=g.create();
		//mundo.desenha(g);
		
		 //g=back;

 			
		}

	}
	
	

