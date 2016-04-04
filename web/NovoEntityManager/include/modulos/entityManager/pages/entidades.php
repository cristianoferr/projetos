<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_TAREFAS);
inicializa();
definePaginaAtual(PAGINA_ENTIDADES);

$id_projeto = projetoAtual();
$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$entidadeController = $controllerManager->getControllerForTable("entidade");

escreveHeader();

$bread = $painelManager->getBread();
$bread->addLink(translateKey("txt_entities"), "entities/$id_projeto");
$bread->mostra();
if (!$acao) {
    getWidgetManager()->showWidget(WIDGET_ENTIDADES, $id_projeto);
}

escreveFooter();
?>