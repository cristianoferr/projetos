<?

class DatatypeController extends BaseController {

    function filtrosExtras() {
        return " and datatype.id_projeto=" . projetoAtual();
    }

    function getName($id) {
        return $this->getFieldFromModel($id, "nm_datatype");
    }

    function getTipoDadoFrom($id_datatype) {//coberto
        $model = $this->loadSingle($id_datatype);
        $primitive = $model->getValorCampo("id_primitive_type");
        $tipo = TIPO_INPUT_TEXTO_CURTO;
        if ($primitive == 1) {//integer
            $tipo = TIPO_INPUT_INTEIRO;
        }
        if ($primitive == 2) {//texto limitado
            $tipo = TIPO_INPUT_TEXTO_CURTO;
        }
        if ($primitive == 3) {//texto sem limite
            $tipo = TIPO_INPUT_TEXTAREA;
        }
        if ($primitive == 4) {//boolean
            $tipo = TIPO_INPUT_BOOLEAN;
        }
        if ($primitive == 5) {//data
            $tipo = TIPO_INPUT_DATA;
        }
        if ($primitive == 6) {//data
            $tipo = TIPO_INPUT_FLOAT;
        }
        if ($primitive == 7) {//char
            //  $tipo=TIPO_INPUT_;
        }
        if ($primitive == 9) {//data
            $tipo = TIPO_INPUT_SELECT_FK;
        }

        return $tipo;
    }

    //retorna o tipo do objeto para o datatype selecionado, exemplo "int" e "varchar(50)" 
    function getSqlTipo($id_datatype, $id_projeto, $id_tipo_banco = null) {
        if (!$id_tipo_banco)
            $id_tipo_banco = getControllerForTable("projeto")->getFieldFromModel($id_projeto, "id_tipo_banco");
        $rowPT = executaQuerySingleRow("select pt.id_primitive_type,codigo_primitive " .
                " from primitive_type_banco ptb,primitive_type pt,datatype d" .
                " where d.id_datatype=? and d.id_primitive_type=pt.id_primitive_type and ptb.id_primitive_type=pt.id_primitive_type and id_tipo_banco=?", array($id_datatype, $id_tipo_banco));
        $saida = $rowPT["codigo_primitive"];
        //writeAdmin("codigo_primitive", $saida);

        $rsParams = executaQuery("select * from datatype_param where id_datatype=?", array($id_datatype));
        $c = 0;
        $par = "";
        while ($row = $rsParams->fetch()) {
            $par .= $row["valor_param"] . ",";
            $c++;
        }
        $par.="#";

        $par = replaceString($par, ",#", "");

        if ($c > 0) {
            $saida.="(" . $par . ")";
        }

        return $saida;
    }

    function insereRegistro($array) {
        $array['id_projeto'] = validaNumero($array['id_projeto'], "id_projeto insereRegistro EntidadeController");
        validaEscrita("projeto", $array['id_projeto']);
        //echo "$nm_projeto,$iniciais_projeto,$package_projeto,$id_tipo_banco,$id_metodologia";
        $array['nm_datatype'] = validaTexto($array['nm_datatype']);
        $array['id_primitive_type'] = validaTexto($array['id_primitive_type']);
        $array['desc_datatype'] = validaTexto($array['desc_datatype']);


        $sql = "insert into datatype (id_projeto,nm_datatype,id_primitive_type,desc_datatype) values ";
        $sql.="(:id_projeto,:nm_datatype,:id_primitive_type,:desc_datatype)";
        $id = executaSQL($sql, $array);

        $this->insereDataypeParam($array['id_projeto'], $array['id_primitive_type'], $id);

        return $id;
    }

    function buscaDatatype($projeto, $tipo, $tamanho = null) {
        $tipo = strtolower($tipo);
        $tipoPrimitivo = -1;
        $param = null;
        //writeAdmin("buscaDatatype, tipo=",$tipo);
        if (($tipo == "integer") || ($tipo == "mediumint") || ($tipo == "bigint") || ($tipo == "int")) {
            $tipoPrimitivo = PRIMITIVE_INT;
        }

        if (($tipo == "date") || ($tipo == "timestamp")) {
            $tipoPrimitivo = PRIMITIVE_DATE;
        }

        if (($tipo == "float")) {
            $tipoPrimitivo = PRIMITIVE_FLOAT;
        }
        if (($tipo == "varchar") || ($tipo == "text") || ($tipo == "char")) {
            $tipoPrimitivo = PRIMITIVE_SHORT_TEXT;
            if ($tamanho) {
                if ($tamanho == 1) {
                    $tipoPrimitivo = PRIMITIVE_CHAR;
                } else {
                    $tipoPrimitivo = PRIMITIVE_SHORT_TEXT;
                    $param = $tamanho;
                }
            } else {
                $tipoPrimitivo = PRIMITIVE_TEXT;
            }
        }

        if ($tipoPrimitivo == -1) {
            //die("Tipo $tipo desconhecido!!");//TODO: remover esse codigo
            writeAdmin("Tipo '$tipo' desconhecido!!!!!!!!!!");
            $tipoPrimitivo = PRIMITIVE_INT;
            return;
        }

        //writeAdmin("tipoPrimitivo=$tipoPrimitivo param=$param");

        $nm = $tipo;

        $tipoPrimitivo = validaNumero($tipoPrimitivo, "tipoPrimitivo controller datatype");
        if ($param) {
            $nm = "$tipo($param)";
            $param = validaNumero($param, "param controller datatype");
            $row = executaQuerySingleRow("select datatype.id_datatype from datatype ,datatype_param dp where datatype.id_datatype=dp.id_datatype and dp.valor_param=? and id_primitive_type=? " . $this->filtrosExtras(), array($param, $tipoPrimitivo));
        } else {
            //echo "select id_datatype from datatype  where id_primitive_type=? ".$this->filtrosExtras();
            $row = executaQuerySingleRow("select id_datatype from datatype  where id_primitive_type=? " . $this->filtrosExtras(), array($tipoPrimitivo));
        }

        if ($row) {
            return $row['id_datatype'];
        }

        writeAdmin("criando datatype... projeto: $projeto  tipoPrimitivo:$tipoPrimitivo tipo: $tipo param:$param");


        $arr = array('id_projeto' => $projeto,
            'nm_datatype' => $nm,
            'id_primitive_type' => $tipoPrimitivo,
            'desc_datatype' => translateKey("txt_datatype_criado_automatico"));

        $id = $this->insereRegistro($arr);
        if ($param) {
            executaSQL("update datatype_param set valor_param=$param where id_datatype=$id ");
        }

        return $id;


        /*

          define("PRIMITIVE_INT",     1);
          define("PRIMITIVE_SHORT_TEXT",     2);
          define("PRIMITIVE_TEXT",     3);
          define("PRIMITIVE_BOOLEAN",     4);
          define("PRIMITIVE_DATE",     5);
          define("PRIMITIVE_FLOAT",     6);
          define("PRIMITIVE_CHAR",     8); */
    }

    function insereDatatypesDefault($id_projeto) {
        $sql = "select * from datatype where id_projeto is null";
        $rsDatatype = executaQuery($sql);
        //echo "fillSelectCombobox:$sql";

        while ($rowData = $rsDatatype->fetch()) {
            $id_to = executaSQL("insert into datatype (nm_datatype,id_primitive_type,id_projeto,desc_datatype) values (?,?,?,?)", array(translateKey($rowData['nm_datatype']), $rowData['id_primitive_type'], $id_projeto, translateKey($rowData['nm_datatype'] . "_desc")));
            $this->insereDataypeParam($id_projeto, $rowData['id_primitive_type'], $id_to);
        }
    }

    function countForProjeto($id_projeto) {
        $id_projeto = validaNumero($id_projeto, "id_projeto countForProjeto EntidadeController");
        $id = "count_datatype_" . projetoAtual();
        if ($this->getCacheInfo($id))
            return $this->getCacheInfo($id);


        $rs = executaQuery("select count(*) as tot from datatype where id_projeto=?", array(projetoAtual()));
        if ($row = $rs->fetch()) {
            $this->setCacheInfo($id, $row['tot']);
            return $this->getCacheInfo($id);
        }
    }

    function insereDataypeParam($id_projeto, $id_primitive_type, $id_to) {
        $sql = "select dp.* from datatype_param dp,primitive_type_param p where p.id_primitive_type_param=dp.id_primitive_type_param and p.id_primitive_type=?";
        $rs = executaQuery($sql, array($id_primitive_type));
        while ($rowData = $rs->fetch()) {
            $id_to = executaSQL("insert into datatype_param (id_datatype,id_primitive_type_param,valor_param) values (?,?,?)", array($id_to, $rowData['id_primitive_type_param'], $rowData['valor_param']));
        }
    }

    function getProjeto($id) {
        $id = validaNumero($id, "id getProjeto DatatypeController");
        $row = executaQuerySingleRow("select id_projeto from datatype c where c.id_datatype=?", array($id));
        return $row['id_projeto'];
    }

    function getOrderSelect() {
        return "id_primitive_type=" . PRIMITIVE_ENTITY . "," . parent::getOrderSelect();
    }

}

?>