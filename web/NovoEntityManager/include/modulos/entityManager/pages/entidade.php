<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_TAREFAS);
inicializa();
definePaginaAtual(PAGINA_ENTIDADE);

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();
$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
//$usuarioController = $controllerManager->getControllerForTable("usuario");
$entidadeController = $controllerManager->getControllerForTable("entidade");
$colunaController = $controllerManager->getControllerForTable("coluna");
$funcaoController = $controllerManager->getControllerForTable("funcao");

//echo "acao:$acao";
if ($acao == ACAO_INSERIR) {
    $painel = $painelManager->getPainel(PAINEL_FORM_INC_ENTIDADE);
    $painel->adicionaInputForm("projeto", $id_projeto);
    $arr = $painel->getArrayWithPostValues();

    $id_entidade = $entidadeController->insereRegistro($arr);
    redirect(getHomeDir() . "entity/$id_projeto/$id_entidade");
}

if ($acao == ACAO_EXCLUIR) {
    $entidadeController->excluirRegistro($id_entidade);
    redirect(getHomeDir() . "project/$id_projeto");
}


escreveHeader();


validaAcesso("entidade", $id_entidade);
validaAcesso("projeto", $id_projeto);

$bread = $painelManager->getBread();


if ($acao == ACAO_NOVO) {
    //Painel projeto
    $painel = $painelManager->getPainel(PAINEL_FORM_INC_ENTIDADE);
    $painel->setTitulo(translateKey("txt_new_entity"));
    $painel->adicionaInputForm("projeto", $id_projeto);
    $painel->setController($entidadeController);

    $bread->addLink(translateKey("txt_entities"), "entities/$id_projeto");
    $bread->addLink(translateKey("txt_new_entity"), "entity/new/$id_projeto");
    $bread->mostra();

    $painel->mostra();
} else {
    $bread->mostra();
}




if (!$acao) {
    $abaEntidade = 1;
    $abaColuna = 2;
    $abaFuncao = 3;
    $abaVisualizacao = 4;

    $viewAba = new ViewAba("ent_" . $id_entidade);
    $viewAba->adicionaAba($abaEntidade, translateKey("txt_entity"), true);
    $viewAba->adicionaAba($abaColuna, translateKey("txt_properties"), true);
    $viewAba->adicionaAba($abaFuncao, translateKey("txt_functions"), true);
    $viewAba->adicionaAba($abaVisualizacao, translateKey("txt_visualization"), false);


    $viewAba->desenhaAbas();
    //Painel Entidade	
    $viewAba->iniciaAba($abaEntidade);
    getWidgetManager()->showWidget(WIDGET_EDIT_ENTIDADE, $id_entidade);
    $viewAba->fimAba();

    $viewAba->iniciaAba($abaColuna);
    getWidgetManager()->showWidget(WIDGET_COLUNAS_ENTIDADE, $id_entidade);
    $viewAba->fimAba();

    $viewAba->iniciaAba($abaFuncao);
    //Painel Funcoes
    getWidgetManager()->showWidget(WIDGET_FUNCOES_ENTIDADE, $id_entidade);

    $viewAba->fimAba();
    $viewAba->iniciaAba($abaVisualizacao);

    //paineis de desenho
    getWidgetManager()->showWidget(WIDGET_VISUALIZA_ENTIDADE, $id_entidade);




    $viewAba->fimAba();

    $viewAba->start();
}


escreveFooter();
?>