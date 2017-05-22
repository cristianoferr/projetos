<?

interface ITable {

    function addColuna(IColuna $coluna);

    function getColunaWithName($name);

    function getColunas();

    function getTableName();
}

class AbstractTable {

    protected $tableName;
    protected $arrColunas; //todas as colunas, independente se vai aparecer ou não

    function __construct($tableName) {
        $this->arrColunas = array();
        $this->tableName = $tableName;
    }

    function getColunas() {
        return $this->arrColunas;
    }

    function getTableName() {
        return $this->tableName;
    }

    function addColuna(IColuna $coluna) {
        //echo "addColuna: ".get_class($coluna)."<br>";
        array_push($this->arrColunas, $coluna);
    }

    function getColunaWithName($name) {
        for ($c = 0; $c < sizeOf($this->arrColunas); $c++) {
            $coluna = $this->arrColunas[$c];
            if ($coluna->getDbName() == $name) {
                return $coluna;
            }
        }
        return false;
    }

    function loadAllColumnsIntoPanel(IPainel $painel) {
        foreach ($this->arrColunas as $coluna) {
            $painel->addColunaWithDBName($coluna->getDbName());
        }
    }

}

?>