<?

class PainelHorizontalMini extends PainelHorizontal {

    function cabecalho() {
        $this->setHorizontal();
        $this->contaLinha = 1;
        $elRoot = BasePainel::cabecalho();

        $elDivRoot = elMaker("div", $elRoot)->setClass("mini_container ")->setId("table_" . $this->getFormName());

        $this->elTable = $elDivRoot;

        $this->escreveTitulo($elDivRoot);


        $this->escreveNomeColunas();
    }

    function getCellClass() {
        return " col-sm-2";
    }

    function criaCell(ElementMaker $elTr, $flagHead) {
        $cell = elMaker("div", $elTr);
        if ($flagHead) {
            $cell->addClass("heading")->addClass($this->getCellClass());
        }
        return $cell;
    }

    function escreveNomeColunas() {
        $colunas = $this->getColunas();
        $elTr = elMaker("div", $this->elTable)->setClass(" ");
        for ($c = 0; $c < $this->countEmptyColumnsBefore(); $c++) {
            $cell = $this->criaCell($elTr, true);
        }

        for ($c = 0; $c < sizeOf($colunas); $c++) {
            $coluna = $colunas[$c];
            $cell = $this->escreveNomeColuna($coluna, $elTr, "div");
            if ($cell) {
                $cell->addClass("heading")->addClass($this->getCellClass());
            }
        }

        for ($c = 0; $c < $this->countEmptyColumnsAfter(); $c++) {
            $cell = $this->criaCell($elTr, true)->setValue("&nbsp;");
        }
        //elMaker("br", $this->elTable);
    }

    function registro(IModel $model) {
        BasePainel::registro($model);
        $colunas = $this->getColunas();
        $rowID = $this->getRowID();
        $elTr = $this->criaRow($rowID, "div")->setClass("panel-body block");

        $this->mostraValoresModelo($model, $colunas, $elTr);

        $cellAcoes = $this->mostraAcoesRegistro($model, $elTr, "div");
        $cellAcoes->addClass($this->getCellClass());

        $this->contaLinha++;
    }

    function escreveCelula($coluna, $model, $formID, $pos) {
        $cell = parent::escreveCelula($coluna, $model, $formID, $pos, "div");
        $cell->addClass($this->getCellClass());
        return $cell;
    }

    function rodape(ElementMaker $elRoot) {

        BasePainel::rodape($elRoot);
    }

    function infoRodape() {
        return false;
    }

}

?>