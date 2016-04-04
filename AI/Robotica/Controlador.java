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
public class Controlador extends Objeto {
//static World mundo=new World();
public static void main(String[] args) {
	int x;
	int y;
	
	x=45;y=315;
	out("ang: a1:"+x+" a2:"+y+" "+ObjetoPlano.difAngles(x,y));
	x=315;y=45;
	out("ang: a1:"+x+" a2:"+y+" "+ObjetoPlano.difAngles(x,y));
	x=90+45;y=315;
	out("ang: a1:"+x+" a2:"+y+" "+ObjetoPlano.difAngles(x,y));
	x=180+45;y=315;
	out("ang: a1:"+x+" a2:"+y+" "+ObjetoPlano.difAngles(x,y));



}
public static void desenha(Graphics g){
//	mundo.desenha(g);
}
}
