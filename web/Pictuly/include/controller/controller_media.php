<?	//controller principal
	class MediaController extends BaseController{
		var $hashTag; // chave:id, valor:nome tag
		var $international;
		var $userController;

		function MediaController(){
			BaseController::BaseController();
			$this->hashTag=new Dicionario("DICT");
			$this->international=new InternationalDict();
		}

		function translateKey($key){
			return $this->international->translateKey($key);
		}
		function setDefaultLang($id_lang){
			$this->international->setDefaultLang($id_lang);
		}


		function getUserController(){
			if (!isset($this->userController)){
				$this->userController=new UserController();
			}
			return $this->userController;
		}


		function getTagNameWithId($tagId){
			
			if (!$this->hashTag->getValor($tagId)){
				validaNumero($tagId);
				$row=executaQuerySingleRow("select nm_tag from tag where id_tag=$tagId");
				$this->hashTag->setValor($tagId,$row['nm_tag']);
			} 
			//echo "getTagName: $tagId=".$this->hashTag->getValor($tagId)."!".$row['nm_tag'].":";
			return $this->hashTag->getValor($tagId);
		}

		function initSingle($id_registro){
			validaNumero($id_registro);

			$sql="select * from media e,media_foto mf where e.id_media=mf.id_media and e.id_media=$id_registro";
			$this->loadFrom($sql);
			return $this->getModelByPk($id_registro);
		}

		function getSQLLoadTag($tags,$orderBy,$startAt,$limit,$userId){
			validaTag($tags);
			$tag = explode("/", $tags);
			
			$sqlTag="(";
			for ($c = 0; $c < sizeOf($tag); $c++){
				$sqlTag.=" and m.id_media in (select id_media from media_tag where id_tag=".$tag[$c].")";
			}
			$sqlTag= str_replace("( and", "(", $sqlTag);
			$sqlTag.=")";



			$sql="select distinct m.*,mf.*,(select avg(valor_voto) from voto_media vm where vm.id_media=m.id_media) as mediaVoto";
			$sql.=" from media m,media_foto mf";
			$sql.=" where mf.id_media=m.id_media  and $sqlTag ";
			if (isset($userId)){
				$sql.=" and m.id_usuario_owner=$userId";
			}
			if (isNSFW()){
				$sql.=" and flagimproprio_media='T'";
			} else {
				$sql.=" and flagimproprio_media<>'T'";
			}
			if ($orderBy!=""){
				$sql="$sql order by $orderBy";
			}
			$sql.=" limit $startAt,$limit";
			//echo "sqlLoad: $sql";
			return $sql;
		}

		function getCurrentTags(){
			$tags=explode("/", tagAtual());
			return $tags;
		}

		function getRelatedTags($userId){
			$tags=$this->getCurrentTags();
			$sql="SELECT t.id_tag, t.nm_tag, COUNT( * ) AS tot ";
			$sql.=" FROM media m, media_tag mt, tag t ";
			$sql.=" WHERE t.id_tag = mt.id_tag ";
			$sql.=" AND mt.id_media = m.id_media ";
			if (isset($userId)){
				$sql.=" and m.id_usuario_owner=$userId";
			}

			if (isNSFW()){
				$sql.=" and flagimproprio_media='T'";
			} else {
				$sql.=" and flagimproprio_media<>'T'";
			}

			//echo $sql;

			$order="tot DESC";

			//if (sizeOf($tags)<=2)$order="rand()";


			for ($c = 0; $c < sizeOf($tags); $c++){
				if ($tags[$c]<>""){
					$sql.=" AND m.id_media IN ( SELECT id_media ";
					$sql.=" FROM media_tag mt1 ";
					$sql.=" WHERE mt1.id_tag=".$tags[$c].")";
					$sql.=" and mt.id_tag<>".$tags[$c];
				}
			}

			$sql.=" GROUP BY t.id_tag, t.nm_tag";
			$sql.=" ORDER BY $order ";
			$sql.=" limit 0,30 ";

			//echo "sql:$sql";

			$rsTags=executaQuery($sql);
			$arrTags=array();
			$c=0;
			while ($row = mysql_fetch_array($rsTags)){
				$arrTags[$c]=$row['id_tag'];

				$this->hashTag->setValor($row['id_tag'],$row['nm_tag']);
				$c++;
			}

			return $arrTags;
		}

		function initLoadFromTag($tag,$startAt,$limit){
			$tag=validaTexto($tag);

			$sql=$this->getSQLLoadTag($tag,"",$startAt,$limit);
			$this->loadFrom($sql);
		}

		function initLoadWhatsNewFromTag($tag,$startAt,$limit){
			$tag=validaTexto($tag);

			$sql=$this->getSQLLoadTag($tag,"dtCriacao_media desc",0,$limit);
			$this->loadFrom($sql);
		}

		function initLoadRandomFromTag($tag,$startAt,$limit,$userId){
			$tag=validaTexto($tag);

			$sql=$this->getSQLLoadTag($tag,"mediaVoto desc,hitcount_media desc,rand()",$startAt,$limit,$userId);
			$this->loadFrom($sql);
		}

		function initLoadWhatsHotFromTag($tag,$startAt,$limit){
			$tag=validaTexto($tag);

			$sql=$this->getSQLLoadTag($tag,"mediaVoto desc",$startAt,$limit);
			$this->loadFrom($sql);
		}

		function getDBName(){
			return "media";
		}

		function getPK(){
			return "id_media";
		}

		function carregaRegistro($row){
			$media=new Media($this);
			$media->setId($row[$this->getPK()]);
			$media->setUrlOriginal($row['urloriginal_media']);
			$media->setTextoOriginal($row['textooriginal_media']);
			$media->setNome($row['nm_media']);
			$media->setFileName($row['filename_media']);
			$media->setIdTipoMedia($row['id_tipo_media']);
			$media->setIdUsuarioUploader($row['id_usuario_owner']);
			$media->setStatusMedia($row['status_media']);
			$media->setHeightPicture($row['heightpicture_media']);
			$media->setWidthPicture($row['widthpicture_media']);
			$media->setDtCriacao($row['dtcriacao_media']);
			$media->setFlagNSFW($row['flagimproprio_media']);
			
			return $media;
		}
	}

?>