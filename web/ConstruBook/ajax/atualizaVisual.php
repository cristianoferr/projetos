<?
include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
inicializa();
//$painelManager=getPainelManager();
$controllerManager=getControllerManager();
$memberController=$controllerManager->getControllerForTable("usuario");

$identificador=$_POST['identificador'];
$valor=$_POST['valor'];

$memberController->atualizaInterfaceUsuario($identificador,$valor);


//addLog("AtualizaVisual($identificador,$valor)");
?>