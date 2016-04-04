import java.awt.Color;
import java.awt.Graphics;


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
public class ObjetoPlano extends Objeto {
	private double angle=0;
	private double x=0;
	private double y=0;
	private int width=0;
	private int height=0;
	public static final double deg2rad=0.0174532925;
	public World world;
	int updatePlano=0;
	int id=0;
	static int totObjs=0;
	String name="";
	private double addtoX=0;
	private double addtoY=0;
	private double addtoAngle=0;
	private double addtoEnergy=0;
	private boolean PickedPossible=true;
	private boolean emiteEnergia=false;
	Color cor=Color.BLACK;

	

	public ObjetoPlano(int px,int py){
		super();
		id=totObjs;
		totObjs++;
		x=px;
		y=py;
	}
	public void desenha(Graphics g){
	}
	public void turn(Graphics g){
		setAngle(realAngle(getAngle()));

		if (getAddtoX()!=0)
		  setX(getX()+getAddtoX());
		addtoX=0;
		if (getAddtoY()!=0)
		setY(getY()+getAddtoY());
		addtoY=0;
		setAngle(getAngle()+getAddtoAngle());
		addtoAngle=0;
		if (getX()<0) setX(0);
		if (getY()<0) setY(0);
		if (getX()>world.getMaxX()) setX(world.getMaxX());
		if (getY()>world.getMaxY()) setY(world.getMaxY());
		
	}
	public static double realAngle(double angle){
		if (angle>360){
			angle=angle-360;
		}
		if (angle<0){
			angle=(360-Math.abs(angle));
		}
		return angle;
	}
	public Component getClosestCompoment(Component obj){
		return null;
	}
	/**
	 * @return
	 */
	public double getAngle() {
		return angle;
	}
	
	public String toString(){
		return name+" "+id;
	}

	/**
	 * @return
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param i
	 */
	public void setAngle(double i) {
		angle = i;
	}

	/**
	 * @param i
	 */
	public void setX(double i) {
		if (x!=i)
		  updatePlano=1;
		x = i;
		if (i==400){
		//  out("setPosx:"+i+" run:"+World.run);
//	  abort(100);
		}
		 
		
	}

	/**
	 * @param i
	 */
	public void setY(double i) {
		if (y!=i)
		  updatePlano=1;
		y = i;
		
	}

	

	/**
	 * @return
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * @param i
	 */
	public void setWidth(int i) {
		width = i;
	}

	/**
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param i
	 */
	public void setHeight(int i) {
		height = i;
	}
	public static double distPontos(double x1, double y1, double x2, double y2){
		return Math.abs(x1-x2)+Math.abs(y1-y2);
	}
	
	public static double difAngles(double ang1,double ang2){
		
		int ax=(int)(Math.cos(ang1 * deg2rad )*10);
		int ay=(int)(Math.sin(ang1*deg2rad)*10);

		int bx=(int)(Math.cos(ang2 * deg2rad )*10);
		int by=(int)(Math.sin(ang2*deg2rad)*10);

		
		double c1=calcAngle(0,0,ax,ay);
		double c2=calcAngle(0,0,bx,by);
		if ((c1<=90) && (c2>=270)) c1=c1+360;
		if ((c2<=90) && (c1>=270)) c2=c2+360;
		
		//out("ax:"+ax+" ay:"+ay+" bx:"+bx+" by:"+by+" c1:"+c1+" c2:"+c2);
		return Math.abs(c1-c2);
	}
	
	public static double calcAngle(double x1, double y1, double x2, double y2)
		{
			double dx = x2-x1;
			double dy = y2-y1;
			double angle=0.0d;

			// Calculate angle
			if (dx == 0.0)
			{
				if (dy == 0.0)
					angle = 0.0;
				else if (dy > 0.0)
					angle = Math.PI / 2.0;
				else
					angle = Math.PI * 3.0 / 2.0;
			}
			else if (dy == 0.0)
			{
				if  (dx > 0.0)
					angle = 0.0;
				else
					angle = Math.PI;
			}
			else
			{
				if  (dx < 0.0)
					angle = Math.atan(dy/dx) + Math.PI;
				else if (dy < 0.0)
					angle = Math.atan(dy/dx) + (2*Math.PI);
				else
					angle = Math.atan(dy/dx);
			}

			// Convert to degrees
			angle = angle * 180 / Math.PI;

			// Return
			return angle;
		}

	/**
	 * @param i
	 */
	public void setAddtoX(double i) {
		addtoX += i;
	}

	/**
	 * @param i
	 */
	public void setAddtoY(double i) {
		addtoY += i;
	}

	/**
	 * @return
	 */
	public double getAddtoX() {
		return addtoX;
	}

	/**
	 * @return
	 */
	public double getAddtoY() {
		return addtoY;
	}

	/**
	 * @param i
	 */
	public void setAddtoAngle(double i) {
		addtoAngle += i;
	}

	/**
	 * @return
	 */
	public double getAddtoAngle() {
		return addtoAngle;
	}
	/**
	 * @return
	 */
	public boolean isPickedPossible() {
		return PickedPossible;
	}

	/**
	 * @param b
	 */
	public void setPickedPossible(boolean b) {
		PickedPossible = b;
	}

	/**
	 * @return
	 */
	public Color getCor() {
		return cor;
	}

	/**
	 * @param color
	 */
	public void setCor(Color color) {
		cor = color;
	}

	/**
	 * @return
	 */
	public boolean isEmiteEnergia() {
		return emiteEnergia;
	}

	/**
	 * @param b
	 */
	public void setEmiteEnergia(boolean b) {
		emiteEnergia = b;
	}

	/**
	 * @return
	 */
	public double getAddtoEnergy() {
		double d=addtoEnergy;
		addtoEnergy=0;
		return d;
	}

	/**
	 * @param d
	 */
	public void AddtoEnergy(double d) {
		addtoEnergy += d;
	}

}
