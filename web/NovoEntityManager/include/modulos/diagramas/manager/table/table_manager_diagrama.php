<?
class TableManagerDiagrama extends TableManager{

	function carregaTabelaComNome($tableName){

		if ($tableName=="tipo_diagrama"){
			return $this->carregaTipoDiagrama();
		}

		if ($tableName=="diagrama"){
			return $this->carregaDiagrama();
		}
		if ($tableName=="componente_diagrama"){
			return $this->carregaComponenteDiagrama();
		}
	}


	function carregaDiagrama(){
		//tipo de banco (mysql,oracle,etc)
		$tabela=new TabelaModel("diagrama","id_diagrama","nm_diagrama");
		$coluna=$this->adicionaColunaNormal($tabela,"ID Diagrama","id_diagrama",true,TIPO_INPUT_INTEIRO);
		$coluna->setInvisible();
		$coluna=$this->adicionaColunaNormal($tabela,translateKey("txt_diagram"),"nm_diagrama",false,TIPO_INPUT_TEXTO_CURTO);
		$coluna->setDetalhesInclusao(true,true,translateKey("txt_new_diagram"));
		$coluna=$this->adicionaColunaComFK($tabela,translateKey("txt_diagram_type"),"id_tipo_diagrama",true,false,"tipo_diagrama",TIPO_INPUT_RADIO_SELECT); $coluna->setDetalhesInclusao(true,true,FIRST_OPTION);

		$this->adicionaColunaCalculada($tabela,translateKey("txt_components"),"cont_components","select count(*) as cont_components from componente_diagrama where componente_diagrama.id_diagrama=");
		$tabela->setPaginaInclusao(getHomeDir()."diagram");

		return $tabela;
	}

	function carregaTipoDiagrama(){
		//tipo de banco (mysql,oracle,etc)
		$tabela=new TabelaModel("tipo_diagrama","id_tipo_diagrama","nm_tipo_diagrama");
		$coluna=$this->adicionaColunaNormal($tabela,"ID tipo_diagrama","id_tipo_diagrama",true,TIPO_INPUT_INTEIRO);
		$coluna->setInvisible();
		$this->adicionaColunaNormal($tabela,translateKey("txt_diagram_type"),"nm_tipo_diagrama",false,TIPO_INPUT_TEXTO_CURTO);
		$tabela->traduzDescricao();
		return $tabela;
	}

	function carregaComponenteDiagrama(){
		//tipo de banco (mysql,oracle,etc)
		$tabela=new TabelaModel("componente_diagrama","id_componente_diagrama",null);
		$coluna=$this->adicionaColunaNormal($tabela,"id_componente_diagrama","id_componente_diagrama",true,TIPO_INPUT_INTEIRO);
		$coluna->setInvisible();
		$coluna=$this->adicionaColunaComFK($tabela,"diagrama","id_diagrama",true,false,"diagrama",TIPO_INPUT_SELECT_FK);
		$coluna=$this->adicionaColunaComFK($tabela,"funcao","id_funcao",true,false,"funcao",TIPO_INPUT_SELECT_FK);
		$coluna=$this->adicionaColunaComFK($tabela,"coluna","id_coluna",true,false,"coluna",TIPO_INPUT_SELECT_FK);
		$coluna=$this->adicionaColunaComFK($tabela,"entidade","id_entidade",true,false,"entidade",TIPO_INPUT_SELECT_FK);
		$coluna=$this->adicionaColunaComFK($tabela,"projeto","id_diagrama",true,false,"projeto",TIPO_INPUT_SELECT_FK);
		return $tabela;
	}
}
	?>