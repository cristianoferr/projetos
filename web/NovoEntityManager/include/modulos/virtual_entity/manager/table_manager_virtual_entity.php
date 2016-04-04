<?

class TableManagerVirtualEntidade extends TableManager {

    function carregaTabelaComNome($tableName) {


        if ($tableName == "virtual") {
            return $this->carregaTabelaVirtual();
        }
    }

    function carregaTabelaVirtual() {
        $table = new TabelaVirtualModel();
        return $table;
    }

}

?>