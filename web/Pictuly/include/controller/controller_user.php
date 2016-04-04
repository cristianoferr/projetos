<?
	class UserController extends BaseController{
		var $hashTag; // chave:id, valor:nome tag
		function UserController(){
			$this->resetArray();
		}


		function getDBName(){
			return "usuario";
		}

		function getPK(){
			return "id_usuario";
		}

		function registraUsuario($name,$email,$password){
			$name=validaTexto($name);
			$email=validaEmail($email);
			$password=validaTexto($password);

			if (existsQuery("select * from usuario where email_usuario='$email'")) return "emailExists";

			$id_usuario=getNext("id_usuario","usuario");
			executaSQL("insert into usuario (id_usuario,nm_usuario,email_usuario,senha_usuario) values ($id_usuario,'$name','$email','$password')");
			$this->setUserLogado($id_usuario,$email);

		}


		function validaUsuario($email,$password){
			$email=validaTexto($email);
			$password=validaTexto($password);
			$row=executaQuerySingleRow("select id_usuario from usuario where email_usuario='$email' and senha_usuario='$password'");
			if ($row){
				$this->setUserLogado($row['id_usuario'],$email);
				redirect("account.php");
			} else {
				redirect("login.php?erro=invalidPasswd&email=$email");
			}
		}

		function setUserLogado($id_usuario,$email){
			$_SESSION["id_usuario"]=$id_usuario;
			$_SESSION["email_usuario"]=$email;
		}


		function getUserName($userId){
			validaNumero($userId);
			$modelo=$this->getModelByPk($userId);
			if ($modelo) return $modelo->getNome;

			$this->loadSingle($userId);
			return $this->getUserName($userId);
			
		}
		function loadSingle($userId){
			$this->loadFrom("select * from usuario where id_usuario=$userId",false);
		}

		function carregaRegistro($row){
			$media=new Usuario($this);
			$media->setId($row[$this->getPK()]);
			$media->setNome($row['nm_usuario']);
			$media->setEmail($row['email_usuario']);
			return $media;
		}

			
	}
?>