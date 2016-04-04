<?
/*				  
$eds=new EntidadeDS;
$eds->initEntidade($id_entidade);
echo "val=".$eds->getCampo($arrColunas[1]['nm_coluna']);
*/
//$a->foo(); metodo do objeto
//A::foo(); metodo estatico

	class  BaseModel{
		var $id;
		var $controller;

		function BaseModel($controller){ 
		    $this->controller=$controller; 
		} 

		
		function setId($id){
			$this->id=$id;
		}
		function getId(){
			return $this->id;
		}

		
	}

	class Tag extends BaseModel{
		var $nome;

		//Getters and setters
		function setNome($nome){
			$this->nome=$nome;
		}
		function getNome(){
			return $this->nome;
		}		
	}

	class Media extends BaseModel{
		var $nome;
		var $urlOriginal;
		var $textoOriginal;
		var $fileName;
		var $idTipoMedia;
		var $tipoMedia; //lazy
		var $idUsuarioUploader;
		var $ownerName;//lazy
		var $statusMedia;
		var $dtCriacao;

		var $mediaVoto;
		var $contaVoto;
		var $flagNSFW;
		
		//exclusivo para imagem
		var $heightPicture;
		var $widthPicture;

		function Media(){
			$this->mediaVoto=-1;
			$this->contaVoto=-1;
		}

		function getUrl(){
			return UPLOAD_FOLDER_NORMAL.$this->fileName;
		}
		function getUrlMini(){
			return UPLOAD_FOLDER_MINI.$this->fileName;
		}
		

		function getMediaVoto(){
			if ($this->mediaVoto>-1)return $this->mediaVoto;
			$row=executaQuerySingleRow("select avg(valor_voto) as tot from voto_media vm where vm.id_media=".$this->getId());
			if ($row){
				$this->mediaVoto=$row['tot'];
				return $this->mediaVoto;
			}
			return 3;
		}

		//retorna 0 se não, 1 se sim
		function isFavorited(){
			$id_usuario=usuarioAtual();
			if (!$id_usuario)return 0;
			$id_tag=TagController::getIdTag("favorite");

			$ret="no";
			$varname="MEDIA_FAV_".$this->getId()."_".$id_tag;
			
			if ($_SESSION[$varname]){
				$ret=$_SESSION[$varname];
			} else{
				if (existsQuery("select * from media_tag_usuario where id_media=".$this->getId()." and id_usuario=$id_usuario and id_tag=$id_tag")) {
					$ret=1;
				}
				$_SESSION[$varname]=$ret;
			}
			if ($ret="no") return 0;
			return $ret;
		}

		function getQtdVotos(){
			if ($this->contaVoto>-1)return $this->contaVoto;
			$row=executaQuerySingleRow("select count(valor_voto) as tot from voto_media vm where vm.id_media=".$this->getId());
			if ($row){
				$this->contaVoto=$row['tot'];
				return $row['tot'];
			}
			return 0;
		}

		function getTagsRS(){
			$sql="select * from media_tag tm,tag t where tm.id_tag=t.id_tag and tm.id_media=".$this->getId()." order by nm_tag";
			//echo $sql;
			return executaQuery($sql);
		}

		//retorna array
		function getTags(){
			$varname="MEDIA_ARR_TAGS_".$this->getId();
			if ($_SESSION[$varname]){
				return $_SESSION[$varname];
			}

			$rs=$this->getTagsRS();
			$ret=array();
			$count=0;
			while ($row = mysql_fetch_array($rs)){
				$ret[$count]=$row['id_tag'];
				$count++;
			}
			$_SESSION[$varname]=$ret;
			return $ret;
		}

		function getTagsComma(){
			$ret="#";
			$varname="MEDIA_TAGS_".$this->getId();
			if ($_SESSION[$varname]){
				return $_SESSION[$varname];
			}

			$rs=$this->getTagsRS();

			while ($row = mysql_fetch_array($rs)){
				$ret.=" ; ".$row['nm_tag'];
			}
			$ret= str_replace("# ;", "", $ret);
			$_SESSION[$varname]=$ret;
			return $ret;
		}


		function addVisit(){
			//echo "addvisit:".'HITS_'.$this->getId().":".$_SESSION['HITS_'.$this->getId()]."=";
			if (!isset($_SESSION['ADD_HITS_'.$this->getId()])) {
				executaSQL("update media set hitcount_media=hitcount_media+1 where id_media=".$this->getId());
				$_SESSION['ADD_HITS_'.$this->getId()]=true;
			}
			return $this->getVisits();
		}

		function getVisits(){
			//echo "visits:".'HITS_'.$this->getId().":".$_SESSION['HITS_'.$this->getId()]."=";
			if (isset($_SESSION['HITS_'.$this->getId()])) 
				return $_SESSION['HITS_'.$this->getId()];
			$row=executaQuerySingleRow("select hitcount_media from media m where m.id_media=".$this->getId());
			$_SESSION['HITS_'.$this->getId()]=$row['hitcount_media'];
			//echo " novo ";
			return $row['hitcount_media'];
		}

		function getOwnerName(){
			if ($this->ownerName)return $this->ownerName;
			$row=executaQuerySingleRow("select nm_usuario from usuario u,media m where m.id_usuario_owner=u.id_usuario and m.id_media=".$this->getId());
			if ($row){
				$this->ownerName=$row['nm_usuario'];
				return $row['nm_usuario'];
			}
		}

		function getMyTags(){
			$rs=executaQuery("select * from media_tag_usuario tm,tag t where tm.id_usuario=".usuarioAtual()." and tm.id_tag=t.id_tag and tm.id_media="&$this->getId());
			$ret=array();
			$count=0;
			while ($row = mysql_fetch_array($rs)){
				$ret[$count]=$row['id_tag'];
				$count++;
			}
			return $ret;
		}

	//Getters and setters
		

		function setFlagNSFW($flagNSFW){
			$this->flagNSFW=$flagNSFW;
		}
		function getFlagNSFW(){
			return $this->flagNSFW;
		}

		function setNome($nome){
			$this->nome=$nome;
		}
		function getNome(){
			return $this->nome;
		}

		function setFileName($fileName){
			$this->fileName=$fileName;
		}
		function getFileName(){
			return $this->fileName;
		}

		function setUrlOriginal($urlOriginal){
			$this->urlOriginal=$urlOriginal;
		}
		function getUrlOriginal(){
			return $this->urlOriginal;
		}

		function setTextoOriginal($textoOriginal){
			$this->textoOriginal=$textoOriginal;
		}
		function getTextoOriginal(){
			return $this->textoOriginal;
		}
		

		function setIdTipoMedia($idTipoMedia){
			$this->idTipoMedia=$idTipoMedia;
		}
		function getIdTipoMedia(){
			return $this->idTipoMedia;
		}

		function setIdUsuarioUploader($idUsuarioUploader){
			$this->idUsuarioUploader=$idUsuarioUploader;
		}
		function getIdUsuarioUploader(){
			return $this->idUsuarioUploader;
		}

		function setStatusMedia($statusMedia){
			$this->statusMedia=$statusMedia;
		}
		function getStatusMedia(){
			return $this->statusMedia;
		}

		function setDtCriacao($dtCriacao){
			$this->dtCriacao=$dtCriacao;
		}

		function getDtCriacao(){
			return $this->dtCriacao;
		}

		
		function getDtCriacaoFmt(){
			$mysqldate = date("d-M-Y", strtotime($this->getDtCriacao()));
			return $mysqldate;
		}

		function setHeightPicture($heightPicture){
			$this->heightPicture=$heightPicture;
		}
		function getHeightPicture(){
			return $this->heightPicture;
		}

		function setWidthPicture($widthPicture){
			$this->widthPicture=$widthPicture;
		}
		function getWidthPicture(){
			return $this->widthPicture;
		}
	}

?>