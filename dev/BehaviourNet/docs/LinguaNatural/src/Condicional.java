package src;


public class Condicional extends ObjetoBasico {
String palavra="";  // Palavra para verificar
String prePalavra=""; //Palavra que vem antes
String novaPalavra=""; // Palavra que estára substituindo
String fimPalavra="";
String fimPalavraNovo="";
int tipoPalavra=-1;
int tempoSujeito=-1;
int tipoSujeito=-1;
int tipoPessoa=-1;
int generoSujeito=-1;
int tipoAdjetivo=-1;
int relacaoVerbo=0; //-1 = antes, 0=indiferente, 1=depois
boolean entreSujeitos=false;

	
	public int getRelacaoVerbo() {
	return relacaoVerbo;
}

public void setRelacaoVerbo(int relacaoVerbo) {
	this.relacaoVerbo = relacaoVerbo;
}

	public String getNovaPalavra() {
	return novaPalavra;
}

public void setNovaPalavra(String novaPalavra) {
	this.novaPalavra = novaPalavra;
}

public String getPalavra() {
	return palavra;
}

public void setPalavra(String palavra) {
	this.palavra = palavra;
}

public String getPrePalavra() {
	return prePalavra;
}

public void setPrePalavra(String prePalavra) {
	this.prePalavra = prePalavra;
}

public int getTempoSujeito() {
	return tempoSujeito;
}

public void setTempoSujeito(int tempoSujeito) {
	this.tempoSujeito = tempoSujeito;
}

public int getTipoPalavra() {
	return tipoPalavra;
}

public void setTipoPalavra(int tipoPalavra) {
	this.tipoPalavra = tipoPalavra;
}

public int getTipoPessoa() {
	return tipoPessoa;
}

public void setTipoPessoa(int tipoPessoa) {
	this.tipoPessoa = tipoPessoa;
}

public int getTipoSujeito() {
	return tipoSujeito;
}

public void setTipoSujeito(int tipoSujeito) {
	if (tipoSujeito!=-1)
		this.tipoSujeito = tipoSujeito;
}

	public Condicional() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getGeneroSujeito() {
		return generoSujeito;
	}

	public void setGeneroSujeito(int generoSujeito) {
		if (generoSujeito!=-1)
			this.generoSujeito = generoSujeito;
	}

	public String getFimPalavra() {
		return fimPalavra;
	}

	public void setFimPalavra(String fimPalavra) {
		this.fimPalavra = fimPalavra;
	}

	public String getFimPalavraNovo() {
		return fimPalavraNovo;
	}

	public void setFimPalavraNovo(String fimPalavraNovo) {
		this.fimPalavraNovo = fimPalavraNovo;
	}

	public int getTipoAdjetivo() {
		return tipoAdjetivo;
	}

	public void setTipoAdjetivo(int tipoAdjetivo) {
		if (tipoAdjetivo!=-1)
			this.tipoAdjetivo = tipoAdjetivo;
	}

	public boolean isEntreSujeitos() {
		return entreSujeitos;
	}

	public void setEntreSujeitos(boolean entreSujeitos) {
		this.entreSujeitos = entreSujeitos;
	}

	public String toString(){
		String s="<condicional palavra=\""+palavra+"\" ";
		if (entreSujeitos)
			s+="entreSujeitos=\""+entreSujeitos+"\" ";
		if (!fimPalavra.equals(""))
		  s+="fimPalavra=\""+fimPalavra+"\" ";
		if (!fimPalavraNovo.equals(""))
			s+="fimPalavraNovo=\""+fimPalavraNovo+"\" ";
		if (!prePalavra.equals(""))
			s+="prePalavra=\""+prePalavra+"\" ";
		if (!novaPalavra.equals(""))
			s+="novaPalavra=\""+novaPalavra+"\" ";
		if (relacaoVerbo!=0)
			s+="relacaoVerbo=\""+relacaoVerbo+"\" ";
		if (generoSujeito>=0)
			s+="generoSujeito=\""+Constantes.descGenero[generoSujeito-1]+"\" ";
		if (tempoSujeito>=0)
			s+="tempoSujeito=\""+Constantes.descTempo[tempoSujeito]+"\" ";
		if (tipoAdjetivo>=0)
		  s+="tipoAdjetivo=\""+Constantes.descTipoAdjetivo[tipoAdjetivo]+"\" ";
		if (tipoPalavra>=0)
			s+="tipoPalavra=\""+Constantes.descTipoPalavra[tipoPalavra]+"\" ";
		if (tipoPessoa>=0)
			s+="tipoPessoa=\""+ tipoPessoa+"\" ";
		if (tipoSujeito>=0)
			s+="tipoSujeito=\""+Constantes.descSujeito[tipoSujeito]+"\" ";
		
		
		return s+"/>";
	}
}
