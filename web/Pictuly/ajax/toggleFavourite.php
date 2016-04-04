<?
	include '../include/include.php';

	$media=$_POST['media'].$_GET['media'];
	$id_usuario=usuarioAtual();
	if(($media) && ($id_usuario)){
		validaNumero($media);
		
		

		$id_tag=TagController::getIdTag("favorite");
		$varname="MEDIA_FAV_".$media."_".$id_tag;
		if (existsQuery("select * from media_tag_usuario where id_media=$media and id_usuario=$id_usuario and id_tag=$id_tag")){
			executaSql("delete from media_tag_usuario where id_media=$media and id_usuario=$id_usuario and id_tag=$id_tag");
			$_SESSION[$varname]=0;
		} else {
			executaSql("insert into media_tag_usuario(id_media,id_usuario,id_tag) values ($media,$id_usuario,$id_tag)");
			$_SESSION[$varname]=1;
		}
	
}