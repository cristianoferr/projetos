<?

class PainelHorizontal extends BasePainel {

    protected $contaLinha;
    protected $flagTotal; //se true então tem coluna totalizando
    protected $elTable;

    function cabecalho() {
        $this->setHorizontal();
        $this->contaLinha = 1;
        $this->elRoot = BasePainel::cabecalho();

        $elDivRoot = elMaker("div", $this->elRoot)->setClass("table-responsive");
        $this->elTable = elMaker("table", $elDivRoot)->setClass("data panelsize fundo caixa panelsizetable table-bordered table-striped")->setId("table_" . $this->getFormName());

        $this->escreveTitulo($this->elTable);

        $elHeader = elMaker("tr", $this->elTable)->setStyle("background:#fafafa");
        $elTd = elMaker("td", $elHeader)->setAtributo("colspan", "100");

        $this->drawFiltros($elTd);
        $this->drawAbas($this->getFormName(), $elTd);


        $this->escreveNomeColunas();
    }

    function infoRodape() {
        $max = $this->getCountAtual();
        return "$max " . translateKey("txt_of") . " $max";
    }

    function idForm() {
        return $this->getFormName() . projetoAtual() . entidadeAtual();
    }

    function getCampoOrderBy() {

        $campoOrderBy = getControllerManager()->getControllerForTable("usuario")->getEstadoInterface($this->idForm(), "false");
        if ($campoOrderBy == "false") {
            return false;
        }
        return $campoOrderBy;
    }

    function getAscOrderBy() {
        $campoOrderBy = getControllerManager()->getControllerForTable("usuario")->getEstadoInterface($this->idForm() . "isAsc", "");
        if ($campoOrderBy == "false") {
            return "desc";
        }
    }

    function countEmptyColumnsBefore() {
        $c = 0;
        if ($this->getEditWidget()) {
            $c++;
        }

        if ($this->getEditLink()) {
            $c++;
        }
        return $c;
    }

    function countEmptyColumnsAfter() {
        $c = 0;
        $deleteLink = $this->getDeleteLink();
        if ($deleteLink) {
            $c++;
        }
        return $c;
    }

    function escreveNomeColunas() {
        $colunas = $this->getColunas();
        $elTr = elMaker("tr", $this->elTable);

        $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setClass("nobg")->setValue("#");
        $elTd = elMaker("td", $elTr)->setStyle("display:none");
        $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setStyle("width:5%;")->setValue();
        $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setStyle("width:5%;")->setValue();

        //$campoOrderByAsc=getControllerManager()->getUsuarioController()->getEstadoInterface($this->idForm()."_isAsc","true");
        //echo "campoOrderBy: $campoOrderBy $asc";

        for ($c = 0; $c < sizeOf($colunas); $c++) {

            $coluna = $colunas[$c];
            $this->escreveNomeColuna($coluna, $elTr);
        }
        $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setStyle("width:5%;")->setValue();
        $elTh = elMaker("th", $elTr)->setAtributo("scope", "col")->setValue();
    }

    function escreveNomeColuna(IColuna $coluna, ElementMaker $elTr, $tipo = "th") {
        $coluna->resetTotalizador();
        if ($this->podeMostrarColuna($coluna)) {

            $elTh = elMaker($tipo, $elTr);
            if ($this->isOrderByEnabled()) {
                $this->verificaColunaOrdenavel($coluna, $elTh);
            }
            $elTh->setAtributo("scope", "col")->addClass("class_" . $this->getFormName() . "_" . $coluna->getDbName());
            $elTh->setValue($coluna->getCaption());
        }
        return $elTh;
    }

    function verificaColunaOrdenavel(Coluna $coluna, ElementMaker $elColuna) {
        $campoOrderBy = $this->getCampoOrderBy();
        $ascOrder = $this->getAscOrderBy();

        $classOrder = "sortable";
        $asc = "true";
        if ($campoOrderBy == $coluna->getDbName()) {
            if (!$ascOrder) {
                $asc = "false";
                $classOrder.="sorting";
            } else {
                $classOrder.="unsorting";
            }
        }

        $orderByEvent = "return defineOrderby('" . $coluna->getDbName() . "','" . $this->idForm() . "',$asc);";
        $elColuna->setAtributo("onclick", $orderByEvent);
        $elColuna->addClass($classOrder);
    }

    function escreveContaLinhas(IModel $model, ElementMaker $elRoot) {
        $elTh = elMaker("th", $elRoot)->setScope("row")->setClass("specalt")->setId("rownum_" . $this->getRowID())->setValue($this->contaLinha);
        $elTd = elMaker("td", $elRoot)->setStyle("display:none")->setId("colid_" . $this->getRowID())->setValue($model->getID());
        $elTd = elMaker("td", $elRoot);

        if ($this->getEditWidget($model)) {
            $widgetRet = $this->generateEditWidget($model->getId());
            $elTd->addElement($widgetRet->generate());
        }

        $elTd = elMaker("td", $elRoot);
        $editLink = $this->getEditLink($model);
        if ($editLink) {
            $elLink = $this->generateEditLink($editLink, $model->getId(), $this->getFormName());
            $elTd->addElement($elLink);
        }
    }

    function getRowID() {
        return $this->getFormName() . "_" . $this->contaLinha;
    }

    function mostraValoresModelo(IModel $model, array $colunas, ElementMaker $elTr) {
        $rowID = $this->getRowID();
        $this->getInputCreator()->addRowID($rowID);
        for ($c = 0; $c < sizeOf($colunas); $c++) {
            $coluna = $colunas[$c];
            if ($coluna->isTotalizado()) {
                $this->flagTotal = true;
                $coluna->addTotal($model->getValor($coluna));
            }
            $uniqueID = $rowID;
            if ($this->podeMostrarColuna($coluna)) {
                $cell = $this->escreveCelula($coluna, $model, $uniqueID, $c);
                $elTr->addElement($cell);
            }
        }
    }

    function registro(IModel $model) {
        BasePainel::registro($model);
        $colunas = $this->getColunas();
        $rowID = $this->getRowID();
        $elTr = $this->criaRow($rowID, "tr");

        $this->escreveContaLinhas($model, $elTr);

        $this->mostraValoresModelo($model, $colunas, $elTr);

        $this->mostraAcoesRegistro($model, $elTr);

        $this->contaLinha++;
    }

    function criaRow($rowID, $tipoEl = "tr") {
        return elMaker($tipoEl, $this->elTable)->setId("tr_$rowID");
    }

    function mostraAcoesRegistro(IModel $model, ElementMaker $elTr, $tipoEl = "td") {
        $elTd = elMaker($tipoEl, $elTr);

        $deleteLink = $this->getDeleteLink($model);
        if ($deleteLink) {
            $elLink = $this->generateDeleteLink($deleteLink, $model->getId(), $this->getFormName());
            $elTd->addElement($elLink);
        }

        if ($this->temAcoes()) {
            $elTd = elMaker($tipoEl, $elTr);
            $this->drawAcoes($model, $elTd);
        }
        return $elTd;
    }

    function geraTotalizador() {
        $colunas = $this->getColunas();
        $elTr = elMaker("tr", $this->elTable);
        $elTh = elMaker("th", $elTr)->setScope("col")->setClass("nobg")->setValue("#");
        $elTd = elMaker("td", $elTr)->setStyle("display:none")->setValue();


        for ($c = 0; $c < $this->countEmptyColumnsBefore(); $c++) {
            $elTd = elMaker("td", $elTr)->setValue();
        }

        for ($c = 0; $c < sizeOf($colunas); $c++) {
            $coluna = $colunas[$c];
            if ($this->podeMostrarColuna($coluna)) {
                $elTd = elMaker("td", $elTr)->setValue($coluna->getTotal());
            }
        }

        for ($c = 0; $c < $this->countEmptyColumnsAfter(); $c++) {
            $elTd = elMaker("td", $elTr)->setValue();
        }
    }

    function rodape(ElementMaker $elRoot) {
        if ($this->flagTotal) {
            $this->geraTotalizador();
        }
        $elTr = criaEl("tr", $this->elTable);
        $elTd = criaEl("td", $elTr)->setAtributo("colspan", 100);
        BasePainel::rodape($elTd);
        $this->inicializaAba($this->getFormName());
        $this->pegaConfigAbas();
    }

}

?>