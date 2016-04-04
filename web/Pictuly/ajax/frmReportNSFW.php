<?
	include '../include/include.php';

	$media=$_POST['media'].$_GET['media'];
	if(($media) ){
	validaNumero($media);
	
	executaSql("insert into report (id_media,motivo_report,id_tipo_report,reportador_report,emailreportador_report,status_report,dtabertura_report) values ($media,'Reportando NSFW. Usuario: ".usuarioAtual()."',3,'".getRealIpAddr()."','anon@anon.com','O',now())");
	
	header( "Location: http://pictuly.com");
	
}