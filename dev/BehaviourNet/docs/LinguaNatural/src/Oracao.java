package src;

import java.util.StringTokenizer;
import java.util.Vector;

public class Oracao extends ObjetoBasico {

Frase frase=new Frase();
Condicionais condicionais=new Condicionais();
Verbos verbos=new Verbos();
Vector strings=new Vector();

	public Oracao(String f){
		StringTokenizer tokens;
		
		condicionais.setVerbos(verbos);
		f=tiraAcento(f);
		f=separaStrings(f);		
		tokens = new StringTokenizer(f,", .!?:;-/|",false);
		while (tokens.hasMoreTokens()) {
			  addPalavra(tokens.nextToken());
		  }
		
		recolocaString();
	}

	public String separaStrings(String f){
		StringTokenizer tokens;
		String s="";
		tokens = new StringTokenizer(f,"\"",true);
		
		boolean status=false;
		while (tokens.hasMoreTokens()) {
			String t=tokens.nextToken();
			//out("t:"+t+" status:"+status);
			
			if ( (t.equals("\"")) ){
				status=!status;
			} else {
				
				
				if (status){
					strings.add(t);
					s=s+"string"+strings.size();
					t="";
					//status=-1;
				} else {
					s=s+t;
				}
			}
			

		}
	//	out("Saida:"+s);
		return s;
	}
	public void recolocaString(){
		for (int c=0;c<frase.size();c++){
			frase.setPos(c);
			for (int i=0;i<strings.size();i++){
			if (frase.getPalavra().getId().equals("string"+(i+1))) {
				frase.getPalavra().setId((String)strings.elementAt(i));
				frase.getPalavra().setTipoPalavra(Constantes.palString);
			}
			
			}

		}
	}	
	public void addPalavra(String p){
		frase.add(new Palavra(p));
	}

	public String tiraAcento(String f){
		StringTokenizer tokens;
		tokens = new StringTokenizer(f,"ãõçâêîôû",true);
		String s="";
		while (tokens.hasMoreTokens()) {
			String t=tokens.nextToken();
			if ((t.equals("ã")) || (t.equals("â")) || (t.equals("á")))
				t="a";
			if ( (t.equals("ê")) )
				t="e";
			if ( (t.equals("î")) )
				t="i";
			if ( (t.equals("ô")) )
				t="o";
			if ( (t.equals("û")) )
				t="u";
			if (t.equals("ç"))
				t="c";
			  s=s+t;
		  }
		return s;
	}
	
	public void parser(){
		int posVerbo=-1;

		
		// Inicial
		for (int c=0;c<frase.size();c++){
			frase.setPos(c);
			if (frase.getPalavra().getTipoPalavra()==-1) {
				condicionais.checkCondicional(frase);
			}
		}
		

		// Procurando Verbo Principal
		for (int c=0;c<frase.size();c++){
			frase.setPos(c);
			if ((frase.getPalavra().getTipoPalavra()==Constantes.palVerbo)) {
				posVerbo=c;
				frase.setVerbo(frase.getPalavra());
			}			
		}

		
		
		// Procurando sujeitos (substantivos e palavras desconhecidas.
		for (int c=0;c<posVerbo;c++){
			frase.setPos(c);
			if (frase.getPalavra().possivelSujeito()) {
				frase.getSujeito().add(frase.getPalavra());
			}
		}
	
		if (posVerbo>0) {
	//		 Procurando predicados (adjetivos, substantivos e palavras desconhecidas.
			for (int c=posVerbo;c<frase.size();c++){
				frase.setPos(c);
				if ((frase.getPalavra().getTipoPalavra()==Constantes.palAdj)||
						(frase.getPalavra().possivelSujeito())) {
					frase.getPredicado().add(frase.getPalavra());
				}
			}
		}
		
	}
	

	
	public String toString(){
		String s="";
		for (int c=0;c<frase.size();c++){
			frase.setPos(c);
			s=s+" "+frase.getPalavra();
		}
		return s;	
	}
	
	public void sumario(){
		if (frase.getGeneroSujeito()!=-1)
			out("Genero: "+Constantes.descGenero[frase.getGeneroSujeito()]);
		if (frase.getTipoPessoa()!=-1)
			out("Tipo Pessoa: "+frase.getTipoPessoa());
		if (frase.getTipoSujeito()!=-1)
			out("Tipo Sujeito: "+Constantes.descSujeito[frase.getTipoSujeito()]);
		if (frase.getTempo()!=-1)
			out("Tempo: "+Constantes.descTempo[frase.getTempo()]);

		for (int c=0;c<frase.size();c++){
			frase.setPos(c);
			String s="";
			Palavra p=frase.getPalavra();
			
			if (p.getTipoAdjetivo()!=-1)
				s=" ("+Constantes.descTipoAdjetivo[p.getTipoAdjetivo()]+")";

			
			if (p.getTipoPalavra()!=-1)
				out(p+": "+Constantes.descTipoPalavra[p.getTipoPalavra()]+s);
			
		}

		out("Sujeito: "+frase.getSujeito());
		out("Verbo: "+frase.getVerbo());
		out("Predicado: "+frase.getPredicado());
		
	}
}
