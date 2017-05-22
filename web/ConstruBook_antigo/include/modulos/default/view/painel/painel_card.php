<?

class CardPainel extends BasePainel {

    private $textoNovo;
    private $linkNovo;

    function cabecalho() {
        BasePainel::cabecalho();
        if ($this->getTitulo()) {
            $elH1 = elMaker("h1")->setClass("titulo");

            if ($this->getLinkTitulo()) {
                $elLink = Out::criaLink($this->getLinkTitulo(), $this->getTitulo());
                $elH1->addElement($elLink);
            } else {
                $elH1->setValue($this->getTitulo());
            }
            $elH1->mostra();
        }
    }

    function criaPainel($titulo, $link, ElementMaker $elDivAcoes = null) {
        $elDivPainel = elMaker("div")->setClass("panel panel-projeto col-md-4")->setAtributo("onclick", "window.location = '" . getHomeDir() . $link . "#" . ANCORA_INICIO . "'");
        $elDivHeading = elMaker("div", $elDivPainel)->setClass("panel-heading");
        $elTitulo = elMaker("h4", $elDivHeading);
        $elLink = Out::criaLink($link, $titulo);
        $elTitulo->addElement($elLink);
        if ($elDivAcoes) {
            $elDivHeading->addElement($elDivAcoes);
        }

        return $elDivPainel;
    }

    function escreveCelulaInicio($titulo, $link, $model = null) {
        $elDivAcoes = elMaker("div");
        $elDivPainel = $this->criaPainel($titulo, $link, $elDivAcoes);

        if ($model) {
            $this->drawAcoes($model, $elDivAcoes);
        }

        return $elDivPainel;
    }

    function registro(IModel $model) {
        BasePainel::registro($model);
        $colunas = $this->getColunas();

        $titulo = $model->getValorFormatado($colunas[1]);
        $elDivPainel = $this->escreveCelulaInicio($titulo, "project/" . $model->getId(), $model);
        $elDivPainel->mostra();
    }

    function setDetalhesNovoRegistro($link, $texto) {
        $this->textoNovo = $texto;
        $this->linkNovo = $link;
    }

    function escreveCardNovo() {
        $elDivPainel = $this->criaPainel(translateKey("txt_start_new_project") . "...", "project/new");
        $elDivPainel->mostra();
    }

    function rodape(ElementMaker $elRoot) {
        if ($this->textoNovo) {
            $this->escreveCardNovo();
        }
    }

}

?>