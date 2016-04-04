<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

adicionaModuloCarga(MODULO_DIAGRAMAS);
adicionaModuloCarga(MODULO_TAREFAS);


inicializa();
definePaginaAtual(PAGINA_DIAGRAMA);

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();
$acao = acaoAtual();
$id_diagrama = diagramaAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$diagramController = $controllerManager->getControllerForTable("diagrama");

$painelSeletor = new SeletorComponentesDiagrama($id_diagrama);


//echo "acao:$acao";
if ($acao == ACAO_INSERIR) {
    $nm_diagrama = $_POST['nm_diagrama'];
    $projeto = $_POST['projeto'];
    $id_tipo_diagrama = $_POST['id_tipo_diagrama'];

    $arr = array('nm_diagrama' => $nm_diagrama,
        'id_tipo_diagrama' => $id_tipo_diagrama,
        'projeto' => $projeto);


    $id_diagrama = $diagramController->insereRegistro($arr);
    redirect(getHomeDir() . "diagram/$id_projeto/$id_diagrama");
}

if ($acao == ACAO_EXCLUIR) {
    $diagramController->excluirRegistro($id_diagrama);
    redirect(getHomeDir() . "diagrams/$id_projeto");
}



$painelSeletor->trataAcao($acao);

escreveHeader(false);
validaAcesso("diagrama", $id_diagrama);




$bread = $painelManager->getBread();
$bread->addLink(translateKey("txt_diagrams"), "diagrams/$id_projeto");
if ($id_diagrama != "") {
    $nm = $diagramController->getDescricao($id_diagrama);
    $bread->addLink($nm, "diagram/$id_projeto/$id_diagrama");
}


if ($acao == ACAO_NOVO) {
    //Painel projeto
    $painel = $painelManager->getPainel(PAINEL_FINC_DIAGRAMA);
    //$painel->setTitulo(translateKey("txt_new_diagram"));
    $painel->adicionaInputForm("projeto", $id_projeto);
    $painel->setController($diagramController);

    $painel->setTituloNovoItem();
    $bread->addLinkNew($diagramController);

    // $bread->addLink(translateKey("txt_new_diagram"), "diagram/new/$id_projeto");
    $bread->mostra();


    $painel->mostra();
} else {
    $bread->mostra();
}


if (($id_diagrama) && (!$acao)) {
    $diagramController->desenhaDiagrama($id_diagrama, $painelSeletor);
}




//if (isAdmin())
//escreveFooter();
?>