<?php
	include 'include/include.php';

	verificaUsuarioLogado();
	
	escreveHeader();

	$tag =tagAtual();
?>
<section class="container">

	<?
		$owner=usuarioAtual();
		$controller=getMediaController();
		$mediaViewer=new MediaViewer($controller);
		//$mediaViewer->showWhatsHot($tag,0,6);
		//$mediaViewer->showWhatsNew($tag,0,6);
		$mediaViewer->showUserMedia($owner,$tag,0,50);
	?>
</section>

<?
	escreveFooter();
?>