<?
define("PAINEL_DIAGRAMAS_PROJETO",'P_DIAG_PROJ');
define("PAINEL_FINC_DIAGRAMA",'P_FINC_DIAG');
class PainelManagerDiagrama{

	function getPainelComNome($nome){
		if ($nome==PAINEL_DIAGRAMAS_PROJETO) return $this->getPainelDiagramasProjeto();
		if ($nome==PAINEL_FINC_DIAGRAMA) return $this->getFormInclusaoDiagrama();
	}

	function getPainelDiagramasProjeto(){
		$table=getTableManager()->getTabelaComNome("diagrama");
		$painel=new PainelHorizontal("pdp",$table);
		$painel->addColunaWithDBName("id_diagrama");
		$painel->addColunaWithDBName("nm_diagrama");
		$painel->addColunaWithDBName("id_tipo_diagrama");
		$painel->addColunaWithDBName("cont_components");
		$painel->modoEdicao();

		$painel->setEditLink("diagram/".projetoAtual()."/");
		$painel->setDeleteLink("diagram/delete/".projetoAtual()."/");
		return $painel;
	}	

	function getFormInclusaoDiagrama(){
		$table=getTableManager()->getTabelaComNome("diagrama");
		$painel=new PainelVertical("fid",$table);
		$painel->addColunaWithDBName("id_diagrama");
		$painel->addColunaWithDBName("nm_diagrama");
		$painel->addColunaWithDBName("id_tipo_diagrama");
		
		$painel->adicionaLinkImportante(getHomeDir()."diagrams/".projetoAtual(),translateKey("txt_back"),false);
		$painel->modoInclusao();
		$painel->desativaAjax();
		return $painel;
	}

}
?>