<?php

class ExportController extends ConteudoExportController {

    private $arrDrop;
    private $arrCreate;

    function __construct() {
        parent::__construct();
        $this->arrDrop = array();
        $this->arrCreate = array();
    }

    /* function addSaida($s) {
      array_push($this->arrSaida, $s);
      } */

    function addDropTable($s) {
        array_push($this->arrDrop, $s);
    }

    function addCreateTable($s) {
        array_push($this->arrCreate, $s);
    }

    function buildProjeto($id_projeto, $id_entregavel_raiz) {
        //writeAdmin("projeto: $id_projeto");
        $id_projeto = validaNumero($id_projeto, "id_projeto buildProjeto");
        $id_entregavel_raiz = validaNumero($id_entregavel_raiz, "id_entregavel_raiz buildProjeto");
        $rsEntidade = $this->entidadeController->consultaEntidadesProjeto($id_projeto, $id_entregavel_raiz);

        $count = 0;


        while ($rowEntidade = $rsEntidade->fetch()) {
            $id_entidade = $rowEntidade['id_entidade'];
            $nm_entidade = $rowEntidade['nm_entidade'];
            $dbname_entidade = $rowEntidade['dbname_entidade'];
            $this->addCreateTable("");
            $this->addCreateTable("-- $nm_entidade --");
            //writeAdmin("nm_entidade: $nm_entidade");

            $this->addDropTable("drop table $dbname_entidade;");
            $this->criaScriptCreateTable($id_entidade, $dbname_entidade, $id_projeto);
            $count++;
        }


        return $this->consolidaSaidaEstrutura();
    }

    function criaScriptCreateTable($id_entidade, $tableName, $id_projeto) {
        $this->addCreateTable("create table $tableName (");

        $pks = "";
        $saidaFKS = "";
        $separador = ",";

        $rsColunas = $this->colunaController->getRSColunas($id_entidade);

        while ($rowColuna = $rsColunas->fetch()) {
            $id_coluna = $rowColuna['id_coluna'];
            $id_relacao_datatype = $rowColuna['id_relacao_datatype'];
            //$nm_coluna = $modelColuna->getValorCampo('nm_coluna');
            // $nm_primitive_type = $modelColuna->getValorCampo('nm_primitive_type');
            $id_primitive_type = $rowColuna['id_primitive_type'];
            $id_entidade_combo = $rowColuna['id_entidade_combo'];
            $id_datatype = $rowColuna['id_datatype'];
            $flag_create = $rowColuna['flag_create'];
            $dbname_coluna = $rowColuna['dbname_coluna'];

            //  writeAdmin("id_entidade_combo:$id_entidade_combo");

            $dbname = $dbname_coluna;
            if ($dbname == "") {
                $dbname = translateKey("txt_undefined");
            }

            $nullable = "not null";
            $auto_inc = "";
            if ($flag_create == "T") {

                if ($id_relacao_datatype == RELACAO_DATATYPE_OPCIONAL)
                    $nullable = ""; //opcional
                if ($id_relacao_datatype == RELACAO_DATATYPE_PK) {
                    if ($pks != "")
                        $pks.=",";
                    $pks.=$dbname;

                    if ($this->colunaController->isAutoIncrement($id_coluna, $id_projeto, $id_primitive_type)) {
                        $auto_inc.="AUTO_INCREMENT ";
                    }
                }

                $sqlDado = getControllerForTable("datatype")->getSqlTipo($id_datatype, $id_projeto);

                $coluna = "$dbname " . $sqlDado . " $nullable $auto_inc $separador";
                $this->addCreateTable($coluna);
                if ($id_entidade_combo > 0) {
                    $saidaFKS = addLine($saidaFKS, $this->criaSQLExportFK($dbname, $id_entidade_combo) . $separador);
                }
            }
        }



        if ($saidaFKS != "") {
            $this->addCreateTable($saidaFKS);
        }
        if ($pks != "") {
            $coluna = "PRIMARY KEY ($pks)$separador";
            $this->addCreateTable($coluna);
        }
        $this->addCreateTable(");");
    }

    function criaSQLExportFK($dbname, $id_entidade_combo) {
        $entidade = $this->entidadeController->getFieldFromModel($id_entidade_combo, "dbname_entidade");
        $id_coluna_fk = $this->entidadeController->getPKForEntidade($id_entidade_combo);
        $coluna_fk = $this->colunaController->getFieldFromModel($id_coluna_fk, "dbname_coluna");

        $saida = "FOREIGN KEY($dbname) REFERENCES $entidade($coluna_fk)";
        return $saida;
    }

    function consolidaSaidaEstrutura() {
        $saida = "";
        for ($c = 0; $c < sizeof($this->arrDrop); $c++) {
            $saida.=$this->arrDrop[$c] . PHP_EOL;
        }

        $saida.=PHP_EOL;

        for ($c = 0; $c < sizeof($this->arrCreate); $c++) {
            $saida.=$this->arrCreate[$c] . PHP_EOL;
        }
        $saida = str_replace("," . PHP_EOL . ");", PHP_EOL . ");", $saida);

        $saida = str_replace(PHP_EOL . PHP_EOL, PHP_EOL, $saida);
        $saida = str_replace(" ,", ",", $saida);
        $saida = str_replace(PHP_EOL . "--", PHP_EOL . PHP_EOL . "--", $saida);
        return $saida;
    }

}

?>
