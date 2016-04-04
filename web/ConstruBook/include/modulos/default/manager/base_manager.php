<?

class BaseManager{
	private $manager;
	
	function BaseManager($manager){
		$this->manager=$manager;
	}

	function getTableManager(){
		return $this->manager->getTableManager();
	}


}

	
	?>