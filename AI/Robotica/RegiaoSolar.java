import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

/*
 * Created on 18/11/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author usuario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RegiaoSolar extends RobotBasico {

	
	
	public RegiaoSolar(World w,int x,int y){
	super(w,x,y,15,15);
		setPickedPossible(false);
		setEmiteEnergia(true);
		setCor(Color.GREEN);
		colocaComponents();
		name="Região Solar";
	}


	

	public void colocaComponents(){
		out("RegiaoSolar.colocaComponents()");
		for (int i=0;i<maxComponentX;i++){
			for (int j=0;j<maxComponentY;j++){
				Component comp=new Component(this,i,j);		
				addComponent(comp);
			}			
		}


		
	}
	
	/**
	 * @return Returns the size.
	 */
}
