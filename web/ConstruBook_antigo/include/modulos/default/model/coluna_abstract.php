<?

interface IColuna {

    function setDatatype($r);

    function getDatatype();

    function getTipoInput();

    function getTable();

    function iniciaColuna($caption = null, $dbName, $flagReadOnly, $tipoInput, $limiteTamanho = null);

    function iniciaColunaComFK($caption = null, $dbName, $flagReadOnly, $fkTable, $tipoInput);

    function getDbName();

    function isReadOnly();

    function isAllowingNull();

    function resetTotalizador();

    function isVisible();

    function getHintText();

    function limiteTamanho();

    function validaValor($valor);

    function getFkTable();

    function setFkTable($v);
}

abstract class ColunaAbstract {

    protected $tipoInput, $datatype;
    protected $flagVisible, $total, $table, $dbName, $flagRequired, $caption, $fkTable;
    protected $limiteTamanho;
    protected $flagAllowNull;
    protected $flagVisivelInclusao;
    protected $defaultValue; //valor default
    protected $flagTotaliza;

    function iniciaColuna($caption = null, $dbName, $flagReadOnly, $tipoInput, $limiteTamanho = null) {//coberto
        $this->caption = $caption;
        $this->dbName = $dbName;
        $this->flagReadOnly = $flagReadOnly;
        $this->tipoInput = $tipoInput;


        if ($limiteTamanho) {
            $this->limiteTamanho = $limiteTamanho;
        }
        return $this;
    }

    function validaValorData($valor) {//coberto
        //0123456789012345
        //2013-02-05T35:40;
        $ano = substr($valor, 0, 4);
        $mes = substr($valor, 5, 2);
        $dia = substr($valor, 8, 2);
        $hora = substr($valor, 11, 2);
        $minuto = substr($valor, 14, 2);

        $ano = limitaValor($ano, 1000, 3000, 4);
        $mes = limitaValor($mes, 1, 12, 2);
        $dia = limitaValor($dia, 1, 31, 2);
        $hora = limitaValor($hora, 0, 23, 2);
        $minuto = limitaValor($minuto, 0, 59, 2);
        $valor = $ano . "-" . $mes . "-" . $dia . "T" . $hora . ":" . $minuto;
        return $valor;
    }

    function validaValor($valor) {//coberto
        if ($this->tipoInput == TIPO_INPUT_DATA) {
            return $this->validaValorData($valor);
        }
        return $valor;
    }

    /**
     * Define detalhes da inclusão (visivel,required,valor default (FIRST_OPTION)
     * @param type $flagVisivel
     * @param type $flagRequired
     * @param type $default
     */
    function setDetalhesInclusao($flagVisivel, $flagRequired, $default) {//coberto
        $this->flagRequired = $flagRequired;
        $this->flagVisivelInclusao = $flagVisivel;
        $this->defaultValue = $default;
    }

    function getDefaultValue() {//coberto
        return $this->defaultValue;
    }

    function setDefaultValue($v) {//coberto
        $this->defaultValue = $v;
    }

    function totaliza() {//coberto
        $this->flagTotaliza = true;
    }

    function isVisibleInclusao() {//coberto
        return $this->flagVisivelInclusao;
    }

    function isTotalizado() {//coberto
        return $this->flagTotaliza;
    }

    function isVisible() {//coberto
        return $this->flagVisible;
    }

    function isReadOnly() {//coberto
        return $this->flagReadOnly;
    }

    function setReadonly($r) {//coberto
        $this->flagReadOnly = $r;
    }

    function setInvisible() {//coberto
        $this->flagVisible = false;
    }

    function setVisible() {//coberto
        $this->flagVisible = true;
    }

    function getTipoInput() {//coberto
        return $this->tipoInput;
    }

    function setTipoInput($v) {//coberto
        $this->tipoInput = $v;
    }

    function setDatatype($r) {//coberto
        $this->datatype = $r;
    }

    function getDatatype() {//coberto
        return $this->datatype;
    }

    function setFlagAllowNull($flagAllowNull) {//coberto
        $this->flagAllowNull = $flagAllowNull;
    }

    function isAllowingNull() {//coberto
        return $this->flagAllowNull;
    }

    function resetTotalizador() {
        $this->total = 0;
    }

    //getter e setters
    function setCaption($v) {//coberto
        $this->caption = $v;
    }

    function getCaption() {//coberto
        if (($this->fkTable) && (!$this->caption)) {
            $controller = getControllerForTable($this->fkTable);
            $this->caption = $controller->getSingular();
        }

        return $this->caption;
    }

    function setDbName($v) {//coberto
        $this->dbName = $v;
    }

    function getDbName() {//coberto
        return $this->dbName;
    }

    function setFkTable($v) {//coberto
        $this->fkTable = $v;
    }

    function getFkTable() {//coberto
        return $this->fkTable;
    }

    function isNumber() {//coberto
        if ($this->tipoInput == TIPO_INPUT_INTEIRO) {
            return true;
        }
        if ($this->tipoInput == TIPO_INPUT_SELECT_FK) {
            return true;
        }
        if ($this->tipoInput == TIPO_INPUT_SELECT_FIXO) {
            return true;
        }
        if ($this->tipoInput == TIPO_INPUT_SEQUENCE) {
            return true;
        }
        if ($this->tipoInput == TIPO_INPUT_RADIO) {
            return true;
        }
        if ($this->tipoInput == TIPO_INPUT_RADIO_SELECT) {
            return true;
        }
        return false;
    }

    function isRequired() {//coberto
        return $this->flagRequired;
    }

    function getTable() {//coberto
        return $this->table;
    }

}

?>