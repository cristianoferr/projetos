<?	
	class BaseController{
		var $arrModels; //contem os modelos
		var $rowCount;
		var $rowAtual;
		
		function BaseController(){
			$this->resetArray();
			if (!isset($this)){
				echo "BaseController this not defined";
			}
		}

		function resetArray(){
			$this->arrModels=array();
			$this->rowAtual=0;
		}

		function getModelByPk($pk){
			for ($c=0; $c<sizeOf($this->arrModels);$c++){
				$model=$this->arrModels[$c];
				if ($model->getId()==$pk){return $model;}
			}
		}


		function next(){
			if ($this->rowAtual<$this->rowCount){
				$this->rowAtual++;
				return $this->arrModels[$this->rowAtual-1];
			} else {

			}
		}

		function loadFrom($sql,$flagReset = true){
			if ($flagReset){
				$this->resetArray();
			}
			$rsColuna=executaQuery($sql);
			$this->rowCount=0;
			
			//echo $sql;
			while ($row = mysql_fetch_array($rsColuna)){
				$media=$this->carregaRegistro($row);

				$this->arrModels[$this->rowCount]=$media;
				//echo $this->arrModels[$this->rowCount]->getId()."|".$media->getId();
				$this->rowCount++;
			}
			$this->rowAtual=0;

		}


		
		function carregaRegistro($row){
			echo "carregaRegistro deve ser extendida.";
			die();
		}

		function getDBName(){
			return "table_name";
		}

		function getPK(){
			return "pk_name";
		}
	}




?>
