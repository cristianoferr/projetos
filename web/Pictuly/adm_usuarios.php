<?php
	include 'include/include.php';
	include 'include/include_admin.php';

	if (!isAdmin()){
		redirect("login.php");
	}
	$pagina="adm_usuarios.php";
	$table="usuario";

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
	$rs=executaQuery("select u.*,(select count(*) from media m where m.id_usuario_owner=u.id_usuario) as tot  from $table u order by id_$table");

?>
	<table border=0>
	<tr>

			<th scope="col" class="nobg">#</th>
			<th scope="col" >ID</th>
			<th scope="col" >Nome</th>
			<th scope="col" >Email</th>
			<th scope="col" >Senha</th>
			<th scope="col" >Total</th>
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
				<?escreveCelulaGenericaAjax("email_$table",$id,$row["email_$table"],$table,"T");?>
				<?escreveCelulaGenericaAjax("senha_$table",$id,'',$table,"T");?>
				<td><?echo $row['tot']?></td>

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
