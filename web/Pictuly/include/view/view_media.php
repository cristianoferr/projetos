<?
class MediaViewer{
	var $controller;
	var $mediaList;
	var $linkPaginaGaleria;

	var $linkAtual;

	function MediaViewer($controller){
		$this->controller=$controller;
	}

	function beginList($int_txt,$url_link,$userId){
		$this->linkPaginaGaleria=$url_link;
		$this->mediaList="";




		?><h2 class="titulo"><?echo $int_txt;?></h2>
		<?$this->listTagsWithLink($url_link);
		$this->listRelatedTagsWithLink($url_link,$userId);?>
		<div id="img_thumbs">
			<div class="mediathumb_row">
		<?
	}

	function listTagsWithLink($url_link){
		$tags=$this->controller->getCurrentTags();
		$tagAtual="/".tagAtual()."/";

		$c=0;
		?><h2 class="subTitulo"><?echo translateKey("txt_browsing_tags");?>: <?
		for ($c = 0; $c < sizeOf($tags); $c++){
			//removo a tag da iteracao da relacao de tags
			$link= "/".str_replace($tags[$c]."/", "", $tagAtual);
			//if (substr($link,0)=="/")$link=substr($link,1);
			$link=rtrim($link, "/");
			$link=ltrim($link, "/");

			if ($link=="") $link="51";


			echo "<a href='$url_link".$link."' class='tagRemove'>".$this->controller->getTagNameWithId($tags[$c])."</a>";
		}
		?></h2><?
	}

	function listRelatedTagsWithLink($url_link,$userId){
		$tags=$this->controller->getRelatedTags($userId);
		$tagAtual=tagAtual();
		$c=0;
		?><h2 class="subTitulo espacador"><?echo translateKey("txt_related_tags");?>: <?
		for ($c = 0; $c < sizeOf($tags); $c++){
			$link= $tagAtual."/".$tags[$c];
			echo "<a href='$url_link".$link."' class='tagAdd'>".$this->controller->getTagNameWithId($tags[$c])."</a> ";
		}
		?></h2><?
	}

	function showMedia($media){
		
		?><span class="mediathumb_brdr" id="th_<?echo $media->getId();?>" data-id="<?echo $media->getId();?>">
			<a href="media.php?media=<?echo $media->getId()?>">
				<span class="mediathumb" title='<?echo $media->getNome();?>' style="background-image:url(&#39;<?echo $media->getUrlMini();?>&#39;);">
					<img src="<?echo $media->getUrlMini();?>" alt="<?echo $media->getNome();?>" title="<?echo translateKey("txt_media_tags");?>: <? echo $media->getTagsComma();?>" class="invisivel">
				</span>
				<?if (usuarioAtual()){?>
					<div id="rate<?echo $media->getId();?>" class='ratingGaleria rating'>&nbsp;</div>
					<script>$('#rate<?echo $media->getId();?>').rating('ajax/toggleFavourite.php?media=<?echo $media->getId();?>', {maxvalue:1, curvalue:<?echo $media->isFavorited();?>});</script>
				<?}?>
			</a>
			<?$auxTags=$media->getTagsComma();
			if (strlen($auxTags)>60){
				$auxTags=substr($auxTags,0,60)." ...";
			}
			?>
			<div class='legenda hint--bottom' data-hint="<?echo translateKey("txt_media_hits");?>: <?echo $media->getVisits();?>   <?echo translateKey("txt_media_tags");?>: <? echo $auxTags;?>" ><?echo substr($media->getNome(),0,20);?></div> 
		</span>

		<?
	}

	function endList(){
		
		?>	
			</div>	
		</div>	
		<?
	}

	
	function showWhatsHot($tag,$startAt,$limit){
		$this->linkAtual="whatsHot.php?tag=".$tag;
		$this->controller->initLoadWhatsHotFromTag($tag,$startAt,$limit);	
		$this->beginList(translateKey("txt_whatsHot"),"whatsHot.php?tag=");
		$this->lista($this->controller);
	}

	function showWhatsNew($tag,$startAt,$limit){
		$this->linkAtual="whatsNew.php?tag=".$tag;
		$this->controller->initLoadWhatsNewFromTag($tag,$startAt,$limit);	
		$this->beginList(translateKey("txt_whatsNew"),"whatsNew.php?tag=");
		$this->lista($this->controller);
	}

	function showRandomStuff($tag,$startAt,$limit){
		$this->linkAtual="explore.php?tag=".$tag;
		$this->controller->initLoadRandomFromTag($tag,$startAt,$limit,null);	
		$this->beginList(translateKey("txt_RandomStuff"),"explore.php?tag=",null);
		$this->lista($this->controller);
	}

	
	function showUserMedia($owner,$tag,$startAt,$limit){
		$this->controller->initLoadRandomFromTag($tag,$startAt,$limit,$owner);	
		$this->linkAtual="owner.php?id=$owner&tag=".$tag;
		$userName=$this->controller->getUserController()->getUserName($owner);
		$this->beginList(translateKey("txt_user_stuff")." ".$userName,"owner.php?id=$owner&tag=",$owner);
		$this->lista($this->controller);
	}


	function lista($controller){
		$c=0;
		while ($media=$this->controller->next()){
			$this->showMedia($media);
			$c++;
		}

		$this->endList();
		if ($c==0){
			?><h2 class="subTitulo"><?echo translateKey("txt_no_results"); 
				if (isNSFW()!="T"){
					?>    <br><a href="http://nsfw.pictuly.com/<?echo $this->linkAtual;?>" class="link"><?echo translateKey("txt_recommend_nsfw");?></a><?
				}?> </h2><?
		}
	}



	
}



class MediaViewerGallery extends MediaViewer{

	

	function endList(){
		MediaViewer::endList();
		?><iframe id="frameMedia" class="mediaFrame" src="index.html">
		</iframe><?
	}
		

	function getLinkMedia(){
		return "showMedia('media.php?media='+ imagesJSON[currImages + i].id);";
	}

	function initializeScrollable(){
		return '$(".scrollable").scrollable();';
	}	
}
?>