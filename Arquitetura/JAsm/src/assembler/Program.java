package assembler;
import java.util.Vector;

import comum.BytePos;
import comum.Clock;
import comum.MathUtils;

/*
 * Created on 04/07/2005
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
public class Program extends InstructionSet {
public int linhasExecutadas=0;
public int codeSize=0;
public int maxCycles=100;
public static int totCycles=0;
public Clock clk=new Clock();
//public Vector asmCode=new Vector();
Vector<Short> parms=new Vector<Short>();

boolean isRunning=true;


	public boolean isRunning() {
	return isRunning;
}

	/**
	 * 
	 */
	public Program() {
		super();
		// TODO Auto-generated constructor stub
		AssembleFile.inicializaVariaveis();
	}
	
public void loadFile(String file,int posIni){
	this.posIni=posIni;
	reset();
	
	AssembleFile.loadFile(file,posIni);
	
	codeSize=AssembleFile.codeSize;
	reset();
}
public double getMHZ(){
	return (totCycles/clk.stopClock());
}
public void loadVector(Vector vCode,int posIni){
	//Clock clk=new Clock();
	reset();
	linhasExecutadas=0;
	String s="";

	//memoryCode=vCode;
	for (int i=0;i<vCode.size();i++){
		VirtualMachine.setXramAt(posIni+i,((BytePos)vCode.elementAt(i)).value());
		//memoryCode.setElementAt(vCode.elementAt(i),i);
		//s=s+((BytePos)vCode.elementAt(i)).getHex();
		
	}
//	out("Load:"+clk.stopClock());
	
	codeSize=vCode.size();
}
	public void run(){
		run(true);
	}
	public void run(boolean display){
		
		//displayStatus("Inicial");
		
		//asmCode.clear();
		
		if (linhasExecutadas>maxCycles) isRunning=false;
		if (pc.value()-posIni>codeSize) isRunning=false;
		
			
			String hexCode=MathUtils.int2hex(VirtualMachine.getXramAt(pc.value()));
			int bytes=AssembleFile.getBytesDB(hexCode);
			parms.clear();
			int i=1;
			String parmS="";
			while (i<bytes) {
				//if (pc.value()+i<codeSize){
					parms.add((VirtualMachine.getXramAt(pc.value()+i)));
					parmS=parmS+" "+(VirtualMachine.getXramAt(pc.value()+i));
				//	if (display) out("i:"+((BytePos)memoryCode.elementAt(pc.value()+i)).value()+" "+((BytePos)memoryCode.elementAt(pc.value()+i)).dado+" "+((BytePos)memoryCode.elementAt(pc.value()+i)).tipo);
				//}
				i++;
			}
			//System.out.print("*");
			if (display)
			displayStatus(pc.getHex()+":"+linhasExecutadas+":"+hexCode+"="+AssembleFile.getComandoDB(hexCode)+"=> parms:"+parmS);
			
			if (display) adicionaAsm(hexCode,parms);
			pc.value(pc.value()+bytes);
			
			runCode(hexCode,parms);
			//displayStatus("@@@");			
			
			
		}
		//out("");
		//displayStatus("Final");
		//displayStatus("Linhas Executadas:"+linhasExecutadas+" Tamanho Código:"+codeSize);
		

	
	public void adicionaAsm(String hexCode, Vector parms){
		Vector v=AssembleFile.getAsmDB(hexCode);
		
		if (v==null) return;
		String s=v.elementAt(1)+" ";
		int p=0;
		for (int i=2;i<5;i++) {
			String dbp=(String)v.elementAt(i);
			if ((dbp.equals("#"))||(dbp.equals("#16"))) {
				s=s+"#"+parms.elementAt(p)+" ";
					p++;
			}
				
			if ((dbp.equals("DIRETO")) ||(dbp.equals("BIT"))
			|| (dbp.equals("\\BIT")) ||(dbp.equals("REL"))
			|| (dbp.equals("END16")) ||(dbp.equals("END11"))
			){
				s=s+parms.elementAt(p)+" ";
				p++;
			} else {
				s=s+dbp+" ";
			}
			
		}
		s=s+" ;"+v.elementAt(0);
		//asmCode.add(s);
		
	}
	public void displayAsm(){
	//	for (int i=0;i<asmCode.size();i++){
	//		out(""+asmCode.elementAt(i));
	//	}
	}
	
	public void runCode(String hexCode,Vector<Short> parms){
		linhasExecutadas++;
		totCycles++;
		BytePos p1=new BytePos(0);
		BytePos p2=new BytePos(0);
		BytePos p3=new BytePos(0);
		BytePos byte16=new BytePos(0);
		

		if (parms.size()>1){
			String bin=MathUtils.vector2string(MathUtils.int2bin(p1))+MathUtils.vector2string(MathUtils.int2bin(p2));
			byte16.setBits(16);
			byte16.value(MathUtils.bin2int(bin));
		}
		
		
		if (parms.size()>0) p1.value(parms.elementAt(0));
		if (parms.size()>1) p2.value(parms.elementAt(1));
		if (parms.size()>2) p3.value(parms.elementAt(2));
		
		switch (MathUtils.hex2Byte(hexCode)) {
			case 0x00:nop();
			break;case 0x01:ajmp(p1.value());
			break; case 0x02:ljmp(byte16);
			break; case 0x03:rr(a);
			break; case 0x04:{inc(a);}
			break; case 0x05:inc(new BytePos(tpIram,p1.value()));
			break; case 0x06:inc(r0.at());
			break; case 0x07:inc(r1.at());
			break; case 0x08:inc(r0);
			break; case 0x09:inc(r1);
			break; case 0x0A:inc(r2);
			break; case 0x0B:inc(r3);
			break; case 0x0C:inc(r4);
			break; case 0x0D:inc(r5);
			break; case 0x0E:inc(r6);
			break; case 0x0F:inc(r7);

			break; case 0x10:{
			p1.setPos(p1.value());
			p1.setTipo(tpBin);
			jbc(p1,p2);			}
			//break; case 0x11:acall(p1.value());
			break; case 0x11:
			break; case 0x12:lcall(byte16);
			break; case 0x13:rrc(a);
			break; case 0x14:dec(a);
			break; case 0x15:dec(new BytePos(tpIram,p1.value()));
			break; case 0x16:dec(r0.at());
			break; case 0x17:dec(r1.at());
			break; case 0x18:dec(r0);
			break; case 0x19:dec(r1);
			break; case 0x1A:dec(r2);
			break; case 0x1B:dec(r3);
			break; case 0x1C:dec(r4);
			break; case 0x1D:dec(r5);
			break; case 0x1E:dec(r6);
			break; case 0x1F:dec(r7);
			
			break; case 0x20:{
			p1.setPos(p1.value());
			p1.setTipo(tpBin);
			jb(p1,p2);			}
			break; case 0x21:
			break; case 0x22:ret();
			break; case 0x23:rl(a);
			break; case 0x24:add(a,p1);
			break; case 0x25:add(a,new BytePos(tpIram,p1.value()));
			break; case 0x26:add(a,r0.at());
			break; case 0x27:add(a,r1.at());
			break; case 0x28:add(a,r0);
			break; case 0x29:add(a,r1);
			break; case 0x2A:add(a,r2);
			break; case 0x2B:add(a,r3);
			break; case 0x2C:add(a,r4);
			break; case 0x2D:add(a,r5);
			break; case 0x2E:add(a,r6);
			break; case 0x2F:add(a,r7);			

			break; case 0x30:{
			p1.setPos(p1.value());
			p1.setTipo(tpBin);
			jnb(p1,p2);			}
			break; case 0x31:
			break; case 0x32:reti();
			break; case 0x33:rlc(a);
			break; case 0x34:addc(a,p1);
			break; case 0x35:addc(a,new BytePos(tpIram,p1.value()));
			break; case 0x36:addc(a,r0.at());
			break; case 0x37:addc(a,r1.at());
			break; case 0x38:addc(a,r0);
			break; case 0x39:addc(a,r1);
			break; case 0x3A:addc(a,r2);
			break; case 0x3B:addc(a,r3);
			break; case 0x3C:addc(a,r4);
			break; case 0x3D:addc(a,r5);
			break; case 0x3E:addc(a,r6);
			break; case 0x3F:addc(a,r7);										

			break; case 0x40:jc(p1);
			break; case 0x41:
			break; case 0x42:orl(new BytePos(tpIram,p1.value()),a);
			break; case 0x43:orl(new BytePos(tpIram,p1.value()),p1);
			break; case 0x44:orl(a,p1);
			break; case 0x45:orl(a,new BytePos(tpIram,p1.value()));
			break; case 0x46:orl(a,r0.at());
			break; case 0x47:orl(a,r1.at());
			break; case 0x48:orl(a,r0);
			break; case 0x49:orl(a,r1);
			break; case 0x4A:orl(a,r2);
			break; case 0x4B:orl(a,r3);
			break; case 0x4C:orl(a,r4);
			break; case 0x4D:orl(a,r5);
			break; case 0x4E:orl(a,r6);
			break; case 0x4F:orl(a,r7);
				
			break; case 0x50:jnc(p1);
			break; case 0x51:
			break; case 0x52:anl(new BytePos(tpIram,p1.value()),a);
			break; case 0x53:anl(new BytePos(tpIram,p1.value()),p1);
			break; case 0x54:anl(a,p1);
			break; case 0x55:anl(a,new BytePos(tpIram,p1.value()));
			break; case 0x56:anl(a,r0.at());
			break; case 0x57:anl(a,r1.at());
			break; case 0x58:anl(a,r0);
			break; case 0x59:anl(a,r1);
			break; case 0x5A:anl(a,r2);
			break; case 0x5B:anl(a,r3);
			break; case 0x5C:anl(a,r4);
			break; case 0x5D:anl(a,r5);
			break; case 0x5E:anl(a,r6);
			break; case 0x5F:anl(a,r7);	

			break; case 0x60:jz(p1);
			break; case 0x61:
			break; case 0x62:xrl(new BytePos(tpIram,p1.value()),a);
			break; case 0x63:xrl(new BytePos(tpIram,p1.value()),p1);
			break; case 0x64:xrl(a,p1);
			break; case 0x65:xrl(a,new BytePos(tpIram,p1.value()));
			break; case 0x66:xrl(a,r0.at());
			break; case 0x67:xrl(a,r1.at());
			break; case 0x68:xrl(a,r0);
			break; case 0x69:xrl(a,r1);
			break; case 0x6A:xrl(a,r2);
			break; case 0x6B:xrl(a,r3);
			break; case 0x6C:xrl(a,r4);
			break; case 0x6D:xrl(a,r5);
			break; case 0x6E:xrl(a,r6);
			break; case 0x6F:xrl(a,r7);	

			break; case 0x70:jnz(p1);
			break; case 0x71:
			break; case 0x72:{
			p1.setPos(p1.value());
			p1.setTipo(tpBin);
			orl(cy,p1);			}
			break; case 0x73:jmp();
			break; case 0x74:mov(a,p1);
			break; case 0x75:mov(new BytePos(tpIram,p1.value()),p2);
			break; case 0x76:mov(r0.at(),p1);
			break; case 0x77:mov(r1.at(),p1);
			break; case 0x78:mov(r0,p1);
			break; case 0x79:{
				mov(r1,p1);
				//out("r1:"+p1);
			}
			break; case 0x7A:mov(r2,p1);
			break; case 0x7B:mov(r3,p1);
			break; case 0x7C:{mov(r4,p1);
				//out("7c:"+p1);
			}
			break; case 0x7D:mov(r5,p1);
			break; case 0x7E:mov(r6,p1);
			break; case 0x7F:mov(r7,p1);
			break; case 0x80:sjmp(p1);
			break; case 0x81:
			break; case 0x82:{
			p1.setPos(p1.value());
			p1.setTipo(tpBin);
			anl(cy,p1);			}
			break; case 0x83:movc(a,pc);
			break; case 0x84:div(a,b);
			break; case 0x85:mov(new BytePos(tpIram,p2.value()),new BytePos(tpIram,p1.value()));
			break; case 0x86:mov(new BytePos(tpIram,p1.value()),r0.at());
			break; case 0x87:mov(new BytePos(tpIram,p1.value()),r1.at());
			break; case 0x88:mov(new BytePos(tpIram,p1.value()),r0);
			break; case 0x89:mov(new BytePos(tpIram,p1.value()),r1);
			break; case 0x8A:mov(new BytePos(tpIram,p1.value()),r2);
			break; case 0x8B:mov(new BytePos(tpIram,p1.value()),r3);
			break; case 0x8C:mov(new BytePos(tpIram,p1.value()),r4);
			break; case 0x8D:mov(new BytePos(tpIram,p1.value()),r5);
			break; case 0x8E:mov(new BytePos(tpIram,p1.value()),r6);
			break; case 0x8F:mov(new BytePos(tpIram,p1.value()),r7);	

			break; case 0x90:mov(dptr,byte16);
			break; case 0x91:
			break; case 0x92:{
			p1.setPos(p1.value());
			p1.setTipo(tpBin);
			mov(p1,cy);			}
			break; case 0x93:movc(a,dptr);
			break; case 0x94:subb(a,p1);
			break; case 0x95:subb(a,new BytePos(tpIram,p1.value()));
			break; case 0x96:subb(a,r0.at());
			break; case 0x97:subb(a,r1.at());
			break; case 0x98:subb(a,r0);
			break; case 0x99:subb(a,r1);
			break; case 0x9A:subb(a,r2);
			break; case 0x9B:subb(a,r3);
			break; case 0x9C:subb(a,r4);
			break; case 0x9D:subb(a,r5);
			break; case 0x9E:subb(a,r6);
			break; case 0x9F:subb(a,r7);		

			break; case 0xA0:{
				p1.setPos(p1.value());
				p1.setTipo(tpBin);
				
				orl(cy,p1.not());			}
			break; case 0xA1:
			break; case 0xA2:{
				p1.setPos(p1.value());
				p1.setTipo(tpBin);
				mov(cy,p1);			}
			break; case 0xA3:inc(dptr);
			break; case 0xA4:mul_AB();
			break; case 0xA5:
			break; case 0xA6:mov(r0.at(),new BytePos(tpIram,p1.value()));
			break; case 0xA7:mov(r1.at(),new BytePos(tpIram,p1.value()));
			break; case 0xA8:mov(r0,new BytePos(tpIram,p1.value()));
			break; case 0xA9:mov(r1,new BytePos(tpIram,p1.value()));
			break; case 0xAA:mov(r2,new BytePos(tpIram,p1.value()));
			break; case 0xAB:mov(r3,new BytePos(tpIram,p1.value()));
			break; case 0xAC:mov(r4,new BytePos(tpIram,p1.value()));
			break; case 0xAD:mov(r5,new BytePos(tpIram,p1.value()));
			break; case 0xAE:mov(r6,new BytePos(tpIram,p1.value()));
			break; case 0xAF:mov(r7,new BytePos(tpIram,p1.value()));	

			break; case 0xB0:{
			p1.setPos(p1.value());
			p1.setTipo(tpBin);
			anl(cy,p1.not());			}
			break; case 0xB1:
			break; case 0xB2:{
					p1.setPos(p1.value());
					p1.setTipo(tpBin);
					cpl(p1);			}
			break; case 0xb3:cpl(cy);
			break; case 0xB4:cjne(a,p1,p2);
			break; case 0xB5:cjne(a,new BytePos(tpIram,p1.value()),p2);
			break; case 0xB6:cjne(r0.at(),p1,p2);
			break; case 0xB7:cjne(r1.at(),p1,p2);
			break; case 0xB8:cjne(r0,p1,p2);
			break; case 0xB9:cjne(r1,p1,p2);
			break; case 0xBA:cjne(r2,p1,p2);
			break; case 0xBB:cjne(r3,p1,p2);
			break; case 0xBC:cjne(r4,p1,p2);
			break; case 0xBD:cjne(r5,p1,p2);
			break; case 0xBE:cjne(r6,p1,p2);
			break; case 0xBF:cjne(r7,p1,p2);	

			break; case 0xC0:push(p1);
			break; case 0xC1:
			break; case 0xC2:{
				
				p1.setPos(p1.value());
				p1.setTipo(tpBin);
				clr(p1);
			}
			break; case 0xC3:clr(cy);
			break; case 0xC4:swap(a);
			break; case 0xC5:xch(a,new BytePos(tpIram,p1.value()));
			break; case 0xC6:xch(a,r0.at());
			break; case 0xC7:xch(a,r1.at());
			break; case 0xC8:xch(a,r0);
			break; case 0xC9:xch(a,r1);
			break; case 0xCA:xch(a,r2);
			break; case 0xCB:xch(a,r3);
			break; case 0xCC:xch(a,r4);
			break; case 0xCD:xch(a,r5);
			break; case 0xCE:xch(a,r6);
			break; case 0xCF:xch(a,r7);	


			break; case 0xD0:pop(p1);
			break; case 0xD1:
			break; case 0xD2:{
			p1.setPos(p1.value());
			p1.setTipo(tpBin);
			setb(p1);	}
			break; case 0xD3:setb(cy);
			break; case 0xD4:da(a);
			break; case 0xD5:djnz(new BytePos(tpIram,p1.value()),p2.value());
			break; case 0xD6:xchd(a,r0.at());
			break; case 0xD7:xchd(a,r1.at());
			break; case 0xD8:djnz(r0,p1.value());
			break; case 0xD9:djnz(r1,p1.value());
			break; case 0xDA:djnz(r2,p1.value());
			break; case 0xDB:djnz(r3,p1.value());
			break; case 0xDC:djnz(r4,p1.value());
			break; case 0xDD:djnz(r5,p1.value());
			break; case 0xDE:djnz(r6,p1.value());
			break; case 0xDF:djnz(r7,p1.value());	
			
			break; case 0xE0:movx_fromX(a,dptr.at());
			break; case 0xE1:
			break; case 0xE2:movx_fromX(a,r0.at());
			break; case 0xE3:movx_fromX(a,r1.at());
			break; case 0xE4:clr_A();
			break; case 0xE5:mov(a,new BytePos(tpIram,p1.value()));
			break; case 0xE6:mov(a,r0.at());
			break; case 0xE7:mov(a,r1.at());
			break; case 0xE8:mov(a,r0);
			break; case 0xE9:mov(a,r1);
			break; case 0xEA:mov(a,r2);
			break; case 0xEB:mov(a,r3);
			break; case 0xEC:mov(a,r4);
			break; case 0xED:mov(a,r5);
			break; case 0xEE:mov(a,r6);
			break; case 0xEF:mov(a,r7);	
						
			break; case 0xF0:movx_toX(dptr.at(),a);
			break; case 0xF1:
			break; case 0xF2:movx_toX(r0.at(),a);
			break; case 0xF3:movx_toX(r1.at(),a);
			break; case 0xF4:cpl(a);
			break; case 0xF5:mov(new BytePos(tpIram,p1.value()),a);
			break; case 0xF6:mov(r0.at(),a);
			break; case 0xF7:mov(r1.at(),a);
			break; case 0xF8:mov(r0,a);
			break; case 0xF9:mov(r1,a);
			break; case 0xFA:mov(r2,a);
			break; case 0xFB:mov(r3,a);
			break; case 0xFC:mov(r4,a);
			break; case 0xFD:mov(r5,a);
			break; case 0xFE:mov(r6,a);
			break; case 0xFF:mov(r7,a);	
						
			
			
			break;default:{ out("Não implementado:"+hexCode);	
			}
		}
		
	}
/**
 * @return
 */
public int getCodeSize() {
	return codeSize;
}

/**
 * @return
 */
public int getLinhasExecutadas() {
	return linhasExecutadas;
}

/**
 * @return
 */
public int getMaxCycles() {
	return maxCycles;
}

/**
 * @param i
 */
public void setMaxCycles(int i) {
	maxCycles = i;
}

}
