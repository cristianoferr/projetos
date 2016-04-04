<?php
	include 'include/include.php';
	include 'include/include_admin.php';

	if (!isAdmin()){
		redirect("login.php");
	}

	
	escreveHeader(false);

	menuAdmin();

	$sfw=admQtdMediaSFW();
	$nsfw=admQtdMediaNSFW();
	$totMedia=$sfw+$nsfw;
?>
	<div class="titulo">Estatísticas</div>
	<div class="subTitulo">Usuários: <?echo admQtdUsuarios();?></div>
	<div class="subTitulo">Media: <? echo "<font color=red>$nsfw</font>+<font color=green>$sfw</font> = $totMedia";?></div>
	<div class="subTitulo">Reports: <?echo admQtdReports();?>.  Abertos: <?echo admQtdReportsOpen();?></div>
	<div class="subTitulo">Tags: <?echo admQtdTags();?></div>
	<div class="subTitulo">Hits: <?echo admSumHits();?></div>
	
</section>

<?
	escreveFooter();
?>