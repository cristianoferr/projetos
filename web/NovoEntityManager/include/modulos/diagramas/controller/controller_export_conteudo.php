<?php

/**
 * Description of controller_export_conteudo
 *
 * @author CMM4
 */
class ConteudoExportController extends BaseController {

    public $colunaController;
    public $projetoController;
    public $entidadeController;
    public $valorController;

    function __construct() {
        $this->colunaController = getControllerManager()->getControllerForTable("coluna");
        $this->projetoController = getControllerManager()->getControllerForTable("projeto");
        $this->entidadeController = getControllerManager()->getControllerForTable("entidade");
        $this->valorController = getControllerManager()->getControllerForTable("valor");
    }

    function conteudoProjeto($id_projeto, $id_entregavel_raiz) {
        $id_projeto = validaNumero($id_projeto, "id_projeto conteudoProjeto");

        $matrizController = new MatrizDependenciaController();
        $indices = $matrizController->calculaOrdemInsert($id_projeto, $id_entregavel_raiz);

        $colsSize = sizeof($indices);

        $saida = "";
        $sql_delete = "\n-- Delete rows\n";
        for ($row = 0; $row < $colsSize; $row++) {
            $saida.=$this->geraSQLInsertEntidade($indices[$row]);
            $sql_delete .= $this->geraSQLDeleteEntidade($indices[$row]);
        }
        return $sql_delete . "\n" . $saida;
    }

    function geraSQLDeleteEntidade($id_entidade) {
//		echo "geraSQLInsertEntidade($id_entidade)<br>";
        $rowEntidade = executaQuerySingleRow("select * from entidade where id_entidade=?", array($id_entidade));
        $table = $rowEntidade['dbname_entidade'];
        return "delete from $table;\n";
    }

    function geraSQLInsertEntidade($id_entidade) {
        $table = $this->entidadeController->getFieldFromModel($id_entidade, "dbname_entidade");
        $campos = "";
        $valores = "";

        //inicioDebug();
        $rsValores = $this->valorController->getRSValores($id_entidade);
       //  fimDebug();
        $id_reg = -1;
        $sql = "\n-- $table\n";
        $sqlRedirect = "";
        while ($rowValores = $rsValores->fetch()) {
            $dbname = $rowValores['dbname_coluna'] ? $rowValores['dbname_coluna'] : translateKey("txt_undefined");
            $id_registro = $rowValores['id_registro'];
            $id_primitive_type = $rowValores['id_primitive_type'];
            $id_entidade_combo = $rowValores['id_entidade_combo'];
            $flag_export = $rowValores['flag_export'];
            $id_coluna = $rowValores['id_coluna_pai'];

            if ($id_reg != $id_registro) {
                $id_reg = $id_registro;

                if ($campos != "") {
                    $sql.=$this->getInsertString($table, $campos, $valores);
                    $campos = "";
                    $valores = "";
                }
            }

            $valor = $rowValores['valor_coluna'];
            //se for texto
            if (getControllerForTable("primitive_type")->isTexto($id_primitive_type)) {
                $valor = "'$valor'";
            }

            //$valor.="!$id_entidade_combo!";
            if ((trim($rowValores['valor_coluna']) != "") && ($id_entidade_combo != "")&& ($id_entidade_combo != "0")) {
               // write("valor:$valor '".$id_entidade_combo."'");

               // $valor = $this->getValorCombo($id_entidade_combo, $rowValores['valor_coluna']);
            }


            if ($flag_export == "T") {
                $campos.=$dbname . ",";
                $valores.=$valor . ",";
            }
        }

        if ($campos != "") {
            $sql.=$this->getInsertString($table, $campos, $valores);
        }

        //sql dos redirects
        if ($sqlRedirect != "") {
            $sql.="\n-- redirects\n" . $sqlRedirect;
        }


        return $sql;
    }

    function getValorCombo($id_entidade_combo, $valor_coluna) {
        $sqlCombo = "select valor_coluna from registro r,valor v,coluna c,entidade e ";
        $sqlCombo.=" where v.id_coluna_pai=c.id_coluna and c.id_entidade_pai=? and e.id_entidade=c.id_entidade_pai and e.id_coluna_pk=c.id_coluna ";
        $sqlCombo.=" and r.id_registro=v.id_registro and r.id_registro=?";
        //echo "ent:$id_entidade_combo sql:$sqlCombo";
        $rowValorCombo = executaQuerySingleRow($sqlCombo, array($id_entidade_combo, $valor_coluna));
        return $rowValorCombo['valor_coluna'];
    }

    function getInsertString($table, $campos, $valores) {//coberto
        $campos.="#";
        $valores.="#";
        $table = strtolower($table);
        $campos = str_replace(",#", "", strtolower($campos));
        $valores.=")";
        $valores = str_replace(",#", "", $valores);
        $valores = str_replace(",)", ",null)", $valores);
        $valores = str_replace(",,", ",null,", $valores);
        $sql = "insert into $table ($campos) values ($valores;\n";
        return $sql;
    }

}

?>
