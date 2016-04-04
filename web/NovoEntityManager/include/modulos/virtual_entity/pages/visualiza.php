<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_TAREFAS);
inicializa();
definePaginaAtual(PAGINA_VISUALIZA);

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();
$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$entidadeController = $controllerManager->getControllerForTable("entidade");
$registroController = $controllerManager->getControllerForTable("registro");



if ($acao == ACAO_NOVO) {
    //inicioDebug();
    $registroController->insereRegistro($id_entidade);
    //fimDebug();
    redirect(getHomeDir() . "view/$id_projeto/$id_entidade");
}

if ($acao == ACAO_EXCLUIR) {
    $id_registro = $_GET['registro'];
    $registroController->excluirRegistro($id_registro);
    redirect(getHomeDir() . "view/$id_projeto/$id_entidade");
}

escreveHeader();

validaAcesso("entidade", $id_entidade);
validaAcesso("projeto", $id_projeto);

$bread = $painelManager->getBread();
$bread->addLink(translateKey("txt_data_input"), "view/$id_projeto/$id_entidade");
$bread->mostra();




getWidgetManager()->showWidget(WIDGET_VIEW_VIRTUAL_ENTITY, $id_entidade);


escreveFooter();
?>
