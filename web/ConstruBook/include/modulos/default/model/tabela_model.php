<?

class TabelaModel extends AbstractTable implements ITable {

    private $pkName;
    private $descName; //campo que descreve o registro, como "nm_projeto"
    private $paginaInclusao; //estilo "projeto.php"
    private $arrDependencias;
    private $flagTraduzDescricao;
    private $sortBy;
    private $dbid;
    private $arrCondicaoSelect; //condicao que será usada para separar as opções do select em grupo.

    function TabelaModel($tableName, $pkName = null, $descName = null) {
        parent::__construct($tableName);
        $this->pkName = $pkName;
        $this->descName = $descName;
        $this->arrDependencias = array();
        $this->flagTraduzDescricao = false;
        $this->arrCondicaoSelect = array();
    }

    //select é o input select do html... a idéia é dividir os options em grupos
    function addCondicaoSelect($valor, $condicao, $rotulo, $campo) {
        $c = new Condicao($condicao, $valor, $rotulo, $campo);
        array_push($this->arrCondicaoSelect, $c);
    }

    function getRotuloCondicaoSelect($row) {
        for ($c = 0; $c < sizeOf($this->arrCondicaoSelect); $c++) {
            $condicao = $this->arrCondicaoSelect[$c];
            $rotulo = $condicao->condicaoOk($row);
            if ($rotulo) {
                return $rotulo;
            }
        }
    }

    function addDependencia($nomeColunaBase, $valorColunaBase, $nomeColunaDependente, $flagForm) {
        $dependencia = new DependenciaDado($nomeColunaBase, $valorColunaBase, $nomeColunaDependente, $flagForm);
        array_push($this->arrDependencias, $dependencia);
    }

    function buscaDependentes($nomeColunaBase) {
        $arrDeps = array();
        for ($c = 0; $c < sizeOf($this->arrDependencias); $c++) {
            $dep = $this->arrDependencias[$c];
            if ($dep->getColunaBase() == $nomeColunaBase) {
                array_push($arrDeps, $dep);
            }
        }

        return $arrDeps;
    }

    function isColunaEditavel($nomeColuna) {
        $max = sizeOf($this->arrColunas);
        for ($c = 0; $c < $max; $c++) {
            $coluna = $this->arrColunas[$c];
            if ($coluna->getDbName() == $nomeColuna) {
                //echo $coluna->isReadonly()."!";
                return !$coluna->isReadonly();
            }
        }
        return false;
    }

    function setDbid($id) {
        $this->dbid = $id;
    }

    function getDbid() {
        return $this->dbid;
    }

    function setSortBy($v) {
        $this->sortBy = $v;
    }

    function getSortField() {
        if (!isset($this->sortBy)) {
            return $this->getDescName();
        }
        return $this->sortBy;
    }

    function traduzDescricao() {
        $this->flagTraduzDescricao = true;
    }

    function isTraduzDescricao() {
        return $this->flagTraduzDescricao;
    }

    function getPaginaInclusao() {
        return $this->paginaInclusao;
    }

    function setPaginaInclusao($v) {
        
        $this->paginaInclusao = $v;
    }

    function getPkName() {
        return $this->pkName;
    }

    function setPkName($v) {
        $this->pkName = $v;
    }

    function getDescName() {
        return $this->descName;
    }

    function getDescColuna() {
        return $this->getColunaWithName($this->descName);
    }

}

?>