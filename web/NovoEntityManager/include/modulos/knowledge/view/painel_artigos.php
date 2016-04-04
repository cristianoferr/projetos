<?

/**
 * Essa classe desenha uma sanfona com os artigos dentro dela
 */
class PainelArtigos extends BasePainel {

    private $count, $flagEditavel, $idElemento;
    private $flagFixo;

    function setFixo($v) {//coberto
        $this->flagFixo = $v;
    }

    function setIdElemento($id) {
        $this->idElemento = $id;
    }

    function cabecalho() {
        BasePainel::cabecalho();
        $elDivRoot = elMaker("div", $this->elRoot)->setClass("panel-group");
        if ($this->flagEditavel) {
            $elDiv = elMaker("div", $elDivRoot)->setStyle('float:right;');
            $elDiv->addElement(getWidgetManager()->createNewButtonForWidget(WIDGET_NEW_ARTIGO, $this->idElemento));
        }
        return $this->elRoot;
    }

    function setEditavel($v) {
        $this->flagEditavel = $v;
    }

    function acoesEditaveis(IModel $model, ElementMaker $elRoot) {//coberto
        $idArtigo = $model->getId();
        $elDiv = elMaker("div", $elRoot)->setStyle('float:right;');
        $widget = getWidgetManager()->generateEditButtonForWidget(WIDGET_EDIT_ARTIGO, $idArtigo);
        $elDiv->addElement($widget->generate());

        $elDiv->addElement(getWidgetManager()->createLinkButtonForWidget(WIDGET_EDIT_ARTIGO, $idArtigo));
        $elDiv->addElement(Out::createDeleteLinkForLink("article/delete/$idArtigo", "", "default"));
        $lingua = getControllerForTable("lingua")->getFieldFromModel($model->getValorCampo('id_lingua'), "nmlongo_lang");
        return $lingua;
    }

    function registro(IModel $model) {//coberto
        BasePainel::registro($model);
        $idArtigo = $model->getId();
        if ($this->count == 0) {
            $elH3 = elMaker("h3", $this->elRoot)->setValue($this->getTitulo());
        }

        if ($this->flagEditavel) {
            $lingua = $this->acoesEditaveis($model, $this->elRoot);
        }

        $elDivPanel = elMaker("div", $this->elRoot)->setClass("panel panel-default");
        $elDivHeading = elMaker("div", $elDivPanel)->setClass("panel-heading");

        $this->escreveTitulo($idArtigo, $model->getValorCampo('title_artigo'), $elDivHeading);
        $elSmall = elMaker("small", $elDivHeading)->setClass("pull-right lang_article")->setValue($lingua);

        $this->escreveConteudo($idArtigo, $model->getValorCampo('texto_artigo'), $elDivPanel);

        $this->count++;
    }

    function escreveTitulo($idArtigo, $titulo, ElementMaker $elRoot) {
        $elH4 = elMaker("h4", $elRoot)->setClass("panel-title");
        if (!$this->flagFixo) {
            $elLink = elMaker("a", $elH4)->setClass("accordion-toggle")->setAtributo("data-toggle", "collapse")
                            ->setAtributo("data-parent", "#accordion")->setHref("#collapse$idArtigo")->setValue($titulo);
        } else {
            $elH4->setValue($titulo);
        }
    }

    function escreveConteudo($idArtigo, $texto, ElementMaker $elRoot) {
        $id = "";
        $class = "";
        if (!$this->flagFixo) {
            $id = "collapse$idArtigo";
            $class = "panel-collapse collapse";
        }
        $elDiv = elMaker("div", $elRoot)->setId($id)->setClass($class);
        $texto = replaceString($texto, "\\\"", "\"");
        $elDivBody = elMaker("div", $elDiv)->setClass("panel-body")->setValue($texto);
    }

}

?>