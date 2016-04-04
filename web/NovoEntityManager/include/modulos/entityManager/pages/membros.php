<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

inicializa();
definePaginaAtual(PAGINA_MEMBROS);

$id_projeto = projetoAtual();
$id_usuario = $_GET['membro'];
$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$usuarioController = $controllerManager->getControllerForTable("usuario");
$memberController = $controllerManager->getControllerForTable("usuario_projeto");

//echo "acao:$acao";
if ($acao == ACAO_INSERIR) {
    validaAdminProjeto("projeto", $id_projeto);
    $id_usuario_novo = $_POST['id_usuario_novo'];

    echo "usuarios: " . $id_usuario_novo . "###";

    $email_externo = $_POST['email_externo'];
    $id_papel = $_POST['id_papel'];
    $texto_invite = $_POST['texto_invite'];

    $id_entidade_extends = 'null'; //$_POST['id_entidade_extends'];
    $id_camada = $_POST['id_camada'];

    $arr = array('id_projeto' => $id_projeto,
        'id_usuario_novo' => $id_usuario_novo,
        'email_externo' => $email_externo,
        'id_papel' => $id_papel,
        'texto_invite' => $texto_invite);


    $memberController->convidaUsuarios($arr);
    redirect(getHomeDir() . "members/$id_projeto");
}

if ($acao == ACAO_EXCLUIR) {
    validaAdminProjeto("projeto", $id_projeto);
    $memberController->excluirRegistro($id_usuario);
    redirect(getHomeDir() . "members/$id_projeto");
}


escreveHeader();


$bread = $painelManager->getBread();
$bread->addLink(translateKey("txt_members"), "members/$id_projeto");


validaAcesso("projeto", $id_projeto);

if ($acao == ACAO_NOVO) {
    //Painel projeto
    $painel = $painelManager->getPainel(PAINEL_FINC_MEMBRO);
    $painel->adicionaInputForm("projeto", $id_projeto);
    $painel->setController($memberController);
    $painel->setTituloNovoItem();
    $bread->addLinkNew($memberController);

    $painel->mostra();
} else {
    $bread->mostra();
}



if (!$acao) {

    //Painel Membro
    getWidgetManager()->showWidget(WIDGET_MEMBROS_PROJETO, $id_projeto, true);
}


escreveFooter();
?>