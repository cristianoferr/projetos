<?
	include '../include/include.php';

	$media=$_POST['media'].$_GET['media'];
	if(($media) ){
	validaNumero($media);
	$tag=validaTexto($_POST['tag']);
	
	executaSql("insert into report (id_media,motivo_report,id_tipo_report,reportador_report,emailreportador_report,status_report,dtabertura_report) values ($media,'Sugestao de tag ($tag). Usuario: ".usuarioAtual()."',4,'".getRealIpAddr()."','anon@anon.com','O',now())");
	
	header( "Location: http://pictuly.com/media.php?media=$media");
	
}