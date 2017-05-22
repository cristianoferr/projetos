<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
adicionaModuloCarga(MODULO_SITE_MANAGER);
inicializa();
definePaginaAtual(PAGINA_ADMIN);
$chave = $_GET['chave'];
$acao = acaoAtual();


$painelManager = getPainelManager();
$controller = getControllerForTable("chave_lingua");

validaAdmin();

if ($acao == ACAO_INSERIR) {
    $painel = $painelManager->getPainel(PAINEL_INCLUSAO_STRING);
    $arr = $painel->getArrayWithPostValues();

    $id = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "strings");
}

if ($acao == ACAO_EXCLUIR) {
    $controller->excluirRegistro($chave);
    redirect(getHomeDir() . "strings");
}

escreveHeader();
$bread = $painelManager->getBread();
$bread->addLink("Strings", "strings");

$mostraBread = novo($controller, $painelManager, $bread, $acao);

if ($mostraBread)
    $mostraBread = edita($controller, $painelManager, $bread);


if ($mostraBread) {
    $bread->mostra();
    $painel = $painelManager->getPainel(PAINEL_STRINGS);
    $painel->setController($controller);
    $painel->setTitulo("Strings");
    $controller->loadRegistros("", $painel);
    $painel->adicionaLink(getHomeDir()
            . "strings/new", "Nova String", false);
    // inicioDebug();
    $painel->mostra();
    // fimDebug();
}

function novo($controller, $painelManager, $bread, $acao) {
    if ($acao == ACAO_NOVO) {
        $painel = $painelManager->getPainel(PAINEL_INCLUSAO_STRING);
        $painel->setTitulo("Nova String");
        $painel->setController($controller);

        $bread->addLink("Nova String", "strings/new");
        $bread->mostra();
        $painel->mostra();
        return false;
    }
    return true;
}

function edita($controller, $painelManager, $bread) {
    $chave = $_GET['chave'];
    if ($chave) {
        /* $painel = $painelManager->getPainel(PAINEL_STRING);
          $model = $controller->loadSingleText($chave, $painel);
          $painel->setTitulo("Edição");
          $painel->setController($controller);
         */
        $bread->addLink($chave, "strings/$chave");
        $bread->mostra();
        // $painel->mostra();

        mostraStrings($chave, $painelManager);
        return false;
    }
    return true;
}

function mostraStrings($chave, $painelManager) {
    $controller = getControllerForTable("valor_lingua");

    $painel = $painelManager->getPainel(PAINEL_VALOR_STRING);
    $controller->loadRegistros("and nm_chave_lingua='$chave'", $painel);
    $painel->setController($controller);
    $painel->mostra();
}

escreveFooter();
?>