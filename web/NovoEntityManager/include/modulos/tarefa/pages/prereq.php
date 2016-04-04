<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_TAREFAS);
inicializa();

$id_projeto = projetoAtual();
$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$controller = $controllerManager->getControllerForTable("prereq_tarefa");
$prereq = $_GET['prereq'];


if ($acao == ACAO_INSERIR) {
    //echo "tarefa:" . $_POST['id_tarefa'];
    validaEscrita("tarefa", $_POST['id_tarefa']);


    $arr = array('id_tarefa' => $_POST['id_tarefa'],
        'id_tarefa_prereq' => $_POST['id_tarefa_prereq']
    );

    $controller->insereRegistro($arr);
    redirect(getHomeDir() . "task/$id_projeto/" . $_POST['id_tarefa']);
}

if ($acao == ACAO_EXCLUIR) {
    $id_tarefa = tarefaAtual();
    $controller->excluirRegistro($id_tarefa, $prereq);
    redirect(getHomeDir() . "task/$id_projeto/$id_tarefa");
}
?>