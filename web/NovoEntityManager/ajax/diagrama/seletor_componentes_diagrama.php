<?
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

adicionaModuloCarga(MODULO_DIAGRAMAS);

inicializa();

$id_projeto=projetoAtual();
$id_entidade=entidadeAtual();
$acao=acaoAtual();
$id_diagrama=diagramaAtual();

$painelManager=getPainelManager();
$controllerManager=getControllerManager();
$diagramController=$controllerManager->getControllerForTable("diagrama");

validaAcesso("projeto",$id_projeto);
validaEscrita("projeto",$id_projeto);

desenhaSeletor($id_diagrama);

function desenhaSeletor($id_diagrama){
	$painel=new SeletorComponentesDiagrama($id_diagrama);
	$painel->mostra();
}



?>