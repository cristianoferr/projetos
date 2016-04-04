<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_TAREFAS);
inicializa();
definePaginaAtual(PAGINA_DATATYPE);

$acao = acaoAtual();
$id_projeto = projetoAtual();
$id = $_GET['id'];
$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$controller = $controllerManager->getControllerForTable("datatype");
$paramController = $controllerManager->getControllerForTable("datatype_param");


if ($acao == ACAO_INSERIR) {

    $nm_datatype = $_POST['nm_datatype'];
    $id_primitive_type = $_POST['id_primitive_type'];
    $desc_datatype = $_POST['desc_datatype'];

    $arr = array('id_projeto' => $id_projeto,
        'nm_datatype' => $nm_datatype,
        'id_primitive_type' => $id_primitive_type,
        'desc_datatype' => $desc_datatype);


    $id = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "datatypes/$id_projeto");
}



escreveHeader();

$bread = $painelManager->getBread();
$bread->addLink(translateKey("txt_datatypes"), "datatypes/$id_projeto");
if ($id != "") {
    $nm = $controller->getName($id);
    $bread->addLink($nm, "datatypes/$id_projeto/$id");
}
$bread->mostra();

validaAcesso("projeto", $id_projeto);


if ($acao == ACAO_NOVO) {
    //Painel projeto
    $painel = $painelManager->getPainel(PAINEL_FORM_INC_DATATYPE);
    $painel->adicionaInputForm("projeto", $id_projeto);
    $painel->setTitulo(translateKey("txt_new_datatype"));
    $painel->setController($controller);
    $painel->mostra();
}


if (!$acao) {

    if ($id == "") {

        $painel = $painelManager->getPainel(PAINEL_DATATYPES);
        $painel->setController($controller);
        $painel->setTitulo(translateKey("txt_datatypes"));
        $model = $controller->loadRegistros("", $painel);
        $painel->adicionaLink(getHomeDir() . "datatypes/new/$id_projeto", translateKey("txt_new_datatype"), false);

        //	$painel->adicionaLinkImportante(getHomeDir()."project/delete/$id_projeto",translateKey("txt_delete_project"),true);
        $painel->mostra();
    } else {
        getWidgetManager()->showWidget(WIDGET_EDIT_DATATYPE, $id);

        $painelParam = $painelManager->getPainel(PAINEL_DATATYPES_PARAMS);
        $painelParam->setController($paramController);
        $painelParam->setTitulo(translateKey("txt_datatype_parameters"));
        $model = $paramController->loadRegistros("and id_datatype=$id ", $painelParam);
        $painelParam->mostra();
    }
}



escreveFooter();
?>