<?
	include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
	inicializa();

	validaAdmin();

	$acao=acaoAtual();

	$painelManager=getPainelManager();
	$controllerManager=getControllerManager();
	$controller=$controllerManager->getControllerForTable("primitive_type");
	$controllerParam=$controllerManager->getControllerForTable("primitive_type_param");



	escreveHeader();

	$id=$_GET['id'];


	?><h1 class="titulo">Primitive Type</h1><?

	if ($id==""){
		//Painel Usuarios
		$painel=$painelManager->getPainelPrimitiveTypeAdmin();
		$painel->setController($controller);
		$model=$controller->loadRegistros("",$painel);

		//	$painel->adicionaLinkImportante(getHomeDir()."project/delete/$id_projeto",translateKey("txt_delete_project"),true);
		$painel->mostra();
	} else {
		$painel=$painelManager->getPainelPrimitiveTypeAdminSingle();
		$painel->setController($controller);
		$model=$controller->loadSingle($id,$painel);
		$painel->mostra();

		$painelParam=$painelManager->getPainelPrimitiveTypeParamAdmin();
		$painelParam->setController($controllerParam);
		$model=$controllerParam->loadRegistros("and id_primitive_type=$id",$painelParam);
		$painelParam->setTitulo("Parametros");
		$model=$controllerParam->loadSingle($id,$painelParam);
		$painelParam->mostra();
	}



	escreveFooter();
?>