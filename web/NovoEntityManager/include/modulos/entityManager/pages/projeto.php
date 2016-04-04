<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

adicionaModuloCarga(MODULO_TAREFAS);
inicializa();
definePaginaAtual(PAGINA_PROJETO);

$id_projeto = projetoAtual();
$acao = acaoAtual();
if (!$_GET["pm"]) {
    $_SESSION["projeto_mestre"] = $id_projeto;
}
writeDebug("projeto.php antes definicao controllers");

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$projetoController = $controllerManager->getControllerForTable("projeto");
//$camadaController = $controllerManager->getControllerForTable("camada_projeto");
//echo "acao:$acao";
if ($acao == ACAO_INSERIR) {
    if (isGuest()) {
        redirect(getHomeDir() . "projects");
    }
    $nm_projeto = $_POST['nm_projeto'];
    $cod_projeto = $_POST['cod_projeto_novo'];
    $iniciais_projeto = $_POST['iniciais_projeto'];
    $package_projeto = $_POST['package_projeto'];
    $id_tipo_banco = $_POST['id_tipo_banco'];
    $id_visibilidade_projeto = $_POST['id_visibilidade_projeto'];
    $id_metodologia = $_POST['id_metodologia'];
    //writeDebug("insereRegistro($nm_projeto,$iniciais_projeto,$package_projeto,$id_tipo_banco,$id_metodologia)");

    $arr = array('nm_projeto' => $nm_projeto,
        'cod_projeto' => $cod_projeto,
        'iniciais_projeto' => $iniciais_projeto,
        'package_projeto' => $package_projeto,
        'id_tipo_banco' => $id_tipo_banco,
        'id_visibilidade_projeto' => $id_visibilidade_projeto,
        'id_metodologia' => $id_metodologia);

    $id_projeto = $projetoController->insereRegistro($arr);
    if ($id_projeto) {
        writeDebug("inserido, id_projeto=$id_projeto");
        redirect(getHomeDir() . "project/" . $id_projeto);
    }
}

if ($acao == ACAO_EXCLUIR) {
    //$id_projeto=$_GET['projeto'];
    $id_projeto = $projetoController->excluirRegistro($id_projeto);
    redirect(getHomeDir() . "projects/");
}

escreveHeader();

$bread = $painelManager->getBread();


validaAcesso("projeto", $id_projeto);

if ($acao == ACAO_NOVO) {
    if (isGuest()) {
        redirect(getHomeDir() . "projects");
    }
    //Painel projeto
    $painel = $painelManager->getPainel(PAINEL_FINC_PROJETO);
    $painel->setTitulo(translateKey("txt_new_project"));
    $painel->setController($projetoController);
    $bread->addLink(translateKey("txt_new_project"), "project/new");
    $bread->mostra();

    $painel->mostra();
} else {
    $bread->mostra();
}

if (!$acao) {

    writeDebug("projeto.php  Painel projeto");
    //Painel projeto
    $painel = $painelManager->getPainel(PAINEL_PROJETO);
    $painel->setController($projetoController);
    $model = $projetoController->loadSingle($id_projeto, $painel);
    $painel->setTitulo($model->getDescricao());
    $_SESSION["nome_projeto_$id_projeto"] = $model->getDescricao();

    //$painel->adicionaLink("projeto.php?acao=linkar&projeto=$id_projeto","Linkar Projeto",false);
    $painel->adicionaLinkImportante(getHomeDir() . "project/delete/$id_projeto", translateKey("txt_delete_project"), true);
    $painel->mostra();


    //Painel Entregaveis/Projeto
    getWidgetManager()->showWidget(WIDGET_ENTREGAVEIS_PROJETO, $id_projeto);
    getWidgetManager()->showWidget(WIDGET_ENTIDADES_PROJETO, $id_projeto);
    getWidgetManager()->showWidget(WIDGET_OPEN_TASKS, $id_projeto);
    getWidgetManager()->showWidget(WIDGET_MEMBROS_PROJETO, $id_projeto, false);
}


escreveFooter();
?>