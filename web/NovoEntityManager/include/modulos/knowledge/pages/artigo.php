<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_KNOWLEDGE);
inicializa();
definePaginaAtual(PAGINA_ARTIGO);

$id_artigo = $_GET['artigo'];
$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();

//echo "!".$_POST['texto_artigo_inclusao']."!";

if ($acao == ACAO_INSERIR) {
    $controller = $controllerManager->getControllerForTable("artigo");
    $painel = $painelManager->getPainel(PAINEL_NOVO_ARTIGO);
    $painel->adicionaInputForm("pagina", "");
    $painel->adicionaInputForm("elemento", "");
    $arr = $painel->getArrayWithPostValues();
    $id = $controller->insereRegistro($arr, $_POST['texto_artigo']);
    redirect($arr['pagina']);
} else if ($acao == ACAO_EXCLUIR) {
    $controller = $controllerManager->getControllerForTable("artigo");
    $controller->excluirRegistro($id_artigo);
    redirect($_SERVER['HTTP_REFERER']);
} else {
    if (!$id_artigo) {
        die();
    }
    escreveHeader();
    $bread = $painelManager->getBread();
    $bread->addLink(translateKey("txt_articles"), "article/$id_artigo");
    $bread->mostra();

    getWidgetManager()->showWidget(WIDGET_VIEW_ARTIGO, $id_artigo);
    escreveFooter();
}
?>