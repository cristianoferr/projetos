<?class MediaViewerSingle extends MediaViewer{
	function singleShow(){
		$this->lista($this->controller);
	}

	function showMedia($media){
	?>
		<h2 class="titulo"><?echo $media->getNome();?></h2>
		<div>
			<table border=1>
				<tr>
					<td class='cabecalho infos' width=15%>
						<ul>
							<li><?echo translateKey("txt_media_tags");?>: <?$this->listMediaTags($media);?></li>
							<li><?echo translateKey("txt_media_resolution");?>: <span class='label subTitulo'><?echo $media->getWidthPicture();?> x <?echo $media->getHeightPicture();?></span></li>
							<li><?echo translateKey("txt_media_owner");?>: <a href='owner.php?id=<?echo $media->getIdUsuarioUploader();?>'><span class='label'><?echo $media->getOwnerName();?></span></a></li>
							<li><?echo translateKey("txt_media_uploaded_date");?>: <span class='label label-default subTitulo'><?echo $media->getDtCriacaoFmt();?></span></li>
							<li><?echo translateKey("txt_media_hits");?>: <span class='label label-default subTitulo'><?echo $media->addVisit();?></span></li>
							<?
							if ($media->getUrlOriginal()!=""){?>
								<li><?echo translateKey("txt_media_as_seen_in");?>: <span class='label'><a rel="nofollow" href="redir.php?media=<?echo $media->getId();?>"><?echo $media->getTextoOriginal();?></a></span></li>
							<?}?>

							<li><?$this->formSuggestTag($media);?></li>

							</ul><ul style="vertical-align:bottom;">
							<?if ($media->getFlagNSFW()=="F"){?>
								<li><?$this->formReportNSFW($media);?></li>
							<?}?>
							<li><?$this->formRequestOwnerShip($media);?></li>
						</ul>
					</td>
					<td>

						<div class='mediaimage '>
							<a href="<?echo $media->getUrl();?>"><img src='<?echo $media->getUrl();?>' title='<?echo $media->getNome();?>' alt='<?echo $media->getNome();?>'/></a>
						</div>
					</td>
				</tr>
			</table>
			
		</div>
	<?
	}

	function formSuggestTag($media){
		?>	<form action=ajax/frmTagSuggestion.php method=post>
				<input type=hidden name=media value=<?echo $media->getId();?>>
				<input type=text class="smallTextArea" required  name=tag value="" placeholder="<?echo translateKey("txt_media_suggest_tag_placeholder");?>"><br>
				<input type=submit class="button-link"  value="<?echo translateKey("txt_media_suggest_tag");?>">
			</form><?
	}

	function formReportNSFW($media){
		?>	<a rel="nofollow" class="button-link importante hint--bottom" data-hint="<?echo translateKey("desc_report_improprio_menores");?>" onclick="return confirmNSFW();" href="ajax/frmReportNSFW.php?media=<?echo $media->getId();?>"><?echo translateKey("report_improprio_menores");?></a>
			
			<script>
			function confirmNSFW() {  
			   return (confirm("<?echo translateKey("desc_report_improprio_menores");?>")) ;
			}
   			</script>

<?
	}

	function formRequestOwnerShip($media){
		?>	<a rel="nofollow" class="button-link importante hint--bottom" data-hint="<?echo translateKey("desc_report_owner");?>" href="reclaimOwnership.php?media=<?echo $media->getId();?>"><?echo translateKey("report_owner");?></a>

<?
	}

	function listMediaTags($media){
		$arrTags=$media->getTags();
		for ($c = 0; $c < sizeOf($arrTags); $c++){
			?><a class="tagList" href=explore.php?tag=<?echo $arrTags[$c];?>><?echo $this->controller->getTagNameWithId($arrTags[$c]);?></a><?
		}
	}


}
