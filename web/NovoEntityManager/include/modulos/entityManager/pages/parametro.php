<?

/* Parametro da Funcao */
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

inicializa();
definePaginaAtual(PAGINA_PARAMETRO_FUNCAO);

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();



$id_funcao = funcaoAtual();

$id_parametro_funcao = parametroAtual();

$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();

$controller = $controllerManager->getControllerForTable("parametro_funcao");
$functionController = $controllerManager->getControllerForTable("funcao");


//echo "acao:$acao";
if ($acao == "inserir") {
    /* $nm_funcao=$_POST['nm_funcao'];
      $id_entidade_retorno =$_POST['id_entidade_retorno'];
      $id_primitive_retorno =$_POST['id_primitive_retorno'];
      writeDebug("insereRegistro($id_entidade,$nm_funcao,$id_entidade_retorno,$id_primitive_retorno)");
      $id_funcao=$controller->insereRegistro($id_entidade,$nm_funcao,$id_entidade_retorno,$id_primitive_retorno);
      redirect(getHomeDir()."function/$id_projeto/$id_entidade/$id_funcao"); */
}

if ($acao == "excluir") {
    $controller->excluirRegistro($id_funcao);
    redirect(getHomeDir() . "function/$id_projeto/$id_entidade/$id_funcao");
}

escreveHeader();
$bread = $painelManager->getBread();
$bread->addLink($functionController->getName($id_funcao), "function/$id_projeto/$id_entidade/$id_funcao");

validaAcesso("funcao", $id_funcao);
validaAcesso("entidade", $id_entidade);
validaAcesso("funcao", $id_funcao);


if ($acao == ACAO_NOVO) {
    $painel = $painelManager->getPainel(PAINEL_FINC_PAR_FUNC);
    $painel->setTitulo(translateKey("txt_new_parameter"));
    $painel->adicionaInputForm("projeto", $id_projeto);
    $painel->adicionaInputForm("entidade", $id_entidade);
    $painel->adicionaInputForm("funcao", $id_funcao);
    $painel->setController($controller);
    $painel->adicionaLinkImportante(getHomeDir() . "function/$id_projeto/$id_entidade/$id_funcao", translateKey("txt_back"), false);

    $bread->addLink(translateKey("txt_new_parameter"), "parameter/new/$id_projeto/$id_entidade/$id_funcao");
    $bread->mostra();

    $painel->mostra();
} else {
    $bread->mostra();
}


if (!$acao) {
    //Painel funcao
    $painel = $painelManager->getPainel(PAINEL_PAR_FUNC);
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