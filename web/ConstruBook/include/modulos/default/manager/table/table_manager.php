<?

function searchTable($tableName) {
    return TableManager::searchTable($tableName);
}

//guarda informações sobre as tabelas físicas do sistema, não as tabelas virtuais (entidades) do projeto
class TableManager {

    public static function searchTable($tableName) {//coberto
        $id = "table_$tableName";

        if (isset($GLOBALS[$id])) {
            return $GLOBALS[$id];
        }
        $c = getFileManager()->getTableWithName($tableName);
        if (!$c) {
            erroFatal("Table $tableName not loaded.");
        }

        $GLOBALS["contaTables"] = $GLOBALS["contaTables"] + 1;
        $GLOBALS[$id] = $c;
        return $c;
    }

    /* function addTabela($tabela){
      array_push($this->arrTables,$tabela);
      } */

    function getTabelaComNome($tableName) {//coberto
        $id = "table_$tableName";
        if (isset($GLOBALS[$id])) {
            return $GLOBALS[$id];
        }

        $tm = $this->carregaTabelaComNome($tableName);
        if ($tm) {
            $GLOBALS["contaTables"] = $GLOBALS["contaTables"] + 1;
            $GLOBALS[$id] = $tm;
            return $tm;
        }
        return TableManager::searchTable($tableName);
    }

    function carregaTabelaComNome($tableName) {//coberto
        return false;
    }

    /**
     * Coluna texto normal (podendo variar o tipo)
     * @param type $table
     * @param type $caption
     * @param type $dbname
     * @param type $flagReadonly
     * @param type $tipo
     * @param type $tamanho
     * @return \Coluna
     */
    function adicionaColunaNormal($table, $caption, $dbname, $flagReadonly, $tipo, $tamanho = 250) {//coberto
        $coluna = new Coluna($table);
        //echo "adicionaColunaNormal $dbname";
        $table->addColuna($coluna->iniciaColuna($caption, $dbname, $flagReadonly, $tipo, $tamanho));
        return $coluna;
    }

    function adicionaColunaCalculada($table, $caption, $dbname, $sql, $concatId = true) {//coberto
        $coluna = new Coluna($table);
        //echo "adicionaColunaCalculada $dbname";
        $table->addColuna($coluna->iniciaColunaCalculada($caption, $dbname, $sql, $concatId));
        return $coluna;
    }

    /**
     * Adiciona uma coluna com fk (combo box)
     * @param type $table
     * @param type $caption (opcional)
     * @param type $dbname
     * @param type $flagReadonly
     * @param type $flagAllowNull
     * @param type $fktablename
     * @param type $tipo
     * @return \Coluna
     */
    function adicionaColunaComFK($table, $caption, $dbname, $flagReadonly, $flagAllowNull, $fktablename, $tipo) {//coberto
        $coluna = new Coluna($table);
        //echo "adicionaColunaComFK $dbname";
        $coluna->setFlagAllowNull($flagAllowNull);
        $table->addColuna($coluna->iniciaColunaComFK($caption, $dbname, $flagReadonly, $fktablename, $tipo));
        return $coluna;
    }

    function adicionaColunaDescreveFK($table, $caption, $dbname, $fktable, $filtro) {//coberto
        $tableFK = $this->getTabelaComNome($fktable);
        if (!$tableFK) {
            erroFatal("TableManager: Tabela $fktable não encontrada.");
        }
        $coluna = new Coluna($tableFK);
        //echo "adicionaColunaDescreveFK $dbname";
        $table->addColuna($coluna->iniciaColunaDescreveFK($caption, $dbname, $fktable, $filtro));
        return $coluna;
    }

    function adicionaColunaComposta($table, $name, $caption, $caption1, $dbname1, $fktable1, $caption2, $dbname2, $fktable2, $flagReadonly, $tipoSub, $condicao) {//coberto
        $tipo = TIPO_INPUT_2_SELECT_OR;

        $tableFK1 = $this->getTabelaComNome($fktable1);
        if (!$tableFK1) {
            erroFatal("TableManager: 1 Tabela $fktable não encontrada.");
        }
        $coluna1 = new Coluna($tableFK1);
        $coluna1->iniciaColunaComFK($caption1, $dbname1, $flagReadonly, $fktable1, $tipoSub);

        $tableFK2 = $this->getTabelaComNome($fktable2);
        if (!$tableFK2) {
            erroFatal("TableManager: 2 Tabela $fktable2 não encontrada.");
        }
        $coluna2 = new Coluna($tableFK2);
        $coluna2->iniciaColunaComFK($caption2, $dbname2, $flagReadonly, $fktable2, $tipoSub);

        $coluna = new Coluna($table);

        $coluna->setColuna1($coluna1);
        $coluna->setColuna2($coluna2);
        $table->addColuna($coluna->iniciaColunaComposta($caption, $name, $flagReadonly, $tipo));
        $coluna->setCondicao($condicao);

        return $coluna;
    }

    function carregaTabelas() {
        
    }

}

?>
