<?php
header("Content-Type: text/html; charset=ISO-8859-1",true);

	include '../include/include.php';


	if (!isAdmin()){
		die();
	}




if ($_POST["modulo"]!=""){
	$idreg=$_POST["idreg"];
	$modulo=$_POST["modulo"];
	$campo=$_POST["campo"];
	
	$novoValor=urldecode($_POST["novoValor"]);
	$flagtexto=$_POST["flagtexto"];
	if ($flagtexto=="T"){
		$novoValor="'$novoValor'";
	}

	$chave="";
	if ($modulo=="partner"){
		$chave="id_partner";
	}


	$sql="update $modulo set $campo=$novoValor where $chave=$idreg";
	//addLog($sql);
	executaSQL("update $modulo set $campo=$novoValor where $chave=$idreg");


}


//atualiza valor da celula
if ($_POST["valorid"]!=""){
	$valorid=$_POST["valorid"];
	$novoValor=urldecode($_POST["novoValor"]);
	executaSQL("update valor set valor_coluna='$novoValor' where id_valor=$valorid");
}




function utf8_urldecode($str) {
    $str = preg_replace("/%u([0-9a-f]{3,4})/i","&#x\\1;",urldecode($str));
    return html_entity_decode($str,null,'UTF-8');;
  }
?>