<?

class ColunaController extends BaseController {

    private $orderBy;

    function excluirRegistro($id_coluna) {

        $id_coluna = validaNumero($id_coluna, "excluiregistro idcol");
        validaEscrita("coluna", $id_coluna);

        if (isUserAdminProjeto($this->getProjeto($id_coluna))) {
            executaSQL("delete from coluna where id_coluna=?", array($id_coluna));
        }
    }

    //usado para determinar o pk de uma entidade... só retorna se tiver 1 pk
    function calcPKForEntidade($id_entidade) {//coberto
        $this->loadRegistros("and id_entidade_pai=$id_entidade and id_relacao_datatype=" . RELACAO_DATATYPE_PK);
        $c = 0;
        while ($model = $this->next()) {
            $campo = $model->getValorCampo("id_coluna");
            $c++;
        }
        if ($c == 1) {
            return $campo;
        }
    }

    //retorna um resultset com os dados das coluna da entidade informada
    function getRSColunas($id_entidade) {
        $sql = "select * " .
                " from coluna c,datatype d,primitive_type pt,sql_coluna s " .
                " where c.id_entidade_pai=? and c.id_sql_coluna=s.id_sql_coluna " .
                " and c.id_datatype=d.id_datatype " .
                " and d.id_primitive_type=pt.id_primitive_type " .
                " order by c.id_relacao_datatype,c.dbname_coluna";
        $rs = executaQuery($sql, array($id_entidade));
        return $rs;
    }

    function filtrosExtras() {
        if (entidadeAtual()) {
            return " and id_entidade_pai=" . entidadeAtual();
        }
    }

    function isAutoIncrement($id_coluna, $id_projeto, $id_primitive_type) {
        //if (($id_primitive_type == PRIMITIVE_INT) && ($id_tipo_banco == TIPO_BANCO_MYSQL) && ($id_datatype != 24)) {
        if ($id_primitive_type != PRIMITIVE_INT)
            return false;

        $id_tipo_banco = getControllerForTable("projeto")->getFieldFromModel($id_projeto, "id_tipo_banco");
        $flagAutoInc = getControllerForTable("tipo_banco")->isAutoIncrementavel($id_tipo_banco);
        if (!$flagAutoInc)
            return false;

        if (countQuery("select * from coluna c,datatype d,primitive_type pt where c.id_datatype=d.id_datatype and pt.id_primitive_type=d.id_primitive_type and c.id_coluna=?", array($id_coluna)) > 1)
            return false;

        return true;
    }

    function verificaDados() {
        executaSQL("update coluna set id_relacao_datatype=2 where id_relacao_datatype is null or id_relacao_datatype<=0");
        //executaSQL("update coluna set id_primitive_type=2 where id_primitive_type is null or id_primitive_type<=0");
    }

    function insereRegistro($arr) {//coberto
        $arr['entidade'] = validaNumero($arr['entidade'], "id_entidade_pai ins.reg contr_coluna");

        validaEscrita("entidade", $arr['entidade']);

        $row = executaQuerySingleRow("select id_coluna from coluna where id_entidade_pai=? and nm_coluna=?", array($arr['entidade'], $arr['nm_coluna']));
        if ($row) {
            return $row['id_coluna'];
        }
        unset($arr['projeto']);
        $sql = "insert into coluna (id_entidade_pai,nm_coluna,dbname_coluna,prop_name_coluna,id_entidade_combo,id_sql_coluna,id_acesso_coluna,id_datatype,id_relacao_datatype) values ";
        $sql.="(:entidade,:nm_coluna,:nm_coluna,:nm_coluna,:id_entidade_combo,1,4,:id_datatype,:id_relacao_datatype)";

        unset($arr["projeto"]);
        $id_coluna = executaSQL($sql, $arr);
        $this->verificaDados();
        return $id_coluna;
    }

    function getProjeto($id_coluna) {//coberto
        $id = "col_proj_$id_coluna";
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }

        $id_coluna = validaNumero($id_coluna, "id_coluna getProjeto contr_coluna");
        $row = executaQuerySingleRow("select id_projeto from entidade e,coluna c where e.id_entidade=c.id_entidade_pai and c.id_coluna=?", array($id_coluna));
        $this->setCacheInfo($id, $row['id_projeto']);
        return $row['id_projeto'];
    }

    function getEntidade($id_coluna) {//coberto
        $id = "col_ent_$id_coluna";
        if ($this->getCacheInfo($id)) {
            return $this->getCacheInfo($id);
        }
        $id_coluna = validaNumero($id_coluna, "id_coluna getEntidade contr_coluna");
        $row = executaQuerySingleRow("select id_entidade_pai from coluna c where c.id_coluna=?", array($id_coluna));
        $this->setCacheInfo($id, $row['id_entidade_pai']);
        return $row['id_entidade_pai'];
    }

    function getIdColunaComNome($campo, $id_entidade) {//coberto
        $campo = validaTexto($campo, "campo getEntidade contr_coluna");
        $id_entidade = validaNumero($id_entidade, "id_entidade getEntidade contr_coluna");
        $row = executaQuerySingleRow("select id_coluna from coluna c where c.dbname_coluna=? and id_entidade_pai=?", array($campo, $id_entidade));
        return $row['id_coluna'];
    }

    function getOrderBy() {
        if (isset($this->orderBy)) {
            return $this->orderBy;
        }
        return "seq_coluna,id_relacao_datatype,nm_coluna";
    }

    function setOrderBy($o) {
        $this->orderBy = $o;
    }

}

?>