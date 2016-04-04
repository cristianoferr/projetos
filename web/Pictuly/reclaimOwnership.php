<?php
	include 'include/include.php';
	escreveHeader();

	$tag =tagAtual();


	if ($_POST['acao']=="incluir"){
		$name=validaTexto($_POST['name']);
		$media=$_POST['media'];
		validaNumero($media);
		$email=$_POST['email'];
		$original_link=$_POST['original_link'];
		$action_taken=validaTexto($_POST['action_taken']);
		$observations=validaTexto($_POST['observations']);

		executaSql("insert into report (id_media,motivo_report,id_tipo_report,reportador_report,emailreportador_report,status_report,dtabertura_report) values ($media,'Reclamando ownership. nome: $name,email: $email, link: $original_link, acao: $action_taken,Usuario: ".usuarioAtual().",Obs: $observations',1,'".getRealIpAddr()."','$email','O',now())");
		echo "<section class=container>Your report was created.</section>";
	}

	if ($_POST['acao']==""){
?>
<section class="container">

	<form class="form1" action=reclaimOwnership.php method=post>
		<div class="formtitle titulo">Reclaiming Ownership</div>
		<h2 class=subTitulo>Please, in order to attest your ownership over the requested file, please fill the following details.  </h2>

		<input type=hidden name=media value=<?echo $_GET['media'];?>>
		<input type=hidden name=acao value="incluir">
		<div class="input">
			<div class="inputtext">Your name: </div>
					<div class="inputcontent">
						<input required type=text name="name">
					</div>
			</div>


			<div class="input nobottomborder">
				<div class="inputtext">Contact email: </div>
				<div class="inputcontent">
					<input required type=email name="email">
				</div>
			</div>

			<div class="input nobottomborder">
				<div class="inputtext">Original Link: </div>
				<div class="inputcontent">
					<input required type=text name="original_link">
				</div>
			</div>

			<div class="input nobottomborder">
				<div class="inputtext">Which action do you want taken?</div>
				<div class="inputcontent">
					<select required name=action_taken>
					<option></option>
					<option value="Remove">Remove from this site</option>
					<option value="LinkBack">Link Back to your site/url as informed above</option>
				</select>
				</div>
			</div>
			
			<div class="input nobottomborder">
				<div class="inputtext">Anything else?</div>
				<div class="inputcontent">
					<textarea name=observations rows=15 cols=60></textarea>
				</div>
			</div>
			<div class="buttons">
					<input class="orangebutton" type="submit" value="Submit for review" />
			</div>

	</form>
	

</section>

<?
}
	escreveFooter();
?>