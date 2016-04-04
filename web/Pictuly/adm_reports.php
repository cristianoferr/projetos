<?php
	include 'include/include.php';
	include 'include/include_admin.php';

	
	$pagina="adm_reports.php";
	$table="report";

	escreveHeader(false);

	menuAdmin();

	if ($_GET["acao"]=='remove'){
		$id=$_GET["id"];
		executaSQL("delete from $table where id_$table=$id");
		redirect(getRedirLink());
	}

	if($_GET['acao']=="close"){
		fechaReport($_GET["id"]);
		redirect(getRedirLink());
	}


	if($_GET['acao']=="setnsfw"){
		$mediaController=getMediaController();
		$id_media=$_GET['media'];
		executaSQL("update media set flagimproprio_media='T' where id_media=$id_media");
		fechaReport($_GET["report"]);
		redirect(getRedirLink());
	}
	
	if($_GET['acao']=="setsfw"){
		$mediaController=getMediaController();
		$id_media=$_GET['media'];
		executaSQL("update media set flagimproprio_media='N' where id_media=$id_media");
		fechaReport($_GET["report"]);
		redirect(getRedirLink());
	}

?>

<section class="container destaque">
		
	<h1 class="titulo">Reports</h1>
	<?
	mostraRegistros();

?>
</section>


<? 

function mostraRegistros(){
	$table=$GLOBALS['table'];
	$pagina=$GLOBALS['pagina'];
	$rs=executaQuery("select * from $table r,tipo_report tr where r.id_tipo_report=tr.id_tipo_report order by id_$table desc");

?>
	<table border=0>
	<tr>

			<th scope="col" class="nobg">#</th>
			<th scope="col" >ID</th>
			<th scope="col" >Media</th>
			<th scope="col" >Motivo</th>
			<th scope="col" >Tipo Report</th>
			<th scope="col" >Reportador</th>
			<th scope="col" >Email</th>
			<th scope="col" >Status</th>
			<th scope="col" >Data</th>
			<th scope="col" ></th>
		</th>
		<?
		$pos=0;
		while ($row= mysql_fetch_array($rs)){
			$id=$row["id_$table"];
			?>
			<tr>
				<th scope="row" class="specalt"><? echo $pos+1;?></th>
				<td><?echo $id;?></td>

				<td><a href=media.php?media=<?echo $row['id_media'];?>><?echo $row['id_media'];?></a></td>
				<?escreveCelulaGenericaAjax("motivo_report",$id,$row["motivo_report"],$table,"T");?>
				<td><?echo $row['nm_tipo_report']?></td>
				<td><?echo $row['reportador_report']?></td>
				<td><?echo $row['emailreportador_report']?></td>
				<td><?$status=$row['status_report'];
				if ($status=="O") echo "Aberto";
				if ($status=="C") echo "Fechado";
				?></td>
				<td><?echo $row['dtabertura_report']?></td>


				<td width=30%>	<form action=<?echo $pagina;?> onsubmit="return confirmExclusao();">
							<input type=hidden name=acao value=remove>
							<input type=hidden name=id value=<?echo $id;?>>
							<input class="button-link" type=submit value=Remover>
						</form>
						<?if ($status=="O"){
							if ($row['id_tipo_report']==3){
								?><a class="button-link" href=adm_reports.php?acao=setnsfw&media=<?echo $row['id_media'];?>&report=<?echo $id;?>>Seta NSFW e fecha</a><?
								?><a class="button-link" href=adm_reports.php?acao=setsfw&media=<?echo $row['id_media'];?>&report=<?echo $id;?>>Seta SFW e fecha</a><?
							}
							?><a class="button-link" href=adm_reports.php?acao=close&report=<?echo $id;?>>Fechar</a><?
						}?>

				
				</td>
				
			</tr>
			<?$pos++;
		}?>
		
	<table>
	<?}

function getRedirLink(){
	$pagina=$GLOBALS['pagina'];
	return $pagina;
}

escreveFooter(); ?>
