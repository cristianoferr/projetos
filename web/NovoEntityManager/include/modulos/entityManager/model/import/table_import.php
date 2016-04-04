<?

class TableImport {

    private $table; //TabelaModel
    private $arrDeps; //array com dependencias dessa tabela, string do nome da tabela, vou removendo a medida que for adicionando tabelas sem dependencias
    private $elemento;
    private $arrCampos; //array com dados do campo
    private $projeto;
    private $pks;
    private $arrFks;

    function __construct($elemento, $projeto) {
        //$this->table=$table;
        $this->elemento = $elemento;
        $this->arrDeps = array();
        $this->arrCampos = array();
        $this->projeto = $projeto;
        $this->arrFks = array();
    }

    function addDeps($tableName) {
        if ($this->table->getTableName() == $tableName)
            return;
        array_push($this->arrDeps, $tableName);
    }

    function simplificaSql() {
        $elemento = trim($this->elemento);
        $elemento = str_replace(",", " , ", $elemento);
        $elemento = str_replace(")", " ) ", $elemento);
        $elemento = str_replace("  ", " ", $elemento);
        $elemento = str_replace("`", "", $elemento);
        $elemento = str_replace("if not exists", "", $elemento);

        $elemento = str_replace("not null", "notnull", $elemento);
        $elemento = str_replace("primary key", PK_TAG, $elemento);
        $elemento = str_replace("foreign key", FK_TAG, $elemento);
        $elemento = str_replace("(", " ( ", $elemento);
        $arr = multiexplode(array("create", "table", " "), $elemento);
        return $arr;
    }

    function trataSql() {
        //$table=new TabelaModel($this->arrElemento[0]);
        $arrPks = array();


        $arr = $this->simplificaSql();

        //writeAdmin("criando tabela",$arr[0]);
        $this->table = new TabelaModel($arr[0]);

        $tipo = 0; //1=pk,2=fk
        $campo = array(); //dados do campo: nome, tipo e outros
        $nivel = 0;

        for ($c = 1; $c < sizeof($arr); $c++) {

            $txt = $arr[$c];
            if ($arr[$c + 1] == ";")
                $c = sizeof($arr);
            //writeAdmin("txt[$c]",$txt);

            if ($txt == FK_TAG) {
                $c++;
                $c++;
                $txt = $arr[$c];
                array_push($campo, FK_TAG);
                array_push($campo, $txt);
                $c++;
                $txt = $arr[$c];
                while ($txt == ",") {
                    $c++;
                    $txt = $arr[$c];
                    array_push($campo, $txt);
                }

                $c++;
                $txt = $arr[$c];
                array_push($campo, $txt); //references

                $c++;
                $txt = $arr[$c];
                array_push($campo, $txt);
                $this->addDeps($txt);
                while ($txt != ")") {
                    $c++;
                    $txt = $arr[$c];
                    array_push($campo, $txt);
                }


                //writeAdmin("txt_fk",$txt);
                array_push($this->arrCampos, $campo);
                $campo = array();
                $c++;
            } else if ($txt == PK_TAG) {
                $c++;
                $c++;
                $txt = $arr[$c];
                array_push($campo, PK_TAG);
                array_push($campo, $txt);
                $c++;
                $txt = $arr[$c];
                if ($txt != ")")
                    array_push($campo, $txt);
                while ($txt == ",") {
                    $c++;
                    $txt = $arr[$c];
                    if ($txt != ")")
                        array_push($campo, $txt);
                }
                array_push($this->arrCampos, $campo);
                $campo = array();
                $c++;
            } else if ($tipo == 0) {
                if ((($txt == "int") || ($txt == "varchar") || ($txt == "char")) && ($arr[$c + 1] == "(")) {
                    array_push($campo, $txt);
                    $c++;
                    array_push($campo, "(");
                    $c++;
                    $txt = $arr[$c];
                    array_push($campo, $txt);
                    array_push($campo, ")");
                    $c++;
                } else {
                    if (($txt == "(") || ($txt == ")") || ($txt == ",")) {//fim do campo
                        if (sizeof($campo) > 0) {
                            array_push($this->arrCampos, $campo);
                            $campo = array();
                        }
                    } else {
                        array_push($campo, $txt);
                    }
                }
            }
        }

        $this->importaCampos();
    }

    function printDependencias() {
        for ($c = 0; $c < sizeof($this->arrDeps); $c++) {
            $campo = $this->arrDeps[$c];
            writeAdmin("dep[$c]", $campo);
        }
    }

    //removo tabelas que estão como dependencias mas que não foram importardas
    function removeDependenciasSemUso($arrTables) {
        for ($c = sizeof($this->arrDeps) - 1; $c >= 0; $c--) {
            $campo = $this->arrDeps[$c];
            $ok = false;
            for ($i = 0; $i < sizeof($arrTables); $i++) {
                $table = $arrTables[$i];
                //writeAdmin("$campo == ".$table->getTableName(),$this->getTableName());
                if ($table->getTableName() == $campo) {
                    $ok = true;
                }
            }
            if (!$ok) {
                writeAdmin("Removendo dependencia sem uso", $campo);
                $this->arrDeps = array_diff($this->arrDeps, array($campo));
            }
        }

        for ($c = 0; $c < sizeof($this->arrDeps); $c++) {
            $campo = $this->arrDeps[$c];
            //writeAdmin("dependencia[$c]",$campo);
        }
    }

    function getTableName() {
        return $this->table->getTableName();
    }

    function getTable() {
        return $this->table;
    }

    function importaCampos() {
        $table = $this->table->getTableName();
        writeAdmin("importaCampos", $table);

        for ($c = 0; $c < sizeof($this->arrCampos); $c++) {
            //writeAdmin("listando Campo[$c]");
            $campo = $this->arrCampos[$c];

            for ($i = 0; $i < sizeof($campo); $i++) {
                $txt = $campo[$i];
                //writeAdmin("i[$i]",$txt);
            }

            $this->importaCampo($campo);
        }



        if ($this->pks) {
            for ($i = 1; $i < sizeof($this->pks); $i++) {
                $txt = $this->pks[$i];

                if ($txt != ",") {
                    //writeAdmin("pk[$i]",$txt);
                    $this->setaCampoPK($txt);
                }
            }
        }
    }

    function possuiDependencia() {
        for ($c = sizeof($this->arrDeps) - 1; $c >= 0; $c--) {
            if (!$this->arrDeps[$c]) {
                unset($this->arrDeps[$c]);
                $this->arrDeps = array_values($this->arrDeps);
            }
        }
        return sizeof($this->arrDeps);
    }

    function buscaCampoComNome($nome) {
        $colunas = $this->table->getColunas();
        for ($c = 0; $c < sizeof($colunas); $c++) {
            $coluna = $colunas[$c];
            if ($coluna->getDbName() == $nome) {
                return $coluna;
            }
        }
    }

    function setaCampoPK($txt) {

        $coluna = $this->buscaCampoComNome($txt);
        if ($coluna) {
            $coluna->setRelacao(RELACAO_DATATYPE_PK);
            return;
        }
        writeAdmin("ERRO!", "$txt náo encontrado como PK!!!");
    }

    function importaCampo($campo) {
        if ($campo[0] == PK_TAG) {

            $this->table->setPkName($campo[1]);
            $this->pks = $campo;
        } else if ($campo[0] == FK_TAG) {
            array_push($this->arrFks, $campo);
        } else {
            $relacao = RELACAO_DATATYPE_OPCIONAL;
            $nome = $campo[0];
            $tipo = $campo[1];

            $pos_notnull = 2;
            $tamanho = null;
            if ($campo[2] == "(") {
                $tamanho = $campo[3];
                $pos_notnull = 5;
            }
            for ($i = $pos_notnull; $i < sizeof($campo); $i++) {
                $txt = $campo[$i];
                if ($txt == "notnull") {
                    $relacao = RELACAO_DATATYPE_OBRIGATORIO;
                }
            }
            $datatypeController = getControllerManager()->getControllerForTable("datatype");

            $coluna = new Coluna($this->table);
            $coluna->setDbName($nome);
            $coluna->setRelacao($relacao);
            //writeAdmin("campo",$nome." ".$campo[1]);

            $dt = $datatypeController->buscaDatatype($this->projeto, $tipo, $tamanho);
            if ($dt) {
                $coluna->setDatatype($dt);
                $this->table->addColuna($coluna);
            }
        }
    }

    function removeDependencia($table) {
        for ($c = sizeof($this->arrDeps) - 1; $c >= 0; $c--) {
            $tab = $this->arrDeps[$c];

            if ($tab == $table->getTableName()) {
                //echo "<br>".$this->getTableName()." --> Comparando $tab com ".$this->arrDeps[$c];
                //echo "   igual!!!";
                $this->arrDeps = array_diff($this->arrDeps, array($table->getTableName()));
                $this->relacionaFK($table);
            }
        }
    }

    function relacionaFK($table) {

        for ($i = 0; $i < sizeof($this->arrFks); $i++) {
            $arrFk = $this->arrFks[$i];
            $campo = $arrFk[1];
            $tabela = $arrFk[3];
            //writeAdmin("verificando campo","$campo $tabela ".$table->getTableName());
            if ($tabela == $table->getTableName()) {
                $coluna = $this->buscaCampoComNome($campo);
                $coluna->setFkTable($table->getTable());
                $fktable = $coluna->getFkTable();

                return;
            }
        }
        echo "FK não foi encontrado!! <br>";
    }

    function criaEntidade() {
        $entregavelController = getControllerManager()->getControllerForTable("entregavel");
        $id_entregavel = $entregavelController->getEntregavelRaiz();

        $entidadeController = getControllerManager()->getControllerForTable("entidade");
        $arr = array('projeto' => $this->projeto,
            'nm_entidade' => $this->getTableName(),
            'id_camada' => CAMADA_MODEL,
            'package_entidade' => "",
            'id_entregavel' => $id_entregavel);

        $id = $entidadeController->insereRegistro($arr);
        $this->table->setDbid($id);
        return $id;
    }

    function incluiEntidade() {

        $id_entidade = $this->criaEntidade();
        $colunaController = getControllerManager()->getControllerForTable("coluna");
        $entidadeController = getControllerManager()->getControllerForTable("entidade");

        $colunas = $this->table->getColunas();
        for ($c = 0; $c < sizeof($colunas); $c++) {
            $coluna = $colunas[$c];

            $fktable = $coluna->getFkTable();

            if ($fktable) {
                $fktable = $fktable->getDbid();
            } else {
                $fktable = "null";
            }

            $arr = array('entidade' => $id_entidade,
                'nm_coluna' => $coluna->getDbName(),
                'dbname_coluna' => $coluna->getDbName(),
                'prop_name_coluna' => $coluna->getDbName(),
                'id_entidade_combo' => $fktable,
                'id_sql_coluna' => SQL_COLUNA_CRIA_EXPORTA,
                'id_acesso_coluna' => ACESSO_COLUNA_PUBLIC,
                'id_datatype' => $coluna->getDatatype(),
                'id_relacao_datatype' => $coluna->getRelacao());

            $id_coluna = $colunaController->insereRegistro($arr);
        }




        //$sql="insert into coluna (id_entidade_pai,nm_coluna,dbname_coluna,prop_name_coluna,id_entidade_combo,id_sql_coluna,id_acesso_coluna,id_datatype,id_relacao_datatype) values ";
        //		$sql.="(:entidade,:nm_coluna,:dbname_coluna,:prop_name_coluna,:id_entidade_combo,:id_sql_coluna,:id_acesso_coluna,:id_datatype,:id_relacao_datatype)";
    }

}

?>