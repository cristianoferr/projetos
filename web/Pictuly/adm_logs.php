<?php
	include 'include/include.php';
	include 'include/include_admin.php';

	if (!isAdmin()){
		redirect("login.php");
	}
	$pagina="adm_logs.php";
	$table="log_site";

	escreveHeader(false);

	menuAdmin();
?>




<?
	


	if ($_GET["acao"]=='remove'){
		$id=$_GET["id"];
		executaSQL("delete from $table where id_$table=$id");
		redirect(getRedirLink());
	}


?>

<section class="container destaque">
		
	<h1 class="titulo">Usuários</h1>
	<?
	mostraRegistros();

?>
</section>


<? 

function mostraRegistros(){
	$table=$GLOBALS['table'];
	$pagina=$GLOBALS['pagina'];
	$sql="select * from $table where id_usuario<>1 and id_usuario<>2 order by id_log_site desc limit 0,200";
	//echo $sql;
	$rs=executaQuery($sql);

?>
	<table border=0>
	<tr>

			<th scope="col" class="nobg">#</th>
			<th scope="col" >ID</th>
			<th scope="col" >Data</th>
			<th scope="col" >Media</th>
			<th scope="col" >Usuario</th>
			<th scope="col" >IP</th>
			<th scope="col" >Pagina</th>
			<th scope="col" >Desc</th>
			<th scope="col" >NSFW?</th>
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
				<td><?echo $row['data_log_site']?></td>
				<td><?if ($row['id_media']>0){?>
				<a href=media.php?media=<?echo $row['id_media']?>><?echo $row['id_media']?></a></td>
				<?}?>
				<td><?echo $row['id_usuario']?></td>
				<td><?echo $row['ip_log_site']?></td>
				<td><a href=<?echo $row['pagina_log_site']?>><?echo $row['pagina_log_site']?></a></td>
				<td><?echo $row['desc_log_site']?></td>
				<td><?echo $row['flagnsfw_log_site']?></td>

				<td>	<form action=<?echo $pagina;?> onsubmit="return confirmExclusao();">
							<input type=hidden name=acao value=remove>
							<input type=hidden name=id value=<?echo $id;?>>
							<input class="button-link" type=submit value=Remover>
						</form>
				
				</td>
				
			</tr>
			<?$pos++;
		}?>
		
	</table>
	<?}

function getRedirLink(){
	$pagina=$GLOBALS['pagina'];
	return $pagina;
}

escreveFooter(); ?>
