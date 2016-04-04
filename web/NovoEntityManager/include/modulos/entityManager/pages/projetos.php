<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

adicionaModuloCarga(MODULO_TAREFAS);
inicializa();
definePaginaAtual(PAGINA_PROJETOS);

$id_projeto = projetoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$projetoController = $controllerManager->getControllerForTable("projeto");
escreveHeader();

Out::titulo(translateKey("txt_my_projects"));
//Painel projeto
$painelProjeto = $painelManager->getPainel(PAINEL_PROJETOS);
$painelProjeto->setController($projetoController);
$model = $projetoController->loadRegistros("and id_projeto in (select id_projeto from usuario_projeto where id_usuario=" . usuarioAtual() . ")", $painelProjeto);
$painelProjeto->mostra();


escreveFooter();
?>