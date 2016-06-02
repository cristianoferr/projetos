package src;

import java.util.Vector;

public class Frase extends ObjetoBasico {
Vector palavras=new Vector();
int pos=0;

int posVerbo=0;
int tempo=-1;
int tipoSujeito=-1;
int tipoPessoa=-1;
int generoSujeito=-1;
Vector sujeito=new Vector();
Vector predicado=new Vector();
Palavra verbo=null;


public boolean isEntreSujeitos(){
	boolean antes=false;
	boolean depois=false;
	if (posVerbo!=0){
		for (int i=posVerbo;i<size();i++){
			Palavra p = (Palavra)palavras.elementAt(i);
			if (p.possivelSujeito()){
				if (i<getPos()) antes=true;
				if (i>getPos()) depois=true;
			}
		}
	} else {
		for (int i=0;i<getPos()+2;i++){
			Palavra p = (Palavra)palavras.elementAt(i);
			if (p.possivelSujeito()){
				if (i<getPos()) antes=true;
				if (i>getPos()) depois=true;
			}
		}
	}
	return ((antes) && (depois));
}




public Vector getPredicado() {
	return predicado;
}

public void setPredicado(Vector predicado) {
	this.predicado = predicado;
}

public Vector getSujeito() {
	return sujeito;
}

public void setSujeito(Vector sujeito) {
	this.sujeito = sujeito;
}

public Palavra getVerbo() {
	return verbo;
}

public void setVerbo(Palavra verbo) {
	this.verbo = verbo;
}

public int getTipoPessoa() {
	return tipoPessoa;
}

public void setTipoPessoa(int tipoPessoa) {
	if (tipoPessoa!=-1) 
		this.tipoPessoa = tipoPessoa;
}

public int getTempo() {
	return tempo;
}

public void setTempo(int tempo) {
	if (tempo!=-1)
		this.tempo = tempo;
}

public int getTipoSujeito() {
	return tipoSujeito;
}

public void setTipoSujeito(int tpSujeito) {
	if (tpSujeito!=-1)
		this.tipoSujeito = tpSujeito;
}

public Frase(){
	
}

public int size(){
	return palavras.size();
}

public Palavra getPalavra(){
	return (Palavra)palavras.elementAt(getPos());
}

public Palavra getPalavraPrev(){
	return (Palavra)palavras.elementAt(getPos()-1);
}

public Palavra getPalavraPost(){
	return (Palavra)palavras.elementAt(getPos()+1);
}

public void add(Palavra p){
	palavras.add(p);
}

public int getPos() {
	return pos;
}

public void setPos(int pos) {
	this.pos = pos;
}

public int getGeneroSujeito() {
	return generoSujeito;
}

public void setGeneroSujeito(int generoSujeito) {
	this.generoSujeito = generoSujeito;
}

public int getPosVerbo() {
	return posVerbo;
}

public void setPosVerbo(int posVerbo) {
	this.posVerbo = posVerbo;
}


}
