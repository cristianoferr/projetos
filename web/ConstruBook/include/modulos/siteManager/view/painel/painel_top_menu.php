<?

/*
  Classe responsável pelo tratamento do painel superior, não é um painel típico, não recebendo controller, sendo mais manual
 */

class ItemMenu_TM {

    private $urlAdd, $key, $url, $valor, $classMenu, $qtd, $onClick;

    function __construct($key, $url, $classMenu = null, $onClick = null) {
        $this->key = $key;
        $this->url = $url;
        $this->classMenu = $classMenu;
        $this->onClick = $onClick;
    }

    function setUrlAdd($urlAdd) {
        $this->urlAdd = $urlAdd;
    }

    function setQuantidade($qtd) {
        $this->qtd = $qtd;
    }

    function setValor($valor) {
        $this->valor = $valor;
    }

    function desenhaSeparador(ElementMaker $elUl) {
        $elRoot = new ElementMaker("li", $elUl);
        $elRoot->setClass("divider")->setValue();
        ;

        //$elPresentation = $elRoot;
        $elPresentation = elMaker("li", $elUl);
        $elPresentation->setValue();
        if ($this->url) {
            $elPresentation->setAtributo("role", "presentation")->setclass("dropdown-header");
            if ($this->classMenu) {
                $elSpan = new ElementMaker("span", $elPresentation);
                $elSpan->setClass($this->classMenu)->setValue(" ");
            }
            $elPresentation->setValue($this->url);
        }
    }

    function desenha(ElementMaker $elUl) {

        if ($this->key == "--") {
            $this->desenhaSeparador($elUl);
        } else {
            $elRoot = new ElementMaker("li", $elUl);

            $elLink = new ElementMaker("a", $elRoot);

            $elLink->setAtributo("href", $this->url);
            $elLink->setAtributo("onclick", $this->onClick);

            if ($this->classMenu) {
                $elSpan = new ElementMaker("span", $elLink);
                $elSpan->setClass($this->classMenu)->setValue(" ");
            }

            $elTexto = new ElementMaker("span", $elLink);
            $elTexto->setValue($this->key);

            if ($this->qtd) {
                $elQtd = new ElementMaker("span", $elLink);
                $elQtd->setClass("badge")->setValue($this->qtd);
            }
        }
    }

}


class Busca_TM{
    private $texto;
    private $action;
    function __construct($texto, $action) {
        $this->texto=$texto;
        $this->action=$action;
    }
   
    function desenha(ElementMaker $elUl) {
        if ($this->current) {
            $current = "active";
        }
       // $elLi = elMaker("li", $elUl);
        
        $elDiv = elMaker("div", $elUl)->setClass("col-sm-3 col-md-3 pull-left");
        
        $elForm = elMaker("form", $elDiv)->setClass("navbar-form")->setAtributo("role","search")->setAction($this->action);
        
        $eldivF = elMaker("div", $elForm)->setClass("input-group");
        $elInput = elMaker("input", $eldivF)->setType("text")->setClass("form-control search-control")->setAtributo("placeholder", $this->texto);
        $elSubD = elMaker("div", $eldivF)->setClass("input-group-btn");
        $elButton = elMaker("button", $elSubD)->setClass("btn btn-default search-button")->setType("submit");
        $elIcon = elMaker("i", $elButton)->setClass("glyphicon glyphicon-search");
       
      /*  <form class="navbar-form" role="search">
        <div class="input-group">
            <input type="text" class="form-control" placeholder="Search" name="srch-term" id="srch-term">
            <div class="input-group-btn">
                <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
            </div>
        </div>
        </form>*/
    }
}

class Menu_TM {

    private $arrItems;
    private $key;
    private $url, $classMenu;

    function count() {//coberto
        return sizeof($this->arrItems);
    }

    function __construct($key, $url = null, $classMenu) {//coberto
        $this->arrItems = array();
        $this->key = $key;
        $this->url = $url;
        $this->classMenu = $classMenu;
    }

    function addSeparador($txt = null) {//coberto
        $menu = new ItemMenu_TM('--', $txt);
        array_push($this->arrItems, $menu);
        return $menu;
    }

    function addOpcao($key, $url, $classMenu = null, $onClick = null) {
        $menu = new ItemMenu_TM($key, $url, $classMenu, $onClick);
        array_push($this->arrItems, $menu);
        return $menu;
    }

    function addOpcaoComAdd($key, $url, $urlAdd, $classMenu = null) {
        $menu = new ItemMenu_TM($key, $url, $classMenu);
        $menu->setUrlAdd($urlAdd);
        array_push($this->arrItems, $menu);
        return $menu;
    }

//usado para mostrar um número à direita do texto, como quantidades de mensagens e/ou notificações que o usuário possui
    function addOpcaoComValor($key, $url, $valor) {
        $menu = new ItemMenu_TM($key, $url);
        $menu->setValor($valor);
        array_push($this->arrItems, $menu);
        return $menu;
    }

    function desenha(ElementMaker $elUl) {
        if ($this->current) {
            $current = "active";
        }

        $elLi = elMaker("li", $elUl);
        if ($this->url) {
            $elLi->addElement(Out::linkG($this->url, $this->key, null, $current));
        } else {
            $elLi->setClass("$current dropdown");

            //criando o link
            $url = $this->url;
            if (!$url) {
                $url = "#";
            }
            $link = elMaker("a", $elLi)->setHref($url)->setClass("dropdown-toggle")->setAtributo("data-toggle", "dropdown");
            if ($this->classMenu) {
                $elSpan = elMaker("span", $link)->setClass($this->classMenu);
            }
            $elDiv = elMaker("", $link)->setValue($this->key);
            $elB = elMaker("b", $link)->setClass("caret")->setValue();

            //criando os itens
            $elUl = elMaker("ul", $elLi)->setClass("dropdown-menu");
            for ($c = 0; $c < sizeof($this->arrItems); $c++) {
                $item = $this->arrItems[$c];
                $item->desenha($elUl);
            }
        }
    }

}

class TopMenuPainel {

    private $arrMenu;
    private $arrMenuSecundario;
    private $titulo;
    private $urlTitulo;
    private $elRoot, $elContainer;
    
    private $busca;
    private $buscaSecundaria;

    function __construct($titulo, $urlTitulo) {
        $this->arrMenu = array();
        $this->arrMenuSecundario = array();
        $this->titulo = $titulo;
        $this->urlTitulo = $urlTitulo;
    }

    function addLink($key, $link, $flagSecundario = null) {
        $menu = new Menu_TM($key, $link, "");
        if (!$flagSecundario) {
            array_push($this->arrMenu, $menu);
        } else {
            array_push($this->arrMenuSecundario, $menu);
        }

        return $menu;
    }

    function addMenu($key, $flagSecundario = null, $classMenu = null) {
        $menu = new Menu_TM($key, "", $classMenu);

        if (!$flagSecundario) {
            array_push($this->arrMenu, $menu);
        } else {
            array_push($this->arrMenuSecundario, $menu);
        }

        return $menu;
    }
    
    function addBusca($texto, $action, $flagSecundario = null) {
        
        $menu = new Busca_TM($texto, $action);
       
       
        if (!$flagSecundario) {
             $this->busca=$menu;
            //array_push($this->arrMenu, $menu);
        } else {
           // array_push($this->arrMenuSecundario, $menu);
            $this->buscaSecundaria=$menu;
        } 
    }
    
    

    function cabecalho() {

        $elDiv = elMaker("div")->setClass("navbar navbar-inverse");
        $this->elRoot = $elDiv;
        $elDivContainer = elMaker("div", $elDiv)->setClass("container")->setAtributo("role", "navigation");
        $elDivHeader = elMaker("div", $elDivContainer)->setClass("navbar-header");
        //toggle button
        $elButton = elMaker("button", $elDivHeader)->setClass("navbar-toggle")->setType("button")->setAtributo("data-toggle", "collapse")
                ->setAtributo("data-target", ".navbar-collapse");
        $elSpan = elMaker("span", $elButton)->setClass("icon-bar");
        $elSpan = elMaker("span", $elButton)->setClass("icon-bar");
        $elSpan = elMaker("span", $elButton)->setClass("icon-bar");
        $elLink = elMaker("a", $elDivHeader)->setClass("navbar-brand")->setHref($this->urlTitulo)->setValue($this->titulo);

        $elDivCollapse = elMaker("div", $elDivContainer)->setClass("navbar-collapse collapse");

        $this->elContainer = $elDivCollapse;
    }

    function rodape(ElementMaker $elRoot) {
        
    }

    function mostraMenuPrincipal() {
        $ul = elMaker("ul", $this->elContainer)->setClass("nav navbar-nav");
        for ($c = 0; $c < sizeof($this->arrMenu); $c++) {
            $item = $this->arrMenu[$c];
            $item->desenha($ul);
        }
        if ($this->busca){
            $this->busca->desenha($this->elContainer);
        }
        
    }

    function mostraMenuSecundario() {
        $elDiv = elMaker("div", $this->elContainer)->setClass("navbar-right");
        $ul = elMaker("ul", $elDiv)->setClass("nav navbar-nav");
        for ($c = 0; $c < sizeof($this->arrMenuSecundario); $c++) {
            $item = $this->arrMenuSecundario[$c];
            $item->desenha($ul);
        }
        if ($this->buscaSecundaria){
            $this->buscaSecundaria->desenha($this->elContainer);
        }
    }

    function mostra(ElementMaker $elRoot) {
        $this->cabecalho();
        $this->mostraMenuPrincipal();
        $this->mostraMenuSecundario();
        $this->rodape($this->elRoot);
        $elRoot->addElement($this->elRoot);
    }

}

?>