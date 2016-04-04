<?

class ImportParser {

    private $sqlLower;
    private $sql;
    private $arrTables; //array de ElementoImport
    private $sqlRestante;
    private $projeto;

    function __construct($sql, $projeto) {
        //writeAdmin("SQL inicial","<pre>$sql</pre>");
        $sql = $this->removeComentario("  " . $sql . "  ");
        $this->sqlLower = strtolower($sql);
        $this->sql = $sql;
        $this->arrTables = array();
        $this->projeto = $projeto;
    }

    function trataTables() {
        for ($c = 0; $c < sizeof($this->arrTables); $c++) {
            $table = $this->arrTables[$c];
            $table->trataSql();
        }


        for ($c = 0; $c < sizeof($this->arrTables); $c++) {
            $table = $this->arrTables[$c];
            $table->removeDependenciasSemUso($this->arrTables);
        }


        $this->realizaImportacaoFinal();
    }

    function importaTabelas() {
        for ($c = sizeof($this->arrTables) - 1; $c >= 0; $c--) {
            $table = $this->arrTables[$c];

            $table->removeDependenciasSemUso($this->arrTables);

            if ($table) {
                if (!$table->possuiDependencia()) {
                    writeAdmin("Importando", $table->getTableName());
                    $table->incluiEntidade();
                    $this->removeDependencia($table);
                    //$this->arrTables = array_diff($this->arrTables, array($table->getTableName));
                    unset($this->arrTables[$c]);
                    $this->arrTables = array_values($this->arrTables);
                }
            } else {
                unset($this->arrTables[$c]);
                $this->arrTables = array_values($this->arrTables);
            }
        }
    }

    function realizaImportacaoFinal() {
        $tam = 0;
        while (($tam != sizeof($this->arrTables)) && (sizeof($this->arrTables) > 0)) {
            writeAdmin("tam=$tam", "size:" . sizeof($this->arrTables));
            $tam = sizeof($this->arrTables);
            $this->importaTabelas();
        }
        writeAdmin("final tam=$tam", "size:" . sizeof($this->arrTables));



        if (sizeof($this->arrTables) > 0) {
            writeAdmin("ERRO!!", "Não foram importadas todas as tabelas, faltou " . sizeof($this->arrTables));

            for ($c = 0; $c < sizeof($this->arrTables); $c++) {
                writeAdmin("====== tab[$c]", $this->arrTables[$c]->getTableName());
                $this->arrTables[$c]->printDependencias();
            }
        }
    }

    function removeDependencia($table) {
        for ($c = 0; $c < sizeof($this->arrTables); $c++) {
            $t = $this->arrTables[$c];
            $t->removeDependencia($table);
        }
    }

    function removeComentario($sql) {
        while (strpos($sql, "--")) {
            $pos = strpos($sql, "--");
            $txtRemove = substr($sql, $pos, strpos($sql, PHP_EOL, $pos) - $pos);
            $sql = str_replace($txtRemove, "", $sql);
        }

        while (strpos($sql, "/*")) {
            $pos = strpos($sql, "/*");
            $txtRemove = substr($sql, $pos, strpos($sql, "*/", $pos) - $pos + 2);
            $sql = str_replace($txtRemove, "", $sql);
        }
        return $sql;
    }

    function extraiElementos() {
        $arrElementos = multiexplode(array(";"), $this->sqlLower);
        for ($c = 0; $c < sizeof($arrElementos); $c++) {
            $elemento = $arrElementos[$c];
            if (constainsSubstring($elemento, "create table ")) {

                $elemento = substr("  " . $elemento, strpos("  " . $elemento, "create table ") - 2);
                //$elemento=substr($elemento, 0,strpos($elemento,";"));
                //writeAdmin("elemento",$elemento);
                $table = new TableImport($elemento, $this->projeto);
                $this->addTable($table);
            }
        }
    }

    function addTable($table) {
        array_push($this->arrTables, $table);
    }

    //retorna um array com os elementos do sql separados, "create table tabela (campo tipo,etc)"" retorna então uma array[tabela,campo tipo,etc] 
    function extraiEstrutura($sql, $tagInicial, $tagInicioElemento, $tagItem, $tagFinal) {
        $arr = array();
        if (!(strpos($sql, $tagInicial)))
            return $arr;
        $sql = str_replace($tagFinal, $tagItem . $tagFinal, $sql);

        //Separo o nome da tabela
        $sql = substr($sql, strpos($sql, $tagInicial));

        $posInicial = strpos($sql, $tagInicioElemento);
        $nome = substr($sql, 0, $posInicial);
        $nome = trim(str_replace($tagInicial, "", $nome));
        array_push($arr, $nome);

        $c = 0;
        $sql = substr($sql, strpos($sql, $tagInicioElemento) + 1);
        $posItem = strpos($sql, $tagItem);
        $posFinal = strpos($sql, $tagFinal);
        //writeAdmin("extraiEstrutura","<pre>$sql</pre>");
        while ($posItem < $posFinal) {
            $posItem = strpos($sql, $tagItem);
            $posFinal = strpos($sql, $tagFinal);
            if ($posFinal) {
                $elemento = substr($sql, 0, $posItem);
                $sql = substr($sql, $posItem + 1);
                //writeAdmin("posicoes","posItem:$posItem posFinal:$posFinal");
                //writeAdmin("elemento",$elemento);
                //writeAdmin("sql",$sql);
                if (trim($elemento)) {
                    array_push($arr, $elemento);
                }

                //$posItem=$posFinal;
                $c++;
            }
        }

        writeAdmin("sql Restante", $sql);
        $this->sqlRestante = $sql;
        return $arr;
    }

}

?>