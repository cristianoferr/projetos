<?

interface IController {

    function atualizaValor($idreg, $campo, $novoValor);

    function isValueWritable($idreg, $campo);

    function checkIfUserCanWrite(IModel $model);

    function excluirRegistro($id);

    function getNewItem();

    function next();

    function first();

    function geraArrayParaColuna(IColuna $coluna, IModel $model = null, $filtro = null);

    function loadSingle($id_registro, $painel = null);

    function loadRegistros($filtro = null, $painel = null);

    function loadEmptyModel();

    function getRowCount();

    function getTable();

    function adicionaRegistro(IModel $model = null);

    function getModelByPk($pk);
}

abstract class ControllerAbstract {

    protected $arrModels; //contem os modelos
    protected $rowCount;
    protected $rowAtual;
    protected $table;
    protected $pluralKey;
    protected $singularKey;
    private $cacheDict; //usado para reutilizar informacoes ...

    function limpaRegistros() {
        $this->arrModels = array();
        $this->rowAtual = 0;
        $this->rowCount = 0;
    }

    function getModelByPk($pk) {
        foreach ($this->arrModels as $model) {
            if ($model->getId() == $pk) {
                return $model;
            }
        }
    }

    function showInfo() {
        writeDebug("showInfo()", "rowAtual:" . $this->rowAtual . " \n<br>rowCount:" . $this->rowCount . "\n<br>Size arrModels:" . sizeof($this->arrModels));
    }

    function next() {
        if ($this->rowAtual < $this->rowCount) {
            $this->rowAtual++;
            return $this->arrModels[$this->rowAtual - 1];
        } else {
            
        }
    }

    function first() {
        $this->rowAtual = 0;
    }

    function getRowCount() {
        return $this->rowCount;
    }

    function getPlural() {
        if (!$this->pluralKey) {
            erroFatal("Plural undefined: " . get_class($this));
        }
        return translateKey($this->pluralKey);
    }

    function isTraduzDescricao() {
        return $this->table->isTraduzDescricao();
    }

    function getSingular() {
        if (!$this->singularKey) {
            erroFatal("Singular undefined:" . get_class($this));
        }
        return translateKey($this->singularKey);
    }

    function getNewItem() {
        if (!$this->newItemLink) {
            erroFatal("New Item undefined:" . get_class($this));
        }
        return $this->newItemLink;
    }

    function corrigeDados() {
        
    }

    function getCache() {
        if (!$this->cacheDict) {
            $v = new DicionarioSession("concac");
            $this->cacheDict = $v;
        }
        return $this->cacheDict;
    }

    function getCacheInfo($var) {
        $cache = $this->getCache();
        return $cache->getValor($var);
    }

    function setCacheInfo($var, $val) {
        $cache = $this->getCache();
        return $cache->setValor($var, $val);
    }

    function adicionaRegistro(IModel $model = null) {
        if (!$model) {
            return;
        }
        $this->arrModels[$this->rowCount] = $model;
        $this->rowCount++;
    }

    function getDBName() {
        return $this->table->getTableName();
    }

    function getPK() {
        return $this->table->getPkName();
    }

    function getDescName() {
        return $this->table->getDescName();
    }

    function getTable() {
        return $this->table;
    }

}

?>
