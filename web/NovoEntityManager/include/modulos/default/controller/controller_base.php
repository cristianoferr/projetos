<?

class BaseController extends ControllerAbstract implements IController {

    private $arrayComboBox;
//Textos
    protected $newItemLink;
    protected $defaultOrder;

    /**
     * Controller
     *
     * @param type $table
     * @param type $pluralKey
     * @param type $singularKey
     * @param LinkView $newItem
     */
    function BaseController(TabelaModel $table = null, $plural = null, $singular = null, LinkView $newItem = null) {
        $this->limpaRegistros();
        $this->table = $table;
        $this->pluralKey = $plural;
        $this->singularKey = $singular;
        $this->newItemLink = $newItem;
        $this->setDefaultOrderBy();

        $this->iniciaCorrigeDados();
    }

    function setDefaultOrderBy($order = "2") {//coberto
        $this->defaultOrder = $order;
    }

    function checkIfUserCanWrite(IModel $model) {
        return checaEscrita($this->table->getTableName(), $model->getId());
    }

    function iniciaCorrigeDados() {
        if ($this->table) {
            $idCorrige = "codad_" . $this->table->getTableName();
            if (!$_SESSION[$idCorrige]) {
                $this->corrigeDados();
                $_SESSION[$idCorrige] = true;
            }
        }
    }

    function getName($id) {//coberto
        trigger_error('Use getDescricao instead', E_USER_NOTICE);
        return $this->getDescricao($id);
    }

    function getDescricao($id_registro, $cached = true) {//coberto
        return $this->getFieldFromModel($id_registro, $this->table->getDescName(), $cached);
    }

    function getDescName() {
        return $this->table->getDescName();
    }

    function getProjeto() {//coberto
        erroFatal(get_class($this) . " doesnt implements getProjeto()");
    }

    function excluirRegistro($id) {//coberto
        erroFatal(get_class($this) . " doesnt implements excluirRegistro()");
    }

//returns an array with models
    function createModelTree($fieldName) {//coberto
        $arr = array();
        while ($model = $this->next()) {
            array_push($arr, $model);
        }



        return $this->createModelTreeFromArray($fieldName, $arr);
    }

    function createModelTreeFromArray($fieldName, $arr) {//coberto
        $arrFinal = array();
        $size = sizeof($arr);
        for ($c = 0; $c < $size; $c++) {
            $model = $arr[$c];
            array_push($arrFinal, $model);
        }
        for ($c = 0; $c < $size - 1; $c++) {
            $modelC = $arr[$c];
            for ($d = $c + 1; $d < $size; $d++) {
                $modelD = $arr[$d];

                $idPaiC = $modelC->getValorCampo($fieldName);
                $idPaiD = $modelD->getValorCampo($fieldName);
                $fd = ($modelC->addChildToTree($modelD, $idPaiD));
                $fc = ($modelD->addChildToTree($modelC, $idPaiC));

                if ($fd) {
                    unset($arrFinal[$d]);
                } else if ($fc) {
                    unset($arrFinal[$c]);
                }
            }
        }

        return $arrFinal;
    }

    function calculatePositions($arr, $nivel = 0) {
        $size = sizeof($arr);
        $left = 0;
        for ($c = 0; $c < $size; $c++) {
            $model = $arr[$c];
            $model->setLeft($left);
        }
    }

    /**
     * Gets a value on nm_field from the table at the record $id_registro
     * @param type $id_registro
     * @param type $nm_field
     * @return type
     */
    function getFieldFromModel($id_registro, $nm_field, $cached = true) {//coberto
        if ($cached) {
            $id_registro = validaNumero($id_registro, "id_registro getDescricao controller_base");
            $nm_field = validaTexto($nm_field, "nm_field getDescricao controller_base");

            $id = $nm_field . "_" . $this->table->getTableName() . "_" . $id_registro;
            if ($this->getCacheInfo($id)) {
                return $this->getCacheInfo($id);
            }
        }

        writeDebug("getFieldFromModel: $id_registro, $nm_field");
        if ($id_registro == "null") {
            erroFatal("null used as key.");
        }
        $model = $this->loadSingle($id_registro);
        if ($model) {
            $this->setCacheInfo($id, $model->getValorCampo($nm_field));
            return $this->getCacheInfo($id);
        }
    }

    function isValueWritable($idreg, $campo) {//coberto
        if (!checkPerm($this->table->getTableName(), $idreg, true)) {
            erroFatal("You dont have permission to change this: " . $this->table->getTableName());
            return false;
        }
        if (!$this->table->isColunaEditavel($campo)) {
            erroFatal("You cant change a readonly field! $campo");
            return false;
        }
        return true;
    }

    function atualizaValor($idreg, $campo, $novoValor) {//coberto
        if (!$this->isValueWritable($idreg, $campo)) {
            writeAdmin("not writable: $campo");
            return;
        }
        $chave = $this->table->getPkName();
        $campo = validaTexto($campo);
        $idreg = validaNumero($idreg);

//atualizo o valor em cache
        $id = $campo . "_" . $this->table->getTableName() . "_" . $idreg;
        $this->setCacheInfo($id, $novoValor);


        $sql = "update " . $this->table->getTableName() . " set $campo=? where $chave=?";
        //write($sql, "$novoValor, $idreg");
        executaSQL($sql, array($novoValor, $idreg));
    }

    function checkIfValueExists($field, $term, $filtro = null) {
//echo "$field,$term,$filtro";
        $tableName = $this->table->getTableName();
        $field = validaTexto($field, "validando $field checkIfValueExists");
        if (!$field) {
            return;
        }
        $id = "cive_" . $field . "_" . $term;
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }

        $sql = "select 1 from $tableName where lcase($field)=? $filtro";
//echo $sql;
        $r = existsQuery($sql, array(strtolower($term)));
        $this->setCacheInfo($id, $r);
        return ($r);
    }

    function getCampoExtraSelect() {
        return false;
    }

    function getOrderSelect() {
        return $this->table->getSortField();
    }

    function getGroupBySelect() {
        return false;
    }

//retorna uma array com ids e descricao
    function geraArrayParaColuna(IColuna $coluna, IModel $model = null, $filtro = null) {//coberto
        $tabelaFK = $coluna->getFkTable();
        $tabelaFKController = getControllerForTable($tabelaFK);
        return $tabelaFKController->geraArrayComChaveDescricao($coluna->getFiltroSelect() . $filtro);
    }

//usado em selects (do html) para mostrar chave e valor: retorna um dict
    function geraArrayComChaveDescricao($filtro = null) {//coberto
        if (isset($this->arrayComboBox)) {
            return $this->arrayComboBox;
        }

        $campoChave = $this->getPK();
        $campoDesc = $this->getDescName();

        if ((!$campoChave) || ((!$campoDesc))) {
            die("Campochave ($campoChave) ou campoDesc ($campoDesc) não definidos para tabela: " . $this->getDBName());
        }

        $ce = $this->getCampoExtraSelect();
        if ($ce) {
            $ce = ",$ce";
        }

        $sql = "select $campoChave as pk,$campoDesc as name $ce ";
        $sql.=$this->getCorpoSelect("", $campoChave, null) . $filtro;
        $sql.=" order by " . $this->getOrderSelect();

//  echo "sql combo: " . $sql;

        $dict = $this->fillDictFromQuery($sql);
        $this->arrayComboBox = $dict;
        return $dict;
    }

    function fillDictFromQuery($sql) {
        $rsValor = executaQuery($sql);
        $dict = new Dicionario();
        $count = 0;
        while ($rowValor = $rsValor->fetch()) {
            $aux = $this->table->getRotuloCondicaoSelect($rowValor);
            if ($this->isTraduzDescricao()) {
                $rowValor['name'] = translateKey($rowValor['name']);
            }
            $dict->setValor($rowValor['pk'], $rowValor, $aux);
            $count++;
        }
        return $dict;
    }

    function loadSingle($id_registro, $painel = null) {//coberto
        $id_registro = validaNumero($id_registro, "loadsingle controller_base");
        return $this->loadSingleText($id_registro, $painel = null);
    }

    function loadSingleText($id_registro, $painel = null) {//coberto
        $pkName = $this->table->getTableName() . "." . $this->table->getPkName();
        $this->loadRegistros(" and $pkName='$id_registro'", $painel);
        return $this->getModelByPk($id_registro);
    }

    function loadRegistros($filtro = null, $painel = null) {//coberto
        $pkName = $this->table->getTableName() . "." . $this->table->getPkName();
        if (!isset($filtro)) {
            $filtro = "";
        }

        $cols = "*";
        if (isset($painel)) {
            $cols = $painel->getSQLColunas($pkName);
        }
        $sql = "select " . $cols;
        $sql.=$this->getCorpoSelect($filtro, $pkName, $painel);
        $sql.=" order by " . $this->getOrderByPanel($painel);
        $this->loadFrom($sql);
    }

    function getOrderByPanel(IPainel $painel = null) {
        if ($painel) {
            $campo = $painel->getOrderbySQL();
            if ($campo != "") {
                return $campo;
            }
        }
        return $this->getOrderBy(); //a 2a coluna é geralmente a descricao
    }

    function getOrderBy() {
        return $this->defaultOrder; //a 2a coluna é geralmente a descricao
    }

    function countRegistros($filtro, $painel = null) {
        $pkName = $this->table->getTableName() . "." . $this->table->getPkName();

        $sql = "select count(*) as tot ";
        $sql.=$this->getCorpoSelect($filtro, $pkName, $painel);
        $row = executaQuerySingleRow($sql);
        return $row['tot'];
    }

    function getCorpoSelect($filtro, $pkName, $painel = null) {
        $tablePainel = "";
        $wherePainel = "";
        if (isset($painel)) {
            $tablePainel = $painel->getFromSQL($pkName);
            $wherePainel = $painel->getWhereSQL($pkName);
        }
        writeDebug("Tabelas  Extras:" . $this->tabelasExtras() . " TableName:" . $this->table->getTableName() . " do Painel:" . $tablePainel);
        $sql = "," . $this->table->getTableName() . ".* from " . $this->table->getTableName() . $this->tabelasExtras() . $tablePainel;
        $sql.=" where 1=1 $filtro " . $this->filtrosExtras() . " " . $wherePainel;
        return $sql;
    }

    function loadEmptyModel() {//coberto
        $model = new BaseModel($this, $this->table);
        return $model;
    }

    function carregaRegistro($row, $id = null) {//coberto
        $model = $this->loadEmptyModel();
        if (!$id) {
            $id = $row[$this->getPK()];
        }
        $model->carregaDados($id, $row);
        return $model;
    }

    function filtrosExtras() {
        return "";
    }

    function tabelasExtras() {
        return "";
    }

    function loadFrom($sql, $flagReset = true) {//coberto
        if ($flagReset) {
            $this->limpaRegistros();
        }
        $rsColuna = executaQuery($sql);
        $this->rowCount = 0;

        $c = 0;

        while ($row = $rsColuna->fetch()) {
            $model = $this->carregaRegistro($row);
            $this->adicionaRegistro($model);
            $c++;
        }

        writeDebug("c: $c rowCount: " . $this->rowCount);

        $this->rowAtual = 0;
    }

}

?>
