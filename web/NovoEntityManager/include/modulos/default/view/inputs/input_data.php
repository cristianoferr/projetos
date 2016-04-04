<?php

/**
 * Essa classe irá armazenar os dados referente ao input, com seus setters e getters
 *
 * @author CMM4
 */
class InputData {

    protected $elRoot;
    public $model;
    protected $flagInputSomente;
    protected $posAtual; //index com a coluna atual... util para determinar se é o primeiro
    public $flagAjax;
    public $formID;
    public $uniqueID;
    public $sufId;
    public $flagInclusao;
    public $placeholder;
    protected $arrClass;
    protected $arrOnchange, $arrOnfocus, $onChangeEvent;
    protected $flagValueInside;
    protected $isFirst, $isText, $flagUsesSize, $isHorizontal, $formName, $showHints, $showValueIfReadonly;
    protected $painel;

    function __construct() {
        $this->isFirst = true;
        $this->onChangeEvent = "onchange";
        $this->flagUsesSize = true;
        $this->arrRowIDs = array();
    }

    function setPainel($painel) {
        $this->painel = $painel;
    }

    function addRowID($rowID) {
        if (!$rowID) {
            erroFatal("Empty rowID");
        }
        array_push($this->arrRowIDs, $rowID);
    }

    function setFormName($v) {
        $this->formName = $v;
    }

    function getTableName() {
        return $this->model->getTable()->getTableName();
    }

    function setHorizontal() {
        $this->isHorizontal = true;
    }

    function setRoot(ElementMaker $elRoot) {
        $this->elRoot = $elRoot;
    }

    //input somente, não escondendo o mesmo
    function inputSomente() {
        $this->flagInputSomente = true;
    }

    function setPosAtual($c) {
        $this->posAtual = $c;
    }

    function setFlagAjax($v) {
        $this->flagAjax = $v;
    }

    function setColuna(IColuna $v) {
        $this->coluna = $v;
    }

    function setModel(IModel $v) {
        $this->model = $v;
    }

    function setOnChangeEvent($v) {
        $this->onChangeEvent = $v;
    }

    function setFormID($formId, $rowId = null) {
        $this->formID = $formId . $rowId;
        $this->uniqueID = $this->createUniqueIDColuna();
        $this->sufId = "";
    }

    function getDbName() {
        return $this->coluna->getDbName();
    }

    function allowNull() {
        return $this->coluna->isAllowingNull();
    }

    function createUniqueIDColuna() {
        return $this->createUniqueID($this->getDbName());
    }

    function getValue() {
        return $this->model->getValor($this->coluna);
    }

    function getValueFormatted() {
        return $this->model->getValorFormatado($this->coluna);
    }

    function showInput() {
        if ($this->coluna->getTipoInput() == TIPO_INPUT_SEQUENCE) {
            return true;
        }
        return false;
    }

    function isInputVisible($flagInclusao) {
        return (($flagInclusao) || ($this->flagInputSomente) || ($this->showInput()));
    }

    /**
     * Se true então ao clicar na celula aparece o input
     */
    function isHideable() {
        if ((!$this->flagInputSomente) && (!$this->showInput()) && ($this->flagAjax)) {
            return true;
        }
        return false;
    }

    function createUniqueID($dbname) {
        return $this->formID . "_" . $dbname;
    }

    function getClasses() {
        $v = "";
        for ($c = 0; $c < sizeof($this->arrClass); $c++) {
            $v.=$this->arrClass[$c] . " ";
        }
        if (trim($v) != "") {
            return $v;
        }
    }

    function addCss($c) {
        array_push($this->arrClass, $c);
    }

    function addOnChange($c) {
        if (!$this->arrOnchange) {
            $this->arrOnchange = array();
        }
        array_push($this->arrOnchange, $c);
    }

    function addOnFocus($c) {
        if (!$c) {
            return array_push($this->arrOnfocus, $c);
        }
    }

    function setFlagInclusao($v) {
        $this->flagInclusao = $v;
    }

    function setPlaceholder($v) {
        $this->placeholder = $v;
    }

    function getUniqueID() {
        return $this->uniqueID();
    }

    function uniqueID() {
        if (!$this->uniqueID) {
            erroFatal("uniqueId empty");
        }

        return $this->uniqueID . $this->sufId;
    }

    function getInputId() {
        return "input_" . $this->uniqueID();
    }

}

?>