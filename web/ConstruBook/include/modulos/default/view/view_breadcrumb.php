<?

class Breadcrumb {

    private $niveis;

    function __construct() {
        $this->niveis = array();
    }

    function addLink($desc, $url) {
        $url = new LinkView($desc, getHomeDir() . $url . "#" . ANCORA_INICIO, false, false);
        $this->addNivel($url);
    }

    function addLinkNew(IController $controller) {
        $this->addLinkView($controller->getNewItem());
    }

    function addLinkView(LinkView $link) {
        $this->addNivel($link);
    }

    function addNivel($url) {
        array_push($this->niveis, $url);
    }

    function mostra() {
        $elOl = elMaker("ol")->setClass("breadcrumb");
        for ($c = 0; $c < sizeof($this->niveis); $c++) {
            $url = $this->niveis[$c];

            $elLi = new ElementMaker("li", $elOl);
            if ($c == sizeof($this->niveis) - 1) {
                $elLi->setClass("active");
                $elTexto = new ElementMaker("span", $elLi);
            } else {
                $elTexto = new ElementMaker("a", $elLi);
                $elTexto->setAtributo("href", $url->getUrl());
            }
            $elTexto->setValue($url->getText());
        }
        $elOl->mostra();
    }

}
