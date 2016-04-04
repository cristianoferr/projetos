<?

class LinkKey extends LinkView {

    function getText() {
        return translateKey(parent::getText());
    }

}

class LinkView {

    private $text;
    private $url;
    private $flagImportante;
    private $flagConfirm;
    private $icone, $glyph;
    private $arrLinkBase; //usado pelo menu
    private $cond_campo;
    private $condicao;
    private $cond_valor;

    /**
     * Cria um link 
     * @param type $text
     * @param type $url
     * @param type $flagImportante
     * @param type $flagConfirm
     */
    function LinkView($text, $url, $flagImportante = null, $flagConfirm = null) {
        $this->text = $text;
        $this->url = $url;
        $this->flagImportante = $flagImportante;
        $this->flagConfirm = $flagConfirm;

        if ($flagImportante) {
            $this->arrLinkBase = array($flagImportante);
        }
    }

    function setCondicao($nomeCampo, $condicao, $valor) {
        $this->cond_campo = $nomeCampo;
        $this->condicao = $condicao;
        $this->cond_valor = $valor;
    }

    function addLinkBase($url) {
        array_push($this->arrLinkBase, $url);
    }

    function getText() {
        return $this->text;
    }

    function getUrl() {
        return $this->url;
    }

    function setIcone($i) {
        $this->icone = $i;
    }

    function setGlyph($i) {
        $this->glyph = $i;
    }

    function getFlagImportante() {
        return $this->flagImportante;
    }

    function getFlagConfirm() {
        return $this->flagConfirm;
    }

    function drawInput(ElementMaker $elRoot = null) {
        $input = elMaker("input", $elRoot)->setType("hidden")->setName($this->text)->setAtributo("value", $this->url);
        if (!$elRoot) {
            $input->mostra();
        }
    }

    function drawAba($formID, $identificador, ElementMaker $elRoot = null) {
        $link = elMaker("a", $elRoot);
        $link->setClass("button-aba aba_" . $formID . "_" . $this->url)->
                setOnclick("return mudaAba('" . $formID . "_" . $this->url . "', arr_aba_" . $formID . "_" . $this->url . ", '" . $identificador . "')")
                ->setHref("#")->setValue($this->text);
        if (!$elRoot) {
            $link->mostra();
        }
    }

    function drawFiltro(ElementMaker $elRoot = null) {
        $estado = getControllerManager()->getControllerForTable("usuario")->getEstadoInterface(FILTRO_TAREFA, "all");
        if ($estado == $this->flagImportante) {
            $estado = "button-aba-off";
        } else {
            $estado = "";
        }
        $link = Out::linkG($this->url, $this->text, null, "button-aba $estado");
        if ($elRoot) {
            $elRoot->addElement($link);
        } else {
            $link->mostra();
        }
    }

    function draw(ElementMaker $elRoot = null) {
        $classeBt = "btn-primary";
        if ($this->flagImportante) {
            $classeBt = "btn-warning";
        }
        $elA = elMaker("a", $elRoot);
        if ($this->flagConfirm) {
            $elA->setAtributo("onclick", 'return confirmAction();');
        }
        $elA->setClass("btn btn-sm $classeBt")->setHref($this->url . "#" . ANCORA_INICIO);
        $elA->setValue($this->text);
        if (!$elRoot) {
            $elA->mostra();
        }
        return $elA;
    }

    function drawAcao($id_registro, $model = null, ElementMaker $elRoot = null) {
        if (!$this->condicaoOk($model)) {
            return;
        }
        $classeBt = "btn-info";
        if ($this->flagImportante) {
            $classeBt = "btn-warning";
        }

        $class = "btn btn-xs $classeBt ";
        if ($this->icone) {
            $class = "";
        }

        $classeBt = "btn-primary";
        if ($this->flagImportante) {
            $classeBt = "btn-warning";
        }

        $elA = elMaker("a");


        if ($this->flagImportante) {
            $elA->setAtributo("id", "important");
        }if ($this->flagConfirm) {
            $elA->setAtributo("onclick", 'return confirmAction();');
        }
        $elA->setClass($class)->setHref(getHomeDir() . $this->url . $id_registro . "#" . ANCORA_INICIO);

        if ($this->icone) {
            $elImg = elMaker("img", $elA)->setAtributo("title", $this->text)->setClass("icone-acao")->setAtributo("src", getHomeDir() . "images/" . $this->icone);
        } if ($this->glyph) {
            $elSpan = elMaker("span", $elA)->setClass($this->glyph)->setId("glyph_" . $id_registro);
            $elSpan->setAtributo("data-toggle", "tooltip")->setAtributo("data-placement", "top")->setAtributo("title", $this->text);
            $elSpan->setScript("$('#glyph_" . $id_registro . "').tooltip();");
        } else {
            $elA->setValue($this->text);
        }

        if ($elRoot) {
            $elRoot->addElement($elA);
        } else {
            $elA->mostra();
        }
        return $elA;
    }

    function condicaoOk($model) {
        if (!$model)
            return true;
        if (!$this->cond_campo)
            return true;

        $valorModel = $model->getValorCampo($this->cond_campo);
        return verficaCondicao($valorModel, $this->condicao, $this->cond_valor);
    }

    function drawMenu($sufixo = null) {
        $id = "";

        if ($this->flagImportante) {
            $pagina = getPaginaAtual();
            for ($c = 0; $c < sizeOf($this->arrLinkBase); $c++) {
                $linkBase = $this->arrLinkBase[$c];
                if (constainsSubstring($pagina, $linkBase))
                    $id = "current";
            }
        }
        if ($this->flagConfirm) {
            $id = "raiz";
        }
        ?><li>
            <a href="<? echo $this->url . $sufixo ?>" id="<? echo $id ?>"><? echo $this->text; ?></a></li>
        <?
    }

}
?>