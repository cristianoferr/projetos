<?
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_DIAGRAMAS);
inicializa();
definePaginaAtual(PAGINA_BUILD);

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();
$acao = acaoAtual();


$painelManager = getPainelManager();
$controllerManager = getControllerManager();


escreveHeader();

validaAcesso("projeto", $id_projeto);

Out::titulo(translateKey("txt_export"));





$abaErrors = 1;
$abaEstrutura = 2;
$abaConteudo = 4;

$viewAba = new ViewAba("build_" . $id_projeto);
$abaAtual = $viewAba->getAbaAtual();
$viewAba->ativaVisaoUnica(getHomeDir() . "export/$id_projeto/");
$viewAba->adicionaAba($abaErrors, translateKey("txt_check_errors"), true);
$viewAba->adicionaAba($abaEstrutura, translateKey("txt_sql_structure"), true);
$viewAba->adicionaAba($abaConteudo, translateKey("txt_table_content"), false);



$entregavel = getWidgetManager()->showWidget(WIDGET_SELECT_DELIVERABLE, "entregavel", "export/$id_projeto/$abaAtual/");

$viewAba->desenhaAbas();



if ($abaAtual == $abaErrors) {
    
}

if ($abaAtual == $abaEstrutura) {
    $controller = new ExportController();
    $saida = $controller->buildProjeto($id_projeto, $entregavel);
    ?><br><?
    $elTextArea = elMaker("textarea")->setAtributo("rows", 40)->setAtributo("cols", 100)->setValue($saida)->mostra();
}

if ($abaAtual == $abaConteudo) {
    $controller = new ExportController();
    $saida = $controller->conteudoProjeto($id_projeto, $entregavel);
    ?><br><?
    $elTextArea = elMaker("textarea")->setAtributo("rows", 40)->setAtributo("cols", 100)->setValue($saida)->mostra();
}

$viewAba->start();

escreveFooter();
?>