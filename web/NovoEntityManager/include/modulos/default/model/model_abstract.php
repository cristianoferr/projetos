<?

interface IModel {

    function getValorFormatado(IColuna $coluna);

    function getId();

    function getTable();

    function getValor(IColuna $coluna);

    function getValorCampo($nomeCampo);

    function setValorCampo($nomeCampo, $valor);

    function carregaDados($id, $arrDados);

    function isFlagInclusao();

    function setFlagInclusao($v);
}

abstract class AbstractModel {

    protected $id;
    protected $controller;
    protected $arrDados;
    protected $table;
    protected $flagInclusao;

    /**
     *    Carrega dados a partir da array do parametro
     *    @param integer $id          record ID.
     *    @param array $arrDados         array with values to store.
     */
    function carregaDados($id, $arrDados) {
        $this->id = $id;
        $this->arrDados = $arrDados;
    }

    function setFlagInclusao($v) {
        $this->flagInclusao = $v;
    }

    function isFlagInclusao() {
        return $this->flagInclusao;
    }

    function setId($id) {
        $this->id = $id;
    }

    function getId() {
        return $this->id;
    }

    function getTable() {//coberto
        return $this->table;
    }

    function getTableName() {//coberto
        return $this->table->getTableName();
    }

    function getValor(IColuna $coluna) {//coberto
        if ($this->isFlagInclusao()) {
            return $coluna->getDefaultValue();
        }
        if (!$coluna) {
            erroFatal("column undefined.");
        }

        $valor = $coluna->validaValor($this->getValorCampo($coluna->getDbName()));
        return $valor;
    }

    function getValorCampo($nomeCampo) {//coberto
        if (!isset($this->arrDados)) {
            $this->arrDados = array();
        }
        //echo "setValorcampo $nomeCampo ".$valor;
        return $this->arrDados[$nomeCampo];
    }

    function setValorCampo($nomeCampo, $valor) {
        if (!isset($this->arrDados)) {
            $this->arrDados = array();
        }
        //echo "setValorcampo $nomeCampo ".$valor;
        $this->arrDados[$nomeCampo] = $valor;
    }

    public function __toString() {
        return $this->id . " - " . $this->getDescricao();
    }

    function getDados() {
        return $this->arrDados;
    }

    function printDados() {
        printArray($this->arrDados);
    }

    function getValorFormatado(IColuna $coluna) {
        //echo "getValorFormatado";
        if ($this->isFlagInclusao()) {
            return $coluna->getDefaultValue();
        }
        $valor = $this->getValor($coluna);
        if ($coluna->getTipoInput() == TIPO_INPUT_BOOLEAN) {
            if ($valor == "T") {
                return txt_true;
            }
            return txt_false;
        }

        if (($coluna->isNumber()) && ($coluna->getFkTable())) {
            $controller = getControllerForTable($coluna->getFkTable());
            $tbl = getTableManager()->getTabelaComNome($coluna->getFkTable());
            $desc = $controller->getFieldFromModel($valor, $tbl->getDescName());
            return $desc;
        }

        return $valor;
    }

    function getDescricao() {
        $col = $this->table->getDescColuna();
        if (!$col) {
            return $this->getId();
        }

        $v = $this->getValorFormatado($col);
        if ($this->table->isTraduzDescricao()) {
            $v = translateKey($v);
        }
        return $v;
    }

}

?>