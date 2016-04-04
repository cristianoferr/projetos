package assembler;
import java.util.Vector;

import comum.BytePos;
import comum.MathUtils;
import comum.Objeto;

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
public class InstructionSet  extends Objeto{
	private int codeSize=(int)Math.pow(2,13c);
	public Vector<BytePos> iram=new Vector<BytePos>();
	int posIni=0;

	
	public BytePos a=new BytePos(tpIram,224);
	public BytePos b=new BytePos(tpIram,240);

BytePos p0=new BytePos(tpIram,128);
BytePos p0b0=new BytePos(tpBin,0,p0);
BytePos p0b1=new BytePos(tpBin,1,p0);
BytePos p0b2=new BytePos(tpBin,2,p0);
BytePos p0b3=new BytePos(tpBin,3,p0);
BytePos p0b4=new BytePos(tpBin,4,p0);
BytePos p0b5=new BytePos(tpBin,5,p0);
BytePos p0b6=new BytePos(tpBin,6,p0);
BytePos p0b7=new BytePos(tpBin,7,p0);

BytePos p1=new BytePos(tpIram,144);
BytePos p1b0=new BytePos(tpBin,0,p1);
BytePos p1b1=new BytePos(tpBin,1,p1);
BytePos p1b2=new BytePos(tpBin,2,p1);
BytePos p1b3=new BytePos(tpBin,3,p1);
BytePos p1b4=new BytePos(tpBin,4,p1);
BytePos p1b5=new BytePos(tpBin,5,p1);
BytePos p1b6=new BytePos(tpBin,6,p1);
BytePos p1b7=new BytePos(tpBin,7,p1);

BytePos p2=new BytePos(tpIram,160);
BytePos p2b0=new BytePos(tpBin,0,p2);
BytePos p2b1=new BytePos(tpBin,1,p2);
BytePos p2b2=new BytePos(tpBin,2,p2);
BytePos p2b3=new BytePos(tpBin,3,p2);
BytePos p2b4=new BytePos(tpBin,4,p2);
BytePos p2b5=new BytePos(tpBin,5,p2);
BytePos p2b6=new BytePos(tpBin,6,p2);
BytePos p2b7=new BytePos(tpBin,7,p2);

BytePos p3=new BytePos(tpIram,176);
BytePos p3b0=new BytePos(tpBin,0,p3);
BytePos p3b1=new BytePos(tpBin,1,p3);
BytePos p3b2=new BytePos(tpBin,2,p3);
BytePos p3b3=new BytePos(tpBin,3,p3);
BytePos p3b4=new BytePos(tpBin,4,p3);
BytePos p3b5=new BytePos(tpBin,5,p3);
BytePos p3b6=new BytePos(tpBin,6,p3);
BytePos p3b7=new BytePos(tpBin,7,p3);

BytePos tmod=new BytePos(tpIram,MathUtils.hex2Byte("89"));
BytePos th0=new BytePos(tpIram,MathUtils.hex2Byte("8C"));
BytePos tl0=new BytePos(tpIram,MathUtils.hex2Byte("8A"));
BytePos th1=new BytePos(tpIram,MathUtils.hex2Byte("8D"));
BytePos tl1=new BytePos(tpIram,MathUtils.hex2Byte("8B"));

BytePos tcon=new BytePos(tpIram,MathUtils.hex2Byte("88"));
BytePos tr0=new BytePos(tpBin,4,tcon);
BytePos tf0=new BytePos(tpBin,5,tcon);

public BytePos r0=new BytePos(tpIram,0);
public BytePos r1=new BytePos(tpIram,1);
public BytePos r2=new BytePos(tpIram,2);
public BytePos r3=new BytePos(tpIram,3);
public BytePos r4=new BytePos(tpIram,4);
public BytePos r5=new BytePos(tpIram,5);
public BytePos r6=new BytePos(tpIram,6);
public BytePos r7=new BytePos(tpIram,7);;
public BytePos dptr=new BytePos(tpIram,MathUtils.hex2Byte("82"));
public BytePos pc=new BytePos(0);
public BytePos sp=new BytePos(tpIram,MathUtils.hex2Byte("81"));

public BytePos psw=new BytePos(0);
public BytePos cy=new BytePos(tpBin,7,psw);
public BytePos ac=new BytePos(tpBin,6,psw);
public BytePos ov=new BytePos(tpBin,3,psw);

//public Vector<BytePos> memoryCode=new Vector<BytePos>();

	/**
	 * 
	 */
	public InstructionSet() {
		super();
//		memoryCode.clear();
	//	xram.clear();
		iram.clear();
	/*	for (int i=memoryCode.size();i<codeSize;i++){
					memoryCode.add(new BytePos(0));
					memoryCode.elementAt(i).setShowIn(MathUtils.tpHex);
			
		}*/
		


		for (int i=0;i<256;i++){
			iram.add(new BytePos(0));
		}		
		
		
	}
	
	public void subb(BytePos p1, BytePos p2){
		String bini=MathUtils.vector2string(MathUtils.int2bin(p1));
		int i1=p1.value();
		p1.value(p1.value() - cy.value() - p2.value());
		if ((i1 - cy.value() - p2.value())<0) {cy.value(1);
		} else {
			cy.value(0);
		}
		String binp=MathUtils.vector2string(MathUtils.int2bin(p1));
		int i0a3=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(i1,8)),0,3));
		int p0a3=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(p2)),0,3));
		
		
		if ((i0a3 - cy.value() - p0a3)<0) {ac.value(1);
		} else {
			ac.value(0);
		}
		
		int i0a6=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(i1,8)),0,6));
		int p0a6=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(p2)),0,6));
		int r0a6=i0a6-cy.value()-p0a6;

		int i0a7=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(i1,8)),0,7));
		int p0a7=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(p2)),0,7));
		int r0a7=i0a6-cy.value()-p0a7;
		
		//01234567
		//76543210
		if (((r0a7<0) && (r0a6>=0)) || ((r0a6<0) && (r0a7>=0))) ov.value(1);
	
		//out("i1: "+i1+" p1:"+p1+" cy.value:"+cy.value());
	}
	
	
	public void reset() {
		BytePos.iram=iram;
		dptr.setBits(16);
		pc.setBits(16);

		// TODO Auto-generated constructor stub
		

		p0.value(255);
		p1.value(255);
		p2.value(255);
		p3.value(255);
		sp.value(7);
		pc.value(posIni);
		
		a.value(0);
		psw.value(0);
		dptr.value(0);
		b.value(0);	
	}
	public void xchd(BytePos p1,BytePos p2){
		String i1=subBin(MathUtils.vector2string(MathUtils.int2bin(p1)),0,3);
		String i2=subBin(MathUtils.vector2string(MathUtils.int2bin(p1)),4,7);

		String o1=subBin(MathUtils.vector2string(MathUtils.int2bin(p2)),0,3);
		String o2=subBin(MathUtils.vector2string(MathUtils.int2bin(p2)),4,7);		
		p2.value(MathUtils.bin2int(o2+i1));
		p1.value(MathUtils.bin2int(i2+o1));
	}	
	public void swap(BytePos p1){
		String i1=subBin(MathUtils.vector2string(MathUtils.int2bin(p1)),0,3);
		String i2=subBin(MathUtils.vector2string(MathUtils.int2bin(p1)),4,7);
		p1.value(MathUtils.bin2int(i1+i2));
	}
	public void reti(){
			//falta algo?		
			int i8a15=(iram.elementAt(sp.value())).value();
			sp.value(sp.value()-1);				
			int i0a7=(iram.elementAt(sp.value())).value();
			sp.value(sp.value()-1);
			//out("ret: i8a15:"+i8a15+" "+MathUtils.vector2string(MathUtils.int2bin(i8a15,8))+" 0 a 7:"+i0a7+" "+MathUtils.vector2string(MathUtils.int2bin(i0a7,8)));
			pc.value(MathUtils.bin2int(MathUtils.vector2string(MathUtils.int2bin(i8a15,8))+MathUtils.vector2string(MathUtils.int2bin(i0a7,8))));
		}
	public void ret(){
		int i8a15=(iram.elementAt(sp.value())).value();
		sp.value(sp.value()-1);				
		int i0a7=(iram.elementAt(sp.value())).value();
		sp.value(sp.value()-1);
		//out("ret: i8a15:"+i8a15+" "+MathUtils.vector2string(MathUtils.int2bin(i8a15,8))+" 0 a 7:"+i0a7+" "+MathUtils.vector2string(MathUtils.int2bin(i0a7,8)));
		pc.value(MathUtils.bin2int(MathUtils.vector2string(MathUtils.int2bin(i8a15,8))+MathUtils.vector2string(MathUtils.int2bin(i0a7,8))));
	}
	
	public void movx_fromX(BytePos p1,BytePos p2){
		int i=VirtualMachine.getXramAt(p2.value());
			p1.value(i);
		}
	public void movx_toX(BytePos p1,BytePos p2){
		//displayStatus("movx "+p1+" "+p2);
			VirtualMachine.setXramAt(p2.value(),p1.value());
		}
	
	public void movc(BytePos p1,BytePos p2){
		int i=VirtualMachine.getXramAt((p1.value()+p2.value()));
		p1.value(i);
	}
	
	public void jnc(BytePos p1){
			if (cy.value()==0){
				pc.value(pc.value()+p1.value());
			}
		}
		
		public void jnb(BytePos p1,BytePos p2){
				if (p1.value()==0){
					pc.value(pc.value()+p2.value());
				}
			}
		public void jb(BytePos p1,BytePos p2){
				if (p1.value()==1){
					pc.value(pc.value()+p2.value());
				}
			}		
		public void jbc(BytePos p1,BytePos p2){
			if (p1.value()==1){
				pc.value(pc.value()+p2.value());
				p1.value(0);
			}
		}					
	public void sjmp(BytePos p1){
		//p1:8 bits
		BytePos b=new BytePos(p1.value());
		b.setSigned(true);
		b.value(p1.value());
		//debug("sjmp:"+b.value());
		pc.value(pc.value()+b.value());
	}		
	public void ljmp(BytePos p1){
		//p1:16 bits
		
		pc.value(p1.value());
	}
	public void jz(BytePos p1){
		if (a.value()==0){
			BytePos b=new BytePos(p1.value());
			b.setSigned(true);
			b.value(p1.value());
			//debug("jz:"+b.value());
			pc.value(pc.value()+b.value());
		}
	}
	
	public void jnz(BytePos p1){
		if (a.value()!=0){
			BytePos b=new BytePos(p1.value());
			b.setSigned(true);
			b.value(p1.value());
			//debug("jnz:"+b.value());
			pc.value(pc.value()+b.value());			
			
		}
	}
	
	public void jmp(){
		pc.value(a.value()+dptr.value());
	}
	
	public void jc(BytePos p1){
		if (cy.value()==1){
			BytePos b=new BytePos(p1.value());
			b.setSigned(true);
			b.value(p1.value());
			//debug("jc:"+b.value());
			pc.value(pc.value()+b.value());
		}
	}
	
	public void push(BytePos p1){
		sp.value(sp.value()+1);
		int valor=(iram.elementAt(p1.value())).value();
		(iram.elementAt(sp.value())).value(valor);
		//debug("push: SP:"+sp+" valor:"+valor );
		
	}
	
	public void pop(BytePos p1){
		 
		int valor=(iram.elementAt(sp.value())).value();
		
		
		(iram.elementAt(p1.value())).value(valor);
		
		//debug("pop: SP:"+sp+" valor:"+valor+" a:"+a+"=>"+(iram.elementAt(p1.value())).value() );
		
		sp.value(sp.value()-1);
		
	}
	public void cjne(BytePos p1,BytePos p2,BytePos p3){
		if (p1.value()!=p2.value()) pc.value(pc.value()+p3.value());
		if (p1.value()<p2.value()) {
			cy.value(1);
		} else {
			cy.value(0);
		}
	}
	public void ajmp(int end){
		int end0a10=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(end,11)),0,11));
		pc.value(end0a10);
		
	}
	public void acall(int end){
		//To-do
		pc.value(pc.value()+2);
		int bin0a7=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(pc)),0,7));
		int bin8a15=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(pc)),8,15));
		int end0a10=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(end,11)),0,11));
		
		sp.value(sp.value()+1);
		(iram.elementAt(sp.value())).value(bin0a7);
		//debug("sp:"+sp+" valor:"+bin0a7);
		sp.value(sp.value()+1);
		(iram.elementAt(sp.value())).value(bin8a15);
		//debug("sp:"+sp+" valor:"+bin8a15);
		
		pc.value(end);
		
		
		
	}
	
	public void lcall(BytePos p1){
			//To-do
			pc.value(pc.value()+2);
			int bin0a7=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(pc)),0,7));
			int bin8a15=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(pc)),8,15));
			//int end0a10=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(end,11)),0,11));
		
			sp.value(sp.value()+1);
			(iram.elementAt(sp.value())).value(bin0a7);
			//debug("sp:"+sp+" valor:"+bin0a7);
			sp.value(sp.value()+1);
			(iram.elementAt(sp.value())).value(bin8a15);
			//debug("sp:"+sp+" valor:"+bin8a15);
		
			pc.value(p1.value());
		
		
		
		}
	
	public void div(BytePos p1,BytePos p2){
		if (p2.value()==0){
		  p1.value(0);	
		  p2.value(0);
		  ov.value(1);
		} else {
		  p1.value((int)(p1.value()/p2.value()));
		  p2.value((int)(p1.value()%p2.value()));
		  ov.value(0);
		}
		cy.value(0);
	}
	

	public void djnz(BytePos p1,int rel){
	  p1.value(p1.value()-1);
	  BytePos b=new BytePos(rel);
	  b.setSigned(true);
	  b.value(rel);
	  if (p1.value()!=0){
		  //debug("djnz:"+b.value());
		  pc.value(pc.value()+b.value());
	  }
	}
	public void add(BytePos p1,BytePos p2){
		p1.value(p1.value()+p2.value());
		String bin1=MathUtils.vector2string(MathUtils.int2bin(p1));
		String bin2=MathUtils.vector2string(MathUtils.int2bin(p2));
		String s1=subBin(bin1,0,3);
		String s2=subBin(bin2,0,3);
		int i1=MathUtils.bin2int(s1);
		int i2=MathUtils.bin2int(s2);
		i1=i1+i2;
		if (i1>15) {ac.value(1);} else {ac.value(0);}
		int resto=i1-15;
		if (resto<0) resto=0;
		
		s1=subBin(bin1,4,7);
		s2=subBin(bin2,4,7);
		i1=MathUtils.bin2int(s1);
		i2=MathUtils.bin2int(s2);
		i1=i1+resto+i2;
		if (i1>15) {cy.value(1);} else {cy.value(0);}
		if ((p1.value()+p2.value())>255) {ov.value(1);} else {
			if (((p1.value()+p2.value())>127)&&((p1.value()<128)&&(p2.value()<128))) 
			{ov.value(1);} else {
			ov.value(0);
			}
		}
		
	}
	
	public void addc(BytePos p1,BytePos p2){
		p1.value(p1.value()+cy.value()+p2.value());
		
		String bin1=MathUtils.vector2string(MathUtils.int2bin(p1));
		String bin2=MathUtils.vector2string(MathUtils.int2bin(p2));
		String s1=subBin(bin1,0,3);
		String s2=subBin(bin2,0,3);
		int i1=MathUtils.bin2int(s1);
		int i2=MathUtils.bin2int(s2);
		i1=i1+cy.value()+i2;
		if (i1>15) {ac.value(1);} else {ac.value(0);}
		int resto=i1-15;
		if (resto<0) resto=0;
		
		s1=subBin(bin1,4,7);
		s2=subBin(bin2,4,7);
		i1=MathUtils.bin2int(s1);
		i2=MathUtils.bin2int(s2);
		i1=i1+cy.value()+resto+i2;
		if (i1>15) {cy.value(1);} else {cy.value(0);}
		if ((p1.value()+cy.value()+p2.value())>255) {ov.value(1);} else {
			if (((p1.value()+cy.value()+p2.value())>127)&&((p1.value()<128)&&(p2.value()<128))) 
			{ov.value(1);} else {
			ov.value(0);
			}
		}
		
	}
	
	public void da(BytePos p1){
		Vector bin=MathUtils.int2bin(p1.value(),8);
		String bin0a3=subBin(MathUtils.vector2string(bin),0,3);
		BytePos i0a3=new BytePos(MathUtils.bin2int(bin0a3));
		i0a3.setBits(4);
		String bin4a7=subBin(MathUtils.vector2string(bin),4,7);
		BytePos i4a7=new BytePos(MathUtils.bin2int(bin4a7));
		i4a7.setBits(4);
		
		
		//out("t:"+MathUtils.vector2string(bin)+" 4a7:"+bin4a7+" 0-3:"+bin0a3);
		if ((MathUtils.bin2int(bin0a3)>9) || (ac.value()==1)){
			if (i0a3.value()+6>15) cy.value(1);
			i0a3.value(i0a3.value()+6);
		}
		if ((MathUtils.bin2int(bin4a7)>9) || (cy.value()==1)) {
			if (i4a7.value()+6>15) cy.value(1);
			i4a7.value(i4a7.value()+6);
		}
		
		bin0a3=MathUtils.vector2string(MathUtils.int2bin(i0a3));
		bin4a7=MathUtils.vector2string(MathUtils.int2bin(i4a7));
		
		int res=MathUtils.bin2int(bin4a7+"0000")+i0a3.value();
		
		//if (p1.value()>99) {cy.value(1);} else  {cy.value(0);}
		
		
		//out("t:"+MathUtils.vector2string(bin)+" 4a7:"+bin4a7+" 0-3:"+bin0a3+" res:"+res+" "+MathUtils.vector2string(MathUtils.int2bin(res,8)));
		p1.value(res);
	}


public void nop() {

}

public void inc(BytePos p1){
	p1.value(p1.value()+1);
}

public void dec(BytePos p1){
	p1.value(p1.value()-1);
}

public void mul_AB(){
	int p1=a.value();
	a.value(a.value()*b.value());
	b.value(p1*b.value()/256);
	
}
public void xch(BytePos p1, BytePos p2){
	int v1=p1.value();
	p1.value(p2.value());
	p2.value(v1);
}


public void rl(BytePos p1){
	Vector bin=MathUtils.int2bin(p1.value(),8);
	Integer d=(Integer)bin.elementAt(0);
	bin.add(d);
	bin.removeElementAt(0);
	p1.value(MathUtils.bin2int(MathUtils.vector2string(bin)));
}

public void rlc(BytePos p1){
	String bin=p1.getBinary()+cy.value();

	if (bin.charAt(0)=='0') {
		cy.value(0);
	} else cy.value(1);
	bin=subBin(bin,0,7);
	p1.value(MathUtils.bin2int(bin));
}

public void rr(BytePos p1){
	Vector bin=MathUtils.int2bin(p1.value(),8);
	Integer d=(Integer)bin.elementAt(bin.size()-1);
	bin.add(0,d);
	bin.removeElementAt(bin.size()-1);
	p1.value(MathUtils.bin2int(MathUtils.vector2string(bin)));
}

public void rrc(BytePos p1){
	String bin=cy.value()+p1.getBinary();
	
	if (bin.charAt(8)=='0') {
		cy.value(0);
	} else cy.value(1);
	bin=subBin(bin,1,8);
	p1.value(MathUtils.bin2int(bin));
	/*	
	Vector bin=MathUtils.int2bin(p1.value(),8);
	Integer d=(Integer)bin.elementAt(bin.size()-1);
	
	cy.value(d.intValue());
	d=new Integer(cy.value());
	bin.add(0,d);
	bin.removeElementAt(bin.size()-1);
	p1.value(MathUtils.bin2int(MathUtils.vector2string(bin)));*/
}


public void clr_A(){
	a.value(0);
}
public void clr(BytePos p1){
	p1.value(0);
}



public void mov(BytePos p1, BytePos p2){
	p1.value(p2.value());
}

public void xrl(BytePos p1, BytePos p2){
	p1.value(p1.value() ^ p2.value());
}

public void anl(BytePos p1, int p2){
		if (p2==0) p1.value(0);

}
public void anl(BytePos p1, BytePos p2){
	
	if (p2.getTipo()!=tpBin) {
		String bin1=p1.getBinary();
		String bin2=p2.getBinary();
		char ch[]=bin1.toCharArray();
		for (int i=0;i<bin1.length();i++){
			if (bin2.charAt(i)=='0') ch[i]='0';
		}
		bin1=String.valueOf(ch);
		p1.setBinary(bin1);
		//if (p1.value()!=p2.value()) p1.value(0);
	} else {
		//?
		if (p2.value()==0) p1.value(0);
	}

}
public void setb(BytePos p1){
	p1.value(1);
}
public void orl(BytePos p1, int p2){
	Vector bin1=MathUtils.int2bin(p1);
	Vector bin2=MathUtils.int2bin(p2,p1.getBits());
	for (int i=0;i<bin1.size();i++){
		Integer i2= (Integer)bin2.elementAt(i);
		if (i2.intValue()==1) bin1.setElementAt(new Integer(1),i);
	}
	
	p1.value(MathUtils.bin2int(MathUtils.vector2string(bin1)));
	
	

}
public void orl(BytePos p1, BytePos p2){
	Vector bin1=MathUtils.int2bin(p1);
	//out("p2:"+p2.value()+" "+p2.tipo+" "+p2.pos);
	Vector bin2=MathUtils.int2bin(p2);
	for (int i=0;i<bin1.size();i++){
		Integer i2= (Integer)bin2.elementAt(i);
		if (i2.intValue()==1) bin1.setElementAt(new Integer(1),i);
	}
	p1.value(MathUtils.bin2int(MathUtils.vector2string(bin1)));
	
	

}
public void cpl_C(){
  if (cy.value()==1) {cy.value(0); } else {cy.value(1);}
}

public void cpl(BytePos p1){
	Vector bin1=MathUtils.int2bin(p1.value(),8);
	for (int i=0;i<bin1.size();i++){
		Integer i1= (Integer)bin1.elementAt(i);
		if (i1.intValue()==1) {bin1.setElementAt(new Integer(0),i); }
		else {
		bin1.setElementAt(new Integer(1),i);}
	}
	//out("p1:"+p1.value()+" "+MathUtils.vector2string(bin1));
	p1.setBinary(MathUtils.vector2string(bin1));
	
}

public void displayStatus(String s){
	out(s+"=>  A:"+a+" B:"+b+" R0:"+r0+" R1:"+r1+" R2:"+r2+" R3:"+r3+" R4:"+r4+" R5:"+r5+" R6:"+r6+" R7:"+r7+" CY:"+cy+" AC:"+ac+" OV:"+ov+" PSW:"+psw+" P0:"+p0+" P1:"+p1+" P2:"+p2+" P3:"+p3+" sp:"+sp);	
}

}
