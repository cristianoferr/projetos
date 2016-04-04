<?
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
inicializa();

validaAdmin();

$acao=acaoAtual();

$painelManager=getPainelManager();
$controllerManager=getControllerManager();
$userController=$controllerManager->getControllerForTable("usuario");



escreveHeader();



//Painel Usuarios
$painel=$painelManager->getPainel(PAINEL_USER_ADMIN);
$painel->setController($userController);
$model=$userController->loadRegistros("",$painel);
?><h1 class="titulo">Usuários</h1><?

//	$painel->adicionaLinkImportante(getHomeDir()."project/delete/$id_projeto",translateKey("txt_delete_project"),true);
$painel->mostra();



escreveFooter();
?>