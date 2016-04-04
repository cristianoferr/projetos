<?
include 'entity_inputs.php';

if (!isAdmin()){
		redirect("login.php");
		die();
}

function menuAdmin(){?>
	<link rel="stylesheet" href="css/table_style.css" type="text/css">
	<script src="js/ajaxAdmin.js" type="text/javascript"></script>
	<section class="container">
	<a href=adm_logs.php class="button-link">Logs</a>
	<a href=adm_partners.php class="button-link">Partners</a>
	<a href=adm_reports.php class="button-link">Reports</a>
	<a href=adm_usuarios.php class="button-link">Usuarios</a>
	<a href=admin/corrigeFileNotFound.php class="button-link">Corrige File not found</a>
	<a href=admin/corrigeTag.php class="button-link">Corrige Tag</a>
<?}

function admQtdUsuarios(){
	$row=executaQuerySingleRow("select count(*) as tot from usuario");
	return $row['tot'];
}

function admQtdMediaSFW(){
	$row=executaQuerySingleRow("select count(*) as tot from media where flagimproprio_media<>'T'");
	return $row['tot'];
}

function admQtdMediaNSFW(){
	$row=executaQuerySingleRow("select count(*) as tot from media where flagimproprio_media='T'");
	return $row['tot'];
}

function admQtdReports(){
	$row=executaQuerySingleRow("select count(*) as tot  from report");
	return $row['tot'];
}

function admQtdReportsOpen(){
	$row=executaQuerySingleRow("select count(*) as tot  from report where status_report='O' ");
	return $row['tot'];
}

function admQtdTags(){
	$row=executaQuerySingleRow("select count(*) as tot  from tag");
	return $row['tot'];
}

function admSumHits(){
	$row=executaQuerySingleRow("select sum(hitcount_media) as tot  from media");
	return $row['tot'];
}


function fechaReport($id){
	//echo "update report set status_report='C' where id_report=$id";
	executaSQL("update report set status_report='C' where id_report=$id");
}

?>
