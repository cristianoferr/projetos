<?

class ValorController extends BaseController {

    function getRSValores($id_entidade) {

        $sql = "select * from valor v,coluna c,registro r,datatype d,relacao_datatype rd,primitive_type pt,sql_coluna sc " . PHP_EOL;
        $sql .= " where d.id_datatype=c.id_datatype and r.id_entidade_pai=?" . PHP_EOL;
        $sql .= " and v.id_coluna_pai=c.id_coluna" . PHP_EOL;
        $sql .= " and c.id_sql_coluna=sc.id_sql_coluna" . PHP_EOL;
        $sql .= " and c.id_relacao_datatype=rd.id_relacao_datatype and d.id_primitive_type=pt.id_primitive_type" . PHP_EOL;
        $sql .= " and v.id_registro=r.id_registro" . PHP_EOL;
        $sql .= " order by r.id_registro,c.seq_coluna" . PHP_EOL;

        writeDebug("id_entidade:$id_entidade");

        $rs = executaQuery($sql, array($id_entidade));
        return $rs;
    }

    function verificaVazios($registro) {
        if (!$registro) {
            erroFatal("Empty recordset");
        }

        $valores = $registro->getValores();
        for ($c = 0; $c < sizeOf($valores); $c++) {
            $valor = $valores[$c];
            $vid = $valor->getId();
            if (!isset($vid)) {
                $coluna = $valor->getColuna();
                $id_registro = $registro->getId();
                $id = $this->preencheCelula($id_registro, $coluna->getId());
                writeDebug("preenchendo celula vazia: v:$id c:" . $coluna->getId());
                $valor->setId($id);
            }
        }
    }

    function preencheCelula($id_registro, $id_coluna, $valor = null) {
        $sql = "insert into valor (id_valor,id_registro,id_coluna_pai,valor_coluna) values (?,?,?,?)";
        $id_valor = executaSQL($sql, array($id_valor, $id_registro, $id_coluna, $valor));
        return $id_valor;
    }

    function atualizaCelula($id_registro, $id_coluna, $valor = null) {//coberto
        $sql = "update valor set valor_coluna=? where id_registro=? and id_coluna_pai=?";
        executaSQL($sql, array($valor, $id_registro, $id_coluna));
    }

    function getValorCelula($id_registro, $id_coluna) {//coberto
        $row = executaQuerySingleRow("select valor_coluna from valor where id_registro=? and id_coluna_pai=?", array($id_registro, $id_coluna));
        return $row['valor_coluna'];
    }

    function insereColunaNoRegistro($coluna, $registro) {
        $valor = $coluna->getValorInicial($registro);
        $this->preencheCelula($registro->getId(), $coluna->getId(), $valor);
    }

}

?>