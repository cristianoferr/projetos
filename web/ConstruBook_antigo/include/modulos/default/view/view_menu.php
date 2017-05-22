<?
	class MenuView{
		private $links;

		function __construct() {
   			//parent::__construct();
   			$this->links=array();
   		}

   		function addMenu($texto,$link,$flagVerificaLinkAtual,$flagRaiz){
   			$link=new LinkView($texto,$link,$flagVerificaLinkAtual,$flagRaiz);
			array_push($this->links,$link);
			return $link;
   		}
   		

   		function desenha(){
			$links=$this->links;
			//echo "drawInputs:".sizeOf($links);
			for ($c=0; $c<sizeOf($links);$c++){
				$link=$links[$c];
				$link->drawMenu("#".ANCORA_INICIO);
			}
		}
	}




?>