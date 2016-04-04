<?
	class MembroProjetoController extends BaseController{

		function filtrosExtras(){
			return " and usuario_projeto.id_projeto=".projetoAtual();
		}

		function convidaUsuarios($array){
			$array['id_projeto']=validaNumero($array['id_projeto'],"id_projeto convidaUsuario MembroProjetoController");
			$array['id_papel']=validaNumero($array['id_papel'],"id_papel convidaUsuario MembroProjetoController");
			$array['id_usuario_novo']=validaTexto($array['id_usuario_novo'],"id_usuario_novo convidaUsuario MembroProjetoController");
			$array['email_externo']=validaTexto($array['email_externo'],"email_externo convidaUsuario MembroProjetoController");
			$array['texto_invite']=validaTexto($array['texto_invite'],"texto_invite convidaUsuario MembroProjetoController");

 			$arrUsers=explode(",", $array['id_usuario_novo']);
 			for ($c=0;$c<sizeof($arrUsers);$c++){
 				$id_user=trim($arrUsers[$c]);
 			//	$this->convidaUsuario($array,$id_user);
 			}

			$email_externo=$array['email_externo'];
			$email_externo = str_replace(PHP_EOL, ",", $email_externo);
			$email_externo = str_replace("\\r\\n", ",", $email_externo);
			$email_externo = str_replace("\n", ",", $email_externo);
			$email_externo = str_replace(" ", ",", $email_externo);
			$email_externo = str_replace(";", ",", $email_externo);
 			$arrUsers=explode(",", $email_externo);
 			echo "<br>";
 			for ($c=0;$c<sizeof($arrUsers);$c++){
 				$email_user=trim($arrUsers[$c]);
 				echo "email:$email_user|<br>";
 			//	$this->convidaUsuarioExterno($array,$email_user);
 			}


		
		}


		function countForProjeto($id_projeto){
		 	$id_projeto=validaNumero($id_projeto,"id_projeto countForProjeto EntidadeController");
		 	$id="count_usuario_projeto_".projetoAtual();
		 	if ($this->getCacheInfo($id))return $this->getCacheInfo($id);


		 	$rs=executaQuery("select count(*) as tot from usuario_projeto where id_projeto=?",array(projetoAtual()));
		 	if ($row = $rs->fetch()){
		 		$this->setCacheInfo($id,$row['tot']);
		 		return $this->getCacheInfo($id);
		 	}
		 }

		function convidaUsuario($array,$id_user){
			if (!$this->existeConviteUsuario($array['id_projeto'],$id_user)){
				executaSQL("insert into invite_usuario_projeto (id_usuario,id_projeto,id_papel,texto_invite,data_invite) values (?,?,?,?,now())",array($id_user,$array['id_projeto'],$array['id_papel'],$array['texto_invite']));
			}
		}

		function convidaUsuarioExterno($array,$email_user){
			if (!$this->existeConviteEmail($array['id_projeto'],$email_user)){
				//executaSQL("insert into invite_usuario_projeto (id_usuario,id_projeto,id_papel,texto_invite,data_invite) values (?,?,?,?,now())",array($id_user,$array['id_projeto'],$array['id_papel'],$array['texto_invite']));
			}
		}

		function existeConviteUsuario($id_projeto,$id_user){
			return (existsQuery("select * from invite_usuario_projeto where id_projeto=? and id_usuario=?",array($id_projeto,$id_user)));
		}

		function existeConviteEmail($id_projeto,$email_usuario){
			return (existsQuery("select * from invite_usuario_projeto where id_projeto=? and email_externo_invite=?",array($id_projeto,$email_usuario)));
		}

}
?>