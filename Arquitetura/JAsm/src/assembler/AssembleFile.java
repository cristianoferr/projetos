package assembler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import comum.BytePos;
import comum.MathUtils;
import comum.Objeto;


/*
 * Created on 27/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrador
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AssembleFile extends Objeto {
	public static Connection conn;
   private static int Error=0;
	private static Vector variables=new Vector();
c	private static Vector labels=new Vector();  
	private static Vector labelsCalls=new Vector();
	public static Vector mnemonicoDB=new Vector();
	public static String url = "jdbc:odbc:jasm";
	
	private static int totSysVars=0;
	public static int codeSize=0;
	
	/**
	 * 
	 */
	public AssembleFile() {
		super();
		
		// TODO Auto-generated constructor stub
	}
	
	public static void loadFile(String arq,int posIni){
		
		
		out("Loading File:"+arq);
		Error=0;
		FileReader theFile;
		BufferedReader fileIn = null;
		String oneLine;
		Vector v=new Vector();
		Vector asmCode=new Vector();
		boolean endFound=false;
		// READ FROM FILENAME UNTIL END OF FILE
		try 
		{
			theFile = new FileReader( arq );
			fileIn  = new BufferedReader( theFile );
			while( ( oneLine = fileIn.readLine( ) ) != null ){
				oneLine=trimCode(oneLine);
			  //out("Lido:"+oneLine);
			/*  StringTokenizer st = new StringTokenizer(oneLine," ,",false);
			  v=new Vector();
			  while (st.hasMoreElements()){
			  	String s=st.nextToken();
			  	if (s!="")
				v.add(s);
					
				
				}*/
			  v=tokenizeCode(oneLine);
			  
			  if ((!endFound) && (v.size()>0)){
			  if (((String)v.elementAt(0)).equals("END")) {
			  	endFound=true;
			  } else 
			  asmCode.add(v);
			  
			  }
			}
		}
		
		catch( Exception e ) { e.printStackTrace(); }
		
		try
		{
			if( fileIn != null )
				fileIn.close( );
		}
		catch( IOException e ) {e.printStackTrace(); }
		
		
		//out("Linhas:"+asmCode.size());
		
		//out("Linhas:"+code.size());
		
		montaAsm(asmCode,posIni);
		replaceLabels();
		displayCode();
		//out("Linhas:"+code.size());
		
	}

	public static void inicializaVariaveis() {
		
		variables.clear();
		
		mnemonicoDB.clear();
		startDB();
		addVariable("A",MathUtils.int2hex(224),tpDec,0);
		
		
		addVariable("R0",MathUtils.int2hex(0),tpDec,0);
		addVariable("R1",MathUtils.int2hex(1),tpDec,0);
		addVariable("R2",MathUtils.int2hex(2),tpDec,0);
		addVariable("R3",MathUtils.int2hex(3),tpDec,0);
		addVariable("R4",MathUtils.int2hex(4),tpDec,0);
		addVariable("R5",MathUtils.int2hex(5),tpDec,0);
		addVariable("R6",MathUtils.int2hex(6),tpDec,0);
		addVariable("R7",MathUtils.int2hex(7),tpDec,0);
		
		totSysVars=variables.size();
		
		addVariable("B",MathUtils.int2hex(240),tpDec,0);
		
		addVariable("P0","80",tpDec,0);
		addVariable("P0.0","80",tpBin,0);
		addVariable("P0.1","81",tpBin,1);
		addVariable("P0.2","82",tpBin,2);
		addVariable("P0.3","83",tpBin,3);
		addVariable("P0.4","84",tpBin,4);
		addVariable("P0.5","85",tpBin,5);
		addVariable("P0.6","86",tpBin,6);
		addVariable("P0.7","87",tpBin,7);

		addVariable("P1","90",tpDec,0);
		addVariable("P1.0","90",tpBin,0);
		addVariable("P1.1","91",tpBin,1);
		addVariable("P1.2","92",tpBin,2);
		addVariable("P1.3","93",tpBin,3);
		addVariable("P1.4","94",tpBin,4);
		addVariable("P1.5","95",tpBin,5);
		addVariable("P1.6","96",tpBin,6);
		addVariable("P1.7","97",tpBin,7);

		addVariable("P2","A0",tpDec,0);
		addVariable("P2.0","A0",tpBin,0);
		addVariable("P2.1","A1",tpBin,1);
		addVariable("P2.2","A2",tpBin,2);
		addVariable("P2.3","A3",tpBin,3);
		addVariable("P2.4","A4",tpBin,4);
		addVariable("P2.5","A5",tpBin,5);
		addVariable("P2.6","A6",tpBin,6);
		addVariable("P2.7","A7",tpBin,7);

		addVariable("P3","B0",tpDec,0);
		addVariable("P3.0","B0",tpBin,0);
		addVariable("P3.1","B1",tpBin,1);
		addVariable("P3.2","B2",tpBin,2);
		addVariable("P3.3","B3",tpBin,3);
		addVariable("P3.4","B4",tpBin,4);
		addVariable("P3.5","B5",tpBin,5);
		addVariable("P3.6","B6",tpBin,6);
		addVariable("P3.7","B7",tpBin,7);
		
		
		addVariable("ACC",MathUtils.int2hex(224),tpDec,0);
		addVariable("TMOD","89",tpDec,0);
		
		
		addVariable("TH0","8C",tpDec,0);
		addVariable("TL0","8A",tpDec,0);
		addVariable("TH1","8D",tpDec,0);
		addVariable("TL1","8B",tpDec,0);		
		addVariable("TCON","88",tpDec,0);
		addVariable("TR0","88",tpBin,4);
		addVariable("TF0","88",tpBin,5);
	}

	private static void addVariable(String p1,String p2,int p3,int bit) {
		Vector v=new Vector();
		//debug("Acrescentando variavel:"+p1+","+p2+","+p3+","+bit);
		//pos 0=nome 1=HEX 2=tipo 3=bit
		v.add(p1.toUpperCase());
		v.add(p2.toUpperCase());
		v.add(new Integer(p3));
		v.add(new Integer(bit));
		variables.add(v);
	}
	
	private static int isVariable(String p1){
		for (int i=totSysVars;i<variables.size();i++){
			Vector v=(Vector)variables.elementAt(i);
			String nome=(String)v.elementAt(0);
			int tipo=((Integer)v.elementAt(2)).intValue();
			//debug(p1+" "+nome+" "+nome.equals(p1));
			if (nome.equals(p1)) return tipo;
		}
		return -1;
	}
	private static String getHexFromVariable(String p1){
			for (int i=0;i<variables.size();i++){
				Vector v=(Vector)variables.elementAt(i);
				String nome=(String)v.elementAt(0);
				String hex=(String)v.elementAt(1);
				if (nome.equals(p1)) return hex;
			}
			if (isNumber(p1)) {
				return MathUtils.int2hex(Integer.parseInt(p1));
			}
			if (isChar(p1)!=p1) {
				
				return MathUtils.int2hex(Integer.parseInt(isChar(p1)));
				}
		if (MathUtils.isHex(p1)!=p1) {
				
						return MathUtils.int2hex(Integer.parseInt(MathUtils.isHex(p1)));
						}				
			 return p1; 
		}
	private static int getTipoVariable(String p1){
				for (int i=0;i<variables.size();i++){
					Vector v=(Vector)variables.elementAt(i);
					String nome=(String)v.elementAt(0);
					int tipo=((Integer)v.elementAt(2)).intValue();
					if (nome.equals(p1)) return tipo;
				}
				return -1;
			}		
	private static Vector getVariable(String p1){
			for (int i=0;i<variables.size();i++){
				Vector v=(Vector)variables.elementAt(i);
				String nome=(String)v.elementAt(0);
				if (nome.equals(p1)) return v;
			}
			return null;
		}

	private static void displayCode() {
	/*	String sh="",s="";
		int c=0;
		for (int i=0;i<codeSize;i++){
			sh=sh+MathUtils.int2hex(c);
			s=s+VirtualMachine.getXramAt(i);
			c++;
			
		}
		//out(" 0 1 2 3 4 5 6 7 8 9 A B C");
		out(sh);
		out(s);*/
	}
	
	public static int getBytesDB(String hexcode){
		for (int i=0;i<mnemonicoDB.size();i++){
			Vector mnemonico=(Vector)mnemonicoDB.elementAt(i);
			if (((String)mnemonico.elementAt(0)).equals(hexcode)) return Integer.parseInt((String)mnemonico.elementAt(5));
	
		}
		return 1;
	}
	public static String getComandoDB(String hexcode){
		for (int i=0;i<mnemonicoDB.size();i++){
			Vector mnemonico=(Vector)mnemonicoDB.elementAt(i);
			if (((String)mnemonico.elementAt(0)).equals(hexcode)) return (String)mnemonico.elementAt(1)+" "+mnemonico.elementAt(2)+" "+mnemonico.elementAt(3)+" "+mnemonico.elementAt(4)+" ";
	
		}
		return "";
	}			
	public static String getMnemonicoDB(String hexcode){
			for (int i=0;i<mnemonicoDB.size();i++){
				Vector mnemonico=(Vector)mnemonicoDB.elementAt(i);
				if (((String)mnemonico.elementAt(0)).equals(hexcode)) return (String)mnemonico.elementAt(1);
	
			}
			return "";
		}	
		public static Vector getAsmDB(String hexcode){
			for (int i=0;i<mnemonicoDB.size();i++){
				Vector mnemonico=(Vector)mnemonicoDB.elementAt(i);
				if (((String)mnemonico.elementAt(0)).equals(hexcode)) return mnemonico;

			}
			return null;
		}			

	private static void montaAsm(Vector asmCode,int posIni) {
				Vector v;
				try {
					
						Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
						conn = DriverManager.getConnection(url,"","");
						
				}		
				catch (Exception e) {  
					System.err.println("Erro de SQL: ");  
					System.err.println(e.getMessage());  
					Error=2;
				}  
				boolean add2code=true;
				
				//1a Passada
				for (int i=0;i<asmCode.size();i++){
					v=(Vector)asmCode.elementAt(i);
				  for (int j=0;j<v.size();j++){
					  String par=(String)v.elementAt(j);
					 if (par.length()>0)
					if ((par.charAt(0)==';')){
						int x=j;
						while (x<v.size()) {
							v.removeElementAt(x);
						}
						asmCode.setElementAt(v,i);
						
					}
								  
					  if ((par.charAt(par.length()-1)==':') &&(j==0)){
						  
						  par=par.replaceFirst(":","");
						  addLabel(par);
						  if (v.size()==1) v.add("NOP");
						  v.add(par+":");
						  v.removeElementAt(0);
						  asmCode.setElementAt(v,i);
						  
					  }
					  
				  }					
				}
				//2a Passada  (mais para limpar)
				int g=0;
				while (g<asmCode.size()){
					if (((Vector)asmCode.elementAt(g)).size()==0) {
						asmCode.removeElementAt(g);
					} else g++;
				}

				
				
				//3a Passada
				for (int i=0;i<asmCode.size();i++){
					add2code=true;
					
				v=(Vector)asmCode.elementAt(i);
				//debug("v:"+v);
				String mnemonico=(String)v.elementAt(0);
				
				
				
				String hex_code="",p1="",p2="",p3="";
				int ap1=0,ap2=0,ap3=0;
				int bytes=0;
				String sql="select hex_code,mnemonico,par1,par2,par3,bytes from Codigos8051 \n";
				sql=sql+" where mnemonico='"+mnemonico+"' ";
				if (mnemonico.charAt(0)==';') {
					//debug("v:"+v);
					add2code=false;
				}
				
				
				
				
				
				if (add2code)
				for (int j=1;j<v.size();j++){
					String par=(String)v.elementAt(j);
					
					if (par.charAt(par.length()-1)==':') {
						String saux=par.replaceFirst(":","");
						setLabelAddress(saux,codeSize-bytes);
						
					}
					if ((par.equals("R0")) || 
					(par.equals("R1")) ||
					(par.equals("R2")) ||
					(par.equals("R3")) ||
					(par.equals("R4")) ||
					(par.equals("R5")) ||
					(par.equals("R6")) ||
					(par.equals("R7")) ||
					(par.equals("@R0")) ||
					(par.equals("@R1")) ||
					(par.equals("@A")) ||
					(par.equals("@DPTR")) ||
					(par.equals("DPTR")) ||
					(par.equals("@A+DPTR")) ||
					(par.equals("@A+PC")) ||
					(par.equals("A")) ||
					
					(par.equals("C")) )
					  sql=sql+"\n and par"+(j)+"='"+par+"'";
					
					if ((j==1) && (par.equals("EQU"))) {
						add2code=false;
						
						String val=(String)v.elementAt(2);
						Vector variavel=getVariable(val);
						if (variavel!=null) {
							String hex=(String)variavel.elementAt(1);
							int tipo=((Integer)variavel.elementAt(2)).intValue();
							int bit=((Integer)variavel.elementAt(3)).intValue();
							
							
							addVariable(mnemonico,hex,tipo,bit);
						} else{
							debug("checar variavel:"+v);
							String hex=MathUtils.int2hex(((Integer)v.elementAt(2)).intValue());
							addVariable(mnemonico,hex,tpDec,0);
						}
						
//						pos 0=nome 1=HEX 2=tipo 3=bit
						//addVariable("P3","B0",tpDec,0);
						//		addVariable("P3.0","B0",tpBin,0);
						} 
					 
					
					boolean negat=false;
					if (par.charAt(0)=='\\'){ 
						negat=true; 
						//out("par:"+par);
						par=par.replaceFirst("\\","");  
					}
					
					if (isVariable(par)>-1){
						int tipo=isVariable(par);
						int tam=1;
						if (tipo==tpBin) {
							if (negat){	sql=sql+"\n and par"+(j)+"='\\BIT'"; }
							else
							{	sql=sql+"\n and (par"+(j)+"='BIT' or par"+(j)+"='DIRETO')"; }
						}
						if (tipo==tpDec) {
							sql=sql+"\n and par"+(j)+"='DIRETO'";
						}
						if (tipo==tpLabel) {
								sql=sql+"\n and (par"+(j)+"='END16' or par"+(j)+"='END11' or  par"+(j)+"='REL' or  par"+(j)+"='DIRETO')";
								
							}
						if (j==1) ap1=tam;
						if (j==2) ap2=tam;
						if (j==3) ap3=tam;
					}
					if (par.charAt(0)=='#') {
						
						if (par.charAt(1)!='\\') {
							String parl=par.replaceFirst("#","");
							v.setElementAt(new String(parl),j);
						  sql=sql+"\n and par"+(j)+" like '#%'";
						  if (j==1) ap1=1;
						  if (j==2) ap2=1;
						  if (j==3) ap3=1;
						}
					  }
					if (isNumber(par)) {
						//Verificar se é 8bits ou 16 bits
						int val=Integer.parseInt(par);
						if (val<256){
							sql=sql+"\n and (par"+(j)+"='REL' or par"+(j)+"='DIRETO' or par"+(j)+"='#16') ";
							if (j==1) ap1=1;
							if (j==2) ap2=1;
							if (j==3) ap3=1;
						} else {
							sql=sql+"\n and par"+(j)+"='#16'";
							if (j==1) ap1=2;
							if (j==2) ap2=2;
							if (j==3) ap3=2;
						}
					}
					
				}
				//out("Sql:"+sql);
				try	{
				
					PreparedStatement ps=conn.prepareStatement(sql);
					ResultSet rs=ps.executeQuery();
					
					boolean found=false;
					if (add2code)
					if (rs.next()){
						found=true;
						
						hex_code=rs.getString("hex_code");
						
						bytes=rs.getInt("bytes");
						p1=rs.getString("par1");
						if (p1==null)p1="";
						p1=p1.toUpperCase();
						
						p2=rs.getString("par2");
						if (p2==null)p2="";
						p2=p2.toUpperCase();
						
						p3=rs.getString("par3");
						if (p3==null)p3="";
						p3=p3.toUpperCase();
						
						//out("Mnemonico:"+mnemonico+" bytes:"+bytes+" p1:"+p1+" p2:"+p2+" p3:"+p3+" hex:"+hex_code+" "+v);
						addCode(add2code,
							v,
							mnemonico,
							hex_code,
							p1,
							p2,
							p3,
							ap1,
							ap2,
							ap3,posIni);
						
	
						
					}
					if ((rs.next()) && (add2code))	{
						out("Erro Fatal:Mais de um Codigo encontrado! "+v+"\n"+sql);
						Error=3;
					}					
					if ((!found) && (add2code))	{
						out("Erro Fatal:Codigo não encontrado! "+v+"\n"+sql);
						Error=1;
						} 
					}
				
				catch (Exception e) {  
					System.err.println("Erro de SQL: "+v+" \n");  
					System.err.println(e.getMessage()+"\n"+sql);
					e.printStackTrace();  
					Error=2;
				}  
		
	}
	}
	private static void addLabel(String par){
		//debug("Label:"+par);
		labels.add(par);
		addVariable(par,(labels.size()-1)+"",tpLabel,0);
		
	}
	private static void setLabelAddress(String label,int pos){
		
		for (int i=0;i<variables.size();i++){
			Vector var=(Vector)variables.elementAt(i);
			String p1=(String)var.elementAt(0);
			String ender=(String)var.elementAt(1);
			int tipo=((Integer)var.elementAt(2)).intValue();
			
			if ((tipo==tpLabel) && (p1.equals(label))){
				var.setElementAt(MathUtils.int2hex(pos),1);
				//debug("setLabel:"+var);
				variables.setElementAt(var,i);
				
			}
			
		}
	}
	private static int getAddress(int mne_code,int x,int posCode) {
		//out("mne_code:"+MathUtils.int2hex(mne_code)+" x:"+x+" pos:"+posCode+" x+p:"+(x+posCode));
		if ((mne_code==0xD5)){
			x=x-posCode-1;
			//out("mne_code:"+MathUtils.int2hex(mne_code)+" x:"+x+" pos:"+posCode+" i:"+i);
		}
		if ((mne_code==0x80) || (mne_code==0x70) || (mne_code==0x60)
		|| (mne_code==0x50) || (mne_code==0x40)
		|| (mne_code==0xDC) || (mne_code==0xD8)
		|| (mne_code==0xD9) || (mne_code==0xDA)
		|| (mne_code==0xDD) || (mne_code==0xDE)
		|| (mne_code==0xDF) || (mne_code==0xDB)		
		) {
			x=x-posCode-1;
			//out("mne_code:"+MathUtils.int2hex(mne_code)+" x:"+x+" pos:"+posCode+" i:"+i);
		}
		
		return x;
	}
	private static void replaceLabels(){
		//debug("replacelabels.start()");
		for (int i=0;i<labelsCalls.size();i++) {
			Vector lab=(Vector)labelsCalls.elementAt(i);
			String label=(String)lab.elementAt(0);
			int posCode=((Integer)lab.elementAt(1)).intValue();
			int byteSize=((Integer)lab.elementAt(2)).intValue();
			String hexValue=getHexFromVariable(label);
			if (byteSize==1){
				int mne_code=VirtualMachine.getXramAt(posCode);
				BytePos x=new BytePos(0);
				x.setShowIn(tpHex);
				
				x.value(MathUtils.hex2Byte(hexValue));
				int xh=x.value();
				x.value(getAddress(mne_code,x.value(),posCode));
				VirtualMachine.setXramAt(posCode,x.value());
			//	if (MathUtils.int2hex(mne_code).equals("60"))debug("posCode:"+posCode+" mne_code:"+MathUtils.int2hex(mne_code)+" xh:"+MathUtils.int2hex(xh)+" x:"+x+" value:"+((BytePos)code.elementAt(posCode))+" tipo:"+((BytePos)code.elementAt(posCode)).getTipo()+" bits:"+((BytePos)code.elementAt(posCode)).getBits());
			}
			if (byteSize==2){
				int mne_code=VirtualMachine.getXramAt(posCode);
				BytePos x=new BytePos(0);
				x.setBits(16);
				x.setShowIn(tpHex);
				x.value(MathUtils.hex2Byte(hexValue));
				x.value(getAddress(mne_code,x.value(),posCode));
				int x0a7=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(x)),0,7));
				int x8a15=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(x)),8,15));
				VirtualMachine.setXramAt(posCode+1,x0a7);
				VirtualMachine.setXramAt(posCode,x8a15);
				//debug("posCode:"+posCode+" mne_code:"+MathUtils.int2hex(mne_code)+" x:"+x+" value:"+((BytePos)code.elementAt(posCode))+" tipo:"+((BytePos)code.elementAt(posCode)).getTipo()+" bits:"+((BytePos)code.elementAt(posCode)).getBits());
				
			}  
			 
		}
	}

	private static void addCode(boolean add2code,
		Vector v,
		String mnemonico,
		String hex_code,
		String p1,
		String p2,
		String p3,
		int ap1,
		int ap2,
		int ap3,int posIni) {
		if (add2code){
		
			VirtualMachine.setXramAt(posIni+codeSize,MathUtils.hex2Byte(hex_code));
			codeSize++;
			if (hex_code.equals("85")) { 
				addValor(hex_code, v, 2, p2, ap2,posIni);
				addValor(hex_code, v, 1, p1, ap1,posIni);
				addValor(hex_code, v, 3, p3, ap3,posIni);
			} else {
				addValor(hex_code,v, 1, p1, ap1,posIni);
				addValor(hex_code, v, 2, p2, ap2,posIni);
				addValor(hex_code, v, 3, p3, ap3,posIni);
			}
		}
		
		
		
	}

	private static void addValor(String hex_code, Vector v, int p,String p1, int ap1,int posIni) {
		boolean ok=true;
		if (ap1>0){
			//debug("p:"+p+" par:"+p1+" size:"+ap1+" valor:"+v.elementAt(p)+"=>"+getHexFromVariable((String)v.elementAt(p)));
			if (((p1.equals("END11")) ||(p1.equals("END16")) ||(p1.equals("#16")) || (ap1==2))&& ((String)v.elementAt(p)!=null)){
				int x=MathUtils.hex2Byte(getHexFromVariable((String)v.elementAt(p)));
				int x0a7=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(x,16)),0,7));
				int x8a15=MathUtils.bin2int(subBin(MathUtils.vector2string(MathUtils.int2bin(x,16)),8,15));
				if (getTipoVariable((String)v.elementAt(p))==tpLabel) {
					Vector lab=new Vector();
					lab.add(v.elementAt(p));
					
					lab.add(new Integer(codeSize));
					lab.add(new Integer(2));
					labelsCalls.add(lab);
					
					debug("v:"+v+" "+MathUtils.hex2Byte(getHexFromVariable((String)v.elementAt(p)))+" "+hex_code);
	
					x0a7=MathUtils.hex2Byte(hex_code);
					x8a15=MathUtils.hex2Byte(hex_code);
					ok=false;					
				}
				VirtualMachine.setXramAt(posIni+codeSize,x0a7);
				codeSize++;
				VirtualMachine.setXramAt(posIni+codeSize,x8a15);
				codeSize++;
				
			} else {
				Vector vv=getVariable((String)v.elementAt(p-1));
				if (getTipoVariable((String)v.elementAt(p))==tpLabel) {
						Vector lab=new Vector();
						lab.add(v.elementAt(p));
						lab.add(new Integer(codeSize));
						lab.add(new Integer(1));
						//debug("v:"+v+" "+MathUtils.hex2Byte(getHexFromVariable((String)v.elementAt(p)))+" "+hex_code);
						
						VirtualMachine.setXramAt(posIni+codeSize,MathUtils.hex2Byte(hex_code));
					codeSize++;
						ok=false;
						labelsCalls.add(lab);
					}				
				
				if (vv!=null) {
				//pos 0=nome 1=HEX 2=tipo 3=pos
				int tipo=((Integer)vv.elementAt(2)).intValue();
				int pos=((Integer)vv.elementAt(3)).intValue();
				int decr=0;
				if (tipo==tpBin) {
					ok=false;
					decr=(pos+1)*3;
					VirtualMachine.setXramAt(posIni+codeSize,MathUtils.hex2Byte(getHexFromVariable((String)v.elementAt(p)))-decr);
					codeSize++;
										
				}
				}

				if (ok)	{
					//debug("=>"+(String)v.elementAt(p)+" "+getHexFromVariable((String)v.elementAt(p))+" "+MathUtils.hex2Byte(getHexFromVariable((String)v.elementAt(p)))+"");
					VirtualMachine.setXramAt(posIni+codeSize,MathUtils.hex2Byte(getHexFromVariable((String)v.elementAt(p))));
					codeSize++;
					}
					
					 
			}
		}
	}

public static void startDB(){
	/*
	
	try {
					
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection(url,"","");
				
		String sql="select * from Codigos8051 order by hex_code\n";
		//sql=sql+" where hex_code='"+hexcode+"'";
		PreparedStatement ps=conn.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		int i=0;
		while (rs.next()) {
			bytes=rs.getInt("bytes");
			String hex_code=rs.getString("hex_code");
			String mnemonico=rs.getString("mnemonico");
						
			String p1=rs.getString("par1");
			if (p1==null)p1="";
			p1=p1.toUpperCase();

			String p2=rs.getString("par2");
			if (p2==null)p2="";
			p2=p2.toUpperCase();

			String p3=rs.getString("par3");
			if (p3==null)p3="";
			p3=p3.toUpperCase();				

			out("mnemonico=new Vector();");
			out("mnemonico.add(new String(\""+hex_code+"\"));");
			out("mnemonico.add(new String(\""+mnemonico+"\"));");
			out("mnemonico.add(new String(\""+p1+"\"));");
			out("mnemonico.add(new String(\""+p2+"\"));");
			out("mnemonico.add(new String(\""+p3+"\"));");
			out("mnemonico.add(new String(\""+bytes+"\"));");	
			out("mnemonicoDB.add(mnemonico);");			
		} 				
						
	}		
	catch (Exception e) {  
		System.err.println("Erro de SQL: ");  
		System.err.println(e.getMessage());  
			
	} */
	
	Vector mnemonico;
	mnemonico=new Vector();
	
	
	
	mnemonico.add(new String("00"));
	mnemonico.add(new String("NOP"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("01"));
	mnemonico.add(new String("AJMP"));
	mnemonico.add(new String("END11"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("02"));
	mnemonico.add(new String("LJMP"));
	mnemonico.add(new String("END16"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("03"));
	mnemonico.add(new String("RR"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("04"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("05"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("06"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("07"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("08"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("09"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("0A"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("0B"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("0C"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("0D"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("0E"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("0F"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("10"));
	mnemonico.add(new String("JBC"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("11"));
	mnemonico.add(new String("ACALL"));
	mnemonico.add(new String("END11"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("12"));
	mnemonico.add(new String("LCALL"));
	mnemonico.add(new String("END16"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("13"));
	mnemonico.add(new String("RRC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("14"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("15"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("16"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("17"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("18"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("19"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("1A"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("1B"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("1C"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("1D"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("1E"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("1F"));
	mnemonico.add(new String("DEC"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("20"));
	mnemonico.add(new String("JB"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("22"));
	mnemonico.add(new String("RET"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("23"));
	mnemonico.add(new String("RL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("24"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("25"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("26"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("27"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("28"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("29"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("2A"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("2B"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("2C"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("2D"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("2E"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("2F"));
	mnemonico.add(new String("ADD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("30"));
	mnemonico.add(new String("JNB"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("32"));
	mnemonico.add(new String("RETI"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("33"));
	mnemonico.add(new String("RLC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("34"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("35"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("36"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("37"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("38"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("39"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("3A"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("3B"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("3C"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("3D"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("3E"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("3F"));
	mnemonico.add(new String("ADDC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("40"));
	mnemonico.add(new String("JC"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("42"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("43"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("44"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("45"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("46"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("47"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("48"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("49"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("4A"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("4B"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("4C"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("4D"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("4E"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("4F"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("50"));
	mnemonico.add(new String("JNC"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("52"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("53"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("54"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("55"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("56"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("57"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("58"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("59"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("5A"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("5B"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("5C"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("5D"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("5E"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("5F"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("60"));
	mnemonico.add(new String("JZ"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("62"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("63"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("64"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("65"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("66"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("67"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("68"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("69"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("6A"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("6B"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("6C"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("6D"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("6E"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("6F"));
	mnemonico.add(new String("XRL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("70"));
	mnemonico.add(new String("JNZ"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("72"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("C"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("73"));
	mnemonico.add(new String("JMP"));
	mnemonico.add(new String("@A+DPTR"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("74"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("75"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("76"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("77"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("78"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("79"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("7A"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("7B"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("7C"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("7D"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("7E"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("7F"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("80"));
	mnemonico.add(new String("SJMP"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("82"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("C"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("83"));
	mnemonico.add(new String("MOVC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@A+PC"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("84"));
	mnemonico.add(new String("DIV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("B"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("85"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("86"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("87"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("88"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("89"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("8A"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("8B"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("8C"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("8D"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("8E"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("8F"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("90"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DPTR"));
	mnemonico.add(new String("#16"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("92"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String("C"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("93"));
	mnemonico.add(new String("MOVC"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@A+DPTR"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("94"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("95"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("96"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("97"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("98"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("99"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("9A"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("9B"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("9C"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("9D"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("9E"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("9F"));
	mnemonico.add(new String("SUBB"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("A0"));
	mnemonico.add(new String("ORL"));
	mnemonico.add(new String("C"));
	mnemonico.add(new String("\\BIT"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("A2"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("C"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("A3"));
	mnemonico.add(new String("INC"));
	mnemonico.add(new String("DPTR"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("A4"));
	mnemonico.add(new String("MULAB"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("A6"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("A7"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("A8"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("A9"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("AA"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("AB"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("AC"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("AD"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("AE"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("AF"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("B0"));
	mnemonico.add(new String("ANL"));
	mnemonico.add(new String("C"));
	mnemonico.add(new String("\\BIT"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("B2"));
	mnemonico.add(new String("CPL"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("B3"));
	mnemonico.add(new String("CPL"));
	mnemonico.add(new String("C"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("B4"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("B5"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("B6"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("B7"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("B8"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("B9"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("BA"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("BB"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("BC"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("BD"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("BE"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("BF"));
	mnemonico.add(new String("CJNE"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String("#"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("C0"));
	mnemonico.add(new String("PUSH"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("C2"));
	mnemonico.add(new String("CLR"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("C3"));
	mnemonico.add(new String("CLR"));
	mnemonico.add(new String("C"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("C4"));
	mnemonico.add(new String("SWAP"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("C5"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("C6"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("C7"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("C8"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("C9"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("CA"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("CB"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("CC"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("CD"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("CE"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("CF"));
	mnemonico.add(new String("XCH"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("D0"));
	mnemonico.add(new String("POP"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("D2"));
	mnemonico.add(new String("SETB"));
	mnemonico.add(new String("BIT"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("D3"));
	mnemonico.add(new String("SETB"));
	mnemonico.add(new String("C"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("D4"));
	mnemonico.add(new String("DA"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("D5"));
	mnemonico.add(new String("DJNZ"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("3"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("D6"));
	mnemonico.add(new String("XCHD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("D7"));
	mnemonico.add(new String("XCHD"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("D8"));
	mnemonico.add(new String("DJNZ"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("D9"));
	mnemonico.add(new String("DJNZ"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("DA"));
	mnemonico.add(new String("DJNZ"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("DB"));
	mnemonico.add(new String("DJNZ"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("DC"));
	mnemonico.add(new String("DJNZ"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("DD"));
	mnemonico.add(new String("DJNZ"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("DE"));
	mnemonico.add(new String("DJNZ"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("DF"));
	mnemonico.add(new String("DJNZ"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String("REL"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("E0"));
	mnemonico.add(new String("MOVX"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@DPTR"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("E2"));
	mnemonico.add(new String("MOVX"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("E3"));
	mnemonico.add(new String("MOVX"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("E4"));
	mnemonico.add(new String("CLR"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("E5"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("E6"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("E7"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("E8"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("E9"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("EA"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("EB"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("EC"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("ED"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("EE"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("EF"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("ER"));
	mnemonico.add(new String(";"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("0"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("F0"));
	mnemonico.add(new String("MOVX"));
	mnemonico.add(new String("@DPTR"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("F2"));
	mnemonico.add(new String("MOVX"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("F3"));
	mnemonico.add(new String("MOVX"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("F4"));
	mnemonico.add(new String("CPL"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("F5"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("DIRETO"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("2"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("F6"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("@R0"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("F7"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("@R1"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("F8"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R0"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("F9"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R1"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("FA"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R2"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("FB"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R3"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("FC"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R4"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("FD"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R5"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("FE"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R6"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	mnemonico=new Vector();
	mnemonico.add(new String("FF"));
	mnemonico.add(new String("MOV"));
	mnemonico.add(new String("R7"));
	mnemonico.add(new String("A"));
	mnemonico.add(new String(""));
	mnemonico.add(new String("1"));
	mnemonicoDB.add(mnemonico);
	
	}

}
