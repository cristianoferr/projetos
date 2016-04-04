<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_TAREFAS);
inicializa();
definePaginaAtual(PAGINA_TAREFAS);


$id_projeto = projetoAtual();
$acao = acaoAtual();
$id_tarefa = tarefaAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$tarefaController = $controllerManager->getControllerForTable("tarefa");
$userController = getControllerManager()->getControllerForTable("usuario");
//

$modo = modoAtual();
if ($modo) {
    $userController->atualizaInterfaceUsuario(MODO_TAREFA, $modo);
} else {
    $modo = $userController->getEstadoInterface(MODO_TAREFA, ABA_FILTRO_ENTREGAVEL_TAREFA);
}



validaAcesso("projeto", $id_projeto);
//echo "acao:$acao";


if ($acao == ACAO_INSERIR) {
    validaEscrita("projeto", $id_projeto);

    $painel = $painelManager->getPainel(PAINEL_FINC_TAREFA);
    $painel->adicionaInputForm("projeto", $id_projeto);
    $arr = $painel->getArrayWithPostValues();

    $id_tarefa = $tarefaController->insereRegistro($arr);
    redirect(getHomeDir() . "tasks/" . $id_projeto);
}

if ($acao == ACAO_EXCLUIR) {
    $id_tarefa = $_GET['tarefa'];
    validaEscrita("projeto", $id_projeto);
    $tarefaController->excluirRegistro($id_tarefa);
    redirect(getHomeDir() . "tasks/" . $id_projeto);
}

if ($acao == ACAO_AVANCAR) {
    $id_tarefa = $_GET['tarefa'];
    validaEscrita("projeto", $id_projeto);
    $tarefaController->avancarTarefa($id_tarefa);
    redirect(getHomeDir() . "tasks/" . $id_projeto);
}

escreveHeader();


$bread = $painelManager->getBread();
$bread->addLink(translateKey("txt_tasks"), "tasks/$id_projeto");
if ($id_tarefa != "") {
    $nm = $tarefaController->getDescricao($id_tarefa);
    $bread->addLink($nm, "task/$id_projeto/$id_tarefa");
}


if ($acao == ACAO_NOVO) {
    $bread->addLink(translateKey("txt_new_task"), "tasks/new/$id_projeto");
    $bread->mostra();
    incluiTarefa($id_projeto, $tarefaController);
} else {
    $bread->mostra();
}

if (!$acao) {

    //Painel tarefa
    if (isset($id_tarefa)) {
        getWidgetManager()->showWidget(WIDGET_EDIT_TAREFA, $id_tarefa);
        getWidgetManager()->showWidget(WIDGET_PREREQS_TAREFA, $id_tarefa);
    } else {
        desenhaAbas($id_projeto);
        listaTarefas($id_projeto);
    }
}


escreveFooter();

/////

function desenhaAbas($id_projeto) {
    Out::titulo(translateKey("txt_tasks"));
}

function listaTarefas($id_projeto) {
    getWidgetManager()->showWidget(WIDGET_LISTA_TAREFAS, $id_projeto);
}

function incluiTarefa($id_projeto, $tarefaController) {
    $painel = $GLOBALS['painelManager']->getPainel(PAINEL_FINC_TAREFA);
    $painel->adicionaInputForm("projeto", $id_projeto);
    $painel->setController($tarefaController);
    $painel->setTitulo(translateKey("txt_new_task"));
    $painel->adicionaLinkImportante(getHomeDir() . "tasks/$id_projeto", translateKey("txt_back"), false);
    $painel->mostra();
}

?>