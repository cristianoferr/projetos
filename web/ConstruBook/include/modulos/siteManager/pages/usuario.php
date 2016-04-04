<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
inicializa();
definePaginaAtual(PAGINA_USER_PROFILE);



$acao = acaoAtual();

escreveHeader();

$bread = getPainelManager()->getBread();
$bread->addLink(translateKey("txt_your_profile"), "profile");
$bread->mostra();
Out::titulo(translateKey("txt_your_profile"));

getWidgetManager()->showWidget(WIDGET_EDIT_USUARIO, usuarioAtual());

getWidgetManager()->showWidget(WIDGET_PROJETOS_USUARIO, usuarioAtual());
escreveFooter();
?>