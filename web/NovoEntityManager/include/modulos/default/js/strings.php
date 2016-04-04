<?
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
inicializa();


?>function getStringWithId(idStr){
<?

defineStringJS(ERROR_ID_EXISTS,"ERROR_ID_EXISTS");
defineStringJS(ERROR_EMAIL_EXISTS,"ERROR_EMAIL_EXISTS");


function defineStringJS($str,$key){
	echo "if (idStr==$str) return '".translateKey($key)."';".PHP_EOL;	
}

?>
}