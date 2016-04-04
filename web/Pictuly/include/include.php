<?php
//Esse arquivo tem que ser o mais genérico possível para fácil reuso
	session_start();
	include $_SERVER['DOCUMENT_ROOT'].'/include/htmltext.php';

	$mysql_host = "localhost";
	$mysql_database = "pictulyc_main";
	$mysql_user = "pictulyc_user";
	$mysql_password = "...";


	$contaQueries=0;



	mysql_connect($mysql_host, $mysql_user, $mysql_password)or die ('I cannot connect to the database because: ' . mysql_error()); 
	// Select database
	mysql_select_db($mysql_database) or die(mysql_error());



	include 'include_especifico.php';

	function redirect($url){
		header( "Location: $url" ) ;
		die();
	}

	function getRealIpAddr()
{
  if (!empty($_SERVER['HTTP_CLIENT_IP']))
  //check ip from share internet
  {
    $ip=$_SERVER['HTTP_CLIENT_IP'];
  }
  elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR']))
  //to check ip is pass from proxy
  {
    $ip=$_SERVER['HTTP_X_FORWARDED_FOR'];
  }
  else
  {
    $ip=$_SERVER['REMOTE_ADDR'];
  }
  return $ip;
}

	function validaNumero($var){
		if (!is_numeric ($var)){
			echo "Invalid number: $var";
			die();
		}
		return $var;
	}

	function validaTexto($var){
		$var=str_replace("'", "&#39;",$var);
		return $var;
	}

	function validaEmail($var){
		
		return validaTexto($var);
	}

	


	function getPaginaAtual() {
		$isHTTPS = (isset($_SERVER["HTTPS"]) && $_SERVER["HTTPS"] == "on");
		$port = (isset($_SERVER["SERVER_PORT"]) && ((!$isHTTPS && $_SERVER["SERVER_PORT"] != "80") || ($isHTTPS && $_SERVER["SERVER_PORT"] != "443")));
		$port = ($port) ? ':'.$_SERVER["SERVER_PORT"] : '';
		$url = ($isHTTPS ? 'https://' : 'http://').$_SERVER["SERVER_NAME"].$port.$_SERVER["REQUEST_URI"];
		
		if (strpos($url, '?') == FALSE){
			$url=$url."?";
		}

		return $url;
	}

	
	function existsQuery($sql) {
		if (!executaQuerySingleRow($sql)){
			return false;
		} else {
			return true;
		}
	}

	function executaQuery($sql) {
		if (verificaFlagEdicao()){
			echo "Executando query: $sql<br>";
		}
		// Execute the query (the recordset $rs contains the result)
		$GLOBALS["contaQueries"]=$GLOBALS["contaQueries"]+1;
		$rs = mysql_query($sql);
		return $rs;
	}


	function addLog($log){
		$log=str_replace("'", "#",$log);
		executaSQL("insert into log (nm_log) values ('$log') ");
	}

	//retorna array
	function executaQuerySingleRow($sql) {
		// Execute the query (the recordset $rs contains the result)
		//echo $sql;
		$rs = executaQuery($sql);
		$row = mysql_fetch_array($rs);
		return $row;
	}

	function executaSQL($sql){
		if (verificaFlagEdicao()){
			echo "Executando SQL: $sql<br>";
		}
		$GLOBALS["contaQueries"]=$GLOBALS["contaQueries"]+1;
		mysql_query($sql) or die(mysql_error());
	}

	function getNext($id,$table){
		$row=executaQuerySingleRow("select max($id)+1 as tot from $table");
		if ($row['tot']=="") return 1;
		return $row['tot'];
	}


function verificaFlagEdicao(){
		$flagEdicao=$_SESSION["flagEdicao"];
		//echo "flagEdicao(session): $flagEdicao<br>";
		if ($flagEdicao=="")$flagEdicao=false;
		//echo "flagEdicao1: $flagEdicao<br>";


		if ($_GET["togleEdicao"]!=""){
			//echo "get flagEdicao:".$_GET["togleEdicao"].	"<br>";
			if ($_GET["togleEdicao"]=="true"){
				$flagEdicao=true;
			} else {
				$flagEdicao=false;
			}
			$_SESSION["flagEdicao"]=$flagEdicao;
		}

		//echo "flagEdicao2: $flagEdicao<br>";
		//return true;
		return $flagEdicao;
	}


/*

mysql_close();
while($row = mysql_fetch_array($rs)) {

	   // Write the value of the column FirstName (which is now in the array $row)
	  echo $row['FirstName'] . "<br />";

	  }
*/
	//$_POST["username"]
	//$_GET["name"]
	//$_SESSION["StartTime"]
	//$_SESSION["StartTime"] = date("r");
?>