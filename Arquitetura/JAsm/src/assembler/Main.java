package assembler;
/*
 * Created on 24/06/2005
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
public class Main {
static String workDir="f:\\pessoal\\Eletronica\\Programa fonte\\";
static VirtualMachine vm;
	/**
	 * 
	 */
	public Main() {
		super();
		String args[];
		args=new String[1];
		args[0]=workDir+"teste1.a51";
		
		// TODO Auto-generated constructor stub
		main(args);
	}

	public static void main(String[] args) {
		
		vm=new VirtualMachine();
		if (args.length<1) {
			args=new String[1];
			args[0]=workDir+"teste1.a51";
		}
		
		vm.loadFile(args[0]);
		vm.loadFile(args[0]);
		vm.loadFile(args[0]);
		vm.loadFile(args[0]);
		
		vm.run();
	}
}
