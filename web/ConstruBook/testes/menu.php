<?include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';

inicializa();

$id_projeto=projetoAtual();
$id_entidade=entidadeAtual();
$acao=acaoAtual();


escreveHeader();




$topMenu=new TopMenuPainel(translateKey("site_name"),getHomeDir()."#top");

//Menu Projetos
$id_projeto=projetoAtual();
$mnProjects=$topMenu->addMenu(translateKey("txt_projects"));
	$subMyProjects=$mnProjects->addOpcao(translateKey('txt_my_projects'),getHomeDir()."projects/$id_projeto");
	$mnProjects->addSeparador();
	$subImport=$mnProjects->addOpcao(translateKey('txt_import_new_project'),getHomeDir()."import");
	$mnProjects->addSeparador();
	$subImport=$mnProjects->addOpcao(translateKey('txt_categories'),getHomeDir()."explore/categories");
	$subImport=$mnProjects->addOpcao(translateKey('txt_hot_new'),getHomeDir()."explore/hotandnew");
	

$id_projeto=projetoAtual();
if ($id_projeto){
	$projController=getControllerManager()->getControllerForTable('projeto');
	$nm_projeto=$projController->getProjectCod($id_projeto);

	//menuProjeto
	$mnProjeto=$topMenu->addMenu($nm_projeto);
		$subEntidades=$mnProjeto->addOpcaoComAdd(translateKey('txt_entities'),getHomeDir()."entities/$id_projeto",getHomeDir()."entities/new/$id_projeto");
		$subMembros=$mnProjeto->addOpcaoComAdd(translateKey('txt_members'),getHomeDir()."members/$id_projeto",getHomeDir()."members/new/$id_projeto");
		$mnProjeto->addSeparador();
		$subDeliverables=$mnProjeto->addOpcaoComAdd(translateKey('txt_deliverables'),getHomeDir()."deliverables/$id_projeto",getHomeDir()."deliverables/new/$id_projeto");
		$subTarefas=$mnProjeto->addOpcaoComAdd(translateKey('txt_tasks'),getHomeDir()."tasks/$id_projeto",getHomeDir()."tasks/new/$id_projeto");


	//menu Diagramas
	$mnDiagramas=$topMenu->addMenu(translateKey("txt_diagrams"));
		$subMatrizDependencia=$mnDiagramas->addOpcao(translateKey('txt_matriz_dependencia'),getHomeDir()."diagrams/matrix/$id_projeto");
		$subER=$mnDiagramas->addOpcao(translateKey('txt_diagram_entity_relationship'),getHomeDir()."diagrams/er/$id_projeto");
		$mnDiagramas->addSeparador();
		$subProjectDiagrams=$mnDiagramas->addOpcaoComAdd(translateKey('txt_project_diagrams'),getHomeDir()."diagrams/$id_projeto",getHomeDir()."diagrams/new/$id_projeto");

	//menu Export
	$mnExport=$topMenu->addMenu(translateKey("txt_export"));
		$subEstrutura=$mnExport->addOpcao(translateKey('txt_sql_structure'),getHomeDir()."export/structure/$id_projeto");
		$subConteudo=$mnExport->addOpcao(translateKey('txt_sql_content'),getHomeDir()."export/content/$id_projeto");
		
}
if (usuarioAtual()!=""){
	/*$nmUsuario=$_SESSION["nm_usuario"];
	if ($nmUsuario==""){
		$userController=getControllerManager()->getControllerForTable('usuario');
		$nmUsuario=$userController->getUserLogin(usuarioAtual());
	}*/

	//menu Help
	$mnHelp=$topMenu->addMenu(translateKey("txt_help"));
		$mnKnowledgeBase=$mnHelp->addOpcao(translateKey("know_knowledge_base"),getHomeDir()."knowledge");
		$mnContact=$mnHelp->addOpcao(translateKey("know_contact_us"),getHomeDir()."contact");

	//menu Usuario
	$mnUsuario=$topMenu->addMenu(translateKey("txt_me"));
		$mnUsuario->addOpcao(translateKey("txt_my_profile"),getHomeDir()."profile");
		$mnUsuario->addOpcaoComValor(translateKey("txt_notificacoes"),getHomeDir()."profile/notifications",'50');
		$mnUsuario->addSeparador();
		$mnUsuario->addOpcao(translateKey("txt_logout"),getHomeDir()."logout");
}

$topMenu->mostra();





escreveFooter();
?>