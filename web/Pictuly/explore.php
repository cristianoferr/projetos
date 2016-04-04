<?php
include 'include/include.php';

escreveHeader();

	$tag =tagAtual();
?>
<section class="container destaques">

	<?
		$controller=getMediaController();
		$mediaViewer=new MediaViewer($controller);
		//$mediaViewer->showWhatsHot($tag,0,6);
		//$mediaViewer->showWhatsNew($tag,0,6);
		$mediaViewer->showRandomStuff($tag,0,100);
		logaAcesso("Explorando","");
	?>
</section>

<?
	escreveFooter();
?>