<?
	include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
	inicializa();

	validaAdmin();

	$acao=acaoAtual();

	$painelManager=getPainelManager();
	$controllerManager=getControllerManager();
	$controller=$controllerManager->getControllerForTable("meta_type");
	
	escreveHeader();

	$id=$_GET['id'];


	?><h1 class="titulo">Meta Type</h1><?

	if ($id==""){
		//Painel Usuarios
		$painel=$painelManager->getPainelMetaTypeAdmin();
		$painel->setController($controller);
		$model=$controller->loadRegistros("",$painel);

		//	$painel->adicionaLinkImportante(getHomeDir()."project/delete/$id_projeto",translateKey("txt_delete_project"),true);
		$painel->mostra();
	} else {
		$painel=$painelManager->getPainelMetaTypeAdminSingle();
		$painel->setController($controller);
		$model=$controller->loadSingle($id,$painel);
		$painel->mostra();

	}



	escreveFooter();
?>