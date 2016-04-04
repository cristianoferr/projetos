<?php
	include 'include/include.php';
	include 'include/include_admin.php';

	if (!isAdmin()){
		redirect("login.php");
	}
	$pagina="adm_partners.php";
	$table="partner";

	escreveHeader(false);

	menuAdmin();
?>




<?
	
	if ($_GET["acao"]=='novo'){
		executaSQL("insert into $table (id_partner,nm_partner,clickcount_partner) values (".getNext("id_$table",$table).",'',0)");
		redirect(getRedirLink());
	}

	if ($_GET["acao"]=='remove'){
		$id=$_GET["id"];
		executaSQL("delete from $table where id_$table=$id");
		redirect(getRedirLink());
	}


?>

<section class="container destaque">
		
	<h1 class="titulo">Partners</h1>
	<?
	mostraRegistros();

?>
</section>


<? 

function mostraRegistros(){
	$table=$GLOBALS['table'];
	$pagina=$GLOBALS['pagina'];
	$rs=executaQuery("select u.*,(select count(*) from media m where m.id_partner=u.id_partner) as tot  from $table u order by id_$table");

?>
	<table border=0>
	<tr>

			<th scope="col" class="nobg">#</th>
			<th scope="col" >ID</th>
			<th scope="col" >Partner</th>
			<th scope="col" >Url</th>
			<th scope="col" >Total Media</th>
			<th scope="col" >Total Click</th>
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

				<?escreveCelulaGenericaAjax("nm_$table",$id,$row["nm_$table"],$table,"T");?>
				<?escreveCelulaGenericaAjax("url_$table",$id,$row["url_$table"],$table,"T");?>
				<td><?echo $row['tot']?></td>
				<td><?echo $row['clickcount_partner']?></td>

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
	<a href=adm_partners.php?acao=novo class="button-link" style="float:left">Novo</a>
	<?}

function getRedirLink(){
	$pagina=$GLOBALS['pagina'];
	return $pagina;
}

escreveFooter(); ?>
