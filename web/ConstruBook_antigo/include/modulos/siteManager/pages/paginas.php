<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
adicionaModuloCarga(MODULO_SITE_MANAGER);
inicializa();
definePaginaAtual(PAGINA_ADMIN);
$chave = $_GET['pagina'];
$acao = acaoAtual();


$painelManager = getPainelManager();
$controller = getControllerForTable("pagina");

validaAdmin();

if ($acao == ACAO_INSERIR) {
    $painel = $painelManager->getPainel(PAINEL_INCLUSAO_PAGINA);
    $arr = $painel->getArrayWithPostValues();

    $id = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "paginas");
}

if ($acao == ACAO_EXCLUIR) {
    $controller->excluirRegistro($chave);
    redirect(getHomeDir() . "paginas");
}

escreveHeader();
$bread = $painelManager->getBread();
$bread->addLink("Páginas", "paginas");

$mostraBread = novo($controller, $painelManager, $bread, $acao);

if ($mostraBread){
    $mostraBread = edita($controller, $painelManager, $bread);
}

if ($mostraBread) {
    $bread->mostra();
    $painel = $painelManager->getPainel(PAINEL_PAGINAS);
    $painel->setController($controller);
    $painel->setTitulo("Páginas");
    $controller->loadRegistros("", $painel);
    $painel->adicionaLink(getHomeDir()
            . "paginas/new", "Nova Página", false);
    // inicioDebug();
    $painel->mostra();
    // fimDebug();
}

function novo($controller, $painelManager, $bread, $acao) {
    if ($acao == ACAO_NOVO) {
        $painel = $painelManager->getPainel(PAINEL_INCLUSAO_PAGINA);
        $painel->setTitulo("Nova Página");
        $painel->setController($controller);

        $bread->addLink("Nova Página", "paginas/new");
        $bread->mostra();
        $painel->mostra();
        return false;
    }
    return true;
}

function edita($controller, $painelManager, $bread) {
    $chave = $_GET['pagina'];
    if ($chave) {
        $painel = $painelManager->getPainel(PAINEL_PAGINA);
        $model = $controller->loadSingle($chave, $painel);
        $painel->setTitulo("Edição");
        $painel->setController($controller);
        $painel->mostra();
        return false;
    }
    return true;
}

escreveFooter();
?>