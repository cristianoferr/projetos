<?
include $_SERVER['DOCUMENT_ROOT'].'/include/include.php';

	corrige(",");
	corrige(";");
	//corrige("_");


	limpaTagErrada("=''");
	limpaTagErrada(" like '%.com%'");
	limpaTagErrada(" like '%www%'");
	limpaTagErrada(" like '%http://%'");


	
	function limpaTagErrada($nmTag){
		$rsTag=executaQuery("select * from tag where nm_tag$nmTag");
		echo "nmTag:$nmTag";
		while ($row = mysql_fetch_array($rsTag) ){
			echo "Excluindo '".$row['nm_tag']."'<br>";
			executaSQL("delete from media_tag where id_tag=".$row['id_tag']);	
			executaSQL("delete from tag where id_tag=".$row['id_tag']);	
		} 
	}

	function corrige($char){
		$rsTag=executaQuery("select * from tag where nm_tag like'%$char%'");
		while ($row = mysql_fetch_array($rsTag) ){
			echo "Corrigindo ".$row['nm_tag']."<br>";
			corrigeTag($row['id_tag'],$row['nm_tag'],$char);
		} 
	}

	function corrigeTag($id_tag,$nm_tag,$char){
		$tags=explode($char, $nm_tag);
		for ($c = 0; $c < sizeOf($tags); $c++){
			$novaTag=$tags[$c];
			if ($novaTag!=""){
				$novaIdTag=getIdTag($novaTag);

				linkaMedia($id_tag,$novaIdTag);
			}
		}

		executaSQL("delete from media_tag where id_tag=$id_tag");
		executaSQL("delete from tag  where id_tag=$id_tag");
	}


function linkaMedia($id_tag,$novaIdTag){
	$rsMedia=executaQuery("select * from media_tag where id_tag=$id_tag");
	while ($row = mysql_fetch_array($rsMedia)){
		$id_media=$row['id_media'];
		executaSQL("delete from media_tag where id_media=$id_media and id_tag=$novaIdTag");	
		executaSQL("insert into media_tag (id_media,id_tag) values ($id_media,$novaIdTag)");	
	}
}

function getIdTag($novaTag){
	$row=executaQuerySingleRow("select * from tag where nm_tag='$novaTag'");
	if ($row) return $row['id_tag'];

	$id=getNext("id_tag","tag");
	executaSQL("insert into tag (id_tag,nm_tag) value (".$id.",'".$novaTag."')");
	return $id;
}

?>