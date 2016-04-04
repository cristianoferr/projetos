<?
	class TagController extends BaseController{
		function TagController(){

		}


		function getIdTag($novaTag){
			if ($_SESSION['ID_TAG_'.$novaTag]){
				return $_SESSION['ID_TAG_'.$novaTag];
			}
			$novaTag=validaTexto($novaTag);
			$row=executaQuerySingleRow("select * from tag where nm_tag='$novaTag'");
			if ($row) {
				$_SESSION['ID_TAG_'.$novaTag]=$row['id_tag'];
				return $row['id_tag'];
			}

			$id=getNext("id_tag","tag");
			executaSQL("insert into tag (id_tag,nm_tag) value (".$id.",'".$novaTag."')");
			$_SESSION['ID_TAG_'.$novaTag]=$id;
			return $id;
		}
	}

?>