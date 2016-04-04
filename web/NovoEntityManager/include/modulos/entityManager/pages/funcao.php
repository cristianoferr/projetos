<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_TAREFAS);
inicializa();
definePaginaAtual(PAGINA_FUNCAO);

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();



$id_funcao = funcaoAtual();
$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$controller = $controllerManager->getControllerForTable("funcao");

$parametroController = $controllerManager->getControllerForTable("parametro_funcao");


//echo "acao:$acao";
if ($acao == ACAO_INSERIR) {
    // $painel = $painelManager->getPainel(PAINEL_FINC_FUNC);
    //$painel->adicionaInputForm("projeto", $id_projeto);
    //$painel->adicionaInputForm("entidade", $id_entidade);
    //$arr = $painel->getArrayWithPostValues();
    $arr = array('projeto' => $id_projeto, 'entidade' => $id_entidade,
        'nm_funcao' => $_POST['nm_funcao'], 'id_entidade_retorno' => $_POST['id_entidade_retorno'],
        'id_datatype_retorno' => $_POST['id_datatype_retorno']);
    $id_funcao = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "function/$id_projeto/$id_entidade/$id_funcao");
}

if ($acao == ACAO_EXCLUIR) {
    $id_funcao = $controller->excluirRegistro($id_funcao);
    redirect(getHomeDir() . "entity/$id_projeto/$id_entidade");
}

escreveHeader();

validaAcesso("funcao", $id_funcao);
validaAcesso("entidade", $id_entidade);


$bread = $painelManager->getBread();
if ($id_funcao != "") {
    $nm = $controller->getName($id_funcao);
    $bread->addLink($nm, "function/$id_projeto/$id_entidade/$id_funcao");
}


if ($acao == ACAO_NOVO) {
    $painel = $painelManager->getPainel(PAINEL_FINC_FUNC);
    $painel->setTitulo(translateKey("txt_new_function"));
    $painel->adicionaInputForm("projeto", $id_projeto);
    $painel->adicionaInputForm("entidade", $id_entidade);
    $painel->setController($controller);
    $painel->adicionaLinkImportante(getHomeDir() . "entity/$id_projeto/$id_entidade", translateKey("txt_back"), false);

    $bread->addLink(translateKey("txt_new_function"), "function/new/$id_projeto/$id_entidade");
    $bread->mostra();

    $painel->mostra();
} else {
    $bread->mostra();
}



if (!$acao) {
    //Painel funcao
    $painel = $painelManager->getPainel(PAINEL_FUNCAO);
    $painel->setController($controller);
    $model = $controller->loadSingle($id_funcao, $painel);
    $painel->setTitulo($model->getDescricao());
    $painel->adicionaLink(getHomeDir() . "function/new/$id_projeto/$id_entidade", translateKey("txt_new_function"), false);
    $painel->adicionaLinkImportante(getHomeDir() . "entity/$id_projeto/$id_entidade", translateKey("txt_back"), false);
    $painel->mostra();


    //Painel Parametro funcao
    $painel = $painelManager->getPainel(PAINEL_PARAM_FUNC);
    $painel->setController($parametroController);
    $parametroController->loadRegistros("and parametro_funcao.id_funcao=$id_funcao", $painel);
    $painel->setTitulo(translateKey("txt_parameters"));
    $painel->mostra();
}


escreveFooter();
?>