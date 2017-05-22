<?

class Out {

    function tituloG($txt) {//coberto
        $el = new ElementMaker("div");
        $el->setAtributo("class", "page-header");
        $elH1 = new ElementMaker("h1", $el);
        $elH1->setValue($txt);
        return $elH1;
    }

    function titulo($txt) {//coberto
        Out::tituloG($txt)->mostra();
    }

    function subTitulo($txt) {//coberto
        $el = new ElementMaker("h2");
        $el->setClass("subtitulo destaque")->setValue($txt)->mostra();
    }

    function painel($titulo, $texto, ElementMaker $elRoot = null) {
        $elPanel = new ElementMaker("div", $elRoot);
        $elPanel->setClass("panel panel-projeto");

        $elHeading = new ElementMaker("div", $elPanel);
        $elHeading->setClass("panel-heading");

        $elTitulo = new ElementMaker("h3", $elHeading);
        $elTitulo->setClass("panel-title")->setValue($titulo);

        $elBody = new ElementMaker("div", $elPanel);
        $elBody->setClass("panel-body")->setValue($texto);

        if (!$elRoot) {
            $elPanel->mostra();
        }
    }

    /**
     * Escreve um link interno simples
     * 
     * @param type $url
     * @param type $texto
     * @param type $id
     * @param type $class
     */
    function linkG($url, $texto, $id = "", $class = "") {//coberto
        return Out::criaLink($url, $texto, $id, $class);
    }

    function link($url, $texto, $id = "", $class = "") {//coberto
        $elLink = Out::linkG($url, $texto, $id, $class);
        $elLink->mostra();
    }

    /**
     * Cria um objeto do tipo ElementMaker (uso interno)
     * @param type $url
     * @param type $texto
     * @param type $id
     * @param type $class
     * @return \ElementMaker
     */
    function criaLink($url, $texto, $id = "", $class = "") {//coberto
        $elLink = new ElementMaker("a");
        $elLink->setAtributo("href", getHomeDir() . $url . "#" . ANCORA_INICIO);
        $elLink->setAtributo("id", $id)->setClass($class)->setValue($texto);
        return $elLink;
    }

    function linkWithHint($url, $texto, $hint, $id = "", $class = "") {
        $elLink = Out::criaLink($url, $texto, $id, $class);
        $elLink->setAtributo("data-hint", $hint)->mostra();
    }

    function linkWithIcon($url, $txtKey, $icone) {
        $elLink = Out::criaLink($url, translateKey($txtKey), null, null);

        $elIcone = new ElementMaker("img", $elLink);
        $elIcone->setClass("imgLink")->setAtributo("src", getHomeDir() . "images/$icone");
        $elLink->mostra();
    }

    function iconeEditar($url, ElementMaker $elRoot = null) {
        $elLink = Out::criaLink($url, "", null, null);
        $elIcone = new ElementMaker("img", $elLink);
        $elIcone->setClass("icone-acao icone-editar-diagrama")->setAtributo("src", getHomeDir() . "images/" . ICON_EDIT);
        if (!$elRoot) {
            $elLink->mostra();
        }
    }

    function createButton($link, $id, $formName, $linkClass = null, $linkText = null) {//coberto
        $idCompleto = "link_" . $formName . "_" . $id;
        $elLink = Out::criaLink($link, "", $idCompleto, "btn btn-warning");

        $elLink->setAtributo("data-toggle", "tooltip")->setAtributo("data-placement", "top")->setAtributo("title", "$linkText");

        $elSpan = new ElementMaker("span", $elLink);
        $elSpan->setClass($linkClass);
        $elLink->setScript("$('$idCompleto').tooltip();");

        return $elLink;
    }

    function createEditLinkForLink($link, $id, $formName) {//coberto
        return Out::createButton($link . $id, "edit_" . $id, $formName, "glyphicon glyphicon-edit", translateKey("txt_edit"));
    }

    function createNewItemForLink($link, $formName) {//coberto
        return Out::createButton($link, 0, $formName, "glyphicon glyphicon-plus", translateKey("txt_create_new"));
    }

    function createDeleteLinkForLink($link, $id, $formName) {//coberto
        return Out::createButton($link . $id, "delete_" . $id, $formName, "glyphicon glyphicon-remove", translateKey("txt_remove"));
    }

}

?>