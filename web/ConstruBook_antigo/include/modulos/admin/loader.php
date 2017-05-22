<?


	function loader_admin($fileManager,$modulo){
		$folder=$modulo->getName();
		$fileManager->addPHP('manager/painel_manager_admin.php',$folder);
	}

	function initialize_admin($fileManager,$modulo){
		$fileManager->addPainelManager(new PainelManagerAdmin());
	}
?>