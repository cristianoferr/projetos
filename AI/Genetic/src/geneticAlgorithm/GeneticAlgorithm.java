package geneticAlgorithm;
import java.util.Vector;

import assembler.Program;

import comum.BytePos;
import comum.Objeto;
/**
 * @author CMM4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GeneticAlgorithm extends Objeto  {
	int GA_POPSIZE	=	300;		// ga population size
	int GA_MAXITER	=	136384;		// maximum iterations
	double GA_ELITRATE	= 	0.1f;		// elitism rate
	double GA_MUTATIONRATE	=0.95f;		// mutation rate
	double GA_MUTATION	=	Math.random()*GA_MUTATIONRATE;
	String GA_TARGET="Hello World by Cristiano, testando algorítmos genéticos no JAVA!!!";
	//int showIn=tpHex;
	int showIn=tpChr;

	//int TARGET_SIZE=50;
	int TARGET_SIZE=GA_TARGET.length();
	
	Vector population, buffer;
	int iter=0;
	Program vm=new Program();
	int minFitness=999999;
	Vector output=new Vector();

	/**
	 * 
	 */

	public GeneticAlgorithm() {
		super();
		// TODO Auto-generated constructor stub
		out("GA_MUTATION:"+GA_MUTATION);
		

			Vector pop_alpha=new Vector(), pop_beta=new Vector();
			

			init_population(pop_alpha, pop_beta);
			
			
			population = pop_alpha;
			buffer = pop_beta;
			
			print_best(population);
			
			//Clock clk=new Clock();

			for (iter=0; iter<GA_MAXITER; iter++) {
				//clk.startClock();
				//out("a1:"+((ga_struct)population.elementAt(0)).getCRC()+" "+((ga_struct)population.elementAt(0)).getFitness());
				calc_fitness();		// calculate fitness
				
				//out("a2:"+((ga_struct)population.elementAt(0)).getCRC()+" "+((ga_struct)population.elementAt(0)).getFitness());
				sort_by_fitness();	// sort them
				//out("a3:"+((ga_struct)population.elementAt(0)).getCRC()+" "+((ga_struct)population.elementAt(0)).getFitness());
				print_best(population);		// print the best one
				
				//out("a4:"+((ga_struct)population.elementAt(0)).getCRC()+" "+((ga_struct)population.elementAt(0)).getFitness());
				if (((ga_struct)population.elementAt(0)).fitness == 0) break;
				
				//out("a5:"+((ga_struct)population.elementAt(0)).getCRC()+" "+((ga_struct)population.elementAt(0)).getFitness());
				mate();		// mate the population together
				
				mutation();
				//out("a6:"+((ga_struct)population.elementAt(0)).getCRC()+" "+((ga_struct)population.elementAt(0)).getFitness());
				swap();		// swap buffers
				
				//out("Tempo:"+clk.stopClock());
			}
		writeOutput(output,"output.txt");
		//showFinal();
	}


	private void showFinal() {
		vm.loadVector(new Vector(((ga_struct)population.elementAt(0)).str));
		vm.r0.value(2);
		vm.r1.value(0);
		vm.r2.value(0);
		vm.r3.value(0);
		vm.r4.value(50);
		vm.r5.value(0);
		vm.r6.value(0);
		vm.r7.value(0);
		vm.a.value(0);
		vm.b.value(0);
		
		//if (i==0){vm.run(true);
		//} else
		vm.run(true);			
		vm.displayStatus("Final");
		vm.displayAsm();
	}
	
	
	void init_population(Vector population,	Vector buffer ) 
	{
		int tsize = TARGET_SIZE;

		

		for (int i=0; i<GA_POPSIZE; i++) {
			ga_struct citizen=new ga_struct();
		
			citizen.fitness = 0;
			citizen.initialize(TARGET_SIZE,0,256);
			//out(citizen+"");

			/*for (int j=0; j<tsize; j++)
				citizen.str += (char)((Math.random() * 90) + 32);*/
			//((ga_struct)population.elementAt(i)).printVector();
			//citizen.printVector();
			population.add(citizen);
			buffer.add(new ga_struct());
			
		}
		Vector v=((ga_struct)population.elementAt(0)).str;
		
		//teste
		/*
		v.setElementAt(new BytePos(0x78),0);
		v.setElementAt(new BytePos(0x00),1);
		v.setElementAt(new BytePos(0x79),2);
		v.setElementAt(new BytePos(0x01),3);
		v.setElementAt(new BytePos(0x7A),4);
		v.setElementAt(new BytePos(0x02),5);
		v.setElementAt(new BytePos(0x7B),6);
		v.setElementAt(new BytePos(0x03),7);
		v.setElementAt(new BytePos(0x7C),8);
		v.setElementAt(new BytePos(0x03),9);
		v.setElementAt(new BytePos(0x7D),10);
		v.setElementAt(new BytePos(0x05),11);
		v.setElementAt(new BytePos(0x7E),12);
		v.setElementAt(new BytePos(0x02),13);
		v.setElementAt(new BytePos(0x7F),14);
		v.setElementAt(new BytePos(0x12),15);
		v.setElementAt(new BytePos(0x79),16);
		v.setElementAt(new BytePos(0x01),17);
		v.setElementAt(new BytePos(0x7A),18);
		v.setElementAt(new BytePos(0x02),19);
		for (int i=19;i<TARGET_SIZE;i++){
			v.setElementAt(new BytePos(0x00),i);
		}*/
	}
	
	void calc_fitness()
	{
		
		int tsize = TARGET_SIZE;
		int fitness;
		//int esize = (int) (GA_POPSIZE * GA_ELITRATE);
		//if (((ga_struct)population.elementAt(0)).fitness==0) esize=0;
		
		

		for (int i=0; i<GA_POPSIZE; i++) {
			
			fitness = 0;
			
			//Clock clkm=new Clock();
			
			//out("run:"+clkm.stopClock());
			
			fitness = getFitness_hello(fitness,i);
			/*for (int j=0; j<tsize; j++) {
				fitness += Math.abs((int)((ga_struct)population.elementAt(i)).str.charAt(j) - target.charAt(j));
			}*/
			
		
			((ga_struct)population.elementAt(i)).fitness = fitness;
			//out("fit:"+fitness);
		}
	}
	
	private int getFitness_hello(int fitness,int index){
		
		for (int i=0;i<TARGET_SIZE;i++){
			Vector str=((ga_struct)population.elementAt(index)).str;
			fitness+=Math.abs(((BytePos)str.elementAt(i)).value()-(int)GA_TARGET.charAt(i));
		}
		return fitness;
	}
	


	private int getFitness(int fitness,int index){
		boolean display=(index==0);
		vm.loadVector(new Vector(((ga_struct)population.elementAt(index)).str));
		for (int j=0;j<256;j++){
			((BytePos)vm.iram.elementAt(j)).value(0);
			((BytePos)vm.xram.elementAt(j)).value(0);
			((BytePos)vm.memoryCode.elementAt(j)).value(0);
		}					


		int r0=10;
		int r4=20;
		int fit1=r4+r0;
		int fit2=r4-r0;
		vm.r0.value(r0);
		vm.r1.value(0);
		vm.r2.value(0);
		vm.r3.value(0);
		vm.r4.value(r4);
		vm.r5.value(0);
		vm.r6.value(0);
		vm.r7.value(0);
		vm.a.value(0);
		vm.b.value(0);

		//if (i==0){vm.run(true);
		//} else
		vm.run();		
		fitness+=Math.abs(vm.r6.value()-fit1);
		fitness+=Math.abs(vm.r7.value()-fit2);
		//if (display)vm.displayStatus("afit:"+fitness+" minFit:"+minFitness);
		
		vm.reset();
		vm.linhasExecutadas=0;
		
		r0=25;
		r4=50;
		fit1=r4+r0;
		fit2=r4-r0;
		vm.r0.value(r0);
		vm.r1.value(0);
		vm.r2.value(0);
		vm.r3.value(0);
		vm.r4.value(r4);
		vm.r5.value(0);
		vm.r6.value(0);
		vm.r7.value(0);
		vm.a.value(0);
		vm.b.value(0);
		
		vm.run();		
		fitness+=Math.abs(vm.r6.value()-fit1);
		fitness+=Math.abs(vm.r7.value()-fit2);
		//if (display)vm.displayStatus("bfit:"+fitness+" minFit:"+minFitness);
				

		vm.reset();
		vm.linhasExecutadas=0;
		r0=5;
		r4=10;
		fit1=r4+r0;
		fit2=r4-r0;
		vm.r0.value(r0);
		vm.r1.value(0);
		vm.r2.value(0);
		vm.r3.value(0);
		vm.r4.value(r4);
		vm.r5.value(0);
		vm.r6.value(0);
		vm.r7.value(0);
		vm.a.value(0);
		vm.b.value(0);
		
		vm.run();		
		fitness+=Math.abs(vm.r6.value()-fit1);
		fitness+=Math.abs(vm.r7.value()-fit2);
		if (display)vm.displayStatus("cfit:"+fitness+" minFit:"+minFitness);

		//if (i==0)out("0Fitness:"+fitness+" "+vm.r0);
/*		fitness+=Math.abs(vm.r1.value()-128);
		//if (i==0)out("1Fitness:"+fitness+" "+vm.r1);
		fitness+=Math.abs(vm.r2.value()-128);
		//if (i==0)out("2Fitness:"+fitness+" "+vm.r2);
		fitness+=Math.abs(vm.r3.value()-128);
		//if (i==0)out("3Fitness:"+fitness+" "+vm.r3);
		fitness+=Math.abs(vm.r4.value()-128);
		//if (i==0)out("4Fitness:"+fitness+" "+vm.r4);
		fitness+=Math.abs(vm.r5.value()-128);
		//if (i==0)out("5Fitness:"+fitness+" "+vm.r5);
		fitness+=Math.abs(vm.r6.value()-128);
		//if (i==0)out("6Fitness:"+fitness+" "+vm.r6);
		fitness+=Math.abs(vm.r7.value()-128);
		//if (i==0) out("7Fitness:"+fitness+" "+vm.r7);
		fitness+=Math.abs(vm.cy.value()-1);*/
		
		return fitness;
	}
	
	boolean fitness_sort(ga_struct x, ga_struct y) 
	{ return (x.fitness > y.fitness); }
	
	
	void sort_by_fitness()
	{ 
		
		for (int i=0;i<population.size();i++){
			for (int j=i+1;j<population.size();j++){
				ga_struct x=new ga_struct();
				x.fitness=((ga_struct)population.elementAt(i)).fitness;
				x.str=new Vector(((ga_struct)population.elementAt(i)).str);
				ga_struct y=new ga_struct();
				y.fitness=((ga_struct)population.elementAt(j)).fitness;
				y.str=new Vector(((ga_struct)population.elementAt(j)).str);
				//ga_struct xaux=new ga_struct((ga_struct)population.elementAt(i));
				
				if (fitness_sort(x,y)) {
				  population.setElementAt(y,i);
				  population.setElementAt(x,j);	
				 // out("y:"+y.fitness+" x:"+x.fitness);
				}
			}
			
		}
		if (((ga_struct)population.elementAt(0)).fitness<minFitness)
		  minFitness=((ga_struct)population.elementAt(0)).fitness;
		
	}
	//	sort(population.begin(), population.end(), fitness_sort); }



	Vector elitism(Vector population, 
					Vector buffer, int esize )
	{
		for (int i=0; i<esize; i++) {
			((ga_struct)buffer.elementAt(i)).str = new Vector(((ga_struct)population.elementAt(i)).str);
			((ga_struct)buffer.elementAt(i)).fitness = ((ga_struct)population.elementAt(i)).fitness;
			
		}
		return buffer;
	}

	void mutation(){
		
		for (int i=0; i<GA_POPSIZE; i++) {
		if (Math.random() < GA_MUTATION) {
			buffer.setElementAt(mutate((ga_struct)buffer.elementAt(i)),i);
		}
		}
		
	}
	ga_struct mutate(ga_struct member)
	{
		int tsize = TARGET_SIZE;
		int ipos = (int)(Math.random() * tsize);
		int delta = (int)((Math.random() * 10) - (Math.random() * 10) ); 
		
		int valorAtual=((BytePos)member.str.elementAt(ipos)).value();
		((BytePos)member.str.elementAt(ipos)).value(valorAtual+delta);
		//out("mutate:"+delta+" "+ipos+" "+valorAtual);
		
			
		/*char[] ch=member.str.toCharArray();
		String s=member.str;
		ch[ipos] = (char)((ch[ipos] + delta));
		if (ch[ipos]<32) ch[ipos] = (char)((ch[ipos] + Math.random()*23));
		member.str=String.copyValueOf(ch);
		//out("s1:"+s+" s2:"+member.str);
		*/
		return member;
	}
	
	void mate()
	{
		int esize = (int) (GA_POPSIZE * GA_ELITRATE);
		int tsize = TARGET_SIZE, spos, i1, i2;

		buffer=elitism(population, buffer, esize);
		String s="";

		// Mate the rest
		//out("esize:"+esize+" GA_POPSIZE:"+GA_POPSIZE);
		int totMutations=0;
		for (int i=esize; i<GA_POPSIZE; i++) {
			i1 = (int)(Math.random() * (GA_POPSIZE/1.5));
			i2 = (int)(Math.random() * (GA_POPSIZE/1.5));
			
			/*i1=i;
			i2=i+1;
			if (i2==GA_POPSIZE) i2=(int)(Math.random() * (GA_POPSIZE));*/
			//out("aaaa");
			spos = (int)(Math.random() * tsize);
			
			int len=((ga_struct)population.elementAt(i2)).str.size();
			Vector bufferstr=((ga_struct)buffer.elementAt(i)).str;
			
			Vector pi1=((ga_struct)population.elementAt(i1)).str;
			Vector pi2=((ga_struct)population.elementAt(i2)).str;
			
			Vector v1=new Vector();
			Vector v2=new Vector();
			
			for (int j=0;j<spos;j++){
				int v=((BytePos)pi1.elementAt(j)).value();
				v1.add(new BytePos(v));
			}
			for (int j=spos;j<len;j++){
				int v=((BytePos)pi2.elementAt(j)).value();
				v2.add(new BytePos(v));
			}
			
			s="";
			((ga_struct)buffer.elementAt(i)).str.clear();
			
			for (int j=0;j<v1.size();j++){
				bufferstr.add(new BytePos((BytePos)v1.elementAt(j)));
				s=s+((BytePos)v1.elementAt(j)).getHex();
			}
			s=s+"|";
			
			for (int j=0;j<v2.size();j++){
				int v=((BytePos)v2.elementAt(j)).value();
				bufferstr.add(new BytePos(v));
				s=s+((BytePos)v2.elementAt(j)).getHex();
			}
			//if (i<5)out("i1:"+i1+" i2:"+i2+" S2:"+s);
			//((ga_struct)buffer.elementAt(i)).str = s1 + s2;
			//if (i==esize)
			//out("str:"+((ga_struct)buffer.elementAt(i)).str);*/

			 
		}
		//out("TotMutations:"+totMutations);
	}

	void print_best(Vector gav)	{
		((ga_struct)gav.elementAt(0)).showIn=showIn;
		String s=" (" + ((ga_struct)gav.elementAt(0)).fitness + ")"+" Iterações:"+iter+" Best: " +((ga_struct)gav.elementAt(0)); 
		out(s);
		output.add(s);
	
	int tot=GA_POPSIZE / 10;
	//if (tot>20) tot=20;
	int i=0;
	//out("tot:"+tot+" "+(GA_POPSIZE / 10));
		for (i=0;i<GA_POPSIZE;i=i+tot){
			((ga_struct)gav.elementAt(i)).showIn=showIn;
		//	out("i:"+i+" "+((ga_struct)gav.elementAt(i)).fitness+" "+((ga_struct)gav.elementAt(i)).getCRC()+" "+((ga_struct)gav.elementAt(i)));		 
		}
		i=GA_POPSIZE-1;
		((ga_struct)gav.elementAt(i)).showIn=showIn;		
		//out("i:"+i+" "+((ga_struct)gav.elementAt(i)).fitness+" "+((ga_struct)gav.elementAt(i)).getCRC()+" "+((ga_struct)gav.elementAt(i)));
		
	}

	void swap()
	{ 
		Vector temp = population; 
		population = buffer; buffer = temp; 
	}



}
