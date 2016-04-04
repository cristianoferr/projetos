<?
	include '../include/include.php';

	$rating=$_POST['rating'];
	$imgId = $_POST['imgId'];
if($rating && $imgId){
	validaNumero($imgId);
	validaNumero($rating);
	//adds the rating to imgID in the database
	//creates a new row if imgID has no own row yet
	if (!existsQuery("select * from voto_media where id_media=$imgId and ip_usuario='".getRealIpAddr()."'")){
		$insert = "insert into voto_media (valor_voto,id_media,ip_usuario) values ($rating,$imgId,'".getRealIpAddr()."')";
		$result = mysql_query($insert);
	}
	//Assume OK, return some text . current rating?
	
}