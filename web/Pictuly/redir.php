<?
include 'include/include.php';

$media=validaNumero($_GET['media']);

$row=executaQuerySingleRow("select urloriginal_media from media where id_media=$media");
executaSQL("update partner set clickcount_partner=clickcount_partner+1 where id_partner=(select id_partner from media where id_media=$media)");
redirect($row['urloriginal_media']);
?>