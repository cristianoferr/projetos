<?
include $_SERVER['DOCUMENT_ROOT'].'/include/controller/dicionarios.php';
include $_SERVER['DOCUMENT_ROOT'].'/include/model/model_media.php';
include $_SERVER['DOCUMENT_ROOT'].'/include/model/model_usuario.php';
include $_SERVER['DOCUMENT_ROOT'].'/include/controller/controller_base.php';
include $_SERVER['DOCUMENT_ROOT'].'/include/controller/controller_media.php';
include $_SERVER['DOCUMENT_ROOT'].'/include/controller/controller_tag.php';
include $_SERVER['DOCUMENT_ROOT'].'/include/controller/controller_user.php';
include $_SERVER['DOCUMENT_ROOT'].'/include/view/view_media.php';
include $_SERVER['DOCUMENT_ROOT'].'/include/view/view_media_single.php';





	define("MAX_MEDIA_PAGE",     20);
	define("TAG_ALL",51);

	define("UPLOAD_FOLDER","uploads/");
	define("UPLOAD_FOLDER_MINI",UPLOAD_FOLDER."mini/");
	define("UPLOAD_FOLDER_NORMAL",UPLOAD_FOLDER."normal/");
	
/*	
	// pega da tabela primitive type
	define("PRIMITIVE_INT",     1);
	define("PRIMITIVE_SHORT_TEXT",     2);
	define("PRIMITIVE_TEXT",     3);
	define("PRIMITIVE_BOOLEAN",     4);
	define("PRIMITIVE_DATE",     5);

	// pega da tabela RELACAO_DATATYPE
	define("RELACAO_DATATYPE_PK",     1);
	define("RELACAO_DATATYPE_OBRIGATORIO",     2);
	define("RELACAO_DATATYPE_OPCIONAL",     3);
*/


	//verificaUsuarioLogado();

	//limpo a tag... se sobrar algo diferente de vazio eu cancelo.
	

	function translateKey($key){
		$controller=getMediaController();
		return $controller->translateKey($key);
	}

	function isNSFW(){
		if (strpos(getPaginaAtual(),"nsfw.pictuly.com")){
			return true;
		} else {
			return false;
		}
	}

	function getHomeLink(){
		if (isNSFW()){
			return "http://nsfw.pictuly.com";
		} else {
			return "http://pictuly.com";
		}
	}



	function validaTag($tag){
		$vtag=$tag;
		$tag=strtolower($tag);
		$tag = str_replace(" ", "", $tag);
		$tag = str_replace("/", "", $tag);

		foreach (range('a', 'z') as $letter) {
		    $tag = str_replace($letter, "", $tag);
		}

		foreach (range('0', '9') as $letter) {
		    $tag = str_replace($letter, "", $tag);
		}

		if ($tag){
			echo "For informada uma tag impropria: $vtag ($tag)";
			die();
		}

	}
	function getUserController(){
		return getMediaController()->getUserController();
	}

	function getMediaController(){
		//echo isset($_SESSION["media_controller"]);
		//unset($_SESSION["media_controller"]);
		if (isset($GLOBALS["media_controller"])) return $GLOBALS["media_controller"];
		$controller=new MediaController();

		$controller->setDefaultLang(1); //EN
		$GLOBALS["media_controller"]=$controller;

		return $GLOBALS["media_controller"];
	}

	function tagAtual(){
		$tag =$_GET["tag"];
		if ($tag=="")$tag=$_POST["tag"];
		if ($tag=="")$tag=$_SESSION["tag"];
		if ($tag=="")$tag=TAG_ALL;

		$_SESSION["tag"]=$tag;
		
		return $tag;
	}

	function verificaUsuarioLogado(){
		//sem sessão eu permito apenas a tela de login
		//echo "pagina:".getPaginaAtual()." contains:".(strpos(getPaginaAtual(),"exportAll.php"));
		//die();

		if (strpos(getPaginaAtual(),"login.php")){
			return "";
		}

		if ($_SESSION["id_usuario"]==""){
			redirect("login.php");
		}
		return $_SESSION["id_usuario"];
	}

	function usuarioAtual(){
		return $_SESSION["id_usuario"];
	}


	function isAdmin(){
		return ($_SESSION["id_usuario"]==2);
	}


	function logaAcesso($desc,$media){
		if (!isAdmin()){
			executaSQL("insert into log_site(pagina_log_site,ip_log_site,data_log_site,desc_log_site,id_media,flagnsfw_log_site,id_usuario) values ('".getPaginaAtual()."','".getRealIpAddr()."',now(),'$desc.  Referrer:".$_SERVER['HTTP_REFERER']."','.$media.','".isNSFW()."','".usuarioAtual()."')");
		}
	}

	
//echo "id:".$_SESSION["id_usuario"];
?>