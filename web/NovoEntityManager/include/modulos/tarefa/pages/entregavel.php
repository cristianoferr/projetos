<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

adicionaModuloCarga(MODULO_TAREFAS);

inicializa();
definePaginaAtual(PAGINA_ENTREGAVEL);

$entregavel = entregavelAtual();

$id_projeto = projetoAtual();
$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
//$tarefaController=$controllerManager->getControllerForTable("tarefa");
$controller = $controllerManager->getControllerForTable("entregavel");
$userController = $controllerManager->getControllerForTable("usuario");



$modo = modoAtual();
if ($modo) {
    $userController->atualizaInterfaceUsuario(MODO_ENTREG, $modo);
} else {
    $modo = $userController->getEstadoInterface(MODO_ENTREG, ABA_FILTRO_GRADE);
}

validaAcesso("projeto", $id_projeto);

if ($acao == ACAO_EXCLUIR) {
    $entregavel = $controller->excluirRegistro($entregavel);
    redirect(getHomeDir() . "deliverables/$id_projeto");
}


//echo "acao:$acao";
if ($acao == ACAO_INSERIR) {
    validaEscrita("projeto", $id_projeto);

    $nm_entregavel = $_POST['nm_entregavel'];
    $desc_entregavel = $_POST['desc_entregavel'];
    $id_entregavel_pai = $_POST['id_entregavel_pai'];

    $arr = array('id_projeto' => $id_projeto,
        'nm_entregavel' => $nm_entregavel,
        'desc_entregavel' => $desc_entregavel,
        'id_entregavel_pai' => $id_entregavel_pai,
        'id_status_entregavel' => STATUS_ENTREG_PLANEJADO);

    $id_entregavel = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "deliverables/" . $id_projeto);
}

if ($acao == ACAO_EXCLUIR) {
    validaEscrita("projeto", $id_projeto);
    $controller->excluirRegistro($entregavel);
    redirect(getHomeDir() . "deliverables/" . $id_projeto);
}

if ($acao == ACAO_AVANCAR) {
    validaEscrita("projeto", $id_projeto);
    $controller->avancarEntregavel($entregavel);
    redirect(getHomeDir() . "deliverables/" . $id_projeto);
}

escreveHeader();

$bread = $painelManager->getBread();
$bread->addLink(translateKey("txt_deliverables"), "deliverables/$id_projeto");
if ($entregavel != "") {
    $nm = $controller->getDescricao($entregavel);
    $bread->addLink($nm, "deliverables/$id_projeto/$entregavel");
}

if ($acao == ACAO_NOVO) {
    $bread->addLink(translateKey("txt_new_deliverable"), "deliverables/new/$id_projeto");
    $bread->mostra();

    inclui($id_projeto, $controller);
} else {
    $bread->mostra();
}

if (!$acao) {



    //Painel tarefa
    if ($entregavel != "") {
        mostra($entregavel, $id_projeto, $controller);
    } else {
        Out::titulo(translateKey("txt_deliverables"));
        $abaAtual = desenhaAbas($id_projeto);

        if ($abaAtual == ABA_FILTRO_GRADE) {
            getWidgetManager()->showWidget(WIDGET_ENTREGAVEIS, $id_projeto);
        }
        if ($abaAtual == ABA_FILTRO_ENTREGAVEL_SOMENTE) {
            listaEntregaveis($id_projeto, $controller, $filtro);
        }
        if ($abaAtual == ABA_FILTRO_ENTREGAVEL_TAREFA) {
            listaEntregaveis($id_projeto, $controller, $filtro, true);
        }
    }
}


escreveFooter();

/////

function desenhaAbas($id_projeto) {

    $viewAba = new ViewAba("modo_tarefa_" . $id_projeto);
    $viewAba->setParam("modo");
    $abaAtual = $viewAba->getAbaAtual();
    //echo $abaAtual;
    $viewAba->ativaVisaoUnica(getHomeDir() . "deliverables/$id_projeto/");
    $viewAba->adicionaAba(ABA_FILTRO_GRADE, translateKey("txt_tarefa_filtro_grade"), false);
    $viewAba->adicionaAba(ABA_FILTRO_ENTREGAVEL_SOMENTE, translateKey("txt_tarefa_filtro_entregavel_somente"), false);
    $viewAba->adicionaAba(ABA_FILTRO_ENTREGAVEL_TAREFA, translateKey("txt_tarefa_filtro_entregavel_tarefa"), false);
    $viewAba->desenhaAbas();
    $viewAba->start();

    return $abaAtual;
}

function listaEntregaveis($id_projeto, $controller, $filtro, $flagtarefa = null) {

    $painel = getPainelManager()->getPainel(PAINEL_ENTR_HIERARQUICO);
    $painel->setController($controller);
    $controller->loadRegistros("and entregavel.id_projeto=$id_projeto $filtro", $painel);
    $painel->adicionaLink(getHomeDir() . "deliverables/new/$id_projeto", translateKey("txt_new_deliverable"), false);
    $painel->adicionaLinkImportante(getHomeDir() . "project/$id_projeto", translateKey("txt_back"), false);

    if ($flagtarefa) {
        $tarefaController = getControllerManager()->getControllerForTable("tarefa");
        $tarefaController->loadRegistros();
        $painel->setTarefaController($tarefaController);
    }
    $painel->mostra();
}

function mostra($id_entregavel, $id_projeto, $controller) {
    getWidgetManager()->showWidget(WIDGET_EDIT_ENTREGAVEL, $id_entregavel);
}

function inclui($id_projeto, $controller) {
    $painel = $GLOBALS['painelManager']->getPainel(PAINEL_FINC_ENTREGAVEL);
    $painel->adicionaInputForm("projeto", $id_projeto);
    $painel->setController($controller);
    $painel->setTitulo(translateKey("txt_new_deliverable"));
    $painel->adicionaLinkImportante(getHomeDir() . "deliverables/$id_projeto", translateKey("txt_back"), false);
    $painel->mostra();
}

?>