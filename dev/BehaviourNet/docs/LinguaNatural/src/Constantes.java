package src;



public class Constantes extends ObjetoBasico {

	//	Tempo da Oração
 public static final int tempoPresente=1;
 public static final int tempoPreteritoImperfeito=2;
public static final int tempoPreteritoPerfeito=3;
public static final int tempoPreteritoMQPerfeito=4;
public static final int tempoFuturoPresente=5;
public static final int tempoFuturoPreterito=6;
public static final int tempoPreteritoPerfeitoSimples=7;
public static final int tempoPreteritoComposto=8;


public static final String[] descTempo=new String[] {"","tempoPresente",
"tempoPreteritoImperfeito",
"tempoPreteritoPerfeito",
"tempoPreteritoMQPerfeito",
"tempoFuturoPresente",
"tempoFuturoPreterito",
"tempoPreteritoPerfeitoSimples",
"tempoPreteritoComposto"};

// Tipo do Sujeito
public static final int sujSimples=0;
public static final int sujComposto=1;
public static final String[] descSujeito=new String[] {"sujSimples",
	"sujComposto"};


// Tipo de Palavra
public static final int palVerboAux=0;
public static final int palArtIndef=1;
public static final int palArtDef=2;
public static final int palAdj=3;
public static final int palSubst=4;
public static final int palVerbo=5;
public static final int palConjAditiva=6;  
public static final int palConjAdversa=7;
public static final int palConjConclusiva=8; 
public static final int palConjExplicativa=9; 
public static final int palPronomePessoal=10;
public static final int palPronomePossessivo=11;
public static final int palPronomeDemonstrativo=12;
public static final int palPronomeIndefinido=13;
public static final int palPronomeRelativo=14;
public static final int palPronomeInterrogativo=15;
public static final int palPreposicao=16;
public static final int palAdverbio=17;
public static final int palString=18;


public static final String[] descTipoPalavra=new String[] {"palVerboAux",
"palArtIndef","palArtDef","palAdj","palSubst","palVerbo","palConjAditiva","palConjAdversa",
"palConjConclusiva","palConjExplicativa","palPronomePessoal","palPronomePossessivo",
"palPronomeDemonstrativo","palPronomeIndefinido","palPronomeRelativo","palPronomeInterrogativo","palPreposicao","palAdverbio","String"};

//Tipo de Adjetivo
public static final int adjCor=0;
public static final int adjForma=1;
public static final int adjTemperatura=2;
public static final int adjIntensidade=3;
public static final int adjProporcao=4;
public static final int adjQualidade=5;
public static final int adjDefeito=6;
public static final int adjPatrio=7;
public static final String[] descTipoAdjetivo=new String[] {"adjCor",
	"adjForma","adjTemperatura","adjIntensidade","adjProporcao",
	"adjQualidade","adjDefeito","adjPatrio"};



// Genero do Sujeito
public static final int genMasculino=1;
public static final int genFeminino=2;
public static final String[] descGenero=new String[] {"genMasculino",
"genFeminino"};
}
