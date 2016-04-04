<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_SITE_MANAGER);
inicializa();
definePaginaAtual(PAGINA_ADMIN);
$chave = $_GET['painel'];
$acao = acaoAtual();


$painelManager = getPainelManager();
$controller = getControllerForTable("painel");

validaAdmin();

if ($acao == ACAO_INSERIR) {
    $painel = $painelManager->getPainel(PAINEL_INCLUSAO_PAINEL);
    $arr = $painel->getArrayWithPostValues();

    $id = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "paineis");
}

if ($acao == ACAO_EXCLUIR) {
    $controller->excluirRegistro($chave);
    redirect(getHomeDir() . "paineis");
}

escreveHeader();
$bread = $painelManager->getBread();
$bread->addLink("Paineis", "paineis");

$mostraBread = novo($controller, $painelManager, $bread, $acao);

if ($mostraBread) {
    $mostraBread = edita($controller, $painelManager, $bread);
}


if ($mostraBread) {
    $bread->mostra();
    $painel = $painelManager->getPainel(PAINEL_PAINEIS);
    $painel->setController($controller);
    $painel->setTitulo("Painéis");
    $controller->loadRegistros("", $painel);
    $painel->adicionaLink(getHomeDir()
            . "paineis/new", "Novo Painel", false);
    $painel->mostra();
}

function novo($controller, $painelManager, $bread, $acao) {
    if ($acao == ACAO_NOVO) {
        $painel = $painelManager->getPainel(PAINEL_INCLUSAO_PAINEL);
        $painel->setTitulo("Novo Painel");
        $painel->setController($controller);

        $bread->addLink("Novo Painel", "paineis/new");
        $bread->mostra();
        $painel->mostra();
        return false;
    }
    return true;
}

function edita($controller, $painelManager, $bread) {
    $chave = $_GET['painel'];
    if ($chave) {
        $painel = $painelManager->getPainel(PAINEL_PAINEL);
        $model = $controller->loadSingle($chave, $painel);
        $painel->setTitulo("Edição");
        $painel->setController($controller);
        $bread->addLink($model->getDescricao(), "paineis/$chave");
        $bread->mostra();

        $painel->mostra();
        return false;
    }
    return true;
}

escreveFooter();
?>