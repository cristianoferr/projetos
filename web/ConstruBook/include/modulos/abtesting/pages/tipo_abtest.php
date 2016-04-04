<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
adicionaModuloCarga(MODULO_ABTEST);
inicializa();
definePaginaAtual(PAGINA_TIPO_ABTEST);

$acao = acaoAtual();


$painelManager = getPainelManager();
$controller = getControllerForTable("tipo_abtest");

validaAdmin();

if ($acao == ACAO_INSERIR) {
    $painel = $painelManager->getPainel(PAINEL_FORM_INC_TIPO_ABTEST);
    $arr = $painel->getArrayWithPostValues();

    $id = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "tipoabtest");
}

if ($acao == ACAO_EXCLUIR) {
    $controller->excluirRegistro($id_entidade);
    redirect(getHomeDir() . "tipoabtest");
}

escreveHeader();
$bread = $painelManager->getBread();
$bread->addLink("Testes AB", "abtest");
$bread->addLink("Tipos de ABTest", "tipoabtest");

$mostraBread = novo($controller, $painelManager, $bread, $acao);



if ($mostraBread) {
    $bread->mostra();
    $painel = $painelManager->getPainel(PAINEL_TIPO_ABTESTS);
    $painel->setController($controller);
    $painel->setTitulo("Tipos de Testes AB");
    $controller->loadRegistros("", $painel);
    $painel->adicionaLink(getHomeDir() . "tipoabtest/new", "Novo Tipo ABTest", false);
    $painel->mostra();
}

function novo($controller, $painelManager, $bread, $acao) {
    if ($acao == ACAO_NOVO) {
        $painel = $painelManager->getPainel(PAINEL_FORM_INC_TIPO_ABTEST);
        $painel->setTitulo("Novo Tipo ABTest");
        $painel->setController($controller);

        $bread->addLink("Novo Tipo ABTest", "abtest/new");
        $bread->mostra();
        $painel->mostra();
        return false;
    }
    return true;
}

escreveFooter();
?>