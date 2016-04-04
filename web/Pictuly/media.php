<?php
	include 'include/include.php';
	escreveHeader();

	$tag =tagAtual();
?>
<section class="container">

	<?
		$controller=getMediaController();
		$mediaViewer=new MediaViewerSingle($controller);
		//$mediaViewer->showWhatsHot($tag,0,6);
		//$mediaViewer->showWhatsNew($tag,0,6);
		$id_media=$_GET['media'];
		validaNumero($id_media);
		$controller->initSingle($id_media);
		$mediaViewer->singleShow();
		logaAcesso("Mostrando Media",$id_media);
	?>
</section>

<?
	escreveFooter();
?>