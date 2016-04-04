<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_ABTEST);
inicializa();
definePaginaAtual(PAGINA_ABTEST);

$acao = acaoAtual();


$painelManager = getPainelManager();
$controller = getControllerForTable("abtest");

$abtest = abtestAtual();
//inicioDebug();
$url = $controller->getUrlForAbTest($abtest);
incAbtest($abtest);
//fimDebug();
//write("url:$url");
redirect(getHomeDir() . $url);
?>