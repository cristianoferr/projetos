/*
 * Created on 04/03/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package Robot3D;

/**
 * @author CMM4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.awt.Color;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import simbad.sim.Arch;
import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;
public class SimbadEnv extends EnvironmentDescription {
	public SimbadEnv(){ 
		add(new Arch(new Vector3d(3,0,-3),this));
		add(new Box(new Vector3d(-3,0,3),new Vector3f(1,1,1), this));
		
		Wall w1=new Wall(new Vector3d(10,0,0),20,2, this);
		w1.rotate90(1);
		w1.setColor(new Color3f(Color.yellow));
		add(w1);
		Wall w2=new Wall(new Vector3d(-10,0,0),20,2, this);
		w2.rotate90(1);
		w2.setColor(new Color3f(Color.yellow));
		add(w2);
		Wall w3=new Wall(new Vector3d(0,0,-10),20,2, this);
		w3.setColor(new Color3f(Color.yellow));
		add(w3);
		Wall w4=new Wall(new Vector3d(0,0,10),20,2, this);
		w4.setColor(new Color3f(Color.yellow));
		add(w4);
		Box b1 = new Box(new Vector3d(-3, 0, -3), new Vector3f(1, 1, 2), this);
		add(b1);

		
		add(new SimbadRobot(new Vector3d(0, 0, 5),"Simbad1"));
		add(new SimbadRobot(new Vector3d(0, 0, 0),"Simbad2"));
	}
}
