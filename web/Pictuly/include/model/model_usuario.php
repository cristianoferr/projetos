<?class Usuario extends BaseModel{
		var $nome;
		var $email;

		//Getters and setters
		function setNome($nome){
			$this->nome=$nome;
		}
		function getNome(){
			return $this->nome;
		}		

		function setEmail($v){
			$this->email=$v;
		}
		function getEmail(){
			return $this->email;
		}	
	}

?>