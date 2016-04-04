<?
include $_SERVER['DOCUMENT_ROOT'].'/include/include.php';


verificaSeExiste();
	
	function verificaSeExiste(){
		$pathNormal="../uploads/normal/";
		$pathMini="../uploads/mini/";
		$rsTag=executaQuery("select * from media_foto order by 1 desc");
		while ($row = mysql_fetch_array($rsTag) ){
			
			$normal=file_exists ( $pathNormal.$row['filename_media'] );
			$mini=file_exists ( $pathMini.$row['filename_media'] );

			//echo "Verificando '".$row['filename_media']." normal:$normal mini:$mini'<br>";
			if ((!$normal) || (!$mini)){
				echo " excluindo ".$row['filename_media']." normal:$normal mini:$mini<br>\n";
				executaSQL("delete from media_foto where id_media=".$row['id_media']);	
				executaSQL("delete from media where id_media=".$row['id_media']);	
			}

			//executaSQL("delete from media_tag where id_tag=".$row['id_tag']);	
			//executaSQL("delete from tag where id_tag=".$row['id_tag']);	
		} 
	}

?>