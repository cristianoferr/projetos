<?

class Coluna extends ColunaAbstract implements IColuna {

    private $relacao; //1=pk,2=obrigatorio,3=opcional// RELACAO_DATATYPE_PK/RELACAO_DATATYPE_OBRIGATORIO/RELACAO_DATATYPE_OPCIONAL
//Usado quando for TIPO_INPUT_SELECT_FIXO:
    private $arrOpcoesID;
    private $arrOpcoesDesc;
//Usado quando for campo calculado
    private $sqlCalculo;
//usado quando for campo descritivo FK 
    private $filtroFK, $filtroSelect;
//usado pelo form para determinar se é requerido ou nao

    private $copyFrom; //copia desse campo se estiver vazio
    private $css_class_id; //caso definido, aplica um estilo css + o id do item.
//usado pelo TIPO_INPUT_2_SELECT_OR
    private $coluna1;
    private $coluna2;
    private $condicao;
    private $hintText;
    private $concatId;

    function Coluna($table) {
        if (!$table) {
            erroFatal("Table not defined.");
        }
        $this->table = $table;
        $this->flagVisible = true;
        $this->flagReadOnly = false;
        $this->limiteTamanho = 999;
    }

    function setRelacao($r) {
        $this->relacao = $r;
    }

    function addTotal($v) {
        $this->total+=$v;
    }

    function getTotal() {
        if (!$this->isTotalizado()) {
            return;
        }
        if ($this->tipoInput == TIPO_INPUT_CURRENCY) {
            return formatNumber($this->total, 2);
        }
        return $this->total;
    }

    function getRelacao() {
        return $this->relacao;
    }

    function setCSSClassId($css) {
        $this->css_class_id = $css;
    }

    function getCSSClassId() {
        return $this->css_class_id;
    }

    function limiteTamanho() {
        return $this->limiteTamanho;
    }

    function setHintText($v) {
        $this->hintText = $v;
    }

    function setHintKey($v) {
        $this->setHintText(translateKey($v));
    }

    function getHintText() {
        return $this->hintText;
    }

    function iniciaColunaComFK($caption = null, $dbName, $flagReadOnly, $fkTable, $tipoInput) {
        $this->iniciaColuna($caption, $dbName, $flagReadOnly, $tipoInput);
        $this->fkTable = $fkTable;
        return $this;
    }

    function iniciaColunaComposta($caption, $name, $flagReadOnly, $tipoInput) {
        $this->iniciaColuna($caption, $name, $flagReadOnly, $tipoInput);
        return $this;
    }

    function setFiltro($filtro) {
        $this->filtroFK = $filtro;
    }

    function setFiltroSelect($filtro) {
        $this->filtroSelect = $filtro;
    }

    function getFiltroSelect() {
        return $this->filtroSelect;
    }

    function iniciaColunaDescreveFK($caption, $dbName, $tableFK, $filtroFK) {
        $this->iniciaColuna($caption, $dbName, true, TIPO_INPUT_TEXTAREA);
        $this->fkTable = $tableFK;
        $this->filtroFK = $filtroFK;
        return $this;
    }

//Metodologia","nm_metodologia","metodologia","projeto.id_metodologia=metodologia.id_metodologia")
//retorna o dbname se não for calculado ou retorna o sqlCalculo caso seja
    function getSelectSQL($pk_id) {
        if ($this->sqlCalculo) {
            if (!$this->concatId)
                $pk_id = null;
            return "(" . $this->sqlCalculo . $pk_id . ") as " . $this->dbName;
        } else {
            if ($this->tipoInput == TIPO_INPUT_2_SELECT_OR) {
                return $this->getColuna1()->getDbName() . "," . $this->getColuna2()->getDbName();
            }
            $valor = $this->table->getTableName() . "." . $this->dbName;


            if (hasDescription($this->tipoInput)) {
                $descFK = $this->getFkDesc();
                if (!empty($descFK)) {
                    $pkFK = $this->getFkPrimaryKey();
                    return $valor . ",(select " . $descFK . " from " . $this->fkTable . " a where a." . $pkFK . "=" . $this->getTable()->getTableName() . "." . $this->dbName . ") as " . $pkFK . SUFIXO_DESC;
                }
            }
            return $valor;
        }
    }

    function getFkDesc() {
        $tableManager = getManager()->getTableManager();
        $tabelaFKObj = $tableManager->getTabelaComNome($this->fkTable);
        $descFK = $tabelaFKObj->getDescName();
        return $descFK;
    }

    function getFkPrimaryKey() {
        $tableManager = getManager()->getTableManager();
        $tabelaFKObj = $tableManager->getTabelaComNome($this->fkTable);
        $descFK = $tabelaFKObj->getPkName();
        return $descFK;
    }

//retorna tabelas extras à clausula from
    function getFromSQL($sql) {
        $sql = ",$sql,";
        if (strpos($sql, ',' . $this->fkTable . ",") !== FALSE) {
            return "";
        }

        if (($this->fkTable) && ($this->filtroFK)) {
            return "," . $this->fkTable;
        }
    }

    function getWhereSQL() {
        if ($this->filtroFK) {
            return " and " . $this->filtroFK;
        }
    }

//sqlCalculo no estilo: select count(*) from tabela t where t.<pk_tabela>=
    function iniciaColunaCalculada($caption, $dbName, $sqlCalculo, $concatId = true) {
        $this->iniciaColuna($caption, $dbName, true, TIPO_INPUT_INTEIRO);
        $this->sqlCalculo = $sqlCalculo;
        $this->concatId = $concatId;
        return $this;
    }

//usar , como separador
    function defineOpcoes($opcoesID, $opcoesDesc) {
        $this->arrOpcoesID = explode(",", $opcoesID);
        $this->arrOpcoesDesc = explode(",", $opcoesDesc);
    }

    function getCopyFrom() {
        return $this->copyFrom;
    }

    function setCopyFrom($v) {
        $this->copyFrom = $v;
    }

    function setCondicao($v) {//coberto
        $this->condicao = $v;
    }

    function getCondicao() {
        return $this->condicao;
    }

    function setColuna1($v) {//coberto
        $this->coluna1 = $v;
    }

    function getColuna1() {//coberto
        return $this->coluna1;
    }

    function setColuna2($v) {//coberto
        $this->coluna2 = $v;
    }

    function getColuna2() {//coberto
        return $this->coluna2;
    }

}

?>