<?php

define("ACAO_INSERIR_DETALHE", "inserirD");
define("ACAO_EXCLUIR_DETALHE", "excluirD");

class PainelMestreDetalhe_Data extends BasePainel {

    protected $orderBy, $orderByDetail;
    protected $detailFilter;
    protected $masterField;
    protected $prevMaster; //ao mudar, mostra o mestre
    protected $elDivRoot;
    protected $controllerDetail;
    protected $arrColsDetail;
    protected $inputMakerDetail;
    protected $id;

    function __construct() {
        parent::__construct();
        $this->arrColsDetail = array();
    }

    function setControllerDetail(IController $c) {//coberto
        $this->controllerDetail = $c;
    }

    function setMasterField($master) {//coberto
        $this->masterField = $master;
    }

    function setMasterOrderBy($order) {//coberto
        $this->orderBy = $order;
    }

    function setDetailOrderBy($order) {//coberto
        $this->orderByDetail = $order;
    }

    function getOrderbySQL() {//coberto
        return $this->orderBy;
    }

    function addColunaMestre($dbname) {//coberto
        $this->addColunaWithDBName($dbname);
    }

    function addColunaDetalhe($dbname) {//coberto
        array_push($this->arrColsDetail, $dbname);
    }

    function getRowIdM(IModel $model) {
        return $this->formName . "_m_" . $model->getId();
    }

    function getRowIdD(IModel $model) {
        return $this->getFormNameDetail($model) . "_" . $model->getId();
    }

    function getFormNameDetail(IModel $model) {
        return $this->formName . "_" . $model->getValorCampo($this->masterField);
    }

    function defineMaster($table, $masterField, $orderBy) {
        $controllerMaster = getControllerForTable($table);
        $this->setController($controllerMaster);
        $this->setMasterField($masterField);
        $this->setMasterOrderBy($orderBy);

        $this->getInputCreator()->setFormName($this->formName . "_m");
    }

    function defineDetail($table, $orderBy) {
        $controller = getControllerForTable($table);
        $this->setControllerDetail($controller);
        $controller->setDefaultOrderBy($orderBy);
    }

    function load($field, $id) {
        $sqlM = " and $field=$id";
        $this->id = $id;
        $sqlD = "and " . $this->masterField . " in (select " . $this->masterField . " from " . $this->getController()->getTable()->getTableName() . " where 1=1 $sqlM)";
        $this->controllerDetail->loadRegistros($sqlD);
        $this->controller->loadRegistros($sqlM, $this);
    }

}

interface IPainelMestreDetalhe {

    function load($field, $id);

    function defineDetail($table, $orderBy);

    function defineMaster($table, $masterField, $orderBy);

    function addColunaMestre($dbname);

    function addColunaDetalhe($dbname);
}

class PainelMestreDetalhe extends PainelMestreDetalhe_Data implements IPainelMestreDetalhe {

    function __construct() {
        parent::__construct();
    }

    function cabecalho() {
        $this->setHorizontal();
        $this->elRoot = BasePainel::cabecalho();

        $this->elDivRoot = elMaker("div", $this->elRoot)->setClass("panel panel-projeto");




        $this->escreveTitulo($this->elDivRoot);
    }

    function escreveTitulo(ElementMaker $elRoot = null) {
        if ($this->isModal()) {
            return;
        }
        if (!$this->getTitulo()) {
            return;
        }

        $elTitulo = elMaker("div", $elRoot)->setClass("panel-heading container");
        if ($this->getLinkTitulo()) {
            $elLink = elMaker("a", $elTitulo)->setHref($this->getLinkTitulo());
        } else {
            $elLink = elMaker("h3", $elTitulo)->setClass("panel-title");
        }
        $elLink->setValue($this->getTitulo());
        if (!$elRoot) {
            $elTitulo->mostra();
        }
        return $elTitulo;
    }

    function registro(IModel $model) {
        parent::registro($model);

        $cols = $this->getColunas();
        $divGrupo = criaEl("div", $this->elDivRoot)->setClass("panel-projeto")->setId("tr_" . $this->getRowIdM($model));
        $divRow = criaEl("div", $divGrupo)->setClass("panel-heading container");
        $this->getInputCreator()->addRowID($this->getRowIdM($model));
        $this->escreveAcoesMaster($model, $divRow, $this->countAtual + 1);

        for ($c = 0; $c < sizeOf($cols); $c++) {
            $col = $cols[$c];
            $this->escreveMasterCol($col, $divRow, $model, $c);
        }
        $this->escreveDetalhes($divGrupo, $model);
    }

    function escreveDetalhes(ElementMaker $divRow, IModel $model) {

        $controller = $this->controllerDetail;
        $this->inputMakerDetail = new InputBusiness($controller);
        $this->inputMakerDetail->setFormName($this->getFormNameDetail($model));

        $controller->first();
        $c = 0;

        while ($detail = $controller->next()) {
            if ($detail->getValorCampo($this->masterField) == $model->getValorCampo($this->masterField)) {
                $this->inputMakerDetail->addRowID($this->getRowIdD($detail));
                $this->escreveDetail($divRow, $detail, $c);
                $c++;
            }
        }

        $this->inputMakerDetail->conclui($this->elDivRoot);
    }

    function escreveAcoesMaster(IModel $detail, ElementMaker $elRow, $c) {
        $col = criaEl("div", $elRow)->setClass("col-sm-2 col");
        $divCount = criaEl("div", $col)->setClass("hidden")->setId("rownum_" . $this->getRowIdM($detail))->setValue($c);
        $divId = criaEl("div", $col)->setClass("hidden")->setId("colid_" . $this->getRowIdM($detail))->setValue($detail->getId());

        $col->addElement($this->adicionaGlyphAcao(ACAO_INSERIR_DETALHE, "glyphicon-plus", $detail->getValorCampo($this->masterField)));
        $col->addElement($this->adicionaGlyphAcao(ACAO_EXCLUIR, "glyphicon-remove", $detail->getValorCampo($this->masterField), "btn-danger"));
    }

    function escreveAcoesDetail(IModel $detail, ElementMaker $elRow, $c) {
        $col = criaEl("div", $elRow)->setClass("col-sm-2 col");
        $divCount = criaEl("div", $col)->setClass("hidden")->setId("rownum_" . $this->getRowIdD($detail))->setValue($c);
        $divId = criaEl("div", $col)->setClass("hidden")->setId("colid_" . $this->getRowIdD($detail))->setValue($detail->getId());

        $col->addElement($this->adicionaGlyphAcao(ACAO_EXCLUIR_DETALHE, "glyphicon-remove", $detail->getId(), "btn-danger"));
    }

    function escreveDetail(ElementMaker $divRow, IModel $detail, $countRow) {
        $elRow = criaEl("div", $divRow)->setClass("color_alternate container")->setId("tr_" . $this->getRowIdD($detail));


        $this->escreveAcoesDetail($detail, $elRow, $countRow);
        $c = 0;
        foreach ($this->arrColsDetail as $col) {
            $this->criaInputDetail($detail, $col, $elRow, $c);
            $c++;
        }
    }

    function criaInputDetail(IModel $detail, $col, ElementMaker $elRow, $c) {
        $input = $this->inputMakerDetail;
        $coluna = $detail->getTable()->getColunaWithName($col);
        if (!$coluna->isVisible()) {
            return;
        }
        $divCol = criaEl("div", $elRow)->setClass("col-sm-2 col");
        $input->setPosAtual($c);
        $input->setFlagAjax(true);
        $input->setColuna($coluna);
        $input->setModel($detail);
        $input->setFormID($this->getRowIdD($detail));
        $input->setFlagInclusao(false);
        $input->setPlaceholder($coluna->getCaption());
        $input->setPainel($this);
        // write("coluna '" . $coluna->getDbName() . "':" . $this->getFiltroColuna($coluna->getDbName()));
        //$input->setFiltro($this->getFiltroColuna($coluna->getDbName()));

        $divCol->addElement($input->criaCelula(false));
    }

    function escreveMasterCol(IColuna $col, ElementMaker $divRow, IModel $model, $c) {
        if (!$col->isVisible()) {
            return;
        }
        $divCol = criaEl("div", $divRow)->setClass("col-sm-2 col"); //->setValue($model->getValor($col));
        $this->criaInputMaster($model, $col, $divCol, $c);
    }

    function criaInputMaster(IModel $model, IColuna $coluna, ElementMaker $divCol, $c) {
        $input = $this->getInputCreator();

        $input->setPosAtual($c);
        $input->setFlagAjax(true);
        $input->setColuna($coluna);
        $input->setModel($model);
        $input->setFormID($this->getRowIdM($model));
        $input->setFlagInclusao(false);
        $input->setPlaceholder($coluna->getCaption());

        $divCol->addElement($input->criaCelula(false));
    }

    function rodape(ElementMaker $elRoot) {

        BasePainel::rodape($this->elDivRoot);

        if ($this->widgetInsertMaster) {
            $link = getWidgetManager()->generateRedirectLinkForWidget($this->widgetInsertMaster, $this->id);
            $this->elDivRoot->addElement($link);
            $divCol = criaEl("div", $link)->setClass("container btn-default");
            $divCol->setValue(translateKey("txt_new_section"));
        }
    }

}
