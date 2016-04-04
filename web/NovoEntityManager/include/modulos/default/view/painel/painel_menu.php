<?

//TODO: substituir referencias a "projeto" por referencias genéricas
class SideMenuPainel extends BasePainel {

    private $linkItem;
    private $painelFilho;
    private $sqlfiltro;
    private $controllerFilho;
    private $elBody;

    function setLinkItem($linkItem) {
        $this->linkItem = $linkItem;
    }

    function setPainelFilho($painelFilho, $sqlfiltro) {
        $this->painelFilho = $painelFilho;
        $this->sqlfiltro = $sqlfiltro;
    }

    function setControllerFilho($controllerFilho) {
        $this->controllerFilho = $controllerFilho;
    }

    function cabecalho() {
        $elRoot = BasePainel::cabecalho();
        $div = elMaker("div", $elRoot)->setClass("sideMenu")->setId("sidebar")->setRole("navigation");
        if ($this->getTitulo()) {
            $ul = elMaker("ul", $div)->setClass("nav");
            $li = elMaker("li", $ul);
            $elA = elMaker("a", $li)->setClass("menu_expandido")->setId("raiz_entidades")->setHref("#")->setOnclick("alternaSideMenu();")->setValue($this->getTitulo());
        }
        $divConteudo = elMaker("div", $div)->setClass("conteudoMenu");
        $this->elBody = $divConteudo;
        $this->elRoot = $elRoot;
    }

    function registro(IModel $model) {
        BasePainel::registro($model);
        $colunas = $this->getColunas();
        $idColuna = $colunas[0];
        $descColuna = $colunas[1];

        $valorID = $model->getValor($idColuna);
        $valorDesc = $model->getValor($descColuna);
        
        $elUl = elMaker("ul", $this->elBody)->setId("projeto$valorID");
        $elLi = elMaker("li", $elUl);
        $elA = elMaker("a", $elLi)->setClass("raiz_projeto menu_expandido")->setId("root_projeto" . $valorID)->setHref("#")->
                setOnclick('alternaSideMenuProjeto(' . $valorID . ');')->setValue($valorDesc)
                ->setAtributo("ondblclick", "redirect('" . getHomeDir() . $this->linkItem . $valorID . "#" . ANCORA_INICIO . "');");

        $elUl = elMaker("ul", $this->elBody)->setId("conteudo_projeto$valorID");
        
        $this->controllerFilho->loadRegistros($this->sqlfiltro . $valorID, $this->painelFilho);
        $this->painelFilho->setController($this->controllerFilho);
        $this->painelFilho->setCodPai($valorID);
        $this->painelFilho->mostra();
        $elUl->addElement($this->painelFilho->getRootElement());


        $estado = getControllerManager()->getControllerForTable("usuario")->getEstadoInterface("menu_projeto$valorID", "false");
        if ($estado == "false") {
            $elUl->setScript("alternaSideMenuProjeto($valorID);");
        }
    }

}

?>