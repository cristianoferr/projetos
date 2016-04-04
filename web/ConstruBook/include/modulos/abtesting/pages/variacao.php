<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
adicionaModuloCarga(MODULO_ABTEST);
inicializa();
definePaginaAtual(PAGINA_ABTEST);
$id_abtest = abtestAtual();

$acao = acaoAtual();

$painelManager = getPainelManager();
$controller = getControllerForTable("abtest_variacao");
$abcontroller = getControllerForTable("abtest");

validaAdmin();

if ($acao == ACAO_INSERIR) {
    $painel = $painelManager->getPainel(PAINEL_FORM_INC_ABTEST_VARIACAO);
    $painel->adicionaInputForm("id_abtest", $id_abtest);
    $arr = $painel->getArrayWithPostValues();
    $id_abtest = $arr['id_abtest'];
    $id_var = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "abtest/variacao/$id_abtest");
}

if ($acao == ACAO_EXCLUIR) {
    $variacao = $_GET['variacao'];
    $controller->excluirRegistro($variacao);
    redirect(getHomeDir() . "abtest/$id_abtest");
}

escreveHeader();
$bread = $painelManager->getBread();

$abtestmodel = $abcontroller->loadSingle($id_abtest);
$bread->addLink($abtestmodel->getDescricao(), "abtest/$id_abtest");
$bread->addLink("Variações", "abtest/variacao/$id_abtest");

if ($acao == ACAO_NOVO) {
    $painel = $painelManager->getPainel(PAINEL_FORM_INC_ABTEST_VARIACAO);
    $painel->adicionaInputForm("id_abtest", $id_abtest);
    $painel->setTitulo("Nova Variação");
    $painel->setController($controller);
    //$painel->adicionaAcao(getHomeDir() . "abtest/" . abtestAtual(), translateKey("txt_back"), false);

    $bread->addLink("Nova Variação", "abtest/variacao/new");
    $bread->mostra();
    $painel->mostra();
} else {
    $bread->mostra();
}

if (!$acao) {
    $painel = $painelManager->getPainel(PAINEL_ABTESTS_VARIACOES);
    $painel->setController($controller);
    $painel->setTitulo("Testes AB");
    $controller->loadRegistros("and id_abtest=" . abtestAtual(), $painel);
    $painel->adicionaLink(getHomeDir() . "abtest/variacao/new/" . abtestAtual(), "Nova Variação", false);
    $painel->adicionaLink(getHomeDir() . "abtest/" . abtestAtual(), translateKey("txt_back"), false);
    $painel->mostra();
}


escreveFooter();
?>