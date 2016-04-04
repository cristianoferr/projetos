import java.awt.Graphics;
import java.util.Vector;

/*
 * Created on 22/11/2004
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
public class RobotLearn extends RobotBasico {
double comando=0;//1=frente,2=tras,3=esquerda,4=direita,5=frente-esquerda,
			  //6=frente-direita,7=tras-esquerda,8=tras-direita,9=garra
double angleDest=0;
double newAngle=0;

ObjetoPlano objetivo;
double distObjetivo=1000;
double lastPerf=0;
LearningNet learn;
int totInputs=0;
	/**
	 * @param w
	 * @param x
	 * @param y
	 */
	public RobotLearn(World w, int x, int y) {
		super(w, x, y,6,6);
		
	}
	public void turn(Graphics g){

		super.turn(g);
		if (learn!=null)
		  learn.turn();

		if (totInputs==0){
			Vector v=getInputs();
			totInputs=v.size();
			learn=new LearningNet(display,totInputs);
			
		}
		
		
		if (objetivo==null)	objetivo=world.getObjeto(1);
		if (timer>0) timer--;
		if (timer==0) if (comando!=0){
			double c=comando;
			comando=0;
			executaComando(c);
		} else
			{status=0;
			learn.treina();
			double newDist=distPontos(getX(),getY(),objetivo.getX(),objetivo.getY());
			newAngle=difAngles(calcAngle(getX(),getY(),objetivo.getX(),objetivo.getY()),getAngle());
			double perf=0;
			Vector out=getOutputs();
			
			for (int i=0;i<out.size();i++){
				Double d=(Double)out.elementAt(i);
				perf=perf+d.doubleValue();
			}
			perf=(distObjetivo-newDist)/10;
			if (energyAcquired) perf+=5;
			perf+=getGlobalPerf();
			//out("180/newAngle:"+180/newAngle+" newAngle:"+newAngle+" getAngle:"+getAngle()+" Ideal:"+calcAngle(getX(),getY(),objetivo.getX(),objetivo.getY()));
			perf+=(180/newAngle)*5;
			//perf=(newAngle-angleDest)/10+distObjetivo-newDist;
			//perf=perf; 
			//System.out.println("angleDest:"+angleDest+" newAngle:"+newAngle+" perf: "+perf );
			learn.checkPerformance(perf);
			lastPerf=perf;
			comando=learn.getAction(out);
			//out("dist:"+distObjetivo+" newDist:"+newDist+" c:"+comando);
			double c=comando;
			comando=0;
			executaComando(c);
			distObjetivo=newDist;
			angleDest=newAngle;
			learn.generateInputs();
			setGlobalPerf(0);
			
		} 
		//out("random:"+randomMovement()+" timer:"+timer);
	}
	
	public void executaComando(double c){
		if (totInputs==0) {
			out("Erro: totInputs Vazio!");
			abort(100);} 
		//out("C:"+c+" totInputs:"+totInputs );
		Vector v=int2bin((int)c,totInputs);
		setInputs(v);
		timer=5;
	}
	
	
	

	
	public int randomMovement(){
		int c=(int)(Math.random()*8+1);
		setComando(0);
		executaComando(c);
		return c;
	}
/**
 * @return
 */
public double getComando() {
	return comando;
}

/**
 * @param i
 */
public void setComando(double i) {
	comando = i;
}

}
