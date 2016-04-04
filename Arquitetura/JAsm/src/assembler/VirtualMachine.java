package assembler;

import java.util.Vector;

import comum.BytePos;

public class VirtualMachine {
public Vector<Program> programs=new Vector<Program>();
private static final int xSize=(int)Math.pow(2,16);
private	static short[] xram=new short[xSize];

	public VirtualMachine(){
		for (int i=0;i<xSize;i++){
			xram[i]=0;
		}
	}
	
	public static short getXramAt(int i){
		while (i>xSize) i=i-xSize;
		return xram[i];
	}
	
	public static void setXramAt(int i,BytePos p){
		while (i>xSize) i=i-xSize;
		xram[i]=(short)p.value();
		//xram.setElementAt(p,i);
	}
	
	public static void setXramAt(int i,int p){
		while (i>xSize) i=i-xSize;
		xram[i]=(short)p;
		//xram.setElementAt(p,i);
	}
	
	public void loadFile(String arg){
		
		Program vm=new Program();
		
		int posIni=(int)(Math.random()*xSize);
		vm.loadFile(arg,posIni);
		
		programs.add(vm);
		//vm.run();
		
		
	}
	
	public void run(){
		
		while (programs.size()>0){
			for (int i=0;i<programs.size();i++){
				
				programs.get(i).run();
				if (!programs.get(i).isRunning) programs.remove(i);
			}
		
		}
		System.out.println("End Execution");
		//vm.displayStatus("Fim:");
	}
	
}
