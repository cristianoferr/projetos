<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
adicionaModuloCarga(MODULO_ABTEST);
inicializa();
definePaginaAtual(PAGINA_ERRO);

$acao = acaoAtual();


$painelManager = getPainelManager();
$controller = getControllerForTable("feature_check");


escreveHeader();

$feature = validaTexto($_GET['feature']);


Out::painel(translateKey("txt_not_yet_implemented"), translateKey("txt_not_yet_implemented_explain"));

$controller->getIdFeature($feature);


escreveFooter();
?>